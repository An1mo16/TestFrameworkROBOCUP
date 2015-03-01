/**
 * Name:    TurnAround.java
 * Created: Feb 26, 2012
 * 
 * @author: ivan
 */
package sk.fiit.testframework.trainer.testsuite.testcases.tournament;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import sk.fiit.testframework.init.Implementation;
import sk.fiit.testframework.init.ImplementationFactory;
import sk.fiit.testframework.trainer.testsuite.ITestCaseObserver;
import sk.fiit.testframework.trainer.testsuite.TestCaseResult;

/**
 * TODO: Replace with a brief purpose of class / interface.
 * 
 * @author ivan
 *
 */
public class TurnAround implements Runnable, ITestCaseObserver {
	private static Logger logger = Logger.getLogger(TurnAround.class.getName());

	private List<TestCaseResult> testResults;
	
	public TurnAround() {
		testResults = new ArrayList<TestCaseResult>();
	}
	
	@Override
	public void run() {
		TurnAroundTest test = new TurnAroundTest();
		
		Implementation impl = ImplementationFactory.getImplementationInstance();
		impl.enqueueTestCase(test, this);
		impl.enqueueTestCase(test, this);
	}

	public void evaluateResult(List<TestCaseResult> results) {
		double result = Double.MAX_VALUE;
		for (TestCaseResult testCaseResult : results) {
			double fitnes = testCaseResult.getFitness();
			if (fitnes < result) result = fitnes;
		}
		logger.info("TEST CASE ENDED successfully with result: " + result);
	}
	
	@Override
	public void testFinished(TestCaseResult result) {
		testResults.add(result);
		if (testResults.size() == 2) {
			evaluateResult(testResults);
			testResults.clear();
		}
	}
	
}
