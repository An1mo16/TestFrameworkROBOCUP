package sk.fiit.testframework.worldrepresentation.models;

import java.util.ArrayList;

import sk.fiit.testframework.parsing.models.*;
import sk.fiit.testframework.worldrepresentation.*;

public class SimulationState {
	
	private ArrayList<ISimulationStateObserver> observers;
	private EnvironmentPart environmentInfo;
	private GameStatePart gameStateInfo;
	private Scene scene;
	
	public SimulationState()	{
		observers = new ArrayList<ISimulationStateObserver>();
		scene = new Scene();
	}
	
	public void registerObserver(ISimulationStateObserver observer) {
		observers.add(observer);
	}
	
	public void unregisterObserver(ISimulationStateObserver observer) {
		observers.remove(observer);
	}
	
	public void notifyObservers() {
		//for (ISimulationStateObserver o:observers) {
		for(int i = 0; i < observers.size(); i++)
			observers.get(i).update();
		//}
	}

	public void setEnvironmentInfo(EnvironmentPart environmentInfo) {
		this.environmentInfo = environmentInfo;
	}

	public EnvironmentPart getEnvironmentInfo() {
		return environmentInfo;
	}

	public void setGameStateInfo(GameStatePart gameStateInfo) {
		this.gameStateInfo = gameStateInfo;
	}

	public GameStatePart getGameStateInfo() {
		return gameStateInfo;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public Scene getScene() {
		return scene;
	}
	
	
}
