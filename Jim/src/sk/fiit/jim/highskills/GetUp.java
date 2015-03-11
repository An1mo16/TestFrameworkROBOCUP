package sk.fiit.jim.highskills;

import sk.fiit.jim.LambdaCallable;
import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.jim.agent.models.AgentModel;
import sk.fiit.jim.agent.models.WorldModel;
import sk.fiit.jim.agent.moves.LowSkill;
import sk.fiit.jim.agent.moves.LowSkills;
import sk.fiit.jim.agent.skills.HighSkill;
import sk.fiit.robocup.library.geometry.Vector3D;

/**
 *
 * Reimplemented from Ruby to Java by roman moravcik
 */

public class GetUp extends HighSkill {
	private LambdaCallable validityProc;

    public GetUp(LambdaCallable validityProc){
    	this.validityProc = validityProc;
    }
	
    @Override
    public LowSkill pickLowSkill() {
        AgentModel agentModel = AgentModel.getInstance();
      
        if (agentModel.isLyingOnBack()) {
            return LowSkills.get("stand_back");
        }
        else if (agentModel.isLyingOnBelly()) {
            return LowSkills.get("stand_front");
        }
        else if (!agentModel.isOnGround()) {
            return null;
        }
        else {
            return LowSkills.get("stand_back");
        }
    }
    
    @Override
    public void checkProgress() {
        
    }
}
