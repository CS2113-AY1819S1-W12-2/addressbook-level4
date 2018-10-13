package seedu.address.storage;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;

import org.w3c.dom.Document;

/**
 * Converts files between xml and csv
 */
public class XmlConverter {

    public XmlConverter () {}

    /**
     * @param xmlSource the file that is converted to .csv format
     * @throws Exception when transformation fails
     */
    public static void XmlToCsv (File xmlSource) throws Exception {
        File stylesheet = new File("data/style.xsl");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlSource);

        StreamSource styleSource = new StreamSource(stylesheet);
        Transformer transformer = TransformerFactory.newInstance()
                .newTransformer(styleSource);
        Source source = new DOMSource(document);
        Result outputTarget = new StreamResult(new File("data/addressbook.csv"));
        transformer.transform(source, outputTarget);
    }
}
