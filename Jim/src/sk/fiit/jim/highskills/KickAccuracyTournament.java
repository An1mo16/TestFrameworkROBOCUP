package sk.fiit.jim.highskills;

import org.apache.commons.logging.Log;

import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.jim.agent.models.AgentModel;
import sk.fiit.jim.agent.models.EnvironmentModel;
import sk.fiit.jim.agent.models.WorldModel;
import sk.fiit.jim.agent.moves.LowSkill;
import sk.fiit.jim.agent.moves.LowSkills;
import sk.fiit.jim.agent.skills.HighSkill;
import sk.fiit.robocup.library.geometry.Vector3D;

/*
 * 
 * Reimplemented from Ruby to Java by Michal Blanarik
 */
public class KickAccuracyTournament extends HighSkill{

	private AgentInfo agentInfo;
	private AgentModel agentModel;
	private WorldModel worldModel;
	private Vector3D kickTarget;
	
	public KickAccuracyTournament(Vector3D kick_target) {
		agentInfo = AgentInfo.getInstance();
		agentModel = AgentModel.getInstance();
		worldModel = WorldModel.getInstance();
		kickTarget = kick_target;
	}
	
	@Override
	public LowSkill pickLowSkill() {
		Vector3D ballPos = agentInfo.ballControlPosition();
		
		System.out.println(ballPos.getY());
		System.out.println(ballPos.getX());

		if(EnvironmentModel.beamablePlayMode() && !EnvironmentModel.isKickOffPlayMode()) {
			System.out.println("beam");
			return null;
		}
		else if(agentModel.falled()) {
			System.out.println("fall");
			return null;
		} 
		else if((EnvironmentModel.SIMULATION_TIME - worldModel.getBall().getLastTimeSeen()) >3) {
			System.out.println("ball unseen");
			return null;
		}
		else if((ballPos.getY() > 0.7) || (ballPos.getY() < -0.7)) {
			System.out.println("very big Y");
			return null;
		}
		else if((ballPos.getX() > 0.7) || (ballPos.getX() < -0.7)) {
			System.out.println("very big X");
			return null;
		}
		else if(ballPos.getY() < 0.7 && ballPos.getY() > 0.3) {
			System.out.println("go a bit");
			return LowSkills.get("walk_slow2");
		}
		else if(ballPos.getX() < 0.1 && ballPos.getX() > 0.3) {
			System.out.println("big X");
			return LowSkills.get("step_right");
		}
		else if(ballPos.getX() < -0.1 && ballPos.getX() > -0.7) {
			System.out.println("small X");
			return LowSkills.get("step_left");
		}
		else if(ballPos.getX() > 0.0) {
			System.out.println("kick right");
			//Smell #SmellType(NeverUsed) | Michal-PC/Michal 2013-12-10T21:22:52.1230000Z 
			Double ballR = ballPos.getR();			//asi nepotrebne nikde sa to nevyuziva
			kickDist("right", ballPos);
		}
		else if(ballPos.getX() < 0.0) {
			System.out.println("kick left");
			//Smell #SmellType(NeverUsed) | Michal-PC/Michal 2013-12-10T21:23:51.5270000Z 
			Double ballR = ballPos.getR();			//asi nepotrebne nikde sa to nevyuziva
			kickDist("left", ballPos);
		}
		else {
			System.out.println("???");
			return null;
		}
		return null;
	}
	
	private void kickDist(String leg, Vector3D ballPos) {
		//Smell #SmellType() | Michal-PC/Michal 2013-12-10T21:33:32.1420000Z 
		ballPos = worldModel.getBall().getPosition();   //neviem naco sem do funkcie prisiel vector ballPos ked ho berem zase
		System.out.println("vzdialenost");
		
		Double kickTargetDist = agentInfo.calculateDistance(ballPos, kickTarget);
		System.out.println(kickTargetDist);
		
		if(kickTargetDist > 4 && leg.equalsIgnoreCase("right")) {
			System.out.println("faster");
			LowSkills.get("kick_step_strong_right");
		}
		else if(kickTargetDist > 4 && leg.equalsIgnoreCase("left")) {
			System.out.println("faster");
			LowSkills.get("kick_step_strong_left");
		}
		else if(kickTargetDist <= 4 && kickTargetDist > 1.5 && leg.equalsIgnoreCase("right")) {
			System.out.println("fast");
			LowSkills.get("kick_right_faster");
		}
		else if(kickTargetDist <= 4 && kickTargetDist > 1.5 && leg.equalsIgnoreCase("left")) {
			System.out.println("fast");
			LowSkills.get("kick_left_faster");
		}
		else if(kickTargetDist <= 1.5 && kickTargetDist > 0.5 && leg.equalsIgnoreCase("right")) {
			System.out.println("normal");
			LowSkills.get("kick_right_normal");
		}
		else if(kickTargetDist <= 1.5 && kickTargetDist > 0.5 && leg.equalsIgnoreCase("left")) {
			System.out.println("normal");
			LowSkills.get("kick_left_normal");
		}
	}

	@Override
	public void checkProgress() throws Exception {
		//Todo #Task() #Solver() #Priority(Normal) | Michal-PC/Michal 2013-12-10T21:24:03.7720000Z 
		// TODO Auto-generated method stub
		
	}

}
