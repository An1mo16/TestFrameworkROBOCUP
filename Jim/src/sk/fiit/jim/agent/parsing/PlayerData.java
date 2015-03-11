/**
 * 
 */
package sk.fiit.jim.agent.parsing;

import java.util.LinkedHashMap;
import java.util.Map;

import sk.fiit.robocup.library.geometry.Vector3D;

/**
 * 
 *  Player.java
 *  
 *  Encapsulate data from see perceptor about player.
 *  
 *@Title        Jim
 *@author       Author: Ondrej Jur��k
 */
public class PlayerData {
	
	public static class PlayerPartsNames
	{
		public static final String head = "head";
		public static final String rlowerarm = "rlowerarm";
		public static final String llowerarm = "llowerarm";
		public static final String rfoot = "rfoot";
		public static final String lfoot = "lfoot";
	}
	
	/**
	 * Player's team.
	 */
	public String team;
	/**
	 * Player's id.
	 */
	public String id;
	/**
	 * Map of body parts of player.
	 */
	public Map<String, Vector3D> bodyParts = new LinkedHashMap<String, Vector3D>();
}
