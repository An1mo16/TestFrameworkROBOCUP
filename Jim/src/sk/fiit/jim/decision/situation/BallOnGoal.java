package sk.fiit.jim.decision.situation;

import sk.fiit.jim.agent.models.WorldModel;

public class BallOnGoal implements ISituation {

	private WorldModel worldModel = WorldModel.getInstance();
	
	public boolean checkSituation() {
            double x = this.worldModel.getBall().getPrediction().getX();
            
            return (x >= 15);
	}	
}