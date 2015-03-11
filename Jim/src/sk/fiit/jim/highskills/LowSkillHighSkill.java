package sk.fiit.jim.highskills;

import sk.fiit.jim.agent.moves.LowSkill;
import sk.fiit.jim.agent.moves.LowSkills;
import sk.fiit.jim.agent.skills.HighSkill;

/**
*
* Reimplemented from Ruby to Java by Stefan Linner
*/
public class LowSkillHighSkill extends HighSkill{
	private LowSkill skill;
	private boolean runAlready = false;

	public LowSkillHighSkill(String skillName){
		skill = LowSkills.get(skillName);
	}
	
	@Override
	public LowSkill pickLowSkill() {
		if (!runAlready){
			runAlready = true;
			return skill;
		} else {
			return null;
		}
	}

	@Override
	public void checkProgress() throws Exception {
		
	}

}
