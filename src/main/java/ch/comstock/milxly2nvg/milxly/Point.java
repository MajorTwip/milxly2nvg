package ch.comstock.milxly2nvg.milxly;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "Point")
public class Point {
	@XmlElement
	private double X=1;

	@XmlElement
	private double Y;
}
