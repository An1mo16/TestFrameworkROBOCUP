package sk.fiit.jim.highskills;

import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.jim.agent.models.AgentModel;
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
public class WalkOld extends HighSkill {
	
	AgentInfo agentInfo = AgentInfo.getInstance();
	AgentModel agentModel = AgentModel.getInstance();
	
	Angles rightRange = new Angles(1.5*Math.PI, (11.0/6.0)*Math.PI);
	Angles leftRange = new Angles((1.0/6.0)*Math.PI, 0.5*Math.PI);
	Angles leftStrafeRange =  new Angles((5.0/12.0)*Math.PI, (0.5*Math.PI));
	Angles rightStrafeRange =  new Angles(1.5*Math.PI, ((19.0/12.0)*Math.PI));
	double strafeDistance = 1.5;
	Vector3D target = null;
	
	public WalkOld(Vector3D target) {
		super();
		this.target = target;
	}

	@Override
	public LowSkill pickLowSkill() {
		Vector3D targetPosition = null;
		if (target.equals(targetPosition = WorldModel.getInstance().getBall().getPosition()))
			targetPosition = WorldModel.getInstance().getBall().getRelativePosition();
		else
			targetPosition = WorldModel.getInstance().getBall().getPosition();
		targetPosition = targetPosition.setZ(0.0);
		if (closeEnought(targetPosition))
			return null;
		if (EnvironmentModel.beamablePlayMode())
			return null;
		if (agentModel.isOnGround())
			return null;
		if (ballUnseen() > 3)
			return null;
		if (straight(targetPosition))
			return LowSkills.get("walk_forward");
		if (leftAndDistant(targetPosition))
			return LowSkills.get("turn_left_cont_20");
		if (rightAndDistant(targetPosition))
			return LowSkills.get("turn_right_cont_20");
		return null;
	}
	
	public boolean closeEnought(Vector3D targetPosition) {
		return targetPosition.getR() < 0.0;
	}
	
	public boolean straight(Vector3D targetPosition) {
		return Angles.angleDiff(targetPosition.getPhi(), 0.0) < Math.PI / 6.0;
	}
	
	public boolean back(Vector3D targetPosition) {
		return Angles.angleDiff(targetPosition.getPhi(), Math.PI) < Math.PI / 6.0;
	}
	
	public boolean backAndRight(Vector3D targetPosition) {
		return (Angles.angleDiff(targetPosition.getPhi(), Math.PI) < Math.PI / 6.0) && (rightRange.include(Angles.normalize(targetPosition.getPhi())));
	}
	
	public boolean backAndLeft(Vector3D targetPosition) {
		return (Angles.angleDiff(targetPosition.getPhi(), Math.PI) < Math.PI / 6.0) && (leftRange.include(Angles.normalize(targetPosition.getPhi())));
	}
	
	public boolean leftAndClose(Vector3D targetPosition) {
		agentInfo.loguj(targetPosition.toString());
		return ((targetPosition.getR() <= strafeDistance) && leftStrafeRange.include(Angles.normalize(targetPosition.getPhi())));
	}
	
	public boolean rightAndClose(Vector3D targetPosition) {
		return ((targetPosition.getR() <= strafeDistance) && rightStrafeRange.include(Angles.normalize(targetPosition.getPhi())));
	}
	
	public boolean leftAndDistant(Vector3D targetPosition) {
		return leftRange.include(Angles.normalize(targetPosition.getPhi()));
	}
	
	public boolean rightAndDistant(Vector3D targetPosition) {
		return rightRange.include(Angles.normalize(targetPosition.getPhi()));
	}
	
	public double ballUnseen() {
		return EnvironmentModel.SIMULATION_TIME - WorldModel.getInstance().getBall().getLastTimeSeen();
	}
	
	@Override
	public void checkProgress() throws Exception {
	}

}
