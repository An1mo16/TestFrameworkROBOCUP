package sk.fiit.jim.agent.models;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import static org.junit.Assert.assertThat;

import sk.fiit.jim.agent.parsing.ParsedData;
import sk.fiit.robocup.library.geometry.Vector3D;

/**
 *  KalmanAdjusterTest.java
 *
 *@Title	Jim
 *@author	marosurbanec
 */
public class KalmanAdjusterTest{
	private KalmanAdjuster kalman;
	private ParsedData data;
	private ParsedData newData;

	@Before
	public void setup(){
		kalman = new KalmanAdjuster();
		data = new ParsedData();
		data.SIMULATION_TIME = 100.0;
		newData = new ParsedData();
		newData.SIMULATION_TIME = 100.0;
	}
	
	@Test
	public void ballAdjustment(){
		data.ballRelativePosition = Vector3D.cartesian(1.0, 1.0, 0.0);
		kalman.processNewServerMessage(data);
		//no adjustment - the first data
		assertThat(data.ballRelativePosition, is(equalTo(Vector3D.cartesian(1.0, 1.0, 0))));
		
		newData.ballRelativePosition = Vector3D.cartesian(0.75, 0.75, 0.0);
		kalman.processNewServerMessage(newData);
		//slightly adjusted
		assertThat(newData.ballRelativePosition, is(equalTo(Vector3D.cartesian(0.84, 0.84, 0))));
		
		newData.ballRelativePosition = Vector3D.cartesian(0.75, 0.75, 0.0);
		kalman.processNewServerMessage(newData);
		//almost completely adjusted
		assertThat(newData.ballRelativePosition, is(equalTo(Vector3D.cartesian(0.78, 0.78, 0))));
	}
	
	@Test
	public void skipObsolete(){
		data.ballRelativePosition = Vector3D.cartesian(1.0, 1.0, 0.0);
		kalman.processNewServerMessage(data);
		
		newData.ballRelativePosition = Vector3D.cartesian(5.0, 5.0, 0);
		newData.SIMULATION_TIME = 500.0;
		kalman.processNewServerMessage(newData);
		assertThat(newData.ballRelativePosition, is(equalTo(Vector3D.cartesian(5.0, 5.0, 0))));
	}
	
	@Test
	public void adjustFixedPoints(){
		data.fixedObjects.put(FixedObject.OUR_LOWER_CORNER, Vector3D.cartesian(1.0, 1.0, 0));
		kalman.processNewServerMessage(data);
		assertThat(data.fixedObjects.get(FixedObject.OUR_LOWER_CORNER), is(equalTo(Vector3D.cartesian(1.0, 1.0, 0))));
		
		newData.fixedObjects.put(FixedObject.OUR_LOWER_CORNER, Vector3D.cartesian(0.75, 0.75, 0.0));
		kalman.processNewServerMessage(newData);
		//slightly adjusted
		assertThat(newData.fixedObjects.get(FixedObject.OUR_LOWER_CORNER), is(equalTo(Vector3D.cartesian(0.84, 0.84, 0))));
	}
}