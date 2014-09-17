package Coalesce.Common.Helpers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.joda.time.DateTime;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

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

public class XmlHelper {

    // private static String MODULE_NAME = "Coalesce.Common.Helpers.XmlHelper";

    // throw new IllegalArgumentException(MODULE_NAME + " : EstablishLinkage");

    public static String Serialize(Object obj)
    {
        try
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            JAXBContext context;
            context = JAXBContext.newInstance(obj.getClass());

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

    public static String Serialize(Object obj, String encodingFormat)
    {
        try
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            JAXBContext context;
            context = JAXBContext.newInstance(obj.getClass());

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

    public static Object Deserialize(String xml, Class<?> classType)
    {
        try
        {
            InputStream in = new ByteArrayInputStream(xml.getBytes());
            JAXBContext context = JAXBContext.newInstance(classType);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            return unmarshaller.unmarshal(in);
        }
        catch (JAXBException ex)
        {
            return null;
        }
    }

    // todo: make shared
    /*
     * public CallResult InitializeXmlWriter(XMLStreamWriter Writer) { try { // TODO // StringWriter sw = new StringWriter();
     * // Writer = factory.createXMLStreamWriter(sw); // // // Create a new Writer // // Create Inner Stream // MemoryStream
     * ms; // ms = new MemoryStream; // // // Create Writer and set properties // Writer = new XMLStreamWriter(ms,
     * System.Text.Encoding.UTF8) // Writer.Indentation = 1; // Writer.IndentChar = Chr(9) // Tab // Writer.Formatting =
     * Xml.Formatting.Indented
     * 
     * // return Success return CallResult.successCallResult;
     * 
     * } catch (Exception ex) { // return Failed Error return new CallResult(CallResults.FAILED_ERROR, ex,
     * "Coalesce.Common.Helpers.XmlHelper"); } }
     * 
     * public CallResult InitializeBase64OutputStream(XMLStreamWriter Writer) { try { // TODO // OutputStream stream = new
     * OutputStream(); return CallResult.successCallResult;
     * 
     * } catch (Exception ex) { // return Failed Error return new CallResult(CallResults.FAILED_ERROR, ex,
     * "Coalesce.Common.Helpers.XmlHelper"); } }
     * 
     * public CallResult XmlWriterToXml(XMLStreamWriter Writer, String Xml) { try { // TODO // if (Writer == Nothing){ //
     * //do nothing // }else{ // // Flush // Writer.flush(); // // // Get Xml // Dim tr As IO.TextReader = New
     * IO.StreamReader(Writer.BaseStream) // Writer.BaseStream.Seek(0, IO.SeekOrigin.Begin) // // // Done, set the out
     * parameter // Xml = tr.ReadToEnd // }
     * 
     * // return Success return CallResult.successCallResult;
     * 
     * } catch (Exception ex) { // return Failed Error return new CallResult(CallResults.FAILED_ERROR, ex,
     * "Coalesce.Common.Helpers.XmlHelper"); } }
     * 
     * // -----------------------------------------------------------------------' // public Shared Methods -
     * Encoding/Decoding Helpers // -----------------------------------------------------------------------' // shared public
     * CallResult WriteBase64(XMLStreamWriter Writer, byte[] Buffer) { try { // Writes Base64 with Breaks. 57 bytes is
     * standard for MIME (e.g. .MHT Base64), so is used here as well. // modulus operator, in java is %
     * 
     * // TODO: // String dirName="C:\\Users\\tmagulick\\Pictures"; // ByteArrayOutputStream baos=new
     * ByteArrayOutputStream(1000); // BufferedImage img=ImageIO.read(new File(dirName,"Like.jpg")); // ImageIO.write(img,
     * "jpg", baos); // baos.flush(); // // String base64String=Base64.encode(baos.toByteArray()); // baos.close(); // //
     * Buffer = Base64.decode(base64String); // // // BufferedImage bufferedImage = ImageIO.read(imgPath); // // // // // get
     * DataBufferBytes from Raster // // WritableRaster raster = bufferedImage .getRaster(); // // DataBufferByte data =
     * (DataBufferByte) raster.getDataBuffer(); // // // // byte[] bData = data.getData(); // // // // //Start ?????? // //
     * image img = newii // // try (OutputStream stream = new FileOutputStream("c:/decode/abc.bmp")) { // // stream.write(b);
     * // // stream.write(Buffer); // // } // // catch(IOException io){ // // return new CallResult(CallResults.FAILED_ERROR,
     * io, "Coalesce.Common.Helpers.XmlHelper"); // // } // // //End ?????? // // int Offset = 0; // int Remainder =
     * Buffer.length % 57; // // for (int i = 0; i < (Buffer.length / 57); i++){ // Writer.WriteString("\r"); //
     * Writer.WriteBase64(Buffer, Offset, 57); // Offset += 57; // } // // // Write remainder // if (Remainder > 0) { //
     * Writer.WriteString(vbCr); // Writer.WriteBase64(Buffer, Offset, Remainder); // }
     * 
     * // return Success return new CallResult(CallResults.SUCCESS);
     * 
     * } catch (Exception ex) { // return Failed Error return new CallResult(CallResults.FAILED_ERROR, ex,
     * "Coalesce.Common.Helpers.XmlHelper"); } }
     */
    // -----------------------------------------------------------------------'
    // public Shared Methods - Attribute Helpers
    // -----------------------------------------------------------------------'

    public static String GetAttribute(Node xmlNode, String Name)
    {
        // try{-}catch ( omitted intentionally; Caller must handle.

        // Check for the Attribute Node
        // Node AttributeNode;
        // xmlNode.
        // AttributeNode = xmlNode.SelectSingleNode("@" + Name);
        //
        // if (AttributeNode == Nothing) {
        // // No Node; return Empty String Value
        // return "";
        // }else{
        // // Found Node; return Value
        // return AttributeNode.Value;
        // }

        // Node currentItem = nl.item(i);

        String value = null;

        NamedNodeMap attributes = xmlNode.getAttributes();

        if (attributes != null)
        {
            Node attribute = attributes.getNamedItem(Name);
            if (attribute != null) value = attribute.getNodeValue();

        }

        return value;
    }

    // shared
    public static DateTime GetAttributeAsDate(Node Node, String Name)
    {
        // try{-}catch ( omitted intentionally; Caller must handle

        // Get Date String from Attribute
        String DateString = GetAttribute(Node, Name);

        if (StringHelper.IsNullOrEmpty(DateString))
        {
            return JodaDateTimeHelper.NowInUtc();
        }
        else
        {
            return JodaDateTimeHelper.FromXmlDateTimeUTC(DateString);
        }

        // Parse Date
        // Dim DateVal As Date
        // If (Date.try{Parse(DateString, DateVal) = True) Then
        // // return Date (Note: try{Parse returns the date as local, even if
        // // the string is a UTC (Z) datetime string. This ensures the
        // // date returned is UTC.)
        // return DateVal.ToUniversalTime
        // Else
        // // return 0 Ticks Date
        // return New Date(0)
        // End If
    }

    // TODO: function
    // public Node SelectSingleNode(Document doc, String xpath){
    //
    // XPathFactory factory=XPathFactory.newInstance();
    // XPath xPath=factory.newXPath();
    //
    //
    // /*DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
    // domFactory.setNamespaceAware(true);
    // DocumentBuilder builder = domFactory.newDocumentBuilder();
    // Document doc = builder.parse("test.xml");
    // XPath xpath = XPathFactory.newInstance().newXPath();
    // XPathExpression expr = xpath.compile("//entry/link/@href");
    // Object result = expr.evaluate(doc, XPathConstants.NODESET);
    // NodeList nodes = (NodeList) result;
    // for (int i = 0; i < nodes.getLength(); i++) {
    // System.out.println(nodes.item(i));
    // }*/
    //
    // }

    // shared
    public static void SetAttribute(Document Doc, Node Node, String Name, String Value)
    {
        Node.getAttributes().getNamedItem(Name).setNodeValue(Value);

        // TODO:
        // // Check for the Attribute Node
        // Node AttributeNode;
        // AttributeNode = Node.SelectSingleNode("@" & Name & "");
        //
        // if (AttributeNode == null) {
        // // Doesn't Exist; Create and Add
        // AttributeNode = Doc.CreateAttribute(Name);
        // AttributeNode.Value = Value;
        // Node.Attributes.SetNamedItem(AttributeNode);
        // }else{
        // // Already Exists; Update
        // //AttributeNode.Value = Value;
        // AttributeNode.setNodeValue(Value);
        // }
    }

    // shared
    public void SetAttributeAsDate(Document Doc, Node Node, String Name, DateTime Value)
    {
        Node.getAttributes().getNamedItem(Name).setNodeValue(JodaDateTimeHelper.ToXmlDateTimeUTC(Value));

        // Check for the Attribute Node
        // TODO:
        // Node AttributeNode;
        // AttributeNode = Node.SelectSingleNode("@" & Name & "");
        //
        // if (AttributeNode == null) {
        // // Doesn't Exist; Create and Add
        // AttributeNode = Doc.CreateAttribute(Name);
        // AttributeNode.Value = DateTimeHelper.ToXmlDateTimeUTC(Value);
        // Node.Attributes.SetNamedItem(AttributeNode);
        // }else{
        // // Already Exists; Update
        // AttributeNode.Value = DateTimeHelper.ToXmlDateTimeUTC(Value);
        // }
    }

    // -----------------------------------------------------------------------'
    // public Shared Methods - XML Formatting
    // -----------------------------------------------------------------------'

    public static String FormatXml(Document Doc)
    {
        return FormatXml(Doc.getFirstChild());
    }

    public static String FormatXml(Node Node)
    {
        try
        {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(Node);

            transformer.transform(source, result);

            return result.getWriter().toString().replaceAll("(?m)^[ \t]*\r?\n", "");
        }
        catch (TransformerException e)
        {
            // TODO Auto-generated catch block
            return null;
        }
    }

    public static org.w3c.dom.Document loadXMLFrom(String xml) throws org.xml.sax.SAXException, java.io.IOException
    {
        return loadXMLFrom(new java.io.ByteArrayInputStream(xml.getBytes()));
    }

    public static org.w3c.dom.Document loadXMLFrom(java.io.InputStream is) throws org.xml.sax.SAXException,
            java.io.IOException
    {
        javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        javax.xml.parsers.DocumentBuilder builder = null;
        try
        {
            builder = factory.newDocumentBuilder();
        }
        catch (javax.xml.parsers.ParserConfigurationException ex)
        {
        }
        org.w3c.dom.Document doc = builder.parse(is);
        is.close();
        return doc;
    }

}
