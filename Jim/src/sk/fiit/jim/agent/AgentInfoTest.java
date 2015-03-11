package sk.fiit.jim.agent;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.robocup.library.geometry.Vector3D;

/**
 *  AgentInfo.java
 *  
 *  Class contains test case for method calculateDistance 
 *
 *@Title	Jim
 *@author	Filip Blanarik, Martin Markech
 *@author	Gitmen09
 */

import sk.fiit.robocup.library.geometry.Vector3D;

public class AgentInfoTest {
	
	
	private AgentInfo agentinfo;
	
	@Before
	public void setup()	{
		agentinfo = new AgentInfo();		
	}
	
	
	@Test
	public void calculate_distance() {
		int x,y;
		Vector3D vector1 = Vector3D.cartesian(0, 0, 0);
	    Vector3D vector2 = Vector3D.cartesian(0, 0, 0);
		
	    for(x=-100; x<100;x++) {
	    	vector1 = Vector3D.cartesian(x, 0, 0);
	    		    	
	    	if(x >= 0) {
	    		assertTrue(agentinfo.calculateDistance(vector1, vector2) == x);
	    	}
	    	else {
	    		assertTrue(agentinfo.calculateDistance(vector1, vector2) == -x);
	    	}
	    }
	    vector1 = Vector3D.cartesian(0, 0, 0);
	    for(y=-100; y<100;y++) {
	    	vector2 = Vector3D.cartesian(0, y, 0);
	    		    	
	    	if(y >= 0) {
	    		assertTrue(agentinfo.calculateDistance(vector1, vector2) == y);
	    	}
	    	else {
	    		assertTrue(agentinfo.calculateDistance(vector1, vector2) == -y);
	    	}
	    }
	    
	   vector1 = Vector3D.cartesian(0, 0, 0);
	   vector2 = Vector3D.cartesian(3, 4, 0);
	   assertTrue(agentinfo.calculateDistance(vector1, vector2) == 5);
	    
	   vector1 = Vector3D.cartesian(5, 12, 0);
	   vector2 = Vector3D.cartesian(0, 0, 0);
	   assertTrue(agentinfo.calculateDistance(vector1, vector2) == 13);
	   
	   vector1 = Vector3D.cartesian(8, 15, 0);
	   vector2 = Vector3D.cartesian(0, 0, 0);
	   assertTrue(agentinfo.calculateDistance(vector1, vector2) == 17);
	   
	   vector1 = Vector3D.cartesian(9, 40, 0);
	   vector2 = Vector3D.cartesian(0, 0, 0);
	   assertTrue(agentinfo.calculateDistance(vector1, vector2) == 41);
	   
	   vector1 = Vector3D.cartesian(11, 60, 0);
	   vector2 = Vector3D.cartesian(0, 0, 0);
	   assertTrue(agentinfo.calculateDistance(vector1, vector2) == 61);
	   
	   vector1 = Vector3D.cartesian(13, 84, 0);
	   vector2 = Vector3D.cartesian(0, 0, 0);
	   assertTrue(agentinfo.calculateDistance(vector1, vector2) == 85);
	   
	   vector1 = Vector3D.cartesian(7, 15, 0);
	   vector2 = Vector3D.cartesian(2, 3, 0);
	   assertTrue(agentinfo.calculateDistance(vector1, vector2) == 13);
    
	}

	@Test
	public void TestWhereIsTarget(){
		Vector3D vector1 = Vector3D.cartesian(0, 1, 2); 
		Vector3D vector2 = Vector3D.cartesian(2, 1, 2); 
		Vector3D vector3 = Vector3D.cartesian(-1, 0, 2);
		Vector3D vector4 = Vector3D.cartesian(0, -1, 2);
			
		assertEquals("Vector3D.cartesian(0, 1, 2) should be equal to left:", "left"  , agentinfo.whereIsTarget(vector1) );		
		assertEquals("Vector3D.cartesian(2, 1, 2) should be equal to front:", "front"  , agentinfo.whereIsTarget(vector2) );
		assertEquals("Vector3D.cartesian(-1, 0, 2) should be equal to back:", "back"  , agentinfo.whereIsTarget(vector3) );
		assertEquals("Vector3D.cartesian(0, -1, 2) should be equal to left:", "right"  , agentinfo.whereIsTarget(vector4) );
	}	


}

