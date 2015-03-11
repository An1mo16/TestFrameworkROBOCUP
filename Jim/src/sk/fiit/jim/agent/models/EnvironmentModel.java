package sk.fiit.jim.agent.models;

import java.util.HashMap;
import java.util.Map;

import sk.fiit.jim.agent.parsing.ParsedData;
import sk.fiit.jim.agent.parsing.ParsedDataObserver;
import sk.fiit.jim.log.Log;

/**
 *  EnvironmentData.java
 *  
 *  Holds static information about the state of the world around the agent,
 *  such as game time, simulation time, play mode and server version. Server
 *  version is supposed to be set by ./scripts/config/settings.rb, support
 *  for 0.6.4 version added by Androids. 
 *
 *@Title	Jim
 *@author	marosurbanec
 *@author	Androids
 * 
 * Support for server vserion 0.6.7 added by Gitmen09.
 * @author Roman Moravcik
 */

public class EnvironmentModel implements ParsedDataObserver{
	private static EnvironmentModel instance = new EnvironmentModel();
	
	/**
	 * Returns EnvironmentModel static instance to work with it.
	 *
	 * @return
	 */
	public static EnvironmentModel getInstance() {
		return instance;
	}
	
	/* (non-Javadoc)
	 * @see sk.fiit.jim.agent.parsing.ParsedDataObserver#processNewServerMessage(sk.fiit.jim.agent.parsing.ParsedData)
	 */
	public void processNewServerMessage(ParsedData data) {
		GAME_TIME = data.GAME_TIME;
		SIMULATION_TIME = data.SIMULATION_TIME;
		PLAY_MODE = data.playMode;
	}
	
	/**
	 * Time step by which time is increased.
	 */
	public static double TIME_STEP = 0.02d;
	
	
	/**
	 * The game time which is calculated when game is running, that
	 * means not in BEFORE_KICK_OFF game mode.
	 * 
	 */
	public static double GAME_TIME;
	
	
	/**
	 * The simulation time which is calculated since server starts.
	 * Time from the beggining of server - runs forward only without any pause
	 */
	public static double SIMULATION_TIME;
	
	
	/**
	 * Play mode of the game with possible values: 
	 *  BEFORE_KICK_OFF,
		PLAY_ON,
		KICK_OFF_LEFT,
		KICK_OFF_RIGHT,
		KICK_IN_LEFT,
		KICK_IN_RIGHT,
		CORNER_KICK_LEFT,
		CORNER_KICK_RIGHT,
		GOAL_KICK_LEFT,
		GOAL_KICK_RIGHT,
		OFFSIDE_LEFT,
		OFFSIDE_RIGHT,
		GAME_OVER,
		GOAL_LEFT,
		GOAL_RIGHT,
		FREE_KICK_LEFT,
		FREE_KICK_RIGHT;
	 */
	public static PlayMode PLAY_MODE;
	
	
	public static Version version;
	
	/**
	 * 
	 *  EnvironmentModel.java
	 *  
	 *@Title        Jim
	 *@author       $Author: ??? $
	 */
	@SuppressWarnings("serial")
	public static enum PlayMode{
		BEFORE_KICK_OFF,
		PLAY_ON,
		KICK_OFF_LEFT,
		KICK_OFF_RIGHT,
		KICK_IN_LEFT,
		KICK_IN_RIGHT,
		CORNER_KICK_LEFT,
		CORNER_KICK_RIGHT,
		GOAL_KICK_LEFT,
		GOAL_KICK_RIGHT,
		OFFSIDE_LEFT,
		OFFSIDE_RIGHT,
		GAME_OVER,
		GOAL_LEFT,
		GOAL_RIGHT,
		FREE_KICK_LEFT,
		FREE_KICK_RIGHT;
		
		private static Map<String, PlayMode> serverNotation = new HashMap<String, PlayMode>(){{
			put("PlayOn", PLAY_ON);
			put("BeforeKickOff", BEFORE_KICK_OFF);
			put("KickOff_Left", KICK_OFF_LEFT);
			put("KickOff_Right", KICK_OFF_RIGHT);
			put("KickIn_Left", KICK_IN_LEFT);
			put("KickIn_Right", KICK_IN_RIGHT);
			put("corner_kick_left", CORNER_KICK_LEFT);
			put("corner_kick_right", CORNER_KICK_RIGHT);
			put("goal_kick_left", GOAL_KICK_LEFT);
			put("goal_kick_right", GOAL_KICK_RIGHT);
			put("offside_left", OFFSIDE_LEFT);
			put("offside_right", OFFSIDE_RIGHT);
			put("GameOver", GAME_OVER);
			put("Goal_Left", GOAL_LEFT);
			put("Goal_Right", GOAL_RIGHT);
			put("free_kick_left", FREE_KICK_LEFT);
			put("free_kick_right", FREE_KICK_RIGHT);
		}};

		public static PlayMode fromString(String group){
			return serverNotation.get(group);
		}
	}
	
	/**
	 * 
	 *  EnvironmentModel.java
	 *  
	 *  Version of server. Current version is VERSION_0_6_7.
	 *  Possible values:
         *  VERSION_0_6_7,
	 *  VERSION_0_6_5,
	 *  VERSION_0_6_4,
	 *  VERSION_0_6_3,
	 *  VERSION_0_6_2;
	 *  
	 *@Title        Jim
	 *@author       $Author: ??? $
	 */
	public static enum Version {
                VERSION_0_6_7,
		VERSION_0_6_5,
		VERSION_0_6_4,
		VERSION_0_6_3,
		VERSION_0_6_2;
	}
	
	//added by high5
	public static enum Beamable{
		BEFORE_KICK_OFF,
		GAME_OVER,
		GOAL_LEFT,
		GOAL_RIGHT,
		KICK_OFF_LEFT,
		KICK_OFF_RIGHT;
	}
	
	//added by high5
	public static enum BefKick{
		BEFORE_KICK_OFF;
	}
	
	public static boolean beamablePlayMode(){
		for (Beamable b : Beamable.values()){
			if (b.name().equals(PLAY_MODE.name())){
				return true;
			}
		}
		return false;
	}
	
	public String getPlayModeString() {
		return PLAY_MODE.name();
	}
	
	public static boolean isKickOffLeftPlayMode() {
		return PLAY_MODE.name().equals(Beamable.KICK_OFF_LEFT.name());
	}
	
	public static boolean isKickOffPlayMode() {
		return PLAY_MODE.name().equals(Beamable.KICK_OFF_LEFT.name()) 
				|| PLAY_MODE.name().equals(Beamable.KICK_OFF_RIGHT.name());
	}
	
	public static boolean isBeforeKickOffPlayMode() {
		return PLAY_MODE.name().equals(Beamable.BEFORE_KICK_OFF.name());
	}

}