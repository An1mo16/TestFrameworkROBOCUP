/**
 * Name:    AgentMonitor.java
 * Created: Nov 22, 2011
 * 
 * @author: ivan
 */
package sk.fiit.testframework.monitor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import sk.fiit.testframework.communication.agent.AgentJim;
import sk.fiit.testframework.communication.agent.AgentManager;
import sk.fiit.testframework.init.C;

/**
 * TODO: Replace with a brief purpose of class / interface.
 * 
 * @author ivan
 *
 */
public class AgentMonitor extends Thread {

	private static Logger logger = Logger.getLogger(AgentMonitor.class.getName());

	private static Map<Integer,AgentMonitor> agentServerInstance = Collections.synchronizedMap(new HashMap<Integer, AgentMonitor>());
	private static Map<String,Set<IAgentMonitorListener>> agentListeners = Collections.synchronizedMap(new HashMap<String,Set<IAgentMonitorListener>>());;
	private static Map<IAgentMonitorListener, Integer> listener_flags_relation = Collections.synchronizedMap(new HashMap<IAgentMonitorListener, Integer>());
	
	private Map<String,AgentMonitorThread> connectedAgentThreads;
	private ServerSocket serverSocket;

	public static AgentMonitor getInstanceForMpi(int offset) {
		return getInstance(C.getProperty(C.TESTFRAMEWORK_MONITOR_AGENT_IP), Integer.parseInt(C.getProperty(C.TESTFRAMEWORK_MONITOR_AGENT_PORT))+offset);
	}
	
	public static AgentMonitor getInstance() {
		return getInstance(C.getProperty(C.TESTFRAMEWORK_MONITOR_AGENT_IP), Integer.parseInt(C.getProperty(C.TESTFRAMEWORK_MONITOR_AGENT_PORT)));
	}

	public static AgentMonitor getInstance(String ip, int port) {
		AgentMonitor instance = agentServerInstance.get(port);
		if (instance == null) {
			AgentManager.getManager(); // make sure AgentManager is initialized
			try {
				instance = new AgentMonitor(ip, port);
				agentServerInstance.put(port, instance);
			} catch (Exception e) {
				return null;
			}
		}
		return instance;
	}

	private AgentMonitor(String ip, int port) throws IOException{
		super("Agent monitor");
		try {
			this.serverSocket = new ServerSocket(port);
			this.connectedAgentThreads = new HashMap<String,AgentMonitorThread>();
			logger.info("Created AgentServer instance for port: " + serverSocket.getInetAddress().getHostAddress() + ":" + port);
		} catch (IOException e) {
			logger.log(Level.WARNING,"ERROR initializing on port " + port, e);
			throw e;
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				Socket clientSocket = serverSocket.accept();
				new AgentMonitorThread(this, clientSocket).start();
			} catch (IOException e) {
				logger.log(Level.WARNING,"ERROR while listening on socket port", e);
			}
		}
	}

	protected void addAgentThread(int uniform, String team, AgentMonitorThread agent) {
		synchronized (connectedAgentThreads) {
			connectedAgentThreads.put(uniform+":"+team.toLowerCase().toString(), agent);
		}
	}

	public AgentMonitorThread getAgentThread(int uniform, String team) {
		AgentMonitorThread thread;
		synchronized (connectedAgentThreads) {
			thread = connectedAgentThreads.get(uniform+":"+team.toLowerCase().toString());
		}
		return thread;
	}

	protected void removeAgentThread(int uniform, String team, AgentMonitorThread agent) {
		synchronized (connectedAgentThreads) {
			connectedAgentThreads.remove(uniform+":"+team.toLowerCase().toString());
		}
	}

	public static Set<IAgentMonitorListener> getListenersForAllAgents() {
		return agentListeners.get("ALL");
	}
	
	public static Set<IAgentMonitorListener> getListenersForAgent(int uniform, String team) {
		return agentListeners.get(uniform+team);
	}
	
	public static void setMessageListener(IAgentMonitorListener listener, int message_type_flags) {
		Set<IAgentMonitorListener> listeners = agentListeners.get("ALL");
		if (listeners == null) {
			listeners = new HashSet<IAgentMonitorListener>();
			listeners.add(listener);
			agentListeners.put("ALL", listeners);
		} else {
			listeners.add(listener);
		}
		listener_flags_relation.put(listener, message_type_flags);
	}
	
	public static void removeMessageListener(IAgentMonitorListener listener) {
		Set<IAgentMonitorListener> listeners = agentListeners.get("ALL");
		if (listeners == null) {
			return;
		} else {
			listeners.remove(listener);
		}
		listener_flags_relation.remove(listener);
	}

	public static void setMessageListener(AgentJim agent,IAgentMonitorListener listener, int message_type_flags) {
		setMessageListener(agent.getAgentData().getUniformNumber(), 
				agent.getAgentData().getTeamName(),
				listener,
				message_type_flags);
	}
	
	public static void setMessageListener(int uniform, String team, IAgentMonitorListener listener, int message_type_flags) {
		Set<IAgentMonitorListener> listeners = agentListeners.get(uniform+team);
		if (listeners == null) {
			listeners = new HashSet<IAgentMonitorListener>();
			listeners.add(listener);
			agentListeners.put(uniform+team, listeners);
		} else {
			listeners.add(listener);
		}
		listener_flags_relation.put(listener, message_type_flags);
	}

	public static void removeMessageListener(int uniform, String team, IAgentMonitorListener listener) {
		Set<IAgentMonitorListener> listeners = agentListeners.get(uniform+team);
		if (listeners == null) {
			return;
		} else {
			listeners.remove(listener);
		}
		listener_flags_relation.remove(listener);
	}
	
	public static int getListenerFlags(IAgentMonitorListener listener) {
		return listener_flags_relation.get(listener);
	}
	
}
