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

public class StandUp implements Runnable, ITestCaseObserver {
	private static Logger logger = Logger.getLogger(StandUp.class.getName());
	
	private List<TestCaseResult> testResults;
	
	public StandUp() {
		testResults = new ArrayList<TestCaseResult>();
	}
	
	public void run() {
		Implementation impl = ImplementationFactory.getImplementationInstance();
		
		impl.enqueueTestCase(new StandUpTest(), this);
		impl.enqueueTestCase(new StandUpTest(), this);
		impl.enqueueTestCase(new StandUpTest(), this);
		impl.enqueueTestCase(new StandUpTest(), this);
		impl.enqueueTestCase(new StandUpTest(), this);
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
		if (testResults.size() == 5) { 
			evaluateFastTestResults(testResults);
			testResults.clear();
		}
	}

}
