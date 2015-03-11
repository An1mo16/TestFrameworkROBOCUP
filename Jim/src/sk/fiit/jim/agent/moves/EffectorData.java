package sk.fiit.jim.agent.moves;

/**
 *  EffectorData.java
 *  
 *  Class representation of an effector tag
 *
 *@Title	Jim
 *@author	marosurbanec
 */
public class EffectorData{
	/**
	 * Effector - joint.
	 */
	public Joint effector;
	/**
	 * End angle of effector.
	 */
	public double endAngle;
	
	@Override
	public String toString(){
		return '<'+effector.name()+" end=\""+endAngle+"\" />";
	}
}