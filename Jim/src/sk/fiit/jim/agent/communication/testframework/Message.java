/**
 * 
 */
package sk.fiit.jim.agent.communication.testframework;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.util.Formatter;

import org.apache.commons.net.util.Base64;

import sk.fiit.jim.Settings;
import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.jim.agent.Side;


/**
 * 
 *  Message.java
 *  
 *@Title        Jim
 *@author       $Author: ??? $
 */
public class Message {

	private String message;
	
	/**
	 * Constructs new message with playerId, team of agent and side which agent plays on.
	 */
	public Message() {
		message = "("+AgentInfo.playerId + " " + AgentInfo.team +  " " + AgentInfo.side + ")";
	}
	/**
	 * Constructs new message with uniform number, team of agent and side which agent plays on.
	 */
	public Message(int uniform, String team, Side side) {
		message = "(" + uniform + " " + team + " " + side + ")";
	}
	
	/**
	 * Returns the message. 
	 *
	 * @return
	 */
	public String getMessage() { return message; }
	
	public Message Init() {
		if (Settings.getBoolean("Tftp_enable")) {
			Init(Settings.getInt("Tftp_port"));
		} else {
			Init(false);
		}
		return this;
	}
	
	public Message Init(boolean tftpServerEnabled) {
		message += " init tftp" + String.valueOf(tftpServerEnabled).toLowerCase();
		if (tftpServerEnabled) message += " " + Init(Settings.getInt("Tftp_port")); 
		return this;
	}
	
	public Message Init(int tftpServerPort) {
		message += " init tftptrue " + tftpServerPort;
		return this;
	}
	
	/**
	 * Returns new instance of HighSkill. 
	 *
	 * @return
	 */
	public HighSkill HighSkill() {
		return new HighSkill();
	}
	
	/**
	 * 
	 *  
	 *@Title        Jim
	 *@author       $Author: ??? $
	 */
	public class HighSkill {
		public HighSkill() {
			message += " highskill";
		}
		
		/**
		 * Creates the message that HighSkill started with
		 * move name and time when it started. 
		 *
		 * @param move_name
		 * @param time
		 * @return
		 */
		public Message Start(String move_name, double time) {
			message += " start " + move_name + " " + time;
			return Message.this;
		}
		/**
		 * Creates the message that HighSkill stopped with
		 * move name and time when it started. 
		 *
		 * @param move_name
		 * @param time
		 * @return
		 */
		public Message Stop(String move_name, double time) {
			message += " stop " + move_name + " " + time;
			return Message.this;
		}
	}
	
	public WorldModel WorldModel() {
		return new WorldModel();
	}
	
	public class WorldModel {
		public WorldModel() {
			message += " worldmodel";
		}
		
		public Message changed(sk.fiit.jim.agent.models.WorldModel model) {
			message += " beginData\n";
			
			try {
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(outStream);
				out.writeObject(model);
				out.close();
				
				
				
				byte[] data = outStream.toByteArray();
				String to_send = Base64.encodeBase64String(data);
				outStream.close();
				
				MessageDigest md = MessageDigest.getInstance("SHA-1");
			    Formatter formatter = new Formatter();
			    for (byte b : md.digest(data)) {
			        formatter.format("%02x", b);
			    }
			    String checksum = formatter.toString();
			    message += checksum + "\n";
				message += to_send;
				message += "endData";
			} catch (Exception e) {
				// TODO proper error logging
			}
			
			return Message.this;
		}
	}
	
}