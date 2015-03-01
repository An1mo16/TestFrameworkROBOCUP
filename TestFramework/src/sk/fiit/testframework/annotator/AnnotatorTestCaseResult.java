package sk.fiit.testframework.annotator;

import sk.fiit.robocup.library.geometry.Vector3;
import sk.fiit.testframework.trainer.testsuite.TestCaseResult;

/**
 * @author Miroslav Bimbo
 */

public class AnnotatorTestCaseResult extends TestCaseResult {

	private String moveName;
	private Vector3 initBallPosition;
	
	private Vector3 move;
	private Vector3 ballMove;
	private double duration = 0;
	private Vector3 rotation;
	
	/**
	 * @return the ballMove
	 */
	public Vector3 getBallMove() {
		return ballMove;
	}

	/**
	 * @param ballMove the ballMove to set
	 */
	public void setBallMove(Vector3 ballMove) {
		this.ballMove = ballMove;
	}

	private boolean fall = false;

	/**
	 * @return the fall
	 */
	public boolean isFall() {
		return fall;
	}

	/**
	 * @param fall the fall to set
	 */
	public void setFall(boolean fall) {
		this.fall = fall;
	}

	/**
	 * @return the move
	 */
	public Vector3 getMove() {
		return move;
	}

	/**
	 * @param move the move to set
	 */
	public void setMove(Vector3 move) {
		this.move = move;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public double getDuration() {
		return duration;
	}

	public void setRotation(Vector3 rotation) {
		this.rotation = rotation;
	}

	public Vector3 getRotation() {
		return rotation;
	}

	public void setMoveName(String moveName) {
		this.moveName = moveName;
	}

	public String getMoveName() {
		return moveName;
	}

	public void setInitBallPosition(Vector3 initBallPosition) {
		this.initBallPosition = initBallPosition;
	}

	public Vector3 getInitBallPosition() {
		return initBallPosition;
	}
	
	

	
}
