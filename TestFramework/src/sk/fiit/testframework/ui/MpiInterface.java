package sk.fiit.testframework.ui;

import java.util.logging.Logger;

import mpi.MPI;
import sk.fiit.robocup.library.annotations.UnderConstruction;
import sk.fiit.testframework.communication.robocupserver.RobocupServerAddress;
import sk.fiit.testframework.monitor.AgentMonitor;
import sk.fiit.testframework.monitor.RobocupMonitor;
import sk.fiit.testframework.trainer.testsuite.TestCaseResult;
import sk.fiit.testframework.worldrepresentation.ISimulationStateObserver;

/**
 * Experimental interface for MPI counting
 * Do nothing yet.
 * 
 * @author Miro Bimbo (High 5)
 *
 */
public class MpiInterface implements UserInterface, ISimulationStateObserver  {
	
	private Logger logger = Logger.getLogger(getClass().getName());
	private RobocupServerAddress serverAddress;
    private RobocupMonitor monitor;
    private AgentMonitor agentServer  = AgentMonitor.getInstance();
	
    @UnderConstruction
	public MpiInterface(RobocupServerAddress robocupAddress) {
    	this.serverAddress = robocupAddress;
    	try {
    	monitor = RobocupMonitor.getMonitorInstance(serverAddress);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
	
    @UnderConstruction
	public void start(){
		logger.info("starting MPI interface ");
		int me = MPI.COMM_WORLD.Rank();
		int size = MPI.COMM_WORLD.Size();
		logger.info("Hello from MPI process ID: "+me+" of "+size+" processes");
		
		//if I am master
		if(me == 0){
			int[] work = new int[100];
			//send some "work" to every node
			for(int i=1;i<size;i++){
				work[0] = i*2;
				//have to study MPJ express API
				//buffer, initialOffset, count, dataType, dest, tag
				MPI.COMM_WORLD.Send(work, 0, 1, MPI.INT, i, 0);
				System.out.println("p:"+me+":sent to "+i);
			}
		//if I am slave
		//recieve some "work"
		}else {
			int[] work = new int[100];
			System.out.println("p:"+me+":before recieving");
			//buffer, initialOffset, count, dataType, source, tag
			MPI.COMM_WORLD.Recv(work, 0, 1, MPI.INT, 0, 0);
			System.out.println("p:"+me+":recieved from 0 value:"+work[0]);
		}
		if (agentServer != null) agentServer.interrupt();
		if (monitor != null) monitor.interrupt();
		MPI.Finalize();
		System.exit(0);	
		
		//:::FUTURE WORK:::
		//IF MPI MASTER
		// LOOP
		//  WHAT TO DO? .. training? annotating? get from somewhere
		//	DISTRIBUTE WORK
		//	BARIER RECIEVE
		// ENDLOOP 	
		//
		//IF MPI SLAVE
		//  START PLAYER? START SERVER?
		//	LOOP
		//		IF end_mess THEN end()
		//		IF work_mess THEN  DO work
		//	ENDLOOP
	}

	@Override
	public void testFinished(TestCaseResult result) {
		logger.info("Test finished with fitness : " +((Double)result.getFitness()).toString());
	}

	@Override
	public boolean shoudExitOnEmptyQueue() {
		return false;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

}
