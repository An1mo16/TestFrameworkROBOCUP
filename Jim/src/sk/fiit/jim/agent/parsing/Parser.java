package sk.fiit.jim.agent.parsing;

import java.util.ArrayList;
import java.util.List;

/**
 *  Parser.java
 *
 *@Title	Jim
 *@author	marosurbanec
 */
public class Parser{
	
	ParsedData data;
	String message;
	private static List<ParsedDataObserver> observers = new ArrayList<ParsedDataObserver>();
	
	//was called in dependencies.rb, since migration to Java it is called in Main
	public static synchronized void subscribe(ParsedDataObserver observer){
		observers.add(observer);
	}
	
	public static synchronized void clearObservers(){
		observers.clear();
	}

	private void notifyObservers(){
		synchronized (Parser.class){
			for (ParsedDataObserver observer : observers)
				observer.processNewServerMessage(data);
		}
	}
	
	/**
	 * Parses specified message to ParsedData. 
	 *
	 * @param message
	 * @return
	 */
	public ParsedData parse(String message){
		data = new ParsedData();
		this.message = message;
		String[] breakDown = breakDown();
		for (String perceptor : breakDown){
			String perceptorId = perceptor.substring(0, perceptor.indexOf(' '));
			Perceptors.processPerceptor(perceptorId, perceptor, data);
		}
		
		notifyObservers();
		return data;
	}

	private String[] breakDown(){
		List<String> contentOfBraces = new ArrayList<String>();
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
				contentOfBraces.add(currentContent.toString());
				currentContent = new StringBuilder();
			}
			
			if (currentOne == '(') indentLevel++;
		}
		return contentOfBraces.toArray(new String[]{});
	}
}