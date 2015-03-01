/**
 * Name:    IAgentServerThreadListener.java
 * Created: Nov 22, 2011
 * 
 * @author: ivan
 */
package sk.fiit.testframework.monitor;

/**
 * TODO: Replace with a brief purpose of class / interface.
 * 
 * @author ivan
 *
 */
public interface IAgentMonitorListener {
	public void receivedMessage(int uniform, String team, AgentMonitorMessage message);
}
