/**
 * 
 */
package sk.fiit.jim.agent.parsing.sexp;

/**
 * 
 *  SException.java
 *  
 *  Error in s-expression parsing.
 *  
 *@Title        Jim
 *@author       Author: Ondrej Jur??k
 */
public class SException extends Exception {

	/**
	 * 
	 */
	public SException() {
		super();
	}

	/**
	 * @param message
	 */
	public SException(String message) {
		super(message);
	}
	

}
