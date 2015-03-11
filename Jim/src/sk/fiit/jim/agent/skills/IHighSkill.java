/**
 * 
 */
package sk.fiit.jim.agent.skills;

import sk.fiit.jim.agent.moves.LowSkill;

/**
 * 
 *  The methods, which cannot be forgotten when implementing some High Skill.
 *  
 *@Title        Jim
 *@author       $Author: Bimbo $
 */
public interface IHighSkill {

	public LowSkill pickLowSkill();
	public void checkProgress() throws Exception;
	
}
