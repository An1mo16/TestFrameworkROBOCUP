package sk.fiit.testframework.monitor;

import java.io.IOException;
import java.net.InetAddress;

import org.junit.Test;

import sk.fiit.robocup.library.geometry.Point3D;
import sk.fiit.testframework.communication.agent.AgentJim;
import sk.fiit.testframework.communication.agent.AgentJim.TeamSide;
import sk.fiit.testframework.communication.agent.AgentData;
import sk.fiit.testframework.communication.robocupserver.RobocupServer;
import sk.fiit.testframework.communication.robocupserver.RobocupServerAddress;
import sk.fiit.testframework.parsing.models.PlayMode;
import sk.fiit.testframework.worldrepresentation.ISimulationStateObserver;

public class RobocupMonitorTest {

    @Test
    public void test1() throws IOException {


        final RobocupMonitor monitor = RobocupMonitor.getMonitorInstance("127.0.0.1", 3200);
        RobocupServerAddress address = new RobocupServerAddress(InetAddress.getLocalHost(), 3100, 3200);

        RobocupServer t = RobocupServer.getServerInstance(address);
        AgentJim jim = new AgentJim(new AgentData(10, TeamSide.LEFT, "ANDROIDS"), "localhost", 3070);

        t.setAgentPosition(jim.getAgentData(), new Point3D(0.0, 0.0, 0.4));
        t.setPlayMode(PlayMode.PlayOn);
       t.setBall(new Point3D(0.5, 0.5, 0.5), new Point3D(1, 1, 1));

        monitor.getSimulationState().registerObserver(
                new ISimulationStateObserver() {

                    @Override
                    public void update() {
                        if (monitor.getSimulationState().getScene().getPlayers().size() > 0) {
//                            Player player = monitor.getSimulationState().getScene().getPlayers().get(0);

                            System.out.println("Ball: " + monitor.getSimulationState().getScene().getBallLocation());
                            System.out.println("Body:" + monitor.getSimulationState().getScene().getPlayers().get(0).getLocation());
                            System.out.println(monitor.getSimulationState().getScene().getPlayers().size());
//                    System.out.println(player.getBody().getHead().getTransformation().getValues()[12]);

                            //System.out.println(player.isOnGround());

                            //System.out.println(((NaoBody)player.getBody()).getHead().getTransformation().getValues()[14]);
                        }


                    }
                });

        System.out.println("start");
        monitor.run();


    }

    public static void main(String[] args) throws IOException {
        //test1();
    }
}
