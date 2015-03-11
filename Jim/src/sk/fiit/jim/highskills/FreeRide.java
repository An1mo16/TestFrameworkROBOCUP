package sk.fiit.jim.highskills;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.jim.agent.moves.LowSkill;
import sk.fiit.jim.agent.moves.LowSkills;
import sk.fiit.jim.agent.skills.HighSkill;

public class FreeRide extends HighSkill {

	private String name = "FreeRide";

	private static String lowSkills[] = { "ruky", "hlava", "hlava", "hlavaaruky", "hlavaaruky", "hlavaaruky", "hlavaaruky", "drepy", "drepy",
			"drepy", "drepy", "drepy", "drepyII", "drepyII", "drepyII", "drepyII", "uvidime", "stand_back", "uvidime" };

	private Iterator<String> it = Arrays.asList(lowSkills).iterator();

	@Override
	public LowSkill pickLowSkill() {
		
		String skill = "";
		while (it.hasNext()) {
			skill = it.next();
			break;
		}
		if(it.hasNext() == false){
			Arrays.asList(lowSkills).iterator();
		}
		
		AgentInfo.logState(this.name + " " + skill);
		return LowSkills.get(skill);
	}

	@Override
	public void checkProgress() throws Exception {
		// TODO Auto-generated method stub

	}
}
