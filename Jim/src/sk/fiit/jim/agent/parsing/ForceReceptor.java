package sk.fiit.jim.agent.parsing;

import sk.fiit.robocup.library.geometry.Vector3D;

/**
 *  ForceReceptor.java
 *  
 *  Agent receives information about a force impacting both of his feet
 *  respectively. Each foot has a center point of this force and
 *  a {@link Vector3D} representation of this forces' magnitude.  
 *
 *@Title	Jim
 *@author	marosurbanec
 */
public class ForceReceptor{
	public Vector3D rightFootForce;
	public Vector3D leftFootForce;
	public Vector3D rightFootPoint;
	public Vector3D leftFootPoint;
}