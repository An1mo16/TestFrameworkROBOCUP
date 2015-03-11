/**
 * 
 */
package sk.fiit.jim.agent.parsing.sexp;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 *  SArray.java
 *  
 *  Parse s-expression into tree. It is set of SObjects. SObjects could by another SArray or simple node of tree SString.
 *  
 *@Title        Jim
 *@author       Author: Ondrej Jur??k
 */
public class SArray extends SObject {
	
	public SArray(String sexpresion) throws SException
	{
		if(checkExpresion(sexpresion))
		{
			parseSExpresion(sexpresion);
		}
		else
			throw new SException("Bad syntax");
	}
	
	private List<SObject> _values = new ArrayList<SObject>();
	
	/**
	 * Returns size of SObject list. 
	 *
	 * @return
	 */
	public int getLength()
	{
		return _values.size();
	}
	
	/**
	 * Returns SObject by index.
	 *
	 * @param index
	 * @return
	 */
	public SObject getOnIndex(int index)
	{
		if(index >= _values.size() || index < 0 )
			return null;
		return _values.get(index);
	}
	
	private void parseSExpresion(String sexpresion) throws SException
	{
		char[] expresion = sexpresion.toCharArray();
		char _char;
		StringBuilder buffer = new StringBuilder();
		for(int i = 0; i < (expresion.length); i++ )
		{
			_char = expresion[i];
			
			if(_char == ' ' && buffer.length() > 0)
			{
				_values.add(new SString(buffer.toString()));
				buffer = new StringBuilder();
				continue;
			}
			
			if(_char == '(')
			{
				int first = i;
				int _leftBranch = 0;
				for( ; i < (expresion.length - 1) ; i++)
				{
					_char = expresion[i];
					if(_char == '('){
						_leftBranch++;
						continue;
					}
					if(_char == ')'){
						_leftBranch--;
					}
					if(_leftBranch == 0)
						break;
				}
				_values.add(new SArray(sexpresion.substring(first+1, i)));
				i++;
				continue;
			}
			buffer.append(_char);
			
			
		}
		if(buffer.length() > 0)
		{
			_values.add(new SString(buffer.toString()));
			buffer = new StringBuilder();
		}
	}
	
	private boolean checkExpresion(String sexpresion)
	{
		int _leftBranch = 0;
		for(char _char : sexpresion.toCharArray())
		{
			if(_char == '('){
				_leftBranch++;
				continue;
			}
			if(_char == ')') {
				_leftBranch--;
			}
			if(_leftBranch < 0)
				return false;
		}
		if(_leftBranch == 0)
			return true;
		else
			return false;
	}
}
