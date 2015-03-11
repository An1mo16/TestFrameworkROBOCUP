package sk.fiit.jim.agent.trajectory;

import java.util.LinkedList;

import sk.fiit.jim.annotation.data.Annotation;
/**
 * 
 *  Trajectory.java
 *  This class represents trajectory as queue of the move names.
 *  Contains also the acceptable deviation used in planning this trajectory.
 *  
 *@Title        Jim
 *@author       $Author: Roman Bilevic $
 */
public class Trajectory {
	LinkedList<Annotation> moveList;
	double angleDeviation;

	/**
	 * Constructs object of this class with given deviation.
	 * 
	 * @param angleDeviation
	 */
	public Trajectory(double angleDeviation){
		moveList = new LinkedList<Annotation>();
		this.angleDeviation = angleDeviation;
	}
	
	/**
	 * 
	 * Adds move at the of the trajectory. 
	 *
	 * @param move
	 */
	public void addMove(Annotation a){
		this.moveList.add(a);
	}
	
	/**
	 * 
	 * Joins given trajectory at the end of this trajectory. 
	 *
	 * @param trajectory
	 */
	public void addList(LinkedList<Annotation> a){
		this.moveList.addAll(a);
	}
	
	/**
	 * 
	 * Prints the whole trajectory on standard output.
	 * Test purposes. 
	 *
	 */
	public void print(){
		for(Annotation a : moveList){
			System.out.println(a.getName());
		}
		System.out.println(getTime()/1000);  // time in seconds
	}
	
	/**
	 * 
	 * Calculates duration of the whole trajectory.
	 *
	 * @return duration (ms)
	 */
	public double getTime(){
		double time = 0;
		for(Annotation a : moveList){
			time += a.getDuration().getAvg();
		}
		return time;
	}
	
	/**
	 * Returns the annotation of the first move in the trajectory and removes it.
	 *
	 * @return first annotation in trajectory
	 */
	public Annotation poll(){
		Annotation a = this.moveList.poll();
		return a;
	}
	
	/**
	 * Returns the annotation of the first move in the trajectory and leaves it in the queue.
	 *
	 * @return first annotation in trajectory
	 */
	public Annotation peek(){
		Annotation a = this.moveList.peek();
		return a;
	}
	
	/**
	 * 
	 * Returns angle deviation of this trajectory. 
	 *
	 * @return angle deviation
	 */
	public double getDeviation(){
		return this.angleDeviation;
	}
}
