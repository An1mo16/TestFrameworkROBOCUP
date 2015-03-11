package sk.fiit.jim.agent.models;

import static sk.fiit.jim.agent.models.EnvironmentModel.TIME_STEP;
import static sk.fiit.jim.log.LogType.AGENT_MODEL;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;

import sk.fiit.jim.Settings;
import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.jim.agent.Side;
import sk.fiit.jim.agent.moves.Joint;
import sk.fiit.jim.agent.parsing.ParsedData;
import sk.fiit.jim.agent.parsing.ParsedDataObserver;
import sk.fiit.jim.annotation.data.Annotation;
import sk.fiit.jim.annotation.data.Axis;
import sk.fiit.jim.log.Log;
import sk.fiit.jim.log.LogLevel;
import sk.fiit.robocup.library.geometry.Angles;
import sk.fiit.robocup.library.geometry.Vector3D;

/**
 *  AgentModel.java
 *  
 *  Data structure encapsulating information about agent's current state,
 *  such as joint positions, global position and rotations against global
 *  axis. Calculations are delegated to {@link AgentPositionCalculator} and
 *  {@link AgentRotationCalculator}. Vector3D calculations from accelerometer
 *  are being used in ./scripts/plan/plan.rb to determine weather the agent
 *  is standing or laying on the ground.
 *
 *@Title	Jim
 *@author	marosurbanec
 *@author	Androids
 */
/**
 * 
 *  AgentModel.java
 *  
 *@Title        Jim
 *@author       $Author: Jojo $
 */
public class AgentModel implements ParsedDataObserver, Serializable{
	
	private boolean falled = false;
	
	private static final long serialVersionUID = -1939740744252731355L;
	
        /*
         * Half time in seconds.
         */
        private static double HALF_TIME = 300.0;
        
        /*
         * True if sides are changed after halftime
         */
        private static boolean HALF_TIME_CHANGE_SIDES = false;
        
        /*
         * True if sides were changed. We want them to change only once.
         */
        private static boolean sidesChanged = false;
        
	private static AgentModel instance = new AgentModel();
	/**
	 * Rotation by axis X value.
	 */
	double rotationX = 0.0;
	/**
	 * Rotation by axis Y value.
	 */
	double rotationY = 0.0;
	/**
	 * Rotation by axis Z value.
	 */
	double rotationZ = 3.0*Math.PI / 2.0;
	/**
	 * Current position of agent.
	 */
	Vector3D position = Vector3D.cartesian(0, 0, 0);
	Queue<Vector3D> lastPositions = new LinkedList<Vector3D>();
	/**
	 * 
	 */
	Vector3D velocity = Vector3D.cartesian(0, 0, 0);
	/**
	 * 
	 */
	Vector3D pureBodyAcceleration = Vector3D.cartesian(0, 0, 0);
	private Map<Joint, Double> jointAngles = new HashMap<Joint, Double>();
	/**
	 * Data last received from server.
	 */
	transient ParsedData lastDataReceived = new ParsedData();
	public static Side side = Side.LEFT;
	protected double lastTimeFlagSeen = 0.0;
	
	public AgentModel(){
		for (Joint joint : Joint.values())
			jointAngles.put(joint, 0.0);
	}
	
	/**
	 * Static instance of AgentModel to work with it in other classes. 
	 *
	 * @return
	 */
	public static AgentModel getInstance(){
		return instance;
	}
	
	/**
	 * @author Samuel Benkovic
	 * @author Spred
	 * @author MegaTroll
	 * Method return distance from enemy goal
	 */
	public double getDistanceFromEnemyGoal()
	{
		return  AgentModel.getInstance().getPosition().getXYDistanceFrom(FixedObject.theirPostMiddle());
	}
	/**
	 * @author Samuel Benkovic
	 * @author Spred
	 * @author MegaTroll
	 * Method return distance from Our goal
	 */
	public double getDistanceFromOurGoal()
	{
		return  AgentModel.getInstance().getPosition().getXYDistanceFrom(FixedObject.ourPostMiddle());
	}
	/**
	 * @author Samuel Benkovic
	 * @author Spred
	 * @author MegaTroll
	 * Method returns the smallest distance between the ball and the team
	 */
	public double getDistanceNereastToBall(List<Player> Team){
		
		double min= 40000;
		for(Player p:Team){
			if (p.getDistanceFromBall()<min){
				min = p.getDistanceFromBall();
			}
		}
		
		return min;
	}
	
	//added by team17
	//vytvori ciastocnu kopiu (chyba cast dat! - umyselne, kvoli pamatovym narokom chybaju casti
	//lastDataReceived ktore sa nepouzivaju, ak sa zacnu, treba ich pridat) modelu pouzitelnu pre predikciu
	//
	//mozno by bolo lepsie spravit samostatnu triedu pre reprezentaciu zjednoduseneho "buduceho"
	//stavu modelu?? ale potom by zas bolo treba reimplementovat metody ako napr. isOnGround atd.
	/**
	 * Creates partial copy of AgentModel without some parts of lastDataReceived attribute. 
	 * These were omitted not to fill memory with unimportant data as they are not used now. 
	 * In the case they have to be used, one must add it here.
	 *
	 * @return
	 */
	public AgentModel partialCopy() {
		AgentModel newModel = new AgentModel();
		newModel.rotationX = this.rotationX;
		newModel.rotationY = this.rotationY;
		newModel.rotationZ = this.rotationZ;
		newModel.position = this.position.clone();
		newModel.velocity = this.velocity.clone();
		newModel.pureBodyAcceleration = this.pureBodyAcceleration.clone();
		newModel.jointAngles = new HashMap<Joint, Double>();
		for (Joint joint : Joint.values()) {
			double val = ((Double) this.jointAngles.get(joint) ).doubleValue();
			newModel.jointAngles.put(joint, new Double(val));
		}
		newModel.lastDataReceived = new ParsedData();
		newModel.lastDataReceived.accelerometer = this.lastDataReceived.accelerometer.clone();
		return newModel;
	}
	
	//added by team17
	/**
	 * Returns current angle of specified joint of AgentModel object. 
	 *
	 * @param joint
	 * @return
	 */
	public double getJointAngle(Joint joint){
		return this.jointAngles.get(joint);
	}
	/**
	 * Returns angle of specified joint of AgentModel static instance. 
	 *
	 * @param joint
	 * @return
	 */
	public static double getJointAngleOf(Joint joint){
		return instance.jointAngles.get(joint);
	}
	
	/**
	 * Returns globalized position of agent from his relative position. 
	 *
	 * @param relative
	 * @return
	 */
	public Vector3D globalize(Vector3D relative) {
		return relative.rotateOverY(rotationY).rotateOverZ(rotationZ).rotateOverX(rotationX).add(position);
	}
	/**
	 * Returns relativized position of agent from his global position. 
	 *
	 * @param relative
	 * @return
	 */
	public Vector3D relativize(Vector3D global) {
		return global.subtract(position).rotateOverX(-rotationX).rotateOverZ(-rotationZ).rotateOverY(-rotationY);
	}

	//player and side identification added by Androids
	/* (non-Javadoc)
	 * @see sk.fiit.jim.agent.parsing.ParsedDataObserver#processNewServerMessage(sk.fiit.jim.agent.parsing.ParsedData)
	 */
	public void processNewServerMessage(ParsedData data){
		if (data.PLAYER_ID != null && data.PLAYER_ID != 0)
			AgentInfo.playerId = data.PLAYER_ID;
		if (data.OUR_SIDE_IS_LEFT != null) {
			AgentInfo.hasAssignedSide = true;
			AgentInfo.side = data.OUR_SIDE_IS_LEFT ? Side.LEFT : Side.RIGHT;
		}
                
		// Added by Roman Moravcik, Giment09
                // According to values and server settings, if sides change at half time, side is changed.
                if (HALF_TIME_CHANGE_SIDES && !sidesChanged && data.GAME_TIME >= HALF_TIME) {
                    sidesChanged = true;
                    AgentInfo.side = AgentInfo.side == Side.RIGHT ? Side.LEFT : Side.RIGHT;
                    System.out.println("Sides changed at half time");
                 }
                
		lastDataReceived = data;
		//getPosition(data);
		updateJointPositions(data);
		adjustRotationsFor(data.gyroscope);
		updateRotations(data);
		updatePureBodyAcceleration(data);
		updatePosition(data);
		//Log.log(LogType.OTHER, position.toString());
	}

	private void updateJointPositions(ParsedData data){
		for (Entry<Joint, Double> record : data.agentsJoints.entrySet())
			this.jointAngles.put(record.getKey(), record.getValue());
	}
	
	/*
	 * Methods calculate vector moves around vectors and axis. The calculations
	 * are retrieved from theory on vector moves (google search on internet).
	 * Added by Androids.
	 */
	private Vector3D rotateVectorAroundVector(Vector3D initialVec, Vector3D axisV, double radAngle){
		Vector3D resultVec = null;
		double c = Math.cos(radAngle);
		double s = Math.sin(radAngle);
		double array[][] = new double[3][3];
		
		array[0][0]= (axisV.getX() * axisV.getX()) * (1.0f - c) + c;
		array[0][1]= (axisV.getY() * axisV.getX()) * (1.0f - c) + (axisV.getZ() * s);
		array[0][2]= (axisV.getZ() * axisV.getX()) * (1.0f - c) - (axisV.getY() * s);
		array[1][0]=(axisV.getX() * axisV.getY()) * (1.0f - c) - (axisV.getZ() * s);
		array[1][1]=(axisV.getY() * axisV.getY()) * (1.0f - c) + c;
		array[1][2]=(axisV.getZ() * axisV.getY()) * (1.0f - c) + (axisV.getX() * s);
		array[2][0]=(axisV.getX() * axisV.getZ()) * (1.0f - c) + (axisV.getY() * s);
		array[2][1]=(axisV.getY() * axisV.getZ()) * (1.0f - c) - (axisV.getX() * s);
		array[2][2]=(axisV.getZ() * axisV.getZ()) * (1.0f - c) + c;
		
		resultVec = vectorTimesMatrix(array, initialVec);
		return resultVec;
	}
	
	//added by Androids
	private Vector3D rotateVectorAroundZAxis(Vector3D initialVec, double angleV){
		Vector3D resultVec = null;
		double x = initialVec.getX() * Math.cos(angleV) - initialVec.getY() * Math.sin(angleV);
		double y = initialVec.getX() * Math.sin(angleV) + initialVec.getY() * Math.cos(angleV);
		
		resultVec = Vector3D.cartesian(x, y, initialVec.getZ());
		return resultVec;
	}

	//added by Androids
	private Vector3D countRotatedXAxis(double angleRotateZ, double angleRotateY){
		Vector3D x = Vector3D.cartesian(1, 0, 0);
		Vector3D y= Vector3D.cartesian(0, 1, 0);
		Vector3D firstRotateX = rotateVectorAroundZAxis(x,angleRotateZ);
		Vector3D firstRotateY = rotateVectorAroundZAxis(y,angleRotateZ);
		
		Vector3D resultVec = rotateVectorAroundVector(firstRotateX, firstRotateY, angleRotateY);
		return resultVec;
	}
	
	//added by Androids
	private Vector3D vectorTimesMatrix(double[][] matrix, Vector3D vector){
		double x = vector.getX()*matrix[0][0] + vector.getX()*matrix[0][1] + vector.getX()*matrix[0][2] ;
		double y = vector.getY()*matrix[1][0] + vector.getY()*matrix[1][1] + vector.getY()*matrix[1][2] ;
		double z = vector.getZ()*matrix[2][0] + vector.getZ()*matrix[2][1] + vector.getZ()*matrix[2][2] ;
		
		Vector3D resultVec = Vector3D.cartesian(x, y, z);
		return resultVec;
	}
	
	//added by Androids
	private double getAngleBetweenTwoVectors(Vector3D firstVec, Vector3D secondVec){
		double angleV;
		angleV = Math.acos(firstVec.getX() * secondVec.getX() + firstVec.getY() * secondVec.getY() + firstVec.getZ() * secondVec.getZ());
		return angleV;
	}
	
	private void updatePureBodyAcceleration(ParsedData data){
		Vector3D oldX = Vector3D.cartesian(1, 0, 0);
		Vector3D oldY = Vector3D.cartesian(0, 1, 0);
		Vector3D oldZ = Vector3D.cartesian(0, 0, 1);
		
		Vector3D rotatedX = countRotatedXAxis(rotationZ, rotationY);
		Vector3D rotatedY = Vector3D.cartesian(rotatedX.getY(), rotatedX.getX(), rotatedX.getZ());
		Vector3D rotatedZ = Vector3D.cartesian(rotatedY.getX(), rotatedY.getZ(), rotatedY.getY());
		
		if (data.accelerometer == null)
			return;
		
		pureBodyAcceleration = Vector3D.cartesian( data.accelerometer.getX() -  Math.cos ( getAngleBetweenTwoVectors(oldX, rotatedX) ) * Settings.getDouble("gravityAcceleration")  ,
				data.accelerometer.getY() -  Math.cos ( getAngleBetweenTwoVectors(oldY, rotatedY) )  * Settings.getDouble("gravityAcceleration") ,
				data.accelerometer.getZ() -  Math.cos ( getAngleBetweenTwoVectors(oldZ, rotatedZ) ) * Settings.getDouble("gravityAcceleration")  );
		
		Log.log(AGENT_MODEL, "Pure body acceleration: [%.5f,%.5f,%.5f]", pureBodyAcceleration.getX() ,pureBodyAcceleration.getY(),pureBodyAcceleration.getZ());
	}

	private void adjustRotationsFor(Vector3D gyroscope){
		if (gyroscope == null)
			return;
		Log.setLogLevel(LogLevel.LOG);
	//Log.log(LogType.OTHER, "Gyroscope: [%.5f,%.5f,%.5f]", gyroscope.getX() ,gyroscope.getY(),gyroscope.getZ());
		rotationX += Math.toRadians(gyroscope.getX() * TIME_STEP);
		rotationX = Angles.normalize(rotationX);
		rotationY += Math.toRadians(gyroscope.getY() * TIME_STEP);
		rotationY = Angles.normalize(rotationY);
		rotationZ += Math.toRadians(gyroscope.getZ() * TIME_STEP);
		rotationZ = Angles.normalize(rotationZ);
		
		Log.log(AGENT_MODEL, "Rotation: [%.5f,%.5f,%.5f]", rotationX ,rotationY,rotationZ);
	}
	
	private void updateRotations(ParsedData data){
		new AgentRotationCalculator(this).updateRotations(data);
	}
	
	private void updatePosition(ParsedData data){
		new AgentPositionCalculator(this).updatePosition(data);
	}
	
	//---------------------QUERIES, GETTERS AND SETTERS-------------------
	/**
	 * Returns true if agent is standing, false otherwise. 
	 *
	 * @return
	 */
	public boolean isStanding(){
		return lastDataReceived.accelerometer.getZ() > 7.0;
	}
	/**
	 * Returns true if agent is standing from specified ParsedData, false otherwise. 
	 *
	 * @return
	 */
	public boolean isStanding(ParsedData data){
		return data.accelerometer.getZ() > 7.0;
	}

	//TEST WTF/////////////////////
	public boolean falled(){
		if(falled){
			return falled;	
		}else{
			if(isOnGround()){
				falled = true;
				return true;
			}
			return false;
		}
	}
	
	public void gotUp(){
		falled = false;
	}
	
	public boolean isFallen(){
		return falled;
	}
	//TEST WTF/////////////////////
	
	/**
	 * Returns true if agent is on ground, false otherwise. 
	 *
	 * @return
	 */
	public boolean isOnGround(){
		if (lastDataReceived.accelerometer != null)
			return isOnGroundJudgedByAccelerometer();
		return Angles.angleDiff(rotationX, 0.0) > (Math.PI / 4.0) || Angles.angleDiff(rotationY, 0.0) > (Math.PI / 4.0);
//		return Math.abs(lastDataReceived.accelerometer.getY()) > 9.3; 
 	}
	
	private boolean isOnGroundJudgedByAccelerometer(){
		double z = Math.abs(lastDataReceived.accelerometer.getZ());
		return z < Settings.getDouble("gravityAcceleration") / 2.0;
	}
	/**
	 * Returns true if agent is lying on back(na chrbte), false otherwise. 
	 *
	 * @return
	 */
	public boolean isLyingOnBack(){
		return isOnGround() && (lastDataReceived.accelerometer.getY() > 9.3) ;
	}
	/**
	 * Returns true if agent is lying on belly(na bruchu), false otherwise. 
	 *
	 * @return
	 */
	public boolean isLyingOnBelly(){
		return isOnGround() && !isLyingOnBack();
	}
	
	/**
	 * Returns agents rotation by axis X. 
	 *
	 * @return
	 */
	public double getRotationX(){
		return rotationX;
	}
	/**
	 * Returns agents rotation by axis Y. 
	 *
	 * @return
	 */
	public double getRotationY(){
		return rotationY;
	}
	/**
	 * Returns agents rotation by axis Z.
	 *
	 * @return
	 */
	public double getRotationZ(){
		return rotationZ;
	}

	/**
	 * Returns agents position. 
	 *
	 * @return
	 */
	public Vector3D getPosition(){
		return position;
	}
	
	public void setPosition(Vector3D position){
		this.position = position;
	}

	public void setRotationX(double rotationX){
		this.rotationX = rotationX;
	}

	public void setRotationY(double rotationY){
		this.rotationY = rotationY;
	}

	/**
	 * Returns last received data from server. 
	 *
	 * @return
	 */
	public ParsedData getLastDataReceived(){
		return this.lastDataReceived;
	}
	
	public void setRotationZ(double rotationZ){
		this.rotationZ = rotationZ;
	}
	
	public double getLastTimeFlagSeen(){
 		return lastTimeFlagSeen;
 	}
	
	//added by team17
	//vracia predikciu buduceho modelu hraca vypocitanu na zaklade anotacie k pohybu
	/**
	 * Returns changed AgentModel object calculated from current agent model
	 * based on specified annotation execution. It is part of prediction.
	 *
	 * @param annotation
	 * @return
	 */
	public AgentModel afterAction(Annotation annotation) {
		AgentModel newModel = this.partialCopy();
		
		// nastavenie noveho natocenia robota
		Axis rotation = annotation.getRotation();
		newModel.setRotationX(newModel.getRotationX() + rotation.getX().getAvg());
		newModel.setRotationY(newModel.getRotationY() + rotation.getY().getAvg());
		newModel.setRotationZ(newModel.getRotationZ() + rotation.getZ().getAvg());
		
		// nastavenie novych uhlov klbov
		for(int i=0; i<annotation.getEnd().getJoints().size(); i++){
			for (Joint joint : Joint.values()){
				if(joint.toString().compareTo(annotation.getEnd().getJoints().get(i).getName().toUpperCase()) == 0){
					newModel.jointAngles.put(joint, annotation.getEnd().getJoints().get(i).getValue());
				}
			}			
		}
		
		// nastavenie, ci robot stoji/lezi
		String stand = annotation.getEnd().getLying();
		if(stand.compareTo("false") == 0){
			newModel.lastDataReceived.accelerometer = Vector3D.cartesian(0, 0, 10);
		}
		else{
			if(stand.compareTo("on_back") == 0){
				newModel.lastDataReceived.accelerometer = Vector3D.cartesian(0, 10, 0);
			}
			else{
				if(stand.compareTo("on_belly") == 0){
					newModel.lastDataReceived.accelerometer = Vector3D.cartesian(0, -10, 0);
				}
			}
		}

		//musi odsuhlasit Peto Holak
//		//relativny posun lopty
//		if(annotation.getBallMov() == true){
//			double x = newModel.lastDataReceived.ballRelativePosition.getX() + annotation.getBallMove().getX().getAvg();
//			double y = newModel.lastDataReceived.ballRelativePosition.getY() + annotation.getBallMove().getY().getAvg();
//			double z = newModel.lastDataReceived.ballRelativePosition.getZ() + annotation.getBallMove().getZ().getAvg();
//			newModel.lastDataReceived.ballRelativePosition = Vector3D.cartesian(x, y, z);
//		}
		
		//ak som pochopil niektore data zle, tak ma opravte
		Axis move = annotation.getMove();
		//priemerna zmena polohy z pohladu hraca (t.j. osi su orientovane relativne k hracovi)
		Vector3D avgPosChange = Vector3D.cartesian(move.getX().getAvg(), move.getY().getAvg(), move.getZ().getAvg());
		//otocime vektor zmeny polohy o sucasnu rotaciu hraca 
		avgPosChange = avgPosChange.rotateOverZ(rotationZ);
		//na zaver pripocitame k sucasnej polohe
		newModel.setPosition(newModel.position.add(avgPosChange));
		return newModel;
	}
}