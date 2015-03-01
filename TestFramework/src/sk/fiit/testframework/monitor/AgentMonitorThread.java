/**
 * Name:    MonitorAgent.java
 * Created: Nov 22, 2011
 * 
 * @author: ivan
 */
package sk.fiit.testframework.monitor;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.net.util.Base64;

import sk.fiit.testframework.monitor.AgentMonitorMessage.Init;

public class AgentMonitorThread extends Thread {

	private static Logger logger = Logger.getLogger(AgentMonitor.class.getName());

	private Socket socket;
	private BufferedReader input;
	private AgentMonitor server;

	private Set<IAgentMonitorListener> listeners;

	public AgentMonitorThread(AgentMonitor server, Socket socket) {
		super("AgentMonitorThread");
		this.server = server;
		this.socket = socket;
		try {
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			logger.log(Level.WARNING,"ERROR reading from socket", e);
		}
	}

	@Override
	public  void run() {
		String inputLine;

		int uniform;
		String team;

		try {
			AgentMonitorMessage.Init message;
			String line = input.readLine();
			message = (Init) AgentMonitorMessage.parse(line);
			message.hostAddress = socket.getInetAddress().getHostAddress();
			
			uniform = message.uniform;
			team = message.teamName.trim();
			server.addAgentThread(uniform, team, this);
			notifyListeners(uniform, team, message);
			logger.fine("Agent connected " + socket.getInetAddress().getHostName() + ":" + socket.getPort() + " (" + uniform + " " + team +")");
		} catch (Exception e){
			logger.log(Level.WARNING,"ERROR while reading header info from socket: " + e.getMessage(), e);
			return;
		}

		try {
			AgentMonitorMessage message;
			// pomocne premenne pri citani dat (byte array encodovany v Base64 aby bolo mozne poslat ako string)
			boolean data = false;
			StringBuilder data_builder = null;
			String checksum_string = null;
			String firstLine = null;
			while ((inputLine = input.readLine()) != null) {
				// prvy riadok oznacuje ze sa budu citat data
				if (inputLine.endsWith("beginData")) {
					data = true;
					data_builder = new StringBuilder();
					firstLine = inputLine;
					continue;
				}
				if (data) {
					// druhy riadok je checksum pola bajtov (vytvoreny na klientskej strane)
					if (checksum_string == null) {
						checksum_string = inputLine;
						continue;
					}
					// nacitavanie stringu dat pokym sa nenarazi na tag endData
					if (inputLine.equals("endData")) {
						byte[] input_data = Base64.decodeBase64(data_builder.toString());
						MessageDigest md = MessageDigest.getInstance("SHA-1");
					    Formatter formatter = new Formatter();
					    for (byte b : md.digest(input_data)) {
					        formatter.format("%02x", b);
					    }
					    String checksum_of_data = formatter.toString();
					    
					    // DOLEZITE !!! -> potrebne mat WorldModel object niekde v path pre classes.
					    try {
					    	// kontrola checksum, vytvorenie a notify listenerov
					    	if (checksum_of_data.equals(checksum_string)) {
					    		ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(input_data));
					    		Object obj = input.readObject();
					    		message = AgentMonitorMessage.parse(firstLine, obj);
					    		message.hostAddress = socket.getInetAddress().getHostAddress();
					    		notifyListeners(uniform, team, message);
					    	} else {
					    		logger.log(Level.WARNING,"ERROR while reading from socket, checksum mismatch !");
					    	}
					    } catch (ClassNotFoundException e) {
					    	// ked bude WorldModel v class path tak odkomentovat tuto chybu !
//							logger.log(Level.WARNING,"ERROR while reading from socket, reading data - cannot read object", e);
						} 
					    
					    firstLine = null;
						data = false;
					    checksum_string = null;
						data_builder = null;
						continue;
					} else {
						data_builder.append(inputLine);
						continue;
					}
				}
				message = AgentMonitorMessage.parse(inputLine);
				message.hostAddress = socket.getInetAddress().getHostAddress();
				notifyListeners(uniform, team, message);
				logger.finest("Agent message: " + inputLine);
			}
			socket.close();
		} catch (IOException e) {
			logger.log(Level.WARNING,"ERROR while reading from socket", e);
		} catch (NoSuchAlgorithmException e) {
			logger.log(Level.WARNING,"ERROR while reading from socket, reading data - cannot create sha1", e);
		} finally {
			server.removeAgentThread(uniform, team, this);
		}
		logger.fine("Agent DISconnected " + socket.getInetAddress().getHostName() + ":" + socket.getPort());
		AgentMonitorMessage message = new AgentMonitorMessage() {};
		message.hostAddress = socket.getInetAddress().getHostName();
		message.uniform = uniform;
		message.teamName = team;
		message.type_flags = AgentMonitorMessage.TYPE_DESTROY;
		notifyListeners(uniform, team, message);
	}
	
	private void notifyListeners(int uniform, String team, AgentMonitorMessage message) {
		if ((listeners = AgentMonitor.getListenersForAllAgents()) != null) {
			int flags;
			for (IAgentMonitorListener listener : listeners) {
				flags = AgentMonitor.getListenerFlags(listener);
				if ((flags & message.type_flags) > 0)
					listener.receivedMessage(uniform, team, message);
			}
		}
		if ((listeners = AgentMonitor.getListenersForAgent(uniform, team)) != null) {
			int flags;
			for (IAgentMonitorListener listener : listeners) {
				flags = AgentMonitor.getListenerFlags(listener);
				if ((flags & message.type_flags) > 0)
					listener.receivedMessage(uniform, team, message);
			}
		}
	}
	
}