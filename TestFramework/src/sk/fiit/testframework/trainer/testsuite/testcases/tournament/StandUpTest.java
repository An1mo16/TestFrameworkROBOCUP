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
import sk.fiit.robocup.library.math.TransformationMatrix;
import sk.fiit.testframework.communication.agent.AgentData;
import sk.fiit.testframework.communication.agent.AgentJim;
import sk.fiit.testframework.communication.agent.AgentJim.TeamSide;
import sk.fiit.testframework.communication.agent.AgentManager;
import sk.fiit.testframework.parsing.models.PlayMode;
import sk.fiit.testframework.trainer.testsuite.TestCase;
import sk.fiit.testframework.trainer.testsuite.TestCaseResult;
import sk.fiit.testframework.worldrepresentation.models.Player;
import sk.fiit.testframework.worldrepresentation.models.SimulationState;

public class StandUpTest extends TestCase {

	private static Logger logger = Logger.getLogger(StandUpTest.class.getName());
	
	private AgentData agentData;	//fs
	private AgentJim agent;		//fs
	private double startTime = -1;
	private boolean started = false;
	private Vector3 lastLocation1;
	private Vector3 initPos;	//fs
	
	private TransformationMatrix lastBodyState;
	private double lastIsStandingTime = 5000;
	private double successTime = 5000;
	private boolean timeIsUp = false;

	public StandUpTest() {
		super();
	}

	@Override
	public boolean init() {
		super.init();
		try {
			initPos = new Vector3(-3, 0, 0.4);	//fs
			agent = AgentManager.getManager().getAgent(5, TeamSide.LEFT.toString(), false); // non blocking call - if agent does not exist then is null
			if(agent == null) {
				agentData = new AgentData(1, TeamSide.LEFT, "ANDROIDS");
			} else {
				agentData = agent.getAgentData();
			}
			started = false;
			logger.info("Test initialized - waiting for PlayOn mode.");
			return true;
		} catch (Exception ex) {
			logger.log(Level.WARNING, "Unable to initialize test", ex);
			return false;
		}

	}

	@Override
	public boolean isStopCriterionMet(SimulationState ss) {
		Player p = ss.getScene().getPlayers().get(0);
		if (!started) {
			if (ss.getGameStateInfo().getPlayMode() == PlayMode.PlayOn.ordinal()) {
				logger.info("Test measurement started");
				started = true;
				//try {																	//fs
					//server.setAgentPosition(agentData, initPos.asPoint3D());			//fs
				//} catch (IOException e) {														
				//	logger.log(Level.WARNING, "Unable to initialize test", e);			//fs
				//	return false;
				//}
//				if (!monitor.getSimulationState().getScene().getPlayers().get(0).isOnGround()) {
//					return true; // should be on ground
//				}
				startTime = getElapsedTime();
				lastBodyState = p.getBody().getHead().getTransformation();
			}
			return false;
		} else {
			double elapsedTime = getElapsedTime();
			boolean standing = false;
			if (p.getBody().getHead().getTransformation().getValues()[14] >= 0.45) {
				standing = true;
			}
			TransformationMatrix currentBodyState = p.getBody().getHead().getTransformation();
			
			if (standing) { // uz je postaveny
				if ((elapsedTime - lastIsStandingTime) < 0) {	// first standed up
					lastIsStandingTime = elapsedTime;
					lastBodyState = currentBodyState;
				} else if (currentBodyState.compareWith(lastBodyState)) {
					if ((elapsedTime - lastIsStandingTime) >= 1) {
						successTime = (elapsedTime - startTime);
						return true;
					}
				} else {
					lastBodyState = currentBodyState;
					lastIsStandingTime = elapsedTime;
				}
			} else {
				lastBodyState = currentBodyState;
				lastIsStandingTime = 5000;
			}
			
			if (elapsedTime - startTime >= 20) { // ak presiel maximalny dany cas
				timeIsUp = true;
				return true;
			}
			
			return false;
		}
	}

	@Override
	public TestCaseResult evaluate(SimulationState ss) {
		if (timeIsUp) {
			logger.info("Standing up was not successful in time limit 20 s.");
			return new TestCaseResult(20);
		} else {
			logger.info("Standing up was successful.");
			return new TestCaseResult(successTime);
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.fine("Test case destroyed");
	}	

}
