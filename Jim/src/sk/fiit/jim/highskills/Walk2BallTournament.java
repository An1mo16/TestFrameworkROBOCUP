package sk.fiit.jim.highskills;

import java.util.Arrays;

import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.jim.agent.models.AgentModel;
import sk.fiit.jim.agent.models.WorldModel;
import sk.fiit.jim.agent.moves.LowSkill;
import sk.fiit.jim.agent.moves.LowSkills;
import sk.fiit.jim.agent.skills.HighSkill;
import sk.fiit.jim.agent.skills.HighSkillUtils;
import sk.fiit.robocup.library.geometry.Angles;
import sk.fiit.robocup.library.geometry.Vector3D;

public class Walk2BallTournament extends HighSkill {

	private double x1 = 0.04;
	private double x2 = 0.12;
	private double y1 = 0.35;
	private double y2 = 0.3;

	private double mediumDistance = 4;
	private double closeDistance = 0.7;

	private Angles straight_range1 = new Angles(0.0, 15.0);
	private Angles straight_range2 = new Angles(345.0, 360.0);
	
	private Angles right_range = new Angles(180.0, 325.0);
	private Angles right_range2 = new Angles(325.0, 340.0);
	private Angles right_range3 = new Angles(340.0, 346.0);
	
	private Angles left_range = new Angles(35.0, 180.0);
	private Angles left_range2 = new Angles(20.0, 35.0);
	private Angles left_range3 = new Angles(14.0, 20.0);

	private Vector3D target;
	private boolean validityProc;
	private boolean ending;

	private AgentInfo agentInfo = AgentInfo.getInstance();
	private AgentModel agentModel = AgentModel.getInstance();
	private WorldModel worldModel = WorldModel.getInstance();

	public Walk2BallTournament(boolean validityProc) {
		this.target = agentInfo.ballControlPosition();
		this.validityProc = validityProc;
		this.ending = false;
		this.agentInfo = AgentInfo.getInstance();
	}

	//Todo #Task(Implement validity_proc) #Solver(xmarkech) #Priority() | xmarkech 2013-12-10T20:27:54.6970000Z
	
	@Override
	public LowSkill pickLowSkill() {
		boolean isStillValid = validityProc;
		ending = !isStillValid;
		if (!isStillValid && ending) {
			return null;
		}
		agentInfo = AgentInfo.getInstance();
		target = agentInfo.ballControlPosition();

		double targetPositionPhi = target.getPhi();

		if (HighSkillUtils.isOnGround(agentModel)) {
			return null;
		} else if (!HighSkillUtils.isBallNear((worldModel))) {
			return null;
		} else if (target.getR() < closeDistance) {
//			agentInfo.loguj("som pri lopte ");
			if (target.getY() > y1) {
				if (target.getX() < -x2) {
//					agentInfo.loguj("ZONA 3");
					return LowSkills.get("step_left");
				} else if (target.getX() > x2) {
//					agentInfo.loguj("ZONA 2");
					return LowSkills.get("step_right");
				} else {
//					agentInfo.loguj("ZONA 1");
					return LowSkills.get("walk_slow");
				}
			} else if (target.getY() > y2) {
				if (target.getX() < -x2) {
//					agentInfo.loguj("ZONA 3");
					return LowSkills.get("step_left");
				} else if (target.getX() > x2) {
//					agentInfo.loguj("ZONA 2");
					return LowSkills.get("step_right");
				} else {
//					agentInfo.loguj("KOPEM");
					return null;
				}
			}else if (target.getY() < 0) {
				if (target.getX() < -x2) {
//					agentInfo.loguj("ZONA 4");
					return LowSkills.get("walk_back");
				} else if (target.getX() > x2) {
//					agentInfo.loguj("ZONA 5");
					return LowSkills.get("walk_back");
				}else if (target.getX() > 0) {
//					agentInfo.loguj("ZONA 7");
					return LowSkills.get("step_right");
				} else {
//					agentInfo.loguj("ZONA 8");
					return LowSkills.get("step_left");
				}
			}else{
//				agentInfo.loguj("ZONA 6");
				return LowSkills.get("walk_back");
			}
		}else{
			if(HighSkillUtils.isStraight(straight_range1, straight_range2, targetPositionPhi)){
				return LowSkills.get("walk_forward");
			}else if (HighSkillUtils.isOnSideAndDistant(right_range, target)) {//right
				return LowSkills.get("turn_right_45");
			}else if (rightAndDistantLess(target)) {
				return LowSkills.get("turn_right_cont_20");
			}else if (rightAndDistantBit(target)) {
				return LowSkills.get("turn_right_cont_20");
			}else if (HighSkillUtils.isOnSideAndDistant(left_range, target)) {//left
				return LowSkills.get("turn_left_45");
			}else if (leftAndDistantLess(target)) {
				return LowSkills.get("turn_left_cont_20");
			}else if (leftAndDistantBit(target)) {
				return LowSkills.get("turn_left_cont_20");
			}else{
				return LowSkills.get("rollback");
			}
		}
	}

	private boolean leftAndDistantBit(Vector3D target2) {
		return left_range3.include(Angles.normalize(target.getPhi()));
	}

	private boolean leftAndDistantLess(Vector3D target2) {
		return left_range2.include(Angles.normalize(target.getPhi()));
	}

	private boolean rightAndDistantBit(Vector3D target2) {
		return right_range3.include(Angles.normalize(target.getPhi()));
	}

	private boolean rightAndDistantLess(Vector3D target2) {
		return right_range2.include(Angles.normalize(target.getPhi()));
	}

	@Override
	public void checkProgress() throws Exception {
	}

}
