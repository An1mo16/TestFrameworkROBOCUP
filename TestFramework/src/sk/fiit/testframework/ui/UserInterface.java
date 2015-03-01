/**
 * Name:    UserInterface.java
 * Created: Nov 19, 2011
 * 
 * @author: ivan
 */
package sk.fiit.testframework.ui;

import sk.fiit.testframework.agenttrainer.IAgentTrainerObserver;
import sk.fiit.testframework.trainer.testsuite.ITestCaseObserver;

/**
 * TODO: Replace with a brief purpose of class / interface.
 * 
 * @author ivan
 *
 */
public interface UserInterface extends ITestCaseObserver, IAgentTrainerObserver {
	
	public abstract void start();
	public boolean shoudExitOnEmptyQueue();
	
}
