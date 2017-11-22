package com.incadencecorp.coalesce.framework.persistance.elasticsearch;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.joda.time.DateTime;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.incadencecorp.coalesce.api.persistance.EPersistorCapabilities;
import com.incadencecorp.coalesce.common.exceptions.CoalesceException;
import com.incadencecorp.coalesce.common.exceptions.CoalescePersistorException;
import com.incadencecorp.coalesce.common.helpers.CoalesceTableHelper;
import com.incadencecorp.coalesce.common.helpers.JodaDateTimeHelper;
import com.incadencecorp.coalesce.framework.CoalesceSettings;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceEntity;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceEntityTemplate;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceField;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceFieldDefinition;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceFieldHistory;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceLinkage;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceLinkageSection;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceObject;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceRecord;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceRecordset;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceSection;
import com.incadencecorp.coalesce.framework.datamodel.ECoalesceFieldDataTypes;
import com.incadencecorp.coalesce.framework.exim.impl.JsonFullEximImpl;
import com.incadencecorp.coalesce.framework.persistance.CoalesceDataConnectorBase;
import com.incadencecorp.coalesce.framework.persistance.CoalesceParameter;
import com.incadencecorp.coalesce.framework.persistance.CoalescePersistorBase;
import com.incadencecorp.coalesce.framework.persistance.ElementMetaData;
import com.incadencecorp.coalesce.framework.persistance.EntityMetaData;
import com.incadencecorp.coalesce.framework.persistance.ObjectMetaData;

/*-----------------------------------------------------------------------------'
 Copyright 2017 - InCadence Strategic Solutions Inc., All Rights Reserved

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
 * This persister is for a ElasticSearch database.
 *
 * @author n78554
 */
public class ElasticSearchPersistor extends CoalescePersistorBase {

    /*--------------------------------------------------------------------------
    Private Members
    --------------------------------------------------------------------------*/

    private String _schema;

    /*--------------------------------------------------------------------------
    Overrided Functions
    --------------------------------------------------------------------------*/

    /**
     * Set the schema to use when making database calls.
     *
     * @param schema
     */
    public void setSchema(String schema)
    {
        _schema = schema;
    }

    protected String getSchemaPrefix()
    {
        if (_schema != null)
        {
            return _schema + ".";
        }
        else
        {
            return "";
        }
    }

    protected String getSchema()
    {
        return _schema;
    }
    
    public class Views {
        public class Entity {
        }
    }

    @Override
    public List<String> getCoalesceEntityKeysForEntityId(String entityId,
                                                         String entityIdType,
                                                         String entityName,
                                                         String entitySource) throws CoalescePersistorException
    {
        try
        {
            if (entitySource != null && entitySource != "")
            {
                return getCoalesceEntityKeysForEntityIdAndSource(entityId, entityIdType, entityName, entitySource);
            }
            else
            {
                return this.getCoalesceEntityKeysForEntityId(entityId, entityIdType, entityName);
            }
        }
        catch (SQLException e)
        {
            throw new CoalescePersistorException("GetCoalesceEntityKeysForEntityId", e);
        }
        catch (Exception e)
        {
            throw new CoalescePersistorException("GetCoalesceEntityKeysForEntityId", e);
        }
    }

    @Override
    public EntityMetaData getCoalesceEntityIdAndTypeForKey(String key) throws CoalescePersistorException
    {
        try (CoalesceDataConnectorBase conn = new ElasticSearchDataConnector(getSchemaPrefix()))
        {
            return this.getCoalesceEntityIdAndTypeForKey(key, conn);
        }
        catch (SQLException e)
        {
            throw new CoalescePersistorException("GetCoalesceEntityIdAndTypeForKey", e);
        }
        catch (Exception e)
        {
            throw new CoalescePersistorException("GetCoalesceEntityIdAndTypeForKey", e);
        }
    }

    @Override
    public DateTime getCoalesceObjectLastModified(String key, String objectType) throws CoalescePersistorException
    {
        try (ElasticSearchDataConnector conn = new ElasticSearchDataConnector(getSchemaPrefix()))
        {
            return this.getCoalesceObjectLastModified(key, objectType, conn.getDBConnector());
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
    public byte[] getBinaryArray(String key) throws CoalescePersistorException
    {
        try (CoalesceDataConnectorBase conn = new ElasticSearchDataConnector(getSchemaPrefix()))
        {

            byte[] binaryArray = null;

            ResultSet results = conn.executeQuery("SELECT BinaryObject FROM " + getSchemaPrefix()
                    + "CoalesceFieldBinaryData WHERE ObjectKey=?", new CoalesceParameter(key, Types.OTHER));

            // Get Results
            if (results != null && results.first())
            {
                Blob dataVal = results.getBlob("BinaryObject");
                binaryArray = dataVal.getBytes(1, (int) dataVal.length());
            }

            return binaryArray;
        }
        catch (SQLException e)
        {
            throw new CoalescePersistorException("GetBinaryArray", e);
        }
        catch (Exception e)
        {
            throw new CoalescePersistorException("GetBinaryArray", e);
        }
    }

    @Override
    public void saveTemplate(CoalesceDataConnectorBase conn, CoalesceEntityTemplate... templates)
            throws CoalescePersistorException
    {
        try
        {
            for (CoalesceEntityTemplate template : templates)
            {
                // Always persist template
                conn.executeProcedure("CoalesceEntityTemplate_InsertOrUpdate",
                                      new CoalesceParameter(template.getKey(), Types.OTHER),
                                      new CoalesceParameter(template.getName()),
                                      new CoalesceParameter(template.getSource()),
                                      new CoalesceParameter(template.getVersion()),
                                      new CoalesceParameter(template.toXml()),
                                      new CoalesceParameter(JodaDateTimeHelper.nowInUtc().toString(), Types.OTHER),
                                      new CoalesceParameter(JodaDateTimeHelper.nowInUtc().toString(), Types.OTHER));
            }
        }
        catch (Exception e)
        {
            throw new CoalescePersistorException("PersistEntityTemplate", e);
        }

    }

    @Override
    public ElementMetaData getXPath(String key, String objectType) throws CoalescePersistorException
    {
        try (CoalesceDataConnectorBase conn = new ElasticSearchDataConnector(getSchemaPrefix()))
        {
            return getXPathRecursive(key, objectType, "", conn);
        }
        catch (SQLException e)
        {
            throw new CoalescePersistorException("GetXPath", e);
        }
        catch (Exception e)
        {
            throw new CoalescePersistorException("GetXPath", e);
        }
    }

    @Override
    public String getFieldValue(String fieldKey) throws CoalescePersistorException
    {
        try (CoalesceDataConnectorBase conn = new ElasticSearchDataConnector(getSchemaPrefix()))
        {
            String value = null;

            ResultSet results = conn.executeQuery("SELECT value FROM " + getSchemaPrefix()
                    + "CoalesceField WHERE ObjectKey =?", new CoalesceParameter(fieldKey, Types.OTHER));

            while (results.next())
            {
                value = results.getString("value");
            }

            return value;
        }
        catch (SQLException e)
        {
            throw new CoalescePersistorException("GetFieldValue", e);
        }
        catch (Exception e)
        {
            throw new CoalescePersistorException("GetFieldValue", e);
        }
    }

    @Override
    public String[] getEntityXml(String... keys) throws CoalescePersistorException
    {
        try (CoalesceDataConnectorBase conn = new ElasticSearchDataConnector(getSchemaPrefix()))
        {
            List<String> xmlList = new ArrayList<String>();
            List<CoalesceParameter> parameters = new ArrayList<CoalesceParameter>();

            StringBuilder sb = new StringBuilder("");

            for (String key : keys)
            {
                if (sb.length() > 0)
                {
                    sb.append(",");
                }

                sb.append("?");
                parameters.add(new CoalesceParameter(key, Types.OTHER));
            }

            String SQL = String.format("SELECT EntityXml FROM %sCoalesceEntity WHERE ObjectKey IN (%s)",
                                       getSchemaPrefix(),
                                       sb.toString());

            ResultSet results = conn.executeQuery(SQL, parameters.toArray(new CoalesceParameter[parameters.size()]));

            while (results.next())
            {
                xmlList.add(results.getString("EntityXml"));
            }

            return xmlList.toArray(new String[xmlList.size()]);
        }
        catch (SQLException e)
        {
            throw new CoalescePersistorException("GetEntityXml", e);
        }
        catch (Exception e)
        {
            throw new CoalescePersistorException("GetEntityXml", e);
        }
    }

    @Override
    public String getEntityXml(String entityId, String entityIdType) throws CoalescePersistorException
    {
        try (CoalesceDataConnectorBase conn = new ElasticSearchDataConnector(getSchemaPrefix()))
        {
            String value = null;

            ResultSet results = conn.executeQuery("SELECT EntityXml FROM " + getSchemaPrefix()
                                                          + "CoalesceEntity WHERE EntityId=? AND EntityIdType=?",
                                                  new CoalesceParameter(entityId),
                                                  new CoalesceParameter(entityIdType));

            while (results.next())
            {
                value = results.getString("EntityXml");
            }

            return value;
        }
        catch (SQLException e)
        {
            throw new CoalescePersistorException("GetEntityXml", e);
        }
        catch (Exception e)
        {
            throw new CoalescePersistorException("GetEntityXml", e);
        }
    }

    @Override
    public String getEntityXml(String name, String entityId, String entityIdType) throws CoalescePersistorException
    {
        try (CoalesceDataConnectorBase conn = new ElasticSearchDataConnector(getSchemaPrefix()))
        {
            String value = null;

            ResultSet results = conn.executeQuery("SELECT EntityXml FROM " + getSchemaPrefix()
                                                          + "CoalesceEntity WHERE Name=? AND EntityId=? AND EntityIdType=?",
                                                  new CoalesceParameter(name),
                                                  new CoalesceParameter(entityId),
                                                  new CoalesceParameter(entityIdType));

            while (results.next())
            {
                value = results.getString("EntityXml");
            }

            return value;
        }
        catch (SQLException e)
        {
            throw new CoalescePersistorException("GetEntityXml", e);
        }
        catch (Exception e)
        {
            throw new CoalescePersistorException("GetEntityXml", e);
        }
    }

    @Override
    protected CoalesceDataConnectorBase getDataConnector() throws CoalescePersistorException
    {
        return new ElasticSearchDataConnector(getSchemaPrefix());
    }

    /*
     * @Override public String[] GetEntityXmlAsStrings(String EntityId, String
     * EntityIdType) { String[] crst = null; try { String sqlStmt =
     * "SELECT ObjectKey from CoalesceEntity WHERE EntityId=?"; sqlStmt +=
     * " AND EntityIdType=?"; ArrayList<String> params = new ArrayList<>();
     * params.add(EntityId.trim()); params.add(EntityIdType.trim()); crst =
     * getEntityKeysFromSQL(sqlStmt, params); return crst; } catch (Exception
     * ex) { CallResult.log(CallResults.FAILED_ERROR, ex, "getEntityKeys");
     * return crst; } } private XsdEntity getXSDEntityXML(String sqlStmt,
     * ArrayList<String> sqlParams) throws Exception { XsdEntity crst = new
     * XsdEntity(); Connection conn = null; Statement stx = null; try {
     * Class.forName("com.mysql.jdbc.Driver"); conn =
     * DriverManager.getConnection(serCon.getURL(), serCon.getUser(),
     * serCon.getPassword()); stx = conn.createStatement();
     * java.sql.PreparedStatement sql; sql = conn.prepareStatement(sqlStmt);
     * Object[] pRams = sqlParams.toArray(); for (int iSetter = 0; iSetter <
     * sqlParams.size(); iSetter++) { sql.setString(iSetter + 1,
     * pRams[iSetter].toString()); } ResultSet srs = sql.executeQuery();
     * ResultSetMetaData rsmd = srs.getMetaData(); if (rsmd.getColumnCount() <=
     * 1) { while (srs.next()) { crst.Initialize(srs.getString("EntityXml")); }
     * } return crst; } finally { stx.close(); conn.close(); } }
     */

    @Override
    protected boolean flattenObject(boolean allowRemoval, CoalesceEntity... entities) throws CoalescePersistorException
    {
        boolean isSuccessful = true;

        try (ElasticSearchDataConnector conn = new ElasticSearchDataConnector(getSchemaPrefix()))
        {

            // Create a Database Connection
            try
            {
               TransportClient client = conn.getDBConnector();

                for (CoalesceEntity entity : entities)
                {
                    // Persist (Recursively)
                    isSuccessful &= updateCoalesceObject(entity, client, allowRemoval);
                }

                conn.commit();
            }
            catch (SQLException e)
            {
                conn.rollback();

                throw new CoalescePersistorException("FlattenObject: " + e.getMessage(), e);
            }

        }

        return isSuccessful;
    }

    @Override
    protected boolean flattenCore(boolean allowRemoval, CoalesceEntity... entities) throws CoalescePersistorException
    {
        boolean isSuccessful = false;

        ElasticSearchDataConnector conn = null;

        // Create a Database Connection
        try
        {
            conn = new ElasticSearchDataConnector(getSchemaPrefix());
            conn.openConnection(false);

            for (CoalesceEntity entity : entities)
            {
                if (persistEntityObject(entity, conn.getDBConnector()))
                {
                    isSuccessful = true;
                }
            }

            conn.getConnection().commit();
        }
        catch (Exception e)
        {
            conn.rollback();

            throw new CoalescePersistorException("FlattenObject: " + e.getMessage(), e);
        }
        finally
        {
            conn.close();
        }

        return isSuccessful;
    }

    @Override
    public String getEntityTemplateKey(String name, String source, String version) throws CoalescePersistorException
    {
        try (CoalesceDataConnectorBase conn = new ElasticSearchDataConnector(getSchemaPrefix()))
        {
            String value = null;

            ResultSet results = conn.executeQuery("SELECT TemplateKey FROM " + getSchemaPrefix()
                                                          + "CoalesceEntityTemplate WHERE Name=? and Source=? and Version=?",
                                                  new CoalesceParameter(name),
                                                  new CoalesceParameter(source),
                                                  new CoalesceParameter(version));

            while (results.next())
            {
                value = results.getString("TemplateKey");
            }

            return value;
        }
        catch (SQLException e)
        {
            throw new CoalescePersistorException("GetEntityTemplateKey", e);
        }
        catch (Exception e)
        {
            throw new CoalescePersistorException("GetEntityTemplateKey", e);
        }
    }

    @Override
    public List<ObjectMetaData> getEntityTemplateMetadata() throws CoalescePersistorException
    {
        try (CoalesceDataConnectorBase conn = new ElasticSearchDataConnector(getSchemaPrefix()))
        {
            return conn.getTemplateMetaData("SELECT * FROM " + getSchemaPrefix() + "CoalesceEntityTemplate");
        }
        catch (Exception ex)
        {
            throw new CoalescePersistorException("getEntityTemplateMetadata", ex);
        }
    }

    @Override
    public String getEntityTemplateXml(String key) throws CoalescePersistorException
    {
        try (CoalesceDataConnectorBase conn = new ElasticSearchDataConnector(getSchemaPrefix()))
        {
            String value = null;

            ResultSet results = conn.executeQuery("SELECT TemplateXml FROM " + getSchemaPrefix()
                    + "CoalesceEntityTemplate WHERE TemplateKey=?", new CoalesceParameter(key, Types.OTHER));

            while (results.next())
            {
                value = results.getString("TemplateXml");
            }

            return value;
        }
        catch (SQLException e)
        {
            throw new CoalescePersistorException("GetEntityTemplateXml", e);
        }
        catch (Exception e)
        {
            throw new CoalescePersistorException("GetEntityTemplateXml", e);
        }
    }

    @Override
    public String getEntityTemplateXml(String name, String source, String version) throws CoalescePersistorException
    {
        try (CoalesceDataConnectorBase conn = new ElasticSearchDataConnector(getSchemaPrefix()))
        {
            String value = null;

            ResultSet results = conn.executeQuery("SELECT TemplateXml FROM " + getSchemaPrefix()
                                                          + "CoalesceEntityTemplate WHERE Name=? and Source=? and Version=?",
                                                  new CoalesceParameter(name),
                                                  new CoalesceParameter(source),
                                                  new CoalesceParameter(version));

            while (results.next())
            {
                value = results.getString("TemplateXml");
            }

            return value;
        }
        catch (SQLException e)
        {
            throw new CoalescePersistorException("GetEntityTemplateXml", e);
        }
        catch (Exception e)
        {
            throw new CoalescePersistorException("GetEntityTemplateXml", e);
        }
    }

    /*--------------------------------------------------------------------------
    Protected Functions
    --------------------------------------------------------------------------*/
    /**
     * Adds or Updates a Coalesce object that matches the given parameters.
     *
     * @param coalesceObject the Coalesce object to be added or updated
     * @param conn is the PostGresDataConnector database connection
     * @return isSuccessful = True = Successful add/update operation.
     * @throws SQLException
     */
    protected boolean persistObject(CoalesceObject coalesceObject, TransportClient conn) throws SQLException
    {
        boolean isSuccessful = true;

        switch (coalesceObject.getType()) {
        case "entity":

            // isSuccessful = checkLastModified(coalesceObject, conn);
            isSuccessful = persistEntityObject((CoalesceEntity) coalesceObject, conn);
            break;

        default:
            isSuccessful = false;
        }

        if (isSuccessful && CoalesceSettings.getUseIndexing())
        {
            // Persist Map Table Entry
            isSuccessful = persistMapTableEntry(coalesceObject, conn);
        }

        return isSuccessful;
    }

    /**
     * Adds or updates map table entry for a given element.
     *
     * @param coalesceObject the Coalesce object to be added or updated
     * @param conn is the SQLServerDataConnector database connection
     * @return True if successfully added/updated.
     * @throws SQLException
     */
    protected boolean persistMapTableEntry(CoalesceObject coalesceObject, TransportClient conn)
            throws SQLException
    {
        return true;
        // String parentKey;
        // String parentType;
        //
        // if (coalesceObject.getParent() != null)
        // {
        // parentKey = coalesceObject.getParent().getKey();
        // parentType = coalesceObject.getParent().getType();
        // }
        // else
        // {
        // parentKey = "00000000-0000-0000-0000-000000000000";
        // parentType = "";
        // }
        //
        // return conn.executeProcedure("CoalesceObjectMap_Insert",
        // new CoalesceParameter(parentKey, Types.OTHER),
        // new CoalesceParameter(parentType),
        // new CoalesceParameter(coalesceObject.getKey(), Types.OTHER),
        // new CoalesceParameter(coalesceObject.getType()));
    }

    /**
     * Adds or Updates a Coalesce entity that matches the given parameters.
     *
     * @param entity the XsdEntity to be added or updated
     * @param conn is the PostGresDataConnector database connection
     * @return True = No Update required.
     * @throws SQLException
     */
    protected boolean persistEntityObject(CoalesceEntity entity, TransportClient conn) throws SQLException
    {
        // Return true if no update is required.
    	//Worry about this later. Just gotta get this working
//        if (!checkLastModified(entity, conn))
//        {
//            return true;
//        }

        JsonFullEximImpl converter = new JsonFullEximImpl();
 
        IndexResponse response;
		try {
			ObjectMapper mapper = new ObjectMapper();
			TypeFactory typeFactory = mapper.getTypeFactory();
			MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, Object.class);
			HashMap<String, Object> map = mapper.readValue(converter.exportValues(entity, true).toString(), mapType);

			// convert JSON string to Map
			response = conn.prepareIndex(entity.getName(), entity.getType(), "1").setSource(map).get();
 
			System.out.println(response.toString());
		} catch (CoalesceException e) {
			e.printStackTrace();
			return  false;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//If the index response is returned and no exception was thrown, the index operation was successful
        return true;
    }

    /*--------------------------------------------------------------------------
    Protected Methods
    --------------------------------------------------------------------------*/

    protected String getCreator(CoalesceEntity entity)
    {
        return null;
    }

    protected String getType(CoalesceEntity entity)
    {
        return null;
    }

    protected String getScope(CoalesceEntity entity)
    {
        return null;
    }

    /**
     * 
     * @param entity
     * @return additional rows that have been registered for your implementaion.
     */
    protected List<CoalesceParameter> getExtendedParameters(CoalesceEntity entity)
    {
        return new ArrayList<CoalesceParameter>();
    }

    /**
     * Returns the EntityMetaData for the Coalesce entity that matches the given
     * parameters.
     *
     * @param key primary key of the Coalesce entity
     * @param conn is the PostGresDataConnector database connection
     * @return metaData the EntityMetaData for the Coalesce entity.
     * @throws SQLException
     */
    protected EntityMetaData getCoalesceEntityIdAndTypeForKey(String key, CoalesceDataConnectorBase conn)
            throws SQLException
    {
        EntityMetaData metaData = null;

        // Execute Query
        ResultSet results = conn.executeQuery("SELECT EntityId,EntityIdType,ObjectKey FROM " + getSchemaPrefix()
                + "CoalesceEntity WHERE ObjectKey=?", new CoalesceParameter(key, Types.OTHER));
        // Get Results
        while (results.next())
        {
            metaData = new EntityMetaData(results.getString("EntityId"),
                                          results.getString("EntityIdType"),
                                          results.getString("ObjectKey"));
        }

        return metaData;
    }

    /**
     * Returns the comparison for the Coalesce object last modified date versus
     * the same objects value in the database.
     *
     * @param coalesceObject the Coalesce object to have it's last modified date
     *            checked.
     * @param conn is the PostGresDataConnector database connection
     * @return False = Out of Date
     * @throws SQLException
     */
    protected boolean checkLastModified(CoalesceObject coalesceObject, TransportClient conn) throws SQLException
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

    /**
     * Deletes the Coalesce object and CoalesceObjectMap that matches the given
     * parameters.
     *
     * @param coalesceObject the Coalesce object to be deleted
     * @param conn is the PostGresDataConnector database connection
     * @return True = Successful delete
     * @throws SQLException
     */
    protected boolean deleteObject(CoalesceObject coalesceObject, CoalesceDataConnectorBase conn) throws SQLException
    {
        String objectType = coalesceObject.getType();
        String objectKey = coalesceObject.getKey();
        String tableName = CoalesceTableHelper.getTableNameForObjectType(objectType);

        conn.executeUpdate("DELETE FROM " + getSchemaPrefix() + "CoalesceObjectMap WHERE ObjectKey=?",
                           new CoalesceParameter(objectKey, Types.OTHER));
        conn.executeUpdate("DELETE FROM " + getSchemaPrefix() + tableName + " WHERE ObjectKey=?",
                           new CoalesceParameter(objectKey, Types.OTHER));

        return true;
    }

    /**
     * Returns the Coalesce entity keys that matches the given parameters.
     *
     * @param entityId of the entity.
     * @param entityIdType of the entity.
     * @param entityName of the entity.
     * @return List<String> of primary keys for the matching Coalesce entity.
     * @throws SQLException ,Exception,CoalescePersistorException
     */
    private List<String> getCoalesceEntityKeysForEntityId(String entityId, String entityIdType, String entityName)
            throws Exception
    {
        List<String> keyList = new ArrayList<String>();

        try (ElasticSearchDataConnector conn = new ElasticSearchDataConnector(getSchemaPrefix()))
        {
        	TransportClient client = conn.getDBConnector();
        	GetResponse response = client.prepareGet(entityId, entityIdType, entityName).get();
        	
        	keyList.add(response.getId());

            return keyList;
        }

    }

    /**
     * Returns the Coalesce entity keys that matches the given parameters.
     *
     * @param entityId of the entity.
     * @param entityIdType of the entity.
     * @param entityName of the entity.
     * @param entitySource of the entity.
     * @return List<String> of primary keys for the matching Coalesce entity.
     * @throws SQLException ,Exception,CoalescePersistorException
     */
    private List<String> getCoalesceEntityKeysForEntityIdAndSource(String entityId,
                                                                   String entityIdType,
                                                                   String entityName,
                                                                   String entitySource) throws Exception
    {

        try (CoalesceDataConnectorBase conn = new ElasticSearchDataConnector(getSchemaPrefix()))
        {
            List<String> keyList = new ArrayList<String>();

            ResultSet results = conn.executeLikeQuery("SELECT ObjectKey FROM "
                                                              + getSchemaPrefix()
                                                              + "CoalesceEntity WHERE (EntityId like ? ) AND (EntityIdType like  ? ) AND (Name=?) AND (Source=?)",
                                                      2,
                                                      new CoalesceParameter(entityId),
                                                      new CoalesceParameter(entityIdType),
                                                      new CoalesceParameter(entityName),
                                                      new CoalesceParameter(entitySource));

            while (results.next())
            {
                keyList.add(results.getString("ObjectKey"));
            }

            return keyList;
        }
    }

    private boolean updateCoalesceObject(CoalesceObject coalesceObject, TransportClient conn, boolean allowRemoval)
            throws SQLException

    {
        boolean isSuccessful = false;

        if (coalesceObject.isFlatten())
        {
            isSuccessful = persistObject(coalesceObject, conn);

            // Successful?
            if (isSuccessful)
            {
                // Yes; Iterate Through Children
                for (CoalesceObject childObject : coalesceObject.getChildCoalesceObjects().values())
                {
                    updateCoalesceObject(childObject, conn, allowRemoval);
                }
            }
        }
        return isSuccessful;
    }

    private DateTime getCoalesceObjectLastModified(String key, String objectType, TransportClient conn)
            throws SQLException
    {
        DateTime lastModified = null;

        //TODO: Figure out how to do this query in ElasticSearch
        /*
        // Determine the Table Name
        String tableName = CoalesceTableHelper.getTableNameForObjectType(objectType);
        String dateValue = null;

        ResultSet results = conn.executeQuery("SELECT LastModified FROM " + getSchemaPrefix() + tableName
                + " WHERE ObjectKey=?", new CoalesceParameter(key.trim(), Types.OTHER));
        ResultSetMetaData resultsmd = results.getMetaData();

        // JODA Function DateTimeFormat will adjust for the Server timezone when
        // converting the time.
        if (resultsmd.getColumnCount() <= 1)
        {
            while (results.next())
            {
                dateValue = results.getString("LastModified");
                if (dateValue != null)
                {
                    lastModified = JodaDateTimeHelper.getPostGresDateTim(dateValue);
                }
            }
        }
        */
        return JodaDateTimeHelper.nowInUtc();

    }

    private ElementMetaData getXPathRecursive(String key, String objectType, String xPath, CoalesceDataConnectorBase conn)
            throws SQLException
    {

        boolean isEntityTable = false;
        ElementMetaData meteData = null;

        String sql = "";

        // Get Table Name
        String tableName = CoalesceTableHelper.getTableNameForObjectType(objectType);

        // Check to see if its the Entity Table
        if (tableName.equals("CoalesceEntity"))
        {
            isEntityTable = true;
        }

        if (isEntityTable)
        {
            sql = "SELECT name FROM ".concat(getSchemaPrefix()).concat(tableName).concat(" WHERE ObjectKey=?");
        }
        else
        {
            sql = "SELECT name, ParentKey, ParentType FROM ".concat(getSchemaPrefix()).concat(tableName).concat(" WHERE ObjectKey=?");
        }

        ResultSet results = conn.executeQuery(sql, new CoalesceParameter(key.trim(), Types.OTHER));

        // Valid Results?
        while (results.next())
        {

            String name = results.getString("name");

            if (isEntityTable)
            {
                xPath = name + "/" + xPath;

                // Set Meta Data
                meteData = new ElementMetaData(key, xPath);
            }
            else
            {
                String parentKey = results.getString("ParentKey");
                String parentType = results.getString("ParentType");

                if (xPath == null || xPath == "")
                {
                    meteData = getXPathRecursive(parentKey, parentType, name, conn);
                }
                else
                {
                    meteData = getXPathRecursive(parentKey, parentType, name + "/" + xPath, conn);
                }
            }

        }

        return meteData;

    }

    /**
     * @return EnumSet of EPersistorCapabilities
     */
    @Override
    public EnumSet<EPersistorCapabilities> getCapabilities()
    {
        EnumSet<EPersistorCapabilities> enumSet = super.getCapabilities();
        EnumSet<EPersistorCapabilities> newCapabilities = EnumSet.of(EPersistorCapabilities.GET_FIELD_VALUE,
                                                                     EPersistorCapabilities.READ_TEMPLATES,
                                                                     EPersistorCapabilities.UPDATE,
                                                                     EPersistorCapabilities.DELETE,
                                                                     EPersistorCapabilities.SEARCH,
                                                                     EPersistorCapabilities.SUPPORTS_BLOB,
                                                                     EPersistorCapabilities.GEOSPATIAL_SEARCH,
                                                                     EPersistorCapabilities.INDEX_FIELDS);
        if (enumSet != null)
        {
            enumSet.addAll(newCapabilities);
        }
        else
        {
            enumSet = newCapabilities;
        }
        return enumSet;
    }
}
