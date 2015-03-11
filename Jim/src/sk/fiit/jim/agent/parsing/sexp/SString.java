/**
 * 
 */
package sk.fiit.jim.agent.parsing.sexp;

/**
 * 
 *  SString.java
 *  
 *  Single node in s-expresion tree.
 *  
 *@Title        Jim
 *@author       Author: Ondrej Jur??k
 */
public class SString extends SObject {
	
	public SString(String string)
	{
		_string = string;
	}
	
	private String _string;

	/**
	 * @return the string
	 */
	public String getString() {
		return _string;
	}
}
