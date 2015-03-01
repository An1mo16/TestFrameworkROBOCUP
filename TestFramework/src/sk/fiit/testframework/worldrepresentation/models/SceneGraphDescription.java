package sk.fiit.testframework.worldrepresentation.models;

import java.util.ArrayList;

import sk.fiit.testframework.parsing.models.*;


public class SceneGraphDescription {

	private ArrayList<Integer> ballPosition;
	private SceneGraphPart fullSceneGraph;
	private ArrayList<Integer> playerPositions;
	
	
	public void setFullSceneGraph(SceneGraphPart fullSceneGraph) {
		this.fullSceneGraph = fullSceneGraph;
	}

	public SceneGraphPart getFullSceneGraph() {
		return fullSceneGraph;
	}

	public void setBallPosition(ArrayList<Integer> ballPosition) {
		this.ballPosition = ballPosition;
	}

	public ArrayList<Integer> getBallPosition() {
		return ballPosition;
	}

	public void setPlayerPositions(ArrayList<Integer> playerPositions) {
		this.playerPositions = playerPositions;
	}

	public ArrayList<Integer> getPlayerPositions() {
		return playerPositions;
	}
	
}
