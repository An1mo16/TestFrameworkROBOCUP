package sk.fiit.jim.plan;

import sk.fiit.jim.LambdaCallable;
import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.jim.agent.models.AgentModel;
import sk.fiit.jim.agent.models.WorldModel;
import sk.fiit.jim.highskills.*;
import sk.fiit.robocup.library.geometry.Vector3D;

/**
 * Rewrited PlanSch from ruby
 * 
 * @author Bosiak
 */

public class PlanSch extends Plan {
	public static PlanSch getInstance() {
		return new PlanSch();
	}

	public void replan() {
		AgentModel agentModel = AgentModel.getInstance();
		Vector3D target_position = AgentInfo.getInstance()
				.ballControlPosition();
		if (agentModel.isOnGround() || agentModel.isLyingOnBack()
				|| agentModel.isLyingOnBelly()) {

			AgentInfo.logState("GetUp");
			Highskill_Queue.add(new GetUp(new LambdaCallable() {
				public boolean call() {
					return ! AgentModel.getInstance().isOnGround();
				}
			}));
		} else if (!WorldModel.getInstance().isSeeBall()) {
			AgentInfo.logState("Localize");
			Highskill_Queue.add(new Localize(new LambdaCallable() {
				public boolean call() {
					return !WorldModel.getInstance().isSeeBall();
				}
			}));
		} else {
			AgentInfo.logState("GoToBall");
			Highskill_Queue.add(new GotoBall(target_position));

		}
	}
}
