package sk.fiit.jim.agent.models.prediction;

import sk.fiit.robocup.library.geometry.Vector3D;

/**
 *  Prophecy.java
 *  
 *  Holds a probable state of affairs in a given time in future.
 *
 *@Title	Jim
 *@author	marosurbanec
 */
public class Prophecy {
	private Vector3D ballPositionRelativized;
	private Vector3D ballPosition;
	private Vector3D ballRelativePosition;

	public Vector3D getBallPosition(){
		return ballPosition;
	}

	public Vector3D getBallPositionRelativized(){	
		return ballPositionRelativized;
	}
	
	public Vector3D getBallRelativePosition(){
		return ballRelativePosition;
	}

	public void setBallRelativePosition(Vector3D position){
		this.ballRelativePosition = position;
	}

	public void setBallPositionRelativized(Vector3D ballPositionRelativized){
		this.ballPositionRelativized = ballPositionRelativized;
	}

	public void setBallPosition(Vector3D position){
		this.ballPosition = position;
	}
}