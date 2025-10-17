package ch.comstock.milxly2nvg;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.comstock.milxly2nvg.milxly.MilXGraphic;
import ch.comstock.milxly2nvg.nvg.ContentType;
import ch.comstock.milxly2nvg.nvg.ObjectFactory;
import ch.comstock.milxly2nvg.nvg.PointType;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

public class Converter {

	private static Logger log = LoggerFactory.getLogger(Converter.class);

	/**
	 * Convert MILXLY file to NVG format
	 * 
	 * @param inputFile  Path to the input MILXLY file
	 * @param outputFile Optional path to the output NVG file
	 * @throws JAXBException
	 * @throws XMLStreamException
	 * @throws FileNotFoundException
	 */
	public static void convert(Path inputFile, Path outputFile) throws JAXBException {
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

			log.debug("Reader initialized, creating marshallers");

			// Create JAXB context and unmarshaller for MilXGraphic. Grpahic elements will
			// be unmarshalled separately
			JAXBContext milXGraphicContext = JAXBContext.newInstance(MilXGraphic.class);
			JAXBContext nvgContext = JAXBContext.newInstance(ContentType.class);
			Unmarshaller milXGraphicUnmarshaller = milXGraphicContext.createUnmarshaller();

			// Create XML event reader to parse the XML
			XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
			XMLEventReader eventReader = xmlInputFactory.createXMLEventReader(reader);

			// Prapare the writer for output NVG file
			ObjectFactory nvgObjectFactory = new ObjectFactory();
			FileOutputStream writer = new FileOutputStream(outputFile.toFile());
	        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newFactory();
            XMLEventWriter eventWriter = xmlOutputFactory.createXMLEventWriter(writer);
            XMLEventFactory  eventFactory = XMLEventFactory.newInstance();
            Marshaller marshaller = nvgContext.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            eventWriter.add(eventFactory.createStartDocument("UTF-8", "1.0"));
            eventWriter.add(eventFactory.createStartElement("", "https://tide.act.nato.int/schemas/2012/10/nvg", "nvg"));
            eventWriter.add(eventFactory.createNamespace("", "https://tide.act.nato.int/schemas/2012/10/nvg"));
            eventWriter.add(eventFactory.createNamespace("g", "https://tide.assct.nato.int/schemas/2012/10/nvg"));
            eventWriter.add(eventFactory.createNamespace("nt", "http://purl.org/dc/terms/"));
            eventWriter.add(eventFactory.createAttribute("version", "2.0.2"));
            
            
            
			while (eventReader.hasNext()) {
				if (eventReader.peek().isStartElement()) {
					String elementName = eventReader.peek().asStartElement().getName().getLocalPart();
					try {
						switch (elementName) {
						case "MilXGraphic":
							//log.info("Unmarshalling MilXGraphic element: {}", eventReader.peek().asStartElement().getName().getLocalPart());
							JAXBElement<MilXGraphic> milXGraphicElementWrapper = milXGraphicUnmarshaller
									.unmarshal(eventReader, MilXGraphic.class);
							MilXGraphic milXGraphicElement = milXGraphicElementWrapper.getValue();
							PointType nvgPoint = nvgObjectFactory.createPointType();
							System.out.println("");
							nvgPoint.setX(milXGraphicElement.getPointList().getPoint().get(0).getX());
							nvgPoint.setY(milXGraphicElement.getPointList().getPoint().get(0).getY());
							marshaller.marshal(new JAXBElement<PointType>(new QName("","point"), PointType.class, nvgPoint), eventWriter);
							
							break;
						default:
							break;
						}
					} catch (JAXBException e) {
						log.warn("JAXB Exception during unmarshalling", e.getMessage());
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				eventReader.nextEvent();
			}
			eventWriter.add(eventFactory.createEndElement("", "https://tide.act.nato.int/schemas/2012/10/nvg", "nvg"));
			eventWriter.add(eventFactory.createEndDocument());
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
	 * @throws JAXBException
	 */
	public static void convert(Path inputFile) throws JAXBException {
		convert(inputFile, inputFile.resolveSibling(inputFile.getFileName().toString() + ".nvg"));
	}
}
