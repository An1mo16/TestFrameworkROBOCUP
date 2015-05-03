package sk.fiit.testframework.trainer.testsuite.testcases.tournament;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import sk.fiit.testframework.communication.agent.AgentJim;
import sk.fiit.testframework.communication.agent.AgentManager;
import sk.fiit.testframework.gp.desiciontreegp.*;
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
	
	@Override
	public void run() {
		
		Implementation impl = ImplementationFactory.getImplementationInstance();
		
		//Vytvorenie triedy vykonavajucej geneticke operacie
		Gp ga = new Gp("dtWalk2Ball", 5000,50);
		
		while(!ga.ConditionSatisfied()){
			
			//Vygenerovat Data na zaklade XML suborov s rozhodovaniami
			ga.ReplaceXmlInData();

			ga.Selection(testResults);
			
			//ga.Crossover();
			
			ga.Mutate();
			
			testResults = new ArrayList<TestCaseResult>();
				
			//Pre kazdeho jedinca  obnovit XML subor z dát a spustit overovanie
			for(int i = 0; i < ga.populationLength; i++){
				ga.DataToXml(i);
				impl.enqueueTestCase(new DtWalkToBallBypassObstaclesTest(ga.Population.get(i)), this);	 // tu sa vytvori parameter kde sa vlozi nazov xml suboru kde je rozhodovaci strom
				
				
			}
			
			//Ak su ukoncene vsetky testy 
			while(testResults.size() != ga.populationLength){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			//Ulozit subory s najlesou fitness hodnotou zalogovat vysledky
			ga.SaveBestFitnessFile(testResults);
			
		}
	}
	
	@Override
	public void testFinished(TestCaseResult result) {
		testResults.add(result);
		
	}
}
