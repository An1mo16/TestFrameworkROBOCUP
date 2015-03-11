package sk.fiit.jim.agent.models;

import static java.lang.Math.PI;
import static java.lang.Math.asin;
import static java.lang.Math.hypot;

import java.io.Serializable;

import sk.fiit.robocup.library.geometry.Angles;
import sk.fiit.robocup.library.geometry.Vector3D;


/**
 *  Player.java
 *  
 *  Class that defines agents as players in the game.
 *  
 *@Title	Jim
 *@author	Androids 
 *@author	xjurcako 
 */
public class Player extends DynamicObject implements Serializable {
	private static final long serialVersionUID = -5970968339641094341L;
	
	private int number;
	private boolean isTeammate;		//0 if opponent, 1 if teammate 
	private boolean isInRange = false; 		//0 no, 1 yes
	private WorldModel worldModel;
	//added by xjurcako
	private Vector3D head = Vector3D.ZERO_VECTOR;
	private Vector3D rlowerarm = Vector3D.ZERO_VECTOR;
	private Vector3D llowerarm = Vector3D.ZERO_VECTOR;
	private Vector3D rfoot = Vector3D.ZERO_VECTOR;
	private Vector3D lfoot = Vector3D.ZERO_VECTOR;
	
	private Vector3D absolutehead = Vector3D.ZERO_VECTOR;
	private Vector3D absoluterlowerarm = Vector3D.ZERO_VECTOR;
	private Vector3D absolutellowerarm = Vector3D.ZERO_VECTOR;
	private Vector3D absoluterfoot = Vector3D.ZERO_VECTOR;
	private Vector3D absolutelfoot = Vector3D.ZERO_VECTOR;
	//
	
	
	/**
	 * Create new instance of Player, with specific context.
	 * 
	 * @param context 
	 * @param teammate
	 * @param number
	 */
	public Player(WorldModel context, final boolean teammate, final int number) {
		this.number = number;
		this.isTeammate = teammate;
		this.worldModel = context;
	} 
	//added by xjurcako
	
	/**
	 * Returns true if player is agent's teammate, false otherwise.
	 * 
	 * @return isTeammate
	 */
	public boolean isTeammate(){
		return this.isTeammate;
	}
	//added by xjurcako
	/**
	 * Returns player's head position. 
	 * 
	 * @return the head
	 */
	public Vector3D getHead() {
		return head;
	}
	//addd by Roman Bilevic
	/**
	 * @return absolute position of the head
	 */
	public Vector3D getAbsoluteHead(){
		return absolutehead;
	}
	//added by xjurcako
	/**
	 * @param head the head to set
	 */
	public void setHead(Vector3D head) {
		this.head = head;
		this.absolutehead =	AgentModel.getInstance().globalize(head);
	}
	
	//added by xjurcako
	/**
	 * Returns player's right lower arm position.
	 * 
	 * @return the rlowerarm
	 */
	public Vector3D getRlowerarm() {
		return rlowerarm;
	}
	//added by xjurcako
	/**
	 * @param rlowerarm the rlowerarm to set
	 */
	public void setRlowerarm(Vector3D rlowerarm) {
		this.rlowerarm = rlowerarm;
		this.absoluterlowerarm = AgentModel.getInstance().globalize(rlowerarm);
	}
	//added by xjurcako
	/**
	 * Returns player's left lower arm position.
	 * 
	 * @return the llowerarm
	 */
	public Vector3D getLlowerarm() {
		return llowerarm;
	}
	//added by xjurcako
	/**
	 * @param llowerarm the llowerarm to set
	 */
	public void setLlowerarm(Vector3D llowerarm) {
		this.llowerarm = llowerarm;
		this.absolutellowerarm = AgentModel.getInstance().globalize(llowerarm);
	}
	//added by xjurcako
	/**
	 * Returns player's right foot position.
	 * 
	 * @return the rfoot
	 */
	public Vector3D getRfoot() {
		return rfoot;
	}
	//added by xjurcako
	/**
	 * @param rfoot the rfoot to set
	 */
	public void setRfoot(Vector3D rfoot) {
		this.rfoot = rfoot;
		this.absoluterfoot = AgentModel.getInstance().globalize(rfoot);
	}
	//added by xjurcako
	/**
	 * Returns player's left foot position.
	 * 
	 * @return the lfoot
	 */
	public Vector3D getLfoot() {
		return lfoot;
	}
	//added by xjurcako
	/**
	 * @param lfoot the lfoot to set
	 */
	public void setLfoot(Vector3D lfoot) {
		this.lfoot = lfoot;
		this.absolutelfoot = AgentModel.getInstance().globalize(lfoot);
	}

	/**
	 * Returns player's number.
	 * 
	 * @return number
	 */
	public int getNumber() {
		return number;
	}
	
	/**
	 * Compute rotation according to main x-axis of game field. 
	 *
	 * @return rotation angle in radians
	 */
	public double getAbsoluteRotation()
	{
		//legs of right-angle triangle
		double deltax = this.absolutelfoot.getX() - this.absoluterfoot.getX();
		double deltay = this.absolutelfoot.getY() - this.absoluterfoot.getY();
		
		return computeAngle(deltax, deltay);
	}
	
	/**
	 * Compute rotation of player relative to rotation of AgentModel.
	 *
	 * @return rotation angle in radians
	 */
	public double getRelativeRotation()
	{
		double deltax = this.lfoot.getX() - this.rfoot.getX();
		double deltay = this.lfoot.getY() - this.rfoot.getY();
		
		return computeAngle(deltax, deltay);
	}
	
	/**
	 * This method compute distance between ball and player. Ball position is set in WorldModel.
	 *
	 * @return distance between ball and player
	 */
	public double getDistanceFromBall()
	{
		if(this.worldModel.getBall() == null)
			return 0;
		else
			return DistanceHelper.computeDistanceBetweenObjects(worldModel.getBall().getPosition(), this.getPosition());
	}
	
	/**
	 * For purpose to compute ball distance, only x and y is used for computation.
	 *
	 * @param first
	 * @param second
	 * @return distance between two points
	 */
	private double computeBallDistance(Vector3D first, Vector3D second)
	{
		double deltax = first.getX() - second.getX();
		double deltay = first.getY() - second.getY();
		
		return  hypot(deltax, deltay);
	}
	/**
	 * Compute angle of axis of hypotenuse of right-angle triangle with legs @param deltax and @param deltay.
	 * 			y
	 * 			^
	 * 			|
	 * 			|		dy |\
	 * 			|		   |/\
	 * 			|		   /dx
	 * 			|---------/--------->x 
	 *
	 * @param deltax
	 * @param deltay
	 * @return angel in radians
	 */
	private double computeAngle(double deltax, double deltay) {
		if(deltay == 0)
		{
			if(deltax <= 0)
				return PI/(double)2;
			else
				return 3*(PI/(double)2);
		}
			
		if(deltax == 0)	
		{
			if(deltay <= 0)
				return PI;
			else
				return 0;
		}
		
		//compute distance between lfoot and rfoot. The hypotenus of right-angle triangle
		double c =  hypot(deltax, deltay);
		
		if(deltay>0)
		{
			//range from 0 to 180 degrees
			
			return Angles.normalize(-asin(deltax/c));
		}
		else
		{
			//range from 180 to 360degrees
			return Angles.normalize(PI + asin(deltax/c));
			//return 0;
		}
	}
	//added by xpassakp
	/**
	 * Returns true if player is in agent's range, otherwise false.
	 * 
	 * @return isInRange
	 */
	public boolean getIsInRange() {
		return this.isInRange;
	}
	//added by xpassakp
	/**
	 * @param isInRange to set
	 */
	public void setIsInRange(boolean isInRange) {
		this.isInRange = isInRange;
	}
	
	/**
	 * Method return player distance from his goal.
	 * 
	 * @author xgregorm
	 * @author A55-Kickers
	 * 
	 * @return isNearest - true or false if agent is nearest to his goal
	 */
	public double getDistanceFromMyGoal() {
		double distance = 0;
		
		distance = computeBallDistance(FixedObject.ourPostMiddle(), this.getPosition());
		
		return distance;
	}
}