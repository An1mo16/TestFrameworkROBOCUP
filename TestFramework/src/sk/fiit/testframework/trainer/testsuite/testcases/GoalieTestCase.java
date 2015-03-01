/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.fiit.testframework.trainer.testsuite.testcases;

import java.io.IOException;
import java.util.Random;

import sk.fiit.robocup.library.annotations.UnderConstruction;
import sk.fiit.testframework.communication.agent.AgentJim;
import sk.fiit.testframework.trainer.testsuite.TestCase;
import sk.fiit.testframework.trainer.testsuite.TestCaseResult;
import sk.fiit.testframework.worldrepresentation.models.SimulationState;

/**
 *
 * @author relation
 */
@UnderConstruction
public class GoalieTestCase extends TestCase {

    /**
	 */
    private AgentJim goalie;
    private static Random rnd = new Random();

    public GoalieTestCase() throws IOException {
        super();
        this.goalie = null;
    }

    @Override
    public boolean init() {
        throw new UnsupportedOperationException();
//        try {
//            trainer.setAgentPosition(goalie, getBaseGoaliePosition());
//            Point3D ballVelocity = new Point3D(-3, rnd.nextDouble(), 0);
//            trainer.setBall(getBaseBallPosInGoalieKick(), ballVelocity);
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }
    }

    @Override
    public TestCaseResult evaluate(SimulationState ss) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isStopCriterionMet(SimulationState ss) {
        throw new UnsupportedOperationException();
    }

	/* (non-Javadoc)
	 * @see sk.fiit.testframework.trainer.testsuite.TestCase#destroy()
	 */
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}
