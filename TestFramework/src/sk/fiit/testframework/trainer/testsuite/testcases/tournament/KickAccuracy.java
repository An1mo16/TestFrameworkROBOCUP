package sk.fiit.testframework.trainer.testsuite.testcases.tournament;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import sk.fiit.testframework.init.Implementation;
import sk.fiit.testframework.init.ImplementationFactory;
import sk.fiit.testframework.trainer.testsuite.ITestCaseObserver;
import sk.fiit.testframework.trainer.testsuite.TestCaseResult;

/**
 * TODO: Replace with a brief purpose of class / interface.
 * 
 * @author Bimbo
 *
 */
public class KickAccuracy implements Runnable, ITestCaseObserver {
	private static int LOOPS = 5;
	
	private static Logger logger = Logger.getLogger(KickDistance.class.getName());
	private List<TestCaseResult> testResults;
	
	@Override
	public void run() {
		testResults = new ArrayList<TestCaseResult>();
		Implementation impl = ImplementationFactory.getImplementationInstance();
		
		for(int i=0;i<LOOPS;i++){
			impl.enqueueTestCase(new KickAccuracyTest(), this);	
		}
		
	}
	
	@Override
	public void testFinished(TestCaseResult result) {
		testResults.add(result);
		logger.log(Level.INFO,"Value returned by test "+testResults.size()+" of "+LOOPS+": "+result.getFitness());
		if (testResults.size() == LOOPS) {
			evaluateResult(testResults);
			testResults.clear();
		}
	}
	
	private void evaluateResult(List<TestCaseResult> results) {
		double result = Double.MAX_VALUE;
		for (TestCaseResult testCaseResult : results) {
			double fitnes = testCaseResult.getFitness();
			if (fitnes < result) result = fitnes;
		}
		logger.info("TEST CASE ENDED successfully with result: " + result);
	}

}
