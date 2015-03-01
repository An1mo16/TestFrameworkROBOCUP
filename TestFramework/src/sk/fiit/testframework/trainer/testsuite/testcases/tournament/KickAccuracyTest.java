package sk.fiit.testframework.trainer.testsuite.testcases.tournament;

import static sk.fiit.robocup.library.geometry.Vector3D.cartesian;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import sk.fiit.robocup.library.geometry.Angles;
import sk.fiit.robocup.library.geometry.Vector3;
import sk.fiit.robocup.library.geometry.Vector3D;
import sk.fiit.testframework.communication.agent.AgentData;
import sk.fiit.testframework.communication.agent.AgentJim;
import sk.fiit.testframework.communication.agent.AgentJim.TeamSide;
import sk.fiit.testframework.communication.agent.AgentManager;
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
public class KickAccuracyTest extends TestCase {
	//how much time between start of playmode and start of testing - if ball started to move (ball kicked)
	//because ball moves a bit after set to initial point
	private static double TIME_FOR_INITIAL_BALL_MOVE = 0.5;
	//minimal ball move set by rules
	private static double MIN_BALL_DIST = 1.5;
	//value for failed kick
	private static double FAILED = 360;
	//time for kick
	private int TIME = 300;
	
	private double fieldLength;
	private double fieldWidth;
	
	private double ballTouchedTime = -1;
	private double TIME_BETWEEN_KICK_AND_TELEPORT = 0.2;
	private boolean agentTeleportedAfterKick = false;
	
//	private Vector3 initPosBall = new Vector3(0.2,0,0.2);
//	private Vector3 initPosAgent = new Vector3(0,0,0.4);
	private Vector3 initPosBall = new Vector3(0.0,0,0.2);
	private Vector3 initPosAgent = new Vector3(-0.7,0,0.4);
	
	
	private static Logger logger = Logger.getLogger(KickDistanceTest.class.getName());

	private AgentData agentData;
	private AgentJim agent;
	private boolean started = false;
	private double startTime;
	
	private boolean ballMoved = false;
	private boolean playerFalled = false;
	private boolean timeExpired = false;
	 
	
	public KickAccuracyTest() {
		super();
	}
	
	
	@Override
	public boolean init() {
		super.init();
		logger.info("KickAccuracyTest init");
		try {
			AgentManager.getManager().setAgentWaitTime(30000);
			logger.info("Waiting for agent...");
			agent = AgentManager.getManager().getAgent(1, TeamSide.LEFT.toString(), false);
			logger.info("Got agent");
			if (agent != null) {
				agentData = agent.getAgentData();
			} else {
				agentData = new AgentData(1, TeamSide.LEFT, "ANDROIDS");
			}
			//server.setPlayMode(PlayMode.KickOff_Left);
			logger.info("Test initialized - waiting for KickOff_Left mode.");
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
			if (ss.getGameStateInfo().getPlayMode() == PlayMode.KickOff_Left.ordinal()) {
//			if (ss.getGameStateInfo().getPlayMode() == PlayMode.PlayOn.ordinal()) {
				try {
					fieldLength = monitor.getSimulationState().getEnvironmentInfo().getFieldLength();
					fieldWidth = monitor.getSimulationState().getEnvironmentInfo().getFieldWidth();
					
					logger.info("Test measurement started");
					started = true;
					server.setBallPosition(initPosBall.asPoint3D());
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
			
			if (playerBehindBorder(p)) {	// controls if player is put behind border by server
				timeExpired = true;
				return true;
			}
			
			//zaznamena, ci spadol hrac
			if(!playerFalled && ss.getScene().getPlayers().get(0).isOnGround()){
				playerFalled = true;
			}
			
			//zaznamena, ci sa lopta uz hybala
			if(!ballMoved && ss.getScene().isBallMoving() && (elapsedTime - startTime > TIME_FOR_INITIAL_BALL_MOVE) ){
				ballMoved = true;
				ballTouchedTime = elapsedTime;
			}
			
			if (!agentTeleportedAfterKick && ballMoved && ss.getScene().isBallMoving() 
					&& (elapsedTime - ballTouchedTime) >= TIME_BETWEEN_KICK_AND_TELEPORT) {
				try {
					server.setAgentPosition(agentData, new Vector3(-1.5, 0, 0.375).asPoint3D());
					agentTeleportedAfterKick = true;
				} catch (IOException e) {
					logger.fine("Error teleporting agent after touching ball.");
				}
			}
			
			//ak sa hybala a uz stoji, koniec testu
			if(ballMoved && !ss.getScene().isBallMoving()){
				return true;
			}
			
			//ak zaciatok kopnutia trva velmi dlho, koniec testu
			if (!ballMoved && (elapsedTime - startTime > TIME)){
				timeExpired = true;
				return true;
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
		Vector3 ballFinalLocation = ss.getScene().getBallLocation();
		
		//ak to trvalo viac ako je povolene, alebo lopta nepresla pozadovanu vzdialenost, uhol je max 
		if(timeExpired){
			logger.info("Time is up, attempt not successful.");
			return new TestCaseResult(FAILED);
		}
		
		if(ballFinalLocation.getXYDistanceFrom(initPosBall) < MIN_BALL_DIST){
			logger.info("Kick distance was less than " + MIN_BALL_DIST + ", attempt not successful.");
			return new TestCaseResult(FAILED);
		}
		
		double result = computeAnglesForCornerFlags(
					ss.getEnvironmentInfo(),
					Vector3D.fromVector3(initPosBall),
					Vector3D.fromVector3(ballFinalLocation));
		
		if(playerFalled){
			logger.info("Player was penalized for falling during kick, result: " + result + " * 2.");
			return new TestCaseResult(result * 2);
		} else
		{
			logger.info("Attempt successful.");
			return new TestCaseResult(result);
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.fine("Test case destroyed");
	}	
	
	private double computeAnglesForCornerFlags(EnvironmentPart ep,
			Vector3D startPos, Vector3D endPos){
		
		double length = ep.getFieldLength()/2;
		double width = ep.getFieldWidth()/2;
		
		double loptaIslaUhlom = endPos.subtract(startPos).getPhi();
		
		//hodnoty idealnych vektorov, po ktorych by lopta mala ist, pre jednotlive zastavky
		//pocita sa podla inicialnej polohy lopty a polohy zastavky
		//idealny vektor pre zastavku vpredu vpravo?
		double R1 = cartesian(length, width, 0).subtract(startPos).getPhi();
		//vzdadu vpravo?
		double R2 = cartesian(-length, width, 0).subtract(startPos).getPhi();
		//vzadu vlavo?
		double L2 = cartesian(-length, -width, 0).subtract(startPos).getPhi();
		//vpredu vlavo?
		double L1 = cartesian(length, -width, 0).subtract(startPos).getPhi();
		
		//hodnoty odchylenia uhlov medzi vektorom, ktory lopta presla 
		//a idealnym vektorom pre jednotlive zastavky
		double R1val = Math.toDegrees(Angles.angleDiff(loptaIslaUhlom, R1));
		double R2val = Math.toDegrees(Angles.angleDiff(loptaIslaUhlom, R2));
		double L2val = Math.toDegrees(Angles.angleDiff(loptaIslaUhlom, L2));
		double L1val = Math.toDegrees(Angles.angleDiff(loptaIslaUhlom, L1));
		
		
		//najmensi rozdiel v uhloch vyhral
		double ret = Math.min(R1val, Math.min(R2val, Math.min(L1val, L2val)));
		
		return ret;
		
	}
}
