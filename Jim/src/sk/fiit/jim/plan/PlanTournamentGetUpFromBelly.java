package sk.fiit.jim.plan;

/**
 * Rewrited PlanTournamentGetUpFromBelly from ruby
 * 
 * @author Petras
 */

/* HIGHSKILL NEEDED: 
 * 1 to pick a LOWSKILL
*/

import sk.fiit.jim.LambdaCallable;
import sk.fiit.jim.agent.models.AgentModel;
import sk.fiit.jim.agent.models.EnvironmentModel;
import sk.fiit.jim.highskills.*;
import sk.fiit.robocup.library.geometry.Vector3D;

public class PlanTournamentGetUpFromBelly extends Plan{
	
	public static PlanTournamentGetUpFromBelly getInstance() {
		return new PlanTournamentGetUpFromBelly();
	}
	
	      boolean in_falled_pose = false;
		  	boolean up = false;
	
          public void replan(){
              AgentModel agentModel = AgentModel.getInstance();
              
        	 
        	  if(!beamed && !agentModel.falled() && in_falled_pose==false && up==false && EnvironmentModel.beamablePlayMode()){
        		  
        		  
        		  
        			beamed = true;
        			Vector3D start_position = Vector3D.cartesian(-5, 1, 0.4);
        			Highskill_Queue.add(new Beam(start_position));
        		  
        	  }else if(beamed && !agentModel.falled()  && in_falled_pose==false && up==false  && EnvironmentModel.beamablePlayMode()){
        		  
        		  
        		
        		//	@plan << LowSkill.new("fall_front")
        		  	  
        	  }else if(beamed && agentModel.falled() && in_falled_pose==false && up==false  && EnvironmentModel.beamablePlayMode() ){
        		  
        		  
        		  in_falled_pose = true;
        				//	@plan << LowSkill.new("rollback2")
        	  
        	  }else if(beamed && agentModel.falled() && in_falled_pose==true && up==false  && EnvironmentModel.beamablePlayMode()){
        		  
        		  
        			up = true;
        			Highskill_Queue.add(new GetUp(new LambdaCallable() {
        				public boolean call() {
        					return ! AgentModel.getInstance().isOnGround();
        				}
        			}));
        		  
        	  
        	  }else if(beamed  && agentModel.falled() && in_falled_pose==true && up==true  && !EnvironmentModel.beamablePlayMode()){
        		  
        		 //	@plan << LowSkill.new("rollback")
        		  
        	  }else if(beamed && agentModel.falled()  && in_falled_pose==true && up==true && EnvironmentModel.beamablePlayMode()){
        		  
        		  
        		  
        		            beamed = false;
        			  		in_falled_pose = false;
        			  		up = false; 
        		  
        		  
        		  
        	  }
        	 
      	  
        	  
          }


}
