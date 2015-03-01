/**
 * Name:    Constants.java
 * Created: Nov 17, 2011
 * 
 * @author: ivan
 */
package sk.fiit.testframework.init;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * TODO: Replace with a brief purpose of class / interface.
 * 
 * @author ivan
 *
 */
public class C {
	
	private static Logger logger = Logger.getLogger(C.class.getName());
	
	public static final Properties properties;
	
	public static final String PROPERTIES_ROBOCUP_SERVER_COMMAND = "robocup.server.command";	
	public static final String PROPERTIES_ROBOCUP_SERVER_KILLCOMMAND = "robocup.server.killcommand";
	public static final String PROPERTIES_ROBOCUP_SERVER_IP = "robocup.server.ip";
	public static final String PROPERTIES_ROBOCUP_SERVER_MONITOR_PORT = "robocup.server.port.monitor";
	public static final String PROPERTIES_ROBOCUP_SERVER_PLAYER_PORT = "robocup.server.port.player";
	
	public static final String PROPERTIES_ROBOCUP_PLAYER_DIR = "robocup.player.dir";
	public static final String PROPERTIES_ROBOCUP_PLAYER_COMMAND = "robocup.player.command";
	public static final String PROPERTIES_ROBOCUP_PLAYER_COMMAND_NON_WINDOWS = "robocup.player.command_non_windows";
	
	public static final String TESTFRAMEWORK_MONITOR_AGENT_IP = "testframework.monitorAgent.ip";
	public static final String TESTFRAMEWORK_MONITOR_AGENT_PORT = "testframework.monitorAgent.port";
	
	public static final String USER_INTERFACE = "userInterface";
	public static final String IMPLEMENTATION = "implementation";
	
	static {
		properties = new Properties();
		try {
			properties.load(C.class.getClassLoader().getResourceAsStream("sk/fiit/testframework/init/default.properties"));
			logger.info("Properties loaded: sk/fiit/testframework/init/default.properties");
			
		} catch (IOException e1) {
			logger.warning("error reading default properties file");
		}
		try {
			properties.load(new FileInputStream("configuration.properties"));
			logger.info("Properties loaded: configuration.properties");
		} catch (IOException e) {
			logger.warning("error reading properties file - using defaults");
		}
	}
	
	public static String getProperty(String key) {
		return properties.getProperty(key);
	}
	
	public static void setProperty(String key, String value) {
		properties.setProperty(key, value);
	}
}
