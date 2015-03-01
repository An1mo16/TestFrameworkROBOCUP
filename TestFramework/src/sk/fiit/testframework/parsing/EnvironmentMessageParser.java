package sk.fiit.testframework.parsing;

import sk.fiit.testframework.parsing.models.*;
import sk.fiit.testframework.parsing.models.messages.*;

public class EnvironmentMessageParser extends MessageParser {

	private SceneGraphHeaderPartParser sceneGraphHeaderPartParser = new SceneGraphHeaderPartParser();
	private SceneGraphPartParser sceneGraphPartParser = new SceneGraphPartParser();
	
	public Message parse(String message) {
		EnvironmentMessage result = new EnvironmentMessage();

		int currentIndex = 0;

		// environment part
		String environmentSection = getWholeSection(message, currentIndex);
		parseEnvironmentPart(environmentSection, result, currentIndex);
		currentIndex += environmentSection.length();

		// scene graph header part
		String sceneGraphHeaderSection = getWholeSection(message, currentIndex);
		result.setSceneGraphHeaderPart(sceneGraphHeaderPartParser.parse(sceneGraphHeaderSection));
		currentIndex += sceneGraphHeaderSection.length();
		
		//scene graph part
		String sceneGraphSection = getWholeSection(message, currentIndex);
		result.setSceneGraphPart(sceneGraphPartParser.parse(sceneGraphSection));
				
		return result;
	}

	private void parseEnvironmentPart(String section,
			EnvironmentMessage message, int currentIndex) {
		currentIndex++;

		EnvironmentPart environmentPart = new EnvironmentPart();
		message.setEnvironmentPart(environmentPart);
		
		while (currentIndex < section.length() - 1) {
			String part = getWholeSection(section, currentIndex);
			processEnvironmentPart(part, environmentPart);
			currentIndex += part.length();
		}
	}

	private void processEnvironmentPart(String part, EnvironmentPart environmentPart) {
		String[] sExpression = processSExpression(part);

		String name = sExpression[0];

		if (name.equals("FieldLength")) {
			environmentPart.setFieldLength(Double.parseDouble(sExpression[1]));
		} else if (name.equals("FieldWidth")) {
			environmentPart.setFieldWidth(Double.parseDouble(sExpression[1]));
		} else if (name.equals("FieldHeight")) {
			environmentPart.setFieldHeight(Double.parseDouble(sExpression[1]));
		} else if (name.equals("GoalWidth")) {
			environmentPart.setGoalWidth(Double.parseDouble(sExpression[1]));
		} else if (name.equals("GoalDepth")) {
			environmentPart.setGoalDepth(Double.parseDouble(sExpression[1]));
		} else if (name.equals("GoalHeight")) {
			environmentPart.setGoalHeight(Double.parseDouble(sExpression[1]));
		} else if (name.equals("FreeKickDistance")) {
			environmentPart.setFreeKickDistance(Double.parseDouble(sExpression[1]));
		} else if (name.equals("WaitBeforeKickOff")) {
			environmentPart.setWaitBeforeKickOff(Double.parseDouble(sExpression[1]));
		} else if (name.equals("AgentRadius")) {
			environmentPart.setAgentRadius(Double.parseDouble(sExpression[1]));
		} else if (name.equals("BallRadius")) {
			environmentPart.setBallRadius(Double.parseDouble(sExpression[1]));
		} else if (name.equals("BallMass")) {
			environmentPart.setBallMass(Double.parseDouble(sExpression[1]));
		} else if (name.equals("RuleGoalPauseTime")) {
			environmentPart.setRuleGoalPauseTime(Double.parseDouble(sExpression[1]));
		} else if (name.equals("RuleKickInPauseTime")) {
			environmentPart.setRuleKickInPauseTime(Double.parseDouble(sExpression[1]));
		} else if (name.equals("RuleHalfTime")) {
			environmentPart.setRuleHalfTime(Double.parseDouble(sExpression[1]));
		} else if (name.equals("play_modes")) {
			PlayMode [] playModes = new PlayMode[sExpression.length-1];
			for (int i=1;i<sExpression.length;i++) {
				playModes[i-1]=PlayMode.valueOf(sExpression[i]);
			}
			environmentPart.setPlayModes(playModes);
		}

	}
}
