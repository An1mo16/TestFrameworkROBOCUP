package sk.fiit.jim.agent.models;

import java.io.Serializable;

import sk.fiit.robocup.library.geometry.Vector3D;

/**
 *  DynamicObject.java
 *  
 *  Class determines the position of a dynamic object, usually a ball.
 *  TODO: implement calculation of opponent position.
 *
 *@Title	Jim
 *@author	marosurbanec
 *@author	Androids
 */
public class DynamicObject implements Serializable {
	private static final long serialVersionUID = -7626479362047381472L;
	
	private static double LOW_TIME = 0.1;
	private Vector3D position = Vector3D.ZERO_VECTOR;
	private Vector3D relativePosition = Vector3D.ZERO_VECTOR;
	private Vector3D speed = Vector3D.ZERO_VECTOR;
	private Vector3D relativeSpeed = Vector3D.ZERO_VECTOR;
	private double lastTimeSeen = 0.0;
	private Vector3D predikcia = Vector3D.ZERO_VECTOR;
	
	//added by team17
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public DynamicObject clone() {
		DynamicObject newObject = new DynamicObject();
		newObject.position = this.position.clone();
		newObject.relativePosition = this.position.clone();
		newObject.speed = this.speed.clone();
		newObject.relativeSpeed = this.relativeSpeed.clone();
		newObject.lastTimeSeen = this.lastTimeSeen;
		return newObject;
	}

	//added by Androids
	/**
	 * Sets relative position of dynamic object according to specified
	 * relative position and time seen. 
	 *
	 * @param relativePos
	 * @param timeSeen
	 */
	public void setRelativePosition(Vector3D relativePos, double timeSeen){
		double elapsedTime = timeSeen - lastTimeSeen;
		if (this.relativePosition != Vector3D.ZERO_VECTOR)
			relativeSpeed = relativePos.subtract(this.relativePosition).divide(elapsedTime);
		this.relativePosition = relativePos;
	}
	
	/**
	 * Sets absolute position of dynamic object according to specified
	 * absolute position and time seen. 
	 *
	 * @param absolutePosition
	 * @param timeSeen
	 */
	public void setPosition(Vector3D absolutePosition, double timeSeen){
		double elapsedTime = timeSeen - lastTimeSeen;
		if (this.position != null)
			speed = absolutePosition.subtract(this.position).divide(elapsedTime);
		this.lastTimeSeen = timeSeen;
		this.position = absolutePosition;
	}
	
	/**
	 * Returns absolute position of dynamic object. 
	 *
	 * @return
	 */
	public Vector3D getPosition(){
		return position;
	}

	//added by Androids
	/**
	 * Returns relative position of dynamic object(relative to current agent model). 
	 *
	 * @return
	 */
	public Vector3D getRelativePosition(){
		if (notSeenLong())
			return AgentModel.getInstance().relativize(position);
		return relativePosition;
	}
	
	//added by Androids
	/**
	 * Returns true if simulation time subtracted by last time seen value is 
	 * greater than LOW_TIME constant, otherwise false.
	 *
	 * @return
	 */
	public boolean notSeenLong() {
		return EnvironmentModel.SIMULATION_TIME - lastTimeSeen > LOW_TIME;
	}
	
	//added by Androids
		/**
		 * Returns simulation time subtracted by last time seen value.
		 *
		 * @return
		 */
		public double notSeenLongTime() {
			return EnvironmentModel.SIMULATION_TIME - lastTimeSeen;
		}
	
	//add tomasblaho
		/**
		 * Gets prediction of dynamic object's future position. 
		 *
		 * @return
		 */
		public Vector3D getPrediction() {
			return predikcia;
		}
		
		//add tomasblaho
		public void setPrediction(Vector3D predikcia) {
			this.predikcia = predikcia;
		}
	
	//added by Androids
	//commented by Juraj Belanji - not sure it works
/*	public Vector3D predictPositionIn(double offset){
		if (offset < 0)
			throw new IllegalArgumentException(String.format("Negative offset supplied: %.2f", offset));
		return speed.multiply(offset).add(position);
	}
	
	public Vector3D predictRelativePositionIn(double offset){
		AgentModel agentModel = AgentModel.getInstance();
		
		Vector3D withUnsyncedRotations = predictPositionIn(offset).
			rotateOverX(-agentModel.getRotationX()).
			rotateOverY(-agentModel.getRotationY()).
			rotateOverZ(-agentModel.getRotationZ());
		return withUnsyncedRotations.subtract(agentModel.getPosition());
	}*/
	
	/**
	 * Returns last time when dynamic object was seen. 
	 *
	 * @return
	 */
	public double getLastTimeSeen(){
		return lastTimeSeen;
	}
	
	/**
	 * Returns dynamic object's absolute speed.
	 *
	 * @return
	 */
	public Vector3D getSpeed() {
		return speed;
	}
	
	/**
	 * Return dynamic object's relative speed. 
	 *
	 * @return
	 */
	public Vector3D getRelativeSpeed() {
		return relativeSpeed;
	}
	
	@Override
	public String toString(){
		return new StringBuilder().
			append("Absolute: ").
			append(position).
			append(' ').
			append("Relative: ").
			append(relativePosition).
			toString();
	}
}