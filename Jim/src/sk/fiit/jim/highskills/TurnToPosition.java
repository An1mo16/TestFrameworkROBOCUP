package sk.fiit.jim.highskills;

import sk.fiit.jim.LambdaCallable;
import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.jim.agent.models.AgentModel;
import sk.fiit.jim.agent.moves.LowSkill;
import sk.fiit.jim.agent.moves.LowSkills;
import sk.fiit.jim.agent.skills.HighSkill;
import sk.fiit.robocup.library.geometry.Angles;
import sk.fiit.robocup.library.geometry.Vector3D;
/*
 * 
 * Reimplemented from Ruby to Java by Michal Blanarik
 */
public class TurnToPosition extends HighSkill{

	private Angles rightRange = new Angles(180.0, 360.0);	//Math::PI...(Math::PI*2.0)
	private Angles leftRange = new Angles(0.0, 180.0);		//0...Math::PI
	private Angles inPlaceRange1 = new Angles(0.0, 15.0);	//0...(Math::PI*0.1)
	private Angles inPlaceRange2 = new Angles(345.0, 360.0);//(Math::PI*1.9)...(Math::PI*2.0)
	private boolean ending = false;
	
	private LambdaCallable validityProc;
	
	Vector3D target;
	
	public TurnToPosition(Vector3D target, LambdaCallable validityProc ) {
        this.target = target;
        this.validityProc = validityProc;
    }
	
	@Override
	public LowSkill pickLowSkill() {
		boolean isStillValid = this.validityProc.call();
		if(!isStillValid){
			this.ending = true;
		}
		if(!isStillValid || this.ending){
			return null;
		}
				
		
		AgentModel agentModel = AgentModel.getInstance();
		
		Vector3D me_flattened = agentModel.getPosition().setZ(0.0); 
		Vector3D target_vector = target.subtract(me_flattened);
		Double target_angle = target_vector.getPhi();
		Double diff_against_current = Angles.normalize(target_angle - agentModel.getRotationZ());
		
		Double target_phi = diff_against_current; 
		
		// On ground, high skill ends
        if (agentModel.isOnGround() || agentModel.isLyingOnBack() || agentModel.isLyingOnBelly()) {
            return null;
        } // Considered straight to target, high skill ends
        else if (inPlaceRange1.include(Angles.normalize(target_phi))) {
            return null;
        } // Considered straight to target, high skill ends
        else if (inPlaceRange2.include(Angles.normalize(target_phi))) {
            return null;
        } else if (leftRange.include(Angles.normalize(target_phi))) {
            return LowSkills.get("test_turn_left");
        } else if (rightRange.include(Angles.normalize(target_phi))) {
            return LowSkills.get("test_turn_right");
        } else {
            return LowSkills.get("test_turn_right");
        }
	}

	@Override
	public void checkProgress() {
	}

}
