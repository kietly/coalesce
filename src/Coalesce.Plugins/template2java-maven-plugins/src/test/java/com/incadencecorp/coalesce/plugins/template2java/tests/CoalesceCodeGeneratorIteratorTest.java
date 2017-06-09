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

package com.incadencecorp.coalesce.plugins.template2java.tests;

import java.nio.file.Paths;

import org.junit.Test;

import com.incadencecorp.coalesce.framework.datamodel.TestEntity;
import com.incadencecorp.coalesce.plugins.template2java.CoalesceCodeGeneratorIterator;

public class CoalesceCodeGeneratorIteratorTest {

    @Test
    public void test() throws Exception
    {
        TestEntity entity = new TestEntity();
        entity.initialize();
        //
        CoalesceCodeGeneratorIterator it = new CoalesceCodeGeneratorIterator();
        it.generateCode(entity, Paths.get("src", "test", "resources"));
    }

}
