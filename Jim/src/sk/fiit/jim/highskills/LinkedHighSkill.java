/**
 * 
 */
package sk.fiit.jim.highskills;

import sk.fiit.jim.agent.moves.LowSkill;
import sk.fiit.jim.agent.moves.LowSkills;
import sk.fiit.jim.agent.skills.HighSkill;
import sk.fiit.jim.agent.AgentInfo;

/**
*
* Reimplemented from Ruby to Java by Stefan Linner
*/
public class LinkedHighSkill extends HighSkill{

	private int currentSkill = 0;
	private LowSkill[] skills = {LowSkills.get("ruky"), 
								LowSkills.get("hlava"),
								LowSkills.get("hlava"),
								LowSkills.get("hlavaaruky"),};
	
	@Override
	public LowSkill pickLowSkill() {
		if (currentSkill < skills.length){
			AgentInfo.getInstance().loguj("picking skill " + skills[currentSkill].name);
			return skills[currentSkill++];
		} else {
			return null;
		}
	}

	@Override
	public void checkProgress() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
