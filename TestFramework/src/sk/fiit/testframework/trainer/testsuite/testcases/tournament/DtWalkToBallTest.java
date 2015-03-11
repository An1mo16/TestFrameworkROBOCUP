package sk.fiit.testframework.trainer.testsuite.testcases.tournament;

import static sk.fiit.robocup.library.geometry.Vector3D.cartesian;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import sk.fiit.robocup.library.geometry.Angles;
import sk.fiit.robocup.library.geometry.Point3D;
import sk.fiit.robocup.library.geometry.Vector3;
import sk.fiit.robocup.library.geometry.Vector3D;
import sk.fiit.testframework.communication.agent.AgentData;
import sk.fiit.testframework.communication.agent.AgentJim;
import sk.fiit.testframework.communication.agent.AgentJim.TeamSide;
import sk.fiit.testframework.communication.agent.AgentManager;
import sk.fiit.testframework.communication.robocupserver.RobocupServer;
import sk.fiit.testframework.monitor.RobocupMonitor;
import sk.fiit.testframework.parsing.models.EnvironmentPart;
import sk.fiit.testframework.parsing.models.PlayMode;
import sk.fiit.testframework.trainer.testsuite.TestCase;
import sk.fiit.testframework.trainer.testsuite.TestCaseResult;
import sk.fiit.testframework.worldrepresentation.models.Player;
import sk.fiit.testframework.worldrepresentation.models.SimulationState;

/**
 * TODO: Replace with a brief purpose of class / interface.
 * 
 * @author Bimbo
 *
 */
public class DtWalkToBallTest extends TestCase {
	//how much time between start of playmode and start of testing - if ball started to move (ball kicked)
	//because ball moves a bit after set to initial point
	private static double TIME_FOR_INITIAL_BALL_MOVE = 2.5;
	//minimal ball move set by rules
	private static double MIN_BALL_DIST = 1.5;
	//value for failed kick
	private static double FAILED = 360;
	//time for kick
	private int TIME = 300;
	
	private double fieldLength;
	private double fieldWidth;
	
	private double ballTouchedTime = -1;
	private double TIME_BETWEEN_KICK_AND_TELEPORT = 1;
	private boolean agentTeleportedAfterKick = false;
	
//	private Vector3 initPosBall = new Vector3(0.2,0,0.2);
//	private Vector3 initPosAgent = new Vector3(0,0,0.4);
	private Vector3 initPosBall = new Vector3(5.0,0,0.2);
	private Vector3 PosBall2 = new Vector3(0,5.0,0.2);
	private double fitness1 = 0;
	private Vector3 initPosAgent = new Vector3(0,0,0.4);
	boolean secondRound = false;
	boolean endFlag = false;
	
	private static Logger logger = Logger.getLogger(KickDistanceTest.class.getName());

	private AgentData agentData;
	private AgentJim agent;
	private boolean started = false;
	private double startTime;
	
	private boolean ballMoved = false;
	private boolean playerFalled = false;
	private boolean timeExpired = false;
	
	public String fileNameDt = null;
	 
	public DtWalkToBallTest(String fileName) {
		super();
		fileNameDt = fileName;
	}
	
	
	@Override
	public boolean init() {
		super.init();
		logger.info("DtWalkToBallTest init");
		try {
		//	AgentManager.getManager().setAgentWaitTime(0);
			logger.info("Waiting for agent...");
			
			//AgentManager.getManager().startAgent(1, "ANDROIDS");
		//	AgentManager.getManager().startAgent(1, "ANDROIDS", "dtWalk2Ball", "Walk2Ball");
			//!!! ZISTIT PRECO SA OBAC HRAC NEPRIPOJI !!!!
			server.setPlayMode(PlayMode.BeforeKickOff);
			server.setBallPosition(initPosBall.asPoint3D());
			
			server.setBallVelocity(new Point3D(0,0,0));
			agent = AgentManager.getManager().getAgent(1,"ANDROIDS", true);
			agent.setDtHghSkill("dtWalk2Ball", fileNameDt,"ANDROIDS1");
			Thread.sleep(1000);
			logger.info("Got agent");
			if (agent != null) {
				agentData = agent.getAgentData();
			} else {
				agentData = new AgentData(1, TeamSide.LEFT, "ANDROIDS");
			}
			server.setAgentPosition(agentData, initPosAgent.asPoint3D());
			server.setPlayMode(PlayMode.PlayOn);
			
			
			return true;
		} catch (Exception ex) {
			logger.log(Level.WARNING, "Unable to initialize test", ex);
			return false;
		}

	}

	@Override
	public boolean isStopCriterionMet(SimulationState ss) {
		Player p = null;
		if (!started) {
			if (ss.getGameStateInfo().getPlayMode()!= PlayMode.BeforeKickOff.ordinal()) {
				try {
					fieldLength = monitor.getSimulationState().getEnvironmentInfo().getFieldLength();
					fieldWidth = monitor.getSimulationState().getEnvironmentInfo().getFieldWidth();
					
					logger.info("Test measurement started");
					started = true;
					server.setBallPosition(initPosBall.asPoint3D());
					
				   server.setBallVelocity(new Point3D(0,0,0));
					//server.setAgentPosition(agentData, initPosAgent.asPoint3D());
					//if (agent != null) {
					//	agent.invokeMove("kick_right");
					//}
					startTime = getElapsedTime();
				} catch (IOException e) {
					logger.log(Level.FINE, "Error running test", e);
				}
			}
			return false;
		} else {
			p = ss.getScene().getPlayers().get(0);
			double elapsedTime = getElapsedTime();
			
			/*if (playerBehindBorder(p)) {	// controls if player is put behind border by server
				timeExpired = true;
				return true;
			}*/
			
			
			//zaznamena, ci spadol hrac
			/*if(!playerFalled && ss.getScene().getPlayers().get(0).isOnGround()){
				playerFalled = true;
			}*/		
			if(elapsedTime - startTime > 20)
				//return true;
				if(!secondRound)
					endFlag = true;
				else return true;
			
			//zaznamena, ci sa lopta uz hybala
			if(!ballMoved && ss.getScene().isBallMoving() && (elapsedTime - startTime > TIME_FOR_INITIAL_BALL_MOVE)){
				//return true;
				if(!secondRound)
					endFlag = true;
				else return true;
			}
			
			if(endFlag == true){
				try {
					secondRound = true;
					endFlag = false;
					server.setPlayMode(PlayMode.BeforeKickOff);
					startTime = getElapsedTime();
					Thread.sleep(1000);
					server.setBallVelocity(new Point3D(0,0,0));
					server.setBallPosition(PosBall2.asPoint3D());
					fitness1 = computeFitness(ss,Vector3D.fromVector3(initPosAgent),
							Vector3D.fromVector3(ss.getScene().getPlayers().get(0).getLocation()),
							Vector3D.fromVector3(initPosBall));
					server.setAgentPosition(agentData, initPosAgent.asPoint3D());
					server.setPlayMode(PlayMode.PlayOn);
				//	Thread.sleep(1000);
					
				} catch (IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
			//inak sa pokracuje
			return false;
			
			
			
		}
	}
	
	/**
	 * Checks if player is out of the field.
	 * 
	 * @author xsuchac
	 * @author A55-Kickers
	 * 
	 * @param player
	 * @return true if player is behind any border of field, else false.
	 */
	private boolean playerBehindBorder(Player player) {
		double playerLocationX = player.getLocation().getX();
		double playerLocationY = player.getLocation().getY();
		
		if (Math.abs(playerLocationX) > (fieldLength / 2) 
				|| Math.abs(playerLocationY) > (fieldWidth / 2)) {
			return true;
		}
		return false;
	}

	@Override
	public TestCaseResult evaluate(SimulationState ss) {
		
		
		Player p = ss.getScene().getPlayers().get(0);
		Vector3 agentFinalLocation = ss.getScene().getPlayers().get(0).getLocation();
		
		double result = computeFitness(
					ss,
					Vector3D.fromVector3(initPosAgent),
					Vector3D.fromVector3(agentFinalLocation),
					Vector3D.fromVector3(PosBall2));
		
		return new TestCaseResult(result);
	}

	@Override
	public void destroy() {
		super.destroy();
		
	
		/*try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		logger.fine("Test case destroyed");
	}	
	
	private double computeFitness(SimulationState ss,
			Vector3D startPos, Vector3D endPos, Vector3D initBall){
		
		double time = startTime - ss.getGameStateInfo().getTime();
		double ret = startPos.getXYDistanceFrom(initBall) - endPos.getXYDistanceFrom(initBall); 
		if(ret < 0)
			return fitness1;
		else
			return fitness1 + Math.pow(ret+1,3) - 1;
		
	}
}
