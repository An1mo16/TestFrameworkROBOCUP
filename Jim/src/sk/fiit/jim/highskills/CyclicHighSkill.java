/**
 * 
 */
package sk.fiit.jim.highskills;

import sk.fiit.jim.agent.moves.LowSkill;
import sk.fiit.jim.agent.moves.LowSkills;
import sk.fiit.jim.agent.skills.HighSkill;

/**
*
* Reimplemented from Ruby to Java by Stefan Linner
*/
public class CyclicHighSkill extends HighSkill{

	private LowSkill skill;
	
	public CyclicHighSkill(String skillName){
		skill = LowSkills.get(skillName);
	}
	
	@Override
	public LowSkill pickLowSkill() {
		return skill;
	}

	@Override
	public void checkProgress() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
