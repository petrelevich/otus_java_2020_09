package ru.otus.xml.dom;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sergey
 * created on 25.09.18.
 */
public class XMLreader {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        ClassLoader classLoader = ru.otus.xml.sax.XMLreader.class.getClassLoader();
        File file = new File(classLoader.getResource("data.xml").getFile());


        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document xmlDocument = dBuilder.parse(file);

        //http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
        xmlDocument.getDocumentElement().normalize();

        List<Share> shareList = new ArrayList<>();
        System.out.println("Root element :" + xmlDocument.getDocumentElement().getNodeName());

        NodeList nList = xmlDocument.getElementsByTagName("share");
        for (int idx = 0; idx < nList.getLength(); idx++) {
            Node nNode = nList.item(idx);
            System.out.println("Element :" + nNode.getNodeName());
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                System.out.println("ticker: " + eElement.getElementsByTagName("ticker").item(0).getTextContent());
                System.out.println("last: " + eElement.getElementsByTagName("last").item(0).getTextContent());
                System.out.println("date : " + eElement.getElementsByTagName("date").item(0).getTextContent());


                shareList.add(new Share(
                        eElement.getElementsByTagName("ticker").item(0).getTextContent(),
                        Double.valueOf(eElement.getElementsByTagName("last").item(0).getTextContent()),
                        eElement.getElementsByTagName("date").item(0).getTextContent()));
            }
        }
        System.out.println(shareList);

        //XPath
        XPath xPath = XPathFactory.newInstance().newXPath();
        String expression = "/shares/share[@id='1']";
        NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);


        for (int idx = 0; idx < nodeList.getLength(); idx++) {
            Node nNode = nodeList.item(idx);
            System.out.println("\nXPath Element :" + nNode.getNodeName());
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                System.out.println("ticker: " + eElement.getElementsByTagName("ticker").item(0).getTextContent());
                System.out.println("last: " + eElement.getElementsByTagName("last").item(0).getTextContent());
                System.out.println("date: " + eElement.getElementsByTagName("date").item(0).getTextContent());
            }
        }

    }
}
