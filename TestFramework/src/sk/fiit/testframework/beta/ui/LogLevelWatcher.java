/**
 * Name:    LogLeverWatcher.java
 * Created: Mar 4, 2012
 * 
 * @author: tomasbolecek
 */
package sk.fiit.testframework.beta.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;

/**
 * TODO: Replace with a brief purpose of class / interface.
 * 
 * @author tomasbolecek
 *
 */
public class LogLevelWatcher implements ActionListener {

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String source = ((JRadioButton)e.getSource()).getName();
		if(!((JRadioButton) e.getSource()).isSelected()) {
			//this one is not selected. ignore this call
			return;
		}
		source  = source.substring(source.length() - 1);
		int level = Integer.parseInt(source);
		Level logLevel;
		switch(level) 
		{
		case 1:
			logLevel = Level.FINEST;
			break;
		case 2:
			logLevel = Level.FINER;
			break;
		case 3:
			logLevel = Level.FINE;
			break;
		case 4:
			logLevel = Level.CONFIG;
			break;
		case 5:
			logLevel = Level.INFO;
			break;
		case 6:
			logLevel = Level.WARNING;
			break;
		case 7: 
			logLevel = Level.SEVERE;
			break;
			default :
			throw new RuntimeException("Unknown enum value for java.util.logging.level");
		}
		Logger.getLogger("").setLevel(logLevel);
		Logger.getLogger("sk.fiit.testframework.beta.ui.LogLevelWatcher").fine("Logging level changed");
	}
}
