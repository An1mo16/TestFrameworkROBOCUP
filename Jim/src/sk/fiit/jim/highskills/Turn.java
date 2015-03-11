package sk.fiit.jim.highskills;

import sk.fiit.jim.LambdaCallable;
import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.jim.agent.communication.Communication;
import sk.fiit.jim.agent.models.AgentModel;
import sk.fiit.jim.agent.moves.LowSkill;
import sk.fiit.jim.agent.moves.LowSkills;
import sk.fiit.jim.agent.skills.HighSkill;
import sk.fiit.robocup.library.geometry.Angles;
import sk.fiit.robocup.library.geometry.Vector3D;

/**
 *
 * Reimplemented from Ruby to Java by roman moravcik
 */
public class Turn extends HighSkill {

    private Angles rightRange = new Angles(180.0, 360.0);
    private Angles leftRange = new Angles(0.0, 180.0);
    private Angles inPlaceRange1 = new Angles(0.0, 15.0);
    private Angles inPlaceRange2 = new Angles(345.0, 360.0);
    Vector3D target;
    private LambdaCallable validityProc;
    private boolean ending;

    public Turn(Vector3D target) {
    	this.target = target;
    	this.validityProc =  new LambdaCallable() {
			public boolean call() {
				return true;
			}
		};
		this.ending = false;
    }
    
    public Turn(Vector3D target,LambdaCallable validityProc) {
        this.target = target;
        this.validityProc = validityProc;
        this.ending = false;
    }

    @Override
    public LowSkill pickLowSkill() {
         boolean isStillValid = validityProc.call();
          if(!isStillValid ){
        	  this.ending = true;
          }
          if (!isStillValid || this.ending){
        	  return null;        	  
          }
    			        	
        AgentModel agentModel = AgentModel.getInstance();
        // FIXME: nemalo by byt asi tu kedze ide ako vstupny parameter do konstruktora.
        target = AgentInfo.getInstance().kickTarget();
        double normalizedAngle = Angles.normalize(target.getPhi());

        // On ground, high skill ends
        if (agentModel.isOnGround() || agentModel.isLyingOnBack() || agentModel.isLyingOnBelly()) {
            return null;
        } // Considered straight to targer, high skill ends
        else if (inPlaceRange1.include(normalizedAngle)) {
            return null;
        } // Considered straight to targer, high skill ends
        else if (inPlaceRange2.include(normalizedAngle)) {
            return null;
        } else if (leftRange.include(normalizedAngle)) {
            return LowSkills.get("test_turn_left");
        } else if (rightRange.include(normalizedAngle)) {
            return LowSkills.get("test_turn_right");
        } else {
            return LowSkills.get("test_turn_right");
        }
    }

    @Override
    public void checkProgress() {
    }
}
