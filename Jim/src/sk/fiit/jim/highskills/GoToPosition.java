package sk.fiit.jim.highskills;

import sk.fiit.jim.LambdaCallable;
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
 *  High skill to move player to position from parameter.
 *  Reimplemented from Ruby to Java by Martin Markech
 *  @author xsuchac
 *  @author: A55-Kickers
 *  
 */
public class GoToPosition extends HighSkill {
	private static Angles leftStrafeCrossing = new Angles(60.0, 80.0);
	private static Angles rightStrafeCrossing = new Angles(280.0, 300.0);
	private static Angles leftBackCrossing = new Angles(80.0, 100.0);
	private static Angles rightBackCrossing = new Angles(260.0, 280.0);
    private static Angles straightRange1 = new Angles(0.0, 30.0);
    private static Angles straightRange2 = new Angles(330.0, 360.0);
    private static Angles backRange = new Angles(90.0, 270.0);
    private static Angles rightRange = new Angles(180.0, 330.0);
    private static Angles leftRange = new Angles(30.0,180.0);
    private static Angles leftStrafeRange = new Angles(45.0, 90.0);
    private static Angles rightStrafeRange = new Angles(270.0, 315.0);
    private static double strafeDistance = 1.5;
    private static double veryCloseDistance = 0.4;
	
    private TacticalInfo tacticalInfo;
    private AgentInfo agentInfo;   
    private Vector3D targetPositionGlobal;
    private Vector3D targetPosition;
    private double targetPositionPhi;
    private double targetPositionPhiTemp;
    private AgentModel agentModel;
    private LambdaCallable validityProc;
    
    /*
     * @param position Vector3D Where player has to move
     * @param validityProc LambdaCallable "lambda" expression
     */
    
    public GoToPosition(Vector3D position, LambdaCallable validityProc) {
    	this.agentInfo = AgentInfo.getInstance();
    	this.agentModel = AgentModel.getInstance();
    	this.tacticalInfo = TacticalInfo.getInstance();
    	this.targetPositionGlobal = position; 
    	this.validityProc = validityProc;
    }
    
    
    
    @Override	
	public LowSkill pickLowSkill() {
    	if (!validityProc.call() ) {
    		return null;
    	}    	
    	
    	
    	if (this.targetPositionGlobal == null){
    		return null;
    	}
    	else {
    		this.agentModel = AgentModel.getInstance();
    		this.targetPosition = this.agentModel.relativize(this.targetPositionGlobal);
    	}
    	
    	 this.targetPositionPhi = this.targetPosition.getPhi();
    	
    	 if (this.targetPositionPhi != 0){
    		 this.targetPositionPhiTemp = this.targetPositionPhi;    		 
    	 }
    	 else if(this.targetPositionPhi == 0 && WorldModel.getInstance().isSeeBall()){
    		 this.targetPositionPhi = this.targetPositionPhiTemp;
    	 }
    	 
    	
    	AgentModel agentModel = AgentModel.getInstance();
    	
    	if(agentModel.isOnGround() ){
    		this.agentInfo.loguj("on ground ");
    		return null;
    	}
    	else if (isOnPosition()){
    		this.agentInfo.loguj("close ");
    		return null;
    	}
    	else if (isLeftAndClose()){
    		return LowSkills.get("step_left");
    	}
    	else if(isRightAndClose()){
    		return LowSkills.get("step_right");
    	}
    	else if(isBackAndClose() ){
    		return LowSkills.get("walk_back");
    	}
    	else if(isStraight() ){
    		return LowSkills.get("walk_forward");
    	}
    	else if(isLeftAndDistant() ){
    		return LowSkills.get("turn_left_cont_20");
    	}
    	else if(isRightAndDistant() ){
    		return LowSkills.get("turn_right_cont_20");
    	}
    	
    	
    	return null;
	}
    
    private boolean isCloseEnough(){
    	return this.targetPosition.getR() < 0.5;
    }
    
    private boolean isStraight(){
    	return this.straightRange1.include(this.targetPositionPhi) || this.straightRange2.include(this.targetPositionPhi);
    }
    
    private boolean isBackAndClose(){
    	return this.backRange.include(this.targetPositionPhi) && (this.targetPosition.getR() <= this.strafeDistance);
    	  
    }
    
    private boolean isLeftAndClose(){
    	return (this.targetPosition.getR() <= this.strafeDistance) && this.leftStrafeRange.include(Angles.normalize(this.targetPositionPhi));
    }
   
    
    private boolean isRightAndClose(){
    	return (this.targetPosition.getR() <= this.strafeDistance) && this.rightStrafeRange.include(Angles.normalize(this.targetPositionPhi)); 
    }
    
    private boolean isLeftAndDistant(){
    	return this.leftRange.include(Angles.normalize(this.targetPositionPhi));
    }
    
    private boolean isRightAndDistant(){
    	return this.rightRange.include(Angles.normalize(this.targetPositionPhi));
    }
   

    //Todo #Task(During rewriting from Ruby - seems it is not used or brokend) #Solver() #Priority(tiny) | xmarkech 2013-12-10T20:36:57.5950000Z
    /*   
    private boolean isBack(double target_position){
    	return Angles.angleDiff(target_phi, Math.PI) < Math.PI / 6.0;    	
    }
*/    
    
    
    private boolean isOnPosition(){
    	return Math.abs(this.agentModel.getPosition().getXYDistanceFrom(this.targetPositionGlobal)) < 0.5;
    }
    
    //Todo #Task(Check if it is needed after migrating to new architecture) #Solver() #Priority() | xmarkech 2013-12-10T20:38:13.4560000Z 
	@Override
	public void checkProgress() throws Exception {
		// TODO Auto-generated method stub
		
	}
    
	
}
