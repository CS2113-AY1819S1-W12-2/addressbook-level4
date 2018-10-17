//@@author Limminghong
package seedu.address.storage;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;

/**
 * A class that converts files between xml and csv
 */
public class XmlConverter {

    public XmlConverter () {}

    /**
     * @param xmlSource the file that is converted to .csv format
     * @throws Exception when transformation fails
     */
    public static void xmlToCsv (File xmlSource) throws Exception {
        File stylesheet = new File("src\\main\\resources\\view\\style.xsl");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlSource);

        StreamSource styleSource = new StreamSource(stylesheet);
        Transformer transformer = TransformerFactory.newInstance()
                .newTransformer(styleSource);
        Source source = new DOMSource(document);
        // TODO: make "data/addressbook.csv" accessible to all objects
        Result outputTarget = new StreamResult(new File("data/addressbook.csv"));
        transformer.transform(source, outputTarget);
    }
}
