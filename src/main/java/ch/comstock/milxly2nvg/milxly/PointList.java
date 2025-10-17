package ch.comstock.milxly2nvg.milxly;

import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement
public class PointList {

	@XmlElement
	private List<Point> Point;
}
