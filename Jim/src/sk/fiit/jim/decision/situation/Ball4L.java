package sk.fiit.jim.decision.situation;

import sk.fiit.jim.agent.models.WorldModel;

public class Ball4L implements ISituation {

	private WorldModel worldModel = WorldModel.getInstance();
	
	public boolean checkSituation() {
		if ((this.worldModel.getBall().getPosition().getX() > 7.5) && (this.worldModel.getBall().getPosition().getY() >= 0)){
			return true;
		}
		return false;
	}	
}
