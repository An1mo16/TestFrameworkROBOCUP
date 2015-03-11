package sk.fiit.jim.plan;

import sk.fiit.jim.LambdaCallable;
import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.jim.agent.models.*;
import sk.fiit.jim.highskills.*;
import sk.fiit.robocup.library.geometry.Vector3D;

/**
 * Rewrited Plan5ko from ruby
 * 
 * @author Petras
 */

public class Plan5ko extends Plan {

	public static Plan5ko getInstance() {
		return new Plan5ko();
	}

	public void replan() {
		AgentInfo agentInfo = AgentInfo.getInstance();
		AgentModel agentModel = AgentModel.getInstance();
		Vector3D kickTarget = agentInfo.kickTarget();

		if (EnvironmentModel.beamablePlayMode()) {
			agentInfo.loguj("Beam");
			Vector3D start_position = Vector3D.cartesian(-5, 1, 0.4);
			Highskill_Queue.add(new Beam(start_position));
			this.beamed = true;
		} else if (agentModel.isOnGround() || agentModel.isLyingOnBack()
				|| agentModel.isLyingOnBelly()) {
			agentInfo.loguj("GetUp");
			Highskill_Queue.add(new GetUp(new LambdaCallable() {
				public boolean call() {
					return ! AgentModel.getInstance().isOnGround();
				}
			}));
		} else if (!WorldModel.getInstance().isSeeBall()) {
			agentInfo.loguj("Localize");
			Highskill_Queue.add(new Localize(new LambdaCallable() {
				public boolean call() {
					return ! WorldModel.getInstance().isSeeBall();
				}
			}));
		} else if (!is_ball_mine() || straight()) {
			agentInfo.loguj("Walk");
			Highskill_Queue.add(new Walk2Ball());
		} else if (!turned_to_goal()) {
			agentInfo.loguj("Turn");
			Highskill_Queue.add(new Turn(kickTarget));
		} else if (is_ball_mine() && straight() && turned_to_goal()) {
			agentInfo.loguj("Kick");
			Highskill_Queue.add(new Kick(kickTarget));
		} else {
			agentInfo.loguj("???");
			Highskill_Queue.add(new GetUp(new LambdaCallable() {
				public boolean call() {
					return ! AgentModel.getInstance().isOnGround();
				}
			}));
		}
	}
}
