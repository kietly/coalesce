package com.incadencecorp.coalesce.common.helpers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.NullArgumentException;
import org.joda.time.DateTime;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

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

/**
 * Provides helper methods for serializing and deserializing objects to XML.
 * 
 * @author InCadence
 *
 */
public class XmlHelper {

    protected static ConcurrentHashMap<String, JAXBContext> jaxbContexts = new ConcurrentHashMap<String, JAXBContext>();
    
    /**
     * Return the {@link String} that contains the serialized representation of the provided object using the 'ISO-8859-1'
     * encoding format.
     * 
     * @param obj the object to be serialized.
     * @return the {@link String} that contains the serialized representation of the object.
     */
    public static String serialize(Object obj)
    {
        try
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            
            // Get JAXB Context
            String className = obj.getClass().getName();            
            JAXBContext context;
            
            if (jaxbContexts.containsKey(className)) {
                context = jaxbContexts.get(className);
            } else {
                // Haven't seen before; create and add to concurrent hash map
                context = JAXBContext.newInstance(obj.getClass());
                jaxbContexts.put(className, context);
            }

            // Marshal
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); // pretty
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1"); // specify
            marshaller.marshal(obj, out);

            return new String(out.toByteArray());
        }
        catch (JAXBException e)
        {
            return null;
        }
    }

    /**
     * Return the {@link String} that contains the serialized representation of the provided object using the specified
     * encoding format.
     * 
     * @param obj the object to be serialized.
     * @param encodingFormat the encoding format to use.
     * @return the {@link String} that contains the serialized representation of the object.
     */
    public static String serialize(Object obj, String encodingFormat)
    {
        try
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            // Get JAXB Context
            String className = obj.getClass().getName();            
            JAXBContext context;
            
            if (jaxbContexts.containsKey(className)) {
                context = jaxbContexts.get(className);
            } else {
                // Haven't seen before; create and add to concurrent hash map
                context = JAXBContext.newInstance(obj.getClass());
                jaxbContexts.put(className, context);
            }

            // Marshal
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); // pretty
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encodingFormat); // specify
            marshaller.marshal(obj, out);

            return new String(out.toByteArray());
        }
        catch (JAXBException e)
        {
            return null;
        }
    }

    /**
     * Create the object instance of the <code>classType</code> that represents the serialized XML string provided using the
     * <code>xml</code>. If there is any problem deserializing the object then <code>null</code> will be returned.
     * 
     * @param xml the XML string to be deserialized.
     * @param classType the type of the object to be deserialized.
     * @return the object instance of the provided XML.
     */
    public static Object deserialize(String xml, Class<?> classType)
    {
        try
        {
            InputStream in = new ByteArrayInputStream(xml.getBytes());
            
            // Get JAXB Context
            String className = classType.getName();            
            JAXBContext context;
            
            if (jaxbContexts.containsKey(className)) {
                context = jaxbContexts.get(className);
            } else {
                // Haven't seen before; create and add to concurrent hash map
                context = JAXBContext.newInstance(classType);
                jaxbContexts.put(className, context);
            }
            
            Unmarshaller unmarshaller = context.createUnmarshaller();

            return unmarshaller.unmarshal(in);
        }
        catch (JAXBException ex)
        {
            return null;
        }
    }

    // -----------------------------------------------------------------------'
    // Public Shared Methods - Attribute Helpers
    // -----------------------------------------------------------------------'

    /**
     * Returns the attribute with the specified name in the provided node. If the attribute name cannot be found then an
     * empty string is returned.
     * 
     * @param xmlNode the node containing the attribute.
     * @param name the name of the attribute.
     * @return the value of the attribute requested.
     */
    public static String getAttribute(Node xmlNode, String name)
    {
        if (xmlNode == null) throw new NullArgumentException("xmlNode");

        if (StringHelper.isNullOrEmpty(name)) return "";

        String value = "";

        NamedNodeMap attributes = xmlNode.getAttributes();

        if (attributes != null)
        {
            Node attribute = attributes.getNamedItem(name);
            if (attribute != null) value = attribute.getNodeValue();
        }

        return value;
    }

    /**
     * Returns the attribute with the specified name in the provided node as a {@link org.joda.time.DateTime}. If the
     * attribute name cannot be found then <code>null</code> is returned.
     * 
     * @param xmlNode the node containing the attribute.
     * @param name the name of the attribute.
     * @return the date representation of the attribute requested.
     */
    public static DateTime getAttributeAsDate(Node xmlNode, String name)
    {
        if (xmlNode == null) throw new NullArgumentException("xmlNode");

        String dateString = getAttribute(xmlNode, name);

        if (StringHelper.isNullOrEmpty(dateString))
        {
            return null;
        }
        else
        {
            return JodaDateTimeHelper.fromXmlDateTimeUTC(dateString);
        }
    }

    /**
     * Sets the value of the specified attribute using the provided value. If the attribute does not exist yet then a new
     * attribute is created for the node and the value is set.
     * 
     * @param doc the parent XML document for the XML node.
     * @param xmlNode the XML node that contains the attribute to change.
     * @param name the attribute name.
     * @param value the value to set the attribute with.
     */
    public static void setAttribute(Document doc, Node xmlNode, String name, String value)
    {
        if (doc == null) throw new NullArgumentException("doc");
        if (xmlNode == null) throw new NullArgumentException("xmlNode");
        if (name == null || StringHelper.isNullOrEmpty(name.trim())) throw new IllegalArgumentException("name cannot be null or empty");

        NamedNodeMap attributes = xmlNode.getAttributes();
        Node nameNode = attributes.getNamedItem(name);

        if (nameNode == null)
        {
            nameNode = doc.createAttribute(name);
            attributes.setNamedItem(nameNode);

        }

        nameNode.setNodeValue(value);

    }

    /**
     * Sets the value of the specified attribute using the provided {@link DateTime}. If the attribute does not exist yet
     * then a new attribute is created for the node and the value is set.
     * 
     * @param doc the parent XML document for the XML node.
     * @param xmlNode the XML node that contains the attribute to change.
     * @param name the attribute name.
     * @param value the value to set the attribute with.
     */
    public static void setAttribute(Document doc, Node xmlNode, String name, DateTime value)
    {
        if (value == null)
        {
            XmlHelper.setAttribute(doc, xmlNode, name, (String) null);
        }
        else
        {
            XmlHelper.setAttribute(doc, xmlNode, name, JodaDateTimeHelper.toXmlDateTimeUTC(value));
        }
    }

    // -----------------------------------------------------------------------'
    // public Shared Methods - XML Formatting
    // -----------------------------------------------------------------------'

    /**
     * Returns the XML representation of the {@link org.w3c.dom.Document} provided.
     * 
     * @param doc the document to be converted.
     * @return the XML representation of the {@link org.w3c.dom.Document}.
     */
    public static String formatXml(Document doc)
    {
        if (doc == null) return null;

        return formatXml(doc.getFirstChild());
    }

    /**
     * Returns the XML representation of the {@link org.w3c.dom.Node} provided.
     * 
     * @param node the node to be converted.
     * @return the XML representation of the {@link org.w3c.dom.Node}.
     */
    public static String formatXml(Node node)
    {
        if (node == null) return null;

        try
        {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(node);

            transformer.transform(source, result);

            return result.getWriter().toString().replaceAll("(?m)^[ \t]*\r?\n", "");
        }
        catch (TransformerException e)
        {
            return null;
        }
    }

    /**
     * Returns the {@link org.w3c.dom.Document} representing the XML provided.
     * 
     * @param xml the XML value to convert.
     * @return the {@link org.w3c.dom.Document} representing the XML provided.
     * 
     * @throws SAXException
     * @throws IOException
     */
    public static Document loadXmlFrom(String xml) throws SAXException, IOException
    {
        if (xml == null) throw new NullArgumentException("xml");

        return loadXmlFrom(new ByteArrayInputStream(xml.getBytes()));
    }

    /**
     * Returns the {@link org.w3c.dom.Document} representing the {@link InputStream} provided.
     * 
     * @param is the InputStream containing the XML to convert.
     * @return the {@link org.w3c.dom.Document} representing the {@link InputStream} provided.
     * 
     * @throws SAXException
     * @throws IOException
     */
    public static Document loadXmlFrom(InputStream is) throws SAXException, IOException
    {
        if (is == null) throw new NullArgumentException("is");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

        DocumentBuilder builder = null;
        try
        {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);

            return doc;

        }
        catch (ParserConfigurationException ex)
        {
        }
        finally
        {
            is.close();
        }

        return null;

    }

}
