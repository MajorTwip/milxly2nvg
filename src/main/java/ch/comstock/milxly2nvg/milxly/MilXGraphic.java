
package ch.comstock.milxly2nvg.milxly;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "MilXGraphic")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class MilXGraphic {
	@XmlElement
	private String MssStringXML;
	@XmlElement
	private PointList PointList;
	
	public MSSSymbol getMSSSymbol() {
		if (MssStringXML == null || MssStringXML.isEmpty()) {
			return null;
		}
		return new MSSSymbol(MssStringXML);
	}
}


