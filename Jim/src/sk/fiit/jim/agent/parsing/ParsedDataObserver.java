package sk.fiit.jim.agent.parsing;

/**
 *  ParsedDataObserver.java
 *  
 *  Any object that needs to be notified when a server message is received
 *  should implement this interface and sign up to {@link Parser}.subscribe().
 *
 *@Title	Jim
 *@author	marosurbanec
 */
public interface ParsedDataObserver{
	public void processNewServerMessage(ParsedData data);
}
