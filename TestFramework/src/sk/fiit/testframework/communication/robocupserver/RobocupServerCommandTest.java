/**
 * Name:    TrainerCommandTest.java
 * Created: Feb 27, 2011
 * 
 * @author: relation
 */
package sk.fiit.testframework.communication.robocupserver;

import org.junit.Test;

import sk.fiit.robocup.library.geometry.Point3D;

/**
 * Test with aim to cover most frequently used trainer command tests and their
 * syntax.
 * 
 * @author relation
 * 
 */
public class RobocupServerCommandTest {

	@Test
	public void SexpressionTest() {

		RobocupServerCommand agent = new RobocupServerCommand.Agent(new Point3D(10, 5, 0), new Point3D(0, 0, 0), "ANDROIDS", 10);
		System.out.println(agent.getCommand());
		
	}
}
