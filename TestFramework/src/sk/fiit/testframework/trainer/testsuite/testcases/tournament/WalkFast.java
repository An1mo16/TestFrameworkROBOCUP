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
public class WalkFast implements Runnable, ITestCaseObserver {
	private static Logger logger = Logger.getLogger(WalkFast.class.getName());
	
	private List<TestCaseResult> testResults;
	
	public WalkFast() {
		testResults = new ArrayList<TestCaseResult>();
	}
	
	@Override
	public void run() {
		Implementation impl = ImplementationFactory.getImplementationInstance();
		
		impl.enqueueTestCase(new WalkFastTest(), this);
		impl.enqueueTestCase(new WalkFastTest(), this);
	}

	public void evaluateFastTestResults(List<TestCaseResult> results) {
		double result = 0;
		for (TestCaseResult testCaseResult : results) {
			result += testCaseResult.getFitness();
		}
		logger.info("TEST CASE ENDED successfully with result: " + result);
	}
	
	@Override
	public void testFinished(TestCaseResult result) {
		testResults.add(result);
		if (testResults.size() == 2) {
			evaluateFastTestResults(testResults);
			testResults.clear();
		}
	}

}
