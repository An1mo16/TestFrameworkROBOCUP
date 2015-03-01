/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.fiit.testframework.trainer.testsuite.testcases;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import sk.fiit.robocup.library.geometry.Vector3;
import sk.fiit.testframework.communication.agent.AgentJim;
import sk.fiit.testframework.communication.agent.AgentJim.TeamSide;
import sk.fiit.testframework.communication.agent.AgentManager;
import sk.fiit.testframework.monitor.AgentMonitor;
import sk.fiit.testframework.monitor.AgentMonitorMessage;
import sk.fiit.testframework.monitor.AgentMonitorMessage.HighSkill;
import sk.fiit.testframework.monitor.IAgentMonitorListener;
import sk.fiit.testframework.parsing.models.PlayMode;
import sk.fiit.testframework.trainer.testsuite.TestCase;
import sk.fiit.testframework.trainer.testsuite.TestCaseResult;
import sk.fiit.testframework.worldrepresentation.models.Player;
import sk.fiit.testframework.worldrepresentation.models.SimulationState;

public class WalkTestCase extends TestCase implements IAgentMonitorListener {

	private static Logger logger = Logger.getLogger(WalkTestCase.class.getName());

    private AgentJim agent;
//    private AgentJim.Data agentData;
    private Vector3 initPos;

    public WalkTestCase()  {
        super();
//      agentData = new AgentJim.Data(1, Team.Left, C.getProperty(C.PROPERTIES_ROBOCUP_PLAYER_IP), Integer.parseInt(C.getProperty(C.PROPERTIES_ROBOCUP_PLAYER_PORT)));
        AgentMonitor.setMessageListener(1, "ANDROIDS", this, AgentMonitorMessage.TYPE_HIGHSKILL);
//		agent = new AgentJim(agentData);
        initPos = new Vector3(0.4, 0.4, 0.4);
    }

    @Override
    public boolean init() {
    	super.init();
        try {
        	agent = AgentManager.getManager().getAgent(5, TeamSide.LEFT.toString(), true);
        	if (agent == null) return false;
        	server.setPlayMode(PlayMode.PlayOn);
        	server.setAgentPosition(agent.getAgentData(), initPos.asPoint3D());
            return true;
        } catch (Exception ex) {
            logger.log(Level.WARNING, "Unable to initialize test", ex);
            return false;
        }
        
    }

    @Override
    public boolean isStopCriterionMet(SimulationState ss) {
        Player p = ss.getScene().getPlayers().get(0);
        if (p.isOnGround() || getElapsedTime() > 10)
            return true;
        return false;
    }
    
    @Override
    public TestCaseResult evaluate(SimulationState ss) {
        Player p = ss.getScene().getPlayers().get(0);
        if (p.isOnGround()) {
            return new TestCaseResult(-1);
        } else {
            return new TestCaseResult(initPos.getXYDistanceFrom(p.getLocation()));
        }
    }

    @Override
    public void destroy() {
    	super.destroy();
    	AgentMonitor.removeMessageListener(1, "ANDROIDS", this);
    	try {
			//agent.invokeRestart();
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	logger.fine("Test case destroyed");
    }

	@Override
	public void receivedMessage(int uniform, String team, AgentMonitorMessage message) {
		// registrovany bol listener iba na typ LOWSKILL takze ClassCasting je bezpecny
		AgentMonitorMessage.HighSkill msg = (HighSkill) message;
		switch(msg.action) {
			case start:
//				msg.move_name a msg.playertime
				break;
			case stop:
//				msg.move_name a msg.playertime
				break;
		}
	}
    
}
