package sk.fiit.jim.agent.models;

import static java.lang.Math.hypot;
import sk.fiit.robocup.library.geometry.Vector3D;

public class DistanceHelper {

	
	public static double computeDistanceBetweenObjects(Vector3D first, Vector3D second)
	{
		double deltax = first.getX() - second.getX();
		double deltay = first.getY() - second.getY();
		
		return  hypot(deltax, deltay);
	}
}
