package com.incadencecorp.coalesce.framework.persistance;

import java.util.List;

import org.joda.time.DateTime;

import com.incadencecorp.coalesce.common.exceptions.CoalescePersistorException;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceEntity;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceEntityTemplate;

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
 * Stores and retrieves Coalesce entities from different databases depending on the implementation.
 */
public interface ICoalescePersistor {

    /**
     * Stores meta data about an Coalesce Entity.
     * 
     * @see {@link ICoalescePersistor#getCoalesceEntityIdAndTypeForKey(String)}.
     */
    public class EntityMetaData {

        private String _entityId;
        private String _entityType;
        private String _entityKey;

        public EntityMetaData(final String id, final String type, final String key)
        {
            _entityId = id;
            _entityType = type;
            _entityKey = key;
        }

        /**
         * @return a comma separated value (CSV) list of unique identifiers that represents a Coalesce entity.
         */
        public final String getEntityId()
        {
            return _entityId;
        }

        /**
         * @return comma separated value (CSV) list of type identifiers that map 1 to 1 with _entityId.
         */
        public final String getEntityType()
        {
            return _entityType;
        }

        /**
         * @return a GUID that uniquely identifies a Coalesce entity.
         */
        public final String getEntityKey()
        {
            return _entityKey;
        }

    }

    /**
     * Stores meta data about an element within an Coalesce Entity.
     * 
     * @see {@link ICoalescePersistor#getXPath(String, String)}.
     */
    public class ElementMetaData {

        private String _entityKey;
        private String _elementXPath;

        public ElementMetaData(final String key, final String xPath)
        {
            _entityKey = key;
            _elementXPath = xPath;
        }

        /**
         * Contains a GUID that uniquely identifies a Coalesce entity that contains the element of interest.
         */
        public final String getEntityKey()
        {
            return _entityKey;
        }

        /**
         * Contains a XML path within the Coalesce entity specified by _entityKey that contains the element of interest.
         */
        public final String getElementXPath()
        {
            return _elementXPath;
        }

    }

    /**
     * Instantiates the persistor which must be done before using.
     * 
     * @param cacher Pass null if caching is not wanted
     * @return true if successful
     * @throws CoalescePersistorException
     */
    boolean initialize(ICoalesceCacher cacher) throws CoalescePersistorException;

    /**
     * Saves the Coalesce entity to the database.
     * 
     * @param entity the Coalesce entity to be saved.
     * @param allowRemoval specifies whether an entity marked as deleted should be removed from the database.
     * @return true if successfully saved.
     * @throws CoalescePersistorException
     */
    boolean saveEntity(CoalesceEntity entity, boolean allowRemoval) throws CoalescePersistorException;

    /**
     * Returns the Coalesce entity that matches the given parameters.
     * 
     * @param key the primary key of the entity.
     * @return the matching Coalesce entity.
     * @throws CoalescePersistorException
     */
    CoalesceEntity getEntity(String key) throws CoalescePersistorException;

    /**
     * Returns the Coalesce entity that matches the given parameters.
     * 
     * @param entityId the unique identifier, such as a TCN number for an EFT.
     * @param entityIdType the type of entityId, such as TCN.
     * @return the matching Coalesce entity.
     * @throws CoalescePersistorException
     */
    CoalesceEntity getEntity(String entityId, String entityIdType) throws CoalescePersistorException;

    /**
     * Returns the Coalesce entity that matches the given parameters.
     * 
     * @param name the name of the entity.
     * @param entityId the unique identifier, such as a TCN number for an EFT.
     * @param entityIdType the type of entityId, such as TCN.
     * @return the matching Coalesce entity.
     * @throws CoalescePersistorException
     */
    CoalesceEntity getEntity(String name, String entityId, String entityIdType) throws CoalescePersistorException;

    /**
     * Returns the Coalesce entity's XML that matches the given parameters.
     * 
     * @param key the primary key of the entity.
     * @return the matching Coalesce entity's XML.
     * @throws CoalescePersistorException
     */
    String getEntityXml(String key) throws CoalescePersistorException;

    /**
     * Returns the Coalesce entity's XML that matches the given parameters.
     * 
     * @param entityId the unique identifier, such as a TCN number for an EFT.
     * @param entityIdType the type of entityId, such as TCN.
     * @return the matching Coalesce entity.
     * @throws CoalescePersistorException
     */
    String getEntityXml(String entityId, String entityIdType) throws CoalescePersistorException;

    /**
     * Returns the Coalesce entity's XML that matches the given parameters.
     * 
     * @param name the name of the entity.
     * @param entityId the unique identifier, such as a TCN number for an EFT.
     * @param entityIdType the type of entityId, such as TCN.
     * @return the matching Coalesce entity.
     * @throws CoalescePersistorException
     */
    String getEntityXml(String name, String entityId, String entityIdType) throws CoalescePersistorException;

    /**
     * Returns the value of the specified Coalesce field.
     * 
     * @param fieldKey the primary key of the field.
     * @return returns the value of the matching field.
     * @throws CoalescePersistorException
     */
    Object getFieldValue(String fieldKey) throws CoalescePersistorException;

    /**
     * Returns the ElementMetaData for the Coalesce object that matches the given parameters.
     * 
     * @param key the Coalesce object primary key
     * @param objectType the Coalesce object type specification.
     * @return ElementMetaData
     * @throws CoalescePersistorException
     */
    ElementMetaData getXPath(String key, String objectType) throws CoalescePersistorException;

    /**
     * Returns the last modified date for the Coalesce object (entity, field, record, linkage, etc.) that matches the given
     * parameters.
     * 
     * @param key the primary key of the Coalesce object.
     * @param objectType is the Coalesce object to retrieve the information for.
     * @return DateTime containing the last modified date for the Coalesce object matching the values.
     * @throws CoalescePersistorException
     */
    DateTime getCoalesceDataObjectLastModified(String key, String objectType) throws CoalescePersistorException;

    /**
     * Returns the Coalesce entity keys that matches the given parameters.
     * 
     * @param entityId of the entity.
     * @param entityIdType of the entity.
     * @param entityName of the entity.
     * @param entitySource of the entity.
     * @return List<String> of primary keys for the matching Coalesce entity.
     * @throws CoalescePersistorException
     */
    List<String> getCoalesceEntityKeysForEntityId(String entityId,
                                                  String entityIdType,
                                                  String entityName,
                                                  String entitySource) throws CoalescePersistorException;

    /**
     * Returns the Coalesce entity meta data that matches the given parameters.
     * 
     * @param key the primary key of the entity.
     * @return EntityMetaData for the matching Coalesce entity.
     * @throws CoalescePersistorException
     */
    EntityMetaData getCoalesceEntityIdAndTypeForKey(String key) throws CoalescePersistorException;

    /**
     * Returns the Coalesce field binary data that matches the given parameters.
     * 
     * @param binaryFieldKey the primary key of the Coalesce field.
     * @return byte[] the binary data of the Coalesce field matching the value.
     * @throws CoalescePersistorException
     */
    byte[] getBinaryArray(String binaryFieldKey) throws CoalescePersistorException;

    /**
     * 
     * @param entityTemplate
     * @return
     * @throws CoalescePersistorException
     */
    boolean persistEntityTemplate(CoalesceEntityTemplate entityTemplate) throws CoalescePersistorException;

    /**
     * Returns the Coalesce entity template XML that matches the given parameters.
     * 
     * @param key the primary key of the entity.
     * @return the matching Coalesce entity's XML.
     * @throws CoalescePersistorException
     */
    String getEntityTemplateXml(String key) throws CoalescePersistorException;

    /**
     * Returns the Coalesce entity template XML that matches the given parameters.
     * 
     * @param name of the entity.
     * @param source of the entity.
     * @param version of the entity.
     * @return the matching Coalesce entity's XML.
     * @throws CoalescePersistorException
     */
    String getEntityTemplateXml(String name, String source, String version) throws CoalescePersistorException;

    /**
     * Returns the Coalesce entity template key that matches the given parameters.
     * 
     * @param name of the entity.
     * @param source of the entity.
     * @param version of the entity.
     * @return the matching Coalesce entity's primary key.
     * @throws CoalescePersistorException
     */
    String getEntityTemplateKey(String name, String source, String version) throws CoalescePersistorException;

    /**
     * Returns the Coalesce entity templates.
     * 
     * @return XML of meta data
     * @throws CoalescePersistorException
     */
    String getEntityTemplateMetadata() throws CoalescePersistorException;

}