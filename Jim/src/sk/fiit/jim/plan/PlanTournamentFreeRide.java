package sk.fiit.jim.plan;

import sk.fiit.jim.LambdaCallable;
import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.jim.agent.models.AgentModel;
import sk.fiit.jim.agent.models.EnvironmentModel;
import sk.fiit.jim.highskills.*;
import sk.fiit.robocup.library.geometry.Vector3D;

/**
 * Rewrited PlanTournamentFreeRide from ruby
 * 
 * @author Matej Badal
 */

public class PlanTournamentFreeRide extends Plan {

	public static PlanTournamentFreeRide getInstance() {
		return new PlanTournamentFreeRide();
	}

	public void replan() {
        AgentModel agentModel = AgentModel.getInstance();
           
		if (EnvironmentModel.beamablePlayMode()) {
			AgentInfo.logState("Beam");
			Vector3D start_position = Vector3D.cartesian(-5, 1, 0.4);
			Highskill_Queue.add(new Beam(start_position));
			this.beamed = true;
		} else if (agentModel.isOnGround() || agentModel.isLyingOnBack() || agentModel.isLyingOnBelly()) {
			AgentInfo.logState("GetUp");
			Highskill_Queue.add(new GetUp(new LambdaCallable() {
				public boolean call() {
					return ! AgentModel.getInstance().isOnGround();
				}
			}));
		} else {
			AgentInfo.logState("FreeRide");
			Highskill_Queue.add(new FreeRide());
		}
	}
}
