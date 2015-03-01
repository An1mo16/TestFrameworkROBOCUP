package sk.fiit.testframework.parsing;

public abstract class MessageParserFactory {

	private static EnvironmentMessageParser environmentMessageParser = new EnvironmentMessageParser();
	private static GameStateMessageParser gameStateMessageParser = new GameStateMessageParser();

	public static MessageParser getParser(String message) {
		if (message.startsWith("((time")) {
			return gameStateMessageParser;
		} else {
			return environmentMessageParser;
		}
	}
}
