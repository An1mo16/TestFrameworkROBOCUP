package sk.fiit.jim.log;

import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.bsf.util.IOUtils;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 *  LogTest.java
 *
 *@Title	Jim
 *@author	marosurbanec
 */
public class LogTest{
	ByteArrayOutputStream out;
	
	@Before
	public void setUp(){
		out = new ByteArrayOutputStream();
		Log.setOutput(new PrintStream(out));
		Log.setLoggable(LogType.OTHER, true);
	}
	
	@Test
	public void debug(){
		logWithLevel(LogLevel.DEBUG, LogLevel.DEBUG, "test");
		assertThat(content(), containsString("test"));
		
		logWithLevel(LogLevel.LOG, LogLevel.DEBUG, "test2");
		logWithLevel(LogLevel.ERROR, LogLevel.DEBUG, "test2");
		assertThat(content(), not(containsString("test2")));
	}
	
	@Test
	public void log(){
		logWithLevel(LogLevel.DEBUG, LogLevel.LOG, "LOG_AT_DEBUG");
		assertThat(content(), containsString("LOG_AT_DEBUG"));
		
		logWithLevel(LogLevel.LOG, LogLevel.LOG, "LOG_AT_LOG");
		assertThat(content(), containsString("LOG_AT_LOG"));
		
		logWithLevel(LogLevel.ERROR, LogLevel.LOG, "LOG_AT_ERROR");
		assertThat(content(), not(containsString("LOG_AT_ERROR")));
	}
	
	@Test
	public void error(){
		logWithLevel(LogLevel.DEBUG, LogLevel.ERROR, "ERROR_AT_DEBUG");
		logWithLevel(LogLevel.LOG, LogLevel.ERROR, "ERROR_AT_LOG");
		logWithLevel(LogLevel.ERROR, LogLevel.ERROR, "ERROR_AT_ERROR");
		assertThat(content(), allOf(containsString("AT_DEBUG"), containsString("AT_LOG"), containsString("AT_ERROR")));
	}
	
	@Test
	public void logPermits(){
		logWithLevel(LogLevel.LOG, LogLevel.LOG, "At true");
		Log.setLoggable(LogType.OTHER, false);
		logWithLevel(LogLevel.LOG, LogLevel.LOG, "At false");
		assertThat(content(), both(containsString("At true")).and(not(containsString("At false"))));
	}
	
	@Test
	public void logPermits2(){
		assertTrue(Log.isLoggable(LogType.OTHER));
		//not set yet - expect false
		assertFalse(Log.isLoggable(LogType.AGENT_MODEL));
		Log.setLoggable(LogType.OTHER, false);
		assertFalse(Log.isLoggable(LogType.OTHER));
	}
	
	@Test
	public void testFileLogging() throws IOException{
		Log.setPattern("fixtures/test_log.txt");
		Log.setOutput("FILE");
		logWithLevel(LogLevel.LOG, LogLevel.LOG, "file_message");
		Log.setOutput("NULL");
		logWithLevel(LogLevel.LOG, LogLevel.LOG, "null_message");
		Log.setOutput("CONSOLE");
		logWithLevel(LogLevel.LOG, LogLevel.LOG, "console_message");
		String content = IOUtils.getStringFromReader(new FileReader("./fixtures/test_log.txt"));
		assertThat(content, both(containsString("file_message")).and(not(containsString("null_message"))).and(not(containsString("console_message"))));
	}
	
	private String content(){
		return out.toString();
	}
	
	private void logWithLevel(LogLevel logLevel, LogLevel method, String message){
		Log.setLogLevel(logLevel);
		switch (method)
		{
			case DEBUG : Log.debug(LogType.OTHER, message); break;
			case LOG : Log.log(LogType.OTHER, message); break;
			case ERROR : Log.error(LogType.OTHER, message); break;
			default : break;
		}
	}
}