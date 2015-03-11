package sk.fiit.jim.highskills;

import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.jim.agent.moves.LowSkill;
import sk.fiit.jim.agent.moves.LowSkills;
import sk.fiit.jim.agent.skills.HighSkill;
import sk.fiit.robocup.library.geometry.Angles;
import sk.fiit.robocup.library.geometry.Vector3D;
import sk.fiit.jim.agent.models.AgentModel;
import sk.fiit.jim.agent.models.EnvironmentModel;
import sk.fiit.jim.agent.models.WorldModel;

/**
 * 
 * Reimplemented from Ruby to Java by Stefan Horvath
 *
 */
public class WalkNew extends HighSkill {
	
	AgentInfo agentInfo = AgentInfo.getInstance();
	AgentModel agentModel = AgentModel.getInstance();
	
	Angles right = new Angles(180, 360);
	Angles left = new Angles(0, 180);
	Angles front1 = new Angles(0, 90);
	Angles front2 = new Angles(270, 360);
	Angles back = new Angles(90, 270);
	
	Angles go1 = new Angles(0, 30);
	Angles go2 = new Angles(330, 360);

	Angles range1 = new Angles(0.0, 25.0);
	Angles range2 = new Angles(335.0, 360.0);
	
	boolean haveToBeTurnedToTarget;
	double radiusBall;
	double radiusSpeed;
	
	Vector3D ball = null;
	Vector3D kickTarget = null;
	
	public WalkNew(boolean haveToBeTurnedToTarget, double radiusBall, double radiusSpeed) {
		super();
		this.haveToBeTurnedToTarget = haveToBeTurnedToTarget;
		this.radiusBall = radiusBall;
		this.radiusSpeed = radiusSpeed;
		
		if (haveToBeTurnedToTarget) {
			go1 = new Angles(0, 15);
			go2 = new Angles(335, 360);
		}
		else {
			go1 = new Angles(0, 30);
			go2 = new Angles(330, 360);
		}
	}
	//Todo #Task(Implement validity_proc) #Solver(xmarkech) #Priority() | xmarkech 2013-12-10T20:27:54.6970000Z 
	
	
	@Override
	public LowSkill pickLowSkill() {
		ball = WorldModel.getInstance().getBall().getRelativePosition();
		kickTarget = agentInfo.kickTarget();
		
		if (false) { // ruby --> validity_proc ???
			agentInfo.loguj("validity");
			return null;
		}
		else if (ball.getR() < radiusBall) {
			agentInfo.loguj("ball radius");
			return null;
		}
		else if (EnvironmentModel.beamablePlayMode()) {
			agentInfo.loguj("beam");
			return null;
		}
		else if (agentModel.falled()) {
			agentInfo.loguj("fall");
			return null;
		}
		else if (ballUnseen() > 3) {
			agentInfo.loguj("ball unseen");
			return null;
		}
		else if (haveToBeTurnedToTarget && !turnedToGoal()) {
			agentInfo.loguj("not turned to goal");
			return null;
		}
		else if (ballInRange(go1) || ballInRange(go2)) {
			if (closeEnough(radiusSpeed)) {
				agentInfo.loguj("go and blizko");
				return LowSkills.get("walk_forward");
			}
			else {
				agentInfo.loguj("go and daleko");
				return LowSkills.get("walk_forward");
			}
		}
		else if (ballInRange(left) && (ballInRange(front1) || ballInRange(front2))) {
			agentInfo.loguj("vlavo vpredu");
			if ((ball.getR() < 0.5) && haveToBeTurnedToTarget)
				return LowSkills.get("walk_forward");
			else
				return LowSkills.get("turn_left_cont_20");
		}
		else if (ballInRange(right) && (ballInRange(front1) || ballInRange(front2))) {
			agentInfo.loguj("vpravo vpredu");
			if ((ball.getR() < 0.5) && haveToBeTurnedToTarget)
				return LowSkills.get("walk_forward");
			else
				return LowSkills.get("turn_right_cont_20");
		}
		else if (ballInRange(left) && ballInRange(back)) {
			agentInfo.loguj("vlavo vzadu");
			return LowSkills.get("turn_left_90");
		}
		else if (ballInRange(right) && ballInRange(back)) {
			agentInfo.loguj("vpravo vzadu");
			return LowSkills.get("turn_right_90");
		}
		return null;
	}
	
	public double ballUnseen() {
		return EnvironmentModel.SIMULATION_TIME - WorldModel.getInstance().getBall().getLastTimeSeen();
	}
	
	public boolean turnedToGoal() {
		return range1.include(kickTarget.getPhi()) || range2.include(kickTarget.getPhi());
	}

	public boolean ballInRange(Angles range) {
		return range.include(Math.toDegrees(ball.getPhi()));
	}
	
	public boolean closeEnough(double radius) {
		return ball.getR() < radius;
	}
	
	@Override
	public void checkProgress() throws Exception {
	}

}
