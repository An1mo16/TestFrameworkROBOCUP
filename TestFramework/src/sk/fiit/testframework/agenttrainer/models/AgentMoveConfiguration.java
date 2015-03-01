/**
 * Name:    AgentMoveTrainerConfiguration.java
 * Created: 25.4.2011
 * 
 * @author: Roman Kovac
 */
package sk.fiit.testframework.agenttrainer.models;

import java.util.ArrayList;

/**
 * TODO: Replace with a brief purpose of class / interface.
 * 
 * @author Roman Kovac
 *
 */
public class AgentMoveConfiguration {

	private ArrayList<AgentMoveConfigPhase> phases;

	public void setPhases(ArrayList<AgentMoveConfigPhase> phases) {
		this.phases = phases;
	}

	public ArrayList<AgentMoveConfigPhase> getPhases() {
		return phases;
	}
	
}
