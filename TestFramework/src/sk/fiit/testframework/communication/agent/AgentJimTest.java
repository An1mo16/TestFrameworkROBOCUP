/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.fiit.testframework.communication.agent;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import sk.fiit.testframework.communication.agent.AgentJim.TeamSide;
import sk.fiit.testframework.communication.robocupserver.RobocupServerAddress;

/**
 *
 * @author relation
 */
public class AgentJimTest {

    public static void main(String[] args) throws UnknownHostException, IOException {

//        TrainerCommand tc = new TrainerCommand.BallVelocity(new Point3D(1, 2, 3));
//
//        OutputStream os = new FileOutputStream(new File("/home/relation/command.dat"));
//        os.write(tc.getAsBytes());
//        os.flush();
//        os.close();


        // define server's and monitor's addresses
        RobocupServerAddress address = new RobocupServerAddress(InetAddress.getLocalHost(), 3100, 3200);

        // we attach to an running agent jim
        AgentJim agentJim = new AgentJim(new AgentData(10, TeamSide.LEFT, "ANDROIDS"), "localhost", 3070);

        //agentJim.invokeRestart();

    }
}
