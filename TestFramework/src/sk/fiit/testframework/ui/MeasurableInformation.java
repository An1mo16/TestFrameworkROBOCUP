/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.fiit.testframework.ui;

import sk.fiit.robocup.library.geometry.Vector3;
import sk.fiit.testframework.communication.agent.AgentJim;
import sk.fiit.testframework.communication.agent.AgentManager;
import sk.fiit.testframework.worldrepresentation.models.Player;
import sk.fiit.testframework.worldrepresentation.models.Scene;
import sk.fiit.testframework.worldrepresentation.models.SimulationState;

/**
 *
 * @author androids
 */
public class MeasurableInformation {

    public Vector3 ballPosition;
    public Vector3 agentPosition = new Vector3();
    public Vector3 agentRotation = new Vector3();
    public AgentJim associatedAgent = null;

    public double time = 0;

    public MeasurableInformation(Vector3 ballPosition, Vector3 agentPosition, Vector3 agentRotation, double time) {
        this.ballPosition = ballPosition;
        this.agentPosition = agentPosition;
        this.agentRotation = agentRotation;
        this.time = time;
    }
    
    public MeasurableInformation(SimulationState ss) {
    	this(ss, 0);
    }

    public MeasurableInformation(SimulationState ss, int playerNumber) {
        Scene scene = ss.getScene();
        this.ballPosition = scene.getBallLocation();
        this.time = ss.getGameStateInfo().getTime();
        if (ss.getScene().getPlayers().size() > 0 && ss.getScene().getPlayers().size() > playerNumber && 0 <= playerNumber) {
        	Player p = ss.getScene().getPlayers().get(playerNumber);
            this.agentPosition = new Vector3(p.getLocation());
            this.agentRotation = new Vector3(p.getRotation());
            this.associatedAgent = p.getAssociatedAgent();
        }
    }

    public static MeasurableInformation createDistanceInformation(MeasurableInformation start, MeasurableInformation stop) {
        Vector3 ballDist = new Vector3(start.ballPosition).subtract(stop.ballPosition);
        Vector3 agentDist = new Vector3(start.agentPosition).subtract(stop.agentPosition);
        Vector3 agentRotAngle = new Vector3(start.agentRotation).subtract(stop.agentRotation);
        double time = start.time - stop.time;
        return new MeasurableInformation(ballDist, agentDist, agentRotAngle, time);
    }

}
