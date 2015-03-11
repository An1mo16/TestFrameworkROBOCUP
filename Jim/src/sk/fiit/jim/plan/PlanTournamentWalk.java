package sk.fiit.jim.plan;

/* HIGHSKILL NEEDED: 0
 */

import sk.fiit.jim.LambdaCallable;
import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.jim.agent.models.AgentModel;
import sk.fiit.jim.agent.models.EnvironmentModel;
import sk.fiit.jim.highskills.*;
import sk.fiit.robocup.library.geometry.Vector3D;

/**
 * Rewrited PlanTournamentGetUpFromBelly from ruby
 * 
 * @author Adamik
 */

public class PlanTournamentWalk extends Plan {

	public static PlanTournamentWalk getInstance() {
		return new PlanTournamentWalk();
	}

	public void replan() {
		AgentModel agentModel = AgentModel.getInstance();

		if (EnvironmentModel.beamablePlayMode()) {
			AgentInfo.logState("Beam");
			beamed = true;
			Vector3D start_position = Vector3D.cartesian(-5, 1, 0.4);
			Highskill_Queue.add(new Beam(start_position));
		} else if (agentModel.isOnGround() || agentModel.isLyingOnBack()
				|| agentModel.isLyingOnBelly()) {
			AgentInfo.logState("GetUp");
			Highskill_Queue.add(new GetUp(new LambdaCallable() {
				public boolean call() {
					return ! AgentModel.getInstance().isOnGround();
				}
			}));
		} else
			AgentInfo.logState("Go");
		Vector3D position_to_go = Vector3D.cartesian(10, 1, 0.4);
		Highskill_Queue.add(new GoToPosition(position_to_go,
				new LambdaCallable() {
					public boolean call() {
						return EnvironmentModel.beamablePlayMode();
					}
				}));

	}
}
