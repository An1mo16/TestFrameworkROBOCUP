package sk.fiit.testframework.monitor;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import sk.fiit.testframework.communication.robocupserver.RobocupServerAddress;
import sk.fiit.testframework.parsing.MessageParserFactory;
import sk.fiit.testframework.parsing.models.messages.Message;
import sk.fiit.testframework.parsing.models.messages.MessageType;
import sk.fiit.testframework.worldrepresentation.MessageInterpreter;
import sk.fiit.testframework.worldrepresentation.models.SimulationState;

public class RobocupMonitor extends Thread {
	
	private static Logger logger = Logger.getLogger(RobocupMonitor.class.getName());

    byte[] buffer = new byte[100000];
    int maxToRead = 1024;
    public Socket socket;
    String serverIp;
    Integer port;
    private DataInputStream input;
    private SimulationState state;
    
    private MessageInterpreter messageInterpreter = new MessageInterpreter();
    
    private static Map<String,RobocupMonitor> monitorInstances = new HashMap<String, RobocupMonitor>();
    
    public static RobocupMonitor getMonitorInstance() {
    	return getMonitorInstance(RobocupServerAddress.getConfigServerAddress());
    }
    
    public static RobocupMonitor getMonitorInstance(RobocupServerAddress adr) {
    	return getMonitorInstance(adr.getServerMonitorAddress().getHostName(), adr.getServerMonitorAddress().getPort());
    }
    
    public static synchronized RobocupMonitor getMonitorInstance(String serverIP, int port) {
    	RobocupMonitor monitor = monitorInstances.get(serverIP + ":" + port);
    	if (monitor == null) {
    		try {
				monitor = new RobocupMonitor(serverIP, port);
				monitorInstances.put(serverIP + ":" + port, monitor);
	    		logger.info("created monitor instance for " + serverIP + ":" + port);
			} catch (UnknownHostException e) {
				logger.log(Level.SEVERE, "Error creating monitor", e);
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Error creating monitor", e);
			}
    	}
    	return monitor;
    }
    
    private static synchronized void removeFromInstanceList(RobocupMonitor monitor) {
    	monitorInstances.remove(monitor.serverIp + ":" + monitor.port);
    }
    
    private RobocupMonitor(String serverIp, int port) throws UnknownHostException, IOException{
    	super("RobocupServer monitor");
        this.serverIp = serverIp;
        this.port = port;
        this.state = new SimulationState();
        this.socket = new Socket(serverIp, port);
    }

    public void run() {
        try {
            input = new DataInputStream(socket.getInputStream());
            mainLoop();
        } catch (EOFException e) {
        	logger.warning("RobocupServer closed connection (EOF)");
        } catch (Exception e) {
        	logger.log(Level.SEVERE, "mainloop error", e);
            return;
        }
    }

    private void mainLoop() throws IOException {
        while (true) {
            while (input.available() == -1) {
                Thread.yield();
            }
            logger.finest("Before message receive");
            String incoming = receive();
            logger.finest("Message received: " + incoming);
            
            if (incoming.length() > 0) {
                Message message = MessageParserFactory.getParser(incoming).parse(incoming);
                logger.finest("Message interpreter starting");
                messageInterpreter.interpret(message, state);
                logger.finest("Message interpreter finished");

                if (message.getType() == MessageType.GameState) {
                    setInformationComplete(true);
                }
            }
            
            if (isInterrupted()) {
            	RobocupMonitor.removeFromInstanceList(this);
            	logger.info("Monitor thread \"" + serverIp + ":" + port + "\" interrupted");
            	return;
            }
            
        }
    }

    private String receive() throws IOException {
        
        int messageLength = input.readInt();
        logger.finest("Read message length " + messageLength);

        if (Math.abs(messageLength) > buffer.length) {
            return "";
        }

        int bytesAlreadyRead = 0;
        int toRead = maxToRead > messageLength ? messageLength : maxToRead;
        while (messageLength > bytesAlreadyRead) {
            bytesAlreadyRead += input.read(buffer, bytesAlreadyRead, toRead);
            toRead = maxToRead > messageLength - bytesAlreadyRead ? messageLength - bytesAlreadyRead : maxToRead;
        }

        String incoming = new String(buffer, 0, messageLength);
        return incoming;
    }
   
    private boolean informationComplete;
    
    public boolean isInformationComplete() {
        return informationComplete;
    }

    /**
	 * @param informationComplete    the informationComplete to set
	 */
    public void setInformationComplete(boolean informationComplete) {
        this.informationComplete = informationComplete;
    }

    public SimulationState getSimulationState() {
        return state;
    }
}
