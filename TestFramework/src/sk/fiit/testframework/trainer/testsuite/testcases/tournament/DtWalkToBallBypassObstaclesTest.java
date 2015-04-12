package sk.fiit.testframework.trainer.testsuite.testcases.tournament;

import static sk.fiit.robocup.library.geometry.Vector3D.cartesian;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import sk.fiit.robocup.library.geometry.Angles;
import sk.fiit.robocup.library.geometry.Point3D;
import sk.fiit.robocup.library.geometry.Vector3;
import sk.fiit.robocup.library.geometry.Vector3D;
import sk.fiit.testframework.GEP.desiciontreeGEP.AgentSimulation;
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
public class DtWalkToBallBypassObstaclesTest extends TestCase {
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
	
	private Vector3 initPosBall = new Vector3(10.0,0,0.2);
	private Vector3 PosBall2 = new Vector3(10.0,10.0,0.2);
	private double fitness1 = 0;
	private Vector3 initPosAgent = new Vector3(0,0,0.4);
	boolean secondRound = false;
	boolean endFlag = false;
	
	private static Logger logger = Logger.getLogger(KickDistanceTest.class.getName());

	private AgentData agentData;
	private AgentData agentData2;
	private AgentJim agent;
	private AgentJim agent2;
	private boolean started = false;
	private double startTime;
	
	private boolean ballMoved = false;
	private boolean playerFalled = false;
	private boolean timeExpired = false;
	private boolean outOfPath = false;
	
	private AgentSimulation agentSim;
	private double  distanceBorder = 1;
	public String fileNameDt = null;
	private double closestToOtherPlayer = 100;
	public DtWalkToBallBypassObstaclesTest(String fileName) {
		super();
		fileNameDt = fileName;
		
	}
	
	
	@Override
	public boolean init() {
		super.init();
		logger.info("DtWalkToBallTest init");
		try {
			logger.info("Waiting for agent...");
			
			//!!! ZISTIT PRECO SA OBAC HRAC NEPRIPOJI !!!!
			server.setPlayMode(PlayMode.BeforeKickOff);
			server.setBallPosition(initPosBall.asPoint3D());
			
			server.setBallVelocity(new Point3D(0,0,0));
			
			agent = AgentManager.getManager().getAgent(1,"ANDROIDS", true);
			agentSim = new AgentSimulation(true, new Vector3(5.0,0,0),agent);
			
			agent.setDtHghSkill("dtWalk2Ball", fileNameDt, "ANDROIDS1");
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
					closestToOtherPlayer = 100;
					agentSim.run();
				   server.setBallVelocity(new Point3D(0,0,0));
					
					startTime = getElapsedTime();
				} catch (IOException e) {
					logger.log(Level.FINE, "Error running test", e);
				}
			}
			return false;
		} else {
			p = ss.getScene().getPlayers().get(0);
			double elapsedTime = getElapsedTime();
			closestToOtherPlayer = Math.min(closestToOtherPlayer, p.getLocation().getXYDistanceFrom(agentSim.getPosition())); 
		
			if(!outOfPath){
				outOfPath = calculateOutOfPath(p.getLocation());
			}
			
			if(elapsedTime - startTime > 30)
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
					outOfPath = false;
					endFlag = false;
					server.setPlayMode(PlayMode.BeforeKickOff);
					startTime = getElapsedTime();
					Thread.sleep(1000);
					server.setBallVelocity(new Point3D(0,0,0));
					server.setBallPosition(PosBall2.asPoint3D());
					//agentSim = new AgentSimulation(true, new Vector3(0,2.5,0),agent);
					fitness1 = computeFitness(ss,Vector3D.fromVector3(initPosAgent),
							Vector3D.fromVector3(ss.getScene().getPlayers().get(0).getLocation()),
							Vector3D.fromVector3(initPosBall),true);
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
	
	private boolean calculateOutOfPath(Vector3 p) {
		double x = p.getX();
		double y = p.getY();
		if(secondRound){
			double pom = x;
			x = y*2;
			y = pom;
		}
		if(x > 0 
		&& x > 4 * y - 1 
		&& x > -4 * y - 1
		&& 3*x < -10 * y + 30
		&& 3*x < 10 * y - 30)
			return false;
		else 
			return true;
	/*	if(secondRound){
			if(y > 0 
					&& 2*y > 4 * x - 1 
					&& 2*y > -4 * x - 1
					&& 6*y < -10 * x + 30
					&& 6*y < 10 * x - 30)
						return false;
		}*/
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
					Vector3D.fromVector3(PosBall2),false);
		
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
			Vector3D startPos, Vector3D endPos, Vector3D initBall, boolean isSecondRound){
		
		double ret = startPos.getXYDistanceFrom(initBall) - endPos.getXYDistanceFrom(initBall); 
		if(ret < startPos.getXYDistanceFrom(initBall)/2)
			ret = ret / 10;
		if(closestToOtherPlayer < distanceBorder){
			if(closestToOtherPlayer < 0.5)
				ret = ret*0.5;
			else
				ret = ret *  closestToOtherPlayer;
		}
		if(outOfPath && !isSecondRound)
			ret = ret * 0.75;
		if(ret < 0)
			return fitness1;
		else
			return fitness1 + Math.pow(ret+1,3) - 1;
		
	}
}
