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
public class KickDistance implements Runnable, ITestCaseObserver {
	private static Logger logger = Logger.getLogger(KickDistance.class.getName());

	private List<TestCaseResult> testResults;
	
	@Override
	public void run() {
		testResults = new ArrayList<TestCaseResult>();
		Implementation impl = ImplementationFactory.getImplementationInstance();
		
		impl.enqueueTestCase(new KickDistanceTest(), this);
		impl.enqueueTestCase(new KickDistanceTest(), this);
		impl.enqueueTestCase(new KickDistanceTest(), this);
		impl.enqueueTestCase(new KickDistanceTest(), this);
		impl.enqueueTestCase(new KickDistanceTest(), this);
	}

	public void evaluateResult(List<TestCaseResult> results) {
		double result = 0;
		for (TestCaseResult testCaseResult : results) {
			double fitnes = testCaseResult.getFitness();
			if (fitnes > result) result = fitnes;
		}
		logger.info("TEST CASE ENDED successfully with result: " + result);
	}
	
	@Override
	public void testFinished(TestCaseResult result) {
		testResults.add(result);
		if (testResults.size() == 5) {
			evaluateResult(testResults);
			testResults.clear();
		}
	}
	
}
