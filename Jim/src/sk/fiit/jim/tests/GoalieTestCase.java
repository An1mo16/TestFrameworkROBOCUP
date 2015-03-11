package sk.fiit.jim.tests;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.robocup.library.init.Script;

/**
 * GoalieTestCase.java
 * 
 * @Title	Jim
 * @author	Androids
 */
//public class GoalieTestCase extends TestCase {
//
//	private TestJim agent;
//	private Vector3 initPos;
//	private boolean isAgentInitialized = false;
//	private Vector3 lastBallPosition;
//	private boolean isBallKicked = false;
//	private int scoreLeft;
//	private int scoreRight;
//	private boolean canEvaluate = false;
//
//	public GoalieTestCase(RobocupServerAddress address) throws IOException {
//		super(address);
//		this.agent = new TestJim();
//
//	}
//
//	@Override
//	public void init() {
//		try {
//			// trainer.setAgentPosition(agent, initPos.asPoint3D());
//
//			Runnable r = new Runnable() {
//				public void run() {
//					kickBall();
//				}
//			};
//
//			Thread thr1 = new Thread(r);
//			thr1.start();
//
//			trainer.setPlayMode(PlayMode.BeforeKickOff);
//			agent.init();
//			agent.run();
//			trainer.setBallPosition(new Point3D(-5, 0, 0));
//			
//
//		} catch (Exception ex) {
//			throw new RuntimeException(ex);
//		}
//	}
//
//	private void kickBall() {
//		while (!isAgentInitialized) {
//			try {
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//		try {
//			trainer.setPlayMode(PlayMode.PlayOn);
//			trainer.setAgentPosition(AgentInfo.team, AgentInfo.playerId,
//					new Point3D(-7.5, 0, 0));
//			
//			
//			trainer.setBall(new Point3D(-5, 0, 0), new Point3D(-8, 0, 0));
//			agent.setPlanningScript(Script
//					.createScript("TestGoaliePlan.instance.control"));
//			try {
//				Thread.sleep(8000);
//				//canEvaluate = true;
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	@Override
//	public TestCaseResult evaluate(SimulationState ss) {
//		System.out.println("evaluate()");
//		try {
//			trainer.setPlayMode(PlayMode.BeforeKickOff);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if (scoreLeft != ss.getGameStateInfo().getScoreLeft()
//				|| scoreRight != ss.getGameStateInfo().getScoreRight()) {
//			return new TestCaseResult(1);
//		} else {
//			return new TestCaseResult(0);
//		}
//	}
//
//	@Override
//	public boolean isStopCriterionMet(SimulationState ss) {
//
//		if (ss.getScene().getPlayers().size() > 0) {
//			isAgentInitialized = true;
//		} else {
//			scoreLeft = ss.getGameStateInfo().getScoreLeft();
//			scoreRight = ss.getGameStateInfo().getScoreRight();
//		}
//
//		if (canEvaluate) {
//			return true;
//		}
//
//		lastBallPosition = ss.getScene().getBallLocation();
//
//		return false;
//	}
//
//}
