package sk.fiit.jim.agent.parsing;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import static java.lang.Math.toRadians;

import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.jim.agent.models.EnvironmentModel;
import sk.fiit.jim.agent.models.FixedObject;
import sk.fiit.jim.agent.moves.Joint;
import sk.fiit.robocup.library.geometry.Vector3D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 *  ParserTest.java
 *
 *@Title	Jim
 *@author	marosurbanec
 */
public class ParserTest{
	Parser parser = new Parser();
	
	private static final String TEST_MESSAGE = "(time (now 46.28))(GS (unum 1) (team left) (t 31.24) (pm PlayOn))(GYR (n torso) (rt -0.35 -0.28"+
		" -0.00))(HJ (n hj1) (ax 0.00))(HJ (n hj2) (ax -0.00))(See (G2R (pol 11.52 -13.65"+
 		" 1.24)) (G1R (pol 11.29 -6.51 1.45)) (F1R (pol 11.41 10.30 -2.53)) (F2R (pol"+
 		" 12.75 -27.77 -2.07)) (B (pol 5.59 -21.13 -5.14)) (P (team M_M) (id 1)"+
		" (rlowerarm (pol 0.19 -35.14 -20.99)) (llowerarm (pol 0.19 33.20 -21.53))))"+
		" (HJ (n raj1) (ax -0.00))(HJ (n raj2) (ax 0.00))(HJ (n raj3) (ax 0.00))(HJ (n raj4)"+
		" (ax -0.00))(HJ (n laj1) (ax -0.50))(HJ (n laj2) (ax 7.00))(HJ (n laj3) (ax -0.00))"+
 		" (HJ (n laj4) (ax 0.00))(HJ (n rlj1) (ax 0.01))(HJ (n rlj2) (ax -0.01))(HJ (n rlj3)"+
		" (ax 2.75))(HJ (n rlj4) (ax -5.50))(HJ (n rlj5) (ax 2.73))(FRP (n rf) (c -0.01 0.08"+
		" -0.02) (f -0.20 -0.75 23.30))(HJ (n rlj6) (ax 0.01))(HJ (n llj1) (ax -0.00))"+
 		" (HJ (n llj2) (ax -0.00))(HJ (n llj3) (ax 2.75))(HJ (n llj4) (ax -5.50))(HJ (n llj5)"+
		" (ax 2.75))(FRP (n lf) (c 0.01 -0.01 -0.02) (f -0.34 -1.03 18.15))(HJ (n llj6)"+
		" (ax -0.00))";
	
	@Test
	public void environmentData(){
		ParsedData data = parser.parse(TEST_MESSAGE);
		
		assertEquals(data.SIMULATION_TIME, 46.28, 0.01);
		assertEquals(data.GAME_TIME, 31.24, 0.01);
		assertEquals(data.playMode, EnvironmentModel.PlayMode.PLAY_ON);
		//one for negative angle
		assertEquals(data.agentsJoints.get(Joint.LAE1), -0.5, 0.01);
		//one for positive angle
		assertEquals(data.agentsJoints.get(Joint.LAE2), 7.0, 0.01);
		assertEquals(data.PLAYER_ID, Integer.valueOf(1));
		assertTrue(data.OUR_SIDE_IS_LEFT == Boolean.TRUE);
		
		assertEquals(data.gyroscope.getX(), -0.35, 0.01);
		assertEquals(data.forceReceptor.leftFootForce.getX(), -0.34, 0.01);
		assertEquals(data.forceReceptor.leftFootPoint.getZ(), -0.02, 0.01);
		assertEquals(data.forceReceptor.rightFootForce.getZ(), 23.3, 0.01);
		assertEquals(data.forceReceptor.rightFootPoint.getY(), 0.08, 0.01);
	}
	
	@Test
	public void seePerceptor(){
		ParsedData data = parser.parse(TEST_MESSAGE);
		
		assertTrue(data.ballRelativePosition != null);
		assertEquals(data.ballRelativePosition.getR(), 5.59, 0.01);
		assertEquals(data.ballRelativePosition.getPhi(), toRadians(-21.13), 0.01);
		
		assertTrue(data.fixedObjects.containsKey(FixedObject.THEIR_LOWER_CORNER));
		assertTrue(data.fixedObjects.containsKey(FixedObject.THEIR_UPPER_CORNER));
		assertTrue(data.fixedObjects.containsKey(FixedObject.THEIR_LOWER_POST));
		assertTrue(data.fixedObjects.containsKey(FixedObject.THEIR_UPPER_POST));
		
		assertEquals(data.fixedObjects.get(FixedObject.THEIR_LOWER_POST).getR(), 11.52, 0.01);
		assertEquals(data.fixedObjects.get(FixedObject.THEIR_UPPER_POST).getPhi(), toRadians(-6.51), 0.01);
	}
	
	@Test
	public void shouldSeePlayers(){
		/*AgentInfo.team = "M_M";
		AgentInfo.playerId = 1;
		ParsedData data = parser.parse(TEST_MESSAGE);
		
		assertThat(data.teammates.size(), is(1));
		assertThat(data.teammates.get(1).size(), is(2));
		assertThat(data.teammates.get(1).get("rlowerarm"), is(equalTo(Vector3D.spherical(0.19, -35.14, -20.99))));*/
	}
}