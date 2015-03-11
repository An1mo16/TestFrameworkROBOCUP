package sk.fiit.jim.annotation.data;

import sk.fiit.robocup.library.geometry.Vector3;

public class Annotation {
	
	/**
	 * Move name which annotation belongs to
	 */
	String name; //nazov pohybu ku ktoremu anotacia patri
	/**
	 * Move duration(in miliseconds).
	 */
	Values duration = new Values(); //trvanie pohybu v milisekundach
	/**
	 * True if rotation during move execution is not null. False if rotation is null.
	 */
	boolean rot = false; //otoci sa behom vykonavania pohybu o nenulovy uhol ?
	/**
	 * Rotation around every axis.
	 */
	Axis rotation = new Axis(); //rotacia okolo jednotlivych osi
	/**
	 * True if position is changed during move execution. False if move causes no position change.
	 */
	boolean mov = false; //pohne sa behom vykonavania pohybu o nenulovu dlzku ?
	/**
	 * Value of distance in direction of every axis by which agent changes its position by executing move.
	 */
	Axis move = new Axis(); //dlzka pohybu v smere jednotlivych osi robota v metroch
	/**
	 * Fall probability in percentage.
	 */
	double fall; //pravdepodobnost padu v percentach
	/**
	 * True if ball's position is changed by executing move. False if it doesn't.
	 */
	boolean b_mov = false; //pohne sa nasledkom vykonania pohybu lopta ?
	/**
	 * Value of distance in direction of every axis by which ball changes its position by executing move.
	 */
	Axis b_move = new Axis(); //dlzka pohybu lopty v smere jednotlivych osi robota v metroch
	/**
	 * True if preconditions are set. False if no preconditions are present.
	 */
	boolean prec = false; //je vykonanie pohybu obmedzene predpodmienkami ?
	/**
	 * Preconditions required for move execution.
	 */
	State preconditions = new State(); //prepodmienky na vykonanie pohybu
	/**
	 * Final state of agent after move execution.
	 */
	State end = new State(); //konecny stav robota po vykonani pohybu
	/**
	 * Note to describe the move
	 */
	String note; //poznamka k pohybu
	/**
	 * Checksum of annotated move calculated by SHA-1.
	 */
	String checksum; //chcecksum anotovaneho pohybu pomocou SHA-1
	/**
	 * Annotation unique identifier.
	 */
	String id; //jedinecny nazov anotacie
	
	//added by High5
	private Values ballMoveDistance = new Values();
	private boolean maxPos = false;
	private Vector3 maxBallDistancePosition = new Vector3();
	
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setDuration(double min, double max, double avg){
		this.duration.setMin(min);
		this.duration.setMax(max);
		this.duration.setAvg(avg);
	}
	
	public void setRot(boolean rotation){
		this.rot = rotation;
	}
	
	public void setRotation(Axis rotation){
		this.rotation = rotation;
	}
	
	public void setMov(boolean mov){
		this.mov = mov;
	}
	
	public void setMove(Axis move){
		this.move = move;
	}
	
	public void setFall(double fall){
		this.fall = fall;
	}
	
	public void setBallMov(boolean ball){
		this.b_mov = ball;
	}
	
	public void setBallMove(Axis ballMove){
		this.b_move = ballMove;
	}
	
	public void setPrec(boolean prec){
		this.prec = prec;
	}
	
	public void setPreconditions(State preconditions){
		this.preconditions = preconditions;
	}
	
	public void setEnd(State end){
		this.end = end;
	}
	
	public void setNote(String note){
		this.note = note;
	}
	
	public void setChecksum(String checksum){
		this.checksum = checksum;
	}
	
	//--------------------------------
	
	public String getId() {
		return this.id;
	}
	
	public String getName(){
		return this.name;
	}
	
	public Values getDuration(){
		return this.duration;
	}
	
	public boolean getRot(){
		return this.rot;
	}
	
	public Axis getRotation(){
		return this.rotation;
	}
	
	public boolean getMov(){
		return this.mov;
	}
	
	public Axis getMove(){
		return this.move;
	}
	
	public double getFall(){
		return this.fall;
	}
	
	public boolean getBallMov(){
		return this.b_mov;
	}
	
	public Axis getBallMove(){
		return this.b_move;
	}
	
	public boolean getPrec(){
		return this.prec;
	}
	
	public State getPreconditions(){
		return this.preconditions;
	}
	
	public State getEnd(){
		return this.end;
	}
	
	public String getNote(){
		return this.note;
	}
	
	public String getChecksum(){
		return this.checksum;
	}
	
	
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
	
	
}