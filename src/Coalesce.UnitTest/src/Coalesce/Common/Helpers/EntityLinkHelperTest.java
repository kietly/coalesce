package Coalesce.Common.Helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Locale;

import org.junit.Test;

import Coalesce.Common.Classification.Marking;
import Coalesce.Common.UnitTest.CoalesceTypeInstances;
import Coalesce.Framework.DataModel.ECoalesceDataObjectStatus;
import Coalesce.Framework.DataModel.ELinkTypes;
import Coalesce.Framework.DataModel.CoalesceDataObject;
import Coalesce.Framework.DataModel.CoalesceEntity;
import Coalesce.Framework.DataModel.CoalesceLinkage;

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

public class EntityLinkHelperTest {

    private static final Marking UNCLASSIFIED_MARKING = new Marking("(U)");
    private static final Marking SECRET_USA_MARKING = new Marking("(//S USA)");
    private static final Marking TOP_SECRET_MARKING = new Marking("(//TS)");

    /*
     * @BeforeClass public static void setUpBeforeClass() throws Exception { }
     * 
     * @AfterClass public static void tearDownAfterClass() throws Exception { }
     * 
     * @Before public void setUp() throws Exception { }
     * 
     * @After public void tearDown() throws Exception { }
     */

    @Test
    public void LinkEntitiesNullFirstDontUpdateAllTypesExistingTest()
    {
        for (ELinkTypes linkType : ELinkTypes.values())
        {
            try
            {
                CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_ONE);
                assertFalse(EntityLinkHelper.linkEntities(null, linkType, entity, false));
            }
            catch (IllegalArgumentException ex)
            {
                // Passed
            }
            catch (Exception ex)
            {
                fail(ex.getMessage());
            }
        }
    }

    @Test
    public void LinkEntitiesNullSecondDontUpdateAllTypesExistingTest()
    {
        for (ELinkTypes linkType : ELinkTypes.values())
        {
            try
            {
                CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_ONE);
                assertFalse(EntityLinkHelper.linkEntities(entity, linkType, null, false));
            }
            catch (IllegalArgumentException ex)
            {
                // Passed
            }
            catch (Exception ex)
            {
                fail(ex.getMessage());
            }
        }
    }

    @Test
    public void LinkEntitiesNullBothDontUpdateAllTypesExistingTest()
    {
        for (ELinkTypes linkType : ELinkTypes.values())
        {
            try
            {
                assertFalse(EntityLinkHelper.linkEntities(null, linkType, null, false));
            }
            catch (IllegalArgumentException ex)
            {
                // Passed
            }
            catch (Exception ex)
            {
                fail(ex.getMessage());
            }
        }
    }

    @Test
    public void LinkEntitiesNullFirstUpdateAllTypesExistingTest()
    {
        for (ELinkTypes linkType : ELinkTypes.values())
        {
            try
            {
                CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_ONE);
                assertFalse(EntityLinkHelper.linkEntities(null, linkType, entity, true));
            }
            catch (IllegalArgumentException ex)
            {
                // Passed
            }
            catch (Exception ex)
            {
                fail(ex.getMessage());
            }
        }
    }

    @Test
    public void LinkEntitiesNullSecondUpdateAllTypesExistingTest()
    {
        for (ELinkTypes linkType : ELinkTypes.values())
        {
            try
            {
                CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_ONE);
                assertFalse(EntityLinkHelper.linkEntities(entity, linkType, null, true));
            }
            catch (IllegalArgumentException ex)
            {
                // Passed
            }
            catch (Exception ex)
            {
                fail(ex.getMessage());
            }
        }
    }

    @Test
    public void LinkEntitiesNullBothUpdateAllTypesExistingTest()
    {
        for (ELinkTypes linkType : ELinkTypes.values())
        {
            try
            {
                assertFalse(EntityLinkHelper.linkEntities(null, linkType, null, true));
            }
            catch (IllegalArgumentException ex)
            {
                // Passed
            }
            catch (Exception ex)
            {
                fail(ex.getMessage());
            }
        }
    }

    @Test
    public void LinkEntitiesDontUpdateExistingAllLinkTypesNoExistingTest()
    {
        for (ELinkTypes linkType : ELinkTypes.values())
        {
            CoalesceEntity entity1 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_ONE);
            CoalesceEntity entity2 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_TWO);

            assertTrue(EntityLinkHelper.linkEntities(entity1, linkType, entity2, false));

            assertEquals(1, entity1.getLinkageSection().getChildDataObjects().size());
            assertEquals(1, entity2.getLinkageSection().getChildDataObjects().size());

            assertLinkages(linkType, entity1, entity2);

        }

    }

    @Test
    public void LinkEntitiesDontUpdateExistingAllLinkTypesExistingTest()
    {
        for (ELinkTypes linkType : ELinkTypes.values())
        {

            CoalesceEntity entity1 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_ONE);
            CoalesceEntity entity2 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_TWO);

            EntityLinkHelper.linkEntities(entity1, linkType, entity2, false);

            CoalesceEntity modifiedEntity1 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_ONE);
            CoalesceEntity modifiedEntity2 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_TWO);

            modifiedEntity1.setName("Entity1name");
            modifiedEntity1.setSource("Entity1source");
            modifiedEntity1.setVersion("Entity1version");

            modifiedEntity2.setName("Entity2name");
            modifiedEntity2.setSource("Entity2source");
            modifiedEntity2.setVersion("Entity2version");

            assertTrue(EntityLinkHelper.linkEntities(modifiedEntity1, linkType, modifiedEntity2, false));

            assertLinkages(linkType, entity1, entity2);

            CoalesceEntity mod2Entity1 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_ONE);
            CoalesceEntity mod2Entity2 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_TWO);

            mod2Entity1.setName("Entity1name");
            mod2Entity1.setSource("Entity1source");
            mod2Entity1.setVersion("Entity1version");

            mod2Entity2.setName("Entity2name");
            mod2Entity2.setSource("Entity2source");
            mod2Entity2.setVersion("Entity2version");

            // Test updating link the other way
            assertTrue(EntityLinkHelper.linkEntities(mod2Entity2, linkType.getReciprocalLinkType(), mod2Entity1, false));

            assertLinkages(linkType, entity1, entity2);

        }
    }

    @Test
    public void LinkEntitiesUpdateExistingAllLinkTypesNoExistingTest()
    {
        for (ELinkTypes linkType : ELinkTypes.values())
        {
            CoalesceEntity entity1 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_ONE);
            CoalesceEntity entity2 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_TWO);

            assertTrue(EntityLinkHelper.linkEntities(entity1, linkType, entity2, true));

            assertEquals(1, entity1.getLinkageSection().getChildDataObjects().size());
            assertEquals(1, entity2.getLinkageSection().getChildDataObjects().size());

            assertLinkages(linkType, entity1, entity2);

        }
    }

    @Test
    public void LinkEntitiesUpdateExistingAllLinkTypesExistingTest()
    {
        for (ELinkTypes linkType : ELinkTypes.values())
        {
            CoalesceEntity entity1 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_ONE);
            CoalesceEntity entity2 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_TWO);

            EntityLinkHelper.linkEntities(entity1, linkType, entity2, true);

            CoalesceEntity modifiedEntity1 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_ONE);
            CoalesceEntity modifiedEntity2 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_TWO);

            modifiedEntity1.setName("Entity1name");
            modifiedEntity1.setSource("Entity1source");
            modifiedEntity1.setVersion("Entity1version");

            modifiedEntity2.setName("Entity2name");
            modifiedEntity2.setSource("Entity2source");
            modifiedEntity2.setVersion("Entity2version");

            assertTrue(EntityLinkHelper.linkEntities(modifiedEntity1, linkType, modifiedEntity2, true));

            assertLinkages(linkType, modifiedEntity1, modifiedEntity2);

            CoalesceEntity mod2Entity1 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_ONE);
            CoalesceEntity mod2Entity2 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_TWO);

            mod2Entity1.setName("Entity1name");
            mod2Entity1.setSource("Entity1source");
            mod2Entity1.setVersion("Entity1version");

            mod2Entity2.setName("Entity2name");
            mod2Entity2.setSource("Entity2source");
            mod2Entity2.setVersion("Entity2version");

            // Test updating link the other way
            assertTrue(EntityLinkHelper.linkEntities(mod2Entity2, linkType.getReciprocalLinkType(), mod2Entity1, true));

            assertLinkages(linkType, mod2Entity1, mod2Entity2);
        }
    }

    @Test
    public void LinkEntitiesDetailedNullFirstDontUpdateExistingTest()
    {
        try
        {
            CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_ONE);
            assertFalse(EntityLinkHelper.linkEntities(null,
                                                      ELinkTypes.HasMember,
                                                      entity,
                                                      EntityLinkHelperTest.TOP_SECRET_MARKING,
                                                      "jford",
                                                      Locale.UK,
                                                      false));
        }
        catch (IllegalArgumentException ex)
        {
            // Passed
        }
        catch (Exception ex)
        {
            fail(ex.getMessage());
        }
    }

    @Test
    public void LinkEntitiesDetailedNullSecondDontUpdateExistingTest()
    {
        try
        {
            CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_ONE);
            assertFalse(EntityLinkHelper.linkEntities(entity,
                                                      ELinkTypes.HasOwnershipOf,
                                                      null,
                                                      EntityLinkHelperTest.TOP_SECRET_MARKING,
                                                      "jford",
                                                      Locale.UK,
                                                      false));
        }
        catch (IllegalArgumentException ex)
        {
            // Passed
        }
        catch (Exception ex)
        {
            fail(ex.getMessage());
        }
    }

    @Test
    public void LinkEntitiesDetailedNullBothDontUpdateExistingTest()
    {
        try
        {
            assertFalse(EntityLinkHelper.linkEntities(null,
                                                      ELinkTypes.HasParticipant,
                                                      null,
                                                      EntityLinkHelperTest.TOP_SECRET_MARKING,
                                                      "jford",
                                                      Locale.UK,
                                                      false));
        }
        catch (IllegalArgumentException ex)
        {
            // Passed
        }
        catch (Exception ex)
        {
            fail(ex.getMessage());
        }
    }

    @Test
    public void LinkEntitiesDetailedNullFirstUpdateExistingTest()
    {
        try
        {
            CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_ONE);
            assertFalse(EntityLinkHelper.linkEntities(null,
                                                      ELinkTypes.HasUseOf,
                                                      entity,
                                                      EntityLinkHelperTest.TOP_SECRET_MARKING,
                                                      "jford",
                                                      Locale.UK,
                                                      true));
        }
        catch (IllegalArgumentException ex)
        {
            // Passed
        }
        catch (Exception ex)
        {
            fail(ex.getMessage());
        }
    }

    @Test
    public void LinkEntitiesDetailedNullSecondUpdateExistingTest()
    {
        try
        {
            CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_ONE);
            assertFalse(EntityLinkHelper.linkEntities(entity,
                                                      ELinkTypes.IsAMemberOf,
                                                      null,
                                                      EntityLinkHelperTest.TOP_SECRET_MARKING,
                                                      "jford",
                                                      Locale.UK,
                                                      true));
        }
        catch (IllegalArgumentException ex)
        {
            // Passed
        }
        catch (Exception ex)
        {
            fail(ex.getMessage());
        }
    }

    @Test
    public void LinkEntitiesDetailedNullBothUpdateExistingTest()
    {
        try
        {
            assertFalse(EntityLinkHelper.linkEntities(null,
                                                      ELinkTypes.IsAParticipantOf,
                                                      null,
                                                      EntityLinkHelperTest.TOP_SECRET_MARKING,
                                                      "jford",
                                                      Locale.UK,
                                                      true));
        }
        catch (IllegalArgumentException ex)
        {
            // Passed
        }
        catch (Exception ex)
        {
            fail(ex.getMessage());
        }
    }

    @Test
    public void LinkEntitiesDetailedDontUpdateExistingANoExistingTest()
    {
        CoalesceEntity entity1 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_ONE);
        CoalesceEntity entity2 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_TWO);

        assertTrue(EntityLinkHelper.linkEntities(entity1,
                                                 ELinkTypes.IsAPeerOf,
                                                 entity2,
                                                 EntityLinkHelperTest.TOP_SECRET_MARKING,
                                                 "jford",
                                                 Locale.UK,
                                                 false));

        assertEquals(1, entity1.getLinkageSection().getChildDataObjects().size());
        assertEquals(1, entity2.getLinkageSection().getChildDataObjects().size());

        assertLinkages(ELinkTypes.IsAPeerOf, EntityLinkHelperTest.TOP_SECRET_MARKING, "jford", Locale.UK, entity1, entity2);

    }

    @Test
    public void LinkEntitiesDetailedDontUpdateExistingExistingTest()
    {
        CoalesceEntity entity1 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_ONE);
        CoalesceEntity entity2 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_TWO);

        EntityLinkHelper.linkEntities(entity1,
                                      ELinkTypes.IsBeingWatchedBy,
                                      entity2,
                                      EntityLinkHelperTest.TOP_SECRET_MARKING,
                                      "jford",
                                      Locale.UK,
                                      false);

        CoalesceEntity modifiedEntity1 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_ONE);
        CoalesceEntity modifiedEntity2 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_TWO);

        modifiedEntity1.setName("Entity1name");
        modifiedEntity1.setSource("Entity1source");
        modifiedEntity1.setVersion("Entity1version");

        modifiedEntity2.setName("Entity2name");
        modifiedEntity2.setSource("Entity2source");
        modifiedEntity2.setVersion("Entity2version");

        assertTrue(EntityLinkHelper.linkEntities(modifiedEntity1,
                                                 ELinkTypes.IsBeingWatchedBy,
                                                 modifiedEntity2,
                                                 EntityLinkHelperTest.SECRET_USA_MARKING,
                                                 "bob",
                                                 Locale.US,
                                                 false));

        assertLinkages(ELinkTypes.IsBeingWatchedBy,
                       EntityLinkHelperTest.TOP_SECRET_MARKING,
                       "jford",
                       Locale.UK,
                       entity1,
                       entity2);

    }

    @Test
    public void LinkEntitiesDetailedUpdateExistingNoExistingTest()
    {
        CoalesceEntity entity1 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_ONE);
        CoalesceEntity entity2 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_TWO);

        assertTrue(EntityLinkHelper.linkEntities(entity1,
                                                 ELinkTypes.IsChildOf,
                                                 entity2,
                                                 EntityLinkHelperTest.TOP_SECRET_MARKING,
                                                 "jford",
                                                 Locale.UK,
                                                 true));

        assertEquals(1, entity1.getLinkageSection().getChildDataObjects().size());
        assertEquals(1, entity2.getLinkageSection().getChildDataObjects().size());

        assertLinkages(ELinkTypes.IsChildOf, EntityLinkHelperTest.TOP_SECRET_MARKING, "jford", Locale.UK, entity1, entity2);

    }

    @Test
    public void LinkEntitiesDetailedUpdateExistingExistingTest()
    {
        CoalesceEntity entity1 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_ONE);
        CoalesceEntity entity2 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_TWO);

        EntityLinkHelper.linkEntities(entity1,
                                      ELinkTypes.IsOwnedBy,
                                      entity2,
                                      EntityLinkHelperTest.TOP_SECRET_MARKING,
                                      "jford",
                                      Locale.UK,
                                      true);

        CoalesceEntity modifiedEntity1 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_ONE);
        CoalesceEntity modifiedEntity2 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_TWO);

        modifiedEntity1.setName("Entity1name");
        modifiedEntity1.setSource("Entity1source");
        modifiedEntity1.setVersion("Entity1version");

        modifiedEntity2.setName("Entity2name");
        modifiedEntity2.setSource("Entity2source");
        modifiedEntity2.setVersion("Entity2version");

        assertTrue(EntityLinkHelper.linkEntities(modifiedEntity1,
                                                 ELinkTypes.IsOwnedBy,
                                                 modifiedEntity2,
                                                 EntityLinkHelperTest.SECRET_USA_MARKING,
                                                 "bob",
                                                 Locale.US,
                                                 true));

        assertLinkages(ELinkTypes.IsOwnedBy,
                       EntityLinkHelperTest.SECRET_USA_MARKING,
                       "bob",
                       Locale.US,
                       modifiedEntity1,
                       modifiedEntity2);

    }

    @Test
    public void UnLinkEntitiesNullFirstTest()
    {
        try
        {
            CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_ONE);
            assertFalse(EntityLinkHelper.unLinkEntities(null, entity));
        }
        catch (IllegalArgumentException ex)
        {
            // Passed
        }
        catch (Exception ex)
        {
            fail(ex.getMessage());
        }
    }

    @Test
    public void UnLinkEntitiesNullSecondTest()
    {
        try
        {
            CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_ONE);
            assertFalse(EntityLinkHelper.unLinkEntities(entity, null));
        }
        catch (IllegalArgumentException ex)
        {
            // Passed
        }
        catch (Exception ex)
        {
            fail(ex.getMessage());
        }
    }

    @Test
    public void UnLinkEntitiesNullBothTest()
    {
        try
        {
            assertFalse(EntityLinkHelper.unLinkEntities(null, null));
        }
        catch (IllegalArgumentException ex)
        {
            // Passed
        }
        catch (Exception ex)
        {
            fail(ex.getMessage());
        }
    }

    @Test
    public void UnLinkEntitiesLinkTypeNullFirstTest()
    {
        try
        {
            CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_ONE);
            assertFalse(EntityLinkHelper.unLinkEntities(null, entity, ELinkTypes.IsParentOf));
        }
        catch (IllegalArgumentException ex)
        {
            // Passed
        }
        catch (Exception ex)
        {
            fail(ex.getMessage());
        }
    }

    @Test
    public void UnLinkEntitiesLinkTypeNullSecondTest()
    {
        try
        {
            CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_ONE);
            assertFalse(EntityLinkHelper.unLinkEntities(entity, null, ELinkTypes.IsUsedBy));
        }
        catch (IllegalArgumentException ex)
        {
            // Passed
        }
        catch (Exception ex)
        {
            fail(ex.getMessage());
        }
    }

    @Test
    public void UnLinkEntitiesLinkTypeNullBothTest()
    {
        try
        {
            assertFalse(EntityLinkHelper.unLinkEntities(null, null, ELinkTypes.IsWatching));
        }
        catch (IllegalArgumentException ex)
        {
            // Passed
        }
        catch (Exception ex)
        {
            fail(ex.getMessage());
        }
    }

    @Test
    public void UnLinkEntitiesLinkTypeNullLinkTypeTest()
    {
        CoalesceEntity entity1 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_ONE);
        CoalesceEntity entity2 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_TWO);

        EntityLinkHelper.linkEntities(entity1, ELinkTypes.Created, entity2, true);

        assertTrue(EntityLinkHelper.unLinkEntities(entity1, entity2, null));
        assertEquals(ECoalesceDataObjectStatus.DELETED,
                     entity1.getLinkageSection().getChildDataObjects().values().iterator().next().getStatus());
        assertEquals(ECoalesceDataObjectStatus.DELETED,
                     entity2.getLinkageSection().getChildDataObjects().values().iterator().next().getStatus());

    }

    @Test
    public void UnLinkEntitiesAllLinkTypesTest()
    {

        for (ELinkTypes linkType : ELinkTypes.values())
        {

            CoalesceEntity entity1 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_ONE);
            CoalesceEntity entity2 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_TWO);

            EntityLinkHelper.linkEntities(entity1, linkType, entity2, true);

            assertTrue(EntityLinkHelper.unLinkEntities(entity1, entity2));
            assertEquals(ECoalesceDataObjectStatus.DELETED,
                         entity1.getLinkageSection().getChildDataObjects().values().iterator().next().getStatus());
            assertEquals(ECoalesceDataObjectStatus.DELETED,
                         entity2.getLinkageSection().getChildDataObjects().values().iterator().next().getStatus());

        }
    }

    @Test
    public void UnLinkEntitiesNotLinkedTest()
    {

        CoalesceEntity entity1 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_ONE);
        CoalesceEntity entity2 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_TWO);

        assertTrue(EntityLinkHelper.unLinkEntities(entity1, entity2));
        assertTrue(entity1.getLinkageSection().getChildDataObjects().isEmpty());
        assertTrue(entity2.getLinkageSection().getChildDataObjects().isEmpty());

    }

    @Test
    public void UnLinkEntitiesNotLinkedFirstTest()
    {

        CoalesceEntity entity1 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_ONE);
        CoalesceEntity entity2 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_TWO);
        CoalesceEntity entity3 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);

        EntityLinkHelper.linkEntities(entity1, ELinkTypes.Created, entity2, true);

        assertTrue(EntityLinkHelper.unLinkEntities(entity1, entity3));
        assertEquals(ECoalesceDataObjectStatus.ACTIVE,
                     entity1.getLinkageSection().getChildDataObjects().values().iterator().next().getStatus());
        assertEquals(ECoalesceDataObjectStatus.ACTIVE,
                     entity2.getLinkageSection().getChildDataObjects().values().iterator().next().getStatus());

        for (CoalesceDataObject xdo : entity3.getChildDataObjects().values())
        {
            assertEquals(ECoalesceDataObjectStatus.ACTIVE, xdo.getStatus());
        }

    }

    @Test
    public void UnLinkEntitiesNotLinkedSecondTest()
    {

        CoalesceEntity entity1 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_ONE);
        CoalesceEntity entity2 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_TWO);
        CoalesceEntity entity3 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);

        EntityLinkHelper.linkEntities(entity1, ELinkTypes.Created, entity2, true);

        assertTrue(EntityLinkHelper.unLinkEntities(entity3, entity2));
        assertEquals(ECoalesceDataObjectStatus.ACTIVE,
                     entity1.getLinkageSection().getChildDataObjects().values().iterator().next().getStatus());
        assertEquals(ECoalesceDataObjectStatus.ACTIVE,
                     entity2.getLinkageSection().getChildDataObjects().values().iterator().next().getStatus());

        for (CoalesceDataObject xdo : entity3.getChildDataObjects().values())
        {
            assertEquals(ECoalesceDataObjectStatus.ACTIVE, xdo.getStatus());
        }

    }

    @Test
    public void UnLinkEntitiesLinkTypesTest()
    {
        CoalesceEntity entity1 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_ONE);
        CoalesceEntity entity2 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_TWO);

        EntityLinkHelper.linkEntities(entity1, ELinkTypes.Created, entity2, true);

        assertTrue(EntityLinkHelper.unLinkEntities(entity1, entity2, ELinkTypes.Created));
        assertEquals(ECoalesceDataObjectStatus.DELETED,
                     entity1.getLinkageSection().getChildDataObjects().values().iterator().next().getStatus());
        assertEquals(ECoalesceDataObjectStatus.DELETED,
                     entity2.getLinkageSection().getChildDataObjects().values().iterator().next().getStatus());

    }

    @Test
    public void UnLinkEntitiesLinkTypesMismatchTest()
    {
        CoalesceEntity entity1 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_ONE);
        CoalesceEntity entity2 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_TWO);

        EntityLinkHelper.linkEntities(entity1, ELinkTypes.HasParticipant, entity2, true);

        assertTrue(EntityLinkHelper.unLinkEntities(entity1, entity2, ELinkTypes.HasUseOf));
        assertEquals(ECoalesceDataObjectStatus.ACTIVE,
                     entity1.getLinkageSection().getChildDataObjects().values().iterator().next().getStatus());
        assertEquals(ECoalesceDataObjectStatus.ACTIVE,
                     entity2.getLinkageSection().getChildDataObjects().values().iterator().next().getStatus());

    }

    @Test
    public void UnLinkEntitiesLinkageTypeNotLinkedTest()
    {

        CoalesceEntity entity1 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_ONE);
        CoalesceEntity entity2 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_TWO);

        assertTrue(EntityLinkHelper.unLinkEntities(entity1, entity2, ELinkTypes.Created));
        assertTrue(entity1.getLinkageSection().getChildDataObjects().isEmpty());
        assertTrue(entity2.getLinkageSection().getChildDataObjects().isEmpty());

    }

    @Test
    public void UnLinkEntitiesLinkageTypeNotLinkedFirstTest()
    {

        CoalesceEntity entity1 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_ONE);
        CoalesceEntity entity2 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_TWO);
        CoalesceEntity entity3 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);

        EntityLinkHelper.linkEntities(entity1, ELinkTypes.HasMember, entity2, true);

        assertTrue(EntityLinkHelper.unLinkEntities(entity1, entity3, ELinkTypes.HasMember));
        assertEquals(ECoalesceDataObjectStatus.ACTIVE,
                     entity1.getLinkageSection().getChildDataObjects().values().iterator().next().getStatus());
        assertEquals(ECoalesceDataObjectStatus.ACTIVE,
                     entity2.getLinkageSection().getChildDataObjects().values().iterator().next().getStatus());

        for (CoalesceDataObject xdo : entity3.getChildDataObjects().values())
        {
            assertEquals(ECoalesceDataObjectStatus.ACTIVE, xdo.getStatus());
        }

    }

    @Test
    public void UnLinkEntitiesLinkagetypeNotLinkedSecondTest()
    {

        CoalesceEntity entity1 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_ONE);
        CoalesceEntity entity2 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION_NO_LINKS_TWO);
        CoalesceEntity entity3 = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);

        EntityLinkHelper.linkEntities(entity1, ELinkTypes.HasOwnershipOf, entity2, true);

        assertTrue(EntityLinkHelper.unLinkEntities(entity3, entity2, ELinkTypes.HasOwnershipOf));
        assertEquals(ECoalesceDataObjectStatus.ACTIVE,
                     entity1.getLinkageSection().getChildDataObjects().values().iterator().next().getStatus());
        assertEquals(ECoalesceDataObjectStatus.ACTIVE,
                     entity2.getLinkageSection().getChildDataObjects().values().iterator().next().getStatus());

        for (CoalesceDataObject xdo : entity3.getChildDataObjects().values())
        {
            assertEquals(ECoalesceDataObjectStatus.ACTIVE, xdo.getStatus());
        }

    }

    // -----------------------------------------------------------------------//
    // Private Methods
    // -----------------------------------------------------------------------//

    private void assertLinkages(ELinkTypes linkType,
                                Marking classificationMarking,
                                String modifiedBy,
                                Locale inputLang,
                                CoalesceEntity entity1,
                                CoalesceEntity entity2)
    {

        assertLinkage(linkType,
                      classificationMarking,
                      modifiedBy,
                      inputLang,
                      entity1,
                      entity2,
                      (CoalesceLinkage) entity1.getLinkageSection().getChildDataObjects().values().iterator().next());

        assertLinkage(linkType.getReciprocalLinkType(),
                      classificationMarking,
                      modifiedBy,
                      inputLang,
                      entity2,
                      entity1,
                      (CoalesceLinkage) entity2.getLinkageSection().getChildDataObjects().values().iterator().next());

    }

    private void assertLinkages(ELinkTypes linkType, CoalesceEntity entity1, CoalesceEntity entity2)
    {

        assertLinkage(linkType,
                      entity1,
                      entity2,
                      (CoalesceLinkage) entity1.getLinkageSection().getChildDataObjects().values().iterator().next());

        assertLinkage(linkType.getReciprocalLinkType(),
                      entity2,
                      entity1,
                      (CoalesceLinkage) entity2.getLinkageSection().getChildDataObjects().values().iterator().next());

    }

    private void assertLinkage(ELinkTypes linkType, CoalesceEntity entity1, CoalesceEntity entity2, CoalesceLinkage linkage)
    {

        assertLinkage(linkType, EntityLinkHelperTest.UNCLASSIFIED_MARKING, "", Locale.US, entity1, entity2, linkage);
    }

    private void assertLinkage(ELinkTypes linkType,
                               Marking classificationMarking,
                               String modifiedBy,
                               Locale inputLang,
                               CoalesceEntity entity1,
                               CoalesceEntity entity2,
                               CoalesceLinkage linkage)
    {

        assertEquals(linkType, linkage.getLinkType());
        assertEquals(classificationMarking, linkage.getClassificationMarking());
        assertEquals(modifiedBy, linkage.getModifiedBy());
        assertEquals(inputLang, linkage.getInputLang());

        assertEquals(entity1.getKey(), linkage.getEntity1Key());
        assertEquals(entity1.getName(), linkage.getEntity1Name());
        assertEquals(entity1.getSource(), linkage.getEntity1Source());
        assertEquals(entity1.getVersion(), linkage.getEntity1Version());

        assertEquals(entity2.getKey(), linkage.getEntity2Key());
        assertEquals(entity2.getName(), linkage.getEntity2Name());
        assertEquals(entity2.getSource(), linkage.getEntity2Source());
        assertEquals(entity2.getVersion(), linkage.getEntity2Version());
        assertEquals(linkage.getInputLang(), inputLang);
    }

}
