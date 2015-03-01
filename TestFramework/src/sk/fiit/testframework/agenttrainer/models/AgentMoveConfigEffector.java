/**
 * Name:    AgentMoveConfigEffector.java
 * Created: 8.5.2011
 * 
 * @author: Roman Kovac
 */
package sk.fiit.testframework.agenttrainer.models;

public class AgentMoveConfigEffector extends Calculated {

	private Range value;
	private String name;

	public AgentMoveConfigEffector() {
		this.value = new Range();
	}

	public void setValue(Range value) {
		this.value = value;
	}

	public Range getValue() {
		return value;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
