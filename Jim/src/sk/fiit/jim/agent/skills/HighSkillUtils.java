package sk.fiit.jim.agent.skills;

import java.util.Arrays;

import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.jim.agent.models.AgentModel;
import sk.fiit.jim.agent.models.WorldModel;
import sk.fiit.robocup.library.geometry.Angles;
import sk.fiit.robocup.library.geometry.Vector3D;

public class HighSkillUtils {

	public static boolean isBallNear(WorldModel worldModel){
		return (worldModel.getBall().notSeenLongTime() < 5);
	}

	public static boolean isOnGround(AgentModel agentModel) {
		return (agentModel.isOnGround() || agentModel.isLyingOnBack() || agentModel.isLyingOnBelly());
	}

	public static boolean isBallMine(AgentInfo agentInfo) {
		return agentInfo.isBallMine();
	}
	
	public static boolean isStraight(Angles range1, Angles range2, double targetPositionPhi) {
		return range1.include(targetPositionPhi) || range2.include(targetPositionPhi);
	}

	public static boolean isOnSideAndClose(Vector3D targetPosition, double strafe_distance, Angles range, double targetPositionPhi) {
		return (targetPosition.getR() <= strafe_distance) && range.include(Angles.normalize(targetPositionPhi));
	}

	public static boolean isBackAndClose(Angles back_range, double targetPositionPhi, Vector3D targetPosition, double strafe_distance) {
		return back_range.include(targetPositionPhi) && (targetPosition.getR() <= strafe_distance);
	}

	public static boolean isOnSideAndDistant(Angles range, Vector3D target) {
		return range.include(Angles.normalize(target.getPhi()));
	}
	
}
