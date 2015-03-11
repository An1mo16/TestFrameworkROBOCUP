/**
 * 
 */
package sk.fiit.jim.agent.communication.testframework;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import sk.fiit.jim.Settings;
import sk.fiit.jim.agent.AgentInfo;

/**
 * 
 *  TestFrameworkCommunication.java
 *  
 *@Title        Jim
 *@author       $Author: ivan $
 */
public class TestFrameworkCommunication {

	private static Logger logger = Logger.getLogger(TestFrameworkCommunication.class.getName());
	
	private static Socket serverSocket;
	private static Writer output;
	
	//aby sa nepokusal pripojit donekonecna, lebo to blokuje cely zvysok
	private static boolean failed = false;

	public static boolean initCommunication() {
		if (failed || !AgentInfo.hasAssignedSide)
			return false;
		try {
			if (!Settings.getBoolean("TestFramework_monitor_enable")) return false;
			String address = Settings.getString("TestFramework_monitor_address");
			int port = Settings.getInt("TestFramework_monitor_port");
			serverSocket = new Socket(address, port);
			output = new OutputStreamWriter(serverSocket.getOutputStream());
			sendMessage(new Message(AgentInfo.playerId, AgentInfo.team, AgentInfo.side).Init());
			return true;
		} catch (Exception e) { 
			//logger.log(Level.SEVERE, "Cannot connect to TestFramework monitor server", e);
		}
		failed = true;
		return false;
	}

	public static void sendMessage(Message message) {
		if (serverSocket == null) if (!initCommunication()) return;
		try {
			output.write(message.getMessage());
			output.write("\n");
			output.flush();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error sending message", e);
			try {serverSocket.close();} catch (Exception e2) {}
			serverSocket = null;
		}
	}
	
	public static void sendRawMessage(String message) {
		if (serverSocket == null) if (!initCommunication()) return;
		try {
			output.write("("+AgentInfo.playerId + " " + AgentInfo.team + ") ");
			output.write(message);
			output.write("\n");
			output.flush();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error sending message", e);
			try {serverSocket.close();} catch (Exception e2) {}
			serverSocket = null;
		}
	}

	public static void closeConnection() {
		try {
			output.close();
			serverSocket.close();
		} catch (IOException e ) {
			logger.log(Level.SEVERE, "Error closing connection to server", e);
			serverSocket = null;
		}
	}
	
}
