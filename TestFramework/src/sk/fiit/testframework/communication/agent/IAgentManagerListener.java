package sk.fiit.testframework.communication.agent;

/**
 * Interface for listening to events related to the agents' life cycle
 */
public interface IAgentManagerListener {
	/**
	 * Called when a new agent has been added to the agent manager.
	 * Could be an externally launched agent that has just connected,
	 * or one that was launched locally by the agent manager.
	 */
	public void agentAdded(AgentJim agent);
	/**
	 * <p>Called when a locally connected agent has been removed.</p>
	 * <p>TODO: Also call it when an externally launched agent disconnects.</p>
	 */
	public void agentRemoved(AgentJim agent);
	/**
	 * Called for every line of text that the agent writes to its standard output.
	 * Only works for agents launched locally by the agent manager.
	 */
	public void agentOutputLine(AgentJim agent, String line);
}
