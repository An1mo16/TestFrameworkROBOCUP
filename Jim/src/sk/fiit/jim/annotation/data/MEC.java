/**
 * Name:    MEC.java
 * Created: 26.2.2012
 * 
 * @author: Miro Bimbo
 */
package sk.fiit.jim.annotation.data;

import java.util.List;

import sk.fiit.robocup.library.geometry.Circle;
import sk.fiit.robocup.library.geometry.Vector2;

/**
 * Class responsibile for computing minimal enclosing circle for 
 * list of points
 * 
 * @author Miro Bimbo
 *
 */
public class MEC {
	
	/**
	 * Calculates minimal enclosing circle for specified points. 
	 *
	 * @param points
	 * @return
	 */
	public static Circle minEnclosingCircle(List<Vector2> points){
		Vector2[] p = new Vector2[points.size()];
		int x = 0;
		for(Vector2 po : points){
			p[x]=po;
			x++;
		}
		return minCircle(points.size(),p,0,new Vector2[3]);
	}
	
	
    //...
    // Compute the center and radius of the smallest circle
    // enclosing the n points in P, such that the m points
    // in B lie on the boundary of the circle.
    //...
	private static Circle minCircle( int n, Vector2[] p, int m, Vector2[] b )
    {
		Vector2 c = new Vector2(0,0);
		double r = 0;
	
	
		//... Compute the smallest circle defined by B.
		if( m == 1 )
		    {
			c = new Vector2( b[0] );
			r = 0;
		    }
		else if( m == 2 )
		    {
			c = new Vector2( (b[0].getX()+b[1].getX())/2, (b[0].getY()+b[1].getY())/2 );
		        r = distance( b[0], c );
		    }
		else if( m == 3 )
			return findCenterRadius( b[0], b[1], b[2] );
	
	
		Circle minC = new Circle( c, r );
	
		//... Now see if all the points in P are enclosed.
		for( int i = 0;  i < n;  i++  )
		    if( distance(p[i], minC.getCenter()) > minC.getRadius() )
			{
			    //... Compute B <--- B union P[i].
			    b[m] = new Vector2( p[i] );
	
			    //... Recurse
			    minC = minCircle( i, p, m+1, b );
			}
	
		return minC;
    }
	
	private static double distance( Vector2 p1, Vector2 p2 )
	{
		return Math.sqrt( (p1.getX()-p2.getX())*(p1.getX()-p2.getX()) + (p1.getY()-p2.getY())*(p1.getY()-p2.getY()) );
	}
	
    //...
    // Given three points defining a circle,
    // compute the center and radius of the circle.
    //...

	private static Circle findCenterRadius( Vector2 p1, Vector2 p2, Vector2 p3 )
    {
		double x = (p3.getX()*p3.getX() * (p1.getY() - p2.getY()) 
			    + (p1.getX()*p1.getX() + (p1.getY() - p2.getY())*(p1.getY() - p3.getY())) 
			    * (p2.getY() - p3.getY()) + p2.getX()*p2.getX() * (-p1.getY() + p3.getY())) 
		    / (2 * (p3.getX() * (p1.getY() - p2.getY()) + p1.getX() * (p2.getY() - p3.getY()) + p2.getX() 
			    * (-p1.getY() + p3.getY())));
	
		double y = (p2.getY() + p3.getY())/2 - (p3.getX() - p2.getX())/(p3.getY() - p2.getY()) 
		    * (x - (p2.getX() + p3.getX())/2);
	
		Vector2 c = new Vector2( x, y );
		double r = distance(c, p1);
	
		return new Circle( c, r );
    }

}
