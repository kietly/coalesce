//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.10.16 at 03:25:33 PM EDT 
//


package com.incadencecorp.coalesce.framework.generatedjaxb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;
import org.joda.time.DateTime;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}fieldhistory" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="key" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="datecreated" type="{http://www.w3.org/2001/XMLSchema}date" />
 *       &lt;attribute name="lastmodified" type="{http://www.w3.org/2001/XMLSchema}date" />
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="datatype" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="classificationmarking" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="label" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="status" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="disablehistory" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="previoushistorykey" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="modifiedby" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="modifiedbyip" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="inputlang" type="{http://www.w3.org/2001/XMLSchema}language" />
 *       &lt;attribute name="filename" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="extension" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="mimetype" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="size" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="hash" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;anyAttribute processContents='skip' namespace='##other'/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "fieldhistory"
})
@XmlRootElement(name = "field")
public class Field {

    protected List<Fieldhistory> fieldhistory;
    @XmlAttribute(name = "key")
    protected String key;
    @XmlAttribute(name = "datecreated")
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "date")
    protected DateTime datecreated;
    @XmlAttribute(name = "lastmodified")
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "date")
    protected DateTime lastmodified;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "datatype")
    protected String datatype;
    @XmlAttribute(name = "classificationmarking")
    protected String classificationmarking;
    @XmlAttribute(name = "label")
    protected String label;
    @XmlAttribute(name = "value")
    protected String value;
    @XmlAttribute(name = "status")
    protected String status;
    @XmlAttribute(name = "disablehistory")
    protected Boolean disablehistory;
    @XmlAttribute(name = "previoushistorykey")
    protected String previoushistorykey;
    @XmlAttribute(name = "modifiedby")
    protected String modifiedby;
    @XmlAttribute(name = "modifiedbyip")
    protected String modifiedbyip;
    @XmlAttribute(name = "inputlang")
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "language")
    protected Locale inputlang;
    @XmlAttribute(name = "filename")
    protected String filename;
    @XmlAttribute(name = "extension")
    protected String extension;
    @XmlAttribute(name = "mimetype")
    protected String mimetype;
    @XmlAttribute(name = "size")
    protected String size;
    @XmlAttribute(name = "hash")
    protected String hash;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Gets the value of the fieldhistory property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fieldhistory property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFieldhistory().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Fieldhistory }
     * 
     * 
     */
    public List<Fieldhistory> getFieldhistory() {
        if (fieldhistory == null) {
            fieldhistory = new ArrayList<Fieldhistory>();
        }
        return this.fieldhistory;
    }

    /**
     * Gets the value of the key property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets the value of the key property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKey(String value) {
        this.key = value;
    }

    /**
     * Gets the value of the datecreated property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public DateTime getDatecreated() {
        return datecreated;
    }

    /**
     * Sets the value of the datecreated property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatecreated(DateTime value) {
        this.datecreated = value;
    }

    /**
     * Gets the value of the lastmodified property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public DateTime getLastmodified() {
        return lastmodified;
    }

    /**
     * Sets the value of the lastmodified property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastmodified(DateTime value) {
        this.lastmodified = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the datatype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatatype() {
        return datatype;
    }

    /**
     * Sets the value of the datatype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatatype(String value) {
        this.datatype = value;
    }

    /**
     * Gets the value of the classificationmarking property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClassificationmarking() {
        return classificationmarking;
    }

    /**
     * Sets the value of the classificationmarking property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClassificationmarking(String value) {
        this.classificationmarking = value;
    }

    /**
     * Gets the value of the label property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the value of the label property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLabel(String value) {
        this.label = value;
    }

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the disablehistory property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDisablehistory() {
        return disablehistory;
    }

    /**
     * Sets the value of the disablehistory property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDisablehistory(Boolean value) {
        this.disablehistory = value;
    }

    /**
     * Gets the value of the previoushistorykey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrevioushistorykey() {
        return previoushistorykey;
    }

    /**
     * Sets the value of the previoushistorykey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrevioushistorykey(String value) {
        this.previoushistorykey = value;
    }

    /**
     * Gets the value of the modifiedby property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModifiedby() {
        return modifiedby;
    }

    /**
     * Sets the value of the modifiedby property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModifiedby(String value) {
        this.modifiedby = value;
    }

    /**
     * Gets the value of the modifiedbyip property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModifiedbyip() {
        return modifiedbyip;
    }

    /**
     * Sets the value of the modifiedbyip property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModifiedbyip(String value) {
        this.modifiedbyip = value;
    }

    /**
     * Gets the value of the inputlang property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Locale getInputlang() {
        return inputlang;
    }

    /**
     * Sets the value of the inputlang property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInputlang(Locale value) {
        this.inputlang = value;
    }

    /**
     * Gets the value of the filename property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Sets the value of the filename property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFilename(String value) {
        this.filename = value;
    }

    /**
     * Gets the value of the extension property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtension() {
        return extension;
    }

    /**
     * Sets the value of the extension property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtension(String value) {
        this.extension = value;
    }

    /**
     * Gets the value of the mimetype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMimetype() {
        return mimetype;
    }

    /**
     * Sets the value of the mimetype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMimetype(String value) {
        this.mimetype = value;
    }

    /**
     * Gets the value of the size property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSize() {
        return size;
    }

    /**
     * Sets the value of the size property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSize(String value) {
        this.size = value;
    }

    /**
     * Gets the value of the hash property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHash() {
        return hash;
    }

    /**
     * Sets the value of the hash property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHash(String value) {
        this.hash = value;
    }

    /**
     * Gets a map that contains attributes that aren't bound to any typed property on this class.
     * 
     * <p>
     * the map is keyed by the name of the attribute and 
     * the value is the string value of the attribute.
     * 
     * the map returned by this method is live, and you can add new attribute
     * by updating the map directly. Because of this design, there's no setter.
     * 
     * 
     * @return
     *     always non-null
     */
    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }

}
