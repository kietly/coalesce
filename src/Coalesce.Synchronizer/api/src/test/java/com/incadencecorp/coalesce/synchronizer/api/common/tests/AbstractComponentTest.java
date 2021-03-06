/*-----------------------------------------------------------------------------'
 Copyright 2016 - InCadence Strategic Solutions Inc., All Rights Reserved

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

package com.incadencecorp.coalesce.synchronizer.api.common.tests;

import com.incadencecorp.coalesce.framework.datamodel.CoalesceEntity;
import com.incadencecorp.coalesce.framework.persistance.derby.DerbyPersistor;
import com.incadencecorp.coalesce.synchronizer.api.common.SynchronizerParameters;
import com.incadencecorp.coalesce.synchronizer.api.common.mocks.*;
import org.junit.Assert;
import org.junit.Test;

import java.sql.ResultSet;
import java.util.*;

/**
 * These tests exercise the abstract components of the synchronizer service.
 *
 * @author n78554
 */
public class AbstractComponentTest {

    /**
     * This test ensures a null pointer exceptions occurs if the scanner and
     * operations are not configured.
     *
     * @throws Exception
     */
    @Test(expected = NullPointerException.class)
    public void testSetupScannerFailure() throws Exception
    {
        MockDriver driver = new MockDriver();
        driver.setup();
    }

    /**
     * This test ensures a null pointer exception occurs if the operations are
     * not configured.
     *
     * @throws Exception
     */
    @Test(expected = NullPointerException.class)
    public void testSetupOperationFailure() throws Exception
    {
        CoalesceEntity entity = new CoalesceEntity();
        entity.initialize();

        DerbyPersistor source = new DerbyPersistor();
        source.saveEntity(false, entity);

        MockScanner scanner = new MockScanner();
        scanner.setSource(source);

        MockDriver driver = new MockDriver();
        driver.setScan(scanner);
        driver.setup();
        driver.run();
    }

    /**
     * Verifies that the driver using the MockOperation successfully changes the
     * entity's title.
     *
     * @throws Exception
     */
    @Test
    public void testDriver() throws Exception
    {
        String newTitle = UUID.randomUUID().toString();

        CoalesceEntity entity = CoalesceEntity.create("UNIT_TEST", "MockPersister", "1", null, null);

        DerbyPersistor persistor = new DerbyPersistor();
        persistor.saveEntity(false, entity);

        Assert.assertEquals(entity.getTitle(), persistor.getEntity(entity.getKey())[0].getTitle());

        createDriver(persistor, newTitle).run();

        Assert.assertEquals(newTitle, persistor.getEntity(entity.getKey())[0].getTitle());
    }

    /**
     * Setting the title to null will cause the MockOperation to throw an
     * exception which should leave the entity's title unmodified.
     *
     * @throws Exception
     */
    @Test
    public void testDriverFailure() throws Exception
    {
        CoalesceEntity entity = CoalesceEntity.create("UNIT_TEST", "MockPersister", "1", null, null);

        DerbyPersistor persistor = new DerbyPersistor();
        persistor.saveEntity(false, entity);

        Assert.assertEquals(entity.getTitle(), persistor.getEntity(entity.getKey())[0].getTitle());

        createDriver(persistor, null).run();

        Assert.assertEquals(entity.getTitle(), persistor.getEntity(entity.getKey())[0].getTitle());
    }

    /**
     * This unit test ensure the correct operation of the Mock Scanner.
     *
     * @throws Exception
     */
    @Test
    public void testScanner() throws Exception
    {
        MockScanner scanner = new MockScanner();
        scanner.setup();

        List<String> columns = new ArrayList<String>();
        columns.add("objectkey");
        columns.add("2");

        Object[] row1 = new Object[] { UUID.randomUUID().toString(), "B", "C"
        };

        Object[] row2 = new Object[] { UUID.randomUUID().toString(), "C"
        };

        List<Object[]> rows = new ArrayList<Object[]>();
        rows.add(row1);
        rows.add(row2);

        scanner.setColumns(columns);
        scanner.setSource(null);
        scanner.setRows(rows);

        ResultSet results = scanner.scan();

        scanner.finished(true, null);

        Assert.assertTrue(results.first());
        Assert.assertEquals(columns.size(), results.getMetaData().getColumnCount());
        Assert.assertEquals(row1[0], results.getString(1));
        Assert.assertEquals(row1[1], results.getString(2));

        Assert.assertTrue(results.next());
        Assert.assertEquals(row2[0], results.getString(1));
        Assert.assertEquals(row2[1], results.getString(2));

        Assert.assertFalse(results.next());
    }

    private MockDriver createDriver(DerbyPersistor persistor, String title) throws Exception
    {
        Map<String, String> params = new HashMap<String, String>();
        params.put(SynchronizerParameters.PARAM_OP_WINDOW_SIZE, "1");

        MockScanner scanner = new MockScanner();
        scanner.setup();
        scanner.setSource(persistor);

        MockOperation operation = new MockOperation();
        operation.setSource(persistor);
        operation.setTarget(persistor);
        operation.setTitle(title);
        operation.setExecutor(null);
        operation.setHandler(new MockHandler(false));

        MockDriver driver = new MockDriver();
        driver.setScan(scanner);
        driver.setOperations(operation);
        driver.setProperties(params);
        driver.setup();

        return driver;
    }

}
