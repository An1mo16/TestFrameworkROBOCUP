package sk.fiit.jim.agent.moves;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
/**
 *  Effector.java
 *  
 *  Enumeration of joints robot has. Is also capable of translating incoming
 *  joint IDs into appropriate {@link Joint}. Holds information about its
 *  available angle range and allows programmer to trim an angle to this range.
 *  
 *  ANGLES SHOULD BE SUPPLIED IN DEG SYSTEM
 *
 *@Title	Jim
 *@author	marosurbanec
 */
@SuppressWarnings("serial")
public enum Joint implements Serializable {
	HE1 (-120.0, 120.0),
	HE2 (-45.0, 45.0),
	RLE1 (-90.0, 1.0),
	RLE2 (-45.0, 25.0),
	RLE3 (-25.0, 100.0),
	RLE4 (-130.0, 1.0),
	RLE5 (-45.0, 75.0),
	RLE6 (-25.0, 45.0),
	RAE1 (-120.0, 120.0),
	RAE2 (-95.0, 1.0),
	RAE3 (-90.0, 90.0),
	RAE4 (-120.0, 120.0),
	
	LLE1 (-90.0, 1.0),
	LLE2 (-25.0, 45.0),
	LLE3 (-25.0, 100.0),
	LLE4 (-130.0, 1.0),
	LLE5 (-45.0, 75.0),
	LLE6 (-45.0, 25.0),
	LAE1 (-120.0, 120.0),
	LAE2 (-1.0, 95.0),
	LAE3 (-90.0, 90.0),
	LAE4 (-120.0, 120.0);
	
	private double lowerLimit;
	private double upperLimit;
	
	private Joint(double low, double up){
		this.lowerLimit = low;
		this.upperLimit = up;
	}
	
	private static Map<String, Joint> serverNotation = new HashMap<String, Joint>(){{
		put("hj1", HE1);
		put("hj2", HE2);
		
		put("raj1", RAE1);
		put("raj2", RAE2);
		put("raj3", RAE3);
		put("raj4", RAE4);

		put("laj1", LAE1);
		put("laj2", LAE2);
		put("laj3", LAE3);
		put("laj4", LAE4);
		
		put("rlj1", RLE1);
		put("rlj2", RLE2);
		put("rlj3", RLE3);
		put("rlj4", RLE4);
		put("rlj5", RLE5);
		put("rlj6", RLE6);
		
		put("llj1", LLE1);
		put("llj2", LLE2);
		put("llj3", LLE3);
		put("llj4", LLE4);
		put("llj5", LLE5);
		put("llj6", LLE6);
	}};
	
	/**
	 * Returns Joint from server by specified string id. 
	 *
	 * @param jointId
	 * @return
	 */
	public static Joint fromServerNotation(String jointId){
		return serverNotation.get(jointId.toLowerCase());
	}
	
	/**
	 * Returns lower limit of Joint angle if specified angle parameter is 
	 * less than lower limit or upper limit of Joint angle if specified angle
	 * parameter is greater than upper limit. Otherwise returns specified angle.
	 *
	 * @param angleInXml
	 * @return
	 */
	public double trim(double angleInXml){
		if (angleInXml < lowerLimit) return lowerLimit;
		if (angleInXml > upperLimit) return upperLimit;
		return angleInXml;
	}
}