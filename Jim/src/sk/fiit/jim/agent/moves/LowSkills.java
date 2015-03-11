package sk.fiit.jim.agent.moves;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import sk.fiit.jim.log.Log;
import sk.fiit.jim.log.LogType;

/**
 *  Manages low skills loaded from XML files in the moves directory.
 *  Each low skill only has one instance that is reused every time that this low
 *  skill is used.
 *
 *@Title	Jim
 *@author	marosurbanec
 */
public final class LowSkills{
	private LowSkills(){}
	static private Map<String, LowSkill> skills = new HashMap<String, LowSkill>();
	
	/**
	 * Adds specified LowSkill to the HashMap. 
	 *
	 * @param skill
	 */
	public static void addSkill(LowSkill skill){
		if (skill == null) return;
		skills.put(skill.name, skill);
	}
	
	/**
	 * Returns true if LowSkill with specified name exists in HashMap. 
	 *
	 * @param name
	 * @return
	 */
	public static boolean exists(String name){
		return skills.containsKey(name);
	}
	
	/**
	 * Returns LowSkill by specified name. 
	 *
	 * @param name
	 * @return
	 */
	public static LowSkill get(String name){
		Log.debug(LogType.LOW_SKILL, "Chosen skill:" + name );
		return skills.get(name);
	}
	
	/**
	 * Resets active phase of every LowSkill in HashMap to null. 
	 *
	 */
	public static void reset(){
		for (LowSkill skill : skills.values())
			skill.activePhase = null;
	}
	
	/**
	 * Returns Collection of all LowSkills from HashMap. 
	 *
	 * @return
	 */
	public static Collection<LowSkill> getAll() { return skills.values(); }
}