package sk.fiit.testframework.parsing;

import sk.fiit.testframework.parsing.models.messages.*;

public abstract class MessageParser extends SExpressionParser {

	public abstract Message parse(String message);

}
