package ch.comstock.milxly2nvg.milxly;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;

import org.slf4j.Logger;
import lombok.Data;

@Data
public class MSSSymbol {
	private String code2525B;
	private Map<String, String> attributes = new HashMap<>();
	Logger log = org.slf4j.LoggerFactory.getLogger(MSSSymbol.class);

	public MSSSymbol(String mssStringXML) {
		// Crudely unencode XML entities
		String mmsStringUnencoded = mssStringXML.replace("&lt;", "<").replace("&gt;", ">").replace("&amp;", "&");
		try {
			XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
			XMLEventReader eventReader = xmlInputFactory.createXMLEventReader(new StringReader(mmsStringUnencoded));
			while (eventReader.hasNext()) {
				var event = eventReader.nextEvent();
				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();
					String elementName = startElement.getName().getLocalPart();
					if (elementName.equals("Symbol")) {
						Attribute idAttr = startElement.getAttributeByName(javax.xml.namespace.QName.valueOf("ID"));
						if (idAttr != null) {
							this.code2525B = idAttr.getValue();
						}
					} else if (elementName.equals("Attribute")) {
						// Read other attributes
						Attribute attr = startElement.getAttributeByName(javax.xml.namespace.QName.valueOf("ID"));
						String value = eventReader.getElementText();
						if (attr != null) {
							attributes.put(attr.getValue(), value);
						}
					} else {
						log.warn("Unknown element in MSSSymbol: {}", elementName);
					}
				}
			}
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getAttributesString() {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : attributes.entrySet()) {
			if (sb.length() > 0) {
				sb.append(";");
			}
			sb.append(entry.getKey()).append(":").append(entry.getValue());
		}
		return sb.toString();
	}
}
