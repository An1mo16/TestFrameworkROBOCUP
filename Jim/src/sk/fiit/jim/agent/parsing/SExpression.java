package sk.fiit.jim.agent.parsing;

import java.util.ArrayList;
import java.util.List;

/**
 *  SExpressionMessage.java
 *
 *@Title	Jim
 *@author	marosurbanec
 */
class SExpression{
	private final String message;

	public SExpression(String message){
		this.message = message;
	}
	
	public Iterable<SExpression> children(){
		List<SExpression> contentOfBraces = new ArrayList<SExpression>();
		int indentLevel = 0;
		StringBuilder currentContent = new StringBuilder();
		
		for (int i = 0 ; i < message.length() ; i++)
		{
			char currentOne = message.charAt(i);
			if (currentOne == ')') indentLevel--;
			
			if (indentLevel > 0)
				currentContent.append(currentOne);
			if (indentLevel == 0 && currentContent.length() > 0)
			{
				contentOfBraces.add(new SExpression(currentContent.toString()));
				currentContent = new StringBuilder();
			}
			
			if (currentOne == '(') indentLevel++;
		}
		return contentOfBraces;
	}
	
	@Override
	public String toString(){
		return message;
	}
	
	//delegated methods on String message
	
	public char charAt(int index){
		return message.charAt(index);
	}
	
	public int indexOf(char c){
		return message.indexOf(c);
	}

	public int indexOf(String str){
		return message.indexOf(str);
	}

	public int length(){
		return message.length();
	}

	public boolean startsWith(String prefix){
		return message.startsWith(prefix);
	}

	public String substring(int beginIndex, int endIndex){
		return message.substring(beginIndex, endIndex);
	}

	public String substring(int beginIndex){
		return message.substring(beginIndex);
	}
}