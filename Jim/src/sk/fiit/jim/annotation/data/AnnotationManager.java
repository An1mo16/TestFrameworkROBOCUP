package sk.fiit.jim.annotation.data;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import sk.fiit.jim.log.Log;
import sk.fiit.jim.log.LogType;

/**
 * 
 *  AnnotationManager.java
 *  
 *  Handles the loading of annotations and their relations to moves (skills)
 *  
 *@Title        Jim
 *@author       peterholak
 */
public class AnnotationManager {
	//mapuje nazov pohybu na zoznam anotacii pre tento pohyb
	private Map<String, List<Annotation>> annotations;
	//mapuje nazov anotacie na jej objekt
	private Map<String, Annotation> annotationIds;
	private List<Annotation> allAnnotations;
	private static AnnotationManager instance = new AnnotationManager();
	
	private AnnotationManager() {
		annotationIds = new HashMap<String, Annotation>();
		annotations = new HashMap<String, List<Annotation>>();
		allAnnotations = new LinkedList<Annotation>(); 
	}
	
	
	
	/**
	 * Returns list of all annotations. 
	 *
	 * @return
	 */
	public List<Annotation>getAllAnnotations() {
		return this.allAnnotations;
	}


	/**
	 * Returns AnnotationManager instance to work with it. 
	 *
	 * @return
	 */
	public static AnnotationManager getInstance() {
		return instance;
	}
	
	/**
	 * Loads annotations from specified directory.  
	 *
	 * @param directory
	 * @throws IOException
	 */
	public void loadAnnotations(String directory) throws IOException {
		Log.log(LogType.INIT, "Loading annotations from " + directory);
		File directoryWithXmls = new File(directory);
		if (!directoryWithXmls.isDirectory())
			throw new IOException("Cannot open moves/annotations directory");
		
		File[] xmlFiles = directoryWithXmls.listFiles(new FilenameFilter(){
			public boolean accept(File dir, String name){
				return name.matches("^[a-zA-Z_0-9]{1,}[-]{1}[0-9]{1,}\\.xml$");
			}
		});
		
		for (File f : xmlFiles) {
			Log.log(LogType.INIT, "Loading annotation "+f.getName());

			try {
				Annotation a = XMLParser.parse(f);
				annotationIds.put(a.getId(), a);
				allAnnotations.add(a);
				
				List<Annotation> moveAnnotList;
				if (annotations.containsKey(a.getName())) {
					moveAnnotList = annotations.get(a.getName());
				}else{
					moveAnnotList = new LinkedList<Annotation>();
					annotations.put(a.getName(), moveAnnotList);
				}
				Log.log(LogType.INIT, "Adding annotation "+a.getId()+" to move "+a.getName());
				moveAnnotList.add(a);
			}catch (Exception e) {
				Log.error(LogType.INIT, "Cannot load annotation from " + f.getName());
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Returns annotation by specified string. 
	 *
	 * @param move
	 * @return
	 */
	public List<Annotation> getAnnotations(String move) {
		return annotations.get(move);
	}
	
	/**
	 * Returns annotation by specified id. 
	 *
	 * @param id
	 * @return
	 */
	public Annotation getAnnotationById(String id) {
		return annotationIds.get(id);
	}
}
