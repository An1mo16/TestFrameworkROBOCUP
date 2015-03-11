package sk.fiit.jim.plan;

/* HIGHSKILL NEEDED: 
 * 1 to pick a LOWSKILL
 */

import sk.fiit.jim.LambdaCallable;
import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.jim.agent.models.AgentModel;
import sk.fiit.jim.agent.models.EnvironmentModel;
import sk.fiit.jim.highskills.*;
import sk.fiit.robocup.library.geometry.Vector3D;

/**
 * Rewrited PlanTournamentGetUpFromBack from ruby
 * 
 * @author Adamik
 */

public class PlanTournamentGetUpFromBack extends Plan {

	
	public static PlanTournamentGetUpFromBack getInstance() {
		return new PlanTournamentGetUpFromBack();
	}

	boolean in_falled_pose 	= false;
	boolean up 				= false;

	public void replan() {
           AgentModel agentModel = AgentModel.getInstance();
           
		if(!beamed && !agentModel.falled() && !in_falled_pose && !up && EnvironmentModel.beamablePlayMode()) {
			AgentInfo.logState("Beam, wait for Fall");
			beamed 			= true;
			Vector3D start_position = Vector3D.cartesian(-5, 1, 0.4);
			Highskill_Queue.add(new Beam(start_position));
		}
		else if(beamed && !agentModel.falled() && !in_falled_pose && !up && EnvironmentModel.beamablePlayMode()) {
			AgentInfo.logState("Fall, wait for To falled pose");
			// falled 			= true;
			// @plan << LowSkill.new("fall_back")
		}
		else if(beamed && agentModel.falled() && !in_falled_pose && !up && EnvironmentModel.beamablePlayMode()) {
			AgentInfo.logState("To falled pose, after that I am ready for GetUp");
			in_falled_pose	= true;
			// @plan << LowSkill.new("rollback2")
		}
		else if(beamed && agentModel.falled() && in_falled_pose && !up && !EnvironmentModel.beamablePlayMode()) {
			AgentInfo.logState("GetUp, wait for Stand");
			up 				= true;
			Highskill_Queue.add(new GetUp(new LambdaCallable() {
				public boolean call() {
					return ! AgentModel.getInstance().isOnGround();
				}
			}));
		}
		else if(beamed && agentModel.falled() && in_falled_pose && up && !EnvironmentModel.beamablePlayMode()) {
			AgentInfo.logState("Stand");
			// @plan << LowSkill.new("rollback")
		}
		else if(beamed && agentModel.falled() && in_falled_pose && up && EnvironmentModel.beamablePlayMode()) {
			beamed 			= false;
			// falled 			= false;
			in_falled_pose 	= false;
			up 				= false;
		}
	}
}
