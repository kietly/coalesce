package com.incadencecorp.coalesce.framework.persistance.accumulo;

import com.incadencecorp.coalesce.api.CoalesceErrors;
import com.incadencecorp.coalesce.api.persistance.EPersistorCapabilities;
import com.incadencecorp.coalesce.common.exceptions.CoalesceDataFormatException;
import com.incadencecorp.coalesce.common.exceptions.CoalesceException;
import com.incadencecorp.coalesce.common.exceptions.CoalescePersistorException;
import com.incadencecorp.coalesce.common.helpers.JodaDateTimeHelper;
import com.incadencecorp.coalesce.framework.datamodel.*;
import com.incadencecorp.coalesce.framework.persistance.*;
import com.incadencecorp.coalesce.search.api.ICoalesceSearchPersistor;
import com.incadencecorp.coalesce.search.api.SearchResults;
import com.incadencecorp.coalesce.search.resultset.CoalesceColumnMetadata;
import com.incadencecorp.coalesce.search.resultset.CoalesceResultSet;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import com.vividsolutions.jts.util.GeometricShapeFactory;
import org.apache.accumulo.core.client.*;
import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Mutation;
import org.apache.accumulo.core.data.Range;
import org.apache.accumulo.core.data.Value;
import org.apache.accumulo.core.iterators.SortedKeyValueIterator;
import org.apache.accumulo.core.iterators.user.RegExFilter;
import org.apache.accumulo.core.iterators.user.WholeRowIterator;
import org.apache.accumulo.core.security.Authorizations;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.io.Text;
import org.geotools.data.*;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.Capabilities;
import org.geotools.filter.text.cql2.CQLException;
import org.joda.time.DateTime;
import org.opengis.feature.Feature;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.identity.FeatureId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

// import org.apache.accumulo.core.client.Durability; // Accumulo 1.7 depenency

/*-----------------------------------------------------------------------------'
Copyright 2014 - InCadence Strategic Solutions Inc., All Rights Reserved

Notwithstanding any contractor copyright notice, the Government has Unlimited
Rights in this work as defined by DFARS 252.227-7013 and 252.227-7014.  Use
of this work other than as specifically authorized by these DFARS Clauses may
violate Government rights in this work.

DFARS Clause reference: 252.227-7013 (a)(16) and 252.227-7014 (a)(16)
Unlimited Rights. The Government has the right to use, modify, reproduce,
perform, display, release or disclose this computer software and to have or
authorize others to do so.

Distribution Statement D. Distribution authorized to the Department of
Defense and U.S. DoD contractors only in support of U.S. DoD efforts.
-----------------------------------------------------------------------------*/

/**
 * @author Dave Boyd May 13, 2016
 * @see AccumuloPersistor2
 * @deprecated
 */
public class AccumuloPersistor extends CoalescePersistorBase implements ICoalesceSearchPersistor {

    public static final String ENTITY_KEY_COLUMN_NAME = "objectkey";
    public static final String ENTITY_NAME_COLUMN_NAME = "name";
    public static final String ENTITY_SOURCE_COLUMN_NAME = "source";
    public static final String ENTITY_TITLE_COLUMN_NAME = "title";
    public static final String ENTITY_RECORD_KEY_COLUMN_NAME = "recordkey";
    public static final String ENTITY_VERSION_COLUMN_NAME = "version";
    public static final String ENTITY_ID_COLUMN_NAME = "entityId";
    public static final String ENTITY_ID_TYPE_COLUMN_NAME = "entityIdType";
    public static final String ENTITY_DATE_CREATED_COLUMN_NAME = "dateCreated";
    public static final String ENTITY_LAST_MODIFIED_COLUMN_NAME = "lastModified";
    public static final String ENTITY_DELETED_COLUMN_NAME = "deleted";
    public static final String ENTITY_SCOPE_COLUMN_NAME = "scope";
    public static final String ENTITY_CREATOR_COLUMN_NAME = "creator";
    public static final String ENTITY_TYPE_COLUMN_NAME = "type";

    // Some constants for the linkage records
    public static final String LINKAGE_ENTITY1_KEY_COLUMN_NAME = "entity1Key";
    public static final String LINKAGE_ENTITY1_NAME_COLUMN_NAME = "entity1Name";
    public static final String LINKAGE_ENTITY1_SOURCE_COLUMN_NAME = "entity1Source";
    public static final String LINKAGE_ENTITY1_VERSION_COLUMN_NAME = "entity1Version";
    public static final String LINKAGE_ENTITY2_KEY_COLUMN_NAME = "entity2Key";
    public static final String LINKAGE_ENTITY2_NAME_COLUMN_NAME = "entity2Name";
    public static final String LINKAGE_ENTITY2_SOURCE_COLUMN_NAME = "entity2Source";
    public static final String LINKAGE_ENTITY2_VERSION_COLUMN_NAME = "entity2Version";
    public static final String LINKAGE_KEY_COLUMN_NAME = "objectKey";
    public static final String LINKAGE_LAST_MODIFIED_COLUMN_NAME = "lastModified";
    public static final String LINKAGE_LINK_TYPE_COLUMN_NAME = "linkType";
    public static final String LINKAGE_LABEL_COLUMN_NAME = "label";

    public static final String DEFAULT_GEO_FIELD_NAME = "theWorld";

    //  Note, these strings are here to match the CoalescePropertyFactory names
    //  If those change this has to change
    public static final String LINKAGE_FEATURE_NAME = "coalescelinkage";
    public static final String ENTITY_FEATURE_NAME = "coalesceentity";

    private static final Logger LOGGER = LoggerFactory.getLogger(AccumuloPersistor.class);

    private final AccumuloDataConnector connect;

    private boolean batchInProgress = false;

    /*--------------------------------------------------------------------------
    Constructor / Initializers
    --------------------------------------------------------------------------*/

    public AccumuloPersistor(ServerConn svConn) throws CoalescePersistorException
    {
        setConnectionSettings(svConn);
        LOGGER.debug("Zookeepers: {} ", AccumuloSettings.getServerConn().getServerName());
        LOGGER.debug("Databasename: {} ", AccumuloSettings.getServerConn().getDatabase());
        connect = (AccumuloDataConnector) getDataConnector();
        createEntityFeature(ENTITY_FEATURE_NAME);
        createLinkageFeature(LINKAGE_FEATURE_NAME);

        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run()
            {
                boolean inLoop = false;
                LOGGER.debug("Shutdown Hook Invoked");
                while (batchInProgress)
                {
                    if (!inLoop)
                        LOGGER.debug("Batch IO in progress waiting");
                    inLoop = true;
                    try
                    {
                        sleep(500);
                    }
                    catch (InterruptedException e)
                    {
                        // Can't count on Log4J not terminating
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    public AccumuloPersistor() throws CoalescePersistorException
    {
        this(AccumuloSettings.getServerConn());

    }

    public AccumuloPersistor(ICoalesceCacher cacher, ServerConn svConn) throws CoalescePersistorException
    {
        this(svConn);

        setCacher(cacher);

    }

    /**
     * @return EnumSet of EPersistorCapabilities
     */
    @Override
    public EnumSet<EPersistorCapabilities> getCapabilities()
    {
        EnumSet<EPersistorCapabilities> enumSet = EnumSet.of(EPersistorCapabilities.CREATE,
                                                             EPersistorCapabilities.READ,
                                                             //EPersistorCapabilities.GET_FIELD_VALUE,
                                                             EPersistorCapabilities.DELETE,
                                                             EPersistorCapabilities.UPDATE,
                                                             EPersistorCapabilities.GEOSPATIAL_SEARCH,
                                                             EPersistorCapabilities.SEARCH,
                                                             EPersistorCapabilities.READ_TEMPLATES);
        return enumSet;
    }

    @Override
    public String[] getEntityXml(String... keys) throws CoalescePersistorException
    {
        List<String> results = new ArrayList<String>();
        ArrayList<Range> ranges = new ArrayList<Range>();
        for (String key : keys)
        {
            ranges.add(new Range(key));
        }

        try (CloseableBatchScanner scanner = new CloseableBatchScanner(connect.getDBConnector(),
                                                                       AccumuloDataConnector.COALESCE_ENTITY_TABLE,
                                                                       Authorizations.EMPTY,
                                                                       4))
        {
            scanner.setRanges(ranges);
            IteratorSetting iter = new IteratorSetting(1, "modifiedFilter", RegExFilter.class);
            RegExFilter.setRegexs(iter, null, "entity:*", "entityxml", null, false, true);
            scanner.addScanIterator(iter);

            for (Map.Entry<Key, Value> e : scanner)
            {
                String xml = new String(e.getValue().get());
                results.add(xml);
            }
            scanner.close();
        }
        catch (TableNotFoundException ex)
        {
            LOGGER.error(ex.getLocalizedMessage(), ex);
        }
        return results != null ? results.toArray(new String[results.size()]) : null;
    }

    public String getEntityXml(String entityId, String entityIdType) throws CoalescePersistorException
    {
        // Use are sharded term index to find the merged keys
        String xml = null;
        String indexcf = entityIdType + "\0" + entityId + ".*";
        Connector dbConnector = connect.getDBConnector();
        try (CloseableScanner scanner = new CloseableScanner(dbConnector,
                                                             AccumuloDataConnector.COALESCE_ENTITY_IDX_TABLE,
                                                             Authorizations.EMPTY))
        {
            // Set up an IntersectingIterator for the values
            IteratorSetting iter = new IteratorSetting(1, "cfmatch", RegExFilter.class);
            RegExFilter.setRegexs(iter, null, indexcf, null, null, false, true);
            scanner.addScanIterator(iter);
            // Just return the first entry
            if (scanner.iterator().hasNext())
            {
                Key rowKey = scanner.iterator().next().getKey();
                String key = rowKey.getRow().toString();
                Text cf = new Text("entity:" + rowKey.getColumnQualifier().toString());
                try (CloseableScanner xmlscanner = new CloseableScanner(dbConnector,
                                                                        AccumuloDataConnector.COALESCE_ENTITY_TABLE,
                                                                        Authorizations.EMPTY))
                {
                    xmlscanner.setRange(new Range(key));
                    xmlscanner.fetchColumn(cf, new Text("entityxml"));
                    if (xmlscanner.iterator().hasNext())
                    {
                        xml = xmlscanner.iterator().next().getValue().toString();
                    }
                    xmlscanner.close();
                }

            }
            scanner.close();
        }
        catch (TableNotFoundException ex)
        {
            LOGGER.error(ex.getLocalizedMessage(), ex);
        }
        return xml;
    }

    public String getEntityXml(String name, String entityId, String entityIdType) throws CoalescePersistorException
    {
        // Use are EWntityIndex to find the merged keys
        String indexcf = entityIdType + "\0" + entityId + "\0" + name + ".*";
        String xml = null;
        Connector dbConnector = connect.getDBConnector();
        try (CloseableScanner keyscanner = new CloseableScanner(dbConnector,
                                                                AccumuloDataConnector.COALESCE_ENTITY_IDX_TABLE,
                                                                Authorizations.EMPTY))
        {
            // Set up an IntersectingIterator for the values
            IteratorSetting iter = new IteratorSetting(1, "cfmatch", RegExFilter.class);
            RegExFilter.setRegexs(iter, null, indexcf, null, null, false, true);
            keyscanner.addScanIterator(iter);
            // Just return the first entry
            if (keyscanner.iterator().hasNext())
            {
                Key rowKey = keyscanner.iterator().next().getKey();
                String key = rowKey.getRow().toString();
                Text cf = new Text("entity:" + rowKey.getColumnQualifier().toString());
                try (CloseableScanner xmlscanner = new CloseableScanner(dbConnector,
                                                                        AccumuloDataConnector.COALESCE_ENTITY_TABLE,
                                                                        Authorizations.EMPTY))
                {
                    xmlscanner.setRange(new Range(key));
                    xmlscanner.fetchColumn(cf, new Text("entityxml"));
                    if (xmlscanner.iterator().hasNext())
                    {
                        xml = xmlscanner.iterator().next().getValue().toString();
                    }
                    xmlscanner.close();
                }
            }
            keyscanner.close();
        }
        catch (TableNotFoundException ex)
        {
            LOGGER.error(ex.getLocalizedMessage(), ex);
        }
        return xml;
    }

    public Object getFieldValue(String fieldKey) throws CoalescePersistorException
    {
        // TODO We will want to create a a table of just field values by key for
        // now we will scan the main table
        Object value = null;
        Connector dbConnector = connect.getDBConnector();
        try (CloseableScanner keyscanner = new CloseableScanner(dbConnector,
                                                                AccumuloDataConnector.COALESCE_ENTITY_TABLE,
                                                                Authorizations.EMPTY))
        {
            // Set up an RegEx Iterator to get the row with a field with the key
            IteratorSetting iter = new IteratorSetting(20, "fieldkeymatch", RegExFilter.class);
            // Only get rows for fields that hold key values and match the key
            RegExFilter.setRegexs(iter, null, "field.*", "key", fieldKey, false, true);
            keyscanner.addScanIterator(iter);

            // Just return the first entry
            if (keyscanner.iterator().hasNext())
            {
                Key rowKey = keyscanner.iterator().next().getKey();
                String key = rowKey.getRow().toString();
                Text cf = rowKey.getColumnFamily();
                try (CloseableScanner valuescanner = new CloseableScanner(dbConnector,
                                                                          AccumuloDataConnector.COALESCE_ENTITY_TABLE,
                                                                          Authorizations.EMPTY))
                {
                    valuescanner.setRange(new Range(key));
                    valuescanner.fetchColumn(cf, new Text("value"));
                    if (valuescanner.iterator().hasNext())
                    {
                        value = valuescanner.iterator().next().getValue().toString();
                    }
                    valuescanner.close();
                }
            }
            keyscanner.close();
        }
        catch (TableNotFoundException ex)
        {
            LOGGER.error(ex.getLocalizedMessage(), ex);
        }
        return value;

    }

    public ElementMetaData getXPath(String key, String objectType) throws CoalescePersistorException
    {
        Connector dbConnector = connect.getDBConnector();

        String xpath = null;
        String entityKey = null;
        try (CloseableScanner keyscanner = new CloseableScanner(dbConnector,
                                                                AccumuloDataConnector.COALESCE_ENTITY_TABLE,
                                                                Authorizations.EMPTY))
        {
            // Set up an RegEx Iterator to get the row with a field with the key
            IteratorSetting iter = new IteratorSetting(20, "objectkeymatch", RegExFilter.class);
            // Only get rows for fields that hold key values and match the key
            RegExFilter.setRegexs(iter, null, objectType + ".*", "key", key, false, true);
            keyscanner.addScanIterator(iter);
            // Just return the first entry
            if (keyscanner.iterator().hasNext())
            {
                Key rowKey = keyscanner.iterator().next().getKey();
                // The plus one is to also eat up the colon between the
                // objecttype and the namepath
                xpath = rowKey.getColumnFamily().toString().substring(objectType.length() + 1);
                entityKey = rowKey.getRow().toString();
            }
            keyscanner.close();
        }
        catch (TableNotFoundException ex)
        {
            LOGGER.error(ex.getLocalizedMessage(), ex);
        }
        return entityKey != null && xpath != null ? new ElementMetaData(entityKey, xpath) : null;
    }

    public List<String> getCoalesceEntityKeysForEntityId(String entityId,
                                                         String entityIdType,
                                                         String entityName,
                                                         String entitySource) throws CoalescePersistorException
    {
        // Use are EntityIndex to find the merged keys
        ArrayList<String> keys = new ArrayList<String>();
        // NOTE - Source can be null meaning find all sources so construct the
        // string carefully

        String indexcf = entityIdType + "\0" + entityId + "\0" + entityName;
        indexcf = indexcf.concat(entitySource != null ? "\0" + entitySource : ".*");
        Connector dbConnector = connect.getDBConnector();
        try (CloseableScanner keyscanner = new CloseableScanner(dbConnector,
                                                                AccumuloDataConnector.COALESCE_ENTITY_IDX_TABLE,
                                                                Authorizations.EMPTY))
        {
            // Set up an RegEx Iterator to get the row with a field with the key
            IteratorSetting iter = new IteratorSetting(20, "cfmatch", RegExFilter.class);
            // Only get rows for fields that hold key values and match the key
            RegExFilter.setRegexs(iter, null, indexcf, null, null, false, true);
            keyscanner.addScanIterator(iter);

            // Return the list of keys
            for (Entry<Key, Value> entry : keyscanner)
            {
                keys.add(entry.getKey().getRow().toString());
            }
            keyscanner.close();
        }
        catch (TableNotFoundException ex)
        {
            LOGGER.error(ex.getLocalizedMessage(), ex);
        }
        return keys;
    }

    public EntityMetaData getCoalesceEntityIdAndTypeForKey(String key) throws CoalescePersistorException
    {
        EntityMetaData metadata = null;
        Connector dbConnector = connect.getDBConnector();
        try (CloseableScanner keyscanner = new CloseableScanner(dbConnector,
                                                                AccumuloDataConnector.COALESCE_ENTITY_TABLE,
                                                                Authorizations.EMPTY))
        {
            // Set up an RegEx Iterator to get the row with a field with the key
            IteratorSetting iter = new IteratorSetting(20, "entitymatch", RegExFilter.class);
            // Only get rows for the entity with columnqualifiers for entityid,
            // entityidType
            RegExFilter.setRegexs(iter, null, "entity:.*", "(entityid)|(entityidtype)", null, false, true);
            keyscanner.addScanIterator(iter);
            keyscanner.setRange(Range.exact(key));

            // Now we should have two entries one for the entityid and one for
            // the entityidtype
            String entityid = null;
            String entityidtype = null;
            for (Entry<Key, Value> entry : keyscanner)
            {
                String cf = entry.getKey().getColumnQualifier().toString();
                String value = entry.getValue().toString();
                if (cf.equals("entityid"))
                {
                    entityid = new String(value);
                }
                if (cf.equals("entityidtype"))
                {
                    entityidtype = new String(value);
                }
            }
            if (entityid != null && entityidtype != null)
            {
                metadata = new EntityMetaData(entityid, entityidtype, key);
            }
            keyscanner.close();
        }
        catch (TableNotFoundException ex)
        {
            LOGGER.error(ex.getLocalizedMessage(), ex);
        }
        return metadata;
    }

    private static String coalesceTemplateColumnFamily = "Coalesce:Template";
    private static String coalesceTemplateNameQualifier = "name";
    private static String coalesceTemplateSourceQualifier = "source";
    private static String coalesceTemplateXMLQualifier = "xml";
    private static String coalesceTemplateVersionQualifier = "version";
    private static String coalesceTemplateDateModifiedQualifier = "lastmodified";
    private static String coalesceTemplateDateCreatedQualifier = "datecreated";

    @Override
    protected void saveTemplate(CoalesceDataConnectorBase conn, CoalesceEntityTemplate... templates)
            throws CoalescePersistorException
    {
        BatchWriter writer = null;

        for (CoalesceEntityTemplate template : templates)
        {
            try
            {
                Connector dbConnector = connect.getDBConnector();
                BatchWriterConfig config = new BatchWriterConfig();
                config.setMaxLatency(1, TimeUnit.SECONDS);
                config.setMaxMemory(10240);
                // config.setDurability(Durability.DEFAULT); // Requires
                // Accumulo
                // 1.7
                config.setMaxWriteThreads(10);
                /*
                 * SQL we are eumulating return conn.executeProcedure( "CoalesceEntityTemplate_InsertOrUpdate", new
                 * CoalesceParameter(UUID.randomUUID().toString(), Types.OTHER), new CoalesceParameter(template.getName()),
                 * new CoalesceParameter(template.getSource()), new CoalesceParameter(template.getVersion()), new
                 * CoalesceParameter(template.toXml()), new CoalesceParameter(JodaDateTimeHelper.nowInUtc().toString(),
                 * Types.OTHER), new CoalesceParameter(JodaDateTimeHelper.nowInUtc().toString(), Types.OTHER));
                 */
                String xml = template.toXml();
                String name = template.getName();
                String source = template.getSource();
                String version = template.getVersion();
                String time = JodaDateTimeHelper.nowInUtc().toString();
                // See if a template with this name, source, version exists
                String templateId = getEntityTemplateKey(name, source, version);
                boolean newtemplate = false;

                if (templateId == null)
                {
                    templateId = UUID.nameUUIDFromBytes((name + source + version).getBytes()).toString();
                    newtemplate = true;
                }
                template.setKey(templateId);
                writer = dbConnector.createBatchWriter(AccumuloDataConnector.COALESCE_TEMPLATE_TABLE, config);
                Mutation m = new Mutation(templateId);
                m.put(coalesceTemplateColumnFamily, coalesceTemplateXMLQualifier, new Value(xml.getBytes()));
                m.put(coalesceTemplateColumnFamily, coalesceTemplateDateModifiedQualifier, new Value(time.getBytes()));
                // Only update the name, source, version, created date if new.
                if (newtemplate)
                {
                    m.put(coalesceTemplateColumnFamily, coalesceTemplateNameQualifier, new Value(name.getBytes()));
                    m.put(coalesceTemplateColumnFamily, coalesceTemplateSourceQualifier, new Value(source.getBytes()));
                    m.put(coalesceTemplateColumnFamily, coalesceTemplateVersionQualifier, new Value(version.getBytes()));
                    m.put(coalesceTemplateColumnFamily, coalesceTemplateDateCreatedQualifier, new Value(time.getBytes()));
                    // Special Column Qualifier to so we can fetch key based on
                    // Name
                    // + Source + Version
                    m.put(coalesceTemplateColumnFamily,
                          template.getName() + template.getSource() + template.getVersion(),
                          new Value(templateId.getBytes()));
                }
                writer.addMutation(m);
                writer.flush();
                writer.close();
                writer = null;
                // Create the associated search features for this template if it
                // is
                // new
                // TODO: Figure out what to do for updates to templates. What to
                // do
                // with the data?
                if (newtemplate)
                {
                    createSearchTables(template);
                }
            }
            catch (CoalescePersistorException | MutationsRejectedException | TableNotFoundException ex)
            {
                LOGGER.error(ex.getLocalizedMessage(), ex);
            }
        }
        if (writer != null)
        { // make sure writer closed even if exception happened before
            try
            {
                writer.close();
                writer = null;
            }
            catch (MutationsRejectedException e)
            {
                LOGGER.error(e.getLocalizedMessage(), e);
            }
        }
    }

    @Override
    public void registerTemplate(CoalesceEntityTemplate... templates) throws CoalescePersistorException
    {
        for (CoalesceEntityTemplate template : templates)
        {
            createSearchTables(template);
        }
    }

    private boolean createSearchTables(CoalesceEntityTemplate template) throws CoalescePersistorException
    {
        // Feature set name will be the recordset name.
        // This is to match the postgres persistor
        //
        // All spaces in names will be converted to underscores.
        // String templateName = (template.getName() + "_" + template.getSource() + "_"
        // + template.getVersion()).replaceAll(" ", "_");

        // Document tempdoc = template.getCoalesceObjectDocument();
        // Confirm Values
        NodeList nodeList = template.getCoalesceObjectDocument().getElementsByTagName("*");
        int numnodes = nodeList.getLength();

        String recordName = null;
        ArrayList<Fielddefinition> fieldlist = new ArrayList<Fielddefinition>();

        // TODO - Deal with noindex sections and records.
        for (int jj = 0; jj < numnodes; jj++)
        {
            Node node = nodeList.item(jj);
            String nodeName = node.getNodeName();

            if (nodeName.compareTo("section") == 0)
            {
                if (!fieldlist.isEmpty())
                {
                    // Now create the geomesa featureset
                    createFeatureSet(recordName.replaceAll(" ", "_"), fieldlist);
                }
                recordName = null;
                fieldlist.clear();
            }

            if (nodeName.compareTo("recordset") == 0)
            {
                // If this is a new record write out the old one if it exists
                if (!fieldlist.isEmpty())
                {
                    // Now create the geomesa featureset
                    createFeatureSet(recordName.replaceAll(" ", "_"), fieldlist);
                }
                recordName = node.getAttributes().getNamedItem("name").getNodeValue().replaceAll(" ", "_");
                fieldlist.clear();
            }

            if (nodeName.compareTo("fielddefinition") == 0)
            {
                Node fieldNode = nodeList.item(jj);
                String datatype = fieldNode.getAttributes().getNamedItem("datatype").getNodeValue();
                String fieldname = fieldNode.getAttributes().getNamedItem("name").getNodeValue();
                Fielddefinition field = new Fielddefinition();

                field.setDatatype(datatype);
                field.setName(fieldname.replaceAll(" ", "_"));
                fieldlist.add(field);
            }

        }
        // Write out last set of fields
        if (!fieldlist.isEmpty())
        {
            createFeatureSet(recordName, fieldlist);
        }

        return true;
    }

    private void createLinkageFeature(String featurename)
    {
        final String geomesaTimeIndex = "geomesa.index.dtg";
        final String indexes = "records,attr";
        DataStore gs = connect.getGeoDataStore();
        try
        {
            SimpleFeatureType linkschema = gs.getSchema(featurename);
            if (linkschema != null)
                return;
        }
        catch (IOException e1)
        {
            LOGGER.error(e1.getMessage(), e1);

        }
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName(featurename);

        tb.add(LINKAGE_KEY_COLUMN_NAME, getTypeForSimpleFeature(ECoalesceFieldDataTypes.STRING_TYPE));
        tb.add(LINKAGE_ENTITY1_KEY_COLUMN_NAME, getTypeForSimpleFeature(ECoalesceFieldDataTypes.STRING_TYPE));
        tb.add(LINKAGE_ENTITY1_NAME_COLUMN_NAME, getTypeForSimpleFeature(ECoalesceFieldDataTypes.STRING_TYPE));
        tb.add(LINKAGE_ENTITY1_SOURCE_COLUMN_NAME, getTypeForSimpleFeature(ECoalesceFieldDataTypes.STRING_TYPE));
        tb.add(LINKAGE_ENTITY1_VERSION_COLUMN_NAME, getTypeForSimpleFeature(ECoalesceFieldDataTypes.STRING_TYPE));
        tb.add(LINKAGE_ENTITY2_KEY_COLUMN_NAME, getTypeForSimpleFeature(ECoalesceFieldDataTypes.STRING_TYPE));
        tb.add(LINKAGE_ENTITY2_NAME_COLUMN_NAME, getTypeForSimpleFeature(ECoalesceFieldDataTypes.STRING_TYPE));
        tb.add(LINKAGE_ENTITY2_SOURCE_COLUMN_NAME, getTypeForSimpleFeature(ECoalesceFieldDataTypes.STRING_TYPE));
        tb.add(LINKAGE_ENTITY2_VERSION_COLUMN_NAME, getTypeForSimpleFeature(ECoalesceFieldDataTypes.STRING_TYPE));
        tb.add(LINKAGE_LAST_MODIFIED_COLUMN_NAME, getTypeForSimpleFeature(ECoalesceFieldDataTypes.DATE_TIME_TYPE));
        tb.add(LINKAGE_LABEL_COLUMN_NAME, getTypeForSimpleFeature(ECoalesceFieldDataTypes.STRING_TYPE));
        tb.add(LINKAGE_LINK_TYPE_COLUMN_NAME, getTypeForSimpleFeature(ECoalesceFieldDataTypes.ENUMERATION_TYPE));
        //tb.add(DEFAULT_GEO_FIELD_NAME, Polygon.class);
        //tb.setDefaultGeometry(DEFAULT_GEO_FIELD_NAME);

        SimpleFeatureType feature = tb.buildFeatureType();
        feature.getUserData().put(geomesaTimeIndex, LINKAGE_LAST_MODIFIED_COLUMN_NAME);

        // index recordkey, cardinality is high because there is only one record per key.
        //feature.getDescriptor(LINKAGE_KEY_COLUMN_NAME).getUserData().put("index", "full");
        //feature.getDescriptor(LINKAGE_KEY_COLUMN_NAME).getUserData().put("cardinality", "high");
        feature.getDescriptor(LINKAGE_ENTITY1_KEY_COLUMN_NAME).getUserData().put("index", "join");
        feature.getDescriptor(LINKAGE_ENTITY1_KEY_COLUMN_NAME).getUserData().put("cardinality", "high");
        feature.getDescriptor(LINKAGE_ENTITY2_KEY_COLUMN_NAME).getUserData().put("index", "join");
        feature.getDescriptor(LINKAGE_ENTITY2_KEY_COLUMN_NAME).getUserData().put("cardinality", "high");
        //feature.getDescriptor(LINKAGE_LAST_MODIFIED_COLUMN_NAME).getUserData().put("index", "join");
        //feature.getDescriptor(LINKAGE_LAST_MODIFIED_COLUMN_NAME).getUserData().put("cardinality", "high");
        feature.getDescriptor(LINKAGE_LABEL_COLUMN_NAME).getUserData().put("index", "join");
        feature.getDescriptor(LINKAGE_LABEL_COLUMN_NAME).getUserData().put("cardinality", "low");
        feature.getDescriptor(LINKAGE_LINK_TYPE_COLUMN_NAME).getUserData().put("index", "join");
        feature.getDescriptor(LINKAGE_LINK_TYPE_COLUMN_NAME).getUserData().put("cardinality", "low");
        feature.getUserData().put(Hints.USE_PROVIDED_FID, true);
        feature.getUserData().put("geomesa.indexes.enabled", indexes);

        try
        {
            LOGGER.debug("Creating Feature for {} ", featurename);

            gs.createSchema(feature);

        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void createFeatureSet(String featurename, ArrayList<Fielddefinition> fields)
    {
        //final String geomesaTimeIndex = "geomesa.index.dtg";
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();

        boolean defaultGeometrySet = false;
        //boolean defaultTimeSet = false;
        //String timeField = null;
        String geomField = null;

        tb.setName(featurename);

        // TODO - Deal with no index fields
        tb.add(ENTITY_KEY_COLUMN_NAME, getTypeForSimpleFeature(ECoalesceFieldDataTypes.STRING_TYPE));
        tb.add(ENTITY_NAME_COLUMN_NAME, getTypeForSimpleFeature(ECoalesceFieldDataTypes.STRING_TYPE));
        tb.add(ENTITY_SOURCE_COLUMN_NAME, getTypeForSimpleFeature(ECoalesceFieldDataTypes.STRING_TYPE));
        tb.add(ENTITY_TITLE_COLUMN_NAME, getTypeForSimpleFeature(ECoalesceFieldDataTypes.STRING_TYPE));
        tb.add(ENTITY_RECORD_KEY_COLUMN_NAME, getTypeForSimpleFeature(ECoalesceFieldDataTypes.STRING_TYPE));

        for (Fielddefinition field : fields)
        {
            String datatype = field.getDatatype();
            ECoalesceFieldDataTypes type = ECoalesceFieldDataTypes.getTypeForCoalesceType(datatype);
            Class<?> featuretype = getTypeForSimpleFeature(type);

            // Binary and File fields will return null so we do not persist them
            // for search
            if (featuretype != null)
            {
                tb.add(field.getName(), featuretype);
                // Index on the first geometry type in the recordset.
                if (!defaultGeometrySet && Geometry.class.isAssignableFrom(featuretype))
                {
                    defaultGeometrySet = true;
                    geomField = field.getName();
                    tb.setDefaultGeometry(geomField);
                }
                // Turn of Z3 time indexing due to not allowing dates earlier than the EPOC.

                //                if (!defaultTimeSet && Date.class.isAssignableFrom(featuretype))
                //                {
                //                    defaultTimeSet = true;
                //                    timeField = field.getName();
                //                }
            }
        }

        if (defaultGeometrySet == false)
        {
            LOGGER.debug("Creating theWorld for {} ", featurename);
            tb.add(DEFAULT_GEO_FIELD_NAME, Polygon.class);
            tb.setDefaultGeometry(DEFAULT_GEO_FIELD_NAME);
            geomField = DEFAULT_GEO_FIELD_NAME;
        }

        SimpleFeatureType feature = tb.buildFeatureType();

        // index recordkey, cardinality is high because there is only one record per key.
        feature.getDescriptor(ENTITY_RECORD_KEY_COLUMN_NAME).getUserData().put("index", "full");
        feature.getDescriptor(ENTITY_RECORD_KEY_COLUMN_NAME).getUserData().put("cardinality", "high");
        // Also index the entity key
        feature.getDescriptor(ENTITY_KEY_COLUMN_NAME).getUserData().put("index", "full");
        feature.getDescriptor(ENTITY_KEY_COLUMN_NAME).getUserData().put("cardinality", "high");
        feature.getUserData().put(Hints.USE_PROVIDED_FID, true);
        //        feature.getUserData().put("geomesa.indexes.enabled",indexes);
        feature.getUserData().put("geomesa.index.dtg", null);

        // Turn of Z3 time indexing due to not allowing dates earlier than the EPOC.
        //        if (null != timeField)
        //        {
        //            feature.getUserData().put(geomesaTimeIndex, timeField);
        //        }

        try
        {
            LOGGER.debug("Creating Feature for {} with fields lenght {}", featurename, fields.size());

            connect.getGeoDataStore().createSchema(feature);
            SimpleFeatureType schema = connect.getGeoDataStore().getSchema(featurename);
            schema.getUserData().put("geomesa.index.dtg", null);
        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private static Class<?> getTypeForSimpleFeature(final ECoalesceFieldDataTypes type)
    {

        switch (type)
        {

        case BOOLEAN_TYPE:
            return Boolean.class;

        case DOUBLE_TYPE:
        case FLOAT_TYPE:
            return Double.class;

        case GEOCOORDINATE_LIST_TYPE:
            return MultiPoint.class;

        case GEOCOORDINATE_TYPE:
            return Point.class;

        case LINE_STRING_TYPE:
            return LineString.class;

        case POLYGON_TYPE:
            return Polygon.class;
        // Circles will be converted to polygons
        case CIRCLE_TYPE:
            return Polygon.class;

        case INTEGER_TYPE:
            return Integer.class;

        case STRING_TYPE:
        case URI_TYPE:
        case STRING_LIST_TYPE:
        case DOUBLE_LIST_TYPE:
        case INTEGER_LIST_TYPE:
        case LONG_LIST_TYPE:
        case FLOAT_LIST_TYPE:
        case GUID_LIST_TYPE:
        case BOOLEAN_LIST_TYPE:
            return String.class;

        case GUID_TYPE:
            return String.class;

        case DATE_TIME_TYPE:
            return Date.class;

        case LONG_TYPE:
            return Long.class;

        case ENUMERATION_TYPE:
            return String.class;

        case FILE_TYPE:
        case BINARY_TYPE:
        default:
            return null;
        }
    }

    @Override
    public CoalesceEntityTemplate getEntityTemplate(String key) throws CoalescePersistorException
    {
        if (key == null)
        {
            throw new CoalescePersistorException(String.format(CoalesceErrors.NOT_FOUND, "Template", key));
        }

        Range range = new Range(key);
        CoalesceEntityTemplate template = null;
        Connector dbConnector = connect.getDBConnector();
        //        try (Scanner keyscanner = dbConnector.createScanner(AccumuloDataConnector.coalesceTemplateTable, Authorizations.EMPTY))
        try (CloseableScanner keyscanner = new CloseableScanner(dbConnector,
                                                                AccumuloDataConnector.COALESCE_TEMPLATE_TABLE,
                                                                Authorizations.EMPTY))
        {
            String xml = null;

            keyscanner.setRange(range);
            keyscanner.fetchColumn(new Text(coalesceTemplateColumnFamily), new Text(coalesceTemplateXMLQualifier));

            // TODO Add error handling if more than one row returned.
            if (keyscanner.iterator().hasNext())
            {
                xml = keyscanner.iterator().next().getValue().toString();
            }
            keyscanner.close();

            if (xml == null)
            {
                throw new CoalescePersistorException(String.format(CoalesceErrors.NOT_FOUND, "Template", key));
            }

            template = CoalesceEntityTemplate.create(xml);
        }
        catch (TableNotFoundException | CoalesceException ex)
        {
            throw new CoalescePersistorException(String.format(CoalesceErrors.NOT_FOUND, "Template", key), ex);
        }

        return template;
    }

    /**
     * Temporary Accumulo Persistor-specific workaround to the fact that the framework does not have the ability to delete
     * templates. Send in a Coalesce Entity and template will be deleted for that entity
     *
     * @return boolean did it work or not
     */
    public boolean deleteEntityTemplate(String entityTemplateKey, Connector connect, BatchWriterConfig config)
    {
        boolean persisted = false;
        try
        {
            BatchDeleter bd = connect.createBatchDeleter(AccumuloDataConnector.COALESCE_TEMPLATE_TABLE,
                                                         Authorizations.EMPTY,
                                                         1,
                                                         config);
            if (entityTemplateKey != null && entityTemplateKey != "")
            {
                bd.setRanges(Collections.singleton(Range.exact(new Text(entityTemplateKey))));

                bd.delete();
                LOGGER.warn("deletion successful");
            }
            else
            {
                LOGGER.warn("no template found!");
            }
            bd.close();

            persisted = true;
        }
        catch (TableNotFoundException | AccumuloException e)
        {
            persisted = false;
        }
        return persisted;
    }

    @Override
    public CoalesceEntityTemplate getEntityTemplate(String name, String source, String version)
            throws CoalescePersistorException
    {
        // TODO This can be optimized to not search twice
        try
        {
            return getEntityTemplate(getEntityTemplateKey(name, source, version));
        }
        catch (CoalescePersistorException e)
        {
            throw new CoalescePersistorException(String.format(CoalesceErrors.NOT_FOUND,
                                                               "Template",
                                                               "Name: " + name + " Source: " + source + " Version: "
                                                                       + version), e);
        }
    }

    @Override
    public String getEntityTemplateKey(String name, String source, String version) throws CoalescePersistorException
    {
        String key = null;
        Connector dbConnector = connect.getDBConnector();
        try (CloseableScanner keyscanner = new CloseableScanner(dbConnector,
                                                                AccumuloDataConnector.COALESCE_TEMPLATE_TABLE,
                                                                Authorizations.EMPTY))
        {
            // Uses special columnqualifier that is a concat of
            // name+source+version
            keyscanner.fetchColumn(new Text(coalesceTemplateColumnFamily), new Text(name + source + version));
            // TODO Add error handling if more than one row returned.
            if (keyscanner.iterator().hasNext())
            {
                key = keyscanner.iterator().next().getValue().toString();
            }
            keyscanner.close();
        }
        catch (TableNotFoundException ex)
        {
            throw new CoalescePersistorException(String.format(CoalesceErrors.NOT_FOUND,
                                                               "Template",
                                                               "Name: " + name + " Source: " + source + " Version: "
                                                                       + version), ex);
        }
        return key;
    }

    // Utility method to strip the row key, visibility, and timestamp from the
    // SortedMap returned from decodeRow
    private static SortedMap<String, Value> columnMap(SortedMap<Key, Value> row)
    {
        TreeMap<String, Value> colMap = new TreeMap<>();
        for (Map.Entry<Key, Value> e : row.entrySet())
        {
            String cf = e.getKey().getColumnFamily().toString();
            String cq = e.getKey().getColumnQualifier().toString();
            colMap.put(cf + ":" + cq, e.getValue());
        }
        return colMap;
    }

    @Override
    public List<ObjectMetaData> getEntityTemplateMetadata() throws CoalescePersistorException
    {
        List<ObjectMetaData> results = new ArrayList<ObjectMetaData>();

        Connector dbConnector = connect.getDBConnector();

        try (CloseableScanner scanner = new CloseableScanner(dbConnector,
                                                             AccumuloDataConnector.COALESCE_TEMPLATE_TABLE,
                                                             Authorizations.EMPTY))
        {
            Text templateColumnFamily = new Text(coalesceTemplateColumnFamily);
            scanner.fetchColumn(templateColumnFamily, new Text(coalesceTemplateNameQualifier));
            scanner.fetchColumn(templateColumnFamily, new Text(coalesceTemplateSourceQualifier));
            scanner.fetchColumn(templateColumnFamily, new Text(coalesceTemplateVersionQualifier));
            scanner.fetchColumn(templateColumnFamily, new Text(coalesceTemplateDateCreatedQualifier));
            scanner.fetchColumn(templateColumnFamily, new Text(coalesceTemplateDateModifiedQualifier));
            IteratorSetting iter = new IteratorSetting(1,
                                                       "rowiterator",
                                                       (Class<? extends SortedKeyValueIterator<Key, Value>>) WholeRowIterator.class);
            scanner.addScanIterator(iter);
            // Create Document
            for (Entry<Key, Value> entry : scanner)
            {
                // Create New Template Element
                SortedMap<Key, Value> wholeRow = null;
                wholeRow = WholeRowIterator.decodeRow(entry.getKey(), entry.getValue());

                SortedMap<String, Value> colmap = columnMap(wholeRow);

                String key = entry.getKey().getRow().toString();
                String name = colmap.get(coalesceTemplateColumnFamily + ":" + coalesceTemplateNameQualifier).toString();
                String source = colmap.get(coalesceTemplateColumnFamily + ":" + coalesceTemplateSourceQualifier).toString();
                String version = colmap.get(
                        coalesceTemplateColumnFamily + ":" + coalesceTemplateVersionQualifier).toString();
                DateTime created = JodaDateTimeHelper.fromXmlDateTimeUTC(colmap.get(
                        coalesceTemplateColumnFamily + ":" + coalesceTemplateDateCreatedQualifier).toString());
                DateTime lastModified = JodaDateTimeHelper.fromXmlDateTimeUTC(colmap.get(
                        coalesceTemplateColumnFamily + ":" + coalesceTemplateDateModifiedQualifier).toString());

                results.add(new ObjectMetaData(key, name, source, version, created, lastModified));
            }

        }
        catch (IOException | TableNotFoundException e)
        {
            throw new CoalescePersistorException("Error Getting Template Metadata", e);
        }

        return results;
    }

    @Override
    protected boolean flattenObject(boolean allowRemoval, CoalesceEntity... entities) throws CoalescePersistorException
    {
        boolean isSuccessful = true;

        Connector dbConnector = connect.getDBConnector();
        BatchWriterConfig config = new BatchWriterConfig();
        config.setMaxLatency(1, TimeUnit.SECONDS);
        config.setMaxMemory(52428800);
        config.setTimeout(600, TimeUnit.SECONDS);
        // config.setDurability(Durability.DEFAULT); // Requires Accumulo
        // 1.7
        config.setMaxWriteThreads(10);

        Map<String, DefaultFeatureCollection> featureCollectionMap = new HashMap<>();

        try (CloseableBatchWriter writer = new CloseableBatchWriter(dbConnector,
                                                                    AccumuloDataConnector.COALESCE_ENTITY_TABLE,
                                                                    config))
        {
            long beginTime = System.currentTimeMillis();

            batchInProgress = true;

            for (CoalesceEntity entity : entities)
            {
                if ("entity".equalsIgnoreCase(entity.getType()))
                {
                    try
                    {
                        isSuccessful &= persistEntityObject(entity, connect, writer, featureCollectionMap, allowRemoval);
                    }
                    catch (CoalesceDataFormatException | SQLException | SAXException | IOException e)
                    {
                        throw new CoalescePersistorException("FlattenObject", e);
                    }
                }
            }
            writer.close();
            LOGGER.debug("Batch Entity Persist Time: {}", System.currentTimeMillis() - beginTime);

        }
        catch (MutationsRejectedException | TableNotFoundException e1)
        {
            LOGGER.error(e1.getMessage(), e1);
        }

        addFeatures(featureCollectionMap);
        batchInProgress = false;
        return isSuccessful;
    }

    private void addFeatures(Map<String, DefaultFeatureCollection> featureCollectionMap)
    {
        long startTime = System.currentTimeMillis();
        for (Entry<String, DefaultFeatureCollection> entry : featureCollectionMap.entrySet())
        {

            try
            {
                SimpleFeatureStore featureStore = (SimpleFeatureStore) connect.getGeoDataStore().getFeatureSource(entry.getKey());
                //				GEOMESA Does not currently support transactions
                //               Transaction transaction = new DefaultTransaction();
                //                featureStore.setTransaction(transaction);
                long beginTime = System.currentTimeMillis();
                featureStore.addFeatures(entry.getValue());
                LOGGER.debug("Feature Add Time: {}: {}", entry.getKey(), System.currentTimeMillis() - beginTime);

                //                transaction.commit();
                //                transaction.close();

            }
            catch (IOException | IllegalArgumentException e)
            {
                LOGGER.error(e.getMessage(), e);
                LOGGER.error("Entry in error: " + entry.getValue().toString());
            }

        }
        LOGGER.debug("Total Feature Add Time:  {}", System.currentTimeMillis() - startTime);

    }

    @Override
    protected CoalesceDataConnectorBase getDataConnector() throws CoalescePersistorException
    {
        return new AccumuloDataConnector(getConnectionSettings());
    }

    protected boolean persistEntityObject(CoalesceEntity entity,
                                          AccumuloDataConnector conn,
                                          CloseableBatchWriter writer,
                                          Map<String, DefaultFeatureCollection> featureCollectionMap,
                                          boolean allowRemoval)
            throws SQLException, CoalescePersistorException, SAXException, IOException, CoalesceDataFormatException
    {
        boolean persisted = false;
        long beginTime;
        try
        {
            Connector dbConnector = connect.getDBConnector();
            BatchWriterConfig config = new BatchWriterConfig();
            config.setMaxLatency(1, TimeUnit.SECONDS);
            config.setMaxMemory(52428800);
            config.setTimeout(600, TimeUnit.SECONDS);
            // config.setDurability(Durability.DEFAULT); // Requires Accumulo
            // 1.7
            config.setMaxWriteThreads(10);
            if (entity.isFlatten())
            {
                switch (entity.getStatus())
                {
                case READONLY:
                case ACTIVE:
                    // Persist Object
                    beginTime = System.currentTimeMillis();
                    persisted =
                            persistBaseData(entity, dbConnector, writer) && persistEntityIndex(entity, dbConnector, config);
                    LOGGER.debug("Entity Base Data Persist Time: {}", System.currentTimeMillis() - beginTime);
                    persistRecordSearchData(entity, dbConnector, featureCollectionMap, allowRemoval);
                    LOGGER.debug("Entity Total Data Persist Time: {}", System.currentTimeMillis() - beginTime);
                    // Persist the linkage information for search
                    persistLinkageSearch(connect.getGeoDataStore(), entity, featureCollectionMap);
                    // This will replace the base data
                    persistEntitySearch(connect.getGeoDataStore(), entity, featureCollectionMap);

                    break;

                case DELETED:
                    if (allowRemoval)
                    {

                        DataStore geoDataStore = connect.getGeoDataStore();

                        // Delete Object
                        persisted = deleteBaseData(entity, dbConnector, config) && deleteEntityIndex(entity,
                                                                                                     dbConnector,
                                                                                                     config);
                        for (CoalesceSection section : entity.getSections().values())
                        {
                            // String sectionname = section.getName();
                            for (CoalesceRecordset recordset : section.getRecordsetsAsList())
                            {
                                String recordname = recordset.getName();
                                String featuresetname = (recordname).replaceAll(" ", "_");

                                // Verify a featureset exists if not skip this
                                // record
                                SimpleFeatureType featuretype = geoDataStore.getSchema(featuresetname);
                                if (featuretype == null)
                                {
                                    break;
                                }

                                deleteRecordset(featuresetname, recordset);

                            }
                        }
                        deleteFromLinks(entity);
                        deleteEntitySearch(entity);
                    }
                    else
                    {
                        // Mark Object as Deleted
                        // Persist Object
                        beginTime = System.currentTimeMillis();
                        persisted = persistBaseData(entity, dbConnector, writer) && persistEntityIndex(entity,
                                                                                                       dbConnector,
                                                                                                       config);
                        LOGGER.debug("Base Data Persist Time: {}", System.currentTimeMillis() - beginTime);
                        beginTime = System.currentTimeMillis();
                        persistRecordSearchData(entity, dbConnector, featureCollectionMap, allowRemoval);
                        LOGGER.debug("Record Search Data Persist Time: {}", System.currentTimeMillis() - beginTime);
                        // Persist the linkage information for search
                        beginTime = System.currentTimeMillis();
                        persistLinkageSearch(connect.getGeoDataStore(), entity, featureCollectionMap);
                        LOGGER.debug("Linkage Data Persist Time: {}", System.currentTimeMillis() - beginTime);
                        // This will replace the base data
                        beginTime = System.currentTimeMillis();
                        persistEntitySearch(connect.getGeoDataStore(), entity, featureCollectionMap);
                        LOGGER.debug("Entity Data Persist Time: {}", System.currentTimeMillis() - beginTime);
                    }

                    break;

                default:
                    persisted = false;
                }
            }

        }
        catch (CoalesceException | CQLException ex)
        {
            LOGGER.error(ex.getLocalizedMessage(), ex);
            persisted = false;
        }
        return persisted;
    }

    private boolean persistBaseData(CoalesceEntity entity, Connector connect, CloseableBatchWriter writer)
            throws CoalesceException
    {
        boolean persisted = false;
        try
        {
            writer.addMutation(createMutation(entity));

            persisted = true;
        }
        catch (MutationsRejectedException e)
        {
            persisted = false;
        }
        return persisted;
    }

    private boolean deleteBaseData(CoalesceEntity entity, Connector connect, BatchWriterConfig config)
    {
        boolean persisted = false;
        try
        {
            BatchDeleter bd = connect.createBatchDeleter(AccumuloDataConnector.COALESCE_ENTITY_TABLE,
                                                         Authorizations.EMPTY,
                                                         1,
                                                         config);
            bd.setRanges(Collections.singleton(Range.exact(new Text(entity.getKey()))));
            bd.delete();
            bd.close();
            //TableOperations ops = connect.tableOperations();
            //Text row = new Text(entity.getKey());
            //ops.deleteRows(AccumuloDataConnector.coalesceTable,row,row);
            persisted = true;
        }
        catch (TableNotFoundException | AccumuloException e)
        {
            persisted = false;
        }
        return persisted;
    }

    private boolean persistEntityIndex(CoalesceEntity entity, Connector dbConnector, BatchWriterConfig config)
    {
        boolean persisted = false;
        try (CloseableBatchWriter writer = new CloseableBatchWriter(dbConnector,
                                                                    AccumuloDataConnector.COALESCE_ENTITY_IDX_TABLE,
                                                                    config))
        {
            Mutation m = new Mutation(entity.getKey());
            Text indexcf = new Text(entity.getEntityIdType() + "\0" + entity.getEntityId() + "\0" + entity.getName() + "\0"
                                            + entity.getSource());
            m.put(indexcf, new Text(entity.getNamePath()), new Value(new byte[0]));
            writer.addMutation(m);
            writer.flush();
            persisted = true;
            writer.close();
        }
        catch (MutationsRejectedException | TableNotFoundException e)
        {
            persisted = false;
        }

        return persisted;
    }

    private boolean deleteEntityIndex(CoalesceEntity entity, Connector connect, BatchWriterConfig config)
    {
        boolean persisted = false;
        try
        {
            BatchDeleter bd = connect.createBatchDeleter(AccumuloDataConnector.COALESCE_ENTITY_TABLE,
                                                         Authorizations.EMPTY,
                                                         1,
                                                         config);
            bd.setRanges(Collections.singleton(Range.exact(new Text(entity.getKey()))));
            bd.delete();
            bd.close();
            //TableOperations ops = connect.tableOperations();
            //Text row = new Text(entity.getKey());
            //ops.deleteRows(AccumuloDataConnector.coalesceTable,row,row);
            persisted = true;
        }
        catch (TableNotFoundException | AccumuloException e)
        {
            persisted = false;
        }
        return persisted;
    }

    private boolean persistRecordSearchData(CoalesceEntity entity,
                                            Connector dbConnector,
                                            Map<String, DefaultFeatureCollection> featureCollectionMap,
                                            boolean allowRemoval)
    {
        boolean persisted = false;
        // CoalesceEntityTemplate template = null;
        DataStore geoDataStore = connect.getGeoDataStore();

        // String templatename = (entity.getName() + "_" + entity.getSource() + "_" + entity.getVersion()).replaceAll(" ",
        // "_");
        // Find the template for this type
        try
        {

            // Validate the entity against the template
            // Note - could not see what this really did other than take up time.  Caused error with CRUD integration test so commenting out.
            //Map<String, String> errors = validator.validate(null, entity, CoalesceEntityTemplate.create(entity));
            //if (errors.isEmpty())
            //{

            for (CoalesceSection section : entity.getSections().values())
            {
                // String sectionname = section.getName();
                for (CoalesceRecordset recordset : section.getRecordsets().values())
                {
                    String recordname = recordset.getName();
                    String featuresetname = (recordname).replaceAll(" ", "_");

                    // Verify a featureset exists if not skip this
                    // record
                    SimpleFeatureType featuretype = geoDataStore.getSchema(featuresetname);
                    if (featuretype == null)
                    {
                        break;
                    }
                    else
                    {
                        LOGGER.trace("Found featureSet {}", featuresetname);
                    }

                    if (allowRemoval)
                    {
                        deleteRecordset(featuresetname, recordset);
                    }

                    addNewFeatureForRecordSet(entity,
                                              featureCollectionMap,
                                              recordset,
                                              featuresetname,
                                              featuretype,
                                              allowRemoval);

                    persisted = true;
                }
            }

            //}    

        }
        catch (IOException | CoalesceDataFormatException | CQLException e)
        {
            LOGGER.error(e.getMessage(), e);
            persisted = false;
        }
        return persisted;
    }

    private void deleteFromLinks(CoalesceEntity entity) throws IOException, CQLException, CoalesceDataFormatException
    {
        DataStore store = connect.getGeoDataStore();
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        SimpleFeatureStore featureStore = (SimpleFeatureStore) store.getFeatureSource(LINKAGE_FEATURE_NAME);
        Filter filter = ff.equals(ff.property(LINKAGE_ENTITY1_KEY_COLUMN_NAME), ff.literal(entity.getKey()));

        featureStore.removeFeatures(filter);

    }

    private void persistLinkageSearch(DataStore gds,
                                      CoalesceEntity entity,
                                      Map<String, DefaultFeatureCollection> featureCollectionMap)
            throws CQLException, CoalesceDataFormatException, IOException
    {
        DefaultFeatureCollection featurecollection = featureCollectionMap.get(LINKAGE_FEATURE_NAME);

        if (featurecollection == null)
        {
            featurecollection = new DefaultFeatureCollection();
            featureCollectionMap.put(LINKAGE_FEATURE_NAME, featurecollection);
        }
        Map<String, CoalesceLinkage> linkages = entity.getLinkages();
        for (Map.Entry<String, CoalesceLinkage> mlink : linkages.entrySet())
        {
            CoalesceLinkage link = mlink.getValue();
            Boolean updated = updateLinkIfExists(LINKAGE_FEATURE_NAME, link);

            if (!updated)
            {
                SimpleFeatureType featuretype = gds.getSchema(LINKAGE_FEATURE_NAME);
                SimpleFeature simplefeature = SimpleFeatureBuilder.build(featuretype, new Object[] {}, link.getKey());
                simplefeature.getUserData().put(Hints.USE_PROVIDED_FID, true);

                setFeatureAttribute(simplefeature,
                                    LINKAGE_KEY_COLUMN_NAME,
                                    ECoalesceFieldDataTypes.STRING_TYPE,
                                    link.getKey());

                setFeatureAttribute(simplefeature,
                                    LINKAGE_ENTITY1_KEY_COLUMN_NAME,
                                    ECoalesceFieldDataTypes.STRING_TYPE,
                                    link.getEntity1Key());
                setFeatureAttribute(simplefeature,
                                    LINKAGE_ENTITY1_NAME_COLUMN_NAME,
                                    ECoalesceFieldDataTypes.STRING_TYPE,
                                    link.getEntity1Name());
                setFeatureAttribute(simplefeature,
                                    LINKAGE_ENTITY1_SOURCE_COLUMN_NAME,
                                    ECoalesceFieldDataTypes.STRING_TYPE,
                                    link.getEntity1Source());
                setFeatureAttribute(simplefeature,
                                    LINKAGE_ENTITY1_VERSION_COLUMN_NAME,
                                    ECoalesceFieldDataTypes.STRING_TYPE,
                                    link.getEntity1Version());
                setFeatureAttribute(simplefeature,
                                    LINKAGE_ENTITY2_KEY_COLUMN_NAME,
                                    ECoalesceFieldDataTypes.STRING_TYPE,
                                    link.getEntity2Key());
                setFeatureAttribute(simplefeature,
                                    LINKAGE_ENTITY2_NAME_COLUMN_NAME,
                                    ECoalesceFieldDataTypes.STRING_TYPE,
                                    link.getEntity2Name());
                setFeatureAttribute(simplefeature,
                                    LINKAGE_ENTITY2_SOURCE_COLUMN_NAME,
                                    ECoalesceFieldDataTypes.STRING_TYPE,
                                    link.getEntity2Source());
                setFeatureAttribute(simplefeature,
                                    LINKAGE_ENTITY2_VERSION_COLUMN_NAME,
                                    ECoalesceFieldDataTypes.STRING_TYPE,
                                    link.getEntity2Version());
                setFeatureAttribute(simplefeature,
                                    LINKAGE_LAST_MODIFIED_COLUMN_NAME,
                                    ECoalesceFieldDataTypes.DATE_TIME_TYPE,
                                    link.getLastModified());
                setFeatureAttribute(simplefeature,
                                    LINKAGE_LABEL_COLUMN_NAME,
                                    ECoalesceFieldDataTypes.STRING_TYPE,
                                    link.getLabel());
                setFeatureAttribute(simplefeature,
                                    LINKAGE_LINK_TYPE_COLUMN_NAME,
                                    ECoalesceFieldDataTypes.ENUMERATION_TYPE,
                                    link.getLinkType());

                // create a polygon of the WORLD!!!!!
                //                    Coordinate coord1 = new Coordinate(-180, -90);
                //                    Coordinate coord2 = new Coordinate(-180, 90);
                //                    Coordinate coord3 = new Coordinate(180, 90);
                //                    Coordinate coord4 = new Coordinate(180, -90);
                //                    Coordinate coord5 = new Coordinate(-180, -90);
                //
                //                    GeometryFactory geoFactory = new GeometryFactory();
                //
                //                    CoordinateSequence coordSeq = new CoordinateArraySequence(new Coordinate[] { coord1, coord2, coord3,
                //                                                                                                 coord4, coord5 });
                //
                //                    LinearRing linearRing = new LinearRing(new CoordinateArraySequence(coordSeq), geoFactory);
                //
                //                    Polygon polygon = new Polygon(linearRing, null, geoFactory);
                //                    String geomname = featuretype.getGeometryDescriptor().getName().toString();
                //                    simplefeature.setAttribute(DEFAULT_GEO_FIELD_NAME, polygon);
                featurecollection.add(simplefeature);

            }

        }
    }

    private void deleteRecordset(String featuresetname, CoalesceRecordset recordset) throws CQLException, IOException
    {

        for (CoalesceRecord record : recordset.getRecords())
        {
            deleteFeatureIfExists(featuresetname, record);
        }

    }

    private DefaultFeatureCollection addNewFeatureForRecordSet(CoalesceEntity entity,
                                                               Map<String, DefaultFeatureCollection> featureCollectionMap,
                                                               CoalesceRecordset recordset,
                                                               String featuresetname,
                                                               SimpleFeatureType featuretype,
                                                               boolean allowRemoval)
            throws CoalesceDataFormatException, CQLException, IOException
    {
        DefaultFeatureCollection featurecollection = featureCollectionMap.get(featuresetname);

        if (featurecollection == null)
        {
            featurecollection = new DefaultFeatureCollection();
            featureCollectionMap.put(featuresetname, featurecollection);
        }
        // Do a feature collection for all records

        // Create a geo record for each record in the
        // recordset
        for (CoalesceRecord record : recordset.getRecords())
        {
            boolean hasGeoField = false;
            boolean updated;
            // delete the feature if it already exists for "update"
            if (allowRemoval)
            {
                // If allowRemoval is true all features have been deleted already
                updated = false;
            }
            else
            {
                long beginTime = System.currentTimeMillis();
                updated = updateFeatureIfExists(featuresetname, record);
                LOGGER.debug("Feature Update Time: {}, Updated: {}", System.currentTimeMillis() - beginTime, updated);

            }

            if (!updated)
            {
                // create
                LOGGER.info(featuretype.toString());

                SimpleFeature simplefeature = SimpleFeatureBuilder.build(featuretype, new Object[] {}, record.getKey());
                simplefeature.getUserData().put(Hints.USE_PROVIDED_FID, true);
                simplefeature.getUserData().put("geomesa.index.dtg", null);
                setFeatureAttribute(simplefeature,
                                    ENTITY_KEY_COLUMN_NAME,
                                    ECoalesceFieldDataTypes.STRING_TYPE,
                                    entity.getKey());
                setFeatureAttribute(simplefeature,
                                    ENTITY_NAME_COLUMN_NAME,
                                    ECoalesceFieldDataTypes.STRING_TYPE,
                                    entity.getName());
                setFeatureAttribute(simplefeature,
                                    ENTITY_SOURCE_COLUMN_NAME,
                                    ECoalesceFieldDataTypes.STRING_TYPE,
                                    entity.getSource());
                setFeatureAttribute(simplefeature,
                                    ENTITY_TITLE_COLUMN_NAME,
                                    ECoalesceFieldDataTypes.STRING_TYPE,
                                    entity.getTitle());
                setFeatureAttribute(simplefeature,
                                    ENTITY_RECORD_KEY_COLUMN_NAME,
                                    ECoalesceFieldDataTypes.STRING_TYPE,
                                    record.getKey());

                for (CoalesceField<?> field : record.getFields())
                {
                    String fieldname = field.getName();
                    ECoalesceFieldDataTypes fieldtype = field.getDataType();
                    Object fieldvalue = field.getValue();
                    boolean isGeoField = false;

                    // If there is not a value do not set the
                    // attribute.
                    if (fieldvalue != null)
                    {
                        LOGGER.trace("Setting FeatureAttribute {}, is type {}", fieldname, fieldtype);
                        setFeatureAttribute(simplefeature, fieldname, fieldtype, fieldvalue);
                    }

                    // Geo fields with null values wont index
                    isGeoField = isGeoField(fieldtype) && (fieldvalue != null);

                    if (isGeoField)
                    {
                        hasGeoField = true;
                        LOGGER.debug("GeoForEntity: " + entity.getKey() + "Value: " + fieldvalue);
                    }
                }

                if (!hasGeoField)
                {
                    // create a polygon of the WORLD!!!!!
                    Coordinate coord1 = new Coordinate(-180, -90);
                    Coordinate coord2 = new Coordinate(-180, 90);
                    Coordinate coord3 = new Coordinate(180, 90);
                    Coordinate coord4 = new Coordinate(180, -90);
                    Coordinate coord5 = new Coordinate(-180, -90);

                    GeometryFactory geoFactory = new GeometryFactory();

                    CoordinateSequence coordSeq = new CoordinateArraySequence(new Coordinate[] { coord1, coord2, coord3,
                                                                                                 coord4, coord5
                    });

                    LinearRing linearRing = new LinearRing(new CoordinateArraySequence(coordSeq), geoFactory);

                    Polygon polygon = new Polygon(linearRing, null, geoFactory);
                    String geomname = featuretype.getGeometryDescriptor().getName().toString();
                    simplefeature.setAttribute(geomname, polygon);
                    LOGGER.debug("NO Geo for entity: " + entity.getKey());
                }
                else
                {
                    LOGGER.debug("Found Geo for entity: " + entity.getKey());
                }

                LOGGER.debug("Adding Feature to collection: {}", featuretype.getName());
                featurecollection.add(simplefeature);

            }

        }
        return featurecollection;
    }

    private void deleteFeatureIfExists(String featuresetname, CoalesceRecord record) throws IOException, CQLException
    {
        //        Transaction transaction = new DefaultTransaction();
        long beginTime = System.currentTimeMillis();

        SimpleFeatureStore store = (SimpleFeatureStore) connect.getGeoDataStore().getFeatureSource(featuresetname);
        //        store.setTransaction(transaction);

        // Filters need fully qualified column name must be quoted
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

        FeatureId fid = ff.featureId(record.getKey());
        // TODO Need to add compare of modified time to see if there was an update
        Filter filter = ff.id(Collections.singleton(fid));

        store.removeFeatures(filter);
        LOGGER.debug("Feature Delete Time: {}: {}", featuresetname, System.currentTimeMillis() - beginTime);

        //        transaction.commit();
        //        transaction.close();
    }

    private boolean updateFeatureIfExists(String featuresetname, CoalesceRecord record)
            throws IOException, CQLException, CoalesceDataFormatException
    {
        boolean updated = false;
        DataStore store = connect.getGeoDataStore();
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

        FeatureId fid = ff.featureId(record.getKey());
        // TODO Need to add compare of modified time to see if there was an update
        Filter filter = ff.id(Collections.singleton(fid));

        // Need to escape the fully qualified column in the feature set for filters
        //Filter filter = CQL.toFilter("\""+featuresetname+".recordKey\" =" + "'" + record.getKey() + "'");

        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = store.getFeatureWriter(featuresetname,
                                                                                        filter,
                                                                                        Transaction.AUTO_COMMIT);

        if (writer.hasNext())
        {
            SimpleFeature toModify = writer.next();
            String liststring;
            for (CoalesceField<?> field : record.getFields())
            {
                // update
                //store.modifyFeatures(field.getName(), field.getValue(), filter);
                String fieldname = field.getName();
                Object fieldvalue = field.getValue();
                // Skip field if value is null
                if (fieldvalue == null)
                    continue;
                switch (field.getDataType())
                {

                // These types should be able to be handled directly
                case BOOLEAN_TYPE:
                case DOUBLE_TYPE:
                case FLOAT_TYPE:
                case INTEGER_TYPE:
                case LONG_TYPE:
                case STRING_TYPE:
                case URI_TYPE:
                case LINE_STRING_TYPE:
                case POLYGON_TYPE:
                    toModify.setAttribute(fieldname, fieldvalue);
                    break;

                case STRING_LIST_TYPE:
                    liststring = Arrays.toString((String[]) fieldvalue);
                    toModify.setAttribute(fieldname, liststring);
                    break;
                case DOUBLE_LIST_TYPE:
                    liststring = Arrays.toString((double[]) fieldvalue);
                    toModify.setAttribute(fieldname, liststring);
                    break;
                case INTEGER_LIST_TYPE:
                    liststring = Arrays.toString((int[]) fieldvalue);
                    toModify.setAttribute(fieldname, liststring);
                    break;
                case LONG_LIST_TYPE:
                    liststring = Arrays.toString((long[]) fieldvalue);
                    toModify.setAttribute(fieldname, liststring);
                    break;
                case FLOAT_LIST_TYPE:
                    liststring = Arrays.toString((float[]) fieldvalue);
                    toModify.setAttribute(fieldname, liststring);
                    break;
                case GUID_LIST_TYPE:
                    liststring = Arrays.toString((UUID[]) fieldvalue);
                    toModify.setAttribute(fieldname, liststring);
                    break;
                case BOOLEAN_LIST_TYPE:

                    liststring = Arrays.toString((boolean[]) fieldvalue);
                    toModify.setAttribute(fieldname, liststring);
                    break;

                case GUID_TYPE:
                    String guid = ((UUID) fieldvalue).toString();
                    toModify.setAttribute(fieldname, guid);
                    break;

                case GEOCOORDINATE_LIST_TYPE:
                    MultiPoint points = new GeometryFactory().createMultiPoint((Coordinate[]) fieldvalue);
                    toModify.setAttribute(fieldname, points);
                    break;

                case GEOCOORDINATE_TYPE:
                    Point point = new GeometryFactory().createPoint((Coordinate) fieldvalue);
                    toModify.setAttribute(fieldname, point);
                    break;

                // Circles will be converted to polygons
                case CIRCLE_TYPE:
                    // Create Polygon

                    CoalesceCircle circle = (CoalesceCircle) fieldvalue;
                    GeometricShapeFactory factory = new GeometricShapeFactory();
                    factory.setSize(circle.getRadius());
                    factory.setNumPoints(360); // 1 degree points
                    factory.setCentre(circle.getCenter());
                    Polygon shape = factory.createCircle();
                    toModify.setAttribute(fieldname, shape);
                    break;

                case DATE_TIME_TYPE:
                    toModify.setAttribute(fieldname, ((DateTime) fieldvalue).toDate());
                    break;
                case FILE_TYPE:
                case BINARY_TYPE:
                default:
                    break;
                }
            }
            updated = true;
            writer.write();

        }
        writer.close();

        return updated;
    }

    private boolean updateLinkIfExists(String featuresetname, CoalesceLinkage link)
            throws IOException, CQLException, CoalesceDataFormatException
    {
        boolean updated = false;
        DataStore store = connect.getGeoDataStore();
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

        FeatureId fid = ff.featureId(link.getKey());

        Filter filter = ff.id(Collections.singleton(fid));

        // Need to escape the fully qualified column in the feature set for filters
        //Filter filter = CQL.toFilter("\""+featuresetname+".recordKey\" =" + "'" + record.getKey() + "'");

        //SimpleFeatureCollection collection = store.getFeatures(filter);
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = store.getFeatureWriter(featuresetname,
                                                                                        filter,
                                                                                        Transaction.AUTO_COMMIT);

        if (writer.hasNext())
        {

            SimpleFeature toModify = writer.next();

            DateTime lm = new DateTime(toModify.getAttribute(LINKAGE_LAST_MODIFIED_COLUMN_NAME));
            // IF the lastModified of this link is less than or equal to the one
            // stored nothing has changed so don't update but return true so we don't
            // persist another copy
            if (!lm.isBefore(link.getLastModified()))
            {
                writer.close();
                return true;
            }
            final String linkfields[] = { LINKAGE_ENTITY1_KEY_COLUMN_NAME, LINKAGE_ENTITY1_NAME_COLUMN_NAME,
                                          LINKAGE_ENTITY1_SOURCE_COLUMN_NAME, LINKAGE_ENTITY1_VERSION_COLUMN_NAME,
                                          LINKAGE_ENTITY2_KEY_COLUMN_NAME, LINKAGE_ENTITY2_NAME_COLUMN_NAME,
                                          LINKAGE_ENTITY2_SOURCE_COLUMN_NAME, LINKAGE_ENTITY2_VERSION_COLUMN_NAME,
                                          LINKAGE_LAST_MODIFIED_COLUMN_NAME, LINKAGE_LABEL_COLUMN_NAME,
                                          LINKAGE_LINK_TYPE_COLUMN_NAME
            };
            Object[] values = new Object[] { link.getEntity1Key(), link.getEntity1Name(), link.getEntity1Source(),
                                             link.getEntity1Version(), link.getEntity2Key(), link.getEntity2Name(),
                                             link.getEntity2Source(), link.getEntity2Version(), link.getLastModified(),
                                             link.getLabel(), link.getLinkType()
            };
            for (int i = 0; i < linkfields.length; i++)
            {
                toModify.setAttribute(linkfields[i], values[i]);
            }

            writer.write();
            updated = true;

        }
        writer.close();

        return updated;
    }

    private boolean isGeoField(ECoalesceFieldDataTypes fieldtype)
    {
        boolean isGeoField;
        switch (fieldtype)
        {
        case CIRCLE_TYPE:
        case GEOCOORDINATE_LIST_TYPE:
        case GEOCOORDINATE_TYPE:
        case POLYGON_TYPE:
            isGeoField = true;
            break;
        default:
            isGeoField = false;
        }
        return isGeoField;
    }

    private boolean setFeatureAttribute(SimpleFeature simplefeature,
                                        String fieldname,
                                        ECoalesceFieldDataTypes fieldtype,
                                        Object fieldvalue) throws CoalesceDataFormatException
    {
        String liststring;

        boolean isGeoField = false;

        try
        {
            switch (fieldtype)
            {

            // These types should be able to be handled directly
            case BOOLEAN_TYPE:
            case DOUBLE_TYPE:
            case FLOAT_TYPE:
            case INTEGER_TYPE:
            case LONG_TYPE:
            case STRING_TYPE:
            case URI_TYPE:
                simplefeature.setAttribute(fieldname, fieldvalue);
                break;

            case STRING_LIST_TYPE:
                liststring = Arrays.toString((String[]) fieldvalue);
                simplefeature.setAttribute(fieldname, liststring);
                break;
            case DOUBLE_LIST_TYPE:
                liststring = Arrays.toString((double[]) fieldvalue);
                simplefeature.setAttribute(fieldname, liststring);
                break;
            case INTEGER_LIST_TYPE:
                liststring = Arrays.toString((int[]) fieldvalue);
                simplefeature.setAttribute(fieldname, liststring);
                break;
            case LONG_LIST_TYPE:
                liststring = Arrays.toString((long[]) fieldvalue);
                simplefeature.setAttribute(fieldname, liststring);
                break;
            case FLOAT_LIST_TYPE:
                liststring = Arrays.toString((float[]) fieldvalue);
                simplefeature.setAttribute(fieldname, liststring);
                break;
            case GUID_LIST_TYPE:
                liststring = Arrays.toString((UUID[]) fieldvalue);
                simplefeature.setAttribute(fieldname, liststring);
                break;
            case BOOLEAN_LIST_TYPE:

                liststring = Arrays.toString((boolean[]) fieldvalue);
                simplefeature.setAttribute(fieldname, liststring);
                break;

            case GUID_TYPE:
                String guid = ((UUID) fieldvalue).toString();
                simplefeature.setAttribute(fieldname, guid);
                break;

            case ENUMERATION_TYPE:
                String enumname = fieldvalue.toString();
                simplefeature.setAttribute(fieldname, enumname);
                break;

            case GEOCOORDINATE_LIST_TYPE:
                MultiPoint points = new GeometryFactory().createMultiPoint((Coordinate[]) fieldvalue);
                simplefeature.setAttribute(fieldname, points);
                isGeoField = true;
                break;

            case GEOCOORDINATE_TYPE:
                Point point = new GeometryFactory().createPoint((Coordinate) fieldvalue);
                simplefeature.setAttribute(fieldname, point);
                isGeoField = true;
                break;

            case LINE_STRING_TYPE:
                simplefeature.setAttribute(fieldname, fieldvalue);
                isGeoField = true;
                break;

            case POLYGON_TYPE:
                simplefeature.setAttribute(fieldname, fieldvalue);
                isGeoField = true;
                break;

            // Circles will be converted to polygons
            case CIRCLE_TYPE:
                // Create Polygon

                CoalesceCircle circle = (CoalesceCircle) fieldvalue;
                GeometricShapeFactory factory = new GeometricShapeFactory();
                factory.setSize(circle.getRadius());
                factory.setNumPoints(360); // 1 degree points
                factory.setCentre(circle.getCenter());
                Polygon shape = factory.createCircle();
                simplefeature.setAttribute(fieldname, shape);
                isGeoField = true;
                break;

            case DATE_TIME_TYPE:
                simplefeature.setAttribute(fieldname, ((DateTime) fieldvalue).toDate());
                break;
            case FILE_TYPE:
            case BINARY_TYPE:
            default:
                break;
            }
        }
        catch (IllegalAttributeException e)
        {
            LOGGER.warn(String.format(CoalesceErrors.INVALID_INPUT_REASON, fieldname, e.getMessage()));
            isGeoField = false;
        }

        return isGeoField;
        /*
         * 
         * // accumulate this new feature in the collection featureCollection.add(simpleFeature); }
         * 
         * return featureCollection; }
         * 
         * static void insertFeatures(String simpleFeatureTypeName, DataStore dataStore, FeatureCollection featureCollection)
         * throws IOException {
         * 
         * FeatureStore featureStore = (SimpleFeatureStore) dataStore.getFeatureSource(simpleFeatureTypeName);
         * featureStore.addFeatures(featureCollection); }
         */
    }

    private Mutation createMutation(CoalesceEntity entity) throws CoalesceException
    {
        MutationWrapperFactory mfactory = new MutationWrapperFactory();
        MutationWrapper mutationGuy = mfactory.createMutationGuy(entity);
        return mutationGuy.getMutation();
    }

    protected String getValues(CoalesceObject coalesceObject, Map<?, ?> values)
    {
        if (coalesceObject.isActive())
        {
            switch (coalesceObject.getType().toLowerCase())
            {
            case "field":
                CoalesceField<?> fieldObject = (CoalesceField<?>) coalesceObject;
                switch (fieldObject.getType().toUpperCase())
                {
                case "BINARY":
                case "FILE":
                default:
                {
                    if (values == null)
                    {

                    }
                }
                break;
                }
            }
        }
        return null;
    }

    protected boolean checkLastModified(CoalesceObject coalesceObject, CoalesceDataConnectorBase conn)
            throws SQLException, CoalescePersistorException
    {
        boolean isOutOfDate = true;

        // Get LastModified from the Database
        DateTime lastModified = this.getCoalesceObjectLastModified(coalesceObject.getKey(), coalesceObject.getType(), conn);

        // DB Has Valid Time?
        if (lastModified != null)
        {
            // Remove NanoSeconds (100 ns / Tick and 1,000,000 ns / ms = 10,000
            // Ticks / ms)
            long objectTicks = coalesceObject.getLastModified().getMillis();
            long SQLRecordTicks = lastModified.getMillis();

            // TODO: Round Ticks for SQL (Not sure if this is required for .NET)
            // ObjectTicks = this.RoundTicksForSQL(ObjectTicks);

            if (objectTicks == SQLRecordTicks)
            {
                // They're equal; No Update Required
                isOutOfDate = false;
            }
        }

        return isOutOfDate;
    }

    private DateTime getCoalesceObjectLastModified(String key, String objectType, CoalesceDataConnectorBase conn)
            throws SQLException, CoalescePersistorException
    {

        DateTime lastModified = null;
        Connector dbConn = connect.getDBConnector();

        String rowRegex = null;
        String colfRegex = StringUtils.isNotBlank(objectType) ? objectType : null;
        String colqRegex = "key";
        String valueRegex = StringUtils.isNotBlank(key) ? key : null;
        // to use a filter, which is an iterator, you must create an
        // IteratorSetting
        // specifying which iterator class you are using
        IteratorSetting iter = new IteratorSetting(1, "modifiedFilter", RegExFilter.class);
        RegExFilter.setRegexs(iter, rowRegex, colfRegex, colqRegex, valueRegex, false, true);
        try (CloseableScanner scanner = new CloseableScanner(dbConn,
                                                             AccumuloDataConnector.COALESCE_ENTITY_TABLE,
                                                             Authorizations.EMPTY))
        {
            scanner.addScanIterator(iter);
            String objectkey = null;
            Text objectcf = null;
            // TODO Add error handling if more than one row returned.
            if (scanner.iterator().hasNext())
            {
                // Get the associated row Id and columnFamily for next search
                Key rowKey = scanner.iterator().next().getKey();
                objectkey = rowKey.getRow().toString();
                objectcf = rowKey.getColumnFamily();
                // Get the lastmodified for that row and columnFamily
                try (CloseableScanner keyscanner = new CloseableScanner(dbConn,
                                                                        AccumuloDataConnector.COALESCE_ENTITY_TABLE,
                                                                        Authorizations.EMPTY))
                {
                    keyscanner.setRange(new Range(objectkey));
                    keyscanner.fetchColumn(objectcf, new Text("lastmodified"));
                    // TODO Add error handling if more than one row returned.
                    // Throw CoalescePersistorException
                    if (keyscanner.iterator().hasNext())
                    {
                        String dateString = new String(keyscanner.iterator().next().getValue().get());
                        lastModified = JodaDateTimeHelper.fromXmlDateTimeUTC(dateString);
                    }
                }
            }
        }
        catch (TableNotFoundException ex)
        {
            LOGGER.error(ex.getLocalizedMessage(), ex);
        }
        return lastModified;
    }

    @Override
    protected boolean flattenCore(boolean allowRemoval, CoalesceEntity... entities) throws CoalescePersistorException
    {
        // TODO Auto-generated method stub
        return false;
    }

    public DateTime getCoalesceObjectLastModified(String key, String objectType) throws CoalescePersistorException
    {
        try
        {
            return getCoalesceObjectLastModified(key, objectType, connect);
        }
        catch (SQLException e)
        {
            throw new CoalescePersistorException("GetCoalesceObjectLastModified", e);
        }
        catch (Exception e)
        {
            throw new CoalescePersistorException("GetCoalesceObjectLastModified", e);
        }
    }

    @Override
    public SearchResults search(Query query) throws CoalescePersistorException
    {

        AccumuloQueryRewriter nameChanger = new AccumuloQueryRewriter(query);
        CachedRowSet rowset = null;
        SimpleFeatureCollection featureCount;
        DataStore geoDataStore = connect.getGeoDataStore();
        // Make a copy of the query
        Query localquery = nameChanger.rewrite();

        LOGGER.debug("Executing search against schema: {}", localquery.getTypeName());
        try
        {

            SimpleFeatureStore featureSource = (SimpleFeatureStore) geoDataStore.getFeatureSource(localquery.getTypeName());

            List<CoalesceColumnMetadata> columnList = new ArrayList<>();
            // Not needed as the Coalesce behavior does not support just get all Columns
            // Check if no properties were defined
            //            if (localquery.retrieveAllProperties()) {
            //                for (PropertyDescriptor entry : featureSource.getSchema().getDescriptors())
            //                {
            //                	String columnName = entry.getName().getLocalPart();
            //                    CoalesceColumnMetadata columnMetadata = new CoalesceColumnMetadata(columnName, "String", Types.VARCHAR);
            //                    columnList.add(columnMetadata);
            //                }
            //
            //            } else {
            // TODO - Why always String and VARCHAR.  Should these not be the real types
            // Use the original property names to populate the Rowset 
            // Make sure the entity key (prefixed by the table) is the first in the list
            // ALSO NO DOTS SEPARATING THE TABLE FROM COLUMN
            columnList.add(new CoalesceColumnMetadata(ENTITY_FEATURE_NAME + ENTITY_KEY_COLUMN_NAME,
                                                      "String",
                                                      Types.VARCHAR));
            if (query.getProperties() != null)
            {
                for (PropertyName entry : query.getProperties())
                {
                    String columnName = entry.getPropertyName().replaceAll("[.]", "");
                    CoalesceColumnMetadata columnMetadata = new CoalesceColumnMetadata(columnName, "String", Types.VARCHAR);
                    columnList.add(columnMetadata);
                }
            }

            // Need to get a count of the query without limitations to support the paging
            // Do no properties or sort, just the rewritten filter.
            Query countQuery = new Query(localquery.getTypeName());
            countQuery.setFilter(localquery.getFilter());
            countQuery.setProperties(Query.NO_PROPERTIES);
            featureCount = featureSource.getFeatures(countQuery);
            FeatureIterator<SimpleFeature> featureItr = featureSource.getFeatures(localquery).features();
            Iterator<Object[]> columnIterator = new FeatureColumnIterator(featureItr, query.getProperties());

            CoalesceResultSet resultSet = new CoalesceResultSet(columnIterator, columnList);

            rowset = RowSetProvider.newFactory().createCachedRowSet();
            rowset.populate(resultSet);
            featureItr.close();
        }
        catch (IOException | SQLException e)
        {
            throw new CoalescePersistorException(e.getMessage(), e);
        }

        SearchResults results = new SearchResults();

        results.setResults(rowset);
        results.setTotal(featureCount.size());

        return results;
    }

    @Override
    public Capabilities getSearchCapabilities()
    {
        Capabilities capability = new Capabilities();
        capability.addAll(Capabilities.SIMPLE_COMPARISONS);
        capability.addAll(Capabilities.LOGICAL);

        return capability;
    }

    public void close()
    {
        try
        {
            connect.getGeoDataStore().dispose();
            connect.close();
        }
        catch (CoalescePersistorException e)
        {
            e.printStackTrace();
        }
    }

    private void createEntityFeature(String featurename)
    {
        final String geomesaTimeIndex = "geomesa.index.dtg";
        final String indexes = "records,attr";
        DataStore gs = connect.getGeoDataStore();
        try
        {
            SimpleFeatureType entityschema = gs.getSchema(featurename);
            if (entityschema != null)
                return;
        }
        catch (IOException e1)
        {
            LOGGER.error(e1.getMessage(), e1);

        }
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName(featurename);

        tb.add(ENTITY_KEY_COLUMN_NAME, getTypeForSimpleFeature(ECoalesceFieldDataTypes.STRING_TYPE));
        tb.add(ENTITY_NAME_COLUMN_NAME, getTypeForSimpleFeature(ECoalesceFieldDataTypes.STRING_TYPE));
        tb.add(ENTITY_SOURCE_COLUMN_NAME, getTypeForSimpleFeature(ECoalesceFieldDataTypes.STRING_TYPE));
        tb.add(ENTITY_VERSION_COLUMN_NAME, getTypeForSimpleFeature(ECoalesceFieldDataTypes.STRING_TYPE));
        tb.add(ENTITY_ID_COLUMN_NAME, getTypeForSimpleFeature(ECoalesceFieldDataTypes.STRING_TYPE));
        tb.add(ENTITY_ID_TYPE_COLUMN_NAME, getTypeForSimpleFeature(ECoalesceFieldDataTypes.STRING_TYPE));
        tb.add(ENTITY_DATE_CREATED_COLUMN_NAME, getTypeForSimpleFeature(ECoalesceFieldDataTypes.DATE_TIME_TYPE));
        tb.add(ENTITY_LAST_MODIFIED_COLUMN_NAME, getTypeForSimpleFeature(ECoalesceFieldDataTypes.DATE_TIME_TYPE));
        tb.add(ENTITY_TITLE_COLUMN_NAME, getTypeForSimpleFeature(ECoalesceFieldDataTypes.STRING_TYPE));
        tb.add(ENTITY_DELETED_COLUMN_NAME, getTypeForSimpleFeature(ECoalesceFieldDataTypes.BOOLEAN_TYPE));
        tb.add(ENTITY_SCOPE_COLUMN_NAME, getTypeForSimpleFeature(ECoalesceFieldDataTypes.STRING_TYPE));
        tb.add(ENTITY_CREATOR_COLUMN_NAME, getTypeForSimpleFeature(ECoalesceFieldDataTypes.STRING_TYPE));
        tb.add(ENTITY_TYPE_COLUMN_NAME, getTypeForSimpleFeature(ECoalesceFieldDataTypes.STRING_TYPE));
        //tb.add(DEFAULT_GEO_FIELD_NAME, Polygon.class);
        //tb.setDefaultGeometry(DEFAULT_GEO_FIELD_NAME);

        SimpleFeatureType feature = tb.buildFeatureType();
        feature.getUserData().put(geomesaTimeIndex, ENTITY_LAST_MODIFIED_COLUMN_NAME);

        // index recordkey, cardinality is high because there is only one record per key.
        feature.getDescriptor(ENTITY_KEY_COLUMN_NAME).getUserData().put("index", "full");
        feature.getDescriptor(ENTITY_KEY_COLUMN_NAME).getUserData().put("cardinality", "high");
        feature.getDescriptor(ENTITY_NAME_COLUMN_NAME).getUserData().put("index", "join");
        feature.getDescriptor(ENTITY_NAME_COLUMN_NAME).getUserData().put("cardinality", "low");
        feature.getDescriptor(ENTITY_SOURCE_COLUMN_NAME).getUserData().put("index", "join");
        feature.getDescriptor(ENTITY_SOURCE_COLUMN_NAME).getUserData().put("cardinality", "low");
        feature.getDescriptor(ENTITY_VERSION_COLUMN_NAME).getUserData().put("index", "join");
        feature.getDescriptor(ENTITY_VERSION_COLUMN_NAME).getUserData().put("cardinality", "low");
        feature.getDescriptor(ENTITY_DELETED_COLUMN_NAME).getUserData().put("index", "join");
        feature.getDescriptor(ENTITY_DELETED_COLUMN_NAME).getUserData().put("cardinality", "low");
        //feature.getDescriptor(LINKAGE_LINK_TYPE_COLUMN_NAME).getUserData().put("index", "join");
        //feature.getDescriptor(LINKAGE_LINK_TYPE_COLUMN_NAME).getUserData().put("cardinality", "low");
        feature.getUserData().put(Hints.USE_PROVIDED_FID, true);
        feature.getUserData().put("geomesa.indexes.enabled", indexes);

        try
        {
            LOGGER.debug("Creating Feature for {} ", featurename);

            gs.createSchema(feature);

        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void persistEntitySearch(DataStore gds,
                                     CoalesceEntity entity,
                                     Map<String, DefaultFeatureCollection> featureCollectionMap)
            throws CQLException, CoalesceDataFormatException, IOException
    {
        DefaultFeatureCollection featurecollection = featureCollectionMap.get(ENTITY_FEATURE_NAME);

        if (featurecollection == null)
        {
            featurecollection = new DefaultFeatureCollection();
            featureCollectionMap.put(ENTITY_FEATURE_NAME, featurecollection);
        }

        Boolean updated = updateEntityIfExists(ENTITY_FEATURE_NAME, entity);

        if (!updated)
        {
            SimpleFeatureType featuretype = gds.getSchema(ENTITY_FEATURE_NAME);
            SimpleFeature simplefeature = SimpleFeatureBuilder.build(featuretype, new Object[] {}, entity.getKey());
            simplefeature.getUserData().put(Hints.USE_PROVIDED_FID, true);

            setFeatureAttribute(simplefeature, ENTITY_KEY_COLUMN_NAME, ECoalesceFieldDataTypes.STRING_TYPE, entity.getKey());
            setFeatureAttribute(simplefeature,
                                ENTITY_NAME_COLUMN_NAME,
                                ECoalesceFieldDataTypes.STRING_TYPE,
                                entity.getName());
            setFeatureAttribute(simplefeature,
                                ENTITY_SOURCE_COLUMN_NAME,
                                ECoalesceFieldDataTypes.STRING_TYPE,
                                entity.getSource());
            setFeatureAttribute(simplefeature,
                                ENTITY_TITLE_COLUMN_NAME,
                                ECoalesceFieldDataTypes.STRING_TYPE,
                                entity.getTitle());
            setFeatureAttribute(simplefeature,
                                ENTITY_VERSION_COLUMN_NAME,
                                ECoalesceFieldDataTypes.STRING_TYPE,
                                entity.getVersion());
            setFeatureAttribute(simplefeature,
                                ENTITY_ID_COLUMN_NAME,
                                ECoalesceFieldDataTypes.STRING_TYPE,
                                entity.getEntityId());
            setFeatureAttribute(simplefeature,
                                ENTITY_ID_TYPE_COLUMN_NAME,
                                ECoalesceFieldDataTypes.STRING_TYPE,
                                entity.getEntityIdType());
            setFeatureAttribute(simplefeature,
                                ENTITY_DATE_CREATED_COLUMN_NAME,
                                ECoalesceFieldDataTypes.DATE_TIME_TYPE,
                                entity.getDateCreated());
            setFeatureAttribute(simplefeature,
                                ENTITY_LAST_MODIFIED_COLUMN_NAME,
                                ECoalesceFieldDataTypes.DATE_TIME_TYPE,
                                entity.getLastModified());
            setFeatureAttribute(simplefeature,
                                ENTITY_DELETED_COLUMN_NAME,
                                ECoalesceFieldDataTypes.BOOLEAN_TYPE,
                                entity.isMarkedDeleted());
            // No use storing always null values
            //                setFeatureAttribute(simplefeature,
            //                		ENTITY_SCOPE_COLUMN_NAME,
            //                        ECoalesceFieldDataTypes.STRING_TYPE,
            //                        null);
            //                setFeatureAttribute(simplefeature,
            //                		ENTITY_CREATOR_COLUMN_NAME,
            //                        ECoalesceFieldDataTypes.STRING_TYPE,
            //                        null);
            //                setFeatureAttribute(simplefeature,
            //                		ENTITY_TYPE_COLUMN_NAME,
            //                        ECoalesceFieldDataTypes.STRING_TYPE,
            //                        null);

            featurecollection.add(simplefeature);

        }

    }

    private boolean updateEntityIfExists(String featuresetname, CoalesceEntity entity)
            throws IOException, CQLException, CoalesceDataFormatException
    {
        boolean updated = false;
        DataStore store = connect.getGeoDataStore();
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

        FeatureId fid = ff.featureId(entity.getKey());

        Filter filter = ff.id(Collections.singleton(fid));

        // Need to escape the fully qualified column in the feature set for filters
        //Filter filter = CQL.toFilter("\""+featuresetname+".recordKey\" =" + "'" + record.getKey() + "'");

        //SimpleFeatureCollection collection = store.getFeatures(filter);
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = store.getFeatureWriter(featuresetname,
                                                                                        filter,
                                                                                        Transaction.AUTO_COMMIT);

        if (writer.hasNext())
        {

            SimpleFeature toModify = writer.next();

            DateTime lm = new DateTime(toModify.getAttribute(ENTITY_LAST_MODIFIED_COLUMN_NAME));
            // IF the lastModified of this link is less than or equal to the one
            // stored nothing has changed so don't update but return true so we don't
            // persist another copy
            if (!lm.isBefore(entity.getLastModified()))
            {
                writer.close();
                return true;
            }
            final String entityfields[] = { ENTITY_KEY_COLUMN_NAME, ENTITY_NAME_COLUMN_NAME, ENTITY_SOURCE_COLUMN_NAME,
                                            ENTITY_TITLE_COLUMN_NAME, ENTITY_VERSION_COLUMN_NAME, ENTITY_ID_COLUMN_NAME,
                                            ENTITY_ID_TYPE_COLUMN_NAME, ENTITY_DATE_CREATED_COLUMN_NAME,
                                            ENTITY_LAST_MODIFIED_COLUMN_NAME, ENTITY_DELETED_COLUMN_NAME
            };

            Object[] values = new Object[] { entity.getKey(), entity.getName(), entity.getSource(), entity.getTitle(),
                                             entity.getVersion(), entity.getEntityId(), entity.getEntityIdType(),
                                             entity.getDateCreated(), entity.getLastModified(), entity.isMarkedDeleted()

            };
            for (int i = 0; i < entityfields.length; i++)
            {
                toModify.setAttribute(entityfields[i], values[i]);
            }

            writer.write();
            updated = true;

        }
        writer.close();

        return updated;
    }

    private void deleteEntitySearch(CoalesceEntity entity) throws IOException, CQLException, CoalesceDataFormatException
    {
        DataStore store = connect.getGeoDataStore();
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        SimpleFeatureStore featureStore = (SimpleFeatureStore) store.getFeatureSource(ENTITY_FEATURE_NAME);
        Filter filter = ff.equals(ff.property(ENTITY_KEY_COLUMN_NAME), ff.literal(entity.getKey()));

        featureStore.removeFeatures(filter);

    }

}
