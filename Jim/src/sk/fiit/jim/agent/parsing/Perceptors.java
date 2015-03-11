package sk.fiit.jim.agent.parsing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.toRadians;

import sk.fiit.jim.agent.models.FixedObject;
import sk.fiit.jim.agent.models.EnvironmentModel.PlayMode;
import sk.fiit.jim.agent.moves.Joint;
import sk.fiit.robocup.library.geometry.Vector3D;

/**
 *  Perceptors.java
 *
 *@Title	Jim
 *@author	marosurbanec
 *@author	Androids
 */
class Perceptors{
	
	private static Pattern touchPattern = Pattern.compile( 
		"^TCH n bumper val (1|0)"
	);
	
	private static Pattern gyroRatePattern = Pattern.compile(
		"^GYR \\(n torso\\) \\(rt ([-.0-9]+) ([-.0-9]+) ([-.0-9]+)"
	);
	
	private static Pattern forcePattern = Pattern.compile(
		"^FRP \\(n (rf|lf)\\) \\(c ([-.0-9]+) ([-.0-9]+) ([-.0-9]+)\\) \\(f ([-.0-9]+) ([-.0-9]+) ([-.0-9]+)"
	);
	
	private static Pattern polarVectorPattern = Pattern.compile(
		"^pol ([-.0-9]+) ([-.0-9]+) ([-.0-9]+)"
	);
	
	private static Pattern acceleratorRatePattern = Pattern.compile(
		"^ACC \\(n torso\\) \\(a ([-.0-9]+) ([-.0-9]+) ([-.0-9]+)"
	);
	
	public static void processPerceptor(String perceptorId, String message, ParsedData data){
		//TODO refactor into class hierarchy
		if ("HJ".equals(perceptorId))
			retrieveHingeJointAngle(message, data);
		else if ("GS".equals(perceptorId))
			retrieveGameState(message, data);
		else if ("time".equals(perceptorId))
			retrieveSimulationTime(message, data);
		else if ("GYR".equals(perceptorId))
			retrieveGyroRate(message, data);
		else if ("FRP".equals(perceptorId))
			retrieveForceReceptor(message, data);
		else if ("See".equals(perceptorId))
			retrieveSeenData(message, data);
		else if ("ACC".equals(perceptorId))
			retrieveAcceleratorPattern(message, data);
		else if ("TCH".equals(perceptorId))
			retrieveTouchReceptor(message,data);
		else if ("hear".equals(perceptorId))
			retrieveHearReceptor(message,data);
	}

	private static void retrieveTouchReceptor(String message, ParsedData data){
		String bumperString = message.substring(message.indexOf("val ")+4);
		data.bumper = ("1".equals(bumperString));
	}
	
	private static void retrieveHearReceptor(String message, ParsedData data){
		String[] tempStr = message.split(" ");
		
		HearReceptor hrp = data.hearReceptor == null ? new HearReceptor() : data.hearReceptor;
		
		hrp.time = Double.valueOf(tempStr[1]);
		hrp.isMessageForMe = "self".equals(tempStr[2]) ? true : false;
		if (!hrp.isMessageForMe )
			hrp.destinationRelativeAngle = Double.valueOf(tempStr[2]);
		hrp.message = tempStr[3];
		
		data.hearReceptor = hrp;
	}
	
	private static void retrieveHingeJointAngle(String message, ParsedData data){
		String jointId = message.substring(message.indexOf("n ")+2, message.indexOf(")"));
		String angleValue = message.substring(message.indexOf("ax ")+3, message.lastIndexOf(")"));
		data.agentsJoints.put(Joint.fromServerNotation(jointId), Double.valueOf(angleValue));
	}

	private static void retrieveGameState(String message, ParsedData data){
		//see ParserTest for message dump
		String gameTimeString = message.substring(message.indexOf("t ")+2);
		gameTimeString = gameTimeString.substring(0, gameTimeString.indexOf(")"));
		data.GAME_TIME = Double.valueOf(gameTimeString);
		String playMode = message.substring(message.indexOf("pm ")+3);
		playMode = playMode.substring(0, playMode.indexOf(")"));
		data.playMode = PlayMode.fromString(playMode);
		if (message.contains("unum")){
			String number = message.substring(message.indexOf("unum ")+5);
			number = number.substring(0, number.indexOf(")"));
			data.PLAYER_ID = Integer.valueOf(number);
		}
		if (message.contains("team")){
			String team = message.substring(message.indexOf("team ")+5);
			team = team.substring(0, team.indexOf(")"));
			data.OUR_SIDE_IS_LEFT = "left".equals(team);
		}
	}

	private static void retrieveSimulationTime(String message, ParsedData data){
		String timeString = message.substring(message.indexOf("now ")+4, message.indexOf(")"));
		data.SIMULATION_TIME = Double.valueOf(timeString);
	}

	private static void retrieveGyroRate(String message, ParsedData data){
		Matcher gyroMatcher = gyroRatePattern.matcher(message);
		if (gyroMatcher.find()){
			double x = Double.valueOf(gyroMatcher.group(1));
			double y = Double.valueOf(gyroMatcher.group(2));
			double z = Double.valueOf(gyroMatcher.group(3));
			data.gyroscope = Vector3D.cartesian(x, y, z);
		}
	}
	
	private static void retrieveAcceleratorPattern(String message, ParsedData data){
		Matcher acceleratorMatcher = acceleratorRatePattern.matcher(message);
		if (acceleratorMatcher.find()){
			double x = Double.valueOf(acceleratorMatcher.group(1));
			double y = Double.valueOf(acceleratorMatcher.group(2));
			double z = Double.valueOf(acceleratorMatcher.group(3));
			data.accelerometer = Vector3D.cartesian(x, y, z);
		}
	}

	private static void retrieveForceReceptor(String message, ParsedData data){
		Matcher frpMatcher = forcePattern.matcher(message);
		if (frpMatcher.find()){
			ForceReceptor frp = data.forceReceptor == null ? new ForceReceptor() : data.forceReceptor;
			boolean isRight = "rf".equals(frpMatcher.group(1));
			double x = Double.valueOf(frpMatcher.group(2));
			double y = Double.valueOf(frpMatcher.group(3));
			double z = Double.valueOf(frpMatcher.group(4));
			Vector3D center = Vector3D.cartesian(x, y, z);
			x = Double.valueOf(frpMatcher.group(5));
			y = Double.valueOf(frpMatcher.group(6));
			z = Double.valueOf(frpMatcher.group(7));
			Vector3D magnitude = Vector3D.cartesian(x, y, z);
			if (isRight){
				frp.rightFootForce = magnitude;
				frp.rightFootPoint = center;
			}
			else{
				frp.leftFootForce = magnitude;
				frp.leftFootPoint = center;
			}
			data.forceReceptor = frp;
		}
	}
	
	private static void retrieveSeenData(String message, ParsedData data){
		
		SeenPerceptorData seendata = SeenPerceptor.retrieveSeenData(message);
		
		data.ballRelativePosition = seendata.ball;
		data.fixedObjects = seendata.fixedObjects;
		data.otherplayers = seendata.players;
		
		//xjurcako exchange by SeenPerceptor parser
		/*int braceDepth = 0;
		StringBuilder buffer = new StringBuilder();
		String id = null;
		Vector3D polarCoordinate = null;
		
		for (int charIndex = 0; charIndex < message.length() ; charIndex++){
			char currentChar = message.charAt(charIndex);
			if (currentChar == ')' && id != null && buffer.indexOf("pol ") == 0){
				polarCoordinate = parseVectorFromPolarString(buffer);
				pushIdAndCoordinateToData(id.trim(), polarCoordinate, data);
				id = null; polarCoordinate = null;buffer = new StringBuilder();
			}
			if (currentChar == ')'){braceDepth--;continue;}
			if (currentChar == '('){braceDepth++;continue;}
			if (braceDepth == 0){continue;}
			
			buffer.append(currentChar);
			if (currentChar == ' ' && braceDepth == 1){
				id = buffer.toString();
				buffer = new StringBuilder();
			}
		}*/
	}
	
	public static Vector3D parseVectorFromPolarString(StringBuilder buffer){
		
		if(buffer == null) {
			return null;
		}
		
		Matcher polarMatcher = polarVectorPattern.matcher(buffer.toString());
		polarMatcher.find();
		double r = Double.valueOf(polarMatcher.group(1));
		double phi = Double.valueOf(polarMatcher.group(2));
		double theta = Double.valueOf(polarMatcher.group(3));
		return Vector3D.spherical(r, toRadians(phi), toRadians(theta));
	}
	
	private static void pushIdAndCoordinateToData(String id, Vector3D polarCoordinate, ParsedData data){
		if ("B".equals(id))
			data.ballRelativePosition = polarCoordinate;
		FixedObject object = FixedObject.fromServerId(id);
		if (object == null) return;
		data.fixedObjects.put(object, polarCoordinate);
	}
}