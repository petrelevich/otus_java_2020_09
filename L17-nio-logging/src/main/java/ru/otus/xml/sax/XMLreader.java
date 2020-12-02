package ru.otus.xml.sax;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author sergey
 */
public class XMLreader {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        ClassLoader classLoader = XMLreader.class.getClassLoader();
        File file = new File(classLoader.getResource("data.xml").getFile());

        List<Share> shareList = new XMLreader().parse(file);
        System.out.println(shareList);
    }

    private List<Share> parse(File file) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        XMLhandler handler = new XMLhandler();
        saxParser.parse(file, handler);
        return handler.getResult();
    }
}


