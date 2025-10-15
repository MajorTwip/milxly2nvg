package ch.comstock.milxly2nvg;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Converter {

	private static Logger log = LoggerFactory.getLogger(Converter.class);
	/**
	 * Convert MILXLY file to NVG format
	 * 
	 * @param inputFile  Path to the input MILXLY file
	 * @param outputFile Optional path to the output NVG file
	 * @throws XMLStreamException 
	 * @throws FileNotFoundException 
	 */
	public static void convert(Path inputFile, Path outputFile){
		// Placeholder for conversion logic
		log.info("Converting MILXLY file: {} to NVG file: {}", inputFile, outputFile);
		// Handle BOM if present
	    try (InputStream is = new FileInputStream(inputFile.toFile())) {
	        byte[] bom = new byte[3];
	        int n = is.read(bom, 0, bom.length);

	        Reader reader;
	        if (n == 3 && (bom[0] & 0xFF) == 0xEF && (bom[1] & 0xFF) == 0xBB && (bom[2] & 0xFF) == 0xBF) {
	            reader = new InputStreamReader(is, StandardCharsets.UTF_8);
	        } else {
	            reader = new InputStreamReader(new FileInputStream(inputFile.toFile()), StandardCharsets.UTF_8);
	        }
			XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
			XMLEventReader eventReader = xmlInputFactory.createXMLEventReader(reader);
			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();
				if (event.isStartElement()) {
					log.info("Start Element: {}", event.asStartElement().getName().getLocalPart());
				} 
			}	
		} catch (XMLStreamException e) {
			log.error("XML Stream Exception during conversion", e.getMessage());
			e.printStackTrace();
		} catch (IOException e1) {
			log.error("IO Exception during conversion", e1.getMessage());
			e1.printStackTrace();
		}
	}

	/**
	 * Convert MILXLY file to NVG format with default output file name
	 * 
	 * @param inputFile Path to the input MILXLY file
	 */
	public static void convert(Path inputFile) {
		convert(inputFile, inputFile.resolveSibling(inputFile.getFileName().toString() + ".nvg"));
	}
}
