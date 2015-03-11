package sk.fiit.jim.agent.trajectory;

import java.util.LinkedList;

import static java.lang.Math.*;
import sk.fiit.jim.agent.models.EnvironmentModel;
import sk.fiit.jim.agent.models.EnvironmentModel.Version;

import sk.fiit.jim.agent.models.Player;
import sk.fiit.jim.agent.models.WorldModel;
import sk.fiit.robocup.library.geometry.Angles;
import sk.fiit.robocup.library.geometry.Vector3D;

/**
 * 
 *  Obstacles.java
 *  
 *@Title        Jim
 *@author       $Author: Roman Bilevic$
 */
public class Obstacles {
	/**
	 * Half field length.
	 * -----------
	 * |  |---|  |
	 * |		 |
	 * |		 |
	 * |----O----| 
	 * |		 | \
	 * |		 |  \ - this distance
	 * |  |---|  |  /
	 * ----------- /
	 */
	public static final double HALF_FIELD_LENGTH = (EnvironmentModel.version == Version.VERSION_0_6_7 ? 15 : 10.5);
	/**
	 * Half field width.
	 * 
	 *        this distance
	 *         |
	 *       ------
	 *       |    |
	 *  -----------
	 *  |  |---|  |
	 *  |		  |
	 *  |		  |
	 *  |----O----| 
	 *  |		  |
	 *  |  		  |
	 *  |  |---|  |
	 *  -----------
	 */
	public static final double HALF_FIELD_WIDTH = 10;
	/**
	 * Radius where it is possible the robot can touch something by its body.
	 */
        // FIXME: Nova verzia ma v nastaveniach Agent Radius 0.4. Je otazne z coho je tato hodnota 0.2 urcena.
	public static final double ROBOT_RADIUS = 0.2; //metre
	/**
	 * Distance by which agent calculates how far from obstacle
	 * should he go, when he wants to pass it by.
	 */
	public static final double BYPASS_DISTANCE = 0.5; // metre
	
	/**
	 * Checks the intersection with robots way and obstacles in
	 * the way.  
	 *
	 * @param start
	 * @param end
	 * @param obstacles
	 * @param flag
	 * @return
	 */
	public static Vector3D checkIntersection(Vector3D start, Vector3D end, LinkedList<Vector3D> obstacles, boolean flag){
		Vector3D check = null;
		
		for(Vector3D centre : obstacles){
			double dx = end.getX() - start.getX();
			double dy = end.getY() - start.getY();
			
			double a = dx*dx + dy*dy;
			double b = 2*(dx*(start.getX() - centre.getX()) + dy*(start.getY() - centre.getY()));
			double c = centre.getX()*centre.getX() + centre.getY()*centre.getY();
			
			c += start.getX()*start.getX() + start.getY()*start.getY();
			c -= 2*(centre.getX()*start.getX() + centre.getY()*start.getY());
			c -= ROBOT_RADIUS*ROBOT_RADIUS;
			
			double discr = b*b - 4*a*c;
			
			if(discr > 0){
				Vector3D v = Vector3D.cartesian(end.getX()-centre.getX(), end.getY()-centre.getY(), 0);
				
				// ak su cielove suradnice v radiuse prekazky, tak sa tato prekazka ignoruje (robot sa musi dostat na cielove suradnice)
				if(!((flag == true) && (v.getR() < ROBOT_RADIUS))){
					check = centre;
					break;
				}
			}
		}
		
		return check;
	}
	
	/**
	 * Checks if specified position is in the area of field. True if 
	 * the position is in this area, false otherwise. 
	 *
	 * @param position
	 * @return
	 */
	public static boolean checkInField(Vector3D position){
		if((position.getX() > HALF_FIELD_LENGTH) || (position.getX() < -HALF_FIELD_LENGTH) || (position.getY() > HALF_FIELD_WIDTH) || (position.getY() < -HALF_FIELD_WIDTH))
			return false;
		else
			return true;
	}
	
	/**
	 * Finds the way to the left from obstacle by which agent can 
	 * pass obstacle by.
	 *
	 * @param phi
	 * @param obstacle
	 * @return
	 */
	public static Vector3D findLeftBypassPoint(double phi, Vector3D obstacle){
		double x = obstacle.getX() + (Math.cos(Angles.normalize(phi + (PI/2))) * BYPASS_DISTANCE);
		double y = obstacle.getY() + (Math.sin(Angles.normalize(phi + (PI/2))) * BYPASS_DISTANCE);
		Vector3D v = Vector3D.cartesian(x, y, 0);
		
		return v;		
	}
	
	/**
	 * Finds the way to the right from obstacle by which agent can 
	 * pass obstacle by.
	 *
	 * @param phi
	 * @param obstacle
	 * @return
	 */
	public static Vector3D findRightBypassPoint(double phi, Vector3D obstacle){
		double x = obstacle.getX() + (Math.cos(Angles.normalize(phi - (PI/2))) * BYPASS_DISTANCE);
		double y = obstacle.getY() + (Math.sin(Angles.normalize(phi - (PI/2))) * BYPASS_DISTANCE);
		Vector3D v = Vector3D.cartesian(x, y, 0);
		
		return v;		
	}
	
	/**
	 * Returns list of all seen players as obstacles. 
	 *
	 * @return
	 */
	public static LinkedList<Vector3D> getRealObstacles(){
		LinkedList<Vector3D> obstacles = new LinkedList<Vector3D>();
		for(Player p : WorldModel.getInstance().getTeamPlayers()){
			Vector3D playerPos = p.getAbsoluteHead();
			playerPos.setZ(0);
			obstacles.add(playerPos);
		}
		for(Player p : WorldModel.getInstance().getOpponentPlayers()){
			Vector3D playerPos = p.getAbsoluteHead();
			playerPos.setZ(0);
			obstacles.add(playerPos);

		}
		
		return obstacles;
	}
}
