package ch.comstock.milxly2nvg.milxly;

import org.yaml.snakeyaml.external.com.google.gdata.util.common.base.UnicodeEscaper;

import lombok.Data;

@Data
public class MSSSymbol {
	private String code2525B;

	public MSSSymbol(String mssStringXML) {
		String mmsStringUnencoded = mssStringXML.replace("&lt;", "<").replace("&gt;", ">").replace("&amp;", "&");
		// Temporary solution
		this.code2525B = mmsStringUnencoded.replace("<Symbol ID=\"", "").replace("\"/>", "");
	}

}
