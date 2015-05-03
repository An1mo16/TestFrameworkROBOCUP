package sk.fiit.testframework.gp.desiciontreegp;

import sk.fiit.robocup.library.geometry.Point3D;
import sk.fiit.robocup.library.geometry.Vector3;
import sk.fiit.testframework.communication.agent.AgentJim;
/**
 * 
 * @author Július Skrisa
 *
 */
public class AgentSimulation{
	private Vector3 InitPos;
	private Vector3 DifVector;
	private Vector3 ActPos;
	private boolean IsStatic;
	private AgentJim Agent;
	private double ActTimeOfSimulation; 
	private double TimeOfSimulation; 
	private double SendDataTimeStep;
	private int DataSentCounter = 1;
	
	/*
	 * Vytvorenie dynamickej prekazky
	 */
	public AgentSimulation(boolean isStatic, Vector3 initPos,  AgentJim agent, double sendDataTimeStep, Vector3 finalPos, double timeOfSimulation ){
		ActPos = InitPos = initPos;
		DifVector = finalPos.subtract(InitPos);
		IsStatic = isStatic;
		Agent = agent; 
		ActTimeOfSimulation = 0;
		TimeOfSimulation = timeOfSimulation;
		SendDataTimeStep = sendDataTimeStep;
	}
	
	 /*
	  * Vytvorenie statickej prekazky
	  */
	public AgentSimulation(boolean isStatic, Vector3 initPos,  AgentJim agent, double sendDataTimeStep){
		ActPos = InitPos = initPos;
		IsStatic = isStatic;
		Agent = agent; 
		ActTimeOfSimulation = 0;
		SendDataTimeStep = sendDataTimeStep;
	}
	
	/*
	 * Vrati akutalnu poziciu simulacneho hraca pola casu testu
	 */
	public Vector3 getPosition(double simulationTime){
		if(!IsStatic){
			ActTimeOfSimulation = simulationTime;
			ActPos = CalculatePosition();
			
			
		}
		if(simulationTime > SendDataTimeStep*DataSentCounter){
			Agent.setCoordinates(ActPos);
			DataSentCounter++;
		}

		return ActPos;
	}
	
	public void setTimeOfSimulation(double time){
		TimeOfSimulation = time;
	}
	
	private Vector3 CalculatePosition(){
		double actPart = ActTimeOfSimulation / TimeOfSimulation;
		
		Vector3 result = new Vector3();
		result.setX( 
				InitPos.getX() + (DifVector.getX() * actPart)
				);
		result.setY( 
				InitPos.getY() + (DifVector.getY() * actPart)
				);
		result.setZ( 
				InitPos.getZ() + (DifVector.getZ() * actPart)
				);
		
		return result;
	}
}
