package sk.fiit.jim.agent.models;

import sk.fiit.jim.agent.Side;

import java.util.HashMap;
import java.util.Map;

import sk.fiit.jim.agent.models.EnvironmentModel.Version;
import sk.fiit.robocup.library.geometry.Vector3D;

/**
 *  FixedObjects.java
 *  Flags seen by SEE perceptor of the robot. Each flag keeps information
 *  about its global position. For proper functioning of this class,
 *  make sure {@link EnvironmentModel}.{@link Version} is properly set.
 *	
 *@Title	Jim
 *@author	marosurbanec
 *@author	Androids
 */
public enum FixedObject{
	OUR_LOWER_CORNER,
	OUR_UPPER_CORNER,
	OUR_UPPER_POST,
	OUR_LOWER_POST,
	THEIR_UPPER_CORNER,
	THEIR_LOWER_CORNER,
	THEIR_UPPER_POST,
	THEIR_LOWER_POST;
	
	/*
	 * The playground size is the same for 0.6.3 and 0.6.4 versions.
         *  Version 0.6.7 has totally different dimensions.
	 */
	/**
	 * Returns absolute position of fixed object. 
	 *
	 * @return
	 */
	public Vector3D getAbsolutePosition(){
            Map<FixedObject, Vector3D> position;
            if (EnvironmentModel.version == Version.VERSION_0_6_2)
                position = positions_0_6_2;
            else if (EnvironmentModel.version == Version.VERSION_0_6_7)
                position = positions_0_6_7;
            else
                position = positions_0_6_5;
            
		Map<FixedObject, Vector3D> mapping = position;
		return mapping.get(this);
	}
	
        @SuppressWarnings("serial")
	private static Map<FixedObject, Vector3D> positions_0_6_7 = new HashMap<FixedObject, Vector3D>(){{
		put(OUR_LOWER_CORNER, Vector3D.cartesian(-15, -10.0, 0));
		put(OUR_UPPER_CORNER, Vector3D.cartesian(-15, 10.0, 0));
                //FIXME: co su tie jednotky?
		put(OUR_UPPER_POST, Vector3D.cartesian(-15, 1.0, 0.8));
		put(OUR_LOWER_POST, Vector3D.cartesian(-15, -1.0, 0.8));
		
		put(THEIR_LOWER_CORNER, Vector3D.cartesian(15, -10, 0));
		put(THEIR_UPPER_CORNER, Vector3D.cartesian(15, 10, 0));
		put(THEIR_UPPER_POST, Vector3D.cartesian(15, 1.0, 0.8));
		put(THEIR_LOWER_POST, Vector3D.cartesian(15, -1.0, 0.8));
	}};
        
	@SuppressWarnings("serial")
	private static Map<FixedObject, Vector3D> positions_0_6_5 = new HashMap<FixedObject, Vector3D>(){{
		put(OUR_LOWER_CORNER, Vector3D.cartesian(-10.5, -7.0, 0));
		put(OUR_UPPER_CORNER, Vector3D.cartesian(-10.5, 7.0, 0));
		put(OUR_UPPER_POST, Vector3D.cartesian(-10.5, 1.0, 0.8));
		put(OUR_LOWER_POST, Vector3D.cartesian(-10.5, -1.0, 0.8));
		
		put(THEIR_LOWER_CORNER, Vector3D.cartesian(10.5, -7.0, 0));
		put(THEIR_UPPER_CORNER, Vector3D.cartesian(10.5, 7.0, 0));
		put(THEIR_UPPER_POST, Vector3D.cartesian(10.5, 1.0, 0.8));
		put(THEIR_LOWER_POST, Vector3D.cartesian(10.5, -1.0, 0.8));
	}};
	
	//TODO: for future reference 0.6.2 version will not be used. This part can be deleted.
	@SuppressWarnings("serial")
	private static Map<FixedObject, Vector3D> positions_0_6_2 = new HashMap<FixedObject, Vector3D>(){{
		put(OUR_LOWER_CORNER, Vector3D.cartesian(-6.0, -4.0, 0));
		put(OUR_UPPER_CORNER, Vector3D.cartesian(-6.0, 4.0, 0));
		put(OUR_UPPER_POST, Vector3D.cartesian(-6.0, 0.8, 0.75));
		put(OUR_LOWER_POST, Vector3D.cartesian(-6.0, -0.8, 0.75));
		
		put(THEIR_LOWER_CORNER, Vector3D.cartesian(6.0, -4.0, 0));
		put(THEIR_UPPER_CORNER, Vector3D.cartesian(6.0, 4.0, 0));
		put(THEIR_UPPER_POST, Vector3D.cartesian(6.0, 0.8, 0.75));
		put(THEIR_LOWER_POST, Vector3D.cartesian(6.0, -0.8, 0.75));
	}};
	
	@SuppressWarnings("serial")
	private static Map<String, FixedObject> namesInServerMessages = new HashMap<String, FixedObject>(){{
		put("G2R", THEIR_LOWER_POST);
		put("G1R", THEIR_UPPER_POST);
		put("F1R", THEIR_UPPER_CORNER);
		put("F2R", THEIR_LOWER_CORNER);
		
		put("G2L", OUR_LOWER_POST);
		put("G1L", OUR_UPPER_POST);
		put("F1L", OUR_UPPER_CORNER);
		put("F2L", OUR_LOWER_CORNER);
	}};
	
	/**
	 * Returns fixed object from server by specified string id. 
	 *
	 * @param id
	 * @return
	 */
	public static FixedObject fromServerId(String id){
		return namesInServerMessages.get(id.toUpperCase());
	}
	
	public static Vector3D ourPostMiddle(){
		Vector3D postMiddle = OUR_LOWER_CORNER.getAbsolutePosition().add(OUR_UPPER_CORNER.getAbsolutePosition()).divide(2);
 		if (AgentModel.side == Side.LEFT)
 			return postMiddle;
 		else
 			return	postMiddle.setX(-postMiddle.getX());
 	}
 
 	public static Vector3D theirPostMiddle(){
 		Vector3D postMiddle = THEIR_LOWER_CORNER.getAbsolutePosition().add(THEIR_UPPER_CORNER.getAbsolutePosition()).divide(2);
 		if (AgentModel.side == Side.LEFT)
 			return postMiddle;
 		else
 			return	postMiddle.setX(-postMiddle.getX());
 	}
}