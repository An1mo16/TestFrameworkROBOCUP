package sk.fiit.jim.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import sk.fiit.jim.agent.models.EnvironmentModel;

/**
 * Class used for logging
 *
 *@Title	Jim
 *@author	marosurbanec
 */
@SuppressWarnings("serial")
public class Log{
	
	StringBuilder buffer = new StringBuilder();
	private static final Log instance = new Log();
	private Map<LogType, Boolean> logTypesPermits = new HashMap<LogType, Boolean>(){{
		//log initialization messages even though we have no config yet
		put(LogType.INIT, true);
	}};
	PrintStream out = new PrintStream(System.out);
	PrintStream sysout = System.out;
	PrintStream syserr = System.err;
	
	private String pattern;
	LogLevel logLevel = LogLevel.LOG;
	
	
	private Log(){
		flushBufferAtExit();
	}
	
	private void flushBufferAtExit(){
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){
			public void run(){
				if (instance.out != null)
					instance.out.flush();
			}
		}));		
	}

	public static void setLoggable(LogType type, boolean shouldBeLogged){
		instance.logTypesPermits.put(type, shouldBeLogged);
	}
	
	public static boolean isLoggable(LogType type){
		return instance.logTypesPermits.containsKey(type) && instance.logTypesPermits.get(type);
	}
	
	public static void setOutput(String where) throws FileNotFoundException{
		if (instance.out != null) instance.out.flush();
		if ("FILE".equals(where)){
			instance.out = new PrintStream(new File("./"+instance.pattern));
			System.setErr(instance.out);
			System.setOut(instance.out);
		}
		else if ("CONSOLE".equals(where)){
			instance.out = new PrintStream(System.out);
			System.setErr(instance.syserr);
			System.setOut(instance.sysout);
		}
		else if ("NULL".equals(where)){
			instance.out = null;
			System.setErr(instance.syserr);
			System.setOut(instance.sysout);
		}
	}
	
	public static void setOutput(PrintStream output){
		instance.out = output;
	}
	
	public static void setPattern(String pattern){
		instance.pattern = pattern;
	}
	
	public static void setLogLevel(LogLevel level){
		instance.logLevel = level;
	}
	
	public static void log(LogType type, String format, Object...args){
		if (instance.out == null || !instance.logLevel.logsLog || !isLoggable(type))
			return;
		log(type, String.format(format, args));
	}
	
	public static void log(LogType type, String message){
		if (instance.out == null || !instance.logLevel.logsLog || !isLoggable(type))
			return;
		appendToBuffer(type, message);
	}
	
	public static void error(LogType type, String format, Object...args){
		if (instance.out == null || !isLoggable(type) || !instance.logLevel.logsError)
			return;
		error(type, String.format(format, args));
	}

	public static void error(LogType type, String message){
		if (instance.out == null || !isLoggable(type) || !instance.logLevel.logsError)
			return;
		appendToBuffer(type, "ERROR!!: "+message);
	}
	
	public static void debug(LogType type, String format, Object...args){
		if (instance.out == null || !instance.logLevel.logsDebug || !isLoggable(type))
			return;
		debug(type, String.format(format, args));
	}
	
	public static void debug(LogType type, String message){
		if (instance.out == null || !instance.logLevel.logsDebug || !isLoggable(type))
			return;
		appendToBuffer(type, message);
	}
	
	private static void appendToBuffer(LogType type, String message){
		instance.buffer.
			append(type.toString()).
			append('[').append(EnvironmentModel.SIMULATION_TIME).append(']').
			append(':').
		append(message);
		instance.out.println(instance.buffer.toString());
		instance.out.flush();
		instance.buffer.delete(0, instance.buffer.length());
	}
	
	@Override
	public String toString(){
		return new StringBuilder().
			append("Log").
			append('\n').
			append("Output: ").
			append(out).
			append("LogTypes: ").
			append(logTypesPermits).
			toString();
	}
}