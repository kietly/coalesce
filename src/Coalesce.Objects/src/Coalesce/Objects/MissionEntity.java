package Coalesce.Objects;

import org.joda.time.DateTime;

import Coalesce.Framework.DataModel.ECoalesceFieldDataTypes;
import Coalesce.Framework.DataModel.CoalesceCoordinateField;
import Coalesce.Framework.DataModel.CoalesceDateTimeField;
import Coalesce.Framework.DataModel.CoalesceEntity;
import Coalesce.Framework.DataModel.CoalesceField;
import Coalesce.Framework.DataModel.CoalesceFieldDefinition;
import Coalesce.Framework.DataModel.CoalesceIntegerField;
import Coalesce.Framework.DataModel.CoalesceRecord;
import Coalesce.Framework.DataModel.CoalesceRecordset;
import Coalesce.Framework.DataModel.CoalesceSection;
import Coalesce.Framework.DataModel.CoalesceStringField;

public class MissionEntity extends CoalesceEntity {

    // ----------------------------------------------------------------------//
    // Static Properties
    // ----------------------------------------------------------------------//

    public static final String Name = "Mission";
    public static final String Source = "Coalesce";

    // ----------------------------------------------------------------------//
    // Protected Member Variables
    // ----------------------------------------------------------------------//

    private CoalesceRecord _informationRecord;
    private CoalesceRecord _liveStatusRecord;

    // ----------------------------------------------------------------------//
    // Initialization
    // ----------------------------------------------------------------------//

    @Override
    public boolean initialize()
    {
        CoalesceSection section;
        CoalesceRecordset recordSet;

        // Already Initialized?
        if (_liveStatusRecord != null || _informationRecord != null) return false;

        // Initialize Entity
        if (!super.initialize(MissionEntity.Name,
                              MissionEntity.Source,
                              "1.0",
                              "",
                              "",
                              "TREXMission/Mission Information Section/Mission Information Recordset/Mission Information Recordset Record/MissionName,TREXMission/Mission Information Section/Mission Information Recordset/Mission Information Recordset Record/IncidentTitle")) return false;

        // Create Live Section
        section = CoalesceSection.create(this, "Live Status Section");
        recordSet = CoalesceRecordset.create(section, "Live Status Recordset");
        CoalesceFieldDefinition.create(recordSet, "CurrentStatus", ECoalesceFieldDataTypes.StringType);

        // Create New Record
        _liveStatusRecord = recordSet.addNew();

        // Create Mission Information Section
        section = CoalesceSection.create(this, MissionEntity.Name + " Information Section");
        recordSet = CoalesceRecordset.create(section, MissionEntity.Name + " Information Recordset");
        CoalesceFieldDefinition.create(recordSet, "ActionNumber", ECoalesceFieldDataTypes.StringType, "Action Number", "U", "0");
        CoalesceFieldDefinition.create(recordSet, "IncidentNumber", ECoalesceFieldDataTypes.StringType);
        CoalesceFieldDefinition.create(recordSet, "IncidentTitle", ECoalesceFieldDataTypes.StringType);
        CoalesceFieldDefinition.create(recordSet, "IncidentDescription", ECoalesceFieldDataTypes.StringType);
        CoalesceFieldDefinition.create(recordSet, "IncidentDateTime", ECoalesceFieldDataTypes.DateTimeType);
        CoalesceFieldDefinition.create(recordSet, "MissionName", ECoalesceFieldDataTypes.StringType);
        CoalesceFieldDefinition.create(recordSet, "MissionType", ECoalesceFieldDataTypes.StringType);
        CoalesceFieldDefinition.create(recordSet, "MissionDescription", ECoalesceFieldDataTypes.StringType);
        CoalesceFieldDefinition.create(recordSet, "MissionIndicatorColor", ECoalesceFieldDataTypes.StringType);
        CoalesceFieldDefinition.create(recordSet, "MissionIndicatorShape", ECoalesceFieldDataTypes.StringType);
        CoalesceFieldDefinition.create(recordSet, "MissionIndicatorNumber", ECoalesceFieldDataTypes.StringType);
        CoalesceFieldDefinition.create(recordSet, "MissionIndicatorNumberBASE10", ECoalesceFieldDataTypes.IntegerType);
        CoalesceFieldDefinition.create(recordSet, "MissionStartDateTime", ECoalesceFieldDataTypes.DateTimeType);
        CoalesceFieldDefinition.create(recordSet, "MissionEndDateTime", ECoalesceFieldDataTypes.DateTimeType);
        CoalesceFieldDefinition.create(recordSet, "MissionGeoLocation", ECoalesceFieldDataTypes.GeocoordinateType);
        CoalesceFieldDefinition.create(recordSet, "MissionAddress", ECoalesceFieldDataTypes.StringType);

        _informationRecord = recordSet.addNew();

        // Initialize References
        return this.initializeReferences();
    }

    @Override
    protected boolean initializeReferences()
    {

        // Live Status Record
        if (this._liveStatusRecord == null)
        {
            CoalesceRecordset recordSet = (CoalesceRecordset) this.getDataObjectForNamePath(MissionEntity.Name
                    + "/Live Status Section/Live Status Recordset");

            // Valid Xml?
            if (recordSet == null) return false;

            if (recordSet.getCount() == 0)
            {
                this._liveStatusRecord = recordSet.addNew();
            }
            else
            {
                this._liveStatusRecord = recordSet.getItem(0);
            }

        }

        // Mission Information Record
        if (this._informationRecord == null)
        {
            CoalesceRecordset recordSet = (CoalesceRecordset) this.getDataObjectForNamePath(MissionEntity.Name + "/"
                    + MissionEntity.Name + " Information Section/" + MissionEntity.Name + " Information Recordset");

            // Valid Xml?
            if (recordSet == null) return false;

            if (recordSet.getCount() == 0)
            {
                this._informationRecord = recordSet.addNew();
            }
            else
            {
                this._informationRecord = recordSet.getItem(0);
            }

        }

        return true;
    }

    // ----------------------------------------------------------------------//
    // Entity Fields
    // ----------------------------------------------------------------------//

    // Current Status
    public EMissionStatuses getCurrentStatus()
    {
        return EMissionStatuses.fromLabel(this._liveStatusRecord.getFieldByName("CurrentStatus").getBaseValue());
    }

    public void setCurrentStatus(EMissionStatuses value)
    {
        ((CoalesceStringField) this._liveStatusRecord.getFieldByName("CurrentStatus")).setValue(value.getLabel());
    }
    
    public CoalesceStringField getActionNumber()
    {
        return (CoalesceStringField) _informationRecord.getFieldByName("ActionNumber");
    }

    public CoalesceStringField getIncidentNumber()
    {
        return (CoalesceStringField) _informationRecord.getFieldByName("IncidentNumber");
    }

    public CoalesceStringField getIncidentTitle()
    {
        return (CoalesceStringField) _informationRecord.getFieldByName("IncidentTitle");
    }

    public CoalesceStringField getIncidentDescription()
    {
        return (CoalesceStringField) _informationRecord.getFieldByName("IncidentDescription");
    }

    public CoalesceField<DateTime> getIncidentDateTime()
    {
        return (CoalesceDateTimeField) _informationRecord.getFieldByName("IncidentDateTime");
    }

    public CoalesceStringField getMissionName()
    {
        return (CoalesceStringField) _informationRecord.getFieldByName("MissionName");
    }

    public CoalesceStringField getMissionType()
    {
        return (CoalesceStringField) _informationRecord.getFieldByName("MissionType");
    }

    public CoalesceStringField getMissionDescription()
    {
        return (CoalesceStringField) _informationRecord.getFieldByName("MissionDescription");
    }

    public CoalesceStringField getMissionIndicatorColor()
    {
        return (CoalesceStringField) _informationRecord.getFieldByName("MissionIndicatorColor");
    }

    public CoalesceStringField getMissionIndicatorShape()
    {
        return (CoalesceStringField) _informationRecord.getFieldByName("MissionIndicatorShape");
    }

    public CoalesceStringField getMissionIndicatorNumber()
    {
        return (CoalesceStringField) _informationRecord.getFieldByName("MissionIndicatorNumber");
    }

    public CoalesceIntegerField getMissionIndicatorNumberBASE10()
    {
        return (CoalesceIntegerField) _informationRecord.getFieldByName("MissionIndicatorNumberBASE10");
    }

    public CoalesceDateTimeField getMissionStartDateTime()
    {
        return (CoalesceDateTimeField) _informationRecord.getFieldByName("MissionStartDateTime");
    }

    public CoalesceDateTimeField getMissionEndDateTime()
    {
        return (CoalesceDateTimeField) _informationRecord.getFieldByName("MissionEndDateTime");
    }

    public CoalesceCoordinateField getMissionGeoLocation()
    {
        return (CoalesceCoordinateField) _informationRecord.getFieldByName("MissionGeoLocation");
    }

    public CoalesceStringField getMissionAddress()
    {
        return (CoalesceStringField) _informationRecord.getFieldByName("MissionAddress");
    }
}
