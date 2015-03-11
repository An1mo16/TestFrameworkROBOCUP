package sk.fiit.jim.highskills;

import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.jim.agent.models.AgentModel;
import sk.fiit.jim.agent.models.EnvironmentModel;
import sk.fiit.jim.agent.models.WorldModel;
import sk.fiit.jim.agent.moves.LowSkill;
import sk.fiit.jim.agent.moves.LowSkills;
import sk.fiit.jim.agent.skills.HighSkill;
import sk.fiit.robocup.library.geometry.Vector3D;

/**
 *
 * Reimplemented from Ruby to Java by roman moravcik
 */
public class Kick extends HighSkill {

    private Vector3D kickTarget;

    public Kick(Vector3D kickTarget) {
        this.kickTarget = kickTarget;
    }

    @Override
    public LowSkill pickLowSkill() {
        AgentInfo agentInfo = AgentInfo.getInstance();
        AgentModel agentModel = AgentModel.getInstance();
       
        Vector3D ballPosition = agentInfo.ballControlPosition();

        if (EnvironmentModel.beamablePlayMode() && !EnvironmentModel.isKickOffPlayMode()) {
            agentInfo.loguj("beam");
            return null;
        } else if (agentModel.falled()) {
            agentInfo.loguj("fall");
            return null;
        } else if (ballUnseen() > 3) {
            agentInfo.loguj("ball unseen");
            return null;
        } else if (Math.abs(ballPosition.getY()) > 0.7) {
            agentInfo.loguj("very big y");
            return null;
        } else if (Math.abs(ballPosition.getX()) > 0.7) {
            agentInfo.loguj("very big y");
            return null;
        } else if (ballPosition.getY() < 0.7 && ballPosition.getY() > 0.25) {
            agentInfo.loguj("go a bit");
            return LowSkills.get("walk_slow2");
        } else if (ballPosition.getX() > 0.1 && ballPosition.getX() < 0.7) {
            agentInfo.loguj("big x");
            return LowSkills.get("step_right");
        } else if (ballPosition.getX() < -0.1 && ballPosition.getX() > -0.7) {
            agentInfo.loguj("small x");
            return LowSkills.get("step_left");
        } else if (ballPosition.getX() > 0.0) {
            agentInfo.loguj("kick right");
            return getKick("right");
        } else if (ballPosition.getX() < 0.0) {
            agentInfo.loguj("kick left");
            return getKick("left");
        } else {
            agentInfo.calculateDistance(ballPosition, ballPosition);
            agentInfo.loguj("unknown kick distance");
            return null;
        }
    }

    private LowSkill getKick(String leg) {
        
        // FIXME: calculateDistance by mala byt static z Vector3D.
        // FIXME: druhy parameter je len odhad kedze v skripte ta premmenna ani nebola inicializovana.
        Vector3D ballPosition = WorldModel.getInstance().getBall().getPosition();
        double kickDistance = AgentInfo.getInstance().calculateDistance(ballPosition, kickTarget);

        if (kickDistance > 4 && leg == "right") {
            System.out.println("faster");
            return LowSkills.get("kick_step_strong_right");
            // return LowSkills.get("kick_right_faster");
        } 
        else if (kickDistance > 4 && leg == "left") {
            System.out.println("faster");
            return LowSkills.get("kick_step_strong_left");
            // return LowSkills.get("kick_left_faster");
        } 
        else if (kickDistance <= 4 && kickDistance > 1.5 && leg == "right") {
            System.out.println("fast");
            return LowSkills.get("kick_right_faster");
            // return LowSkills.get("kick_right_normal");
        } 
        else if (kickDistance <= 2.5 && kickDistance > 1.5 && leg == "left") {
            System.out.println("fast");
            return LowSkills.get("kick_left_faster");
            // return LowSkills.get("kick_left_normal");
        } 
        else if (kickDistance <= 1.5 && kickDistance > 0.5 && leg == "right") {
            System.out.println("normal");
            return LowSkills.get("kick_right_normal");
            // return LowSkills.get("kick_right_slow");
        } 
        else if (kickDistance <= 1.5 && kickDistance > 0.5 && leg == "left") {
            System.out.println("normal");
            return LowSkills.get("kick_left_normal");
            // return LowSkills.get("kick_left_slow");
        } 
        else {
            return null;
        }
    }

    @Override
    public void checkProgress() {
    }

    // FIXME: to iste je aj v Plan.java, treba dat do dakych spolocnych konstant.
    public static double ballUnseen() {
        return EnvironmentModel.SIMULATION_TIME - WorldModel.getInstance().getBall().getLastTimeSeen();
    }
}
