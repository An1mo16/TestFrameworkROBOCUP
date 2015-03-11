package sk.fiit.jim.agent.parsing;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.jim.agent.models.FixedObject;
import sk.fiit.robocup.library.geometry.Vector3D;
import static java.lang.Math.toRadians;

/**
 *  SeePerceptor.java
 *  
 *  Updates {@link ParsedData} based on the information received in SEE
 *  perceptor. This includes ball's position, players' positions and position
 *  of flags.
 *
 *@Title	Jim
 *@author	marosurbanec
 */
final class SeePerceptor{

	private static Pattern polarVectorPattern = Pattern.compile(
		"^[A-Z1-2]{1,3} \\(pol ([-.0-9]+) ([-.0-9]+) ([-.0-9]+)"
	);
	
	private static Pattern playerPattern = Pattern.compile(
		"^P \\(team ([A-Za-z_0-9]+)\\) \\(id ([0-9])\\)"
	);
	
	private static Pattern bodyPartPattern = Pattern.compile(
		"\\(([a-z]+) \\(pol ([0-9-.]+) ([0-9-.]+) ([0-9-.]+)\\)\\)"
	);
	
	private final SExpression seeMessage;

	public SeePerceptor(SExpression sExpression){
		this.seeMessage = sExpression;
	}

	public void update(ParsedData data){
		for (SExpression seenObject : seeMessage.children()){
			if (seenObject.charAt(0) == 'B'){
				data.ballRelativePosition = parseVectorFromPolarString(seenObject.toString());
				continue;
			}
			if (seenObject.charAt(0) == 'P'){
				parsePlayer(seenObject.toString(), data);
				continue;
			}
			FixedObject object = FixedObject.fromServerId(seenObject.substring(0, 3));
			if (object != null){
				data.fixedObjects.put(object, parseVectorFromPolarString(seenObject.toString()));
				continue;
			}
		}
	}
	
	private final void parsePlayer(final String message, final ParsedData data){
		//don't do this unless we know who we are!
		if (AgentInfo.playerId == 0)
			return;
		
		final Matcher matcher = playerPattern.matcher(message);
		
		if (!matcher.find())
			return;
		
		final String team = matcher.group(1);
		final boolean isTeammate = team.equals(AgentInfo.team);
		final int playerId = Integer.valueOf(matcher.group(2));
		final Map<String, Vector3D> bodyParts = extractBodyParts(message.substring(matcher.end()));
		
		
		// xjurcako this is not actual
		/*if (isTeammate)
			data.teammates.put(playerId, bodyParts);
		else
			data.opponents.put(playerId, bodyParts);*/
	}

	private Map<String, Vector3D> extractBodyParts(String message) {
		Matcher bodyPartMatcher = bodyPartPattern.matcher(message);
		
		Map<String, Vector3D> bodyParts = new HashMap<String, Vector3D>();
		String bodyPart = null;
		Vector3D where = null;
		while(bodyPartMatcher.find()){
			bodyPart = bodyPartMatcher.group(1);
			double r = Double.valueOf(bodyPartMatcher.group(2));
			double phi = Double.valueOf(bodyPartMatcher.group(3));
			double theta = Double.valueOf(bodyPartMatcher.group(4));
			where = Vector3D.spherical(r, phi, theta);
			bodyParts.put(bodyPart, where);
		}
		return bodyParts;
	}

	private static Vector3D parseVectorFromPolarString(String message){
		Matcher polarMatcher = polarVectorPattern.matcher(message);
		polarMatcher.find();
		double r = Double.valueOf(polarMatcher.group(1));
		double phi = Double.valueOf(polarMatcher.group(2));
		double theta = Double.valueOf(polarMatcher.group(3));
		return Vector3D.spherical(r, toRadians(phi), toRadians(theta));
	}
}