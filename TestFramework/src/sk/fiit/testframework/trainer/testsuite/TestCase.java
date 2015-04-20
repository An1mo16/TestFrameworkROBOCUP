/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.fiit.testframework.trainer.testsuite;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import sk.fiit.robocup.library.annotations.UnderConstruction;
import sk.fiit.testframework.communication.robocupserver.RobocupServer;
import sk.fiit.testframework.communication.robocupserver.RobocupServerAddress;
import sk.fiit.testframework.monitor.AgentMonitor;
import sk.fiit.testframework.monitor.RobocupMonitor;
import sk.fiit.testframework.parsing.models.PlayMode;
import sk.fiit.testframework.worldrepresentation.ISimulationStateObserver;
import sk.fiit.testframework.worldrepresentation.models.SimulationState;

/**
 * Provides an abstract TestCase class. 
 * @author relation
 */
@UnderConstruction
public abstract class TestCase implements Callable<TestCaseResult>, ISimulationStateObserver {

	private static Logger logger = Logger.getLogger(TestCase.class.getName());
	
    protected RobocupServerAddress address;
    protected RobocupServer server;
    protected RobocupMonitor monitor;
    protected SimulationState simulationState;
    protected AgentMonitor agentServer;
    
    protected boolean updated = false;
    protected double initTime = 0;
    private boolean initialized = false;

    public String fileNameDt = null; //TODO: pridane pre aspekty
    public TestCase() {
        this.address = RobocupServerAddress.getConfigServerAddress();
    }

    @Override
    public synchronized void update() {
        simulationState = monitor.getSimulationState();
        if (!initialized) {
            if (monitor.isInformationComplete()) {
                setInitTime(simulationState.getGameStateInfo().getTime());
                initialized = true;
            }
        }
        setUpdated(true);
        notifyAll();
    }

    @Override
    public TestCaseResult call() throws Exception {
    	logger.info(this.getClass().getName() + ": Running test case ");
        if (init() == false) {
        	logger.warning(this.getClass().getName() + ": Init test case FAILED!");
        	return null;
        }
		Thread.sleep(1000);
		monitor.getSimulationState().registerObserver(this);
        while (true) {
            if (isUpdated()) {
                setUpdated(false);
                Thread.yield();
                if (!initialized) 
                    continue;

                if (isStopCriterionMet(simulationState)) {
                	TestCaseResult result = evaluate(simulationState);
                	logger.info(this.getClass().getName() + ": Destroy test case with result: " + result);
                	destroy();
                    return result;
                }
            }
        }
    }
    
    
    public synchronized boolean isUpdated() {
        return updated;
    }

    public synchronized void setUpdated(boolean updated) {
        this.updated = updated;
    }
    
    public void setInitTime(double initTime) {
        this.initTime = initTime;
    }

    public double getInitTime() {
        return initTime;
    }

    public double getElapsedTime() {
        return (monitor.getSimulationState().getGameStateInfo().getTime() - getInitTime());
    }
    
    protected boolean init() {
        try {
        	this.monitor = RobocupMonitor.getMonitorInstance(address);
			this.server = RobocupServer.getServerInstance(address);
			this.agentServer = AgentMonitor.getInstance();
	    	return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
    }
    public abstract boolean isStopCriterionMet(SimulationState ss);
    public abstract TestCaseResult evaluate(SimulationState ss);
    
    protected void destroy() {
    	try {
			this.server.setPlayMode(PlayMode.BeforeKickOff);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

}
