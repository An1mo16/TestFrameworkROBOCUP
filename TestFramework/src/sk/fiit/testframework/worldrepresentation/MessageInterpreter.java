package sk.fiit.testframework.worldrepresentation;

import sk.fiit.testframework.parsing.models.*;
import sk.fiit.testframework.parsing.models.messages.*;
import sk.fiit.testframework.worldrepresentation.models.*;

public class MessageInterpreter {

	private SceneUpdater sceneUpdater = new SceneUpdater();
	private boolean init = true;
	
	
	public void interpret(Message message, SimulationState currentState) {
		if (init) {
			setInitialGameStateInfo(currentState);
			init=false;
		}
		if (message.getType() == MessageType.Environment) {
			EnvironmentMessage em = (EnvironmentMessage) message;

			currentState.setEnvironmentInfo(em.getEnvironmentPart());
			
			sceneUpdater.update(currentState.getScene(),
					em.getSceneGraphHeaderPart(), em.getSceneGraphPart());

		} else {
				GameStateMessage gm = (GameStateMessage) message;

				// update game state information
				mergeGameStateInfo(currentState.getGameStateInfo(),
						gm.getGameStatePart());

				sceneUpdater.update(currentState.getScene(),
						gm.getSceneGraphHeaderPart(), gm.getSceneGraphPart());
		}

		currentState.notifyObservers();

	}

	private void setInitialGameStateInfo(SimulationState currentState) {
		GameStatePart gsp = new GameStatePart();

		gsp.setTime(0);
		gsp.setTeamLeft("");
		gsp.setTeamRight("");

		currentState.setGameStateInfo(gsp);
	}

	private void mergeGameStateInfo(GameStatePart currentState,
			GameStatePart newState) {
		if (newState.getTime() != -1) {
			currentState.setTime(newState.getTime());
		}

		if (newState.getTeamLeft() != null) {
			currentState.setTeamLeft(newState.getTeamLeft());
		}

		if (newState.getTeamRight() != null) {
			currentState.setTeamRight(newState.getTeamRight());
		}

		if (newState.getHalf() != -1) {
			currentState.setHalf(newState.getHalf());
		}

		if (newState.getPlayMode() != -1) {
			currentState.setPlayMode(newState.getPlayMode());
		}

		if (newState.getScoreLeft() != -1) {
			currentState.setScoreLeft(newState.getScoreLeft());
		}

		if (newState.getScoreRight() != -1) {
			currentState.setScoreRight(newState.getScoreRight());
		}
	}

}
