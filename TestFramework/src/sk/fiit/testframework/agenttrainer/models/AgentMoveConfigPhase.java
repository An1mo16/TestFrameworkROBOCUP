/**
 * Name:    AgentMoveConfigPhase.java
 * Created: 30.4.2011
 * 
 * @author: Roman Kovac
 */
package sk.fiit.testframework.agenttrainer.models;

import java.util.ArrayList;

public class AgentMoveConfigPhase {

	private String name;
	private AgentMoveConfigDuration duration;
	private ArrayList<AgentMoveConfigEffector> effectors;

	public AgentMoveConfigPhase() {
		this.duration = new AgentMoveConfigDuration();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setDuration(AgentMoveConfigDuration duration) {
		this.duration = duration;
	}

	public AgentMoveConfigDuration getDuration() {
		return duration;
	}

	public void setEffectors(ArrayList<AgentMoveConfigEffector> effectors) {
		this.effectors = effectors;
	}

	public ArrayList<AgentMoveConfigEffector> getEffectors() {
		return effectors;
	}

}
