package sk.fiit.testframework.ui;

import java.util.LinkedList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import sk.fiit.testframework.communication.agent.AgentManager;
import sk.fiit.testframework.communication.agent.AgentJim;
import sk.fiit.testframework.communication.agent.IAgentManagerListener;

/**
 * Combo box model containing all agents managed by the test framework
 */
public class AgentComboModel implements ComboBoxModel, IAgentManagerListener {
	
	private List<ListDataListener> listeners;
	private AgentJim selectedAgent = null;
	
	public AgentComboModel() {
		listeners = new LinkedList<ListDataListener>();
		//automatically listen for newly added and removed agents
		AgentManager.getManager().addAgentManagerListener(this);
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		listeners.add(l);
	}

	@Override
	public AgentJim getElementAt(int at) {
		return AgentManager.getManager().getAgentByOrder(at);
	}

	@Override
	public int getSize() {
		return AgentManager.getManager().getAgentCount();
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		listeners.remove(l);
	}

	@Override
	public Object getSelectedItem() {
		if (getSize() == 0)
			return null;
		if (selectedAgent == null)
			return getElementAt(0);
		return selectedAgent;
	}

	@Override
	public void setSelectedItem(Object obj) {
		selectedAgent = (AgentJim)obj;
	}

	@Override
	public void agentAdded(AgentJim agent) {
		for (ListDataListener l : listeners) {
			l.intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, getSize()-1, getSize()));
		}
	}

	@Override
	public void agentRemoved(AgentJim agent) {
		for (ListDataListener l : listeners) {
			l.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, getSize()));
		}
		
		if (getSelectedItem() == agent) {
			setSelectedItem(AgentManager.getManager().getAgentByOrder(0));
		}
	}

	@Override
	public void agentOutputLine(AgentJim agent, String line) { }
}
