package sk.fiit.testframework.parsing;

import java.security.InvalidParameterException;

public abstract class SExpressionParser {

	protected String getWholeSection(String message, int currentIndex) {
		if (message.charAt(currentIndex) != '(') {
			throw new InvalidParameterException();
		}

		int bracketsToSkip = 0;
		int startIndex = currentIndex;

		currentIndex++;

		while (true) {
			if (message.charAt(currentIndex) == ')') {
				if (bracketsToSkip == 0) {
					// section end
					break;
				} else {
					bracketsToSkip--;
				}
			} else if (message.charAt(currentIndex) == '(') {
				bracketsToSkip++;
			}

			currentIndex++;
		}

		return message.substring(startIndex, currentIndex + 1);
	}

	protected String[] processSExpression(String sExpression) {
		return removeOuterBrackets(sExpression).split(" ");
	}

	protected String removeOuterBrackets(String string) {
		return string.substring(1, string.length() - 1);
	}

	protected int moveToNextSection(String string, int currentIndex) {
		int length = string.length();

		while (++currentIndex < length) {
			if (string.charAt(currentIndex) == '(') {
				return currentIndex;
			}
		}

		return -1;
	}
}
