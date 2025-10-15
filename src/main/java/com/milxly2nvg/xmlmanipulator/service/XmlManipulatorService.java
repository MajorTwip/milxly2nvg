package com.milxly2nvg.xmlmanipulator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Service for XML file manipulation operations
 */
@Slf4j
@Service
public class XmlManipulatorService {

    private final DocumentBuilderFactory documentBuilderFactory;
    private final TransformerFactory transformerFactory;
    private final XPathFactory xPathFactory;

    public XmlManipulatorService() {
        this.documentBuilderFactory = DocumentBuilderFactory.newInstance();
        this.transformerFactory = TransformerFactory.newInstance();
        this.xPathFactory = XPathFactory.newInstance();
    }

    /**
     * Read and display XML file content
     */
    public void readXml(String filePath) throws Exception {
        log.info("Reading XML file: {}", filePath);
        
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            log.error("File not found: {}", filePath);
            return;
        }

        Document document = parseXmlFile(filePath);
        String xmlContent = documentToString(document);
        
        log.info("XML Content:\n{}", xmlContent);
        log.info("Root element: {}", document.getDocumentElement().getNodeName());
    }

    /**
     * Validate XML file structure
     */
    public void validateXml(String filePath) throws Exception {
        log.info("Validating XML file: {}", filePath);
        
        try {
            Document document = parseXmlFile(filePath);
            log.info("✓ XML file is well-formed");
            log.info("Root element: {}", document.getDocumentElement().getNodeName());
            
            NodeList childNodes = document.getDocumentElement().getChildNodes();
            int elementCount = 0;
            for (int i = 0; i < childNodes.getLength(); i++) {
                if (childNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    elementCount++;
                }
            }
            log.info("Number of child elements: {}", elementCount);
            
        } catch (SAXException e) {
            log.error("✗ XML file is not well-formed: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Transform XML file (copy with modifications)
     */
    public void transformXml(String inputPath, String outputPath) throws Exception {
        log.info("Transforming XML from {} to {}", inputPath, outputPath);
        
        Document document = parseXmlFile(inputPath);
        
        // Add a comment to show transformation
        Node comment = document.createComment("Transformed by XML Manipulator");
        document.getDocumentElement().insertBefore(comment, document.getDocumentElement().getFirstChild());
        
        writeXmlToFile(document, outputPath, true);
        log.info("✓ Transformation completed successfully");
    }

    /**
     * Prettify XML file with proper indentation
     */
    public void prettifyXml(String inputPath, String outputPath) throws Exception {
        log.info("Prettifying XML file: {}", inputPath);
        
        Document document = parseXmlFile(inputPath);
        
        String finalOutputPath = outputPath != null ? outputPath : inputPath.replace(".xml", "_pretty.xml");
        
        writeXmlToFile(document, finalOutputPath, true);
        log.info("✓ Prettified XML written to: {}", finalOutputPath);
    }

    /**
     * Extract data using XPath expression
     */
    public void extractByXPath(String filePath, String xpathExpression) throws Exception {
        log.info("Extracting data from {} using XPath: {}", filePath, xpathExpression);
        
        Document document = parseXmlFile(filePath);
        XPath xpath = xPathFactory.newXPath();
        
        try {
            XPathExpression expression = xpath.compile(xpathExpression);
            NodeList nodeList = (NodeList) expression.evaluate(document, XPathConstants.NODESET);
            
            log.info("Found {} node(s) matching the XPath expression", nodeList.getLength());
            
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                log.info("\nNode {}:", i + 1);
                log.info("  Name: {}", node.getNodeName());
                log.info("  Type: {}", getNodeTypeName(node.getNodeType()));
                
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    log.info("  Content: {}", node.getTextContent().trim());
                } else if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
                    log.info("  Value: {}", node.getNodeValue());
                }
            }
        } catch (XPathExpressionException e) {
            log.error("Invalid XPath expression: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Run demonstration of XML manipulation features
     */
    public void runDemo() throws Exception {
        log.info("\n=== Starting XML Manipulation Demo ===\n");
        
        // Create demo directory if it doesn't exist
        Path demoDir = Paths.get("data");
        if (!Files.exists(demoDir)) {
            Files.createDirectories(demoDir);
        }
        
        // Create sample XML file
        String sampleXmlPath = "data/demo_sample.xml";
        createSampleXmlFile(sampleXmlPath);
        
        // Demo 1: Read XML
        log.info("\n--- Demo 1: Reading XML File ---");
        readXml(sampleXmlPath);
        
        // Demo 2: Validate XML
        log.info("\n--- Demo 2: Validating XML File ---");
        validateXml(sampleXmlPath);
        
        // Demo 3: Prettify XML
        log.info("\n--- Demo 3: Prettifying XML File ---");
        prettifyXml(sampleXmlPath, "data/demo_pretty.xml");
        
        // Demo 4: Extract with XPath
        log.info("\n--- Demo 4: Extracting Data with XPath ---");
        extractByXPath(sampleXmlPath, "//book");
        
        // Demo 5: Transform XML
        log.info("\n--- Demo 5: Transforming XML File ---");
        transformXml(sampleXmlPath, "data/demo_transformed.xml");
        
        log.info("\n=== Demo Completed Successfully ===");
        log.info("Demo files created in 'data' directory:");
        log.info("  - demo_sample.xml");
        log.info("  - demo_pretty.xml");
        log.info("  - demo_transformed.xml");
    }

    /**
     * Parse XML file into Document object
     */
    private Document parseXmlFile(String filePath) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        return documentBuilder.parse(new File(filePath));
    }

    /**
     * Convert Document to String
     */
    private String documentToString(Document document) throws TransformerException {
        StringWriter writer = new StringWriter();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.transform(new DOMSource(document), new StreamResult(writer));
        return writer.toString();
    }

    /**
     * Write Document to file
     */
    private void writeXmlToFile(Document document, String filePath, boolean indent) throws Exception {
        Transformer transformer = transformerFactory.newTransformer();
        if (indent) {
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        }
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(filePath));
        transformer.transform(source, result);
    }

    /**
     * Create sample XML file for demo
     */
    private void createSampleXmlFile(String filePath) throws Exception {
        String xmlContent = """
                <?xml version="1.0" encoding="UTF-8"?>
                <bookstore>
                    <book id="1" category="fiction">
                        <title>The Great Adventure</title>
                        <author>John Doe</author>
                        <year>2023</year>
                        <price>29.99</price>
                    </book>
                    <book id="2" category="science">
                        <title>Understanding Quantum Physics</title>
                        <author>Jane Smith</author>
                        <year>2022</year>
                        <price>45.50</price>
                    </book>
                    <book id="3" category="programming">
                        <title>Mastering Spring Boot</title>
                        <author>Bob Johnson</author>
                        <year>2024</year>
                        <price>39.99</price>
                    </book>
                </bookstore>
                """;
        
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(xmlContent);
        }
        
        log.info("Sample XML file created: {}", filePath);
    }

    /**
     * Get human-readable node type name
     */
    private String getNodeTypeName(short nodeType) {
        return switch (nodeType) {
            case Node.ELEMENT_NODE -> "Element";
            case Node.ATTRIBUTE_NODE -> "Attribute";
            case Node.TEXT_NODE -> "Text";
            case Node.CDATA_SECTION_NODE -> "CDATA Section";
            case Node.COMMENT_NODE -> "Comment";
            default -> "Unknown";
        };
    }
}
