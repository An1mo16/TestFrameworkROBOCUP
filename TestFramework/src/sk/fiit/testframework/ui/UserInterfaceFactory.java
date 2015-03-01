/**
 * Name:    UserInterfaceFactory.java
 * Created: Nov 19, 2011
 * 
 * @author: ivan
 */
package sk.fiit.testframework.ui;

import java.lang.reflect.Constructor;
import java.util.logging.Level;
import java.util.logging.Logger;

import sk.fiit.testframework.communication.robocupserver.RobocupServerAddress;
import sk.fiit.testframework.init.C;

/**
 * TODO: Replace with a brief purpose of class / interface.
 * 
 * @author ivan
 *
 */
public class UserInterfaceFactory {

	private static Logger logger = Logger.getLogger(UserInterfaceFactory.class.getName());
	private static UserInterface userInterfaceInstance;
	
	public static UserInterface getUserInterfaceInstance() {
		return getUserInterfaceInstance(RobocupServerAddress.getConfigServerAddress());
	}
	
	public static UserInterface getUserInterfaceInstance(RobocupServerAddress rsa) {
		if (userInterfaceInstance != null) return userInterfaceInstance;
		
		String interface_class_name = C.getProperty(C.USER_INTERFACE);
		
		try {
			Constructor<?> c = Class.forName(interface_class_name).getConstructor(RobocupServerAddress.class);
			userInterfaceInstance = (UserInterface) c.newInstance(rsa);
			logger.info("Created user interface instance: " + userInterfaceInstance.getClass().getName());
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unable to instantiate user interface class: " + interface_class_name, e);
		}
		
		return userInterfaceInstance;
	}
	
	private UserInterfaceFactory() {}
	
}
