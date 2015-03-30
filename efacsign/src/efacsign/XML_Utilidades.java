/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package efacsign;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author desarrollador
 */
public class XML_Utilidades {

    public Document getDoc(String dir) throws SAXException, ParserConfigurationException, IOException {

        File fXmlFile = new File(dir);

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);

        return doc;
    }

    public String convertDocumentToString(Document doc) {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = tf.newTransformer();
            // below code to remove XML declaration
            // transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            String output = writer.getBuffer().toString();
            return output;
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String converBase64(Document doc) throws UnsupportedEncodingException {

        String str = convertDocumentToString(doc);

        String bytesEncoded = DatatypeConverter.printBase64Binary(str.getBytes("UTF-8"));

        return bytesEncoded;
    }

    public String getNodes(String rootNodo, String infoNodo, Document doc) {

        String resultNodo = null;
        Element docEle = doc.getDocumentElement();

        NodeList studentList = docEle.getElementsByTagName(rootNodo);

        if (studentList.getLength() > 0) {
            Node node = studentList.item(0);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) node;
                NodeList nodeList = e.getElementsByTagName(infoNodo);
                resultNodo = nodeList.item(0).getChildNodes().item(0).getNodeValue();
            }
        }
        return resultNodo;
    }

    public String getNode(String rootNodo, String infoNodo, Document doc) {

        String resultNodo = null;
        Element docEle = doc.getDocumentElement();

        NodeList studentList = docEle.getElementsByTagName(rootNodo);

        if (studentList.getLength() > 0) {
            Node node = studentList.item(0);
            if (node.getNodeType() == Node.DOCUMENT_NODE) {
                Element e = (Element) node;
                NodeList nodeList = e.getElementsByTagName(infoNodo);
                resultNodo = nodeList.item(0).getTextContent();
            }
        }
        return resultNodo;
    }

    public String getNodeXml(String rootNodo, Document doc) {
        Element docEle = doc.getDocumentElement();
        Node studentList = docEle.getElementsByTagName(rootNodo).item(0);        
        //System.out.println(studentList.getNodeType() == Node.ELEMENT_NODE);
        String xml = nodeToString(studentList);
        xml = xml.replace("&lt;", "<");
        xml = xml.replace("&gt;", ">");
        return xml;
    }

    public String getLastNode(String pathLevelXML, String nodo, Document doc) throws XPathExpressionException {

        //Ejemplo: //RespuestaAutorizacionComprobante/autorizaciones/autorizacion[last()]/estado
        String pathFull = pathLevelXML + nodo;

        XPath xpath = XPathFactory.newInstance().newXPath();

        return xpath.evaluate(pathFull, doc);

    }

    private static String nodeToString(Node node) {
        StringWriter sw = new StringWriter();
        try {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.transform(new DOMSource(node), new StreamResult(sw));
        } catch (TransformerException te) {
            System.out.println("nodeToString Transformer Exception");
        }
        return sw.toString();
    }

}
