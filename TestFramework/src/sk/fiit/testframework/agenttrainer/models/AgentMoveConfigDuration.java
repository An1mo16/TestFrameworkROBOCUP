/**
 * Name:    AgentMoveConfigDuration.java
 * Created: 8.5.2011
 * 
 * @author: Roman Kovac
 */
package sk.fiit.testframework.agenttrainer.models;

public class AgentMoveConfigDuration extends Calculated {

	private Range value;

	public AgentMoveConfigDuration() {
		this.value = new Range();
	}

	public void setValue(Range value) {
		this.value = value;
	}

	public Range getValue() {
		return value;
	}

}
