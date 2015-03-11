package sk.fiit.jim.agent.moves;

import java.util.HashMap;
import java.util.Map;

import sk.fiit.robocup.library.review.ReviewOk;

/**
 *  SkipFlags.java
 *  
 *  Static class recording a "database" of a value of each flag => default
 *  value for any flag is FALSE. Provides querying methods on those values.
 *
 *@Title	Jim
 *@author	marosurbanec
 */
@ReviewOk
public class SkipFlags{
	private SkipFlags(){}
	private static Map<SkipFlag, Boolean> values = new HashMap<SkipFlag, Boolean>();
	
	/**
	 * Returns true if specified flag is set to true, otherwise false. 
	 *
	 * @param flag
	 * @return
	 */
	public static boolean isTrue(SkipFlag flag){
		//do NOT autobox to 'boolean' => values may contain null, raising NullPointerException on autoboxing
		Boolean isSet = values.get(flag);
		return isSet != null && isSet == Boolean.TRUE;
	}
	
	/**
	 * Sets specified flag to true and adds it to the HashMap. 
	 *
	 * @param flag
	 */
	public static void setTrue(SkipFlag flag){
		values.put(flag, Boolean.TRUE);
	}
	/**
	 * Sets specified flag to false and adds it to the HashMap. 
	 *
	 * @param flag
	 */
	public static void setFalse(SkipFlag flag){
		values.put(flag, Boolean.FALSE);
	}
	
	/**
	 * Clears the flags HashMap. 
	 *
	 */
	public static void reset(){
		values.clear();
	}
}