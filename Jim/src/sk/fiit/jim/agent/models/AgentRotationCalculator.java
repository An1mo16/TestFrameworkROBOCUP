package sk.fiit.jim.agent.models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static sk.fiit.jim.log.LogType.AGENT_MODEL;
import static java.lang.Math.asin;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;

import sk.fiit.jim.agent.parsing.ParsedData;
import sk.fiit.jim.log.Log;
import sk.fiit.robocup.library.geometry.Angles;
import sk.fiit.robocup.library.geometry.Vector3D;

/**
 *  AgentRotationCalculator.java
 *  
 *  Calculates rotation of the agent based on the fixed points received.
 *
 *@Title	Jim
 *@author	marosurbanec
 *@author	Androids
 */
class AgentRotationCalculator{
	private static final int FLAGS_TO_COMPUTE_ROTATION = 3;
	private final AgentModel agent;

	/**
	 * Sets current AgentModel.
	 * @param agent
	 */
	public AgentRotationCalculator(AgentModel agent){
		this.agent = agent;
	}
	
	/**
	 *  Updates agent's rotation in current AgentModel from
	 * last received data from server by calculation using fixed
	 * objects(flags) and agents rotation and position.
	 *
	 * @param data
	 */
	public void updateRotations(ParsedData data){
		Map<Vector3D, Vector3D> sameSideFlags = getFlagsOfSideWithMoreFlagsSeen(data.fixedObjects);
		//3 flags are needed in order to infer our rotation
		if (sameSideFlags.size() >= FLAGS_TO_COMPUTE_ROTATION){
			List<Vector3D> absolute = new ArrayList<Vector3D>();
			List<Vector3D> seen = new ArrayList<Vector3D>();
			for (Entry<Vector3D, Vector3D> flag : sameSideFlags.entrySet()){
				absolute.add(flag.getKey());
				seen.add(flag.getValue());
			}
			calculateRotation(absolute, seen);
		}
	}
	
	private Map<Vector3D, Vector3D> getFlagsOfSideWithMoreFlagsSeen(Map<FixedObject, Vector3D> fixedObjects){
		Map<Vector3D, Vector3D> ourSide = new LinkedHashMap<Vector3D, Vector3D>();
		Map<Vector3D, Vector3D> theirSide = new LinkedHashMap<Vector3D, Vector3D>();
		if (fixedObjects == null || fixedObjects.size() < FLAGS_TO_COMPUTE_ROTATION)
			return ourSide;
		
		for (Entry<FixedObject, Vector3D> flag : fixedObjects.entrySet()){
			boolean isOnOurSide = flag.getKey().getAbsolutePosition().getX() == FixedObject.OUR_LOWER_CORNER.getAbsolutePosition().getX();
			if (isOnOurSide)
				ourSide.put(flag.getKey().getAbsolutePosition(), flag.getValue());
			else
				theirSide.put(flag.getKey().getAbsolutePosition(), flag.getValue());
		}
		return theirSide.size() > ourSide.size() ? theirSide : ourSide;
	}
	
	/**
	 * Infers agent's rotation from 3 seen flags.
	 * 
	 * @param absolute Global positions of seen flags
	 * @param seen Perceived positions of seen flags
	 */
	private void calculateRotation(List<Vector3D> absolute, List<Vector3D> seen){
		int[] order = orderToFormAxes(absolute);
		Vector3D y1 = seen.get(order[0]);
		Vector3D y2 = seen.get(order[1]);
		Vector3D y2MinusY1 = y2.subtract(y1);
		Vector3D yAxis = y2MinusY1.negate().toUnitVector();
		
		Vector3D z1 = seen.get(order[2]);
		/*
		 * 	this.agent is how we get closest point on a line [Y1, Y2] to a point Z_known
		 * 
		 * 								(Y_2 - Y_1) x (Z_known - Y_1) x (Y_2 - Y_1)
		 * 		Z_needed = Z_known -  -------------------------------------------------
		 * 										(Y_2 - Y_1) . (Y_2 - Y_1)
		 */
		Vector3D z2 = z1.subtract(y2MinusY1.crossProduct(z1.subtract(y1)).crossProduct(y2MinusY1).divide(y2MinusY1.dotProduct(y2MinusY1)));
		Vector3D zAxis = z1.subtract(z2).toUnitVector();
		
		if (absolute.get(order[2]).getZ() == 0.0)
			zAxis = zAxis.negate();
			
		Vector3D xAxis = yAxis.crossProduct(zAxis).toUnitVector();
		
		double rotationX = asin(-xAxis.getZ());
		double rotationZ = atan2( -xAxis.rotateOverX(rotationX).getY() / cos(rotationX), xAxis.rotateOverX(rotationX).getX() / cos(rotationX));
		double rotationY = atan2( -yAxis.rotateOverX(rotationX).getZ() / cos(rotationX), zAxis.rotateOverX(rotationX).getZ() / cos(rotationX));
		this.agent.rotationX = Angles.normalize(rotationX);
		this.agent.rotationY = Angles.normalize(rotationY);
		this.agent.rotationZ = Angles.normalize(rotationZ);
		Log.log(AGENT_MODEL, "My rotation: [%.2f,%.2f,%.2f]", rotationX, rotationY, rotationZ);
	}
	
	/**
	 * Of 3 flags, determine which two of them to use to form Y axis
	 * (0. and 1. member) and which one is a complementary point to
	 * form Z axis perpendicular to Y. 
	 */
	private int[] orderToFormAxes(List<Vector3D> absolute){
		Vector3D first = absolute.get(0);
		Vector3D second = absolute.get(1);
		Vector3D third = absolute.get(2);
		
		if(first.getZ() == second.getZ()){
			if (first.getY() > second.getY()) 
				return new int[]{0, 1, 2};
			return new int[]{1, 0, 2};
		}
		
		if(first.getZ() == third.getZ()){
			if (first.getY() > third.getY())
				return new int[]{0, 2, 1};
			return new int[]{2, 0, 1};
		}
		
		if (second.getY() > third.getY())
			return new int[]{1, 2, 0};
		return new int[]{2, 1, 0};
	}
}