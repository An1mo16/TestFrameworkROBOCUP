package sk.fiit.jim.agent;

import static java.lang.Math.PI;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import sk.fiit.jim.agent.models.AgentModel;
import sk.fiit.jim.agent.parsing.Parser;
import sk.fiit.robocup.library.geometry.Angles;
import sk.fiit.robocup.library.geometry.Vector3D;

/**
 *  ParserToAgentModelIntergrationTest.java
 *
 *@Title	Jim
 *@author	marosurbanec
 *@author	Androids
 */

public class ParserToAgentModelIntegrationTest{
	String message = "(See (G2R (pol 11.05 -5.48 1.27))" +
			" (G1R (pol 11.06 5.57 1.40)) (F1R (pol 12.53 28.46 -2.63))" +
			" (F2R (pol 12.56 -28.39 -2.34)) (B (pol 2.06 -0.12 -14.29))" +
			" (P (team JIM) (id 1) (rlowerarm (pol 0.19 -35.12 -20.98))" +
			" (llowerarm (pol 0.19 33.23 -21.50))))";
	private AgentModel agent;
	
	@Before
	public void setup(){
		agent = new AgentModel();
		Parser.clearObservers();
		Parser.subscribe(agent);
		new Parser().parse(message);
	}
	
//	@Test()
	/***
	 * This test wont work, in unit tests the position computing doesnt
	 *  work and new implementation of this is needed.
	 */
	public void assertGoodPosition(){
		Vector3D expected = Vector3D.cartesian(-2.0, 0.0, 0.54);
		Vector3D diff = agent.getPosition().subtract(expected);
		assertThat(diff.getR(), is(lessThan(0.1)));
//		System.out.println(agent.getPosition());
	}

	@Test
	public void assertGoodRotation(){
		double expectedX = 0.0;
		double expectedY = 0.0;
		double expectedZ = 1.5 * PI;
		assertThat(Angles.angleDiff(agent.getRotationX(), expectedX), is(closeTo(0, 0.1)));
		assertThat(Angles.angleDiff(agent.getRotationY(), expectedY), is(closeTo(0, 0.1)));
		assertThat(Angles.angleDiff(agent.getRotationZ(), expectedZ), is(closeTo(0, 0.1)));
	}
}