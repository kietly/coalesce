//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.08.25 at 02:21:40 PM EDT 
//


package Coalesce.Framework.GeneratedJAXB;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.joda.time.DateTime;


/**
 * <p>Java class for section complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="section">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}recordset_node" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="key" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="datecreated" type="{http://www.w3.org/2001/XMLSchema}date" />
 *       &lt;attribute name="lastmodified" type="{http://www.w3.org/2001/XMLSchema}date" />
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="noindex" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="status" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "recordsetNode"
})
@XmlRootElement(name = "section")
public class Section {

    @XmlElement(name = "recordset_node")
    protected List<Recordset> recordsetNode;
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
    @XmlAttribute(name = "noindex")
    protected String noindex;
    @XmlAttribute(name = "status")
    protected String status;

    /**
     * Gets the value of the recordsetNode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the recordsetNode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRecordsetNode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Recordset }
     * 
     * 
     */
    public List<Recordset> getRecordsetNode() {
        if (recordsetNode == null) {
            recordsetNode = new ArrayList<Recordset>();
        }
        return this.recordsetNode;
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
     * Gets the value of the noindex property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoindex() {
        return noindex;
    }

    /**
     * Sets the value of the noindex property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoindex(String value) {
        this.noindex = value;
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

}
