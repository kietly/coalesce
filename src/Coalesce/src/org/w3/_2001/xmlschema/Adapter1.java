//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.08.21 at 10:26:26 AM EDT 
//


package org.w3._2001.xmlschema;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.joda.time.DateTime;

public class Adapter1
    extends XmlAdapter<String, DateTime>
{


    public DateTime unmarshal(String value) {
        return (Coalesce.Common.Helpers.DateTimeConverter.parseDate(value));
    }

    public String marshal(DateTime value) {
        return (Coalesce.Common.Helpers.DateTimeConverter.printDate(value));
    }

}
