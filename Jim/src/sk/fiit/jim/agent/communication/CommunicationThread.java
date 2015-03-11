package sk.fiit.jim.agent.communication;

import static sk.fiit.jim.log.LogType.INIT;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.jim.agent.Planner;
import sk.fiit.jim.agent.parsing.Parser;
import sk.fiit.jim.code_review.Problem;
import sk.fiit.jim.code_review.UnderConstruction;
import sk.fiit.jim.log.Log;
import sk.fiit.jim.log.LogType;

/**
 *  
 *  <p>Not used anymore. Can be safely removed. The same functionality is implemented in the {@link Communication} class</p>
 *  
 *  <p>Class used for creating one communication thread in the testing framework.
 *  The two communicating sides are the agent and the testing framework.</p>
 *  
 *@Title	Jim
 *@author	Androids
 */
@Deprecated
@UnderConstruction(todo = { "refactor! please refactor!!!" })
public class CommunicationThread extends Thread {
	private static final String BYE  = "(bye)"; 
	byte[] buffer = new byte[5120];
	Socket socket;
	String serverIp;
	Integer port;
	StringBuilder outMessageBuffer = new StringBuilder();
	
	private DataInputStream input;
	private DataOutputStream output;
	private Parser parser = new Parser();
	
	private static boolean restartRequested = false;
	
	public static synchronized boolean isRestartRequested() {
		return restartRequested;
	}
	
	public static synchronized void requestRestart() {
		restartRequested = true;
	}
	
	private static synchronized void setRequestProcessed() {
		restartRequested = false;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				Log.log(INIT, "Attempting to log to: %s:%d", serverIp, port);
				socket = new Socket("localhost", 3100);
				input = new DataInputStream(socket.getInputStream());
				output = new DataOutputStream(socket.getOutputStream());
				registerSayingByeOnExit();
				handshake();
			} catch (Exception e) {
				Log.error(LogType.INIT, "Unable to connect. Cause: %s", e.getMessage());
				System.exit(-1);
			}
			
			try {
				while(true){
					if (restartRequested) {
						setRequestProcessed();
						try {
							input.close();
							output.close();
							if (socket.isConnected()) {
								socket.close();
							}
						} catch (Exception e) {
							Log.error(LogType.INIT, "Unable to restart. Cause: %s", e.getMessage());
							System.exit(-1);
						}
						break;
					}
					while (input.available() == -1) Thread.yield();
					String incoming = receive();
					parser.parse(incoming);
					Planner.proceed();
					transmit(outMessageBuffer.append("(syn)").toString());
					outMessageBuffer = new StringBuilder();
				}
			} catch (Exception e) {
				Log.error(LogType.OTHER, "Main loop exception:", e.getMessage());
			}
		}
	}
	
	private void registerSayingByeOnExit(){
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){
			public void run(){
				try{
					transmit(BYE);
				}catch (IOException e){e.printStackTrace();}
			}
		}));
	}

	private void handshake() throws IOException {
		transmit("(scene rsg/agent/nao/nao.rsg)");
		
		String handShake = receive();
		System.out.println(handShake);
		String initMessage = String.format("(init (unum %d)(teamname %s))", AgentInfo.playerId, AgentInfo.team);
		transmit(initMessage);
	}

	@Problem("Document!")
	public void addToMessage(String message){
		outMessageBuffer.append(message);
	}
	
	private void transmit(String what) throws IOException{
		Log.log(LogType.OUTCOMING_MESSAGE, what);
		output.writeInt(what.length());
		output.write(what.getBytes());
		output.flush();
	}
	
	private String receive() throws IOException{
		//first number of the message denotes the length of the message
		int messageLength = input.readInt();
		if (Math.abs(messageLength) > buffer.length)
			return "";
		
		/*
		 * InputStream.read is kinda weird. It may not read all of
		 * the bytes you request it to read. Instead, it may read 0-n
		 *  bytes and return the actual number of bytes it read.
		 */
		int bytesAlreadyRead = 0;
		while (messageLength > bytesAlreadyRead)
		  bytesAlreadyRead += input.read(buffer, bytesAlreadyRead, messageLength - bytesAlreadyRead);
		
		String incoming = new String(buffer, 0, messageLength);
		Log.log(LogType.INCOMING_MESSAGE, incoming);
		return incoming;
	}
}