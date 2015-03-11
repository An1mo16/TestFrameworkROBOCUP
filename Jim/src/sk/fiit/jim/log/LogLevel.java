package sk.fiit.jim.log;

/**
 *  LogLevel.java
 *
 *@Title	Jim
 *@author	marosurbanec
 */
public enum LogLevel {
	ERROR(false, false, true),
	LOG(false, true, true),
	DEBUG(true, true, true);

	private LogLevel(boolean debug, boolean log, boolean error){
		this.logsDebug = debug;
		this.logsLog = log;
		this.logsError = error;
	}
	
	public final boolean logsDebug;
	public final boolean logsLog;
	public final boolean logsError;
}