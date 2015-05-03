package sk.fiit.testframework.gp.desiciontreegp;

import javax.xml.parsers.*;

import org.w3c.dom.Document;

import sk.fiit.jim.Settings;

/**
 * 
 * @author Július Skrisa
 *
 */
public class XmlLoader {
		public static Document Load(String file){
			//get the factory
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setIgnoringElementContentWhitespace(true);
			try {

				DocumentBuilder db = dbf.newDocumentBuilder();
				
				Document dom = db.parse(Settings.getString("desicion_file_path") + file);

				return dom;
				
			}catch(Exception pce) {
				pce.printStackTrace();
			}
			return null;
		}
		
		
}
