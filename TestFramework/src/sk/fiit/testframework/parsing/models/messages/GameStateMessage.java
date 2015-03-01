package sk.fiit.testframework.parsing.models.messages;

import sk.fiit.testframework.parsing.models.*;

public class GameStateMessage extends Message {

	private GameStatePart gameStatePart;
	private SceneGraphHeaderPart sceneGraphHeaderPart;
	private SceneGraphPart sceneGraphPart;

	public GameStateMessage() {
		super(MessageType.GameState);
	}

	public void setGameStatePart(GameStatePart gameStatePart) {
		this.gameStatePart = gameStatePart;
	}

	public GameStatePart getGameStatePart() {
		return gameStatePart;
	}

	public void setSceneGraphHeaderPart(
			SceneGraphHeaderPart sceneGraphHeaderPart) {
		this.sceneGraphHeaderPart = sceneGraphHeaderPart;
	}

	public SceneGraphHeaderPart getSceneGraphHeaderPart() {
		return sceneGraphHeaderPart;
	}

	public void setSceneGraphPart(SceneGraphPart sceneGraphPart) {
		this.sceneGraphPart = sceneGraphPart;
	}

	public SceneGraphPart getSceneGraphPart() {
		return sceneGraphPart;
	}

}
