package com.milxly2nvg.xmlmanipulator;

import com.milxly2nvg.xmlmanipulator.service.XmlManipulatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Console runner that executes XML manipulation operations
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class XmlConsoleRunner implements CommandLineRunner {

    private final XmlManipulatorService xmlManipulatorService;

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting XML Manipulator Console Application");
        
        if (args.length == 0) {
            printUsage();
            return;
        }

        String command = args[0].toLowerCase();
        
        try {
            switch (command) {
                case "read":
                    if (args.length < 2) {
                        log.error("Please provide the XML file path");
                        return;
                    }
                    xmlManipulatorService.readXml(args[1]);
                    break;
                    
                case "validate":
                    if (args.length < 2) {
                        log.error("Please provide the XML file path");
                        return;
                    }
                    xmlManipulatorService.validateXml(args[1]);
                    break;
                    
                case "transform":
                    if (args.length < 3) {
                        log.error("Please provide input and output file paths");
                        return;
                    }
                    xmlManipulatorService.transformXml(args[1], args[2]);
                    break;
                    
                case "prettify":
                    if (args.length < 2) {
                        log.error("Please provide the XML file path");
                        return;
                    }
                    String outputPath = args.length >= 3 ? args[2] : null;
                    xmlManipulatorService.prettifyXml(args[1], outputPath);
                    break;
                    
                case "extract":
                    if (args.length < 3) {
                        log.error("Please provide file path and XPath expression");
                        return;
                    }
                    xmlManipulatorService.extractByXPath(args[1], args[2]);
                    break;
                    
                case "demo":
                    log.info("Running demonstration of XML manipulation capabilities");
                    xmlManipulatorService.runDemo();
                    break;
                    
                default:
                    log.error("Unknown command: {}", command);
                    printUsage();
            }
        } catch (Exception e) {
            log.error("Error executing command: {}", e.getMessage(), e);
        }
        
        log.info("XML Manipulator Console Application finished");
    }

    private void printUsage() {
        log.info("\n=== XML Manipulator Console Application ===\n");
        log.info("Usage: java -jar xml-manipulator.jar <command> [options]\n");
        log.info("Commands:");
        log.info("  read <file>              - Read and display XML file content");
        log.info("  validate <file>          - Validate XML file structure");
        log.info("  transform <input> <output> - Transform XML file");
        log.info("  prettify <file> [output] - Format XML file with proper indentation");
        log.info("  extract <file> <xpath>   - Extract data using XPath expression");
        log.info("  demo                     - Run demonstration of features");
        log.info("\nExamples:");
        log.info("  java -jar xml-manipulator.jar read data/sample.xml");
        log.info("  java -jar xml-manipulator.jar prettify data/sample.xml data/pretty.xml");
        log.info("  java -jar xml-manipulator.jar extract data/sample.xml \"//book[@id='1']\"");
    }
}
