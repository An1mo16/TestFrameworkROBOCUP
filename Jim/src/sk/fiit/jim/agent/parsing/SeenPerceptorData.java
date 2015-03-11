/**
 * 
 */
package sk.fiit.jim.agent.parsing;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import sk.fiit.jim.agent.models.FixedObject;
import sk.fiit.robocup.library.geometry.Vector3D;

/**
 * 
 *  SeenPerceptorData.java
 *  Encapsulate data from see perceptora
 *  
 *@Title        Jim
 *@author       Author: Ondrej Jur??k
 */
public class SeenPerceptorData{
	
	public Vector3D ball;
	public Map<FixedObject, Vector3D> fixedObjects = new LinkedHashMap<FixedObject, Vector3D>();
	public List<PlayerData> players = new ArrayList<PlayerData>();
}
