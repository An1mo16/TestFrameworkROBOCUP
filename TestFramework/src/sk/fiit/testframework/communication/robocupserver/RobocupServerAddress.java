/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.fiit.testframework.communication.robocupserver;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import sk.fiit.testframework.init.C;

/**
 *
 * @author relation
 */
public class RobocupServerAddress {

	private static Logger logger = Logger.getLogger(RobocupServerAddress.class.getName());
	
    
    private InetAddress address;
    private int serverPort;
    private int monitorPort;

    public static RobocupServerAddress getConfigServerAddress() {
    	String ip = C.getProperty(C.PROPERTIES_ROBOCUP_SERVER_IP);
		int portMonitor = Integer.parseInt(C.getProperty(C.PROPERTIES_ROBOCUP_SERVER_MONITOR_PORT));
		int portPlayer = Integer.parseInt(C.getProperty(C.PROPERTIES_ROBOCUP_SERVER_PLAYER_PORT));
		try {
			return new RobocupServerAddress(Inet4Address.getByName(ip), portPlayer, portMonitor);
		} catch (UnknownHostException e) {
			logger.log(Level.SEVERE, "Unknown Host", e);
			return null;
		}
    }
    
    public RobocupServerAddress(InetAddress address, int serverPort, int monitorPort) {
        this.address = address;
        this.serverPort = serverPort;
        this.monitorPort = monitorPort;
    }

    public InetSocketAddress getServerSocketAddress() {
        return new InetSocketAddress(address, serverPort);
    }

    public InetSocketAddress getServerMonitorAddress() {
        return new InetSocketAddress(address, monitorPort);
    }

}
