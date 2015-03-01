/**
 * Name:    LocalImplementation.java
 * Created: Nov 19, 2011
 * 
 * @author: ivan
 */
package sk.fiit.testframework.init;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;

import sk.fiit.testframework.agenttrainer.AgentMoveTrainer;
import sk.fiit.testframework.communication.agent.AgentManager;
import sk.fiit.testframework.communication.robocupserver.RobocupServerAddress;
import sk.fiit.testframework.monitor.AgentMonitor;
import sk.fiit.testframework.monitor.RobocupMonitor;
import sk.fiit.testframework.trainer.testsuite.ITestCaseObserver;
import sk.fiit.testframework.trainer.testsuite.TestCase;
import sk.fiit.testframework.trainer.testsuite.TestCaseResult;
import sk.fiit.testframework.ui.MainFrame;
import sk.fiit.testframework.ui.UserInterface;
import sk.fiit.testframework.ui.UserInterfaceFactory;

public class LocalImplementation extends Implementation {
	
	private Process robocupServerProcess;
	
	@Override
	public void run(String[] args) {		
		String robocupServerStartcommand = C.getProperty(C.PROPERTIES_ROBOCUP_SERVER_COMMAND);
		
		if (robocupServerStartcommand.length()>0) {
			try {
				Socket socket = new Socket();
				try {
					socket.connect(new InetSocketAddress("localhost", 3100), 200);
					if (socket.isConnected())
						logger.info("Using existing RobocupServer instance");
				} catch (IOException e) {
					//used to split the command by spaces, to make it possible to run commands with arguments
					StringTokenizer commandStr = new StringTokenizer(robocupServerStartcommand);
					List<String> command = new LinkedList<String>();
					while (commandStr.hasMoreTokens())
						command.add(commandStr.nextToken());
					ProcessBuilder builder = new ProcessBuilder(command);
					builder.redirectErrorStream(true);
					robocupServerProcess = builder.start();
					new Thread(new Runnable() {
						public void run() { readForever(new BufferedReader(new InputStreamReader(robocupServerProcess.getInputStream())));}
					},"RobocupServer read").start();
					logger.info("Starting robocup server instance");
					int tries = 0;
					while (true) {
						socket = new Socket();
						tries++;
						Thread.sleep(1000);
						try {
							socket.connect(new InetSocketAddress("localhost", 3100), 200);
							break;
						} catch (IOException e2) {
							if (tries > 5) throw e2;
						}
						logger.fine("Checking if RoboupServer is alive");
					}
				}
				socket.close();
			} catch (IOException e) {
				logger.log(Level.SEVERE, "RobocupServer instance could not be started - exiting", e);
			} catch (InterruptedException e) {
				logger.log(Level.SEVERE, "RobocupServer instance could not be started - exiting", e);
			}
		} else
			logger.info("NOT starting robocup server instance - no command");
		
		RobocupServerAddress rsa = RobocupServerAddress.getConfigServerAddress();
		RobocupMonitor monitor = RobocupMonitor.getMonitorInstance(rsa);
		AgentMonitor agentServer  = AgentMonitor.getInstance();
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
		
		while (true) {
			synchronized (this) {
				while (ProcessQueue.isEmpty() && TrainerQueue.isEmpty()) {
					if (userInterface == null || userInterface.shoudExitOnEmptyQueue()) {
						logger.info("Nothing to do - exiting");
						if (agentServer != null) agentServer.interrupt();
						monitor.interrupt();
						System.exit(0);
						return;
					} else 
					logger.info("Nothing to do - waiting");
					try {
						this.wait();
					} catch (InterruptedException e) {
						logger.warning("ProcessQueue or TrainerQueue interrupted");
						return;
					}
				}
			}
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
			AgentMoveTrainer trainer = TrainerQueue.poll();
			if (trainer != null) {
				// TODO IMPLEMENT TRAINER
			}
		}
	}

	@Override
	public void exit() {
		AgentManager.getManager().shutDownAgents();
		if (robocupServerProcess != null) {
			logger.info("Shutting down robocup server");
			robocupServerProcess.destroy();
			if (!C.getProperty(C.PROPERTIES_ROBOCUP_SERVER_KILLCOMMAND).isEmpty()) {
				try {
					Runtime.getRuntime().exec(C.getProperty(C.PROPERTIES_ROBOCUP_SERVER_KILLCOMMAND));
				} catch (IOException e) {
					logger.warning("Failed to execute server kill command: " + e.getMessage());
				}
			}
		}
		System.exit(0);
	}
	
	private void readForever(BufferedReader stream) {
		try {
			while(stream.readLine() != null);
		} catch (Exception e) {}
	}
	
}
