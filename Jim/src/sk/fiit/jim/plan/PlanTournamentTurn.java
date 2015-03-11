package sk.fiit.jim.plan;

import sk.fiit.jim.LambdaCallable;
import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.jim.agent.models.*;
import sk.fiit.jim.highskills.*;
import sk.fiit.robocup.library.geometry.Vector3D;

/**
 * Rewrited PlanTournamentGetUpFromBelly from ruby
 * 
 * @author Homola
 */

public class PlanTournamentTurn extends Plan {

	public static PlanTournamentTurn getInstance() {
		return new PlanTournamentTurn();
	}
	
	public void replan() {     
		AgentInfo agentInfo = AgentInfo.getInstance();
        AgentModel agentModel = AgentModel.getInstance();
        Vector3D target_position = agentInfo.ballControlPosition();
		
        if (EnvironmentModel.beamablePlayMode())
        {
        	agentInfo.loguj("Beam");
        	Vector3D start_position = Vector3D.cartesian(-5, 1, 0.4);
			Highskill_Queue.add(new Beam(start_position));
        	beamed = true;
        }
        else if(agentModel.isOnGround() || agentModel.isLyingOnBack() || agentModel.isLyingOnBelly())
        {
        	agentInfo.loguj("GetUp");
			Highskill_Queue.add(new GetUp(new LambdaCallable() {
				public boolean call() {
					return ! AgentModel.getInstance().isOnGround();
				}
			}));
        }
        else
        {
        	agentInfo.loguj("Turn");
        	Highskill_Queue.add(new TurnToPosition(target_position,
        			new LambdaCallable() {
						public boolean call() {
							return ! turned_to_goal();
						}
					}
        	));
        }
	
	}
	
	
}
