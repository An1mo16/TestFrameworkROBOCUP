package sk.fiit.testframework.annotator.serialization;

import java.io.File;
import java.util.Arrays;

import sk.fiit.robocup.library.geometry.Vector3;

/** 
 * @author: Roman Bilevic
 */

public class Annotation {
	String name = "";
	Values duration = new Values();
	boolean rot = false;
	Axis rotation = new Axis();
	boolean mov = false;
	Axis move = new Axis();
	double fall = 0;
	boolean b_mov = false;
	Axis b_move = new Axis();
	boolean prec = false;
	State preconditions = new State();
	State end = new State();
	String note = "";
	String checksum = "";
	String id = ""; //jedinecny nazov anotacie
	
	//added by High5
	private Values ballMoveDistance = new Values();
	private boolean maxPos = false;
	private Vector3 maxBallDistancePosition = new Vector3();
	private Values ballDisperion = new Values();

	@Override
	public String toString() {
		return "Annotation [name=" + name
				+ ", \r\n duration=" + duration + ",\r\n rot=" + rot + ", rotation="
				+ rotation + ",\r\n mov=" + mov + ", move=" + move + ",\r\n fall="
				+ fall + ",\r\n b_mov=" + b_mov + ", b_move=" + b_move + ",\r\n prec="
				+ prec + ", preconditions=" + preconditions + ",\r\n end=" + end
				+ ",\r\n note=" + note + ",\r\n checksum=" + checksum + "]";
	}

	public String getName(){
		return this.name;
	}

	/**
	 * @return the duration
	 */
	public Values getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(Values duration) {
		this.duration = duration;
	}

	/**
	 * @return the rot
	 */
	public boolean isRot() {
		return rot;
	}

	/**
	 * @param rot the rot to set
	 */
	public void setRot(boolean rot) {
		this.rot = rot;
	}

	/**
	 * @return the rotation
	 */
	public Axis getRotation() {
		return rotation;
	}

	/**
	 * @param rotation the rotation to set
	 */
	public void setRotation(Axis rotation) {
		this.rotation = rotation;
	}

	/**
	 * @return the mov
	 */
	public boolean isMov() {
		return mov;
	}

	/**
	 * @param mov the mov to set
	 */
	public void setMov(boolean mov) {
		this.mov = mov;
	}

	/**
	 * @return the move
	 */
	public Axis getMove() {
		return move;
	}

	/**
	 * @param move the move to set
	 */
	public void setMove(Axis move) {
		this.move = move;
	}

	/**
	 * @return the fall
	 */
	public double getFall() {
		return fall;
	}

	/**
	 * @param fall the fall to set
	 */
	public void setFall(double fall) {
		this.fall = fall;
	}

	/**
	 * @return the b_mov
	 */
	public boolean isB_mov() {
		return b_mov;
	}

	/**
	 * @param b_mov the b_mov to set
	 */
	public void setB_mov(boolean b_mov) {
		this.b_mov = b_mov;
	}

	/**
	 * @return the b_move
	 */
	public Axis getB_move() {
		return b_move;
	}

	/**
	 * @param b_move the b_move to set
	 */
	public void setB_move(Axis b_move) {
		this.b_move = b_move;
	}

	/**
	 * @return the prec
	 */
	public boolean isPrec() {
		return prec;
	}

	/**
	 * @param prec the prec to set
	 */
	public void setPrec(boolean prec) {
		this.prec = prec;
	}

	/**
	 * @return the preconditions
	 */
	public State getPreconditions() {
		return preconditions;
	}

	/**
	 * @param preconditions the preconditions to set
	 */
	public void setPreconditions(State preconditions) {
		this.preconditions = preconditions;
	}

	/**
	 * @return the end
	 */
	public State getEnd() {
		return end;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(State end) {
		this.end = end;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return the checksum
	 */
	public String getChecksum() {
		return checksum;
	}

	/**
	 * @param checksum the checksum to set
	 */
	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param type the type to set
	 */
	
	public void setBallMoveDistance(Values ballMoveDistance) {
		this.ballMoveDistance = ballMoveDistance;
	}

	public Values getBallMoveDistance() {
		return ballMoveDistance;
	}

	public void setMaxBallDistancePosition(Vector3 maxBallDistancePosition) {
		this.maxBallDistancePosition = maxBallDistancePosition;
	}

	public Vector3 getMaxBallDistancePosition() {
		return maxBallDistancePosition;
	}
	
	
	public boolean isMaxPos() {
		return maxPos;
	}

	public void setMaxPos(boolean maxPos) {
		this.maxPos = maxPos;
	}

	public void setBallDisperion(Values ballDisperion) {
		this.ballDisperion = ballDisperion;
	}

	public Values getBallDisperion() {
		return ballDisperion;
	}
	
	
}

//TODO: este upravir set a get metody