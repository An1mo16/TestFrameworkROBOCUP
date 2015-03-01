/**
 * Name:    AgentMove.java
 * Created: 25.4.2011
 * 
 * @author: Roman Kovac
 */
package sk.fiit.testframework.agenttrainer.models;

import java.util.ArrayList;

import org.w3c.dom.Document;

/**
 * Represents a class holding information about agent move created by
 * AgentMoveReader.
 * 
 * @author Roman Kovac
 * 
 */
public class AgentMove {

	private Document doc;
	private ArrayList<AgentMovePhase> phases;
	
	public void setDoc(Document doc) {
		this.doc = doc;
	}

	public Document getDoc() {
		return doc;
	}

	public void setPhases(ArrayList<AgentMovePhase> phases) {
		this.phases = phases;
	}

	public ArrayList<AgentMovePhase> getPhases() {
		return phases;
	}
}
