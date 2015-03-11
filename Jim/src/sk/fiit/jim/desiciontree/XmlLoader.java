package sk.fiit.jim.desiciontree;

import javax.xml.parsers.*;

import org.w3c.dom.Document;

public class XmlLoader {
		public static Document Load(String file){
			//get the factory
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setIgnoringElementContentWhitespace(true);
			try {

				DocumentBuilder db = dbf.newDocumentBuilder();
				
				Document dom = db.parse(System.getProperty("user.dir") + "/dtHighskills/" + file);

				return dom;
				
			}catch(Exception pce) {
				pce.printStackTrace();
			}
			return null;
		}
		
		
}
