/**
 * Name:    Message.java
 * Created: Dec 1, 2011
 * 
 * @author: ivan
 */
package sk.fiit.testframework.monitor;

import java.lang.reflect.Constructor;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AgentMonitorMessage {

	private static Logger logger = Logger.getLogger(AgentMonitorMessage.class.getName());
	
	
	public static final int TYPE_ALL  		= 15;	// 1111
	public static final int TYPE_INIT  		= 4;	// 0100
	public static final int TYPE_DESTROY	= 2;	// 0010
	public static final int TYPE_HIGHSKILL 	= 1;	// 0001
	public static final int TYPE_WORLD_MODEL= 8;	// 1000
	
	public int uniform;
	public String teamName;
	public String teamSide;
	public int type_flags = 0;
	public String hostAddress;
	
	protected void init(Stack<String> tokens) {
		String init = tokens.pop();
		init = init.substring(1, init.length()-1); //odstranenie zatvoriek
		Scanner scanner = new Scanner(init);
		uniform = scanner.nextInt();
		teamName = scanner.next();
		teamSide = scanner.next();
	}

	public static class Init extends AgentMonitorMessage {
		
		public boolean tftp_enabled;
		public int tftp_port;
		
		public void init(Stack<String> tokens) {
			type_flags |= TYPE_INIT;
			super.init(tokens);
		}
		
		public static class TftpTrue extends Init {
			public TftpTrue(Stack<String> tokens) {
				tftp_enabled = true;
				tftp_port = Integer.valueOf(tokens.pop());
				super.init(tokens);
			}
		}
		
		public static class TftpFalse extends Init {
			public TftpFalse(Stack<String> tokens) {
				tftp_enabled = false;
				super.init(tokens);
			}
		}
		
	}
	
	public static class HighSkill extends AgentMonitorMessage {

		public String move_name;
		public double player_time;
		public ACTION action;
		public enum ACTION {
			start, stop
		}
		
		public void init(Stack<String> tokens) {
			player_time = Double.parseDouble(tokens.pop());
			move_name = tokens.pop();
			type_flags |= TYPE_HIGHSKILL;
			super.init(tokens);
		}

		public static class Start extends HighSkill {
			public Start(Stack<String> tokens) {
				action = ACTION.start;
				super.init(tokens);
			}
		}

		public static class Stop extends HighSkill {
			public Stop(Stack<String> tokens) {
				action = ACTION.stop;
				super.init(tokens);
			}
		}		
	}

	public static class WorldModel extends AgentMonitorMessage {
		// WorldModel objekt este nieje v classpath - zakomentovane aby sa nerozbila funkcionalita
		public sk.fiit.jim.agent.models.WorldModel model = null;
		
		public WorldModel(Stack<String> tokens, Object model) {
			this.model = (sk.fiit.jim.agent.models.WorldModel) model;
			tokens.pop(); // string begindata
			tokens.pop(); // string worldmodel
			type_flags |= TYPE_WORLD_MODEL;
			super.init(tokens);
		}
	}
	
	public static AgentMonitorMessage parse(String firstLine, Object deserialized) {
		// zatial takyto typ spravy je platny len s worldModel
		// do buducna zuniverzalnit na posielanie aj inych objektov
		
		String msg = firstLine.substring(firstLine.indexOf(')') + 2); // get rid of (1 left)
		StringTokenizer tokenizer = new StringTokenizer(msg);
		String token;
		Stack<String> unusedtokens = new Stack<String>();
		unusedtokens.add(firstLine.substring(0, firstLine.indexOf(')') + 1));
		
		while (tokenizer.hasMoreTokens()) {
			token = tokenizer.nextToken();
			unusedtokens.add(token);
		}
		
		AgentMonitorMessage.WorldModel message = new WorldModel(unusedtokens, deserialized);
		return message;
	}
	
	public static AgentMonitorMessage parse(String input) {
		String msg = input.substring(input.indexOf(')') + 2); // get rid of (1 left)
		Class<?> targetClass = AgentMonitorMessage.class;
		StringTokenizer tokenizer = new StringTokenizer(msg);
		String token;
		boolean change;
		Stack<String> unusedtokens = new Stack<String>();
		unusedtokens.add(input.substring(0, input.indexOf(')') + 1));

		while (tokenizer.hasMoreTokens()) {
			token = tokenizer.nextToken();
			change = false;
			for (Class<?> current_class  : targetClass.getDeclaredClasses()) {
				if (current_class.getSimpleName().toLowerCase().equals(token)) {
					targetClass = current_class;
					change = true;
					break;
				}
			}
			if (change == false) unusedtokens.add(token);
		}

		try {
			Constructor<AgentMonitorMessage> constructor = (Constructor<AgentMonitorMessage>) targetClass.getConstructor(Stack.class);
			AgentMonitorMessage message = constructor.newInstance(unusedtokens);
			return message;
		} catch (Exception e) {
			logger.log(Level.WARNING, "Could not parse message", e);
		}

		return null;
	}

	

	@Override
	public String toString() {
		return "AgentMonitorMessage [uniform=" + uniform + ", teamName="
				+ teamName + ", teamSide=" + teamSide + ", type_flags="
				+ type_flags + ", hostAddress=" + hostAddress + "]";
	}
}