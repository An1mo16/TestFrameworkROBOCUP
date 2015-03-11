package sk.fiit.jim.agent.moves;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import sk.fiit.jim.Settings;
import sk.fiit.jim.agent.models.AgentModel;
import sk.fiit.jim.agent.models.EnvironmentModel;
import sk.fiit.jim.log.Log;
import sk.fiit.jim.log.LogType;

/**
 *  JointPlacement.java
 *  
 *  Holds data about desired joint placements and configuration and updates this
 *  state according to executed phases. Class has its own error correction 
 *  algorithm that smoothly adjusts to missed server communication.
 *  
 *  <b>Note:</b> Class expects incoming parameters to be in DEG system 
 *
 *@Title	Jim
 *@author	marosurbanec
 */
class JointPlacement{

	static Map<Joint, Double> anglesToStriveFor = new HashMap<Joint, Double>();
	public static double nextTimePhaseEnds = 0.0;
	public static String lastMessage = "";
	
	static{
		populateAngleTargets();
	}
	
	/**
	 * Initial population of the map - each angle starts at 0.0 degrees
	 */
	private static void populateAngleTargets(){
		for (Joint joint : Joint.values())
			anglesToStriveFor.put(joint, 0.0);
	}
	
	/**
	 * Adjusts target state to newly performed phase
	 * @param activePhase the next phase
	 */
	public static void calculateNewTargetState(Phase activePhase){
		nextTimePhaseEnds = EnvironmentModel.SIMULATION_TIME + activePhase.duration;
		
		for (EffectorData joint : activePhase.effectors)
			anglesToStriveFor.put(joint.effector, joint.endAngle);
	}
	
	/**
	 * 	Transforms desired joint angles into a corresponding server message.
	 * 
	 * @return S-expression formatted message of angular joint movements
	 */
	public static String generateMessage(){
		StringBuilder message = new StringBuilder();
		for (Entry<Joint, Double> wishedConfiguration : anglesToStriveFor.entrySet()){
			double angularSpeed = calculateAngularSpeedFor(wishedConfiguration.getKey());
			message.
				append('(').
				append(wishedConfiguration.getKey().name().toLowerCase()).
				append(' ').
				append(angularSpeed).
				append(')');
		}
		lastMessage = message.toString();
		return lastMessage;
	}

	private static double calculateAngularSpeedFor(Joint joint){
		double actualAngle = AgentModel.getJointAngleOf(joint);
		double finalAngle = anglesToStriveFor.get(joint);
		double now = EnvironmentModel.SIMULATION_TIME;
		
		if (nextTimePhaseEnds == 0.0)
			return 0;
		double angularSpeed = (finalAngle - actualAngle) / (nextTimePhaseEnds - now) * 0.02;
		
		if (angularSpeed > Settings.getDouble("maximumAngularChangePerQuantum"))
			Log.error(LogType.LOW_SKILL, "Too high angular speed for %s, %.2f", joint.toString(), angularSpeed);
		
		return angularSpeed;
	}
	
	@Override
	public String toString(){
		return anglesToStriveFor.toString();
	}
}