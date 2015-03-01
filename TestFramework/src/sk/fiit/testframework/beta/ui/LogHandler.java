/**
 * Name:    LogHandler.java
 * Created: Mar 4, 2012
 * 
 * @author: tomasbolecek
 */
package sk.fiit.testframework.beta.ui;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import javax.swing.text.*;

/**
 * TODO: Replace with a brief purpose of class / interface.
 * 
 * @author tomasbolecek
 *
 */
public class LogHandler extends Handler {

	private JTextComponent textOutput;
	private StringBuilder messages;
	/**
	 * 
	 */
	public LogHandler(JTextComponent textOutput) {
		this.textOutput = textOutput;
		messages = new StringBuilder();
	}

	/* (non-Javadoc)
	 * @see java.util.logging.Handler#close()
	 */
	@Override
	public void close() throws SecurityException {
		
	}

	/* (non-Javadoc)
	 * @see java.util.logging.Handler#flush()
	 */
	@Override
	public void flush() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see java.util.logging.Handler#publish(java.util.logging.LogRecord)
	 */
	@Override
	public void publish(LogRecord arg0) {		
		messages.append(parseRecord(arg0));
		textOutput.setText(messages.toString());
		int start = java.lang.Math.max(messages.length() - 1200, 0);
		int end = java.lang.Math.min(start + 1200, messages.length());
		messages = new StringBuilder(messages.substring(start, end));
		
	}
	
	private String parseRecord(LogRecord record) {
		String retVal = record.getLevel().toString();
		retVal += ": ";
		retVal += record.getMessage();
		retVal += '\n';
		return retVal;
	}

}
