package sk.fiit.testframework.communication.agent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

import org.apache.commons.net.tftp.TFTPClient;

import sk.fiit.jim.agent.models.WorldModel;
import sk.fiit.robocup.library.annotations.TestCovered;
import sk.fiit.robocup.library.annotations.UnderConstruction;
import sk.fiit.robocup.library.geometry.Vector3;
import sk.fiit.robocup.library.init.Script;

/**
 * 
 * Represents a single agent instance that's connected to the test framework.
 * It can be an agent launched locally by the test framework, or an externally launched
 * agent that's connected to the test framework.
 * 
 */
@UnderConstruction
public class AgentJim {	
	
	private static Logger logger = Logger.getLogger(AgentJim.class.getName());
	private MessageSender ms = null;
	/**
	 * Used for accumulating all of the agent's standard output text
	 */
	private StringBuilder output = new StringBuilder();
	private int sceneGraphPlayer = -1;
	
	private WorldModel worldModel;
	
	/**
	 * Represents a side that the agent plays on. SimSpark's network protocol
	 * uses this information along with an agent's unifrom number to identify
	 * an agent. 
	 */
	public enum TeamSide {
		RIGHT {
			public String toString() {
				//must be exactly like this!
				//http://simspark.sourceforge.net/wiki/index.php/Network_Protocol
				return "Right";
			}
		},
		
		LEFT {
			public String toString() {
				return "Left"; //must be exactly like this!
			}
		}
	}
	
    private AgentData agentData;
    private TFTPClient tftpClient = new TFTPClient();
    private int tftpPort;
    private String tftpIP;
    
    /**
     * Constructor used when we want to attach to an running Jim agent 
     * connected to the server on address <code>serverAddress</code>. 
     * This is especially useful when debugging, because we don't have to 
     * run new instance of agent everytime we want to use testcase. We can
     * simply run agent in shell and by this constructor treat is like a
     * regular runned agent.
     * 
     * @param agentData
     */
    public AgentJim(AgentData agentData, String tftpIP, int tftpPort) {
        this.agentData = agentData;
        this.tftpPort = tftpPort;
        this.tftpIP = tftpIP;
        
        ms = new MessageSender();
		ms.run();
    }
    
    /**
     * Returns the string builder used for accumulating all of the agent's 
     * standard output text. Only works for agents launched by the test framework.
     */
    public StringBuilder getStdout() {
    	return output;
    }

    public AgentData getAgentData() {
        return agentData;
    }
    
    public int getTftpPort() {
        return tftpPort;
    }

    public String getTftpIP() {
        return tftpIP;
    }

    public InetSocketAddress getTFTPAddress() {
        // TODO: Only localhost now | is that still true???
        return new InetSocketAddress(getTftpIP(), getTftpPort());
    }
    
    //TODO: what's the purpose of these 3 methods?
    //Looks like they're remains from an older version.
    //As far as I know, the agent must always be connected to the server.
    public boolean isConnectedToServer() {
        throw new UnsupportedOperationException();
    }

    public void connectToServer() {
        throw new UnsupportedOperationException();
    }

    public void disconnectFromServer() {
        ms.close();
    }
    public void setDtHghSkill(String highSkill, String dtXmlName, String player) throws IOException {
    	StringBuilder sb = new StringBuilder();
    	sb.append("#player=" + player);
    	sb.append("\n");
		sb.append("-" + highSkill + "=" + dtXmlName);
		sendCommand(sb.toString());
    }

	/**
	 * Sends a file to remote TFTP server. 
	 * 
	 * @param host	Remote TFTP server.
	 * @param saveFileAs	Determines how file on remote TFTP server will be 
	 * 						saved.
	 * @param inputStream	Input stream of data.
	 * @throws IOException	
	 */
	@TestCovered
	private void sendFile(String saveFileAs, InputStream inputStream) throws IOException {
		tftpClient.open();
		tftpClient.sendFile(saveFileAs, TFTPClient.BINARY_MODE, inputStream, getTFTPAddress().getAddress(), getTFTPAddress().getPort());
		tftpClient.close();
	}
	
	/**
	 * Invokes executing of ruby script on special TFTP server.
	 * Used for internal purposes with agent Jim.
	 * 
	 * @param script	UTF-8 String text to be executed on server side.
	 * @throws IOException
	 */	
	private void sendCommand(String command) throws IOException {
		//ByteArrayInputStream bais = new ByteArrayInputStream(command.getBytes("UTF-8"));
		/*tftpClient.open();
		tftpClient.sendFile("setHighSkill", TFTPClient.BINARY_MODE, bais, "127.0.0.1", getTFTPAddress().getPort());
		tftpClient.close();*/
		
		
		ms.sendMessage(command);
		logger.finer("HighSkill dt set");
	}
	
	/**
	 * Returns a string that is used as a unique identifier of the agent
	 * in the AgentManager class. It consists of the agent's uniform number
	 * followed by its team name (no spaces)
	 */
	public String toString() {
		return String.valueOf(agentData.getUniformNumber()) + agentData.getTeamName();
	}

	public int getSceneGraphPlayer() {
		return sceneGraphPlayer;
	}

	public void setSceneGraphPlayer(int sceneGraphPlayer) {
		this.sceneGraphPlayer = sceneGraphPlayer;
	}
	
	public void setWorldModel(WorldModel model) {
		worldModel = model;
	}
	
	public WorldModel getWorldModel() {
		return worldModel;
	}

	public void setCoordinates(Vector3 actPos) {
		StringBuilder sb = new StringBuilder();
    	sb.append("$xr=" + actPos.getX() + ";yr=" + actPos.getY());
		try {
			sendCommand(sb.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
