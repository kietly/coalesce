package com.incadencecorp.coalesce.framework.persistance;

import com.incadencecorp.coalesce.api.CoalesceErrors;
import com.incadencecorp.coalesce.api.persistance.EPersistorCapabilities;
import com.incadencecorp.coalesce.common.exceptions.CoalescePersistorException;
import com.incadencecorp.coalesce.common.helpers.EntityLinkHelper;
import com.incadencecorp.coalesce.framework.datamodel.*;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.UUID;

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

public abstract class AbstractCoalescePersistorTest<T extends ICoalescePersistor> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCoalescePersistorTest.class);

    protected abstract T createPersister() throws CoalescePersistorException;

    private static boolean isInitialized = false;

    @Before
    public void registerEntities()
    {
        if (!isInitialized)
        {
            LOGGER.warn("Registering Entities");

            TestEntity entity = new TestEntity();
            entity.initialize();

            try
            {
                createPersister().registerTemplate(CoalesceEntityTemplate.create(entity));
            }
            catch (CoalescePersistorException | SAXException | IOException e)
            {
                LOGGER.warn("Failed to register templates");
            }

            isInitialized = true;
        }
    }

    /**
     * This test attempts to create a entity within the data store.
     *
     * @throws Exception
     */
    @Test
    public void testCreation() throws Exception
    {
        T persister = createPersister();

        Assume.assumeTrue(persister.getCapabilities().contains(EPersistorCapabilities.CREATE));

        TestEntity entity = new TestEntity();
        entity.initialize();

        Assert.assertTrue(persister.saveEntity(false, entity));

        TestEntity entity2 = new TestEntity();
        entity2.initialize();

        EntityLinkHelper.linkEntities(entity, ELinkTypes.IS_PARENT_OF, entity2);

        Assert.assertTrue(persister.saveEntity(true, entity, entity2));

        // Cleanup
        entity.markAsDeleted();

        persister.saveEntity(true, entity);

    }

    /**
     * This test attempts to create and then update a entity within the data
     * store.
     *
     * @throws Exception
     */
    @Test
    public void testUpdates() throws Exception
    {
        T persister = createPersister();

        Assume.assumeTrue(persister.getCapabilities().contains(EPersistorCapabilities.CREATE));
        Assume.assumeTrue(persister.getCapabilities().contains(EPersistorCapabilities.READ));
        Assume.assumeTrue(persister.getCapabilities().contains(EPersistorCapabilities.UPDATE));

        TestEntity entity = new TestEntity();
        entity.initialize();

        Assert.assertTrue(persister.saveEntity(false, entity));

        TestRecord record1 = entity.addRecord1();
        record1.getBooleanField().setValue(true);

        Assert.assertTrue(persister.saveEntity(false, entity));

        CoalesceEntity updated = persister.getEntity(entity.getKey())[0];

        CoalesceRecord updatedRecord = (CoalesceRecord) updated.getCoalesceObjectForKey(record1.getKey());

        Assert.assertNotNull(updatedRecord);
        Assert.assertEquals(record1.getBooleanField().getBaseValue(),
                            updatedRecord.getFieldByName(record1.getBooleanField().getName()).getBaseValue());

        // Cleanup
        entity.markAsDeleted();

        persister.saveEntity(true, entity);
    }

    /**
     * This test attempts to mark an entity as deleted as well as remove it
     * Completely.
     *
     * @throws Exception
     */
    @Test
    public void testDeletion() throws Exception
    {
        T persister = createPersister();

        Assume.assumeTrue(persister.getCapabilities().contains(EPersistorCapabilities.CREATE));
        Assume.assumeTrue(persister.getCapabilities().contains(EPersistorCapabilities.DELETE));

        TestEntity entity = new TestEntity();
        entity.initialize();

        Assert.assertTrue(persister.saveEntity(false, entity));
        Assert.assertNotNull(persister.getEntityXml(entity.getKey())[0]);

        entity.markAsDeleted();

        Assert.assertTrue(persister.saveEntity(true, entity));
        Assert.assertEquals(0, persister.getEntityXml(entity.getKey()).length);
    }

    /**
     * This test attempts to retrieve an invalid entity key and should fail.
     *
     * @throws Exception
     */
    @Test
    public void testRetrieveInvalidKey() throws Exception
    {
        T persister = createPersister();

        Assume.assumeTrue(persister.getCapabilities().contains(EPersistorCapabilities.CREATE));
        Assume.assumeTrue(persister.getCapabilities().contains(EPersistorCapabilities.READ));

        TestEntity entity = new TestEntity();
        entity.initialize();

        Assert.assertTrue(persister.saveEntity(false, entity));

        String results[] = persister.getEntityXml(entity.getKey(),
                                                  UUID.randomUUID().toString(),
                                                  UUID.randomUUID().toString(),
                                                  UUID.randomUUID().toString());

        Assert.assertEquals(1, results.length);
        Assert.assertEquals(entity.getKey(), CoalesceEntity.create(results[0]).getKey());
    }

    /**
     * This test attempts to save a template and retrieve it.
     *
     * @throws Exception
     */
    @Test
    public void testTemplates() throws Exception
    {
        T persister = createPersister();

        Assume.assumeTrue(persister.getCapabilities().contains(EPersistorCapabilities.READ_TEMPLATES));

        TestEntity entity = new TestEntity();
        entity.initialize();

        CoalesceEntityTemplate template1 = CoalesceEntityTemplate.create(entity);
        entity.setName("HelloWorld");
        CoalesceEntityTemplate template2 = CoalesceEntityTemplate.create(entity);

        persister.saveTemplate(template1, template2);

        Assert.assertEquals(template1.getKey(), persister.getEntityTemplate(template1.getKey()).getKey());
        Assert.assertEquals(template2.getKey(), persister.getEntityTemplate(template2.getKey()).getKey());
    }

    /**
     * This test verifies that an exception is thrown when attempting to retrieve an invalid template with a key.
     *
     * @throws Exception
     */
    @Test
    public void testTemplatesInvalid() throws Exception
    {
        T persister = createPersister();

        Assume.assumeTrue(persister.getCapabilities().contains(EPersistorCapabilities.READ_TEMPLATES));

        String key = UUID.randomUUID().toString();

        try
        {
            persister.getEntityTemplate(key);
            Assert.fail("Expected an Exception");
        }
        catch (CoalescePersistorException e)
        {
            Assert.assertEquals(String.format(CoalesceErrors.NOT_FOUND, "Template", key), e.getMessage());
        }

        try
        {
            persister.getEntityTemplate(key, key, key);
            Assert.fail("Expected an Exception");
        }
        catch (CoalescePersistorException e)
        {
            Assert.assertEquals(String.format(CoalesceErrors.NOT_FOUND,
                                              "Template",
                                              "Name: " + key + " Source: " + key + " Version: " + key), e.getMessage());
        }
    }

}
