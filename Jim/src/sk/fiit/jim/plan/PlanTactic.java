package sk.fiit.jim.plan;


import sk.fiit.jim.LambdaCallable;
import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.jim.agent.models.AgentModel;
import sk.fiit.jim.agent.models.EnvironmentModel;
import sk.fiit.jim.agent.models.TacticalInfo;
import sk.fiit.jim.agent.models.WorldModel;
import sk.fiit.robocup.library.geometry.Vector3D;
import sk.fiit.jim.highskills.*;
import sk.fiit.jim.log.Log;
import sk.fiit.jim.log.LogType;

/**
 * Rewrited PlanTactic from ruby 
 * 
 * @author Benkovic
 */

/* HIGHSKILL NEEDED: 
 * 1 to pick a LOWSKILL
 * 2 FormationHelper
*/

public class PlanTactic extends Plan {
     
	// BASIC CALL IN jim.agent.Planner.java
	public static PlanTactic getInstance() {
		return new PlanTactic();
	}
	//END
	  
      
	public void replan()  {
		AgentInfo agentInfo = AgentInfo.getInstance();
	    AgentModel agentModel = AgentModel.getInstance();
	    Vector3D kick_target = agentInfo.kickTarget();
	    TacticalInfo tacticalInfo = TacticalInfo.getInstance();
	     
		Log.log(LogType.PLANNING, "v replan plan tactic");
		/*if (EnvironmentModel.beamablePlayMode()){
			agentInfo.loguj("Beam");
			Vector3D start_position = Vector3D.cartesian(-5, 1, 0.4);
			Highskill_Queue.add(new Beam(start_position));
			this.beamed=true;
		}
		else */if (agentModel.isOnGround() || agentModel.isLyingOnBack() || agentModel.isLyingOnBelly()){
			agentInfo.loguj("GetUp");
			Highskill_Queue.add(new GetUp(new LambdaCallable() {
				public boolean call() {
					return ! AgentModel.getInstance().isOnGround();
				}
			}));
		}
		else if (! WorldModel.getInstance().isSeeBall()){
			Highskill_Queue.add(new Localize(new LambdaCallable() {
				public boolean call() {
					return ! WorldModel.getInstance().isSeeBall();
				}
			}));
		}
		
//////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////// 1. If someone doesn't hold the ball!!!! ///////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////
		
		// If no one have a ball + I am closest to the ball///
		else if (!agentInfo.isBallMine()  && !tacticalInfo.isBallOwnedByUs() && !tacticalInfo.isBallOwnedByThem() && tacticalInfo.isBallNearestToMeInMyTeam()){
			agentInfo.loguj("1/Walk to ball");
			Highskill_Queue.add(new Walk2Ball());
		}
		
		// If no one have a ball + I am NOT closest to the ball//
		else if (!agentInfo.isBallMine()  && !tacticalInfo.isBallOwnedByUs() && !tacticalInfo.isBallOwnedByThem() && !tacticalInfo.isBallNearestToMeInMyTeam())
		{
			tacticalInfo.setMyFormPosition();
			if (tacticalInfo.isInPositionArea()){
				agentInfo.loguj("1/On position");
				//LowSkills.get("rollback");
				//HighSkill low ;
				//Highskill_Queue.add(low);
				////////////////////// ???????????????//////////////////////////
				
			}
			else
			{
				agentInfo.loguj("1/Go to formation");
				//@plan << FormationHelper.getHighSkillToGoToFormation(Proc.new{see_ball? and not is_ball_mine? and not @is_ball_owned_by_us and not @is_ball_owned_by_them and not @is_ball_nearest_to_me_in_my_team})
				//Highskill_Queue.add(new FormationHelper());
			}
		}
			
//////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////// 2. IF THEY HAVE BALL !!!! ////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////
			
			// if they have ball + I am closest to the ball
		else if (!agentInfo.isBallMine() && tacticalInfo.isBallOwnedByThem() && tacticalInfo.isBallNearestToMeInMyTeam())
		{
			agentInfo.loguj("2/Walk to ball");
			Highskill_Queue.add(new Walk2Ball());
		}
			
			// if they have ball + I am NOT closest to the ball
		else if (!agentInfo.isBallMine() && tacticalInfo.isBallOwnedByThem() && !tacticalInfo.isBallNearestToMeInMyTeam())
		{
			tacticalInfo.setMyFormPosition();
			if (tacticalInfo.isInPositionArea()){
				agentInfo.loguj("2/On position");
				//LowSkills.get("rollback");
				//HighSkill low ;
				//Highskill_Queue.add(low);
				////////////////////// ???????????????//////////////////////////
				
			}
			else
				agentInfo.loguj("2/Go to formation");
				//@plan << FormationHelper.getHighSkillToGoToFormation(Proc.new{see_ball? and not is_ball_mine? and @is_ball_owned_by_them and not @is_ball_nearest_to_me_in_my_team})
	    	
		}
		
//////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////// 3. IF WE HAVE BALL !!!! //////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////
		
		// if We have ball + I do not have ball
		
		else if (!agentInfo.isBallMine() && !tacticalInfo.isBallOwnedByUs()){
			tacticalInfo.setMyFormPosition();
			if (tacticalInfo.isInPositionArea()){
				agentInfo.loguj("3/On position");
				//LowSkills.get("rollback");
				//HighSkill low ;
				//Highskill_Queue.add(low);
				////////////////////// ???????????????//////////////////////////
				
			}
			else
			{
				agentInfo.loguj("3/Go to formation");
				//@plan << FormationHelper.getHighSkillToGoToFormation(Proc.new{see_ball? and not is_ball_mine? and @is_ball_owned_by_us})
			}
		}
		
		// ive got a ball + I'm not rotated on ball
		else if (agentInfo.isBallMine() && !straight()){
			agentInfo.loguj("3/Walk to straight");
			Highskill_Queue.add(new Walk2Ball());
		}
		// ive got a ball + I'm not rotated on goal
		else if (agentInfo.isBallMine() && !turned_to_goal()){
			agentInfo.loguj("3/Turn to goal");
			Highskill_Queue.add( new Turn(kick_target));
			
		}
		// ive got a ball + I'm rotated on goal +  I'm rotated on ball
		else if (agentInfo.isBallMine() && turned_to_goal() && straight()){
			agentInfo.loguj("3/Kick");
			Highskill_Queue.add( new Kick(kick_target));
		}
		
		
//////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////// Unhandled issue !!!! //////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////
		else {
			agentInfo.loguj("Unhandled issue");
			Highskill_Queue.add( new GetUp(new LambdaCallable() {
				public boolean call() {
					return ! AgentModel.getInstance().isOnGround();
				}
			}));
		}
			
	}
	
}
