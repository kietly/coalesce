package com.incadencecorp.coalesce.framework.datamodel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.xml.namespace.QName;

import org.joda.time.DateTime;

import com.incadencecorp.coalesce.common.helpers.JodaDateTimeHelper;
import com.incadencecorp.coalesce.common.helpers.StringHelper;

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

public abstract class CoalesceObject implements ICoalesceObject {

    /*--------------------------------------------------------------------------
    Protected Member Variables
    --------------------------------------------------------------------------*/

    private CoalesceObject _parent;
    private HashMap<String, CoalesceObject> _children = new HashMap<String, CoalesceObject>();

    /*--------------------------------------------------------------------------
    Constructors
    --------------------------------------------------------------------------*/
    /**
     * Class constructor. Creates a CoalesceObject class.
     */
    CoalesceObject()
    {
        // Do Nothing
    }

    /**
     * Class constructor. Creates a CoalesceObject class.
     * 
     * @param coalesceObject allowed object is {@link {@link CoalesceObject }
     */
    CoalesceObject(CoalesceObject coalesceObject)
    {
        // Copy Member Variables
        setParent(coalesceObject.getParent());
        _children = coalesceObject.getChildCoalesceObjects();
    }

    /*--------------------------------------------------------------------------
    Public Abstract Functions
    --------------------------------------------------------------------------*/

    @Override
    public abstract String getName();

    @Override
    public abstract void setName(String value);

    @Override
    public abstract DateTime getDateCreated();

    @Override
    public abstract void setDateCreated(DateTime value);

    @Override
    public abstract DateTime getLastModified();

    @Override
    public abstract String getType();

    /**
     * Returns the (XML) String of the {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceObject}.
     * 
     * @return (XML) String of the Coalesce object
     */
    public abstract String toXml();

    /**
     * Sets the value of the {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceObject}'s attribute corresponding
     * to the name argument.
     * 
     * @param name String, name of attribute to be set
     * @param value String, value to be assigned to the attribute
     * @return boolean indicating success/failure
     */
    public abstract boolean setAttribute(String name, String value);

    /*--------------------------------------------------------------------------
    Protected Abstract Functions
    --------------------------------------------------------------------------*/

    /**
     * Returns the value of the {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceObject}'s key attribute which
     * should be the same as the entity's ObjectKey database value.
     * 
     * @return String of the {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceObject}'s object key
     */
    protected abstract String getObjectKey();

    /**
     * Sets the value of the {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceObject}'s key attribute.
     * 
     * @param value String to be the {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceObject}'s object key
     */
    protected abstract void setObjectKey(String value);

    /**
     * Sets the value of the {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceObject}'s LastModified attribute.
     * 
     * @param value DateTime to be the {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceObject}'s last modified
     *            date
     */
    protected abstract void setObjectLastModified(DateTime value);

    /**
     * Returns the value of the {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceObject}'s status attribute.
     * 
     * @return String of the {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceObject}'s status
     */
    protected abstract String getObjectStatus();

    /**
     * Sets the value of the {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceObject}'s status attribute.
     * 
     * @param _value ECoalesceObjectStatus to be the {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceObject}'s
     *            status
     */
    protected abstract void setObjectStatus(ECoalesceObjectStatus status);

    /**
     * Returns a hashmap key-value pair of the {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceObject}'s
     * attributes.
     * 
     * @return Map<QName, String> of the {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceObject}'s attributes
     */
    protected abstract Map<QName, String> getAttributes();

    /**
     * Returns a hashmap key-value pair of the {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceObject}'s other
     * attributes - attributes that fall into the {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceObject}'s @XmlAnyAttribute
     * HashMap.
     * 
     * @return Map<QName, String> of the {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceObject}'s other
     *         attributes falling into the @XmlAnyAttribute because they weren't specified in the schema
     */
    protected abstract Map<QName, String> getOtherAttributes();

    /*--------------------------------------------------------------------------
    Public Interface Functions
    --------------------------------------------------------------------------*/

    @Override
    public ECoalesceObjectStatus getStatus()
    {
        // Get Status
        String statusString = this.getObjectStatus();

        // Valid String?
        if (StringHelper.isNullOrEmpty(statusString))
        {
            // No; Return Default
            return ECoalesceObjectStatus.ACTIVE;
        }
        else
        {
            // Yes; Parse String
            return ECoalesceObjectStatus.getTypeForLabel(getObjectStatus());
        }
    }

    @Override
    public void setStatus(ECoalesceObjectStatus value)
    {
        // Set Status SUccessful?
        this.setObjectStatus(value);

        // Yes; Update Last Modified
        this.setLastModified(JodaDateTimeHelper.nowInUtc());
    }

    @Override
    public CoalesceObject getParent()
    {
        return this._parent;
    }

    @Override
    public void setParent(CoalesceObject parent)
    {
        this._parent = parent;
    }

    @Override
    public String getKey()
    {
        return getObjectKey();
    }

    @Override
    public void setKey(String key)
    {
        this.setObjectKey(key);
    }

    @Override
    public void setKey(UUID guid)
    {
        setObjectKey(guid.toString());
    }

    @Override
    public String getTag()
    {
        return this.getAttribute("tag");
    }

    @Override
    public void setTag(String value)
    {
        this.setAttribute("tag", value);
    }

    @Override
    public boolean getFlatten()
    {
        String value = this.getAttribute("flatten");

        // Default Behavior.
        if (value == null) return true;

        return Boolean.parseBoolean(value);
    }

    @Override
    public void setFlatten(boolean value)
    {
        this.setAttribute("flatten", Boolean.toString(value));
    }

    @Override
    public void setLastModified(DateTime value)
    {
        if (value != null)
        {
            // Set Last Modified
            this.setObjectLastModified(value);

            // Bubble Up to Parent
            if (this._parent != null)
            {
                this._parent.setLastModified(value);
            }
        }
    }

    @Override
    public boolean getNoIndex()
    {
        String value = this.getAttribute("noindex");

        if (StringHelper.isNullOrEmpty(value)) return false;

        return Boolean.parseBoolean(value);
    }

    @Override
    public void setNoIndex(boolean value)
    {
        if (value)
        {
            setAttribute("noindex", Boolean.toString(value));
        }
        else
        {
            getOtherAttributes().remove("noindex");
        }
    }

    @Override
    public HashMap<String, CoalesceObject> getChildCoalesceObjects()
    {
        return this._children;
    }

    /**
     * Adds {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceObject} as a child. New values with new keys are
     * added, values with existing keys are replaced.
     * 
     * @param key key identifying the child Coalesce object.
     * @param value child Coalesce object to add to the Coalesce object's children
     */
    public void addChildCoalesceObject(String key, CoalesceObject value)
    {
        this._children.put(key, value);
    }

    /**
     * Returns the child CoalesceObject, for this CoalesceObject based on the String key parameter.
     * 
     * @param key allowed object is {@link String }
     * @return possible object is {@link CoalesceObject }
     */
    public CoalesceObject getChildCoalesceObject(String key)
    {
        return this._children.get(key);
    }

    @Override
    public String getNamePath()
    {
        if (this._parent == null)
        {
            return this.getName();
        }
        else
        {
            return this._parent.getNamePath() + "/" + this.getName();
        }
    }

    /*--------------------------------------------------------------------------
    Public Functions
    --------------------------------------------------------------------------*/

    /**
     * Returns the value of the {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceObject}'s attribute that
     * corresponds to the name.
     * 
     * @param name Attribute's name
     * @return String, Attribute's value
     */
    public String getAttribute(String name)
    {
        String attribute = getAttributes().get(new QName(name.toLowerCase()));

        if (attribute == null)
        {
            attribute = getOtherAttributes().get(new QName(name.toLowerCase()));
        }

        return attribute;
    }

    /**
     * Returns the String value of the {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceObject}'s other
     * attribute that corresponds to the name; other attributes are those that fall into the
     * {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceObject}'s @XmlAnyAttribute HashMap.
     * 
     * @param name Attribute's name
     * @return String, Attribute's value
     */
    public String getOtherAttribute(String name)
    {
        return this.getOtherAttributes().get(new QName(name));
    }

    /**
     * Returns the DateTime value of the {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceObject}'s other
     * attribute that corresponds to the name; other attributes are those that fall into the
     * {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceObject}'s @XmlAnyAttribute HashMap.
     * 
     * @param name Attribute's name
     * @return DateTime, Attribute's value
     */
    public DateTime getOtherAttributeAsDate(String name)
    {
        return JodaDateTimeHelper.fromXmlDateTimeUTC(getOtherAttribute(name));
    }

    /**
     * Sets the String value of the {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceObject}'s
     * {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceObject}'s other attribute that corresponds to the name;
     * other attributes are those that fall into the {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceObject}'s @XmlAnyAttribute
     * HashMap.
     * 
     * @param name String @XmlAnyAttribute attribute name
     * @param value @XmlAnyAttribute attribute value
     * @return boolean indicating success/failure
     */
    public boolean setOtherAttribute(String name, String value)
    {
        getOtherAttributes().put(new QName(name.toLowerCase()), value);
        return true;
    }

    /**
     * Sets the DateTime value of the {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceObject}'s
     * {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceObject}'s other attribute that corresponds to the name;
     * other attributes are those that fall into the {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceObject}'s @XmlAnyAttribute
     * HashMap.
     * 
     * @param name String, @XmlAnyAttribute attribute name
     * @param value @XmlAnyAttribute attribute DateTime value
     */
    public void setOtherAttributeAsDate(String name, DateTime value)
    {
        setOtherAttribute(name, JodaDateTimeHelper.toXmlDateTimeUTC(value));
    }

    /**
     * Returns the {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceObject}'s children that corresponds
     * to the provided name path.
     * 
     * @param namePath String corresponding to the desired child.
     * @return {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceObject} of the child or null if one is
     *         not found
     */
    public CoalesceObject getCoalesceObjectForNamePath(String namePath)
    {
        if (namePath == null) return null;

        String[] names = namePath.split("/");

        switch (names.length) {
        case 0:

            // No path. Object not found.
            break;

        case 1:

            // End of the path, is our Base Object named the Name Path?
            if (getName().equals(names[0]))
            {
                return this;
            }

            // No object found
            break;

        default:

            // Find next child
            CoalesceObject coalesceObject = null;

            for (CoalesceObject child : _children.values())
            {
                String childName = child.getName();

                if (childName != null && childName.equals(names[1]))
                {
                    coalesceObject = child;
                    break;
                }
            }

            if (coalesceObject != null)
            {

                String newPath = namePath.substring(namePath.indexOf("/") + 1);

                return coalesceObject.getCoalesceObjectForNamePath(newPath);

            }

            // No object found
            break;
        }

        return null;
    }

    /**
     * Returns the {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceObject}'s child that corresponds
     * to the provided key.
     * 
     * @param key String corresponding to the desired child key.
     * @return {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceObject} for the child or null if one is
     *         not found
     */
    public CoalesceObject getCoalesceObjectForKey(String key)
    {
        CoalesceObject result = null;

        if (this.getKey().equalsIgnoreCase(key))
        {
            result = this;
        }
        else
        {

            for (CoalesceObject child : _children.values())
            {
                result = child.getCoalesceObjectForKey(key);
                if (result != null) break;
            }
        }

        return result;
    }

    /*--------------------------------------------------------------------------
    Protected Functions
    --------------------------------------------------------------------------*/

    protected boolean initialize()
    {

        if (getKey() == null || getKey().equals(""))
        {
            setKey(java.util.UUID.randomUUID().toString());
        }

        DateTime utcDate = JodaDateTimeHelper.nowInUtc();

        if (getDateCreated() == null)
        {
            setDateCreated(utcDate);
        }
        if (getLastModified() == null)
        {
            setLastModified(utcDate);
        }

        return true;

    }

    protected boolean initialize(CoalesceObject coalesceObject)
    {
        setParent(coalesceObject.getParent());

        _children = coalesceObject.getChildCoalesceObjects();

        return true;
    }

    protected String getStringElement(String value)
    {
        if (value == null) return "";

        return value;
    }

    protected boolean getBooleanElement(Boolean value)
    {
        if (value == null)
        {
            return false;
        }
        else
        {
            return value;
        }
    }

    protected void addChildCoalesceObject(CoalesceObject newChild)
    {
        // Add to Parent's Child Collection
        if (!(_children.containsKey(newChild.getKey())))
        {
            _children.put(newChild.getKey(), newChild);
        }

    }

}