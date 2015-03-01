package sk.fiit.testframework.init;

import java.util.logging.Logger;

import mpi.MPI;
import sk.fiit.robocup.library.annotations.UnderConstruction;
import sk.fiit.testframework.agenttrainer.AgentMoveTrainer;
import sk.fiit.testframework.communication.robocupserver.RobocupServerAddress;
import sk.fiit.testframework.monitor.AgentMonitor;
import sk.fiit.testframework.monitor.RobocupMonitor;
import sk.fiit.testframework.trainer.testsuite.ITestCaseObserver;
import sk.fiit.testframework.trainer.testsuite.TestCase;
import sk.fiit.testframework.trainer.testsuite.TestCaseResult;
import sk.fiit.testframework.ui.UserInterface;
import sk.fiit.testframework.ui.UserInterfaceFactory;

/**
 * Experimental Implementation for MPI counting
 * 
 * 
 * @author Miro Bimbo (High 5)
 *
 */

public class MpiImplementation extends Implementation {

	private static Logger logger = Logger.getLogger(MpiImplementation.class.getName());
	
	private int me,size;
	
	public int getMe() {
		return me;
	}

	public int getSize() {
		return size;
	}
	
	@UnderConstruction
	@Override
	public void run(String[] args) {
		
		MPI.Init(args);
		me = MPI.COMM_WORLD.Rank();
		size = MPI.COMM_WORLD.Size();
		
		RobocupServerAddress rsa = RobocupServerAddress.getConfigServerAddress();
		RobocupMonitor monitor = RobocupMonitor.getMonitorInstance(rsa);
		AgentMonitor agentServer  = AgentMonitor.getInstanceForMpi(me);
		UserInterface userInterface = UserInterfaceFactory.getUserInterfaceInstance(rsa);
		
		
		if (monitor == null) {
			logger.severe("Monitor instance not created - exiting");
			System.exit(-1);
		}
		
		logger.info("starting monitor thread");
		monitor.start();
		
		if (userInterface != null) {
			testCaseObservers.add(userInterface);
			agentTrainerObservers.add(userInterface);
			logger.info("starting user interface");
			userInterface.start();
		}
		
		if (agentServer != null) {
			logger.info("starting agentServer thread");
			agentServer.start();
		}
		
		logger.info("starting main loop");
		
		//endless loop
		while (true) {
			synchronized (this) {
				//if there is nothing to do
				while (ProcessQueue.isEmpty() && TrainerQueue.isEmpty()) {
					//if there is no source of new commands 
					//= there is no userInterface, or interface is unable to 
					//assign new work
					if (userInterface == null || userInterface.shoudExitOnEmptyQueue()) {
						//end program
						logger.info("Nothing to do - exiting");
						if (agentServer != null) agentServer.interrupt();
						monitor.interrupt();
						System.exit(0);
						return;
					} else 
					//if there is some way of getting new commands
					logger.info("Nothing to do - waiting");
					try {
						//wait until they will come
						this.wait();
					} catch (InterruptedException e) {
						logger.warning("Error while waiting in queue");
						return;
					}
				}
			}
			//if there is something to do (mean testCase), do it.
			TestCase testcase = ProcessQueue.poll();
			if (testcase != null) {
				try {
					TestCaseResult result = testcase.call();
					for (ITestCaseObserver observer : testCaseObservers) {
						observer.testFinished(result);
					}
					ITestCaseObserver observer = localTestCaseObservers.remove(testcase);
					if (observer != null) observer.testFinished(result);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			//if there is something to do (mean trainer), do it.
			AgentMoveTrainer trainer = TrainerQueue.poll();
			if (trainer != null) {
				// TODO IMPLEMENT TRAINER
			}
		}
	}

	@Override
	public void exit() {
		
	}
}
