package com.milxly2nvg.xmlmanipulator;

import com.milxly2nvg.xmlmanipulator.service.XmlManipulatorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class XmlManipulatorServiceTest {

    @Autowired
    private XmlManipulatorService xmlManipulatorService;

    @TempDir
    Path tempDir;

    @Test
    void testReadXml() throws Exception {
        // Create a test XML file
        File testFile = tempDir.resolve("test.xml").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<root><item>Test</item></root>");
        }

        // Test reading the file
        assertDoesNotThrow(() -> xmlManipulatorService.readXml(testFile.getAbsolutePath()));
    }

    @Test
    void testValidateXml() throws Exception {
        // Create a valid XML file
        File testFile = tempDir.resolve("valid.xml").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<root><item>Test</item></root>");
        }

        // Test validation
        assertDoesNotThrow(() -> xmlManipulatorService.validateXml(testFile.getAbsolutePath()));
    }

    @Test
    void testPrettifyXml() throws Exception {
        // Create a test XML file
        File inputFile = tempDir.resolve("input.xml").toFile();
        try (FileWriter writer = new FileWriter(inputFile)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><item>Test</item></root>");
        }

        File outputFile = tempDir.resolve("output.xml").toFile();

        // Test prettifying
        assertDoesNotThrow(() -> 
            xmlManipulatorService.prettifyXml(inputFile.getAbsolutePath(), outputFile.getAbsolutePath())
        );
        
        assertTrue(outputFile.exists());
    }

    @Test
    void testTransformXml() throws Exception {
        // Create a test XML file
        File inputFile = tempDir.resolve("input.xml").toFile();
        try (FileWriter writer = new FileWriter(inputFile)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<root><item>Test</item></root>");
        }

        File outputFile = tempDir.resolve("transformed.xml").toFile();

        // Test transformation
        assertDoesNotThrow(() -> 
            xmlManipulatorService.transformXml(inputFile.getAbsolutePath(), outputFile.getAbsolutePath())
        );
        
        assertTrue(outputFile.exists());
    }

    @Test
    void testExtractByXPath() throws Exception {
        // Create a test XML file
        File testFile = tempDir.resolve("test.xml").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<root><item id=\"1\">Test</item></root>");
        }

        // Test XPath extraction
        assertDoesNotThrow(() -> 
            xmlManipulatorService.extractByXPath(testFile.getAbsolutePath(), "//item")
        );
    }

    @Test
    void testRunDemo() {
        // Test demo execution
        assertDoesNotThrow(() -> xmlManipulatorService.runDemo());
    }
}
