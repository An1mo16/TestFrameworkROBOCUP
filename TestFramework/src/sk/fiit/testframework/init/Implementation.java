/**
 * Name:    Implementation.java
 * Created: Nov 19, 2011
 * 
 * @author: ivan
 */
package sk.fiit.testframework.init;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

import sk.fiit.testframework.agenttrainer.AgentMoveTrainer;
import sk.fiit.testframework.agenttrainer.IAgentTrainerObserver;
import sk.fiit.testframework.trainer.testsuite.ITestCaseObserver;
import sk.fiit.testframework.trainer.testsuite.TestCase;

/**
 * TODO: Replace with a brief purpose of class / interface.
 * 
 * @author ivan
 *
 */
public abstract class Implementation {
	
	protected Logger logger = Logger.getLogger(getClass().getName());
	
	protected Map<TestCase, ITestCaseObserver> localTestCaseObservers = new HashMap<TestCase, ITestCaseObserver>();
	
	protected Queue<TestCase> ProcessQueue = new ConcurrentLinkedQueue<TestCase>();
	protected Queue<AgentMoveTrainer> TrainerQueue = new ConcurrentLinkedQueue<AgentMoveTrainer>();
	
	protected List<ITestCaseObserver> testCaseObservers = new Vector<ITestCaseObserver>();
	protected List<IAgentTrainerObserver> agentTrainerObservers = new Vector<IAgentTrainerObserver>();	
	
	public void enqueueTestCase(TestCase testCase, ITestCaseObserver observer) {
		if (testCase == null) return;
		synchronized(this) {
			ProcessQueue.add(testCase);
			if (observer != null) localTestCaseObservers.put(testCase, observer);
			this.notifyAll();
		}
		logger.fine("Added testcase to the queue: " + testCase.getClass().getName());
	}
	
	public void enqueueAgentMoveTrainer(AgentMoveTrainer trainer) {
		synchronized (this) {
			TrainerQueue.add(trainer);
			this.notifyAll();
		}
	}
	
	protected abstract void run(String[] args);
	public abstract void exit();	
}
