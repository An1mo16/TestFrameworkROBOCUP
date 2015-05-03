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
import sk.fiit.testframework.gp.desiciontreegp.AgentSimulation;
import sk.fiit.testframework.monitor.RobocupMonitor;
import sk.fiit.testframework.parsing.models.EnvironmentPart;
import sk.fiit.testframework.parsing.models.PlayMode;
import sk.fiit.testframework.trainer.testsuite.TestCase;
import sk.fiit.testframework.trainer.testsuite.TestCaseResult;
import sk.fiit.testframework.worldrepresentation.models.Player;
import sk.fiit.testframework.worldrepresentation.models.SimulationState;

/**
 * 
 * @author Július Skrisa
 *
 */
public class DtWalkToBallBypassObstaclesTest extends TestCase {
	private static double TIME_FOR_INITIAL_BALL_MOVE = 1;
	
	private Vector3 initPosBall = new Vector3(10.0,0,0.2);
	private Vector3 PosBall2 = new Vector3(5.0,5.0,0.2);
	private double fitness1 = 1;
	private Vector3 initPosAgent = new Vector3(0,0,0.4);
	private double caseTime = 30;
	boolean secondRound = false;
	boolean endFlag = false;
	
	private static Logger logger = Logger.getLogger(KickDistanceTest.class.getName());

	private AgentData agentData;
	private AgentJim agent;
	private boolean started = false;
	private double startTime;
	
	private boolean ballMoved = false;
	
	private AgentSimulation agentSim;
	private double  distanceBorder = 1;
	public String fileNameDt = null;
	private double closestToOtherPlayer = 100;
	public DtWalkToBallBypassObstaclesTest(String fileName) {
		super();
		fileNameDt = fileName;
		
	}
	
	
	@Override
	//Inizializacia testu
	public boolean init() {
		super.init();
		logger.info("DtWalkToBallTest init");
		try {
			logger.info("Waiting for agent...");
			
			//Nastavenie herneho modu
			server.setPlayMode(PlayMode.BeforeKickOff);
			
			//Nastavenie pozicie a rychlosti lopty
			server.setBallPosition(initPosBall.asPoint3D());
			server.setBallVelocity(new Point3D(0,0,0));
			
			//Pripojenie hraca a nastavenie rozhodovania
			agent = AgentManager.getManager().getAgent(1,"ANDROIDS", true);
			agent.setDtHghSkill("dtWalk2Ball", fileNameDt, "ANDROIDS1");
			
			//Vytvorenie simulacneho agenta ako dynamicku prekazku
			agentSim = new AgentSimulation(false, new Vector3(2.0,-1.0,0),agent, 5,new Vector3(10.0,2.0,0), caseTime);
 
			logger.info("Got agent");
			
			if (agent != null) {
				agentData = agent.getAgentData();
			} else {
				agentData = new AgentData(1, TeamSide.LEFT, "ANDROIDS");
			}
			
			//Nastavenie poziciee hraca a zacatie testu
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
		//Ak je to prve volanie tejto metody - (overenie splnenia podimenky na ukoncenie testu)
		if (!started) {
			if (ss.getGameStateInfo().getPlayMode()!= PlayMode.BeforeKickOff.ordinal()) {
				try {
					
					logger.info("Test measurement started");
					started = true;
					
					startTime = getElapsedTime();
				} catch (Exception e) {
					logger.log(Level.FINE, "Error running test", e);
				}
			}
			return false;
		} else {
			//Hrac p
			p = ss.getScene().getPlayers().get(0);
			
			//Prejdeny cas fázy testu
			double elapsedTime = getElapsedTime();
			
			//Zistenie najblizsej polohy k prekazke
			closestToOtherPlayer = Math.min(closestToOtherPlayer, p.getLocation().getXYDistanceFrom(agentSim.getPosition(elapsedTime - startTime))); 
			
			
			//Ak je cas fázy naplnený prejs do druhej fázy alebo ukonèit test
			if(elapsedTime - startTime > caseTime)
				if(!secondRound)
					endFlag = true;
				else return true; // Ukoncenie testu (poslednej fazy)
			
			//zaznamena, ci sa lopta uz hybala
			if(!ballMoved && ss.getScene().isBallMoving() && (elapsedTime - startTime > TIME_FOR_INITIAL_BALL_MOVE)){
				ballMoved = true;
				if(!secondRound)
					endFlag = true;
				else return true; // Ukoncenie testu (poslednej fazy)
			}
			
			if(endFlag){
				try {
					//Nastavit dtuhu fazu testu
					secondRound = true;
					endFlag = false;
					ballMoved = false;
					server.setPlayMode(PlayMode.BeforeKickOff);
					
					startTime = getElapsedTime();
					
					//Nastavenie lopty pre dalsie fazu
					server.setBallVelocity(new Point3D(0,0,0));
					server.setBallPosition(PosBall2.asPoint3D());
					
					//Vytvorenie novej pohyblivej prekazky
					agentSim = new AgentSimulation(false, new Vector3(0.0,1.0,0),agent,5 ,new Vector3(7.0,4.0,0), caseTime);
					
					//Vypocet fitness hodnoty prvej fazy
					fitness1 = computeFitness(ss,Vector3D.fromVector3(initPosAgent),
							Vector3D.fromVector3(ss.getScene().getPlayers().get(0).getLocation()),
							Vector3D.fromVector3(initPosBall),true);
					
					server.setAgentPosition(agentData, initPosAgent.asPoint3D());
					logger.info("Hráè nastavený do ïalšej fázy");
					server.setPlayMode(PlayMode.PlayOn);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//inak sa pokracuje
			return false;
			
			
			
		}
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
		
		logger.fine("Test case destroyed");
	}	
	
	//Fitness funkcia 
	private double computeFitness(SimulationState ss,
			Vector3D startPos, Vector3D endPos, Vector3D initBall, boolean isSecondRound){
		
		double ret = startPos.getXYDistanceFrom(initBall) - endPos.getXYDistanceFrom(initBall); 
		if(ret < startPos.getXYDistanceFrom(initBall)/2)
			ret = ret / 10;
		if(closestToOtherPlayer < distanceBorder){
			if(closestToOtherPlayer < 0.5)
				ret = ret*0.2;
			else
				ret = ret *  0.5;
		}

		if(ret < 0)
			return fitness1;
		else{
			return fitness1 + Math.pow(ret+1,3) - 1;
			
		}
	}
}
