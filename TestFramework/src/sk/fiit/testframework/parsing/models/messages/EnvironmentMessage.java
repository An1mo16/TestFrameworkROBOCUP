package sk.fiit.testframework.parsing.models.messages;

import sk.fiit.testframework.parsing.models.*;

public class EnvironmentMessage extends Message {

	private EnvironmentPart environmentPart;
	private SceneGraphHeaderPart sceneGraphHeaderPart;
	private SceneGraphPart sceneGraphPart;
	
	public EnvironmentMessage() {
		super(MessageType.Environment);
	}

	public void setEnvironmentPart(EnvironmentPart environmentPart) {
		this.environmentPart = environmentPart;
	}

	public EnvironmentPart getEnvironmentPart() {
		return environmentPart;
	}

	public void setSceneGraphHeaderPart(SceneGraphHeaderPart sceneGraphHeaderPart) {
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
