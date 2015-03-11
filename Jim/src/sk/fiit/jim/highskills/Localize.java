package sk.fiit.jim.highskills;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import org.jruby.javasupport.Java;

import sk.fiit.jim.LambdaCallable;
import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.jim.agent.models.WorldModel;
import sk.fiit.jim.agent.moves.LowSkill;
import sk.fiit.jim.agent.skills.HighSkill;
import sk.fiit.robocup.library.geometry.Vector3D;
import sk.fiit.jim.agent.models.AgentModel;
import sk.fiit.jim.agent.models.DynamicObject;
import sk.fiit.jim.agent.models.EnvironmentModel;
import sk.fiit.jim.agent.moves.LowSkills;
import sk.fiit.robocup.library.geometry.Angles;

/**
 *
 * Reimplemented from Ruby to Java by roman moravcik
 */
public class Localize extends HighSkill {

    private Angles right = new Angles(180.0, 360.0);
    private Angles left = new Angles(0.0, 180.0);
    private Angles front1 = new Angles(0.0, 90.0);
    private Angles front2 = new Angles(270.0, 360.0);
    private Angles back = new Angles(90.0, 270.0);
    
    private boolean leftLook = false;
    private boolean rightLook = false;
    private LambdaCallable validityProc;

    public Localize(LambdaCallable validityProc){
    	this.validityProc = validityProc;
    }
    
    public void checkProgress() {
    }

    public LowSkill pickLowSkill() {
        DynamicObject ball = WorldModel.getInstance().getBall();
        double ballAngle = Angles.normalize(ball.getRelativePosition().getPhi());
        AgentModel agentModel = AgentModel.getInstance();

        if (validityProc.call() ) {
        	return null;
        }
        else if (EnvironmentModel.beamablePlayMode()) {
            return null;
        } 
        else if (agentModel.falled()) {
            return null;
        }
        else if (!leftLook) {
            leftLook = true;
            return LowSkills.get("head_left_120");
        }
        else if (!rightLook) {
            rightLook = true;
            return LowSkills.get("head_right_120");
        }
        else if (left.include(ballAngle) && (front1.include(ballAngle) || front2.include(ballAngle))) {
            // Vlavo vpredu
            return LowSkills.get("turn_left_cont_20");
        } 
        else if (right.include(ballAngle) && (front1.include(ballAngle) || front2.include(ballAngle))) {
            // Vpravo vpredu
            return LowSkills.get("turn_right_cont_20");
        }
        else if (left.include(ballAngle) && back.include(ballAngle)) {
            // Vlavo vzadu
            return LowSkills.get("turn_left_90");
        }
        else if (right.include(ballAngle) && back.include(ballAngle)) {
            // Vpravo vzadu
            return LowSkills.get("turn_right_90");
        }

        return LowSkills.get("turn_right_90");
    }
}