package sk.fiit.jim.decision.situation;

import sk.fiit.jim.agent.models.AgentModel;
 
/**
 * @author Samuel Benkovic
 */
public class FarFromEnemyGoal implements ISituation {
	
	public static final int EDGE_DISTANCE_FROM_GOAL = 3;

	public boolean checkSituation() {
		AgentModel agentModel = AgentModel.getInstance();
		if (agentModel.getDistanceFromEnemyGoal() > FarFromEnemyGoal.EDGE_DISTANCE_FROM_GOAL) {
			return true;
		}
		return false;
	}
	
}
