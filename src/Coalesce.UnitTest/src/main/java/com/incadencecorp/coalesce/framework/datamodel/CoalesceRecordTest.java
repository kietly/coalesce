package com.incadencecorp.coalesce.framework.datamodel;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.NullArgumentException;
import org.joda.time.DateTime;
import org.junit.Test;

import com.incadencecorp.coalesce.common.CoalesceTypeInstances;
import com.incadencecorp.coalesce.common.exceptions.CoalesceException;
import com.incadencecorp.coalesce.common.exceptions.CoalesceInvalidFieldException;
import com.incadencecorp.coalesce.common.helpers.JodaDateTimeHelper;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceEntity;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceField;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceFieldDefinition;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceRecord;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceRecordset;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceSection;
import com.incadencecorp.coalesce.framework.datamodel.ECoalesceFieldDataTypes;
import com.incadencecorp.coalesce.framework.generatedjaxb.Entity.Section.Recordset.Record;

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

public class CoalesceRecordTest {

    /*
     * @BeforeClass public static void setUpBeforeClass() throws Exception { }
     * 
     * @AfterClass public static void tearDownAfterClass() throws Exception { }
     * 
     * @Before public void setUp() throws Exception { }
     * 
     * @After public void tearDown() throws Exception { }
     */

    @Test(expected = NullArgumentException.class)
    public void createNullParentTest()
    {
        @SuppressWarnings("unused")
        CoalesceRecord record = CoalesceRecord.create(null, "New Record");

    }

    @Test(expected = NullArgumentException.class)
    public void createNullNameTest()
    {
        CoalesceRecordset recordset = getMissionRecordset();

        @SuppressWarnings("unused")
        CoalesceRecord record = CoalesceRecord.create(recordset, null);
    }

    @Test(expected = NullArgumentException.class)
    public void createNullBothTest()
    {
        @SuppressWarnings("unused")
        CoalesceRecord record = CoalesceRecord.create(null, null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void createEmptyNameTest()
    {
        CoalesceRecordset recordset = getMissionRecordset();

        @SuppressWarnings("unused")
        CoalesceRecord record = CoalesceRecord.create(recordset, "");

    }

    @Test(expected = IllegalArgumentException.class)
    public void createWhiteSpaceNameTest()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);
        CoalesceRecordset recordset = (CoalesceRecordset) entity.getDataObjectForNamePath(CoalesceTypeInstances.TEST_MISSION_RECORDSET_PATH);

        @SuppressWarnings("unused")
        CoalesceRecord record = CoalesceRecord.create(recordset, "   ");

    }

    @Test
    public void createFromXmlTest()
    {
        CoalesceRecordset recordset = getMissionRecordset();

        CoalesceRecord record = recordset.getRecords().get(0);

        assertEquals(17, record.getFields().size());
        assertEquals("D7067C3F-54B1-47FD-9C8A-A2D7946E0C2A", record.getFieldByName("ActionNumber").getKey());
        assertEquals("6CB12648-A061-4CC5-B593-3D0407EF4392", record.getFieldByName("MissionName").getKey());
        assertEquals("37496274-2077-454F-9CB3-5A57C1753640", record.getFieldByName("MissionAddress").getKey());

    }

    @Test
    public void createNewRecordTest()
    {
        CoalesceRecordset recordset = getMissionRecordset();

        CoalesceRecord newRecord = CoalesceRecord.create(recordset, "New Record");

        assertNotNull(newRecord);
        assertTrue(recordset.getChildDataObjects().containsValue(newRecord));
        assertEquals(recordset, newRecord.getParent());
        assertEquals("New Record", newRecord.getName());
        assertEquals(17, newRecord.getFields().size());

        for (CoalesceField<?> field : newRecord.getFields())
        {
            CoalesceFieldDefinition fd = recordset.getFieldDefinition(field.getName());

            assertNotNull(fd);
            CoalesceFieldDefinitionTest.assertNewField(fd, field);
        }

    }

    @Test
    public void createExistingNameTest()
    {
        CoalesceRecordset recordset = getMissionRecordset();

        CoalesceRecord existingRecord = recordset.getItem(0);

        CoalesceRecord newRecord = CoalesceRecord.create(recordset, "New Record");

        assertFalse(existingRecord == newRecord);

    }

    @Test(expected = NullArgumentException.class)
    public void initializeNullParentTest()
    {
        CoalesceRecord record = new CoalesceRecord();
        record.initialize(null, new Record());

    }

    @Test(expected = NullArgumentException.class)
    public void initializeNullRecordTest()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);
        CoalesceRecordset recordset = (CoalesceRecordset) entity.getDataObjectForNamePath(CoalesceTypeInstances.TEST_MISSION_RECORDSET_PATH);

        CoalesceRecord record = new CoalesceRecord();
        record.initialize(recordset, null);
    }

    @Test(expected = NullArgumentException.class)
    public void initializeNullBothTest()
    {
        @SuppressWarnings("unused")
        CoalesceRecord record = CoalesceRecord.create(null, null);

    }

    @Test
    public void initializeNewRecordTest()
    {
        CoalesceRecordset recordset = getMissionRecordset();

        CoalesceRecord newRecord = new CoalesceRecord();
        Record newEntityRecord = new Record();
        newRecord.initialize(recordset, newEntityRecord);

        assertTrue(recordset.getChildDataObjects().containsValue(newRecord));
        assertEquals(recordset, newRecord.getParent());
        assertEquals(newEntityRecord.getKey(), newRecord.getKey());
        assertEquals("", newRecord.getName());
        assertEquals(0, newRecord.getFields().size());

    }

    @Test
    public void keyTest()
    {
        CoalesceRecord record = getMissionRecord();

        assertEquals("9A03833C-AC15-47C8-A037-1FFFD13A26E9", record.getKey());

        UUID guid = UUID.randomUUID();

        record.setKey(guid);

        assertEquals(guid.toString(), record.getKey());

        UUID guid2 = UUID.randomUUID();

        record.setKey(guid2.toString());

        assertEquals(guid2.toString(), record.getKey());

    }

    @Test
    public void nameTest()
    {
        CoalesceRecord record = getMissionRecord();

        assertEquals("Mission Information Recordset Record", record.getName());

        record.setName("New Information Recordset Record");

        assertEquals("New Information Recordset Record", record.getName());

    }

    @Test
    public void typeTest()
    {
        CoalesceRecord record = getMissionRecord();

        assertEquals("record", record.getType());

        CoalesceEntity newEntity = CoalesceEntity.create("Operation", "Portal", "1.2.3.4", "ID", "Type");
        CoalesceSection newSection = CoalesceSection.create(newEntity, "Operation/New Section");
        CoalesceRecordset newRecordset = CoalesceRecordset.create(newSection, "New Recordset");
        CoalesceRecord newRecord = newRecordset.addNew();

        assertEquals("record", newRecord.getType());

    }

    @Test
    public void getFieldsTest()
    {
        CoalesceRecord record = getMissionRecord();

        List<CoalesceField<?>> fields = record.getFields();

        assertEquals(17, fields.size());

        boolean firstFound = false;
        boolean middleFound = false;
        boolean lastFound = false;

        for (CoalesceField<?> field : fields)
        {
            switch (field.getKey()) {
            case "D7067C3F-54B1-47FD-9C8A-A2D7946E0C2A":

                firstFound = true;

                break;

            case "C6695025-F302-4C28-A428-EF5AE54C3CFC":

                middleFound = true;

                break;

            case "37496274-2077-454F-9CB3-5A57C1753640":

                lastFound = true;

                break;

            }
        }

        assertTrue(firstFound);
        assertTrue(middleFound);
        assertTrue(lastFound);

    }

    @Test
    public void getFieldNamesTest()
    {
        CoalesceRecord record = getMissionRecord();

        List<String> fieldNames = record.getFieldNames();

        assertEquals(17, fieldNames.size());

        boolean firstFound = false;
        boolean middleFound = false;
        boolean lastFound = false;

        for (String fieldName : fieldNames)
        {
            switch (fieldName) {
            case "ActionNumber":

                firstFound = true;

                break;

            case "MissionType":

                middleFound = true;

                break;

            case "MissionAddress":

                lastFound = true;

                break;

            }
        }

        assertTrue(firstFound);
        assertTrue(middleFound);
        assertTrue(lastFound);

    }

    @Test
    public void getFieldKeysTest()
    {
        CoalesceRecord record = getMissionRecord();

        List<String> fieldKeys = record.getFieldKeys();

        assertEquals(17, fieldKeys.size());

        boolean firstFound = false;
        boolean middleFound = false;
        boolean lastFound = false;

        for (String fieldKey : fieldKeys)
        {
            switch (fieldKey) {
            case "D7067C3F-54B1-47FD-9C8A-A2D7946E0C2A":

                firstFound = true;

                break;

            case "C6695025-F302-4C28-A428-EF5AE54C3CFC":

                middleFound = true;

                break;

            case "37496274-2077-454F-9CB3-5A57C1753640":

                lastFound = true;

                break;

            }
        }

        assertTrue(firstFound);
        assertTrue(middleFound);
        assertTrue(lastFound);

    }

    @Test
    public void getFieldByKeyTest()
    {
        CoalesceRecord record = getMissionRecord();

        CoalesceField<?> field = record.getFieldByKey("D7067C3F-54B1-47FD-9C8A-A2D7946E0C2A");
        assertEquals("D7067C3F-54B1-47FD-9C8A-A2D7946E0C2A", field.getKey());

        field = null;
        field = record.getFieldByKey("C6695025-F302-4C28-A428-EF5AE54C3CFC");
        assertEquals("C6695025-F302-4C28-A428-EF5AE54C3CFC", field.getKey());

        field = null;
        field = record.getFieldByKey("37496274-2077-454F-9CB3-5A57C1753640");
        assertEquals("37496274-2077-454F-9CB3-5A57C1753640", field.getKey());

        field = null;
        field = record.getFieldByKey("B0101194-B600-4FE6-BBB8-019300C812DC");
        assertNull(field);

    }

    @Test
    public void getFieldValueTest() throws CoalesceException
    {
        CoalesceRecord record = getMissionRecord();

        assertEquals("0", record.getFieldValue("ActionNumber"));

        assertEquals("Checkpoint Operations", record.getFieldValue("MissionType"));

        assertEquals(null, record.getFieldValue("MissionAddress"));

    }

    @Test(expected = CoalesceInvalidFieldException.class)
    public void getFieldValueInvalidFieldNameTest() throws CoalesceException
    {
        CoalesceRecord record = getMissionRecord();

        @SuppressWarnings("unused")
        String value = record.getFieldValue("Invalid");

    }

    @Test
    public void getTypedFieldValueTest() throws CoalesceException, UnsupportedEncodingException
    {
        CoalesceRecordset recordset = getMissionRecordset();

        CoalesceFieldDefinition.create(recordset, "Boolean", "", "(U)", false);
        CoalesceFieldDefinition.create(recordset, "Binary", ECoalesceFieldDataTypes.BinaryType);

        CoalesceRecord newRecord = recordset.addNew();
        newRecord.getFieldByName("MissionIndicatorNumberBASE10").setTypedValue(CoalesceTypeInstances.TEST_MISSION_BASE64_VALUE);
        newRecord.getFieldByName("Boolean").setTypedValue(true);
        newRecord.getFieldByName("MissionStartDateTime").setTypedValue(JodaDateTimeHelper.fromXmlDateTimeUTC(CoalesceTypeInstances.TEST_MISSION_START_TIME_VALUE));

        String byteString = "Testing String";
        byte[] dataBytes = byteString.getBytes("US-ASCII");
        newRecord.getFieldByName("Binary").setTypedValue(dataBytes);

        assertEquals(CoalesceTypeInstances.TEST_MISSION_BASE64_VALUE,
                     newRecord.getIntegerFieldValue("MissionIndicatorNumberBASE10"));
        assertEquals(true, newRecord.getBooleanFieldValue("Boolean"));
        assertEquals(JodaDateTimeHelper.fromXmlDateTimeUTC(CoalesceTypeInstances.TEST_MISSION_START_TIME_VALUE),
                     newRecord.getDateTimeFieldValue("MissionStartDateTime"));
        assertArrayEquals(dataBytes, newRecord.getBinaryFieldValue("Binary"));

    }

    @Test(expected = CoalesceInvalidFieldException.class)
    public void getTypedFieldValueIntegerInvalidFieldNameTest() throws CoalesceException
    {
        CoalesceRecord record = getMissionRecord();

        @SuppressWarnings("unused")
        int value = record.getIntegerFieldValue("Invalid");

    }

    @Test(expected = CoalesceInvalidFieldException.class)
    public void getTypedFieldValueBooleanInvalidFieldNameTest() throws CoalesceException
    {
        CoalesceRecord record = getMissionRecord();

        @SuppressWarnings("unused")
        boolean value = record.getBooleanFieldValue("Invalid");

    }

    @Test(expected = CoalesceInvalidFieldException.class)
    public void getTypedFieldValueDateTimeInvalidFieldNameTest() throws CoalesceException
    {
        CoalesceRecord record = getMissionRecord();

        @SuppressWarnings("unused")
        DateTime value = record.getDateTimeFieldValue("Invalid");

    }

    @Test(expected = CoalesceInvalidFieldException.class)
    public void getTypedFieldValueBinaryInvalidFieldNameTest() throws CoalesceException
    {
        CoalesceRecord record = getMissionRecord();

        @SuppressWarnings("unused")
        byte[] value = record.getBinaryFieldValue("Invalid");

    }

    @Test
    public void fieldValueAsTest() throws UnsupportedEncodingException, CoalesceException
    {
        CoalesceRecordset recordset = getMissionRecordset();

        CoalesceFieldDefinition.create(recordset, "Boolean", "", "(U)", false);
        CoalesceFieldDefinition.create(recordset, "Binary", ECoalesceFieldDataTypes.BinaryType);

        CoalesceRecord newRecord = recordset.addNew();

        CoalesceField<?> binaryField = (CoalesceField<?>) newRecord.getFieldByName("Binary");

        assertEquals("default address", newRecord.getFieldValueAsString("Invalid", "default address"));
        assertEquals(5555, newRecord.getFieldValueAsInteger("MissionIndicatorNumberBASE10", 5555));
        assertEquals(3333, newRecord.getFieldValueAsInteger("Invalid", 3333));
        assertEquals(true, newRecord.getFieldValueAsBoolean("Invalid", true));

        DateTime testDate = JodaDateTimeHelper.convertyyyyMMddDateStringToDateTime("19800122");
        assertEquals(testDate, newRecord.getFieldValueAsDate("MissionEndDateTime", testDate));
        assertEquals(testDate, newRecord.getFieldValueAsDate("Invalid", testDate));

        String defaultByteString = "DefaultTesting String";
        byte[] defaultDataBytes = defaultByteString.getBytes("US-ASCII");
        assertArrayEquals(defaultDataBytes, newRecord.getFieldValueAsByteArray("Invalid", defaultDataBytes));

        newRecord.setFieldValue("MissionIndicatorNumberBASE10", CoalesceTypeInstances.TEST_MISSION_BASE64_VALUE);
        newRecord.setFieldValue("Boolean", true);
        newRecord.setFieldValue("MissionStartDateTime",
                                JodaDateTimeHelper.fromXmlDateTimeUTC(CoalesceTypeInstances.TEST_MISSION_START_TIME_VALUE));

        String byteString = "Testing String";
        byte[] dataBytes = byteString.getBytes("US-ASCII");
        newRecord.setFieldValue("Binary", dataBytes);

        assertEquals(CoalesceTypeInstances.TEST_MISSION_BASE64_VALUE,
                     newRecord.getFieldValueAsInteger("MissionIndicatorNumberBASE10", 5555));
        assertEquals(true, newRecord.getFieldValueAsBoolean("Boolean", false));
        assertEquals(JodaDateTimeHelper.fromXmlDateTimeUTC(CoalesceTypeInstances.TEST_MISSION_START_TIME_VALUE),
                     newRecord.getFieldValueAsDate("MissionStartDateTime", testDate));

        assertArrayEquals(dataBytes, newRecord.getFieldValueAsByteArray("Binary", defaultDataBytes));
        assertEquals("", binaryField.getFilename());
        assertEquals("", binaryField.getExtension());
        assertEquals("", binaryField.getMimeType());

    }

    @Test
    public void fieldValueAsBinaryFileNameTest() throws UnsupportedEncodingException, CoalesceException
    {
        CoalesceRecordset recordset = getMissionRecordset();

        CoalesceFieldDefinition.create(recordset, "Binary", ECoalesceFieldDataTypes.BinaryType);

        CoalesceRecord newRecord = recordset.addNew();

        CoalesceField<?> binaryField = (CoalesceField<?>) newRecord.getFieldByName("Binary");

        String defaultByteString = "DefaultTesting String";
        byte[] defaultDataBytes = defaultByteString.getBytes("US-ASCII");

        String filenameByteString = "Testing String";
        byte[] filenameDataBytes = filenameByteString.getBytes("US-ASCII");
        newRecord.setFieldValue("Binary", filenameDataBytes, "testingFileName");

        assertArrayEquals(filenameDataBytes, newRecord.getFieldValueAsByteArray("Binary", defaultDataBytes));
        assertEquals("{testingFileName}", binaryField.getFilename());
        assertEquals("jpg", binaryField.getExtension());
        assertEquals("", binaryField.getMimeType());

        newRecord.setFieldValue("Binary", filenameDataBytes, null);

        assertArrayEquals(filenameDataBytes, newRecord.getFieldValueAsByteArray("Binary", defaultDataBytes));
        assertEquals("{testingFileName}", binaryField.getFilename());
        assertEquals("jpg", binaryField.getExtension());
        assertEquals("", binaryField.getMimeType());

        newRecord.setFieldValue("Binary", filenameDataBytes, "");

        assertArrayEquals(filenameDataBytes, newRecord.getFieldValueAsByteArray("Binary", defaultDataBytes));
        assertEquals("{testingFileName}", binaryField.getFilename());
        assertEquals("jpg", binaryField.getExtension());
        assertEquals("", binaryField.getMimeType());

        newRecord.setFieldValue("Binary", filenameDataBytes, "   ");

        assertArrayEquals(filenameDataBytes, newRecord.getFieldValueAsByteArray("Binary", defaultDataBytes));
        assertEquals("{testingFileName}", binaryField.getFilename());
        assertEquals("jpg", binaryField.getExtension());
        assertEquals("", binaryField.getMimeType());

    }

    @Test
    public void hasFieldTest()
    {
        CoalesceRecord record = getMissionRecord();

        assertTrue(record.hasField("MissionName"));
        assertFalse(record.hasField("Missionname"));
        assertFalse(record.hasField("Invalid"));
    }

    @Test
    public void noIndexTest()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);

        CoalesceRecordset recordset = getMissionRecordset(entity);

        CoalesceRecord record = recordset.addNew();
        record.setName("New Record");

        assertFalse(record.getNoIndex());

        record.setNoIndex(true);

        String entityXml = entity.toXml();

        CoalesceEntity desEntity = CoalesceEntity.create(entityXml);
        CoalesceRecord desRecord = (CoalesceRecord) desEntity.getDataObjectForNamePath(CoalesceTypeInstances.TEST_MISSION_RECORDSET_PATH
                + "/New Record");

        assertTrue(desRecord.getNoIndex());

        CoalesceEntity newEntity = CoalesceEntity.create("Operation", "Portal", "1.2.3.4", "ID", "Type");
        CoalesceSection newSection = CoalesceSection.create(newEntity, "Operation/New Section");
        CoalesceRecordset newRecordset = CoalesceRecordset.create(newSection, "New Recordset");
        CoalesceRecord newRecord = newRecordset.addNew();

        assertFalse(newRecord.getNoIndex());

    }

    @Test
    public void DateCreatedTest()
    {
        CoalesceRecord record = getMissionRecord();

        assertEquals(JodaDateTimeHelper.fromXmlDateTimeUTC("2014-05-02T14:33:51.8595755Z"), record.getDateCreated());

        DateTime now = JodaDateTimeHelper.nowInUtc();
        record.setDateCreated(now);

        assertEquals(now, record.getDateCreated());

    }

    @Test
    public void LastModifiedTest()
    {
        CoalesceRecord record = getMissionRecord();

        assertEquals(JodaDateTimeHelper.fromXmlDateTimeUTC("2014-05-02T14:33:59.193995Z"), record.getLastModified());

        DateTime now = JodaDateTimeHelper.nowInUtc();
        record.setLastModified(now);

        assertEquals(now, record.getLastModified());

    }

    // -----------------------------------------------------------------------//
    // Private Methods
    // -----------------------------------------------------------------------//

    private CoalesceRecordset getMissionRecordset()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);

        return getMissionRecordset(entity);

    }

    private CoalesceRecordset getMissionRecordset(CoalesceEntity entity)
    {
        return (CoalesceRecordset) entity.getDataObjectForNamePath(CoalesceTypeInstances.TEST_MISSION_RECORDSET_PATH);
    }

    private CoalesceRecord getMissionRecord()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);

        return (CoalesceRecord) entity.getDataObjectForNamePath(CoalesceTypeInstances.TEST_MISSION_RECORD_PATH);

    }
}