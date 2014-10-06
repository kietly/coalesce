package com.incadencecorp.coalesce.framework.datamodel;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.commons.lang.NullArgumentException;
import org.joda.time.DateTime;

import com.incadencecorp.coalesce.common.helpers.JodaDateTimeHelper;
import com.incadencecorp.coalesce.common.helpers.XmlHelper;
import com.incadencecorp.coalesce.framework.generatedjaxb.Entity.Linkagesection;
import com.incadencecorp.coalesce.framework.generatedjaxb.Entity.Linkagesection.Linkage;

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

public class CoalesceLinkageSection extends CoalesceDataObject {

    private Linkagesection _entityLinkageSection;

    // -----------------------------------------------------------------------//
    // Factory and Initialization
    // -----------------------------------------------------------------------//

    /**
     * Creates an XsdLinkageSection and ties it to its parent XsdEntity.
     * 
     * @param parent XsdEntity that the new XsdLinkageSection will belong to
     * @return XsdLinkageSection the new XsdLinkageSection
     */
    public static CoalesceLinkageSection create(CoalesceEntity parent)
    {
        return CoalesceLinkageSection.create(parent, true);
    }

    /**
     * Creates an XsdLinkageSection and ties it to its parent XsdEntity. Also sets the noIndex attribute.
     * 
     * @param parent XsdEntity that the new XsdLinkageSection will belong to
     * @param noIndex boolean value
     * @return XsdLinkageSection the new XsdLinkageSection
     */
    public static CoalesceLinkageSection create(CoalesceEntity parent, boolean noIndex)
    {
        if (parent == null) throw new NullArgumentException("parent");

        if (parent.getLinkageSection() != null) return parent.getLinkageSection();

        CoalesceLinkageSection linkageSection = new CoalesceLinkageSection();
        if (!linkageSection.initialize(parent)) return null;

        linkageSection.setNoIndex(noIndex);

        // Add to parent's child collection
        if (!parent._childDataObjects.containsKey(linkageSection.getKey()))
        {
            parent._childDataObjects.put(linkageSection.getKey(), linkageSection);
        }

        return linkageSection;

    }

    /**
     * Initializes a previously new XsdLinkageSection and ties it to its parent XsdEntity.
     * 
     * @param parent XsdEntity containing an LinkageSection to base this XsdLinkageSection on
     * @return boolean indicator of success/failure
     */
    public boolean initialize(CoalesceEntity parent)
    {
        if (parent == null) throw new NullArgumentException("parent");

        // Set References
        _parent = parent;
        _entityLinkageSection = parent.getEntityLinkageSection();

        super.initialize();

        this.setName("Linkages");

        if (_entityLinkageSection != null)
        {

            // Add Linkages to Child List
            for (Linkage childLinkage : _entityLinkageSection.getLinkage())
            {

                CoalesceLinkage newLinkage = new CoalesceLinkage();
                if (!newLinkage.initialize(this, childLinkage)) continue;

                if (!_childDataObjects.containsKey(newLinkage.getKey()))
                {
                    _childDataObjects.put(newLinkage.getKey(), newLinkage);
                }
            }

            return true;
        }
        else
        {
            return false;
        }

    }

    // -----------------------------------------------------------------------//
    // Public Methods
    // -----------------------------------------------------------------------//

    @Override
    protected String getObjectKey()
    {
        return _entityLinkageSection.getKey();
    }

    @Override
    public void setObjectKey(String value)
    {
        _entityLinkageSection.setKey(value);
    }

    @Override
    public String getName()
    {
        return _entityLinkageSection.getName();
    }

    @Override
    public void setName(String value)
    {
        _entityLinkageSection.setName(value);
    }

    @Override
    public String getType()
    {
        return "linkagesection";
    }

    /**
     * Returns a hashmap of the XsdLinkages contained in the XsdLinkageSection
     * @return HashMap of this XsdLinkageSection's XsdLinkages
     */
    public Map<String, CoalesceLinkage> getLinkages()
    {
        Map<String, CoalesceLinkage> linkages = new HashMap<String, CoalesceLinkage>();

        for (CoalesceDataObject xdo : _childDataObjects.values())
        {
            if (xdo instanceof CoalesceLinkage)
            {
                linkages.put(xdo.getKey(), (CoalesceLinkage) xdo);
            }
        }

        return linkages;

    }

    /**
     * Creates an XsdLinkage child for this XsdLinkageSection.
     * 
     * @return XsdLinkage newly created and added to this XsdLinkageSection
     */
    public CoalesceLinkage createLinkage()
    {
        return CoalesceLinkage.create(this);
    }

    @Override
    public String toXml()
    {
        return XmlHelper.serialize(_entityLinkageSection);
    }

    @Override
    public boolean getNoIndex()
    {
        return Boolean.parseBoolean(_entityLinkageSection.getNoindex());
    }

    @Override
    public void setNoIndex(boolean value)
    {
        _entityLinkageSection.setNoindex(Boolean.toString(value));
    }

    @Override
    public DateTime getDateCreated()
    {
        // SimpleDateFormat("yyyy-MMM-dd HH:mm:ssZ").parse(_entityLinkageSection.getDatecreated());
        return _entityLinkageSection.getDatecreated();
    }

    @Override
    public void setDateCreated(DateTime value)
    {
        // SimpleDateFormat("yyyy-MMM-dd HH:mm:ssZ").format(value));
        _entityLinkageSection.setDatecreated(value);
    }

    @Override
    public DateTime getLastModified()
    {
        // SimpleDateFormat("yyyy-MMM-dd HH:mm:ssZ").parse(_entityLinkageSection.getLastmodified());
        return _entityLinkageSection.getLastmodified();
    }

    @Override
    protected void setObjectLastModified(DateTime value)
    {
        // SimpleDateFormat("yyyy-MMM-dd HH:mm:ssZ").format(value));
        _entityLinkageSection.setLastmodified(value);
    }

    // -----------------------------------------------------------------------//
    // Protected Methods
    // -----------------------------------------------------------------------//

    @Override
    protected String getObjectStatus()
    {
        return _entityLinkageSection.getStatus();
    }

    @Override
    protected void setObjectStatus(ECoalesceDataObjectStatus status)
    {
        _entityLinkageSection.setStatus(status.getLabel());
    }

    protected Linkagesection getEntityLinkageSection()
    {
        return _entityLinkageSection;
    }

    @Override
    protected Map<QName, String> getOtherAttributes()
    {
        return this._entityLinkageSection.getOtherAttributes();
    }

    @Override
    public boolean setAttribute(String name, String value)
    {
        switch (name) {
        case "key":
            _entityLinkageSection.setKey(value);
            return true;
        case "datecreated":
            _entityLinkageSection.setDatecreated(JodaDateTimeHelper.fromXmlDateTimeUTC(value));
            return true;
        case "lastmodified":
            _entityLinkageSection.setLastmodified(JodaDateTimeHelper.fromXmlDateTimeUTC(value));
            return true;
        case "name":
            _entityLinkageSection.setName(value);
            return true;
        case "noindex":
            _entityLinkageSection.setNoindex(value);
            return true;
        case "status":
            _entityLinkageSection.setStatus(value);
            return true;
        default:
            this.setOtherAttribute(name, value);
            return true;
        }
    }

    @Override
    protected Map<QName, String> getAttributes()
    {
        Map<QName, String> map = new HashMap<QName, String>();
        map.put(new QName("key"), _entityLinkageSection.getKey());
        map.put(new QName("datecreated"), JodaDateTimeHelper.toXmlDateTimeUTC(_entityLinkageSection.getDatecreated()));
        map.put(new QName("lastmodified"), JodaDateTimeHelper.toXmlDateTimeUTC(_entityLinkageSection.getLastmodified()));
        map.put(new QName("name"), _entityLinkageSection.getName());
        map.put(new QName("noindex"), _entityLinkageSection.getNoindex());
        map.put(new QName("status"), _entityLinkageSection.getStatus());
        return map;
    }

}