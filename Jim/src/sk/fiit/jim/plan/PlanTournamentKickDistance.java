package sk.fiit.jim.plan;


import sk.fiit.jim.LambdaCallable;
import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.jim.agent.models.AgentModel;
import sk.fiit.jim.agent.models.EnvironmentModel;
import sk.fiit.jim.agent.models.WorldModel;
import sk.fiit.jim.highskills.Beam;
import sk.fiit.jim.highskills.GetUp;
import sk.fiit.jim.highskills.KickTournament;
import sk.fiit.jim.highskills.Localize;
import sk.fiit.jim.highskills.TurnToPosition;
import sk.fiit.jim.highskills.Walk2BallTournament;
import sk.fiit.robocup.library.geometry.Vector3D;

/**
 * Rewrited PlanTournamentGetUpFromBelly from ruby
 * 
 * @author Matej Badal
 */

public class PlanTournamentKickDistance extends Plan {
	
	public static PlanTournamentKickDistance getInstance() {
		return new PlanTournamentKickDistance();
	}
	
	public void replan() {
		AgentInfo agentInfo = AgentInfo.getInstance();
        AgentModel agentModel = AgentModel.getInstance();
        Vector3D kick_target = agentInfo.kickTarget();
		
		
		if (EnvironmentModel.beamablePlayMode() && !EnvironmentModel.isKickOffLeftPlayMode()) {
			AgentInfo.logState("Beam");
			Vector3D start_position = Vector3D.cartesian(-5, 1, 0.4);
			Highskill_Queue.add(new Beam(start_position));
			this.beamed = true;
		} else if (agentModel.isOnGround() || agentModel.isLyingOnBack()
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
					return ! WorldModel.getInstance().isSeeBall();
				}
			}));
		} else if (!this.is_ball_mine() || !this.straight()) {
			AgentInfo.logState("Walk");
			boolean a = !is_ball_mine() && !this.straight();
			boolean b = EnvironmentModel.beamablePlayMode() && !EnvironmentModel.isKickOffLeftPlayMode();
			Highskill_Queue.add(new Walk2BallTournament(a && !b));
		} else if (!this.turned_to_goal()) {
			AgentInfo.logState("Turn");
			Highskill_Queue.add(new TurnToPosition(kick_target,
					new LambdaCallable() {
						public boolean call() {
							return ! turned_to_goal();
						}
					}
					));
		} else if (this.is_ball_mine() && this.straight() && this.turned_to_goal()) {
			AgentInfo.logState("Kick");
			Highskill_Queue.add(new KickTournament(kick_target));
		} else {
			AgentInfo.logState("???");
			Highskill_Queue.add(new GetUp(new LambdaCallable() {
				public boolean call() {
					return ! AgentModel.getInstance().isOnGround();
				}
			}));
		}
	}
}
