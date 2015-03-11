package sk.fiit.jim.agent.models;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sk.fiit.jim.Settings;
import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.jim.agent.Side;
import sk.fiit.jim.agent.communication.testframework.Message;
import sk.fiit.jim.agent.communication.testframework.TestFrameworkCommunication;
import sk.fiit.jim.agent.parsing.ParsedData;
import sk.fiit.jim.agent.parsing.ParsedDataObserver;
import sk.fiit.jim.agent.parsing.PlayerData;
import sk.fiit.jim.agent.parsing.PlayerData.PlayerPartsNames;
import sk.fiit.jim.annotation.data.Annotation;
import sk.fiit.jim.annotation.data.Axis;
import sk.fiit.jim.log.Log;
import sk.fiit.jim.log.LogLevel;
import sk.fiit.jim.log.LogType;
import sk.fiit.robocup.library.geometry.Vector3D;

/**
 *  WorldModel.java
 *  
 *  Data holder for objects on the playground. Carries information about
 *  their respective position, velocity and their average observed velocity.
 *
 *@Title	Jim
 *@author	marosurbanec
 *@author	Androids
 */
public class WorldModel implements ParsedDataObserver, Serializable {
	private static final long serialVersionUID = -3520471121050216807L;
	
	private DynamicObject ball = new DynamicObject();
	private AgentModel agentModel;
	private static WorldModel instance = new WorldModel(AgentModel.getInstance());
	//private Vector3D predikciaBall2 = Vector3D.ZERO_VECTOR;
	
	//added by Androids.
	//TODO: prediction of team and opponent players - done, v adent.models.prediction.Prophet
	private Map<Integer, Player> teamPlayers = new HashMap<Integer, Player>();
	private Map<Integer, Player> opponentPlayers = new HashMap<Integer, Player>();
	
	public WorldModel(AgentModel model){
		this.agentModel = model;
		Log.setPattern(Settings.getString("Jim_root_path") + "/fixtures/test_log.txt");
		try {
			Log.setOutput("FILE");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Returns WorldModel static instance to work with it. 
	 *
	 * @return
	 */
	public static WorldModel getInstance(){
		return instance;
	}
	
	
	
	
	public boolean isSeeBall(){
		return this.getBall().notSeenLongTime() < 5;
	}
	
	//added by Androids
	//TODO: calculating who is on our team and who is the opponent.
	/* (non-Javadoc)
	 * @see sk.fiit.jim.agent.parsing.ParsedDataObserver#processNewServerMessage(sk.fiit.jim.agent.parsing.ParsedData)
	 */
	public void processNewServerMessage(ParsedData data){
		if (data.ballRelativePosition != null)
			calculateBallPosition(data);
		Log.setLogLevel(LogLevel.LOG);
		//Log.log(LogType.OTHER, ball.toString());
		calculatePlayers(data.otherplayers, data.SIMULATION_TIME);
		
		try{
			//System.out.println("started:"+lowSkill.name);
			TestFrameworkCommunication.sendMessage(new Message().WorldModel().changed(WorldModel.getInstance()));
		}catch(Exception e){
			System.out.println("chyba v spracovani spravy vo WorldModeli.");
		}
		
	}
	
	//added by Androids
	//changed by Jurcak
	private void calculatePlayers(List<PlayerData> players, double timeSeen) {
		for(PlayerData playerdata: players)
		{
			if(Integer.parseInt(playerdata.id) == AgentInfo.playerId && AgentInfo.team.equals(playerdata.team))
				continue;
			
			if(AgentInfo.team.equals(playerdata.team))
			{
				updateOurTeam(playerdata, timeSeen);
			}
			else
			{
				updateOpponentTeam(playerdata, timeSeen);
			}
		}
	}
	
	private void updateOurTeam(PlayerData playerdata, double timeSeen)
	{
		int id = Integer.parseInt(playerdata.id);
		Player player = teamPlayers.get(id);
		if(player == null)
		{
			player = new Player(this, true, id);
			teamPlayers.put(id, player);
		}
		updatePlayer(player, playerdata, timeSeen);
	}
	
	private void updateOpponentTeam(PlayerData playerdata, double timeSeen)
	{
		int id = Integer.parseInt(playerdata.id);
		Player player = opponentPlayers.get(id);
		if(player == null)
		{
			player = new Player(this, false, id);
			opponentPlayers.put(id, player);
		}
		updatePlayer(player, playerdata, timeSeen);
	}
	
	private void updatePlayer(Player player, PlayerData playerdata, double timeSeen)
	{
		Vector3D relativeposition = playerdata.bodyParts.get(PlayerPartsNames.llowerarm);
		if(relativeposition != null && relativeposition != Vector3D.ZERO_VECTOR)
			player.setLlowerarm(relativeposition);
		
		relativeposition = playerdata.bodyParts.get(PlayerPartsNames.rlowerarm);
		if(relativeposition != null && relativeposition != Vector3D.ZERO_VECTOR)
			player.setRlowerarm(relativeposition);
		
		relativeposition = playerdata.bodyParts.get(PlayerPartsNames.lfoot);
		if(relativeposition != null && relativeposition != Vector3D.ZERO_VECTOR)
			player.setLfoot(relativeposition);
		
		relativeposition = playerdata.bodyParts.get(PlayerPartsNames.rfoot);
		if(relativeposition != null && relativeposition != Vector3D.ZERO_VECTOR)
			player.setRfoot(relativeposition);
		
		relativeposition = playerdata.bodyParts.get(PlayerPartsNames.head);
		if(relativeposition != null && relativeposition != Vector3D.ZERO_VECTOR)
			player.setHead(relativeposition);
		//set position of player. Main point is head position
		
		if(relativeposition != null)
		{
			player.setRelativePosition(relativeposition, timeSeen);
			Vector3D absolutePosition = agentModel.globalize(relativeposition);
			player.setPosition(absolutePosition, timeSeen);	
		}
//		double angle = player.getAbsoluteRotation();
	//	System.out.println(angle);
			
		isInRange(player);	
	}
	
	//added by Androids
	/**
	 * Calculates ball's position from specified parsed data.
	 *
	 * @param data
	 */
	public void calculateBallPosition(ParsedData data){
		returnBallRelativePosition(data.ballRelativePosition, data.SIMULATION_TIME);
		Log.log(LogType.WORLD_MODEL, "Player IDs are not implemented yet!!!");
	}
	
	//added by Androids
	/**
	 * Returns ball's relative position, relative to agents position. 
	 *
	 * @param ballRelativePosition
	 * @param simulationTime
	 */
	public void returnBallRelativePosition(Vector3D ballRelativePosition, double simulationTime) {
		ball.setRelativePosition(ballRelativePosition, simulationTime);
		Vector3D absolutePosition = agentModel.globalize(ballRelativePosition);
		ball.setPosition(absolutePosition, simulationTime);
	}

	/**
	 * Returns ball as dynamic object. 
	 *
	 * @return
	 */
	public DynamicObject getBall(){
		return ball;
	}
	
	/**
	 * Sets AgentModel to compute with. 
	 *
	 * @param agent
	 */
	public void setAgentModel(AgentModel agent){
		this.agentModel = agent;
	}
	
	public AgentModel getAgentModel() {
		return agentModel;
	}
	
	//added by Androids
	/**
	 * Returns list of team mates - players with same team as agent's team is. 
	 *
	 * @return
	 */
	public List<Player> getTeamPlayers() {
		return new ArrayList<Player>(teamPlayers.values());
	}
	/**
	 * Returns list of oppontent players - players with same team as agent's team is. 
	 *
	 * @return
	 */
	//added by Androids
	public ArrayList<Player> getOpponentPlayers() {
		return new ArrayList<Player>(opponentPlayers.values());
	}
	
	//added by team17
	//vracia predikciu buducej polohy lopty vypocitanu na zaklade anotacie k pohybu
	/**
	 * Returns ball as dynamic object with new values
	 * based on specified Annotation execution saying how object changes after 
	 * Annotation execution. This is a part of prediction
	 * as these values are future values of object.  
	 *
	 * @param annotation
	 * @return
	 */
	public DynamicObject ballAfterAction(Annotation annotation) {
		DynamicObject newBall = ball.clone();
		double avgElapsed = annotation.getDuration().getAvg();
		//predpoklada sa ze loptu hrac videl v okamihu ukonceni pohybu
		//ak tomu tak nie je, treba tuto informaciu pridat do anotacii
		double ballTime = EnvironmentModel.SIMULATION_TIME + avgElapsed;
		//kedze pozicia lopty v anotacii je dana z pohladu hraca, treba vektor jej zmeny otocit
		//o rotaciu (orientaciu) hraca
		Axis ballMoveAxis = annotation.getBallMove();
		Vector3D avgBallMove = Vector3D.cartesian(ballMoveAxis.getX().getAvg(), ballMoveAxis.getY().getAvg(), ballMoveAxis.getZ().getAvg());
		avgBallMove = avgBallMove.rotateOverZ(agentModel.getRotationZ());
		Vector3D absBallPosition = ball.getPosition().add(avgBallMove);
		newBall.setPosition(absBallPosition, ballTime);
		//relativna pozicia lopty musi byt urcena vzhladom k buducej polohe a rotacii hraca
		AgentModel futureAgent = agentModel.afterAction(annotation);
		newBall.setRelativePosition(absBallPosition.subtract(futureAgent.position).rotateOverZ(
				-futureAgent.getRotationZ()), ballTime);
		return newBall;
	}
	
	
	//added by team17
	/** 
	 * Checks if agent can kick to goal and sets this value for specified player.
	 * 
	 */
	public void isInRange(Player player){
		Vector3D absolutePosition = player.getPosition();
		double goalRange;
		if(player.isTeammate())
			goalRange = AgentInfo.getInstance().getOurGoalRange();
		else
			goalRange = AgentInfo.getInstance().getEnemyGoalRange();
		
		if((player.isTeammate() && AgentInfo.side==Side.LEFT) || 
			(!player.isTeammate() && AgentInfo.side==Side.RIGHT)){
			if ((Math.abs(absolutePosition.getY()) < goalRange) &&
				((AgentInfo.HALF_FIELD_WIDTH - absolutePosition.getX())< goalRange)){
				if(player.getIsInRange() == false){
					/*if(player.isTeammate())
						AgentInfo.logState("TeammatePlayer " + player.getNumber() + "isInRange: true");
					else
						AgentInfo.logState("OponentPlayer " + player.getNumber() + "isInRange: true");*/
					player.setIsInRange(true);
				}
			} else {
				if(player.getIsInRange()){
					/*if(player.isTeammate())
						AgentInfo.logState("TeammatePlayer " + player.getNumber() + "isInRange: false");
					else
						AgentInfo.logState("OponentPlayer " + player.getNumber() + "isInRange: false");*/
					player.setIsInRange(false);
				}
			}
		} else {	
			if ((Math.abs(absolutePosition.getY()) < goalRange) &&
				((AgentInfo.HALF_FIELD_WIDTH + absolutePosition.getX()) < goalRange )){
				if(player.getIsInRange() == false){
					/*if(player.isTeammate())
						AgentInfo.logState("TeammatePlayer " + player.getNumber() + "isInRange: true");
					else
						AgentInfo.logState("OponentPlayer " + player.getNumber() + "isInRange: true");*/
					player.setIsInRange(true);
				}
			} else {
				if(player.getIsInRange()){
					/*if(player.isTeammate())
						AgentInfo.logState("TeammatePlayer " + player.getNumber() + "isInRange: false");
					else
						AgentInfo.logState("OponentPlayer " + player.getNumber() + "isInRange: false");*/
					player.setIsInRange(false);
				}
			}
		}
	}
}