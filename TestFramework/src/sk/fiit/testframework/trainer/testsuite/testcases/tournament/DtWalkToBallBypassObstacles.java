package sk.fiit.testframework.trainer.testsuite.testcases.tournament;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import sk.fiit.testframework.GEP.desiciontreeGEP.*;
import sk.fiit.testframework.communication.agent.AgentJim;
import sk.fiit.testframework.communication.agent.AgentManager;
import sk.fiit.testframework.init.Implementation;
import sk.fiit.testframework.init.ImplementationFactory;
import sk.fiit.testframework.trainer.testsuite.ITestCaseObserver;
import sk.fiit.testframework.trainer.testsuite.TestCaseResult;

/**
 * 
 * @author Július Skrisa
 *
 */
public class DtWalkToBallBypassObstacles implements Runnable, ITestCaseObserver {
	private static Logger logger = Logger.getLogger(DtWalkToBall.class.getName());
	private List<TestCaseResult> testResults;
	Implementation impl = ImplementationFactory.getImplementationInstance();
	@Override
	public void run() {
		
		
		GP ga = new GP("dtWalk2Ball", 5000,50);
		while(!ga.ConditionSatisfied()){
			
			ga.ReplaceXmlInData();

			ga.Selection(testResults);
			
			//ga.Crossover();
			
			ga.Mutate();
			
			testResults = new ArrayList<TestCaseResult>();
					
			for(int i = 0; i < ga.populationLength; i++){
				ga.DataToXml(i);
				impl.enqueueTestCase(new DtWalkToBallBypassObstaclesTest(ga.Population.get(i)), this);	 // tu sa vytvori parameter kde sa vlozi nazov xml suboru kde je rozhodovaci strom
			}
			while(testResults.size() != ga.populationLength){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			ga.SaveBestFitnessFile(testResults);
			
		}
	}
	
	@Override
	public void testFinished(TestCaseResult result) {
		testResults.add(result);
		
	/*	logger.log(Level.INFO,"Value returned by test "+testResults.size()+" of "+LOOPS+": "+result.getFitness());
		if (testResults.size() == LOOPS) {
			evaluateResult(testResults);
			testResults.clear();
		}*/
		
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
