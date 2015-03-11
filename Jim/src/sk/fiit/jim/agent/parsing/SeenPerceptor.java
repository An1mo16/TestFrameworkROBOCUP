/**
 * 
 */
package sk.fiit.jim.agent.parsing;

import static java.lang.Math.toRadians;

import sk.fiit.jim.agent.models.FixedObject;
import sk.fiit.jim.agent.parsing.sexp.SArray;
import sk.fiit.jim.agent.parsing.sexp.SException;
import sk.fiit.jim.agent.parsing.sexp.SObject;
import sk.fiit.jim.agent.parsing.sexp.SString;
import sk.fiit.robocup.library.geometry.Vector3D;

/**
 * 
 *  SeenPerceptor.java
 *  
 *  SeenPerceptor parse message from see perceptor. 
 *  This class use SArray class on parse s-expresion into tree structure.
 *  Encapsulate data retrieve from seen perceptor into SeenPerceptroData class
 *  
 *@Title        Jim
 *@author       Author: Ondrej Jur??k
 */
public class SeenPerceptor {
	
	public static SeenPerceptorData retrieveSeenData(String message)
	{
		SArray _sarray = parseSeenMessage(message);
		
		SeenPerceptorData data = new SeenPerceptorData();
		
		for(int i = 0; i < _sarray.getLength(); i++)
		{
			SObject object = _sarray.getOnIndex(i);
			if(object instanceof SArray)
			{
				dispatch((SArray)object, data);
			}
		}
		
		return data;
	}
	
	private static void dispatch(SArray array, SeenPerceptorData data)
	{
		FixedObject object;
		if(array.getOnIndex(0) != null && (array.getOnIndex(0) instanceof SString))
		{
			String id = ((SString)array.getOnIndex(0)).getString();
			if ("B".equals(id))
			{
				data.ball = getPolarCoordinates((SArray)array.getOnIndex(1));
				return;
			}
				
			if("P".equals(id))
			{
				PlayerData player = getPlayer(array);
				if(player != null)
					data.players.add(player);
				return;
			}
						
			object = FixedObject.fromServerId(id);			
			if (object != null)
			{
				data.fixedObjects.put(object,  getPolarCoordinates((SArray)array.getOnIndex(1)));
				return;
			}
		}
	}
	
	private static PlayerData getPlayer(SArray message)
	{
		SObject object= message.getOnIndex(0);
		if(object != null && object instanceof SString)
		{
			if(((SString)object).getString().equals("P"))
			{
				
				PlayerData player = new PlayerData();
				
				for(int i = 1; i < message.getLength(); i ++)
				{
					SArray obj = (SArray)message.getOnIndex(i);
					if(obj != null && obj instanceof SArray)
					{
						
						String id = ((SString)obj.getOnIndex(0)).getString();
						
						if("team".equals(id))
						{
							player.team = ((SString)obj.getOnIndex(1)).getString(); 
							continue;
						}
						if("id".equals(id))
						{
							player.id = ((SString)obj.getOnIndex(1)).getString();
							continue;
						}
						
						player.bodyParts.put(id, getPolarCoordinates((SArray)obj.getOnIndex(1)));
						
						
					}
				}
				
				return player;
			}
		}
		return null;
	}
	
	private static Vector3D getPolarCoordinates(SArray message)
	{
		SObject object= message.getOnIndex(0);
		if(object != null && object instanceof SString)
		{
			if(((SString)object).getString().equals("pol"))
			{
				double r = 0;
				SObject polar = message.getOnIndex(1);
				if(polar != null && polar instanceof SString)
					r = Double.valueOf(((SString)polar).getString());
				else
					return null;
				
				double phi = 0;
				polar = message.getOnIndex(2);
				if(polar != null && polar instanceof SString)
					phi = Double.valueOf(((SString)polar).getString());
				else
					return null;
								
				double theta = 0;
				polar = message.getOnIndex(3);
				if(polar != null && polar instanceof SString)
					theta = Double.valueOf(((SString)polar).getString());
				else
					return null;
				
				return Vector3D.spherical(r, toRadians(phi), toRadians(theta));
			}
		}
			
		
		return null;
		
	}
	
	private static SArray parseSeenMessage(String message)
	{
		String expresion = message.substring(4);
		SArray parsedexpresion;
		try {
			parsedexpresion = new SArray(expresion);
		} catch (SException e) {
			e.printStackTrace();
			return null;
		}
		return parsedexpresion;
	}

}
