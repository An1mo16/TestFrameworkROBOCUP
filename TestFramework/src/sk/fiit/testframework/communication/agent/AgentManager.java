/**
 * Name:    AgentManager.java
 * Created: Dec 6, 2011
 * 
 * @author: ivan
 */
package sk.fiit.testframework.communication.agent;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import sk.fiit.jim.agent.models.WorldModel;
import sk.fiit.testframework.communication.agent.AgentJim.TeamSide;
import sk.fiit.testframework.init.C;
import sk.fiit.testframework.monitor.AgentMonitor;
import sk.fiit.testframework.monitor.AgentMonitorMessage;
import sk.fiit.testframework.monitor.IAgentMonitorListener;
import sk.fiit.testframework.worldrepresentation.models.Player;

/**
 * 
 * <p>Manages agent instances that are connected to the test framework.
 * Handles agents that are launched externally after they connect to the test framework,
 * and can also launch agents locally itself. Agents are represented as {@link AgentJim} objects.</p>
 * <p>A new agent is launched by calling the {@link #getAgent} method with a uniform number
 * and team name that aren't taken yet, with the blocking parameter set to true.</p>
 * <p>Agents that are launched locally have the advantage of having all their
 * standard output redirected to the test framework, where it can be handled by the GUI</p>
 *
 */
public class AgentManager implements IAgentMonitorListener {

	private Logger logger = Logger.getLogger(AgentManager.class.getName());

	private static AgentManager agentManager = new AgentManager();

	/**
	 * Maps the agents' unique names to their process objects. Only for agents launched
	 * by the test framework. The unique name consists of the agent's uniform number
	 * followed by its team name (no spaces).
	 */
	private Map<String, Process> runningProcesses = new HashMap<String, Process>();
	
	/**
	 * <p>Maps the agents' unique names to their agent objects.
	 * The unique name consists of the agent's uniform number
	 * followed by its team name (no spaces).</p>
	 * Unlike runningProcesses, this also contains agents that were launched externally.
	 */
	private Map<String, AgentJim> runningAgents = new HashMap<String, AgentJim>();
	
	//for linking Player objects to the correct AgentJim objects
	private List<AgentJim> orderedAgents = new ArrayList<AgentJim>();
	private List<Integer> queuedScenePlayers = new ArrayList<Integer>();
	private Map<Integer, Player> scenePlayerMap = new HashMap<Integer, Player>();
	
	private long agentWaitTime = 3000;
	
	private List<IAgentManagerListener> managerListeners;
	
	
	private AgentManager() {
		managerListeners = new ArrayList<IAgentManagerListener>();
		AgentMonitor.setMessageListener(this, AgentMonitorMessage.TYPE_INIT | AgentMonitorMessage.TYPE_DESTROY | AgentMonitorMessage.TYPE_WORLD_MODEL);
	}

	/**
	 * Returns the instance of this singleton class
	 */
	public static AgentManager getManager() {
		return agentManager;
	}

	/**
	 * 
	 * <p>Returns the object of an agent determined by its uniform number and team name.</p>
	 * <p>Can also launch a new agent when blocking is set to true. That is a blocking
	 * operation which takes quite a long time (roughly 10 seconds).</p>
	 * 
	 * @param uniform uniform number of the agent, must be unique within a team
	 * @param team name of the team that the agent plays for
	 * @param blocking if true, a new agent will be started locally in case an agent with the given uniform number and team doesn't exist 
	 * @return the agent's object, or null in case of an error
	 */
	public AgentJim getAgent(int uniform, String team, boolean blocking) {
		AgentJim jim = runningAgents.get(uniform+team);
		if (jim == null && blocking) {
			synchronized (this) {
				try {
					long timeout = agentWaitTime;
					long startTime = System.currentTimeMillis();
					int port = getFreeTFTPPort("localhost");
					startAgent(uniform, team, true, port);
					
					while ((jim = runningAgents.get(uniform+team)) == null) {
						this.wait(timeout);
						if (System.currentTimeMillis() - startTime > timeout) break;
					}
				} catch (InterruptedException e) {
					return null;
				}
			}
		}
		return jim;
	}
	
	/**
	 * Returns the number of currently managed agents
	 */
	public int getAgentCount() {
		return runningAgents.size();
	}
	
	/**
	 * Returns an agent determined by its position in the internal structures, or null
	 * if no such agent exists.
	 * Useful for picking an agent out of a list (like in a combo box model). 
	 */
	public AgentJim getAgentByOrder(int order) {
		if (order < 0 || order >= orderedAgents.size())
			return null;
		return orderedAgents.get(order);
	}

	/**
	 * Handles a message received from the agent
	 * 
	 * @param uniform the uniform number of the agent who sent the message
	 * @param team the team of the agent who sent the message
	 * @param message the message's object
	 */
	@Override
	public void receivedMessage(int uniform, String team, AgentMonitorMessage message) {
		team = team.trim();
		switch (message.type_flags) {
			case AgentMonitorMessage.TYPE_INIT:
				AgentMonitorMessage.Init msgInit = (AgentMonitorMessage.Init) message;
				AgentData data = new AgentData(message.uniform, TeamSide.valueOf(msgInit.teamSide), msgInit.teamName );
				synchronized (this) {
					AgentJim newAgent = new AgentJim(data, "localhost", msgInit.tftp_port);
					runningAgents.put(uniform+team, newAgent);
					orderedAgents.add(newAgent);
					if (!queuedScenePlayers.isEmpty()) {
						Integer pOrder = queuedScenePlayers.remove(0);
						Player p = scenePlayerMap.get(pOrder);
						p.setAssociatedAgent(newAgent);
						newAgent.setSceneGraphPlayer(p.getOrder());
					}
					logger.info("Connected agent with uniform " + msgInit.uniform + " teamName " + msgInit.teamName + " teamSide " + msgInit.teamSide + " debug tftp enabled: " + msgInit.tftp_enabled + " with port " + msgInit.tftp_port);
					this.notifyAll();
					for (IAgentManagerListener listener : managerListeners) {
						listener.agentAdded(newAgent);
					}
				}
				break;
			case AgentMonitorMessage.TYPE_DESTROY:
				runningAgents.remove(message.uniform+message.teamName);
				runningProcesses.remove(message.uniform+message.teamName);
				break;
				
			case AgentMonitorMessage.TYPE_WORLD_MODEL:
				WorldModel model = ((AgentMonitorMessage.WorldModel)message).model;
				AgentJim agent = runningAgents.get(message.uniform + message.teamName);
				agent.setWorldModel(model);
				break;
		}
	}
	
	
	/**
	 * 
	 * Starts a new agent with the specified parameters
	 * 
	 * @param uniform uniform number of the agent
	 * @param team name of the agent's team
	 * @param tftp_enabled whether to enable TFTP server on the agent
	 * @param tftp_port port on which the agent's TFTP server should listen
	 */

	public void startAgent(final int uniform, final String team, boolean tftp_enabled, int tftp_port) {		

		String javaHome = System.getProperty("java.home");
        String javaBin = javaHome +
                File.separator + "bin" +
                File.separator + "java";
        String classpath = System.getProperty("java.class.path");
        String className = "sk.fiit.jim.init.Main";
        
		logger.info(String.format("Starting Jim agent with uniform %d team %s tftp enabled %s on port %d", uniform, team, String.valueOf(tftp_enabled), tftp_port));
		List<String> command = new ArrayList<String>();
		command.add(javaBin);
		command.add("-cp");
		command.add(classpath);
		command.add(className);
		command.add("-runGui=false");
		command.add("-uniform=" + String.valueOf(uniform));
		command.add("-team=" + team);
		command.add("-TestFramework_monitor_enable=true");
		command.add("-TestFramework_monitor_address=" + C.getProperty(C.TESTFRAMEWORK_MONITOR_AGENT_IP));
		command.add("-TestFramework_monitor_port=" + C.getProperty(C.TESTFRAMEWORK_MONITOR_AGENT_PORT));
		if (tftp_enabled) {
			command.add("-Tftp_enable=true");
			command.add("-Tftp_port=" + String.valueOf(tftp_port));
		}
		try {
			logger.finer("Running: " + command.toString().replace(",", " "));
			ProcessBuilder builder = new ProcessBuilder(command);
			//if the command needs to be called in a certain working directory
			if (!C.getProperty(C.PROPERTIES_ROBOCUP_PLAYER_DIR).isEmpty())
				builder.directory(new File(C.getProperty(C.PROPERTIES_ROBOCUP_PLAYER_DIR)));
			builder.redirectErrorStream(true);
			//builder.command(command);
			//start the agent
			
			final Process agentProcess = builder.start();
			runningProcesses.put(uniform+team, agentProcess);
			
			//asynchronous reading and handling of the agent's output
			Thread reader = new Thread(new Runnable() {
				@Override
				public void run() {
					readForever(String.valueOf(uniform)+team, new BufferedReader(new InputStreamReader(agentProcess.getInputStream())));
				}
			},"Agent manager process reader: " + uniform + " " + team);
			reader.start();
		} catch (Exception e) {
			logger.log(Level.WARNING, "Error starting player", e);
		}
	}

	/**
	 * 
	 * <p>Handles the standard output of an agent process started by the test framework.</p>
	 * <p>Notifies all the manager listeners about each new line of output.</p>
	 * 
	 * @param agentName unique identifier of the agent, which is its uniform number followed by its team name (no spaces)
	 * @param stream input stream connected to the agent's standard output
	 */
	private void readForever(String agentName, BufferedReader stream) {
		try {
			String line = null;
			do {
				line = stream.readLine();
				AgentJim agent = runningAgents.get(agentName);
				if (agent != null) {
					agent.getStdout().append(line + "\n");
					for (IAgentManagerListener listener : managerListeners) {
						listener.agentOutputLine(agent, line);
					}
				}
			}while(line != null);
			stream.close();
		} catch (IOException e) {
			logger.warning("Error while reading agent's output: " + e.getMessage());
		}
	}
	
	/**
	 * 
	 * <p>Removes an agent that was started by the test framework and terminates its process.
	 * Does nothing for agents that were not launched by the test framework.</p>
	 * <p>TODO: send an exit command to agents not launched by the test framework</p>
	 * 
	 * @param agent the agent to remove
	 */
	public void removeAgent(AgentJim agent) {
		Process process = runningProcesses.get(agent.toString());
		if (process != null) {
			process.destroy();
			runningProcesses.remove(agent.toString());
			runningAgents.remove(agent.toString());
			orderedAgents.remove(agent);
			for (IAgentManagerListener listener : managerListeners) {
				listener.agentRemoved(agent);
			}
		}
	}

	/**
	 * Destroys all agent processes launched by the test framework
	 */
	public void shutDownAgents() {
		logger.info("Shutting down all started agents (" + String.valueOf(runningProcesses.size()) + ")");
		try {
			Collection<Process> proc = runningProcesses.values();
			for (Process process : proc) {
				process.destroy();
			}
			runningProcesses.clear();
			runningAgents.clear();
			orderedAgents.clear();
		} catch (Exception e ){
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the lowest expected free port for a TFTP server on a certain IP address
	 */
	public int getFreeTFTPPort(String ip) {
		int port = 3071;
		while (port <= 65535) {
			if (isPortAvailableOnLocalhost(port) && port != 3100 && port != 3200) {
				if (isPortOnIpAvailableOnRunningAgents(ip, port)) {
					return port;
				}
			}
			port++;
		}
		return 0;
	}

	/** 
	 * Checks if port is available for use on localhost.
	 * 
	 * @author xsuchac
	 * @author A55-Kickers
	 * 
	 * @param port - port number.
	 * @return true if port is available for use on localhost, else returns false.
	 */
	private boolean isPortAvailableOnLocalhost(int port) {
	    if (port < 3071 || port > 65535) {
	        throw new IllegalArgumentException("Invalid start port: " + port);
	    }

	    ServerSocket ss = null;
	    DatagramSocket ds = null;
	    try {
	        ss = new ServerSocket(port);
	        ss.setReuseAddress(true);
	        ds = new DatagramSocket(port);
	        ds.setReuseAddress(true);
	        return true;
	    } catch (IOException e) {
	    } finally {
	        if (ds != null) {
	            ds.close();
	        }

	        if (ss != null) {
	            try {
	                ss.close();
	            } catch (IOException e) {
	                /* should not be thrown */
	            }
	        }
	    }
	    return false;
	}
	
	/** 
	 * Checks if running agents do not use port on IP.
	 * 
	 * @author xsuchac
	 * @author A55-Kickers
	 * 
	 * @param ip - string of specific IP address.
	 * @param port - port number.
	 * @return true if port on ip is not used by any of running agents, else returns false.
	 */
	private boolean isPortOnIpAvailableOnRunningAgents(String ip, int port) {
		for (AgentJim jim : runningAgents.values()) {
			if ((jim.getTftpIP().equals(ip) && jim.getTftpPort() == port)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Returns the lowest expected free uniform number in the specified team.
	 * Assumes all agents to be managed by this test framework instance. 
	 */
	public int getFreeUniform(String team) {
		int uniform = 1;
		while (true) {
			AgentJim agent = runningAgents.get(uniform+team);
			if (agent == null)
				return uniform;
			uniform++;
		}
	}
	
	public void addAgentManagerListener(IAgentManagerListener listener) {
		managerListeners.add(listener);
	}
	
	public void removeAgentManagerListener(IAgentManagerListener listener) {
		managerListeners.remove(listener);
	}
	
	public void enqueueScenePlayer(Player p) {
		if (!queuedScenePlayers.contains(p.getOrder())) {
			queuedScenePlayers.add(p.getOrder());
		}
		scenePlayerMap.put(p.getOrder(), p);
	}
	
	public void setAgentWaitTime(long waitTime) {
		agentWaitTime = waitTime;
	}
}
