/**
 * Name:    Main.java
 * Created: Nov 17, 2011
 * 
 * @author: ivan
 */
package sk.fiit.testframework.init;

import java.util.logging.Level;
import java.util.logging.Logger;

import sk.fiit.testframework.ui.UserInterface;

/**
 * TODO: Replace with a brief purpose of class / interface.
 * 
 * @author ivan
 *
 */
public class Init {
	private static Logger logger = Logger.getLogger(Init.class.getName());
	public static UserInterface interfaceInstance;
	
	//Todo #Task(After migrating from Ruby to Java test framework is not working because of some ruby dependencies.) #Solver() #Priority() | xmarkech 2013-12-10T20:38:13.4560000Z
	
	public static void main(String args[]) {
		Logger tmpLogger = logger;
		
		while(tmpLogger != null  && tmpLogger.getLevel() == null) {
			tmpLogger = tmpLogger.getParent();
		}
		logger.log(Level.INFO, tmpLogger.getLevel().toString());
		logger.log(Level.INFO, "CULIMessage: " + tmpLogger.getName());
		int argument_pointer = 0;
		
		/* Parse command line arguments
		 * arguments in the form:
		 * -key=value
		 * where key corresponds to the setting name.
		 * Will override the default settings.
		 */
		while (argument_pointer < args.length) {
			String key = args[argument_pointer++];
			
			if (key.startsWith("-")) {
				key = key.substring(1);
				String value;
				if (key.contains("=")) {
					value = key.substring(key.indexOf("=") + 1);
					key = key.substring(0, key.indexOf("="));
				} else {
					value = args[argument_pointer++];
				}
				if (value.startsWith("\"") || value.startsWith("'")) {
					value = value.substring(1, value.length()-1);
				}
				C.setProperty(key, value);
			}	
		}
		
		// initialize testframework 		
		logger.info("initializing TestFramework");		
		Implementation implementation = ImplementationFactory.getImplementationInstance();
		
		// run testframework		
		logger.info("starting implementation");		
		implementation.run(args);
		
	}
}
