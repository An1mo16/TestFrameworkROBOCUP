/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.fiit.testframework.trainer.testsuite.testcases;

import java.io.IOException;
import java.util.Random;

import sk.fiit.robocup.library.annotations.UnderConstruction;
import sk.fiit.robocup.library.geometry.Point3D;
import sk.fiit.testframework.communication.robocupserver.RobocupServerAddress;
import sk.fiit.testframework.trainer.testsuite.TestCase;
import sk.fiit.testframework.trainer.testsuite.TestCaseResult;
import sk.fiit.testframework.worldrepresentation.models.SimulationState;

/**
 *
 * @author relation
 */
@UnderConstruction
public class TCEvaluationTest extends TestCase {

    private static Random rnd = new Random();

    public TCEvaluationTest() throws IOException {
        super();
    }

    @Override
    public boolean init() {
        try {
            server.setBall(new Point3D(0, 0, 0.1), new Point3D(1, rnd.nextDouble(), rnd.nextDouble()));
            return true;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public TestCaseResult evaluate(SimulationState ss) {
        final double desiredX = 1.0;
        return new TestCaseResult(ss.getScene().getBallLocation().getX() - desiredX);
    }

    @Override
    public boolean isStopCriterionMet(SimulationState ss) {
        System.out.println(getElapsedTime());
        if (ss.getScene().getBallLocation().getX() > 1) {
            return true;
        } else {
            return false;
        }
    }

	/* (non-Javadoc)
	 * @see sk.fiit.testframework.trainer.testsuite.TestCase#destroy()
	 */
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}
