package sk.fiit.jim.agent.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import sk.fiit.jim.Settings;
import sk.fiit.jim.agent.parsing.ParsedData;
import sk.fiit.jim.agent.parsing.ParsedDataObserver;
import sk.fiit.robocup.library.geometry.Vector3D;
import sk.fiit.robocup.library.math.KalmanForVector;
import sk.fiit.robocup.library.review.ReviewOk;

/**
 *  KalmanAdjuster.java
 *  
 *  Adjusts percieved coordinates of ball and flags on the pitch
 *  using a kalman filter, thereby attempting to reduce noise for
 *  further calculations.
 *
 *@Title	Jim
 *@author	marosurbanec
 *@author	Androids
 */
@ReviewOk
public class KalmanAdjuster implements ParsedDataObserver{

	//SEE perceptor is received every 60ms. Hence, 250ms is an arbitrary number for 5 or more SEE data
	private static final double TIME_CONSIDERED_OBSOLETE = 0.25;
	/**
	 * Current time.
	 */
	double now;
	private KalmanForVector ballKalman;
	private double lastTimeBallSeen;
	private HashMap<FixedObject, KalmanForVector> fixedObjectsKalmans;
	private HashMap<FixedObject, Double> fixedObjectSeenTimes;
	
	public KalmanAdjuster(){
		fixedObjectsKalmans = new HashMap<FixedObject, KalmanForVector>();
		fixedObjectSeenTimes = new HashMap<FixedObject, Double>();
		
		for (FixedObject fixedObject : FixedObject.values())
			fixedObjectSeenTimes.put(fixedObject, 0.0);
	}
	
	/* (non-Javadoc)
	 * @see sk.fiit.jim.agent.parsing.ParsedDataObserver#processNewServerMessage(sk.fiit.jim.agent.parsing.ParsedData)
	 */
	@Override
	public void processNewServerMessage(ParsedData data){
		this.now = data.SIMULATION_TIME;
		adjustBallPosition(data);
		adjustFixedPointsPosition(data.fixedObjects);
	}

	private void adjustBallPosition(ParsedData data){
		if (data.ballRelativePosition == null)
			return;
		
		if (isObsolete(lastTimeBallSeen) || ballKalman == null)
			ballKalman = freshKalman();
		
		data.ballRelativePosition = ballKalman.update(data.ballRelativePosition);
		lastTimeBallSeen = now;
	}

	private boolean isObsolete(double when){
		return (now - when) > TIME_CONSIDERED_OBSOLETE; 
	}
	
	private void adjustFixedPointsPosition(Map<FixedObject, Vector3D> fixedObjects){
		for (Entry<FixedObject, Vector3D> entry : fixedObjects.entrySet()){
			FixedObject flag = entry.getKey();
			if (isObsolete(fixedObjectSeenTimes.get(flag)) || fixedObjectsKalmans.get(flag) == null)
				fixedObjectsKalmans.put(flag, freshKalman());
			
			KalmanForVector kalman = fixedObjectsKalmans.get(flag);
			Vector3D newValue = kalman.update(entry.getValue());
			fixedObjects.put(flag, newValue);
			fixedObjectSeenTimes.put(flag, now);
		}
	}

	private KalmanForVector freshKalman(){
		return new KalmanForVector(Settings.getDouble("kalmanDefaultQ"), Settings.getDouble("kalmanDefaultR"));
	}
}