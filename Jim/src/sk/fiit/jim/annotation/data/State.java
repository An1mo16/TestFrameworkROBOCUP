package sk.fiit.jim.annotation.data;

import java.util.ArrayList;

import sk.fiit.robocup.library.geometry.Circle;

/** 
 * @author: Roman Bilevic
 */

public class State {
	/**
	 * 
	 */
	String lying = "";
	boolean b_pos = false;
	Axis ball = new Axis();
	ArrayList<Joint> joints = new ArrayList<Joint>();
	private Circle ballPosCircle = new Circle();
	
	public String getLying() {
		return lying;
	}
	public void setLying(String lying) {
		this.lying = lying;
	}
	public boolean isB_pos() {
		return b_pos;
	}
	public void setB_pos(boolean b_pos) {
		this.b_pos = b_pos;
	}
	public Axis getBall() {
		return ball;
	}
	public void setBall(Axis ball) {
		this.ball = ball;
	}
	public ArrayList<Joint> getJoints() {
		return joints;
	}
	public void setJoints(ArrayList<Joint> joints) {
		this.joints = joints;
	}
	public void setBallPosCircle(Circle ballPosCircle) {
		this.ballPosCircle = ballPosCircle;
	}
	public Circle getBallPosCircle() {
		return ballPosCircle;
	}
	
	@Override
	public String toString() {
		return "State [lying=" + lying + ", b_pos=" + b_pos + ", ball=" + ball
				+ ", joints=" + joints + ", ballPosCircle=" + ballPosCircle
				+ "]";
	}
	
	
}