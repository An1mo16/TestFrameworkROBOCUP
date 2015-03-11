package sk.fiit.jim.highskills;

import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.jim.agent.models.AgentModel;
import sk.fiit.jim.agent.models.EnvironmentModel;
import sk.fiit.jim.agent.models.WorldModel;
import sk.fiit.jim.agent.moves.LowSkill;
import sk.fiit.jim.agent.moves.LowSkills;
import sk.fiit.jim.agent.skills.HighSkill;
import sk.fiit.robocup.library.geometry.Vector3D;

public class KickTournament extends HighSkill {

	private Vector3D target;
	private AgentInfo agentInfo = AgentInfo.getInstance();
	private AgentModel agentModel = AgentModel.getInstance();
	private WorldModel worldModel = WorldModel.getInstance();


	public KickTournament(Vector3D target) {
		this.target = target;
	}

	@Override
	public LowSkill pickLowSkill() {

		Vector3D ballPosition = agentInfo.ballControlPosition();

		if (EnvironmentModel.beamablePlayMode() && !EnvironmentModel.isKickOffPlayMode()) {
			return null;
		} else if (agentModel.falled()) {
			AgentInfo.logState("Beam");
			return null;
		} else if (isballLongUnseen(3)) {
			AgentInfo.logState("ball unseen");
			return null;
		} else if ((Math.abs(worldModel.getBall().getPosition().getY())) > 0.7) {
			AgentInfo.logState("very big Y");
			return null;
		} else if ((Math.abs(worldModel.getBall().getPosition().getX())) > 0.7) {
			AgentInfo.logState("very big X");
			return null;
		} else if ((Math.abs(worldModel.getBall().getPosition().getY()) > 0.35) && (Math.abs(worldModel.getBall().getPosition().getY()) < 0.7)) {
			AgentInfo.logState("go a bit");
			return LowSkills.get("walk_slow2");
		} else if ((Math.abs(worldModel.getBall().getPosition().getX()) > 0.1) && (Math.abs(worldModel.getBall().getPosition().getX()) < 0.7)) {
			AgentInfo.logState("big x");
			return LowSkills.get("step_right");
		} else if ((Math.abs(worldModel.getBall().getPosition().getX()) < -0.1) && (Math.abs(worldModel.getBall().getPosition().getX()) > -0.7)) {
			AgentInfo.logState("small x");
			return LowSkills.get("step_left");
		} else if (worldModel.getBall().getPosition().getX() > 0.0) {
			AgentInfo.logState("kick right");
			return kickDistance("right", worldModel.getBall().getPosition());
		} else if (worldModel.getBall().getPosition().getX() < 0.0) {
			AgentInfo.logState("kick left");
			return kickDistance("left", worldModel.getBall().getPosition());
		}
		return null;
	}

	private LowSkill kickDistance(String leg, Vector3D position) {
		double distance = agentInfo.calculateDistance(position, target);
		AgentInfo.logState("vzdialenost=" + distance);

		if (distance > 4 && "right".equals(leg)) {
			AgentInfo.logState("kick_step_strong_right");
			return LowSkills.get("kick_step_strong_right");
		} else if (distance > 4 && "left".equals(leg)) {
			AgentInfo.logState("kick_step_strong_left");
			return LowSkills.get("kick_step_strong_left");
		} else if (distance > 1.5 && distance <= 4 && "right".equals(leg)) {
			AgentInfo.logState("kick_right_faster");
			return LowSkills.get("kick_right_faster");
		} else if (distance > 1.5 && distance <= 4 && "left".equals(leg)) {
			AgentInfo.logState("kick_left_faster");
			return LowSkills.get("kick_left_faster");
		}else if (distance > 0.5 && distance <= 1.5 && "right".equals(leg)) {
			AgentInfo.logState("kick_right_normal");
			return LowSkills.get("kick_right_normal");
		} else if (distance > 0.5 && distance <= 1.5 && "left".equals(leg)) {
			AgentInfo.logState("kick_left_normal");
			return LowSkills.get("kick_left_normal");
		}
		else {
			return null;
		}
	}

	@Override
	public void checkProgress() throws Exception {
	}

}
