package sk.fiit.jim.agent.communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import static sk.fiit.jim.log.LogType.INIT;

import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.jim.agent.Planner;
import sk.fiit.jim.agent.parsing.Parser;
import sk.fiit.jim.code_review.Problem;
import sk.fiit.jim.log.Log;
import sk.fiit.jim.log.LogType;

/**
 *  Communication.java
 *  
 *  Class implements the low-level communication with the server - receives
 *  and delivers messages. Settings for the class are changed in
 *  ./scripts/config/settings.rb
 *
 *@Title	Jim
 *@author	marosurbanec
 *@author	Androids
 */
public class Communication {
	private static final String BYE  = "(bye)"; 
	private byte[] buffer = new byte[5120];
	private Socket socket;
	private String serverIp;
	private Integer port;
	private StringBuilder outMessageBuffer = new StringBuilder();
	
	private static Communication instance = new Communication();
	private DataInputStream input;
	private DataOutputStream output;
	private Parser parser = new Parser();
	
	/**
	 * Returns an instance of this singleton class 
	 */
	public static Communication getInstance(){
		return instance;
	}
	
	/**
	 * Start of communication. Firstly it attempts to connect to server, then 
	 * sets input and output streams. 
	 *
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public void start() throws UnknownHostException, IOException{
		try {
			socket = new Socket(serverIp, port);
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());
			registerSayingByeOnExit();			
			handshake();
		} catch (Exception e) {
			Log.error(LogType.INIT, "Unable to connect. Cause: %s", e.getMessage());
			System.exit(-1);
		}		
		mainLoop();		
	}
	
	/*
	 * Method used to restart the communication between the agent and the
	 * server. Added by Androids.	
	 */
/*	public void restart() {
		try {
			input.close();
			output.close();
			if (socket.isConnected()) {
				socket.close();
			}
			start();
		} catch (Exception e) {
			Log.error(LogType.INIT, "Unable to restart. Cause: %s", e.getMessage());
			System.exit(-1);
		}
	}*/	//commented by Juraj Belanji - not working properly
	//TODO: fix bugs with restart

	private void registerSayingByeOnExit(){
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){
			public void run(){
				try{
					transmit(BYE);
				} catch (IOException e){e.printStackTrace();}
			}
		}));
	}

	private void mainLoop() throws IOException {					
		while(true){			
			while (input.available() == -1) Thread.yield();
			String incoming = receive();			
			parser.parse(incoming);			
			Planner.proceed();			
			transmit(outMessageBuffer.append("(syn)").toString());			
			outMessageBuffer = new StringBuilder();
		}
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
	
	public void setServerIp(String serverIp){
		this.serverIp = serverIp;
	}
	
	public void setPort(Integer port){
		this.port = port;
	}
}