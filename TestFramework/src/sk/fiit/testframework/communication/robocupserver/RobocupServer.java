/**
 * Name:    Trainer.java
 * Created: Feb 27, 2011
 * 
 * @author: relation
 */
package sk.fiit.testframework.communication.robocupserver;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.jruby.embed.LocalVariableBehavior;
import org.jruby.embed.PathType;
import org.jruby.embed.ScriptingContainer;

import sk.fiit.robocup.library.annotations.UnderConstruction;
import sk.fiit.robocup.library.geometry.Point3D;
import sk.fiit.testframework.communication.agent.AgentData;
import sk.fiit.testframework.parsing.models.PlayMode;

/**
 * <code>Trainer</code> is used for sending commands to RoboCup 3D simulation
 * soccer server with aim to set specific state on server.
 * 
 * @author relation
 */
@UnderConstruction
public class RobocupServer {
	
	private static Logger logger = Logger.getLogger(RobocupServer.class.getName());
	
	private RobocupServerAddress address;
	private Socket socket;
	
	private static Map<String, RobocupServer> serverInstances = new HashMap<String, RobocupServer>();
	
	public static RobocupServer getServerInstance() throws IOException {
		return getServerInstance(RobocupServerAddress.getConfigServerAddress());
	}
	
	public static RobocupServer getServerInstance(RobocupServerAddress address) throws IOException {
		String idstring = address.getServerSocketAddress().getAddress() + ":" + address.getServerMonitorAddress().getPort();
		RobocupServer trainer = serverInstances.get(idstring);
		if (trainer == null) {
			trainer = new RobocupServer(address);
			serverInstances.put(idstring, trainer);
		}
		return trainer;
	}
	
	private RobocupServer(RobocupServerAddress address) throws IOException {
		this.address = address;
		this.socket = new Socket(address.getServerSocketAddress().getAddress(), address.getServerMonitorAddress().getPort());
	}

	private synchronized void performCommand(RobocupServerCommand command) throws IOException {
		if (!socket.isConnected()) {
			socket.connect(address.getServerMonitorAddress());
		}

		socket.getOutputStream().write(command.getAsBytes());
		socket.getOutputStream().flush();
		logger.finer("Sent command to server:" + command.getCommand());
	}
	
	public void setAgentPosition(AgentData agentData, Point3D position)
			throws IOException {
		RobocupServerCommand tc = new RobocupServerCommand.Agent(position, new Point3D(0,
				0, 0), agentData.getTeam().toString(), agentData.getUniformNumber());
		performCommand(tc);
	}

	public void setAgentPosition(String team, int playerid, Point3D position)
			throws IOException {
		RobocupServerCommand tc = new RobocupServerCommand.Agent(position, new Point3D(0,
				0, 0), team, playerid);
		performCommand(tc);
	}

	public void setBallPosition(Point3D position) throws IOException {
		RobocupServerCommand tc = new RobocupServerCommand.BallPosition(position);
		performCommand(tc);
	}

	public void setBallVelocity(Point3D velocity) throws IOException {
		RobocupServerCommand tc = new RobocupServerCommand.BallVelocity(velocity);
		performCommand(tc);
	}

	public void setBall(Point3D position, Point3D velocity) throws IOException {
		RobocupServerCommand tc = new RobocupServerCommand.Ball(position, velocity);
		performCommand(tc);
	}

	public void setPlayMode(PlayMode mode) throws IOException {
		RobocupServerCommand tc = new RobocupServerCommand.PlayMode(mode);
		performCommand(tc);
	}

}
