package sk.fiit.jim.decision.situation;

import sk.fiit.jim.agent.models.AgentModel;
import sk.fiit.jim.agent.models.WorldModel;

/**
 * @author Samuel Benkovic
 */
public class FightForBall implements ISituation {
	
	public static final int EDGE_DISTANCE_FROM_BALL = 2;

	private AgentModel agentModel = AgentModel.getInstance();

	public boolean checkSituation() {
		if (this.agentModel.getDistanceNereastToBall(WorldModel.getInstance()
				.getTeamPlayers()) < FightForBall.EDGE_DISTANCE_FROM_BALL
				&& this.agentModel.getDistanceNereastToBall(WorldModel
						.getInstance().getOpponentPlayers()) < FightForBall.EDGE_DISTANCE_FROM_BALL) {
			return true;
		}
		return false;
	}
	
}
