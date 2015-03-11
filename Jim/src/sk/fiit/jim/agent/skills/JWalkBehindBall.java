/**
 * 
 */
package sk.fiit.jim.agent.skills;

import sk.fiit.jim.Settings;
import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.jim.agent.models.WorldModel;
import sk.fiit.robocup.library.init.Script;

/**
 * 
 *  WalkBehindBall.java
 *  
 *@Title        Jim
 *@author       $Author: Bimbo $
 */
public class JWalkBehindBall extends ComplexHighSkill{
	
	@Override
	public HighSkill pickHighSkill() {
		if(WorldModel.getInstance().getBall().notSeenLongTime()>5){
		}
		return null;
	}

}
