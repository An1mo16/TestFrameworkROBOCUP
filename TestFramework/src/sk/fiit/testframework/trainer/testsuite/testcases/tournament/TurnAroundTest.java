/**
 * Name:    TurnAround.java
 * Created: Feb 26, 2012
 * 
 * @author: ivan
 */
package sk.fiit.testframework.trainer.testsuite.testcases.tournament;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import sk.fiit.robocup.library.geometry.Vector3;
import sk.fiit.testframework.communication.agent.AgentData;
import sk.fiit.testframework.communication.agent.AgentJim;
import sk.fiit.testframework.communication.agent.AgentJim.TeamSide;
import sk.fiit.testframework.communication.agent.AgentManager;
import sk.fiit.testframework.parsing.models.PlayMode;
import sk.fiit.testframework.trainer.testsuite.TestCase;
import sk.fiit.testframework.trainer.testsuite.TestCaseResult;
import sk.fiit.testframework.worldrepresentation.models.Player;
import sk.fiit.testframework.worldrepresentation.models.SimulationState;

/**
 * TODO: Replace with a brief purpose of class / interface.
 * 
 * @author ivan
 *
 */
public class TurnAroundTest extends TestCase {
	private static Logger logger = Logger.getLogger(TurnAroundTest.class.getName());

	private AgentData agentData;
	private AgentJim agent;
	private boolean started = false;
	private double startTime;
	private double startingNormalizedRotation;
	private double exptectedRotation;
	private double exptectedRotation1;
	private double exptectedRotation2;
	private Vector3 initPos;	//fs
	rotation currentroration;
	enum rotation {none, left, right}
	
	private double fieldLength;
	private double fieldWidth;
	
	private boolean turningFailed = false;
	
	public TurnAroundTest() {
		super();
	}

	@Override
	public boolean init() {
		super.init();
		try {        	
			initPos = new Vector3(-3, 0, 0.4);	//fs
			
			fieldLength = monitor.getSimulationState().getEnvironmentInfo().getFieldLength();
			fieldWidth = monitor.getSimulationState().getEnvironmentInfo().getFieldWidth();
			
			agent = AgentManager.getManager().getAgent(1, TeamSide.LEFT.toString(), false);
			if (agent != null) {
				agentData = agent.getAgentData();
			} else {
				agentData = new AgentData(1, TeamSide.LEFT, "ANDROIDS");
			}
			logger.info("Test initialized - waiting for PlayOn mode.");
			started = false;
			currentroration = rotation.none;
			return true;
		} catch (Exception ex) {
			logger.log(Level.WARNING, "Unable to initialize test", ex);
			return false;
		}

	}

	@Override
	public boolean isStopCriterionMet(SimulationState ss) {
		if (!started) {
			if (ss.getGameStateInfo().getPlayMode() == PlayMode.PlayOn.ordinal()) {
				//try {
					logger.info("Test measurement started");
					started = true;
//					server.setBallPosition(new Vector3(1, 0, 0.4).asPoint3D());
//					server.setAgentPosition(agentData, new Vector3(0,0,0.4).asPoint3D());
//					server.setAgentPosition(agentData, initPos.asPoint3D());	//fs
					startingNormalizedRotation = ss.getScene().getPlayers().get(0).getNormalizedRotation();
					exptectedRotation = startingNormalizedRotation + 3.14;
					exptectedRotation1 = startingNormalizedRotation + 3.0543;
					exptectedRotation2 = startingNormalizedRotation + 3.2288;
					if (exptectedRotation >= 3.14) exptectedRotation = startingNormalizedRotation - 3.14;
					if (exptectedRotation1 >= 3.14) {
						exptectedRotation1 = startingNormalizedRotation - 3.0543;
					}
					if (exptectedRotation2 >= 3.14) {
						exptectedRotation2 = startingNormalizedRotation - 3.2288;
					}
					logger.log(Level.FINE, "starting rotation (radian) " +  startingNormalizedRotation + ", end is expecting (radians)" + exptectedRotation);
					if (agent != null) {
//						agent.invokeMove("turn_right_cont_20");
					}
				//} catch (IOException e) {
				//	logger.log(Level.FINE, "Error running test", e);
				//}
				startTime = getElapsedTime();
			}
			return false;
		} else {
			/*
			Ta rotacia je v Player.getNormalizedRotation() (ono to uz aj predtym
			bolo spravne, akurat to getRotation 3.tam davalo rotaciu v opacnom smere
			nez poucuval agent), vracia rotaciu v rovnakej forme ako sa nachadza v
			datach agenta ( AgentModel.getRotationZ() ), teda v radianoch kde 0 je
			tam, kde je agent otoceny k lavemu okraju (strana, ktora je pri
			pozerani sa na modru branu po lavej ruke). Je to pouzite napriklad v
			tom grafickom zobrazeni v test frameworku.
			*/
			
			Player p = ss.getScene().getPlayers().get(0);
			double actualPlayerRotation = p.getNormalizedRotation();
			
			if (playerBehindBorder(p)) {	// controls if player is put behind border by server
				turningFailed = true;
				return true;
			}
			
			boolean standing = false;
			if (p.getBody().getHead().getTransformation().getValues()[14] >= 0.45) {
				standing = true;
			}
			
			boolean turned = false;
			if (exptectedRotation1 < exptectedRotation2) {
				if (actualPlayerRotation >= exptectedRotation1 && actualPlayerRotation <= exptectedRotation2) {
					turned = true;
				}
			} else {
				if (actualPlayerRotation >= exptectedRotation2 && actualPlayerRotation <= exptectedRotation1) {
					turned = true;
				}
			}
			
			if (standing && turned) {
				return true;
			}
			
			/* old code, commented: xsuchac, a55-kickers
			if (getElapsedTime() - startTime > 2 && currentroration == rotation.none) {
				if (actualPlayerRotation < startingNormalizedRotation) {
					logger.log(Level.FINE, "rotation right, expecting (radians) " + exptectedRotation);
					currentroration = rotation.right;
				} else {
					logger.log(Level.FINE, "rotation left, expecting (radians) " + exptectedRotation);
					currentroration = rotation.left;
				}
			}
			
			switch(currentroration) {
				case left: {
					if (actualPlayerRotation >= exptectedRotation) {
						return true;
					}
					break;
				}
				case right: {
					if (actualPlayerRotation <= exptectedRotation) {
						return true;
					}
					break;
				}
			}
			 */
			
			if (getElapsedTime() - startTime > 300) {
				turningFailed = true;
				return true;
			}
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
		if (turningFailed) {
			logger.info("Attempt failed.");
			return new TestCaseResult(5000);
		} else {
			logger.info("Attempt successful.");
			return new TestCaseResult(getElapsedTime() - startTime);
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

}
