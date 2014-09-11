﻿using System;

namespace wDataServiceTester
{
    public class CoalesceTypeInstances
    {
        public static String TestMission = 
        "<entity key=\"62857EF8-3930-4F0E-BAE3-093344EBF389\" datecreated=\"2014-05-02T14:33:51.851575Z\" lastmodified=\"2014-05-20T16:17:13.2293139Z\" name=\"TREXMission\" source=\"TREX Portal\" version=\"1.0.0.0\" entityid=\"\" entityidtype=\"\" title=\"TREXMission/Mission Information Section/Mission Information Recordset/Mission Information Recordset Record/MissionName,TREXMission/Mission Information Section/Mission Information Recordset/Mission Information Recordset Record/IncidentTitle\" status=\"active\">" +
          "<linkagesection key=\"F4F126AF-4658-4D7F-A67F-4833F7EADDC3\" datecreated=\"2014-05-02T14:33:51.851575Z\" lastmodified=\"2014-05-20T16:17:13.2293139Z\" name=\"Linkages\" noindex=\"True\" status=\"active\">" +
            "<linkage key=\"DB7E0EAF-F4EF-4473-94A9-B93A7F46281E\" datecreated=\"2014-05-02T14:33:51.8615756Z\" lastmodified=\"2014-05-02T14:33:51.8615756Z\" name=\"Linkage\" entity1key=\"62857EF8-3930-4F0E-BAE3-093344EBF389\" entity1name=\"TREXMission\" entity1source=\"TREX Portal\" entity1version=\"1.0.0.0\" linktype=\"IsChildOf\" entity2key=\"AEACD69E-5365-4401-87A1-D95E657E0785\" entity2name=\"TREXOperation\" entity2source=\"TREX Portal\" entity2version=\"1.0.0.0\" classificationmarking=\"U\" modifiedby=\"\" inputlang=\"en-US\" status=\"active\" />" +
            "<linkage key=\"9A04CBCD-297F-43E2-A590-F59D8438E386\" datecreated=\"2014-05-02T14:33:51.8645758Z\" lastmodified=\"2014-05-02T14:33:51.8645758Z\" name=\"Linkage\" entity1key=\"62857EF8-3930-4F0E-BAE3-093344EBF389\" entity1name=\"TREXMission\" entity1source=\"TREX Portal\" entity1version=\"1.0.0.0\" linktype=\"IsParentOf\" entity2key=\"C42DFD35-EA71-4F56-BC3B-D4287279123D\" entity2name=\"TREXComments\" entity2source=\"TREX Portal\" entity2version=\"1.0.0.0\" classificationmarking=\"U\" modifiedby=\"\" inputlang=\"en-US\" status=\"active\" />" +
            "<linkage key=\"309153E3-5F53-4EDB-B89C-35AE6EECEBF1\" datecreated=\"2014-05-02T14:33:53.1546496Z\" lastmodified=\"2014-05-02T14:33:53.1546496Z\" name=\"Linkage\" entity1key=\"62857EF8-3930-4F0E-BAE3-093344EBF389\" entity1name=\"TREXMission\" entity1source=\"TREX Portal\" entity1version=\"1.0.0.0\" linktype=\"HasParticipant\" entity2key=\"BDCD779B-3C74-4391-BCCB-2DB8D06D5A6F\" entity2name=\"TREXUnit\" entity2source=\"TREX Portal\" entity2version=\"1.0.0.0\" classificationmarking=\"U\" modifiedby=\"\" inputlang=\"en-US\" status=\"active\" />" +
            "<linkage key=\"6AD08B0F-C492-4105-8033-A5E43056B864\" datecreated=\"2014-05-20T16:17:13.2293139Z\" lastmodified=\"2014-05-20T16:17:13.2293139Z\" name=\"Linkage\" entity1key=\"62857EF8-3930-4F0E-BAE3-093344EBF389\" entity1name=\"TREXMission\" entity1source=\"TREX Portal\" entity1version=\"1.0.0.0\" linktype=\"IsParentOf\" entity2key=\"55DFA165-0AB8-48B7-BF35-5DA6CADB5E1E\" entity2name=\"TREXCollection\" entity2source=\"TREXAction\" entity2version=\"1.0.0.0\" classificationmarking=\"U\" modifiedby=\"\" inputlang=\"en-US\" status=\"active\" />" +
          "</linkagesection>" +
          "<section key=\"85CB4256-4CC2-4F96-A03D-5EF880989822\" datecreated=\"2014-05-02T14:33:51.851575Z\" lastmodified=\"2014-05-02T14:33:59.1309914Z\" name=\"Live Status Section\" noindex=\"True\" status=\"active\">" +
            "<recordset key=\"E4299F94-9EFF-4BE6-8EB6-570988B11A03\" datecreated=\"2014-05-02T14:33:51.851575Z\" lastmodified=\"2014-05-02T14:33:59.1309914Z\" name=\"Live Status Recordset\" minrecords=\"0\" maxrecords=\"0\" status=\"active\">" +
              "<fielddefinition key=\"3BD1AFAD-93C8-48B7-8ED6-63BCFB3091A8\" datecreated=\"2014-05-02T14:33:51.851575Z\" lastmodified=\"2014-05-02T14:33:51.851575Z\" status=\"active\" name=\"CurrentStatus\" defaultclassificationmarking=\"U\" defaultvalue=\"\" datatype=\"string\" />" +
              "<record key=\"3592A6C5-B13A-4901-8876-BA5B63FD2BEB\" datecreated=\"2014-05-02T14:33:51.8605756Z\" lastmodified=\"2014-05-02T14:33:59.1309914Z\" status=\"active\" name=\"Live Status Recordset Record\">" +
                "<field key=\"6DEABFA4-42B3-46C8-BA6E-662219C19F1F\" datecreated=\"2014-05-02T14:33:51.8605756Z\" lastmodified=\"2014-05-02T14:33:59.1309914Z\" name=\"CurrentStatus\" datatype=\"string\" classificationmarking=\"U\" label=\"\" value=\"In Progress\" status=\"active\" />" +
              "</record>" +
            "</recordset>" +
          "</section>" +
          "<section key=\"383EA645-E695-4E75-ADA6-0C79BEC09A18\" datecreated=\"2014-05-02T14:33:51.8525751Z\" lastmodified=\"2014-05-02T14:33:59.193995Z\" name=\"Mission Information Section\" status=\"active\">" +
            "<recordset key=\"7A158E39-B6C4-4912-A712-DF296375A368\" datecreated=\"2014-05-02T14:33:51.8525751Z\" lastmodified=\"2014-05-02T14:33:59.193995Z\" name=\"Mission Information Recordset\" minrecords=\"0\" maxrecords=\"0\" status=\"active\">" +
              "<fielddefinition key=\"93C6A209-AD86-4474-9FFB-D6801B2548AA\" datecreated=\"2014-05-02T14:33:51.8525751Z\" lastmodified=\"2014-05-02T14:33:51.8525751Z\" status=\"active\" name=\"ActionNumber\" defaultclassificationmarking=\"U\" defaultvalue=\"0\" datatype=\"string\" label=\"Action Number\" />" +
              "<fielddefinition key=\"DBBB6CEC-DD98-4B31-9995-8AF0A5E184EC\" datecreated=\"2014-05-02T14:33:51.8525751Z\" lastmodified=\"2014-05-02T14:33:51.8525751Z\" status=\"active\" name=\"IncidentNumber\" defaultclassificationmarking=\"U\" defaultvalue=\"\" datatype=\"string\" />" +
              "<fielddefinition key=\"7D45F5BD-14A0-4890-B8C5-D502806A4607\" datecreated=\"2014-05-02T14:33:51.8535752Z\" lastmodified=\"2014-05-02T14:33:51.8535752Z\" status=\"active\" name=\"IncidentTitle\" defaultclassificationmarking=\"U\" defaultvalue=\"\" datatype=\"string\" />" +
              "<fielddefinition key=\"1A7DA2CD-8A83-4E86-ADE8-15FDECE0564E\" datecreated=\"2014-05-02T14:33:51.8535752Z\" lastmodified=\"2014-05-02T14:33:51.8535752Z\" status=\"active\" name=\"IncidentDescription\" defaultclassificationmarking=\"U\" defaultvalue=\"\" datatype=\"string\" />" +
              "<fielddefinition key=\"5F2D150A-CEDB-4BF9-9A66-DD3212721E2B\" datecreated=\"2014-05-02T14:33:51.8535752Z\" lastmodified=\"2014-05-02T14:33:51.8535752Z\" status=\"active\" name=\"IncidentDateTime\" defaultclassificationmarking=\"U\" defaultvalue=\"\" datatype=\"datetime\" />" +
              "<fielddefinition key=\"00D19206-5246-45EE-BA15-07507767040A\" datecreated=\"2014-05-02T14:33:51.8535752Z\" lastmodified=\"2014-05-02T14:33:51.8535752Z\" status=\"active\" name=\"MissionName\" defaultclassificationmarking=\"U\" defaultvalue=\"\" datatype=\"string\" />" +
              "<fielddefinition key=\"FADEBDFD-4477-4D2E-8C33-186717209F16\" datecreated=\"2014-05-02T14:33:51.8535752Z\" lastmodified=\"2014-05-02T14:33:51.8535752Z\" status=\"active\" name=\"MissionType\" defaultclassificationmarking=\"U\" defaultvalue=\"\" datatype=\"string\" />" +
              "<fielddefinition key=\"3B28419A-6744-4B10-B4E7-01B3A04620ED\" datecreated=\"2014-05-02T14:33:51.8545752Z\" lastmodified=\"2014-05-02T14:33:51.8545752Z\" status=\"active\" name=\"MissionDescription\" defaultclassificationmarking=\"U\" defaultvalue=\"\" datatype=\"string\" />" +
              "<fielddefinition key=\"F862B507-8523-4BB2-8A49-EB45FBEB88AD\" datecreated=\"2014-05-02T14:33:51.8545752Z\" lastmodified=\"2014-05-02T14:33:51.8545752Z\" status=\"active\" name=\"MissionIndicatorColor\" defaultclassificationmarking=\"U\" defaultvalue=\"\" datatype=\"string\" />" +
              "<fielddefinition key=\"6C2296DD-205E-432B-B381-2236E7A43162\" datecreated=\"2014-05-02T14:33:51.8545752Z\" lastmodified=\"2014-05-02T14:33:51.8545752Z\" status=\"active\" name=\"MissionIndicatorShape\" defaultclassificationmarking=\"U\" defaultvalue=\"\" datatype=\"string\" />" +
              "<fielddefinition key=\"5150BB68-0156-469D-9C93-C62CF3F7FA19\" datecreated=\"2014-05-02T14:33:51.8545752Z\" lastmodified=\"2014-05-02T14:33:51.8545752Z\" status=\"active\" name=\"MissionIndicatorNumber\" defaultclassificationmarking=\"U\" defaultvalue=\"\" datatype=\"string\" />" +
              "<fielddefinition key=\"12E16C1A-2D48-4228-90AE-596591E66536\" datecreated=\"2014-05-02T14:33:51.8545752Z\" lastmodified=\"2014-05-02T14:33:51.8545752Z\" status=\"active\" name=\"MissionIndicatorNumberBASE10\" defaultclassificationmarking=\"U\" defaultvalue=\"\" datatype=\"integer\" />" +
              "<fielddefinition key=\"EEA36E54-601E-4254-84D3-2B387CF4192A\" datecreated=\"2014-05-02T14:33:51.8555753Z\" lastmodified=\"2014-05-02T14:33:51.8555753Z\" status=\"active\" name=\"MissionStartDateTime\" defaultclassificationmarking=\"U\" defaultvalue=\"\" datatype=\"datetime\" />" +
              "<fielddefinition key=\"CD00930C-1659-415A-BE64-67A57FD8A1E9\" datecreated=\"2014-05-02T14:33:51.8555753Z\" lastmodified=\"2014-05-02T14:33:51.8555753Z\" status=\"active\" name=\"MissionEndDateTime\" defaultclassificationmarking=\"U\" defaultvalue=\"\" datatype=\"datetime\" />" +
              "<fielddefinition key=\"1019A528-481A-4E20-BF32-868C36B19ED0\" datecreated=\"2014-05-02T14:33:51.8555753Z\" lastmodified=\"2014-05-02T14:33:51.8555753Z\" status=\"active\" name=\"MissionGeoLocation\" defaultclassificationmarking=\"U\" defaultvalue=\"\" datatype=\"geocoordinate\" />" +
              "<fielddefinition key=\"1EF2E901-DDD8-4C38-A5BF-858CB13F9562\" datecreated=\"2014-05-02T14:33:51.8555753Z\" lastmodified=\"2014-05-02T14:33:51.8555753Z\" status=\"active\" name=\"MissionAddress\" defaultclassificationmarking=\"U\" defaultvalue=\"\" datatype=\"string\" />" +
              "<record key=\"9A03833C-AC15-47C8-A037-1FFFD13A26E9\" datecreated=\"2014-05-02T14:33:51.8595755Z\" lastmodified=\"2014-05-02T14:33:59.193995Z\" status=\"active\" name=\"Mission Information Recordset Record\">" +
                "<field key=\"D7067C3F-54B1-47FD-9C8A-A2D7946E0C2A\" datecreated=\"2014-05-02T14:33:51.8565753Z\" lastmodified=\"2014-05-02T14:33:59.1629932Z\" name=\"ActionNumber\" datatype=\"string\" value=\"0\" classificationmarking=\"U\" label=\"Action Number\" status=\"active\" />" +
                "<field key=\"30976B50-21C7-4365-9ADC-9C857BB34AA3\" datecreated=\"2014-05-02T14:33:51.8565753Z\" lastmodified=\"2014-05-02T14:33:59.1649933Z\" name=\"IncidentNumber\" datatype=\"string\" classificationmarking=\"U\" label=\"\" value=\"WTTS97DE05020E21\" status=\"active\" />" +
                "<field key=\"70138FDF-AE40-428B-A39E-2ACCD83D2EC4\" datecreated=\"2014-05-02T14:33:51.8565753Z\" lastmodified=\"2014-05-02T14:33:59.1719937Z\" name=\"IncidentTitle\" datatype=\"string\" classificationmarking=\"U\" label=\"\" value=\"NORTHCOM Volunteer Background Checks\" status=\"active\" />" +
                "<field key=\"A9464115-AA29-4C1A-94EA-8B5068BDF607\" datecreated=\"2014-05-02T14:33:51.8565753Z\" lastmodified=\"2014-05-02T14:33:59.1739939Z\" name=\"IncidentDescription\" datatype=\"string\" classificationmarking=\"U\" label=\"\" value=\"NORTHCOM Operations Volunteer Background Check. Enrolled Identity 14\" status=\"active\" />" +
                "<field key=\"44B8E79C-9201-41BD-A35B-06D0F8A090A3\" datecreated=\"2014-05-02T14:33:51.8565753Z\" lastmodified=\"2014-05-02T14:33:59.1749939Z\" name=\"IncidentDateTime\" datatype=\"datetime\" classificationmarking=\"U\" label=\"\" value=\"2014-05-02T14:33:51.8605756Z\" status=\"active\" />" +
                "<field key=\"6CB12648-A061-4CC5-B593-3D0407EF4392\" datecreated=\"2014-05-02T14:33:51.8575754Z\" lastmodified=\"2014-05-02T14:33:59.1779941Z\" name=\"MissionName\" datatype=\"string\" classificationmarking=\"U\" label=\"\" value=\"NORTHCOM Volunteer Background Checks\" status=\"active\" />" +
                "<field key=\"C6695025-F302-4C28-A428-EF5AE54C3CFC\" datecreated=\"2014-05-02T14:33:51.8575754Z\" lastmodified=\"2014-05-02T14:33:59.1789941Z\" name=\"MissionType\" datatype=\"string\" classificationmarking=\"U\" label=\"\" value=\"Checkpoint Operations\" status=\"active\" />" +
                "<field key=\"7661C4AB-E159-484E-BB6D-17AD430B4D86\" datecreated=\"2014-05-02T14:33:51.8575754Z\" lastmodified=\"2014-05-02T14:33:59.1809943Z\" name=\"MissionDescription\" datatype=\"string\" classificationmarking=\"U\" label=\"\" value=\"NORTHCOM Operations Volunteer Background Check. Enrolled Identity 14\" status=\"active\" />" +
                "<field key=\"1035DAC1-B469-4F93-8AC3-A12B7720A567\" datecreated=\"2014-05-02T14:33:51.8575754Z\" lastmodified=\"2014-05-02T14:33:59.1839944Z\" name=\"MissionIndicatorColor\" datatype=\"string\" classificationmarking=\"U\" label=\"\" value=\"White\" status=\"active\" />" +
                "<field key=\"7613A993-7D8D-4CDD-91F1-4669D8644262\" datecreated=\"2014-05-02T14:33:51.8585754Z\" lastmodified=\"2014-05-02T14:33:59.1859945Z\" name=\"MissionIndicatorShape\" datatype=\"string\" classificationmarking=\"U\" label=\"\" value=\"Triangle\" status=\"active\" />" +
                "<field key=\"FD9188CF-6B34-40DB-9398-CC5BF29AB7EB\" datecreated=\"2014-05-02T14:33:51.8585754Z\" lastmodified=\"2014-05-02T14:33:59.1869946Z\" name=\"MissionIndicatorNumber\" datatype=\"string\" classificationmarking=\"U\" label=\"\" value=\"TS9\" status=\"active\" />" +
                "<field key=\"6838429C-899F-40F9-85CF-3E76512D275F\" datecreated=\"2014-05-02T14:33:51.8585754Z\" lastmodified=\"2014-05-02T14:33:59.1879947Z\" name=\"MissionIndicatorNumberBASE10\" datatype=\"integer\" classificationmarking=\"U\" label=\"\" value=\"38601\" status=\"active\" />" +
                "<field key=\"4F9D3FD2-877D-45D0-9619-6341C5C7B48C\" datecreated=\"2014-05-02T14:33:51.8585754Z\" lastmodified=\"2014-05-02T14:33:59.1899948Z\" name=\"MissionStartDateTime\" datatype=\"datetime\" classificationmarking=\"U\" label=\"\" value=\"2014-05-02T14:33:51.8605756Z\" status=\"active\" />" +
                "<field key=\"21E3B888-2913-4E1C-8BB8-97655C53DE7E\" datecreated=\"2014-05-02T14:33:51.8595755Z\" lastmodified=\"2014-05-02T14:33:59.1909948Z\" name=\"MissionEndDateTime\" datatype=\"datetime\" classificationmarking=\"U\" label=\"\" status=\"active\" />" +
                "<field key=\"45BE93A8-2163-4ACB-87B4-ADBC133BC624\" datecreated=\"2014-05-02T14:33:51.8595755Z\" lastmodified=\"2014-05-02T14:33:59.1929949Z\" name=\"MissionGeoLocation\" datatype=\"geocoordinate\" classificationmarking=\"U\" label=\"\" value=\"POINT (-80.9363995 43.6616578)\" status=\"active\" />" +
                "<field key=\"37496274-2077-454F-9CB3-5A57C1753640\" datecreated=\"2014-05-02T14:33:51.8595755Z\" lastmodified=\"2014-05-02T14:33:59.193995Z\" name=\"MissionAddress\" datatype=\"string\" classificationmarking=\"U\" label=\"\" status=\"active\" />" +
              "</record>" +
            "</recordset>" +
          "</section>" +
        "</entity>";

        public static String TestMissionLinkageSection =
        "<linkagesection key=\"F4F126AF-4658-4D7F-A67F-4833F7EADDC3\" datecreated=\"2014-05-02T14:33:51.851575Z\" lastmodified=\"2014-05-20T16:17:13.2293139Z\" name=\"Linkages\" noindex=\"True\" status=\"active\">" +
            "<linkage key=\"DB7E0EAF-F4EF-4473-94A9-B93A7F46281E\" datecreated=\"2014-05-02T14:33:51.8615756Z\" lastmodified=\"2014-05-02T14:33:51.8615756Z\" name=\"Linkage\" entity1key=\"62857EF8-3930-4F0E-BAE3-093344EBF389\" entity1name=\"TREXMission\" entity1source=\"TREX Portal\" entity1version=\"1.0.0.0\" linktype=\"IsChildOf\" entity2key=\"AEACD69E-5365-4401-87A1-D95E657E0785\" entity2name=\"TREXOperation\" entity2source=\"TREX Portal\" entity2version=\"1.0.0.0\" classificationmarking=\"U\" modifiedby=\"\" inputlang=\"en-US\" status=\"active\" />" +
            "<linkage key=\"9A04CBCD-297F-43E2-A590-F59D8438E386\" datecreated=\"2014-05-02T14:33:51.8645758Z\" lastmodified=\"2014-05-02T14:33:51.8645758Z\" name=\"Linkage\" entity1key=\"62857EF8-3930-4F0E-BAE3-093344EBF389\" entity1name=\"TREXMission\" entity1source=\"TREX Portal\" entity1version=\"1.0.0.0\" linktype=\"IsParentOf\" entity2key=\"C42DFD35-EA71-4F56-BC3B-D4287279123D\" entity2name=\"TREXComments\" entity2source=\"TREX Portal\" entity2version=\"1.0.0.0\" classificationmarking=\"U\" modifiedby=\"\" inputlang=\"en-US\" status=\"active\" />" +
            "<linkage key=\"309153E3-5F53-4EDB-B89C-35AE6EECEBF1\" datecreated=\"2014-05-02T14:33:53.1546496Z\" lastmodified=\"2014-05-02T14:33:53.1546496Z\" name=\"Linkage\" entity1key=\"62857EF8-3930-4F0E-BAE3-093344EBF389\" entity1name=\"TREXMission\" entity1source=\"TREX Portal\" entity1version=\"1.0.0.0\" linktype=\"HasParticipant\" entity2key=\"BDCD779B-3C74-4391-BCCB-2DB8D06D5A6F\" entity2name=\"TREXUnit\" entity2source=\"TREX Portal\" entity2version=\"1.0.0.0\" classificationmarking=\"U\" modifiedby=\"\" inputlang=\"en-US\" status=\"active\" />" +
            "<linkage key=\"6AD08B0F-C492-4105-8033-A5E43056B864\" datecreated=\"2014-05-20T16:17:13.2293139Z\" lastmodified=\"2014-05-20T16:17:13.2293139Z\" name=\"Linkage\" entity1key=\"62857EF8-3930-4F0E-BAE3-093344EBF389\" entity1name=\"TREXMission\" entity1source=\"TREX Portal\" entity1version=\"1.0.0.0\" linktype=\"IsParentOf\" entity2key=\"55DFA165-0AB8-48B7-BF35-5DA6CADB5E1E\" entity2name=\"TREXCollection\" entity2source=\"TREXAction\" entity2version=\"1.0.0.0\" classificationmarking=\"U\" modifiedby=\"\" inputlang=\"en-US\" status=\"active\" />" +
        "</linkagesection>";

        public static String TestMissionRecordSet =
        "<recordset key=\"7A158E39-B6C4-4912-A712-DF296375A368\" datecreated=\"2014-05-02T14:33:51.8525751Z\" lastmodified=\"2014-05-02T14:33:59.193995Z\" name=\"Mission Information Recordset\" minrecords=\"0\" maxrecords=\"0\" status=\"active\">" +
            "<fielddefinition key=\"93C6A209-AD86-4474-9FFB-D6801B2548AA\" datecreated=\"2014-05-02T14:33:51.8525751Z\" lastmodified=\"2014-05-02T14:33:51.8525751Z\" status=\"active\" name=\"ActionNumber\" defaultclassificationmarking=\"U\" defaultvalue=\"0\" datatype=\"string\" label=\"Action Number\" />" +
            "<fielddefinition key=\"DBBB6CEC-DD98-4B31-9995-8AF0A5E184EC\" datecreated=\"2014-05-02T14:33:51.8525751Z\" lastmodified=\"2014-05-02T14:33:51.8525751Z\" status=\"active\" name=\"IncidentNumber\" defaultclassificationmarking=\"U\" defaultvalue=\"\" datatype=\"string\" />" +
            "<fielddefinition key=\"7D45F5BD-14A0-4890-B8C5-D502806A4607\" datecreated=\"2014-05-02T14:33:51.8535752Z\" lastmodified=\"2014-05-02T14:33:51.8535752Z\" status=\"active\" name=\"IncidentTitle\" defaultclassificationmarking=\"U\" defaultvalue=\"\" datatype=\"string\" />" +
            "<fielddefinition key=\"1A7DA2CD-8A83-4E86-ADE8-15FDECE0564E\" datecreated=\"2014-05-02T14:33:51.8535752Z\" lastmodified=\"2014-05-02T14:33:51.8535752Z\" status=\"active\" name=\"IncidentDescription\" defaultclassificationmarking=\"U\" defaultvalue=\"\" datatype=\"string\" />" +
            "<fielddefinition key=\"5F2D150A-CEDB-4BF9-9A66-DD3212721E2B\" datecreated=\"2014-05-02T14:33:51.8535752Z\" lastmodified=\"2014-05-02T14:33:51.8535752Z\" status=\"active\" name=\"IncidentDateTime\" defaultclassificationmarking=\"U\" defaultvalue=\"\" datatype=\"datetime\" />" +
            "<fielddefinition key=\"00D19206-5246-45EE-BA15-07507767040A\" datecreated=\"2014-05-02T14:33:51.8535752Z\" lastmodified=\"2014-05-02T14:33:51.8535752Z\" status=\"active\" name=\"MissionName\" defaultclassificationmarking=\"U\" defaultvalue=\"\" datatype=\"string\" />" +
            "<fielddefinition key=\"FADEBDFD-4477-4D2E-8C33-186717209F16\" datecreated=\"2014-05-02T14:33:51.8535752Z\" lastmodified=\"2014-05-02T14:33:51.8535752Z\" status=\"active\" name=\"MissionType\" defaultclassificationmarking=\"U\" defaultvalue=\"\" datatype=\"string\" />" +
            "<fielddefinition key=\"3B28419A-6744-4B10-B4E7-01B3A04620ED\" datecreated=\"2014-05-02T14:33:51.8545752Z\" lastmodified=\"2014-05-02T14:33:51.8545752Z\" status=\"active\" name=\"MissionDescription\" defaultclassificationmarking=\"U\" defaultvalue=\"\" datatype=\"string\" />" +
            "<fielddefinition key=\"F862B507-8523-4BB2-8A49-EB45FBEB88AD\" datecreated=\"2014-05-02T14:33:51.8545752Z\" lastmodified=\"2014-05-02T14:33:51.8545752Z\" status=\"active\" name=\"MissionIndicatorColor\" defaultclassificationmarking=\"U\" defaultvalue=\"\" datatype=\"string\" />" +
            "<fielddefinition key=\"6C2296DD-205E-432B-B381-2236E7A43162\" datecreated=\"2014-05-02T14:33:51.8545752Z\" lastmodified=\"2014-05-02T14:33:51.8545752Z\" status=\"active\" name=\"MissionIndicatorShape\" defaultclassificationmarking=\"U\" defaultvalue=\"\" datatype=\"string\" />" +
            "<fielddefinition key=\"5150BB68-0156-469D-9C93-C62CF3F7FA19\" datecreated=\"2014-05-02T14:33:51.8545752Z\" lastmodified=\"2014-05-02T14:33:51.8545752Z\" status=\"active\" name=\"MissionIndicatorNumber\" defaultclassificationmarking=\"U\" defaultvalue=\"\" datatype=\"string\" />" +
            "<fielddefinition key=\"12E16C1A-2D48-4228-90AE-596591E66536\" datecreated=\"2014-05-02T14:33:51.8545752Z\" lastmodified=\"2014-05-02T14:33:51.8545752Z\" status=\"active\" name=\"MissionIndicatorNumberBASE10\" defaultclassificationmarking=\"U\" defaultvalue=\"\" datatype=\"integer\" />" +
            "<fielddefinition key=\"EEA36E54-601E-4254-84D3-2B387CF4192A\" datecreated=\"2014-05-02T14:33:51.8555753Z\" lastmodified=\"2014-05-02T14:33:51.8555753Z\" status=\"active\" name=\"MissionStartDateTime\" defaultclassificationmarking=\"U\" defaultvalue=\"\" datatype=\"datetime\" />" +
            "<fielddefinition key=\"CD00930C-1659-415A-BE64-67A57FD8A1E9\" datecreated=\"2014-05-02T14:33:51.8555753Z\" lastmodified=\"2014-05-02T14:33:51.8555753Z\" status=\"active\" name=\"MissionEndDateTime\" defaultclassificationmarking=\"U\" defaultvalue=\"\" datatype=\"datetime\" />" +
            "<fielddefinition key=\"1019A528-481A-4E20-BF32-868C36B19ED0\" datecreated=\"2014-05-02T14:33:51.8555753Z\" lastmodified=\"2014-05-02T14:33:51.8555753Z\" status=\"active\" name=\"MissionGeoLocation\" defaultclassificationmarking=\"U\" defaultvalue=\"\" datatype=\"geocoordinate\" />" +
            "<fielddefinition key=\"1EF2E901-DDD8-4C38-A5BF-858CB13F9562\" datecreated=\"2014-05-02T14:33:51.8555753Z\" lastmodified=\"2014-05-02T14:33:51.8555753Z\" status=\"active\" name=\"MissionAddress\" defaultclassificationmarking=\"U\" defaultvalue=\"\" datatype=\"string\" />" +
            "<record key=\"9A03833C-AC15-47C8-A037-1FFFD13A26E9\" datecreated=\"2014-05-02T14:33:51.8595755Z\" lastmodified=\"2014-05-02T14:33:59.193995Z\" status=\"active\" name=\"Mission Information Recordset Record\">" +
              "<field key=\"D7067C3F-54B1-47FD-9C8A-A2D7946E0C2A\" datecreated=\"2014-05-02T14:33:51.8565753Z\" lastmodified=\"2014-05-02T14:33:59.1629932Z\" name=\"ActionNumber\" datatype=\"string\" value=\"0\" classificationmarking=\"U\" label=\"Action Number\" status=\"active\" />" +
              "<field key=\"30976B50-21C7-4365-9ADC-9C857BB34AA3\" datecreated=\"2014-05-02T14:33:51.8565753Z\" lastmodified=\"2014-05-02T14:33:59.1649933Z\" name=\"IncidentNumber\" datatype=\"string\" classificationmarking=\"U\" label=\"\" value=\"WTTS97DE05020E21\" status=\"active\" />" +
              "<field key=\"70138FDF-AE40-428B-A39E-2ACCD83D2EC4\" datecreated=\"2014-05-02T14:33:51.8565753Z\" lastmodified=\"2014-05-02T14:33:59.1719937Z\" name=\"IncidentTitle\" datatype=\"string\" classificationmarking=\"U\" label=\"\" value=\"NORTHCOM Volunteer Background Checks\" status=\"active\" />" +
              "<field key=\"A9464115-AA29-4C1A-94EA-8B5068BDF607\" datecreated=\"2014-05-02T14:33:51.8565753Z\" lastmodified=\"2014-05-02T14:33:59.1739939Z\" name=\"IncidentDescription\" datatype=\"string\" classificationmarking=\"U\" label=\"\" value=\"NORTHCOM Operations Volunteer Background Check. Enrolled Identity 14\" status=\"active\" />" +
              "<field key=\"44B8E79C-9201-41BD-A35B-06D0F8A090A3\" datecreated=\"2014-05-02T14:33:51.8565753Z\" lastmodified=\"2014-05-02T14:33:59.1749939Z\" name=\"IncidentDateTime\" datatype=\"datetime\" classificationmarking=\"U\" label=\"\" value=\"2014-05-02T14:33:51.8605756Z\" status=\"active\" />" +
              "<field key=\"6CB12648-A061-4CC5-B593-3D0407EF4392\" datecreated=\"2014-05-02T14:33:51.8575754Z\" lastmodified=\"2014-05-02T14:33:59.1779941Z\" name=\"MissionName\" datatype=\"string\" classificationmarking=\"U\" label=\"\" value=\"NORTHCOM Volunteer Background Checks\" status=\"active\" />" +
              "<field key=\"C6695025-F302-4C28-A428-EF5AE54C3CFC\" datecreated=\"2014-05-02T14:33:51.8575754Z\" lastmodified=\"2014-05-02T14:33:59.1789941Z\" name=\"MissionType\" datatype=\"string\" classificationmarking=\"U\" label=\"\" value=\"Checkpoint Operations\" status=\"active\" />" +
              "<field key=\"7661C4AB-E159-484E-BB6D-17AD430B4D86\" datecreated=\"2014-05-02T14:33:51.8575754Z\" lastmodified=\"2014-05-02T14:33:59.1809943Z\" name=\"MissionDescription\" datatype=\"string\" classificationmarking=\"U\" label=\"\" value=\"NORTHCOM Operations Volunteer Background Check. Enrolled Identity 14\" status=\"active\" />" +
              "<field key=\"1035DAC1-B469-4F93-8AC3-A12B7720A567\" datecreated=\"2014-05-02T14:33:51.8575754Z\" lastmodified=\"2014-05-02T14:33:59.1839944Z\" name=\"MissionIndicatorColor\" datatype=\"string\" classificationmarking=\"U\" label=\"\" value=\"White\" status=\"active\" />" +
              "<field key=\"7613A993-7D8D-4CDD-91F1-4669D8644262\" datecreated=\"2014-05-02T14:33:51.8585754Z\" lastmodified=\"2014-05-02T14:33:59.1859945Z\" name=\"MissionIndicatorShape\" datatype=\"string\" classificationmarking=\"U\" label=\"\" value=\"Triangle\" status=\"active\" />" +
              "<field key=\"FD9188CF-6B34-40DB-9398-CC5BF29AB7EB\" datecreated=\"2014-05-02T14:33:51.8585754Z\" lastmodified=\"2014-05-02T14:33:59.1869946Z\" name=\"MissionIndicatorNumber\" datatype=\"string\" classificationmarking=\"U\" label=\"\" value=\"TS9\" status=\"active\" />" +
              "<field key=\"6838429C-899F-40F9-85CF-3E76512D275F\" datecreated=\"2014-05-02T14:33:51.8585754Z\" lastmodified=\"2014-05-02T14:33:59.1879947Z\" name=\"MissionIndicatorNumberBASE10\" datatype=\"integer\" classificationmarking=\"U\" label=\"\" value=\"38601\" status=\"active\" />" +
              "<field key=\"4F9D3FD2-877D-45D0-9619-6341C5C7B48C\" datecreated=\"2014-05-02T14:33:51.8585754Z\" lastmodified=\"2014-05-02T14:33:59.1899948Z\" name=\"MissionStartDateTime\" datatype=\"datetime\" classificationmarking=\"U\" label=\"\" value=\"2014-05-02T14:33:51.8605756Z\" status=\"active\" />" +
              "<field key=\"21E3B888-2913-4E1C-8BB8-97655C53DE7E\" datecreated=\"2014-05-02T14:33:51.8595755Z\" lastmodified=\"2014-05-02T14:33:59.1909948Z\" name=\"MissionEndDateTime\" datatype=\"datetime\" classificationmarking=\"U\" label=\"\" status=\"active\" />" +
              "<field key=\"45BE93A8-2163-4ACB-87B4-ADBC133BC624\" datecreated=\"2014-05-02T14:33:51.8595755Z\" lastmodified=\"2014-05-02T14:33:59.1929949Z\" name=\"MissionGeoLocation\" datatype=\"geocoordinate\" classificationmarking=\"U\" label=\"\" value=\"POINT (-80.9363995 43.6616578)\" status=\"active\" />" +
              "<field key=\"37496274-2077-454F-9CB3-5A57C1753640\" datecreated=\"2014-05-02T14:33:51.8595755Z\" lastmodified=\"2014-05-02T14:33:59.193995Z\" name=\"MissionAddress\" datatype=\"string\" classificationmarking=\"U\" label=\"\" status=\"active\" />" +
            "</record>" +
          "</recordset>";
    }
}