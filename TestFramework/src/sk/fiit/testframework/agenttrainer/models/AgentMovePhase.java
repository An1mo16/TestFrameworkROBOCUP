/**
 * Name:    AgentMovePhase.java
 * Created: 22.4.2011
 * 
 * @author: Roman Kovac
 */
package sk.fiit.testframework.agenttrainer.models;

import java.util.ArrayList;


/**
 * 
 * 
 * @author Roman Kovac
 *
 */
public class AgentMovePhase {

	private String name;
	private int duration;
	private ArrayList<AgentMoveEffector> effectors;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getDuration() {
		return duration;
	}

	public void setEffectors(ArrayList<AgentMoveEffector> effectors) {
		this.effectors = effectors;
	}

	public ArrayList<AgentMoveEffector> getEffectors() {
		return effectors;
	}
	
	
	
}
