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
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.comstock.milxly2nvg.milxly.MilXGraphic;
import ch.comstock.milxly2nvg.nvg.ContentType;
import ch.comstock.milxly2nvg.nvg.NvgType;
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

			log.debug("Reader initialized, creating unmarshallers");

			// Create JAXB context and unmarshaller for MilXGraphic. Grpahic elements will
			// be unmarshalled sequentially
			JAXBContext milXGraphicContext = JAXBContext.newInstance(MilXGraphic.class);
			Unmarshaller milXGraphicUnmarshaller = milXGraphicContext.createUnmarshaller();

			// Create XML event reader to parse the XML
			XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
			XMLEventReader eventReader = xmlInputFactory.createXMLEventReader(reader);

			// Create JAXB context and marshaller for NVG output
			JAXBContext nvgContext = JAXBContext.newInstance(ContentType.class);
			ObjectFactory nvgObjectFactory = new ObjectFactory();
			FileOutputStream writer = new FileOutputStream(outputFile.toFile());
            Marshaller marshaller = nvgContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            
            //initialize root element for nvg
            NvgType nvgRoot = nvgObjectFactory.createNvgType();
            nvgRoot.setVersion("2.0.2");
            List<ContentType> nvgContent = nvgRoot.getGOrCompositeOrText();
            
			while (eventReader.hasNext()) {
				if (eventReader.peek().isStartElement()) {
					String elementName = eventReader.peek().asStartElement().getName().getLocalPart();
					try {
						switch (elementName) {
						case "MilXGraphic":
							log.info("Processing MilXGraphic element");
							// Unmarshal MilXGraphic element
							JAXBElement<MilXGraphic> milXGraphicElementWrapper = milXGraphicUnmarshaller
									.unmarshal(eventReader, MilXGraphic.class);
							MilXGraphic milXGraphicElement = milXGraphicElementWrapper.getValue();
							// Convert to NVG PointType
							PointType nvgPoint = nvgObjectFactory.createPointType();
							nvgPoint.setX(milXGraphicElement.getPointList().getPoint().get(0).getX());
							nvgPoint.setY(milXGraphicElement.getPointList().getPoint().get(0).getY());
							// Add to NVG content list
							nvgContent.add(nvgPoint);								
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
			// Marshal NVG content to output file
			marshaller.marshal(nvgObjectFactory.createNvg(nvgRoot), writer);
			writer.close();
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
