/**
 * Name:    ImplementationFactory.java
 * Created: Nov 19, 2011
 * 
 * @author: ivan
 */
package sk.fiit.testframework.init;

import java.lang.reflect.Constructor;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImplementationFactory {

	private static Logger logger = Logger.getLogger(ImplementationFactory.class.getName());
	
	private static Implementation implementationInstance;

	public static Implementation getImplementationInstance() {
		if (implementationInstance != null) return implementationInstance;
		String implementationClass = C.getProperty(C.IMPLEMENTATION);
		
		try {
			Constructor<?> c = Class.forName(implementationClass).getConstructor();
			implementationInstance = (Implementation) c.newInstance();
			logger.info("Created implementation instance: " + implementationInstance.getClass().getName());
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unable to instantiate implementation class: " + implementationClass, e);
			System.exit(1);
		}

		return implementationInstance;
	}

	private ImplementationFactory() {}

}
