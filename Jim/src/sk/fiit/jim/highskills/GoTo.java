/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.fiit.jim.highskills;

import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.jim.agent.Planner;
import sk.fiit.jim.agent.communication.Communication;
import sk.fiit.jim.agent.models.AgentModel;
import sk.fiit.jim.agent.models.TacticalInfo;
import sk.fiit.jim.agent.models.WorldModel;
import sk.fiit.jim.agent.moves.LowSkill;
import sk.fiit.jim.agent.moves.LowSkills;
import sk.fiit.jim.agent.skills.HighSkill;
import sk.fiit.robocup.library.geometry.Angles;
import sk.fiit.robocup.library.geometry.Vector3D;

/**
 *
 * Reimplemented from Ruby to Java by Martin Markech
 */
public class GoTo extends HighSkill {
	private static Angles leftStrafeCrossing = new Angles(60.0, 80.0);
	private static Angles rightStrafeCrossing = new Angles(280.0, 300.0);
	private static Angles leftBackCrossing = new Angles(80.0, 100.0);
	private static Angles rightBackCrossing = new Angles(260.0, 280.0);
    private static Angles straightRange1 = new Angles(0.0, 30.0);
    private static Angles straightRange2 = new Angles(330.0, 360.0);
    private static Angles backRange = new Angles(90.0, 270.0);
    private static Angles rightRange = new Angles(180.0, 330.0);
    private static Angles leftRange = new Angles(30.0,180.0);
    public static Angles leftStrafeRange = new Angles(45.0, 90.0);
    public static Angles rightStrafeRange = new Angles(270.0, 315.0);
    public static double strafeDistance = 1.5;
    private static double veryCloseDistance = 0.4;
    
    private TacticalInfo tacticalInfo;
    private AgentInfo agentInfo;   
    private Vector3D targetPosition;
    private double targetPositionPhi;
    private double targetPositionPhiTemp;
    
    public GoTo() {
    	this.tacticalInfo = TacticalInfo.getInstance();
    	this.agentInfo = AgentInfo.getInstance();
    	this.targetPosition = this.tacticalInfo.getFormPosition();
    }
    
    
    
    @Override
    public void checkProgress() {
    }

    @Override
    public LowSkill pickLowSkill() {
    	// TODO  Why it is initializing already here?
    	this.tacticalInfo = TacticalInfo.getInstance();
    	this.targetPosition = this.tacticalInfo.getFormPosition();
    	this.targetPositionPhi = this.targetPosition.getPhi();
    	
    	if ( this.targetPosition != null){//TODO check if works, in ruby it was @target_position_phi != nil
    		this.targetPositionPhiTemp = this.targetPositionPhi;
    	}
    	else if( this.targetPosition == null && WorldModel.getInstance().isSeeBall() ) {
    		this.targetPositionPhi = this.targetPositionPhiTemp;
    	}
    	
   	
    	AgentModel agentModel = AgentModel.getInstance();
    	
    	if(agentModel.isOnGround() ){
    		//TODO
    		return null;
    	}
    	else if (this.tacticalInfo.isOnPosition() ){
    		this.agentInfo.loguj("close ");
    		return null;
    	}
    	else if (isLeftAndClose() ){    		
    		return LowSkills.get("step_left");
    	}
    	else if (isRightAndClose()){
    		return LowSkills.get("step_right"); 
    	}
    	else if (isStraight()){
    		return LowSkills.get("walk_forward"); 
    	}
    	else if (isBackAndClose()){
    		return LowSkills.get("walk_back"); 
    	}
    	else if (isLeftAndDistant()){
    		return LowSkills.get("turn_left_cont_20"); 
    	}
    	else if (isRightAndDistant()){
    		return LowSkills.get("turn_right_cont_20"); 
    	}
    	else return null;
    }
    
   /* TODO - with high probability this is dead code
    * def is_current_skill? possibilities
    * current_skill_name = currentSkill.nil? ? "null" : currentSkill.name
    * possibilities.member?(current_skill_name)
    * end
    */
    
    private boolean isCloseEnough(){
    	return this.targetPosition.getR() < 0.5;
    }
    
    private boolean isRightAndDistant() {
		return GoTo.rightRange.include(Angles.normalize(this.targetPosition.getPhi()));
	}

	private boolean isLeftAndDistant() {
		return GoTo.leftRange.include(Angles.normalize(this.targetPositionPhi));
	}

	private boolean isBackAndClose() {
		return GoTo.backRange.include(this.targetPositionPhi) &&
				this.targetPosition.getR() <= GoTo.strafeDistance;

	}

	private boolean isStraight() {
		return GoTo.straightRange1.include(this.targetPositionPhi) ||
				GoTo.straightRange2.include(this.targetPositionPhi);
	}

	private boolean isRightAndClose() {
		return this.targetPosition.getR() <= GoTo.strafeDistance &&
				 GoTo.rightStrafeRange.include(Angles.normalize(this.targetPositionPhi));
	}

	private boolean isLeftAndClose(){
    	return (this.targetPosition.getR() <= GoTo.strafeDistance ) && 
    			GoTo.leftStrafeRange.include(Angles.normalize(this.targetPositionPhi));
    }
}
