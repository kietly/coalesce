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

package com.incadencecorp.coalesce.framework.persistance;

import java.nio.file.Paths;

import org.junit.BeforeClass;

import com.incadencecorp.coalesce.framework.persistance.postgres.PostGreSQLPersistorExt;
import com.incadencecorp.coalesce.framework.persistance.postgres.PostGreSQLSettings;
import com.incadencecorp.coalesce.search.AbstractSearchTest;
import com.incadencecorp.unity.common.connectors.FilePropertyConnector;

/**
 * This implementation execute test against {@link PostGreSQLPersistorExt}.
 * 
 * @author n78554
 */
public class PostGresSearchTestIT extends AbstractSearchTest<PostGreSQLPersistorExt> {

    /**
     * Initializes the test configuration.
     */
    @BeforeClass
    public static void initialize()
    {
        FilePropertyConnector connector = new FilePropertyConnector(Paths.get("src", "test", "resources"));
        connector.setReadOnly(true);
        
        PostGreSQLSettings.setConnector(connector);
    }

    @Override
    protected PostGreSQLPersistorExt createPersister()
    {
        return new PostGreSQLPersistorExt();
    }
}
