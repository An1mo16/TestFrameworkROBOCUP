package sk.fiit.testframework.parsing;

import sk.fiit.testframework.parsing.models.*;
import sk.fiit.testframework.parsing.models.messages.*;

public class GameStateMessageParser extends MessageParser  {

	private SceneGraphHeaderPartParser sceneGraphHeaderPartParser = new SceneGraphHeaderPartParser();
	private SceneGraphPartParser sceneGraphPartParser = new SceneGraphPartParser();
	
	public Message parse(String message) {
		GameStateMessage result = new GameStateMessage();

		int currentIndex = 0;

		// game state part
		String gameStateSection = getWholeSection(message, currentIndex);
		parseGameStatePart(gameStateSection, result, currentIndex);
		currentIndex += gameStateSection.length();

		// scene graph header part
		String sceneGraphHeaderSection = getWholeSection(message, currentIndex);
		result.setSceneGraphHeaderPart(sceneGraphHeaderPartParser.parse(sceneGraphHeaderSection));
		currentIndex += sceneGraphHeaderSection.length();
		
		//scene graph part
		String sceneGraphSection = getWholeSection(message, currentIndex);
		result.setSceneGraphPart(sceneGraphPartParser.parse(sceneGraphSection));
				
		return result;
	}

	private void parseGameStatePart(String section,
			GameStateMessage message, int currentIndex) {
		currentIndex++;

		GameStatePart gameStatePart = new GameStatePart();
		message.setGameStatePart(gameStatePart);
		
		while (currentIndex < section.length() - 1) {
			String part = getWholeSection(section, currentIndex);
			processGameStatePart(part, gameStatePart);
			currentIndex += part.length();
		}
	}

	private void processGameStatePart(String part, GameStatePart gameStatePart) {
		String[] sExpression = processSExpression(part);

		String name = sExpression[0];

		if (name.equals("time")) {
			gameStatePart.setTime(Double.parseDouble(sExpression[1]));
		} else if (name.equals("team_left")) {
			gameStatePart.setTeamLeft(sExpression[1]);
		} else if (name.equals("team_right")) {
			gameStatePart.setTeamRight(sExpression[1]);
		} else if (name.equals("half")) {
			gameStatePart.setHalf(Integer.parseInt(sExpression[1]));
		} else if (name.equals("score_left")) {
			gameStatePart.setScoreLeft(Integer.parseInt(sExpression[1]));
		} else if (name.equals("score_right")) {
			gameStatePart.setScoreRight(Integer.parseInt(sExpression[1]));
		} else if (name.equals("play_mode")) {
			gameStatePart.setPlayMode(Integer.parseInt(sExpression[1]));
		} 
		

	}
}
