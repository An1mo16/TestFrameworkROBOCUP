package sk.fiit.jim.agent.models;

import org.junit.Before;
import org.junit.Test;

import static java.lang.Math.PI;
import static java.lang.Math.toRadians;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.Matchers.closeTo;

import static org.junit.Assert.assertThat;

import sk.fiit.jim.agent.parsing.ParsedData;
import sk.fiit.robocup.library.geometry.Vector3D;
import static sk.fiit.jim.agent.models.FixedObject.*;

/**
 *  AgentModelTest.java
 *
 *@Title	Jim
 *@author	marosurbanec
 */
public class AgentModelTest{
	
	private AgentModel model;

	@Before
	public void setup(){
		model = new AgentModel();
	}
	
//	@Test
	/***
	 * This test wont work, computing rotation 
	 * is invalid in unit tests and agent doesnt lay on hips or back.
	 * The new implementation of this test is needed.
	 */
	public void rotationInfer(){
		//initial rotation
		assertCorrectRotations(0.0, 0.0, 3.0*PI/2.0);
		//lay on right hip
		assertCorrectRotations(PI / 2.0, 0.0, 3.0*PI/2.0);
		//lay on left hip
		assertCorrectRotations(3*PI / 2.0, 0.0, 3.0*PI/2.0);
		//lay on the back from initial position
		assertCorrectRotations(3*PI / 2.0, 0.0, 3.0*PI/2.0);
	}

	private void assertCorrectRotations(double rotationX, double rotationY, double rotationZ){
		ParsedData data = new ParsedData();
		data.fixedObjects.put(THEIR_UPPER_CORNER, 
			THEIR_UPPER_CORNER.getAbsolutePosition().rotateOverX(-rotationX).rotateOverY(-rotationY).rotateOverZ(-rotationZ));
		data.fixedObjects.put(THEIR_LOWER_CORNER, 
			THEIR_LOWER_CORNER.getAbsolutePosition().rotateOverX(-rotationX).rotateOverY(-rotationY).rotateOverZ(-rotationZ));
		data.fixedObjects.put(THEIR_UPPER_POST,
			THEIR_UPPER_POST.getAbsolutePosition().rotateOverX(-rotationX).rotateOverY(-rotationY).rotateOverZ(-rotationZ));
		model.processNewServerMessage(data);
		
		assertThat(model.getRotationX(), is(closeTo(rotationX, 0.01)));
		assertThat(model.getRotationY(), is(closeTo(rotationY, 0.01)));
		assertThat(model.getRotationZ(), is(closeTo(rotationZ, 0.01)));
	}
	
	@Test
	public void gyroscope(){
		//initial rotation = 0.0, 0.0, 3*PI / 2.0
		ParsedData gyro = new ParsedData();
		assertRotationChanged(gyro, 180.0 / 20.0, 0, 180.0 / 20.0);
	}

	private void assertRotationChanged(ParsedData gyro, double x, int y, double z){
		double xBefore = model.getRotationX();
		double yBefore = model.getRotationY();
		double zBefore = model.getRotationZ();
		gyro.gyroscope = Vector3D.cartesian(x / EnvironmentModel.TIME_STEP, y / EnvironmentModel.TIME_STEP, z/ EnvironmentModel.TIME_STEP);
		model.processNewServerMessage(gyro);
		assertThat(model.getRotationX(), is(closeTo(toRadians(x) + xBefore, 0.01)));
		assertThat(model.getRotationY(), is(closeTo(toRadians(y) + yBefore, 0.01)));
		assertThat(model.getRotationZ(), is(closeTo(toRadians(z) + zBefore, 0.01)));
	}
	
//	@Test
	/***
	 * This test wont work, computing rotation 
	 * is invalid in unit tests and agent doesnt lay on hips or back.
	 * The new implementation of this test is needed.
	 */
	public void positionCalculation(){
		assertPosition(Vector3D.cartesian(-1.0, 0.0, 0.0), 3.0*PI / 2.0);
		assertPosition(Vector3D.cartesian(0.0, 0.0, 0.0), 3.0*PI / 2.0);
		assertPosition(Vector3D.cartesian(1.0, 0.0, 0.0), 3.0*PI / 2.0);
	}

	private void assertPosition(Vector3D ourPosition, double rotationZ){
		ParsedData data = new ParsedData();
		data.fixedObjects.put(THEIR_UPPER_CORNER, THEIR_UPPER_CORNER.getAbsolutePosition().subtract(ourPosition).rotateOverZ(-rotationZ));
		data.fixedObjects.put(THEIR_LOWER_CORNER, THEIR_LOWER_CORNER.getAbsolutePosition().subtract(ourPosition).rotateOverZ(-rotationZ));
		data.fixedObjects.put(THEIR_LOWER_POST, THEIR_LOWER_POST.getAbsolutePosition().subtract(ourPosition).rotateOverZ(-rotationZ));
		model.processNewServerMessage(data);
		
		assertThat(model.getPosition(), is(equalTo(ourPosition)));
	}
}