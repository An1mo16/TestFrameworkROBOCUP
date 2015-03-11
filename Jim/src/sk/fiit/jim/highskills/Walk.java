package sk.fiit.jim.highskills;

import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.jim.agent.models.AgentModel;
import sk.fiit.jim.agent.models.WorldModel;
import sk.fiit.jim.agent.moves.LowSkill;
import sk.fiit.jim.agent.moves.LowSkills;
import sk.fiit.jim.agent.skills.HighSkill;
import sk.fiit.jim.agent.skills.HighSkillUtils;
import sk.fiit.robocup.library.geometry.Angles;
import sk.fiit.robocup.library.geometry.Vector3D;

public class Walk extends HighSkill {

	private Angles left_strafe_crossing = new Angles(60.0, 80.0);
	private Angles right_strafe_crossing = new Angles(280.0, 300.0);
	private Angles left_back_crossing = new Angles(0.0, 100.0);
	private Angles right_back_crossing = new Angles(260.0, 280.0);
	private Angles straight_range1 = new Angles(0.0, 30.0);
	private Angles straight_range2 = new Angles(330.0, 360.0);
	private Angles back_range = new Angles(90.0, 270.0);
	private Angles right_range = new Angles(180.0, 330.0);
	private Angles left_range = new Angles(30.0, 180.0);
	private Angles left_strafe_range = new Angles(45.0, 90.0);
	private Angles right_strafe_range = new Angles(270.0, 315.0);
	private double strafe_distance = 1.3;
	private double very_close_distance = 0.4;

	private Vector3D target;
	private AgentInfo agentInfo = AgentInfo.getInstance();
	private AgentModel agentModel = AgentModel.getInstance();
	private WorldModel worldModel = WorldModel.getInstance();
	private boolean ending = false;
	private Vector3D targetPosition = agentInfo.ballControlPosition();
	private boolean validityProc;

	// private Object validity_proc = validity_proc || Proc.new{return true}
	//Todo #Task(Implement validity_proc) #Solver(xmarkech) #Priority() | xmarkech 2013-12-10T20:27:54.6970000Z

	public Walk(Vector3D target, boolean validityProc){
		this.agentInfo = AgentInfo.getInstance();
		this.target = target;
		this.validityProc= validityProc;
		this.ending = false;
		this.targetPosition = agentInfo.ballControlPosition();
	}
	
	@Override
	public LowSkill pickLowSkill() {

		boolean isStillValid = validityProc;//@validity_proc.call;
		ending = !isStillValid;
		if(!isStillValid && ending){
			return null;
		}
		agentInfo = AgentInfo.getInstance();
		targetPosition = agentInfo.ballControlPosition();
		double targetPositionPhi = targetPosition.getPhi();
		
		if(HighSkillUtils.isOnGround(agentModel)){
			return null;
		}else if (!HighSkillUtils.isBallNear(worldModel)) {
			return null;
		}else if (HighSkillUtils.isBallMine(agentInfo) && HighSkillUtils.isStraight(straight_range1, straight_range1, targetPositionPhi)) {
			return null;
		}else if (HighSkillUtils.isOnSideAndClose(targetPosition, strafe_distance, left_strafe_range, targetPositionPhi)) {//left
			return LowSkills.get("step_left");
		}else if (HighSkillUtils.isOnSideAndClose(targetPosition, strafe_distance, right_strafe_range, targetPositionPhi)) {//right
			return LowSkills.get("step_right");
		}else if (HighSkillUtils.isStraight(straight_range1, straight_range2, targetPositionPhi)) {
			return LowSkills.get("walk_forward");
		}else if (HighSkillUtils.isBackAndClose(back_range, targetPositionPhi, targetPosition, strafe_distance)) {
			return LowSkills.get("walk_back");
		}else if (HighSkillUtils.isOnSideAndDistant(left_range, target)) {//left
			return LowSkills.get("turn_left_cont_20");
		}else if (HighSkillUtils.isOnSideAndDistant(right_range, target)) {//right
			return LowSkills.get("turn_right_cont_20");
		}
		return null;
	}

	@Override
	public void checkProgress() throws Exception {
	}

}
