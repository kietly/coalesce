package com.incadencecorp.coalesce.framework.persistance.mysql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.stereotype.Repository;

import com.incadencecorp.coalesce.common.exceptions.CoalescePersistorException;
import com.incadencecorp.coalesce.common.helpers.JodaDateTimeHelper;
import com.incadencecorp.coalesce.common.runtime.CoalesceSettings;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceDataObject;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceEntity;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceEntityTemplate;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceField;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceFieldDefinition;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceFieldHistory;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceLinkage;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceLinkageSection;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceRecord;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceRecordset;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceSection;
import com.incadencecorp.coalesce.framework.datamodel.ECoalesceDataObjectStatus;
import com.incadencecorp.coalesce.framework.datamodel.ECoalesceFieldDataTypes;
import com.incadencecorp.coalesce.framework.persistance.CoalesceParameter;
import com.incadencecorp.coalesce.framework.persistance.CoalescePersisterBase;
import com.incadencecorp.coalesce.framework.persistance.CoalesceTable;
import com.incadencecorp.coalesce.framework.persistance.ICoalesceCacher;
import com.incadencecorp.coalesce.framework.persistance.ServerConn;
import com.mysql.jdbc.Blob;

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

@Repository("MySQLPersistor")
public class MySQLPersistor extends CoalescePersisterBase {

    /*--------------------------------------------------------------------------
    Private Member Variables
    --------------------------------------------------------------------------*/

    private ServerConn serCon;

    /*--------------------------------------------------------------------------
    Constructor / Initializers
    --------------------------------------------------------------------------*/

    public MySQLPersistor()
    {
        serCon = new ServerConn();
    }

    /**
     * Sets the server connection.
     * 
     * @param server connection object.
     */
    public void initialize(ServerConn svConn)
    {
        serCon = svConn;
    }

    /**
     * Sets the cacher and server connection.
     * 
     * @param base class cacher.
     * @param server connection object.
     */
    public boolean initialize(ICoalesceCacher cacher, ServerConn svConn) throws CoalescePersistorException
    {
        serCon = svConn;

        return super.initialize(cacher);
    }

    /*--------------------------------------------------------------------------
    Overrided Functions
    --------------------------------------------------------------------------*/

    @Override
    public List<String> getCoalesceEntityKeysForEntityId(String EntityId,
                                                         String EntityIdType,
                                                         String EntityName,
                                                         String EntitySource) throws CoalescePersistorException
    {
        try
        {
            if (EntitySource != null && EntitySource != "")
            {
                return this.getCoalesceEntityKeysForEntityIdAndSource(EntityId, EntityIdType, EntityName, EntitySource);
            }
            else
            {
                return this.getCoalesceEntityKeysForEntityId(EntityId, EntityIdType, EntityName);
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
    public EntityMetaData getCoalesceEntityIdAndTypeForKey(String Key) throws CoalescePersistorException
    {
        try (MySQLDataConnector conn = new MySQLDataConnector(this.serCon))
        {
            return this.getCoalesceEntityIdAndTypeForKey(Key, conn);
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
    public DateTime getCoalesceDataObjectLastModified(String Key, String ObjectType) throws CoalescePersistorException
    {
        try (MySQLDataConnector conn = new MySQLDataConnector(this.serCon))
        {
            return this.getCoalesceDataObjectLastModified(Key, ObjectType, conn);
        }
        catch (SQLException e)
        {
            throw new CoalescePersistorException("GetCoalesceDataObjectLastModified", e);
        }
        catch (Exception e)
        {
            throw new CoalescePersistorException("GetCoalesceDataObjectLastModified", e);
        }
    }

    @Override
    public byte[] getBinaryArray(String key) throws CoalescePersistorException
    {
        try (MySQLDataConnector conn = new MySQLDataConnector(this.serCon))
        {

            byte[] binaryArray = null;

            ResultSet results = conn.executeQuery("SELECT BinaryObject FROM CoalesceFieldBinaryData WHERE ObjectKey=?",
                                                  new CoalesceParameter(key, Types.OTHER));

            // Get Results
            if (results != null && results.first())
            {
                Blob dataVal = (Blob) results.getBlob("BinaryObject");
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
    public boolean persistEntityTemplate(CoalesceEntityTemplate template) throws CoalescePersistorException
    {
        try (MySQLDataConnector conn = new MySQLDataConnector(this.serCon))
        {
            // Always persist template
            return conn.executeProcedure("CoalesceEntityTemplate_InsertOrUpdate",
                                         new CoalesceParameter(UUID.randomUUID().toString()),
                                         new CoalesceParameter(template.getName()),
                                         new CoalesceParameter(template.getSource()),
                                         new CoalesceParameter(template.getVersion()),
                                         new CoalesceParameter(template.toXml()),
                                         new CoalesceParameter(JodaDateTimeHelper.nowInUtc()),
                                         new CoalesceParameter(JodaDateTimeHelper.nowInUtc()));
        }
        catch (SQLException e)
        {
            throw new CoalescePersistorException("PersistEntityTemplate", e);
        }
        catch (Exception e)
        {
            throw new CoalescePersistorException("PersistEntityTemplate", e);
        }
    }

    @Override
    public ElementMetaData getXPath(String Key, String ObjectType) throws CoalescePersistorException
    {
        try (MySQLDataConnector conn = new MySQLDataConnector(this.serCon))
        {
            return this.getXPathRecursive(Key, ObjectType, "", conn);
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
    public String getFieldValue(String FieldKey) throws CoalescePersistorException
    {
        try (MySQLDataConnector conn = new MySQLDataConnector(this.serCon))
        {
            String value = null;

            ResultSet results = conn.executeQuery("SELECT value FROM CoalesceField WHERE ObjectKey =?",
                                                  new CoalesceParameter(FieldKey));

            if (results != null && results.first())
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
    public String getEntityXml(String Key) throws CoalescePersistorException
    {
        try (MySQLDataConnector conn = new MySQLDataConnector(this.serCon))
        {
            String value = null;

            ResultSet results = conn.executeQuery("SELECT EntityXml from CoalesceEntity WHERE ObjectKey=?",
                                                  new CoalesceParameter(Key));

            if (results != null && results.first())
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
    public String getEntityXml(String EntityId, String EntityIdType) throws CoalescePersistorException
    {
        try (MySQLDataConnector conn = new MySQLDataConnector(this.serCon))
        {
            String value = null;

            ResultSet results = conn.executeQuery("SELECT EntityXml from CoalesceEntity WHERE EntityId=? AND EntityIdType=?",
                                                  new CoalesceParameter(EntityId),
                                                  new CoalesceParameter(EntityIdType));

            if (results != null && results.first())
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
    public String getEntityXml(String Name, String EntityId, String EntityIdType) throws CoalescePersistorException
    {
        try (MySQLDataConnector conn = new MySQLDataConnector(this.serCon))
        {
            String value = null;

            ResultSet results = conn.executeQuery("SELECT EntityXml from CoalesceEntity WHERE Name=? AND EntityId=? AND EntityIdType=?",
                                                  new CoalesceParameter(Name),
                                                  new CoalesceParameter(EntityId),
                                                  new CoalesceParameter(EntityIdType));

            if (results != null && results.first())
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

    /*
     * @Override public String[] GetEntityXmlAsStrings(String EntityId, String EntityIdType) {
     * 
     * String[] crst = null; try { String sqlStmt = "SELECT ObjectKey from CoalesceEntity WHERE EntityId=?"; sqlStmt +=
     * " AND EntityIdType=?"; ArrayList<String> params = new ArrayList<>(); params.add(EntityId.trim());
     * params.add(EntityIdType.trim()); crst = getEntityKeysFromSQL(sqlStmt, params); return crst; } catch (Exception ex) {
     * CallResult.log(CallResults.FAILED_ERROR, ex, "getEntityKeys"); return crst; } }
     * 
     * private XsdEntity getXSDEntityXML(String sqlStmt, ArrayList<String> sqlParams) throws Exception { XsdEntity crst = new
     * XsdEntity(); Connection conn = null; Statement stx = null; try { Class.forName("com.mysql.jdbc.Driver"); conn =
     * DriverManager.getConnection(serCon.getURL(), serCon.getUser(), serCon.getPassword()); stx = conn.createStatement();
     * java.sql.PreparedStatement sql;
     * 
     * sql = conn.prepareStatement(sqlStmt); Object[] pRams = sqlParams.toArray();
     * 
     * for (int iSetter = 0; iSetter < sqlParams.size(); iSetter++) { sql.setString(iSetter + 1, pRams[iSetter].toString());
     * } ResultSet srs = sql.executeQuery(); ResultSetMetaData rsmd = srs.getMetaData(); if (rsmd.getColumnCount() <= 1) {
     * while (srs.next()) { crst.Initialize(srs.getString("EntityXml")); } } return crst; } finally { stx.close();
     * conn.close(); } }
     */

    @Override
    protected boolean FlattenObject(CoalesceEntity entity, boolean AllowRemoval) throws CoalescePersistorException
    {
        boolean isSuccessful = false;

        // Create a Database Connection
        try (MySQLDataConnector conn = new MySQLDataConnector(this.serCon))
        {

            conn.openConnection();

            // Persist (Recursively)
            isSuccessful = this.updateDataObject(entity, conn, AllowRemoval);

            // Persist Entity Last to Include Changes
            switch (entity.getType().toLowerCase()) {
            case "entity":
                isSuccessful = persistEntityObject(entity, conn);
            }

        }
        catch (SQLException e)
        {
            throw new CoalescePersistorException("FlattenObject", e);
        }
        catch (Exception e)
        {
            throw new CoalescePersistorException("FlattenObject", e);
        }

        return isSuccessful;
    }

    @Override
    protected boolean FlattenCore(CoalesceEntity entity, boolean AllowRemoval) throws CoalescePersistorException
    {
        boolean isSuccessful = false;

        // Create a Database Connection
        try (MySQLDataConnector conn = new MySQLDataConnector(this.serCon))
        {

            conn.openConnection();

            if (persistEntityObject(entity, conn))
            {

                isSuccessful = updateFileContent(entity, conn);

            }

        }
        catch (SQLException e)
        {
            throw new CoalescePersistorException("FlattenCore", e);
        }
        catch (Exception e)
        {
            throw new CoalescePersistorException("FlattenCore", e);
        }

        return isSuccessful;
    }

    @Override
    public String getEntityTemplateKey(String Name, String Source, String Version) throws CoalescePersistorException
    {
        try (MySQLDataConnector conn = new MySQLDataConnector(this.serCon))
        {
            String value = null;

            ResultSet results = conn.executeQuery("SELECT TemplateKey FROM CoalesceEntityTemplate WHERE Name=? and Source=? and Version=?",
                                                  new CoalesceParameter(Name),
                                                  new CoalesceParameter(Source),
                                                  new CoalesceParameter(Version));

            if (results != null && results.first())
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
    public String getEntityTemplateMetadata()
    {
        // TODO - I need to make sure it cannot be done!!!
        // *************************************************************************************************************************************
        // SQL in the .NET version does not seem to be supported in MySQL - it uses
        // "FOR XML RAW, ROOT('coalesceentitytemplates')"
        //
        // This means: By specifying the ROOT option in the FOR XML query, you can request a single, top-level element for
        // the resulting XML, as shown in this query. The argument specified for the ROOT directive provides the root element
        // name. http://msdn.microsoft.com/en-us/library/bb522623.aspx
        // *************************************************************************************************************************************
        return null;
    }

    @Override
    public String getEntityTemplateXml(String Key) throws CoalescePersistorException
    {
        try (MySQLDataConnector conn = new MySQLDataConnector(this.serCon))
        {
            String value = null;

            ResultSet results = conn.executeQuery("SELECT TemplateXml FROM CoalesceEntityTemplate WHERE TemplateKey=?",
                                                  new CoalesceParameter(Key));

            if (results != null && results.first())
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
    public String getEntityTemplateXml(String Name, String Source, String Version) throws CoalescePersistorException
    {
        try (MySQLDataConnector conn = new MySQLDataConnector(this.serCon))
        {
            String value = null;

            ResultSet results = conn.executeQuery("SELECT TemplateXml FROM CoalesceEntityTemplate WHERE Name=? and Source=? and Version=?",
                                                  new CoalesceParameter(Name),
                                                  new CoalesceParameter(Source),
                                                  new CoalesceParameter(Version));

            if (results != null && results.first())
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
     * @param dataObject the XsdDataObject to be added or updated
     * @param conn is the MySQLDataConnector database connection
     * @return isSuccessful = True = Successful add/update operation.
     * @throws SQLException
     */
    protected boolean persistObject(CoalesceDataObject dataObject, MySQLDataConnector conn) throws SQLException
    {
        boolean isSuccessful = true;

        switch (dataObject.getType()) {
        case "entity":

            isSuccessful = this.checkLastModified(dataObject, conn);
            // isSuccessful = PersistEntityObject(dataObject);
            break;

        case "section":
            if (CoalesceSettings.getUseIndexing())
            {
                isSuccessful = persistSectionObject((CoalesceSection) dataObject, conn);
            }
            break;

        case "recordset":
            if (CoalesceSettings.getUseIndexing())
            {
                isSuccessful = persistRecordsetObject((CoalesceRecordset) dataObject, conn);
            }
            break;
        case "fielddefinition":
            if (CoalesceSettings.getUseIndexing())
            {
                // Removed Field Definition Persisting
                // isSuccessful = PersistFieldDefinitionObject((XsdFieldDefinition) dataObject, conn);
            }
            break;

        case "record":
            if (CoalesceSettings.getUseIndexing())
            {
                isSuccessful = persistRecordObject((CoalesceRecord) dataObject, conn);
            }
            break;

        case "field":// Not testing the type to ascertain if it is BINARY now.
            if (CoalesceSettings.getUseIndexing())
            {
                isSuccessful = persistFieldObject((CoalesceField<?>) dataObject, conn);
            }
            break;

        case "fieldhistory":
            if (CoalesceSettings.getUseIndexing())
            {
                isSuccessful = persistFieldHistoryObject((CoalesceFieldHistory) dataObject, conn);
            }
            break;

        case "linkagesection":
            if (CoalesceSettings.getUseIndexing())
            {
                isSuccessful = persistLinkageSectionObject((CoalesceLinkageSection) dataObject, conn);
            }
            break;

        case "linkage":
            if (CoalesceSettings.getUseIndexing())
            {
                isSuccessful = persistLinkageObject((CoalesceLinkage) dataObject, conn);
            }
            break;

        default:
            isSuccessful = false;
        }
        return isSuccessful;
    }

    /**
     * Adds or Updates a Coalesce entity that matches the given parameters.
     * 
     * @param entity the XsdEntity to be added or updated
     * @param conn is the MySQLDataConnector database connection
     * @return True = No Update required.
     * @return True = Successful add/update operation.
     * @throws SQLException
     */
    protected boolean persistEntityObject(CoalesceEntity entity, MySQLDataConnector conn) throws SQLException
    {
        // Return true if no update is required.
        if (!this.checkLastModified(entity, conn)) return true;

        // Yes; Call Store Procedure
        return conn.executeProcedure("CoalesceEntity_InsertOrUpdate",
                                     new CoalesceParameter(entity.getKey()),
                                     new CoalesceParameter(entity.getName()),
                                     new CoalesceParameter(entity.getSource()),
                                     new CoalesceParameter(entity.getVersion()),
                                     new CoalesceParameter(entity.getEntityId()),
                                     new CoalesceParameter(entity.getEntityIdType()),
                                     new CoalesceParameter(entity.toXml()),
                                     new CoalesceParameter(entity.getDateCreated()),
                                     new CoalesceParameter(entity.getLastModified()));
    }

    /**
     * Adds or Updates a Coalesce section that matches the given parameters.
     * 
     * @param section the XsdSection to be added or updated
     * @param conn is the MySQLDataConnector database connection
     * @return True = No Update required.
     * @return True = Successful add/update operation.
     * @throws SQLException
     */
    protected boolean persistSectionObject(CoalesceSection section, MySQLDataConnector conn) throws SQLException
    {
        // Return true if no update is required.
        if (!this.checkLastModified(section, conn)) return true;

        // Yes; Call Store Procedure
        return conn.executeProcedure("CoalesceSection_InsertOrUpdate",
                                     new CoalesceParameter(section.getKey()),
                                     new CoalesceParameter(section.getName()),
                                     new CoalesceParameter(section.getParent().getKey()),
                                     new CoalesceParameter(section.getParent().getType()),
                                     new CoalesceParameter(section.getDateCreated()),
                                     new CoalesceParameter(section.getLastModified()));
    }

    /**
     * Adds or Updates a Coalesce recordset that matches the given parameters.
     * 
     * @param recordset the XsdRecordset to be added or updated
     * @param conn is the MySQLDataConnector database connection
     * @return True = No Update required.
     * @return True = Successful add/update operation.
     * @throws SQLException
     */
    protected boolean persistRecordsetObject(CoalesceRecordset recordset, MySQLDataConnector conn) throws SQLException
    {
        // Return true if no update is required.
        if (!this.checkLastModified(recordset, conn)) return true;

        // Yes; Call Store Procedure
        return conn.executeProcedure("CoalesceRecordset_InsertOrUpdate",
                                     new CoalesceParameter(recordset.getKey()),
                                     new CoalesceParameter(recordset.getName()),
                                     new CoalesceParameter(recordset.getParent().getKey()),
                                     new CoalesceParameter(recordset.getParent().getType()),
                                     new CoalesceParameter(recordset.getDateCreated()),
                                     new CoalesceParameter(recordset.getLastModified()));
    }

    /**
     * Adds or Updates a Coalesce field definition that matches the given parameters.
     * 
     * @param fieldDefinition the XsdFieldDefinition to be added or updated
     * @param conn is the MySQLDataConnector database connection
     * @return True = No Update required.
     * @return True = Successful add/update operation.
     * @throws SQLException
     */
    protected boolean persistFieldDefinitionObject(CoalesceFieldDefinition fieldDefinition, MySQLDataConnector conn)
            throws SQLException
    {

        // Return true if no update is required.
        if (!this.checkLastModified(fieldDefinition, conn)) return true;

        // Yes; Call Store Procedure
        return conn.executeProcedure("CoalesceFieldDefinition_InsertOrUpdate",
                                     new CoalesceParameter(fieldDefinition.getKey()),
                                     new CoalesceParameter(fieldDefinition.getName()),
                                     new CoalesceParameter(fieldDefinition.getParent().getKey()),
                                     new CoalesceParameter(fieldDefinition.getParent().getType()),
                                     new CoalesceParameter(fieldDefinition.getDateCreated()),
                                     new CoalesceParameter(fieldDefinition.getLastModified()));
    }

    /**
     * Adds or Updates a Coalesce record that matches the given parameters.
     * 
     * @param record the XsdRecord to be added or updated
     * @param conn is the MySQLDataConnector database connection
     * @return True = No Update required.
     * @return True = Successful add/update operation.
     * @throws SQLException
     */
    protected boolean persistRecordObject(CoalesceRecord record, MySQLDataConnector conn) throws SQLException
    {
        // Return true if no update is required.
        if (!this.checkLastModified(record, conn)) return true;

        // Yes; Call Store Procedure
        return conn.executeProcedure("CoalesceRecord_InsertOrUpdate",
                                     new CoalesceParameter(record.getKey()),
                                     new CoalesceParameter(record.getName()),
                                     new CoalesceParameter(record.getParent().getKey()),
                                     new CoalesceParameter(record.getParent().getType()),
                                     new CoalesceParameter(record.getDateCreated()),
                                     new CoalesceParameter(record.getLastModified()));
    }

    /**
     * Adds or Updates a Coalesce field that matches the given parameters.
     * 
     * @param field the XsdField to be added or updated
     * @param conn is the MySQLDataConnector database connection
     * @return True = No Update required.
     * @return True = Successful add/update operation.
     * @throws SQLException
     */
    protected boolean persistFieldObject(CoalesceField<?> field, MySQLDataConnector conn) throws SQLException
    {
        // Return true if no update is required.
        if (!this.checkLastModified(field, conn)) return true;

        // Yes; Call Store Procedure
        return conn.executeProcedure("CoalesceField_InsertOrUpdate",
                                     new CoalesceParameter(field.getKey()),
                                     new CoalesceParameter(field.getName()),
                                     new CoalesceParameter(field.getBaseValue()),
                                     new CoalesceParameter(field.getDataType().getLabel()),
                                     new CoalesceParameter(""),
                                     new CoalesceParameter(field.getClassificationMarkingAsString()),
                                     new CoalesceParameter(field.getModifiedBy()),
                                     new CoalesceParameter(field.getParent().getKey()),
                                     new CoalesceParameter(field.getParent().getType()),
                                     new CoalesceParameter(field.getDateCreated()),
                                     new CoalesceParameter(field.getLastModified()),
                                     new CoalesceParameter(field.getPreviousHistoryKey()));
    }

    /**
     * Adds or Updates a Coalesce field history that matches the given parameters.
     * 
     * @param fieldHistory the XsdFieldHistory to be added or updated
     * @param conn is the MySQLDataConnector database connection
     * @return True = No Update required.
     * @return True = Successful add/update operation.
     * @throws SQLException
     */
    protected boolean persistFieldHistoryObject(CoalesceFieldHistory fieldHistory, MySQLDataConnector conn) throws SQLException
    {
        // Return true if no update is required.
        if (!this.checkLastModified(fieldHistory, conn)) return true;

        // Yes; Call Store Procedure
        return conn.executeProcedure("CoalesceFieldHistory_InsertOrUpdate",
                                     new CoalesceParameter(fieldHistory.getKey()),
                                     new CoalesceParameter(fieldHistory.getName()),
                                     new CoalesceParameter(fieldHistory.getValue()),
                                     new CoalesceParameter(fieldHistory.getDataType().getLabel()),
                                     new CoalesceParameter(""),
                                     new CoalesceParameter(fieldHistory.getClassificationMarkingAsString()),
                                     new CoalesceParameter(fieldHistory.getModifiedBy()),
                                     new CoalesceParameter(fieldHistory.getParent().getKey()),
                                     new CoalesceParameter(fieldHistory.getParent().getType()),
                                     new CoalesceParameter(fieldHistory.getDateCreated()),
                                     new CoalesceParameter(fieldHistory.getLastModified()),
                                     new CoalesceParameter(fieldHistory.getPreviousHistoryKey()));
    }

    /**
     * Adds or Updates a Coalesce linkage section that matches the given parameters.
     * 
     * @param linkageSection the XsdLinkageSection to be added or updated
     * @param conn is the MySQLDataConnector database connection
     * @return True = No Update required.
     * @return True = Successful add/update operation.
     * @throws SQLException
     */
    protected boolean persistLinkageSectionObject(CoalesceLinkageSection linkageSection, MySQLDataConnector conn)
            throws SQLException
    {
        // Return true if no update is required.
        if (!this.checkLastModified(linkageSection, conn)) return true;

        // Yes; Call Store Procedure
        return conn.executeProcedure("CoalesceLinkageSection_InsertOrUpdate",
                                     new CoalesceParameter(linkageSection.getKey()),
                                     new CoalesceParameter(linkageSection.getName()),
                                     new CoalesceParameter(linkageSection.getParent().getKey()),
                                     new CoalesceParameter(linkageSection.getParent().getType()),
                                     new CoalesceParameter(linkageSection.getDateCreated()),
                                     new CoalesceParameter(linkageSection.getLastModified()));
    }

    /**
     * Adds or Updates a Coalesce linkage that matches the given parameters.
     * 
     * @param linkage the XsdLinkage to be added or updated
     * @param conn is the MySQLDataConnector database connection
     * @return True = No Update required.
     * @return True = Successful add/update operation.
     * @throws SQLException
     */
    protected boolean persistLinkageObject(CoalesceLinkage linkage, MySQLDataConnector conn) throws SQLException
    {
        // Return true if no update is required.
        if (!this.checkLastModified(linkage, conn)) return true;

        // Yes; Call Store Procedure
        return conn.executeProcedure("CoalesceLinkage_InsertOrUpdate",
                                     new CoalesceParameter(linkage.getKey()),
                                     new CoalesceParameter(linkage.getName()),
                                     new CoalesceParameter(linkage.getEntity1Key()),
                                     new CoalesceParameter(linkage.getEntity1Name()),
                                     new CoalesceParameter(linkage.getEntity1Source()),
                                     new CoalesceParameter(linkage.getEntity1Version()),
                                     new CoalesceParameter(linkage.getLinkType().getLabel()),
                                     new CoalesceParameter(linkage.getStatus().getLabel()),
                                     new CoalesceParameter(linkage.getEntity2Key()),
                                     new CoalesceParameter(linkage.getEntity2Name()),
                                     new CoalesceParameter(linkage.getEntity2Source()),
                                     new CoalesceParameter(linkage.getEntity2Version()),
                                     new CoalesceParameter(linkage.getClassificationMarking().toPortionString()),
                                     new CoalesceParameter(linkage.getModifiedBy()),
                                     new CoalesceParameter(""),
                                     new CoalesceParameter(linkage.getParent().getKey()),
                                     new CoalesceParameter(linkage.getParent().getType()),
                                     new CoalesceParameter(linkage.getDateCreated()),
                                     new CoalesceParameter(linkage.getLastModified()));
    }

    /**
     * Returns the EntityMetaData for the Coalesce entity that matches the given parameters
     * 
     * @param Key primary key of the Coalesce entity
     * @param conn is the MySQLDataConnector database connection
     * @return metaData the EntityMetaData for the Coalesce entity.
     * @throws SQLException
     */
    protected EntityMetaData getCoalesceEntityIdAndTypeForKey(String Key, MySQLDataConnector conn) throws SQLException
    {
        EntityMetaData metaData = new EntityMetaData();

        // Execute Query
        ResultSet results = conn.executeQuery("SELECT EntityId,EntityIdType,ObjectKey FROM CoalesceEntity WHERE ObjectKey=?",
                                              new CoalesceParameter(Key));
        // Get Results
        if (results != null && results.first())
        {
            metaData.entityId = results.getString("EntityId");
            metaData.entityType = results.getString("EntityIdType");
            metaData.entityKey = results.getString("ObjectKey");
        }

        return metaData;
    }

    /**
     * Returns the rounded milliseconds
     * 
     * @param Ticks time in milliseconds to be rounded up
     * @return Ticks rounded up time in milliseconds.
     */
    protected long roundTicksForSQL(long Ticks)
    {
        int iTick = (int) (Ticks % 10);

        switch (iTick) {
        case 0:
        case 3:
        case 7:
            break;
        case 1:
        case 4:
        case 8:
            Ticks = Ticks - 1;
            break;
        case 2:
        case 6:
        case 9:
            Ticks = Ticks + 1;
            break;
        case 5:
            Ticks = Ticks + 2;
            break;
        }
        return Ticks;
    }

    /**
     * Returns the comparison for the XsdDataObject last modified date versus the same objects value in the database.
     * 
     * @param dataObject the XsdDataObject to have it's last modified date checked.
     * @param conn is the MySQLDataConnector database connection
     * @return False = Out of Date
     * @throws SQLException
     */
    protected boolean checkLastModified(CoalesceDataObject dataObject, MySQLDataConnector conn) throws SQLException
    {
        boolean isOutOfDate = true;

        // Get LastModified from the Database
        DateTime LastModified = this.getCoalesceDataObjectLastModified(dataObject.getKey(), dataObject.getType(), conn);

        // DB Has Valid Time?
        if (LastModified != null)
        {
            // Remove NanoSeconds (100 ns / Tick and 1,000,000 ns / ms = 10,000 Ticks / ms)
            long ObjectTicks = dataObject.getLastModified().getMillis();
            long SQLRecordTicks = LastModified.getMillis();

            // TODO: Round Ticks for SQL (Not sure if this is required for .NET)
            // ObjectTicks = this.RoundTicksForSQL(ObjectTicks);

            if (ObjectTicks == SQLRecordTicks)
            {
                // They're equal; No Update Required
                isOutOfDate = false;
            }
        }

        return isOutOfDate;
    }

    /**
     * Deletes the Coalesce object & CoalesceObjectMap that matches the given parameters
     * 
     * @param dataObject the XsdDataObject to be deleted
     * @param conn is the MySQLDataConnector database connection
     * @return True = Successful delete
     * @throws SQLException
     */
    protected boolean deleteObject(CoalesceDataObject dataObject, MySQLDataConnector conn) throws SQLException
    {
        String objectType = dataObject.getType();
        String objectKey = dataObject.getKey();
        String tableName = CoalesceTable.getTableNameForObjectType(objectType);

        conn.executeCmd("DELETE FROM CoalesceObjectMap WHERE ObjectKey=?", new CoalesceParameter(objectKey));
        conn.executeCmd("DELETE FROM " + tableName + " WHERE ObjectKey=?", new CoalesceParameter(objectKey));

        return true;
    }

    /**
     * Returns the Coalesce entity keys that matches the given parameters.
     * 
     * @param EntityId of the entity.
     * @param EntityIdType of the entity.
     * @param EntityName of the entity.
     * @return List<String> of primary keys for the matching Coalesce entity.
     * @throws SQLException,Exception,CoalescePersistorException
     */
    protected List<String> getCoalesceEntityKeysForEntityId(String EntityId, String EntityIdType, String EntityName)
            throws SQLException, Exception, CoalescePersistorException
    {
        List<String> keyList = new ArrayList<String>();

        try (MySQLDataConnector conn = new MySQLDataConnector(this.serCon))
        {
            ResultSet results = conn.executeQuery("SELECT ObjectKey FROM CoalesceEntity WHERE (EntityId like '%' ? '%') AND (EntityIdType like '%' ? '%') AND (Name=?)",
                                                  new CoalesceParameter(EntityId),
                                                  new CoalesceParameter(EntityIdType),
                                                  new CoalesceParameter(EntityName));

            if (results.first())
            {

                do
                {
                    keyList.add(results.getString("ObjectKey"));
                }
                while (results.next());
            }

            return keyList;
        }

    }

    /**
     * Returns the Coalesce entity keys that matches the given parameters.
     * 
     * @param EntityId of the entity.
     * @param EntityIdType of the entity.
     * @param EntityName of the entity.
     * @param EntitySource of the entity.
     * @return List<String> of primary keys for the matching Coalesce entity.
     * @throws SQLException,Exception,CoalescePersistorException
     */
    protected List<String> getCoalesceEntityKeysForEntityIdAndSource(String EntityId,
                                                                     String EntityIdType,
                                                                     String EntityName,
                                                                     String EntitySource) throws SQLException, Exception,
            CoalescePersistorException
    {

        try (MySQLDataConnector conn = new MySQLDataConnector(this.serCon))
        {
            List<String> keyList = new ArrayList<String>();

            ResultSet results = conn.executeQuery("SELECT ObjectKey FROM CoalesceEntity WHERE (EntityId like '%' ? '%') AND (EntityIdType like '%' ? '%') AND (Name=?) AND (Source=?)",
                                                  new CoalesceParameter(EntityId),
                                                  new CoalesceParameter(EntityIdType),
                                                  new CoalesceParameter(EntityName),
                                                  new CoalesceParameter(EntitySource));

            if (results.first())
            {
                do
                {
                    keyList.add(results.getString("ObjectKey"));
                }
                while (results.next());
            }

            return keyList;
        }
    }

    /**
     * Sets the active Coalesce field objects matching the parameters given.
     * 
     * @param dataObject the Coalesce field object.
     * @param conn is the MySQLDataConnector database connection
     * @throws SQLException,Exception,CoalescePersistorException
     */
    protected boolean updateFileContent(CoalesceDataObject dataObject, MySQLDataConnector conn) throws SQLException
    {
        boolean isSuccessful = false;

        if (dataObject.getStatus() == ECoalesceDataObjectStatus.ACTIVE)
        {
            if (dataObject.getType().toLowerCase() == "field")
            {
                if (((CoalesceField<?>) dataObject).getDataType() == ECoalesceFieldDataTypes.FILE_TYPE)
                {
                    isSuccessful = persistFieldObject((CoalesceField<?>) dataObject, conn);
                }
            }

            for (Map.Entry<String, CoalesceDataObject> s : dataObject.getChildDataObjects().entrySet())
            {
                isSuccessful = updateFileContent(s.getValue(), conn);
            }
        }
        return isSuccessful;
    }

    private boolean updateDataObject(CoalesceDataObject coalesceDataObject, MySQLDataConnector conn, boolean AllowRemoval)
            throws SQLException

    {
        boolean isSuccessful = false;

        // System.out.println(xsdDataObject.getStatus().getLabel() + " OBJECT [" + xsdDataObject.getName() + " : "
        // + xsdDataObject.getType() + "] Processing Key:  " + xsdDataObject.getKey());

        switch (coalesceDataObject.getStatus()) {
        case ACTIVE:
            // Persist Object
            isSuccessful = persistObject(coalesceDataObject, conn);
            break;

        case DELETED:
            if (AllowRemoval)
            {
                // Delete Object
                isSuccessful = deleteObject(coalesceDataObject, conn);
            }
            else
            {
                // Mark Object as Deleted
                isSuccessful = persistObject(coalesceDataObject, conn);
            }

            break;

        default:
            isSuccessful = false;

        }

        // Successful?
        if (isSuccessful)
        {

            // Yes; Iterate Through Children
            for (Map.Entry<String, CoalesceDataObject> s : coalesceDataObject.getChildDataObjects().entrySet())
            {
                updateDataObject(s.getValue(), conn, AllowRemoval);
            }
        }

        return isSuccessful;
    }

    private DateTime getCoalesceDataObjectLastModified(String Key, String ObjectType, MySQLDataConnector conn)
            throws SQLException
    {
        DateTime lastModified = DateTime.now(DateTimeZone.UTC);

        // Determine the Table Name
        String tableName = CoalesceTable.getTableNameForObjectType(ObjectType);
        String dateValue = null;

        ResultSet results = conn.executeQuery("SELECT LastModified FROM " + tableName + " WHERE ObjectKey=?", new CoalesceParameter(Key.trim()));
        ResultSetMetaData resultsmd = results.getMetaData();

        // JODA Function DateTimeFormat will adjust for the Server timezone when converting the time.
        if (resultsmd.getColumnCount() <= 1 && results.first())
        {
            dateValue = results.getString("LastModified");
            if (dateValue != null)
            {
                lastModified = JodaDateTimeHelper.getMySQLDateTime(dateValue);
            }
        }
        return lastModified;

    }

    private ElementMetaData getXPathRecursive(String Key, String ObjectType, String XPath, MySQLDataConnector conn)
            throws SQLException
    {

        boolean isEntityTable = false;
        ElementMetaData meteData = null;

        String sql = "";

        // Get Table Name
        String tableName = CoalesceTable.getTableNameForObjectType(ObjectType);

        // Check to see if its the Entity Table
        if (tableName.equals("CoalesceEntity")) isEntityTable = true;

        if (isEntityTable)
        {
            sql = "SELECT name FROM ".concat(tableName).concat(" WHERE ObjectKey=?");
        }
        else
        {
            sql = "SELECT name, ParentKey, ParentType FROM ".concat(tableName).concat(" WHERE ObjectKey=?");
        }

        ResultSet results = conn.executeQuery(sql, new CoalesceParameter(Key.trim()));

        // Valid Results?
        if (results.first())
        {

            String name = results.getString("name");

            if (isEntityTable)
            {
                XPath = name + "/" + XPath;

                // Set Meta Data
                meteData = new ElementMetaData();
                meteData.entityKey = Key;
                meteData.elementXPath = XPath;

            }
            else
            {
                String parentKey = results.getString("ParentKey");
                String parentType = results.getString("ParentType");

                if (XPath == null || XPath == "")
                {
                    meteData = getXPathRecursive(parentKey, parentType, name, conn);
                }
                else
                {
                    meteData = getXPathRecursive(parentKey, parentType, name + "/" + XPath, conn);
                }
            }

        }

        return meteData;

    }
}
