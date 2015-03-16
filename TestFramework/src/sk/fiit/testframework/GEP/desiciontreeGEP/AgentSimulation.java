package sk.fiit.testframework.GEP.desiciontreeGEP;

import sk.fiit.robocup.library.geometry.Point3D;
import sk.fiit.robocup.library.geometry.Vector3;
import sk.fiit.testframework.communication.agent.AgentJim;

public class AgentSimulation implements Runnable {
	private Vector3 InitPos;
	private Vector3 ActPos;
	private boolean IsStatic;
	private AgentJim Agent;
	
	public AgentSimulation(boolean isStatic, Vector3 init, AgentJim agent){
		ActPos = InitPos = init;
		IsStatic = isStatic;
		Agent = agent; 
	}
	
	public Vector3 getPosition(){
		return ActPos;
	}

	@Override
	public void run() {
		Agent.setCoordinates(ActPos);
		if(!IsStatic){
			
		}
	}
}
