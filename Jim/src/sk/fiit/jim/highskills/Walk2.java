package sk.fiit.jim.highskills;

import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.jim.agent.models.AgentModel;
import sk.fiit.jim.agent.models.DynamicObject;
import sk.fiit.jim.agent.models.WorldModel;
import sk.fiit.jim.agent.moves.LowSkill;
import sk.fiit.jim.agent.moves.LowSkills;
import sk.fiit.jim.agent.skills.HighSkill;
import sk.fiit.robocup.library.geometry.Angles;
import sk.fiit.robocup.library.geometry.Vector3D;

/**
* Asi by mali byt vsetky step_circ_rigt_small vymenene za step_circ_left_small a naopak.
* kick_target sa pouziva len v jedinej podmienke metody straight, ale ta cast podmienky je zakomentovana.
* Reimplemented from Ruby to Java by Filip Blanarik
*/

public class Walk2 extends HighSkill {
	
	private double y1 = 0.35;
    private double y2 = 0.2;
    private double x1 = 0.04;
    private double x2 = 0.12;
	private double mediumDistance = 4;
	private double closeDistance = 0.7;
	private Angles straightRange1 = new Angles(0.0, 30.0);
	private Angles straightRange2 = new Angles(330.0, 360.0);
	private Angles rightRange = new Angles(180.0, 330.0);
	private Angles leftRange = new Angles(30.0, 180.0);
	Vector3D target, kick_target;
	
	//Todo #Task(Implement validity_proc) #Solver(xmarkech) #Priority() | xmarkech 2013-12-10T20:27:54.6970000Z
	
	@Override
	public LowSkill pickLowSkill() {
		
		AgentInfo agentInfo = AgentInfo.getInstance();
        AgentModel agentModel = AgentModel.getInstance();
        this.kick_target = agentInfo.kickTarget();  
        this.target = agentInfo.ballControlPosition();
        DynamicObject ball = WorldModel.getInstance().getBall(); 
		
        if (agentModel.isOnGround() || agentModel.isLyingOnBack() || agentModel.isLyingOnBelly()) {
            return null;
        } else if (ball.notSeenLongTime() >= 5) { 
            return null;
        } else { //ak stoji a vie kde je lopta
            if (target.getR() < closeDistance) {  // je blizko ciela
	            agentInfo.loguj("som pri lopte ");
            	if (target.getY() > y1) {
	                if (target.getX() < -x2) {
	                    //agentInfo.loguj("ZONA 3");
	                	//agentInfo.loguj("step_left");
	                    return LowSkills.get("step_left");
	                } else if (target.getX() > x2) {
	                    //agentInfo.loguj("ZONA 2");
	                	//agentInfo.loguj("step_right");
	                    return LowSkills.get("step_right");
	                } else {
	                    //agentInfo.loguj("ZONA 1");
	                	//agentInfo.loguj("walk_slow");
	                	//return LowSkills.get("walk_slow");
	                    return LowSkills.get("walk_slow2");
	                }
	            } else if (target.getY() > y2) {
	                if (target.getX() < -x2) {
	                    //agentInfo.loguj("ZONA 3");
	                	//agentInfo.loguj("step_left");
	                    return LowSkills.get("step_left");
	                } else if (target.getX() > x2) {
	                    //agentInfo.loguj("ZONA 2");
	                	//agentInfo.loguj("step_right");
	                    return LowSkills.get("step_right");
	                } else {
	                    //agentInfo.loguj("mozem kopat");
	                    return null; //LowSkills.get("kick_right_faster");
	                }
	            } else if (target.getY() < 0) {
	                if (target.getX() < -x2) {
	                    //agentInfo.loguj("ZONA 4");
	                	//agentInfo.loguj("walk_back");
	                    return LowSkills.get("walk_back");
	                } else if (target.getX() > x2) {
	                    //agentInfo.loguj("ZONA 5");
	                	//agentInfo.loguj("walk_back");
	                    return LowSkills.get("walk_back");
	                } else if (target.getX() > 0) {
	                    //agentInfo.loguj("ZONA 7");
	                	//agentInfo.loguj("step_right");
	                    return LowSkills.get("step_right");
	                } else {
	                    //agentInfo.loguj("ZONA 8");
	                	//agentInfo.loguj("step_left");
	                    return LowSkills.get("step_left");
	                }
	            } else {
	                //agentInfo.loguj("ZONA 6");
	            	//agentInfo.loguj("walk_back");
	                return LowSkills.get("walk_back");
	            }
            } else if ((target.getR() < mediumDistance) && (target.getR() > closeDistance)) { //stredna vzdialenost od ciela
            	agentInfo.loguj("medium distance ");
	            if (straight()) {
	                //agentInfo.loguj("rovno");
	                return LowSkills.get("walk_forward");
	            } else if (right_and_distant()) {
	                agentInfo.loguj("vpravo");
	                //return LowSkills.get("turn_right_cont_10");
	                return LowSkills.get("step_circ_right_small");
	            } else if (left_and_distant()) {
	                agentInfo.loguj("vlavo");
	                //return LowSkills.get("turn_left_cont_10");
	                return LowSkills.get("step_circ_left_small");
	            } else {
	                agentInfo.loguj("olalaa");
	                return LowSkills.get("rollback");
	            }
	        } else {  // je dalej od lopty
                //agentInfo.loguj("som daleako od lopty ");;
	            if (straight()) {
	                //agentInfo.loguj("rovno");
	            	agentInfo.loguj("walk_fast");
	                return LowSkills.get("walk_forward");
	            } else if (right_and_distant()) {
	                agentInfo.loguj("vpravo_daleko ");
	                //return LowSkills.get("turn_right_cont_20");
	                return LowSkills.get("step_circ_right_small");
	            } else if (left_and_distant()) {
	                agentInfo.loguj("vlavo_daleko ");
	                //return LowSkills.get("turn_left_cont_20");
	                return LowSkills.get("step_circ_left_small");
	            } else {
	                //agentInfo.loguj("olalaa");
	                return LowSkills.get("rollback");
	            }
	        }
        }        
	}
	

	private boolean straight() {
        return (straightRange1.include(target.getPhi()) || straightRange2.include(target.getPhi())) /*&& (straightRange1.include(kick_target.getPhi()) || straightRange2.include(kick_target.getPhi()))*/;
    }

    private boolean left_and_distant() {
        return leftRange.include(Angles.normalize(target.getPhi()));
    }

    private boolean right_and_distant() {
        return rightRange.include(Angles.normalize(target.getPhi()));
    }

    public void checkProgress() {
    }

}
