/**
 * Name:    Walk.java
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
public class WalkStable implements Runnable, ITestCaseObserver {
	private static Logger logger = Logger.getLogger(WalkStable.class.getName());
	
	private List<TestCaseResult> testResults;
	
	public WalkStable() {
		testResults = new ArrayList<TestCaseResult>();
	}
	
	@Override
	public void run() {
		Implementation impl = ImplementationFactory.getImplementationInstance();
		
		impl.enqueueTestCase(new WalkStableTest(), this);
		impl.enqueueTestCase(new WalkStableTest(), this);
	}

	public void evaluateStableTestResults(List<TestCaseResult> results) {
		double result = Double.MAX_VALUE;
		for (TestCaseResult testCaseResult : results) {
			double fitnes = testCaseResult.getFitness();
			if (fitnes < result && fitnes >= 0) result = fitnes;
		}
		logger.info("TEST CASE ENDED successfully with result: " + result);
	}
	
	@Override
	public void testFinished(TestCaseResult result) {
		testResults.add(result);
		if (testResults.size() == 2) {
			evaluateStableTestResults(testResults);
			testResults.clear();
		}
	}

}
