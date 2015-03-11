package sk.fiit.jim.agent.trajectory;

import java.util.LinkedList;

import sk.fiit.jim.agent.models.AgentModel;
import sk.fiit.jim.annotation.data.Annotation;
import sk.fiit.robocup.library.geometry.Vector3D;

/**
 * 
 *  TrajectoryRealTime.java
 *  Plans trajectory using "real-time" information from AgentModel and WorldModel.
 *  
 *@Title        Jim
 *@author       $Author: Roman Bilevic $
 */
public class TrajectoryRealTime {
	TrajectoryPlanner tp = null;
	public static TrajectoryRealTime instance = new TrajectoryRealTime();

	/**
	 * 
	 * Plans trajectory using position in form of the vector.
	 *
	 * @param final position of agent
	 * @param final rotation of agent
	 */
	public void plan(Vector3D finalPos, double finalRot){
		setTrajectory(finalPos, finalRot);
	}
	
	/**
	 * 
	 * Plans trajectory using position in form of coordinates. 
	 *
	 * @param x-axis coordinate
	 * @param y-axis coordinate
	 * @param z-axis coordinate
	 * @param finalRot
	 */
	public void plan(double x, double y, double z, double finalRot){
		setTrajectory(Vector3D.cartesian(x, y, z), finalRot);
	}
	
	/**
	 * 
	 * Gets information about actual agent position and rotation, about the obstacles in form of other players
	 * and calls TrajectoryPlanner constructor.
	 *
	 * @param finalPos
	 * @param finalRot
	 */
	private void setTrajectory(Vector3D finalPos, double finalRot){
		Vector3D actualPos = AgentModel.getInstance().getPosition();
		double actualRot = AgentModel.getInstance().getRotationZ();
		
		LinkedList<Vector3D> obstacles = Obstacles.getRealObstacles();
		//obstacles.add(Vector3D.cartesian(-4, 0, 0));
		
		tp = new TrajectoryPlanner(actualPos, actualRot, finalPos, finalRot, obstacles);
	}
	
	/**
	 * 
	 * Returns TrajectoryPlanner object.
	 *
	 * @return trajectory
	 */
	public TrajectoryPlanner getTrajectoryPlanner(){
		return this.tp;
	}
	
	/**
	 * 
	 * Gets name of the next lowskill move in the trajectory and removes it from the queue.
	 *
	 * @return name of the move
	 */
	public String getMoveName(){
		//overenie aktualnej pozicie pre testovacie ucely
		Vector3D actualPos = AgentModel.getInstance().getPosition();
		System.out.println("Position [" + actualPos.getX() + ", " + actualPos.getY() + ", " + actualPos.getZ() + "] " + "phi = " + AgentModel.getInstance().getRotationZ());
		
		//ak je zasobnik pohybov trajektorie prazdny, tak sa vrati null (a nie exception)
		Annotation a = this.tp.getTrajectory().poll();
		if(a == null)
			return null;
		else
			return a.getName();
	}
	
	/**
	 * 
	 * Gets name of the next lowskill move in the trajectory leaving it in queue.
	 * 
	 * @return name of the move
	 */
	public String peekName(){
		Annotation a = this.tp.getTrajectory().peek();
		if(a == null)
			return null;
		else
			return a.getName();
	}
	
	/**
	 * Returns the instance of this class.
	 */
	public static TrajectoryRealTime getInstance(){
		return instance;
	}
}
