package sk.fiit.jim.highskills;

import sk.fiit.jim.Settings;
import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.jim.agent.models.AgentModel;
import sk.fiit.jim.agent.models.DynamicObject;
import sk.fiit.jim.agent.models.WorldModel;
import sk.fiit.jim.agent.moves.LowSkill;
import sk.fiit.jim.agent.moves.LowSkills;
import sk.fiit.jim.agent.skills.HighSkill;
import sk.fiit.jim.desiciontree.XmlDesicionLoader;
import sk.fiit.robocup.library.geometry.Angles;
import sk.fiit.robocup.library.geometry.Vector3D;

/**
 *
 * Reimplemented from Ruby to Java by roman moravcik
 */

public class Walk2Ball extends HighSkill {
    // TODO y1,y2,x1,x2,mediumDistance, closeDistance should be static
    private double y1 = 0.35;
    private double y2 = 0.2;
    private double x1 = 0.04;
    private double x2 = 0.12;
    private double mediumDistance = 4;
    private double closeDistance = 0.7;
    private Angles straightRange1 = new Angles(0.0, 15.0);
    private Angles straightRange2 = new Angles(345.0, 360.0);
    private Angles rightRange3 = new Angles(340.0, 346.0);
    private Angles rightRange2 = new Angles(325.0, 340.0);
    private Angles rightRange = new Angles(180.0, 325.0);
    private Angles leftRange3 = new Angles(14.0, 20.0);
    private Angles leftRange2 = new Angles(20.0, 35.0);
    private Angles leftRange = new Angles(35.0, 180.0);
    Vector3D target;

	//Todo #Task(Implement validity_proc) #Solver(xmarkech) #Priority() | xmarkech 2013-12-10T20:27:54.6970000Z
    
    @Override
    public LowSkill pickLowSkill() {

        AgentInfo agentInfo = AgentInfo.getInstance();
        AgentModel agentModel = AgentModel.getInstance();
        this.target = agentInfo.ballControlPosition();
        DynamicObject ball = WorldModel.getInstance().getBall();

        if (agentModel.isLyingOnBack() || agentModel.isLyingOnBelly() || agentModel.isOnGround()) {
            return null;
        }

        if (ball.notSeenLongTime() >= 5) {
      //      return null;
        }
        
        String file = "";
        if(!Settings.getString("dtWalk2Ball").isEmpty())
        	file = Settings.getString("dtWalk2Ball");
       
        
        XmlDesicionLoader pickLowSkill = new XmlDesicionLoader(file, (HighSkill)this);
        
        return LowSkills.get(pickLowSkill.GetLowSkill());
       /* // je blizko lopty
        agentInfo.loguj("Walk2Ball");
        if (target.getR() < closeDistance) {
            //agentInfo.loguj("som pri lopte ")

            if (target.getY() > y1) {
                if (target.getX() < -x2) {
                    //agentInfo.loguj("ZONA 3")
                    return LowSkills.get("step_left");
                } else if (target.getX() > x2) {
                    //agentInfo.loguj("ZONA 2")
                    return LowSkills.get("step_right");
                } else {
                    //agentInfo.loguj("ZONA 1")
                    return LowSkills.get("walk_slow");
                }
            } else if (target.getY() > y2) {
                if (target.getX() < -x2) {
                    //agentInfo.loguj("ZONA 3")
                    return LowSkills.get("step_left");
                } else if (target.getX() > x2) {
                    //agentInfo.loguj("ZONA 2")
                    return LowSkills.get("step_right");
                } else {
                    //agentInfo.loguj("mozem kopat")
                    return null; //LowSkills.get("kick_right_faster");
                }
            } else if (target.getY() < 0) {
                if (target.getX() < -x2) {
                    //agentInfo.loguj("ZONA 4")
                    return LowSkills.get("walk_back");
                } else if (target.getX() > x2) {
                    //agentInfo.loguj("ZONA 5")
                    return LowSkills.get("walk_back");
                } else if (target.getX() > 0) {
                    //agentInfo.loguj("ZONA 7")
                    return LowSkills.get("step_right");
                } else {
                    //agentInfo.loguj("ZONA 8")
                    return LowSkills.get("step_left");
                }
            } else {
                //agentInfo.loguj("ZONA 6")
                return LowSkills.get("walk_back");
            }
        } else {
            // je dalej od lopty
            //agentInfo.loguj("som daleako2 od lopty ")
            if (straight()) {
                //agentInfo.loguj("rovno")
                return LowSkills.get("walk_forward");
            } else if (right_and_distant()) {
                //agentInfo.loguj("vpravo")
                //return LowSkills.get("turn_right_cont_4.5")
                return LowSkills.get("turn_right_45");
            } else if (right_and_distant_less()) {
                //agentInfo.loguj("vpravo MENEJ")
                return LowSkills.get("turn_right_cont_20");
            } else if (right_and_distant_bit()) {
                //gentInfo.loguj("vpravo malicko")
                return LowSkills.get("turn_right_cont_20");
            } else if (left_and_distant()) {
                //gentInfo.loguj("vlavo")
                //return LowSkills.get("turn_left_cont_4.5")
                return LowSkills.get("turn_left_45");
            } else if (left_and_distant_less()) {
                //agentInfo.loguj("vlavo MENEJ")
                //return LowSkills.get("turn_left_cont_4.5")
                return LowSkills.get("turn_left_cont_20");
            } else if (left_and_distant_bit()) {
                //agentInfo.loguj("vlavo Malicko")
                //return LowSkills.get("turn_left_cont_4.5")
                return LowSkills.get("turn_left_cont_20");
            } else {
                //agentInfo.loguj("olalaa")
                return LowSkills.get("rollback");
            }
        }*/
    }
    public double getR(){
    	return target.getR();
    }
    
    public double getY(){
    	return target.getY();
    }
    
    public double getX(){
    	return target.getX();
    }
    
    
    public boolean straight(Angles straightRange1, Angles straightRange2) {
        return straightRange1.include(target.getPhi()) || straightRange2.include(target.getPhi());
    }

    public boolean left_and_distant(Angles leftRange) {
        return leftRange.include(Angles.normalize(target.getPhi()));
    }

    public boolean left_and_distant_less(Angles leftRange2) {
        return leftRange2.include(Angles.normalize(target.getPhi()));
    }

    public boolean left_and_distant_bit(Angles leftRange3) {
        return leftRange3.include(Angles.normalize(target.getPhi()));
    }

    public boolean right_and_distant(Angles rightRange) {
        return rightRange.include(Angles.normalize(target.getPhi()));
    }

    public boolean right_and_distant_less(Angles rightRange2) {
        return rightRange2.include(Angles.normalize(target.getPhi()));
    }

    public boolean right_and_distant_bit(Angles rightRange3) {
        return rightRange3.include(Angles.normalize(target.getPhi()));
    }

    @Override
    public void checkProgress() {
    }
}
