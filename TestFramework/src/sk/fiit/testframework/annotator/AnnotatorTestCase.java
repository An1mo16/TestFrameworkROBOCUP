package sk.fiit.testframework.annotator;

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

/**
 * Class is testing move 
 * Based  on messages from player
 * 
 * @author Miro Bimbo
 *
 */

public class AnnotatorTestCase extends TestCase implements IAgentMonitorListener {

	protected Logger logger = Logger.getLogger(getClass().getName());

    private AgentJim agent;
    private static Vector3 INIT_PLAYER_POSITION = new Vector3(0, 0, 0.4);
    
    private Vector3 initBallPosition;
    private String moveToTest;
    private volatile boolean started = false;
    private volatile boolean stopped = false;
    
    private double timeStarted,timeStopped;
    

    public AnnotatorTestCase(Vector3 initBall, String moveToTest)  {
        super();
        this.moveToTest = moveToTest; //"kick_right";
        initBallPosition = initBall; //new Vector3(0.17, -0.1, 0.05);
    }

    @Override
    public boolean init() {
    	super.init();
    	//select player to be annotated
    	agent = AgentManager.getManager().getAgent(1, "ANDROIDS", true);
    	if (agent == null) return false;
    	AgentMonitor.setMessageListener(agent, this, AgentMonitorMessage.TYPE_HIGHSKILL);
        try {
        	server.setPlayMode(PlayMode.PlayOn);
        	server.setBallPosition(initBallPosition.asPoint3D());
        	server.setAgentPosition(agent.getAgentData(), INIT_PLAYER_POSITION.asPoint3D());
        	//agent.invokeMove(moveToTest);
        	Thread.sleep(500);
            return true;
        } catch (Exception ex) {
            logger.log(Level.WARNING, "Unable to initialize test", ex);
            return false;
        }      
        
    }

    @Override
    public boolean isStopCriterionMet(SimulationState ss) {
        Player p = ss.getScene().getPlayers().get(0);
        
        // if player fall, end test
        if (p.isOnGround())
            return true;
        
        // if player has already started and then ended his move
        // wait until ball is not moving
        if(started && stopped){
        	if(!ss.getScene().isBallMoving())
        		return true;
        }
        return false;
    }
    
    @Override
    public TestCaseResult evaluate(SimulationState ss) {
        AnnotatorTestCaseResult result = new AnnotatorTestCaseResult();
    	Player p = ss.getScene().getPlayers().get(0);

    	result.setMoveName(this.moveToTest);
    	result.setInitBallPosition(initBallPosition);
    	
        result.setMove(p.getLocation().subtract(INIT_PLAYER_POSITION));
        result.setFall(p.isOnGround());
        result.setBallMove(ss.getScene().getBallLocation().subtract(initBallPosition));
        result.setDuration((timeStopped - timeStarted)*1000);
        //initial rotation value after beam for player on left side is 0;0;pi/2
        // (same as 0,0,90 in degrees)
        result.setRotation(p.getRotation().subtract(new Vector3(0,0,Math.PI/2))); 

        return result;
    }

	@Override
    public void destroy() {
		super.destroy();
		AgentMonitor.removeMessageListener(1, "ANDROIDS", this);
		try {
			// player is beamed in playmode BeforeKickOff,
			// but it takes some time
			// TODO: is there some better solution?
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	logger.fine("Test case destroyed");
    }
	
	public void receivedMessage(int uniform, String team, AgentMonitorMessage message) {
		// registrovany bol listener iba na typ LOWSKILL takze ClassCasting je bezpecny
		AgentMonitorMessage.HighSkill msg = (HighSkill) message;
		switch(msg.action) {
			case start:
				logger.fine("Started move: "+msg.move_name+" Time:"+msg.player_time);
				// checking !stopped beacuse of doing move only once
				if(msg.move_name.equals(moveToTest) && !stopped){
					this.started = true;
					this.timeStarted = msg.player_time;
				}
				break;
			case stop:
				logger.fine("Ended move: "+msg.move_name+" Time:"+msg.player_time);
				// checking !started beacuse there may be listened ending of move from previous testCase ?
				if(msg.move_name.equals(moveToTest) && started){
					this.stopped = true;
					this.timeStopped = msg.player_time;
				}
				break;
		}
	}
    
}
