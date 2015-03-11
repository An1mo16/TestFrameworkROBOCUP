package sk.fiit.jim.agent;

import sk.fiit.jim.agent.models.AgentModel;
import sk.fiit.jim.agent.models.EnvironmentModel;
import sk.fiit.jim.agent.models.WorldModel;
import sk.fiit.jim.agent.models.Player;
import sk.fiit.robocup.library.geometry.Vector3D;
import java.io.IOException;  
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.FileHandler;  
import java.util.logging.Logger;  
import java.util.logging.SimpleFormatter; 
import sk.fiit.jim.agent.models.EnvironmentModel.Version;


/**
 *  AgentInfo.java
 *  
 *  Class contains information about the agent. This includes meta-information such as
 *  team and side that the agent is playing on, and various higher-level states and contants.
 *  Lower level information about the agent is contained in the class AgentModel
 *
 *@Title	Jim
 *@author	marosurbanec
 *@author	Androids
 */
public class AgentInfo{
	private enum playerState {STANDING, LYING};
	private String state = "";//vykonavany pohyb

	private String whereIsBall = "";
	private String whereIsGoal = "";
	private boolean isBallMine = false;
	private boolean nearBall = false;
	private boolean isInRange = false;
	private boolean isUnderCover = false;
	private double ourGoalRange = OUR_GOAL_RANGE;
	private double enemyGoalRange = ENEMY_GOAL_RANGE;
	private static AgentInfo instance = new AgentInfo();
	private HashMap<Integer, playerState> opponentsStates = new HashMap<Integer, playerState>(); 
	private HashMap<Integer, playerState> teammatesStates = new HashMap<Integer, playerState>(); 
	
	/**
	 * Team of the agent
	 */
	public static String team;
	/**
	 * Agent's ID - should be unique in team
	 */
	public static int playerId;
	/**
	 * Side where agent has his own net
	 */
	//Todo #Bug(Agent's goal is allways left, also during second period) #Solver() #Priority() | xmarkech 2013-12-10T20:38:13.4560000Z
	public static Side side = Side.LEFT;
	/**
	 * Whether the player's side was already assigned by the server
	 */
	public static boolean hasAssignedSide = false;
	/**
	 * Distance used in ball control calculation.
	 * If x and y distance between agent and ball is less
	 * than this value, agent thinks he controls the ball.
	 */
	public static final double BALL_IN_CONTROL = 0.4;
	/**
	 * Distance used in is ball near agent calculation.
	 * If x and y distance between agent and ball is less
	 * than this value, agent thinks he is near ball.
	 */
	public static final double NEAR_BALL = 2;
	/**
	 * Distance used in under cover calculation.
	 * If x and y distance between agent and opponent player is less
	 * than this value, agent thinks he is covered.
	 */
	public static final int PLAYER_UNDER_COVER = 1;
	/**
	 * Distance from  net, from where agent is able
	 * to shoot to enemy's net.
	 */
	public static final int OUR_GOAL_RANGE = 4;
	/**
	 * Distance from our net, from where opponent player is able
	 * to shoot to enemy's net.
	 */
	public static final int ENEMY_GOAL_RANGE = 5;
	/**
	 * Half field width.
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
	public static final double HALF_FIELD_WIDTH = (EnvironmentModel.version == Version.VERSION_0_6_7 ? 15 : 10.5);
	/**
	 * 
	 */
	public static final double PASS_SPACE = 0.5;
	/**
	 * 
	 */
	public static final double PASS_ANGLE = 15;
	/**
	 * Minimum value for agents state calculation which is
	 * calculated for every seen player. If value is less 
	 * than this value, seen player is considered as 
	 * it was on the ground. Otherwise is considered as standing.
	 */
	public static final double STANDING_LIMIT = 0.36;
	/**
	 * 
	 */
	public static final double CONTROL_DISTANCE = 0.2;
	
	/**
	 * Returns instance of agentInfo we can set attributes of. 
	 *
	 * @return
	 */
	public static AgentInfo getInstance(){
		return instance;
	}
	
	public Vector3D kickTarget(){
		return AgentModel.getInstance().relativize(getGoalAbsolutePosition(side));
	}
	
	/**  
	 * @return how much time player doesn't seen a ball.  
	 */
	public double ballUnseen(){
		return EnvironmentModel.SIMULATION_TIME - WorldModel.getInstance().getBall().getLastTimeSeen();	
	}

	public Vector3D ballControlPosition(){
		Vector3D ball_position = WorldModel.getInstance().getBall().getRelativePosition();
		return ball_position;
	/*	Vector3D ball_position2;
		
	//	if(ball_position){
	//		ball_position2 = ball_position;
	//	}
		Vector3D goal_position = getGoalAbsolutePosition(side);
	//	Vector3D agentPosition = AgentModel.getInstance().getPosition();
		logState("ball_phi: "+ ball_position.getPhi()*180/Math.PI);
	//	logState("goal_position x_y: "+ goal_position.getX()+"_"+goal_position.getY());
	//	logState("agentPosition x_y: "+ agentPosition.getX()+"_"+agentPosition.getY());
		double[] line = new double[2];
		double x1,x2,y1,y2;
		line = getLineFromPoints(ball_position, goal_position);
	//	logState("line k_q: "+ line[0] + "_" + line[1]);
		x1 = ((line[0]*line[1]) + Math.sqrt((line[0]*line[0]*CONTROL_DISTANCE*CONTROL_DISTANCE) - (line[1]*line[1]) + (CONTROL_DISTANCE*CONTROL_DISTANCE))) / ((line[0]*line[0]) + 1 );
		x2 = ((line[0]*line[1]) - Math.sqrt((line[0]*line[0]*CONTROL_DISTANCE*CONTROL_DISTANCE) - (line[1]*line[1]) + (CONTROL_DISTANCE*CONTROL_DISTANCE))) / ((line[0]*line[0]) + 1 );
		y1 = (line[0]*x1) + line[1];
		y2 = (line[0]*x2) + line[1];
		if(this.calculateDistance(Vector3D.cartesian(x1, y1, 0), goal_position) > this.calculateDistance(Vector3D.cartesian(x2, y2, 0), goal_position)){
	//		logState("ballControlPosition x1_y1: "+ x1 + "_" + y1);
		//	goal = AgentModel.getInstance().relativize(goal);
			
			return AgentModel.getInstance().relativize(Vector3D.cartesian(x1, y1, 0));
		} else {
	//		logState("ballControlPosition x2_y2: "+ x2 + "_" + y2);
			return AgentModel.getInstance().relativize(Vector3D.cartesian(x2, y2, 0));
		}*/
	}
	
	/**
	 * 
	 * calculate line for two points 
	 *
	 * @param first_point
	 * @param second_point
	 * @return
	 * 
	 * @deprecated  use robocup.library.geometry.Line2D
	 */
	@Deprecated
	protected double[] getLineFromPoints(Vector3D first_point, Vector3D second_point){
		double[] line = new double[2]; //v poradi k,q 	tvar y=kx+q
		if(first_point.getX() - second_point.getX() != 0){
			line[0] = ((second_point.getY() - first_point.getY()) / (first_point.getX() - second_point.getX())); //vypocet rovnice priamky
		} else {
			line[0] = 0;
		}
		line[1] = (-1) * (first_point.getY() + (line[0] * first_point.getX()));
		line[0] = (-1) * line[0];
		return line;
	}
	
	/**
	 * 
	 * return position of goal 
	 *
	 * @param side
	 * @return goal absolute position
	 */
	protected Vector3D getGoalAbsolutePosition(Side side){
		if(side == Side.LEFT){
			return Vector3D.cartesian(HALF_FIELD_WIDTH, 0, 0);
		} else {
			return Vector3D.cartesian(-HALF_FIELD_WIDTH, 0, 0);
		}
	}
	
	//added by xpassakp
	/** check if agent is near to ball
	 * 
	 */
	public boolean isBallMine(){
		double ballDistance= WorldModel.getInstance().getBall().getRelativePosition().getY();
	//	logState("lopta: "+ ballDistance);
		if (ballDistance < BALL_IN_CONTROL){
				if(this.getIsBallMine()!= true){
	//				logState("isBallMine = true, lopta: "+ ballDistance);
					setIsBallMine(true);
				}
		} else {
			if(this.getIsBallMine()== true){
	//			logState("isBallMine = false, lopta: "+ ballDistance);
			setIsBallMine(false);
			}
		}
		return getIsBallMine();
	}
	
	//added by xpassakp
		/** check if agent is near to ball
		 * 
		 */
		public boolean nearBall(){
			double ballDistance= WorldModel.getInstance().getBall().getRelativePosition().getR();
			if (ballDistance < NEAR_BALL){
					if(this.getNearBall()!= true){
					//	logState("nearBall = true, lopta x_y: "+ ballPosition.getX()+"_"+ballPosition.getY()
					//			+" hrac x_y: " + agentPosition.getX()+ "_" + agentPosition.getY());
						setNearBall(true);
					}
			} else {
				if(this.getNearBall()== true){
				///	logState("nearBall = false, lopta x_y: "+ ballPosition.getX()+"_"+ballPosition.getY()
				//			+" hrac x_y: " + agentPosition.getX()+ "_" + agentPosition.getY());
				setNearBall(false);
				}
			}
			return getNearBall();
		}
	//added by xpassakp
	/** check relative orientation of goal to agent
	 * 
	 */
	public void whereIsGoal(){
		Vector3D goal = this.getGoalAbsolutePosition(side);
		goal = AgentModel.getInstance().relativize(goal);
		setWhereIsGoal(whereIsTarget(goal));
		/*
		double y=goal.getY();
		double x=goal.getX();
		if(Math.abs(x)<Math.abs(y)){
			if(y>0){
				setWhereIsGoal("front");
			} else {
				setWhereIsGoal("back");
			}
		} else {
			if(x>0){
				setWhereIsGoal("right");
			} else {
				setWhereIsGoal("left");
			}
		}	*/	
	}
	
//-------------------------------------------------------------------------------------------
// Zaciatok pridan�ho kodu zo suboru tim 17
	public int lastBallPositiontoAgent(){
		Vector3D ballPosition = WorldModel.getInstance().getBall().getRelativePosition();
	//	setWhereIsBall(whereIsTarget(ballPosition));
		double x=ballPosition.getX();
		
			if(x>0){
				return 1;//right
			} else {
				return 0;//left
			}
			
	}	
// Koniec pridan�ho kodu zo suboru tim 17
//-------------------------------------------------------------------------------------------
	//added by xpassakp
		/** check relative orientation of goal to agent
		 * 
		 */
		public String whereIsTarget(Vector3D target){
			target = AgentModel.getInstance().relativize(target);
			
			double y=target.getY();
			double x=target.getX();
			if(Math.abs(x)<Math.abs(y)){
				if(y>0){
					return "front";
				} else {
					return "back";
				}
			} else {
				if(x>0){
					return "right";
				} else {
					return "left";
				}
			}		
		}
	//added by xpassakp
	/** check relative orientation of ball to agent
	 * 
	 */
	public void whereIsBall(){
		Vector3D ballPosition = WorldModel.getInstance().getBall().getRelativePosition();
	//	setWhereIsBall(whereIsTarget(ballPosition));
		double y=ballPosition.getY();
		double x=ballPosition.getX();
		if(Math.abs(x)<Math.abs(y)){
			if(y>0){
				setWhereIsBall("front");
			} else {
				setWhereIsBall("back");
			}
		} else {
			if(x>0){
				setWhereIsBall("right");
			} else {
				setWhereIsBall("left");
			}
		}
	//	logState("ballRelPosision = " + ballPosition.getX() + "_"+ ballPosition.getY()+"_"+ballPosition.getZ());
		//+" hrac x_y: " + agentPosision.getX()+ "_" + agentPosision.getY());
	}
	//added by xpassakp
	/** check if agent can kick to goal
	 * 
	 */
	public void isInRange(){
		Vector3D agentPosition = AgentModel.getInstance().getPosition();
		double goalRange = this.ourGoalRange;
		if(side==Side.LEFT){
			if (Math.abs(agentPosition.getY()) < goalRange &&
				(HALF_FIELD_WIDTH - agentPosition.getX())< goalRange){
				setIsInRange(true);
			} else {
				setIsInRange(false);
			}
		} else {
			if (Math.abs(agentPosition.getY()) < goalRange &&
				(HALF_FIELD_WIDTH + agentPosition.getX())< goalRange){
				setIsInRange(true);
			} else {
				setIsInRange(false);
			}
		}
	}
	//added by xpassakp
	/** check if enemy players are near to agent
	 * 
	 */
	public void isUnderCover(){
		Vector3D agentPosition = AgentModel.getInstance().getPosition();
		ArrayList<Player> opponentPlayers = WorldModel.getInstance().getOpponentPlayers();
		int i = 0;
		for(Player opponentPlayer: opponentPlayers){
			if (Math.abs(opponentPlayer.getPosition().getX() - agentPosition.getX())< PLAYER_UNDER_COVER &&
				Math.abs(opponentPlayer.getPosition().getY() - agentPosition.getY())< PLAYER_UNDER_COVER){
				i++;
			}
		}
		if(i!=0){
			if(this.getIsUnderCover()!= true){
		//		logState("isUnderCover = true");
				setIsUnderCover(true);
			}
		} else {
			if(this.getIsUnderCover()== true){
		//		logState("isUnderCover = false");
				setIsUnderCover(false);
			}
		}
	}
	
	//added by xpassakp
	/** check if is free way for pass
	 * 
	 */
	public boolean isWayFree(/*Vector3D startPosition, Vector3D stopPosition*/){
		ArrayList<Player> opponentPlayers = WorldModel.getInstance().getOpponentPlayers();
		
		Vector3D startPosition = Vector3D.cartesian(2, 1, 0);//AgentModel.getInstance().getPosition();
		Vector3D stopPosition = Vector3D.cartesian(-4, 1.1, 0);
		
		boolean half_plane1, half_plane2;
		double a, c, kp, kq1, kq2, a1, a2, c1, c2, distance;
		
		distance = calculateDistance(startPosition, stopPosition);
		
		if(startPosition.getX() - stopPosition.getX() != 0){
			a = ((stopPosition.getY() - startPosition.getY()) / (startPosition.getX() - stopPosition.getX())); //vypocet rovnice priamky pre prihravku
		} else {
			a = 0;
		}

		c = (-1) * (startPosition.getY() + (a * startPosition.getX()));
		
		kp = -a; //zo vzorca kp = -1*(a/b) v nasom pripade b=1
		
		kq1 = (kp + Math.tan(Math.toRadians(PASS_ANGLE))) / (1 - (Math.tan(Math.toRadians(PASS_ANGLE)) * kp));
		kq2 = (kp - Math.tan(Math.toRadians(PASS_ANGLE))) / (1 + (Math.tan(Math.toRadians(PASS_ANGLE)) * kp));
		
		a1 = -kq1;
		c1 = (kq1 * startPosition.getX()) - startPosition.getY();
		
		a2 = -kq2;
		c2 = (kq2 * startPosition.getX()) - startPosition.getY();
		
		half_plane1 = isInHalfPlane(a1, c1, stopPosition);
		half_plane2 = isInHalfPlane(a2, c2, stopPosition);
		
		for(Player opponentPlayer: opponentPlayers){
			if(!opponentPlayer.isTeammate()){
				Vector3D opponentPlayerPosition = opponentPlayer.getPosition();
			//	logState("opo_half_plane1 = " + isInHalfPlane(a1,c1,opponentPlayerPosition));
			//	logState("opo_half_plane2 = " + isInHalfPlane(a2,c2,opponentPlayerPosition));
				if	((isInHalfPlane(a1,c1,opponentPlayerPosition) == half_plane1) &&
					(isInHalfPlane(a2,c2,opponentPlayerPosition) == half_plane2) &&
					(calculateDistance(startPosition, opponentPlayerPosition) <= distance + (distance * PASS_SPACE))){
					logState("opponentPosition = [" + opponentPlayerPosition.getX() + "," + opponentPlayerPosition.getY() + "]");
					logState("isWayFree = false");
					return false;
				}
				
			}
		}
		/*for(Player opponentPlayer: opponentPlayers){
			if(!opponentPlayer.isTeammate()){
				Vector3D opponentPlayerPosition = opponentPlayer.getPosition();
				v = (Math.abs((a * opponentPlayerPosition.getX()) - opponentPlayerPosition.getY() + c ))/(Math.sqrt(a*a+1)); //vypocet vzdialenosti hracov od priamky prihravky
				distance_to_player = calculateDistance(startPosition, opponentPlayerPosition);
				position = Math.sqrt(distance_to_player*distance_to_player + v*v);
				if ((position < distance) && (v < position * PASS_SPACE)){
					return false;
				} else {
					if((position < distance + distance * PASS_SPACE) && (v < (distance + distance * PASS_SPACE) - position)){
						return false;
					}
				}
					
			}
		}*/
		return true;
	}
	
	//added by xpassakp
	/** 
	 * Checks if specified point falls into half plane(bod do polroviny). 
	 * True if it falls into half plane, false otherwise.
	 */
	public boolean isInHalfPlane(double a, double c, Vector3D position){
		
		if((a * position.getX()) + position.getY() + c >= 0){
			return true;
		} else {
			return false;
		}
	}
	
	//added by xpassakp
	/** 
	 * Calculates distance between start position and stop position.
	 */
	public double calculateDistance(Vector3D startPosition, Vector3D stopPosition){
		double x,y,distance;
		
		x = startPosition.getX() - stopPosition.getX();
		y = startPosition.getY() - stopPosition.getY();
		distance = Math.sqrt(x*x + y*y);
		return distance;
	}
	
	//added by xpassakp
	/** 
	 * Checks if specified opponent is in the way of the agent. 
	 */
	public boolean isForward(Vector3D startPosition, Vector3D stopPosition, Vector3D opponentPlayerPosition){
		double x1,x2,y1,y2;
		x1 = startPosition.getX() - stopPosition.getX();
		y1 = startPosition.getY() - stopPosition.getY();
		x2 = startPosition.getX() - opponentPlayerPosition.getX();
		y2 = startPosition.getY() - opponentPlayerPosition.getY();
		if(((x1 >= 0)&&(x2 >= 0) || (x1 <= 0)&&(x2 <= 0)) && ((y1 >= 0)&&(y2 >= 0) || (y1 <= 0)&&(y2 <= 0))){
			return true;
		} else {
			return false;
		}
	}
	
	//added by xpassakp
	/**
	 * @param state to set
	 */
	public void setState(String state){
		if(!this.state.equals(state)){
			this.state = state;
		//	logState(state);
		}
	}
	
	//added by xpassakp
	/**
	 * returns the state of agent
	 * @return state
	 */
	public String getState(){
		return this.state;
	}
	
	//added by xpassakp
	/**
	 * @param whereIsBall to set
	 */
	public void setWhereIsBall(String whereIsBall) {
		if(!this.whereIsBall.equals(whereIsBall)){
			this.whereIsBall = whereIsBall;
	//		logState("whereIsBall = " + whereIsBall);
		}
	}
	
	//added by xpassakp
	/**
	 * returns where is ball value
	 * @return whereIsBall
	 */
	public String getWhereIsBall(){
		return this.whereIsBall;
	}
	
	//added by xpassakp
	/**
	 * @param whereIsGoal to set
	 */
	public void setWhereIsGoal(String whereIsGoal) {
		if( !this.whereIsGoal.equals(whereIsGoal) ){
			this.whereIsGoal = whereIsGoal;
	//		logState("whereIsGoal = " + whereIsGoal);
		}
	}
	
	//added by xpassakp
	/**
	 * @return whereIsGoal
	 */
	public String getWhereIsGoal(){
		return this.whereIsGoal;
	}
	
	//added by xpassakp
	/**
	 * @param isBallMine to set
	 */
	public void setIsBallMine(boolean isBallMine) {
		if( this.isBallMine != isBallMine ){
			this.isBallMine = isBallMine;
		}
	}
	
	//added by xpassakp
	/**
	 * @return isBallMine
	 */
	public boolean getIsBallMine(){
		return this.isBallMine;
	}
	
	//added by xpassakp
	/**
	 * @param isBallMine to set
	 */
	public void setNearBall(boolean nearBall) {
		if( this.nearBall != nearBall ){
			this.nearBall = nearBall;
		}
	}
	
	//added by xpassakp
	/**
	 * @return isBallMine
	 */
	public boolean getNearBall(){
		return this.nearBall;
	}
	
	//added by xpassakp
	/**
	 * @param isInRange to set
	 */
	public void setIsInRange(boolean isInRange) {
		if( this.isInRange != isInRange ){
			this.isInRange = isInRange;
	//		logState("isInRange = " + isInRange);
		}
	}
	
	//added by xpassakp
	/**
	 * @return isInRange
	 */
	public boolean getIsInRange(){
		return this.isInRange;
	}
	
	//added by xpassakp
	/**
	 * @param isUnderCover to set
	 */
	public void setIsUnderCover(boolean isUnderCover) {
		this.isUnderCover = isUnderCover;
	}
	
	//added by xpassakp
	/**
	 * @return isUnderCover
	 */
	public boolean getIsUnderCover(){
		return this.isUnderCover;
	}
	
	//added by xpassakp
	/**
	 * @param ourGoalRange to set
	 */
	public void setOurGoalRange(double ourGoalRange) {
		this.ourGoalRange = ourGoalRange;
	}
	
	//added by xpassakp
	/**
	 * @return ourGoalRange
	 */
	public double getOurGoalRange(){
		return this.ourGoalRange;
	}
	
	//added by xpassakp
	/**
	 * @param enemyGoalRange to set
	 */
	public void setEnemyGoalRange(double enemyGoalRange) {
		this.enemyGoalRange = enemyGoalRange;
	}
	
	//added by xpassakp
	/**
	 * @return enemyGoalRange
	 */
	public double getEnemyGoalRange(){
		return this.enemyGoalRange;
	}
	
	/**
	 * Logs using specified String 
	 *
	 * @param state
	 */
	public void loguj(String state){
		logState(state);
	}
	/**
	 * Logs using specified String if this doesn't equal agentInfo state
	 *
	 * @param state
	 */
	public void logujStav(String state){
		if( !this.getState().equals(state)){
			logState(state);
			this.setState(state);
		}	
	}
	
	//added by xpassakp
	/** log function
	 * @param string to log to LogForState.log
	 */
	public static void logState(String state){
		Logger logger = Logger.getLogger("StateLog");  
        FileHandler fh;  
        try {  
            // This block configure the logger with handler and formatter  
            fh = new FileHandler("./LogForState.log", true);  
            logger.addHandler(fh);    
            SimpleFormatter formatter = new SimpleFormatter();  
            fh.setFormatter(formatter);
            logger.info(state); 
            fh.close();
              
        } catch (SecurityException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }
	}
	/**
	 * Returns the HashMap containing opponent players states 
	 * each by the number of player(key value).
	 *
	 * @return
	 */
	public HashMap<Integer, playerState> getOpponentsStates() {
		return opponentsStates;
	}
	/**
	 * Returns the HashMap containing team mates states 
	 * each by the number of player(key value).
	 *
	 * @return
	 */
	public HashMap<Integer, playerState> getTeammatesStates() {
		return teammatesStates;
	}
	//added by xmachoj1
		/**
		 * Checks state of opponents that this agent can see. Current values are standing or lying.
		 */
	public void getOpponentsState(){
		ArrayList<Player> opponents = WorldModel.getInstance().getOpponentPlayers();
		if(opponents==null)
			return;
		//if no opponent is seen then return
		if(opponents.size()<1){
			return;
		}
		for(Player p: opponents){
			if(p.isTeammate())
				continue;
			getPlayerState(p, true);
		}
	}
	/**
	 * Checks state of team mates that agent can see. Current values are standing or lying.
	 */
	public void getTeammatesState(){
		List<Player> teammates = WorldModel.getInstance().getTeamPlayers();
		if(teammates==null)
			return;
		//if no team mate is seen then return
		if(teammates.size()<1){
			return;
		}
		for(Player p: teammates){
			if(p.getNumber()==playerId)
				continue;
			if(!p.isTeammate())
				continue;
			getPlayerState(p, false);
		}
	}
	/**
	 * Method calculating specified player�s state for specified team. If Z-axis value of seen player is
	 * less than STANDING_LIMIT, player's state is set to lying. Otherwise 
	 * it is set to standing. 
	 *
	 * @param player
	 * @param isOpponent
	 * @return
	 */
	public playerState getPlayerState(Player player, boolean isOpponent){
		if(player!=null){ 
			if((player.getHead().getZ()-player.getLfoot().getZ())>STANDING_LIMIT||(player.getHead().getZ()-player.getRfoot().getZ())>STANDING_LIMIT){
				if(isOpponent){
					if(!opponentsStates.containsKey(player.getNumber())){
						opponentsStates.put(player.getNumber(), playerState.STANDING);
					}
					else{
						if(!opponentsStates.get(player.getNumber()).equals(playerState.STANDING)){
							opponentsStates.remove((player.getNumber()));
							opponentsStates.put(player.getNumber(), playerState.STANDING);
						}
					}
				}else{
					if(!teammatesStates.containsKey(player.getNumber())){
						teammatesStates.put(player.getNumber(), playerState.STANDING);
					}
					else{
						if(!teammatesStates.get(player.getNumber()).equals(playerState.STANDING)){
							teammatesStates.remove((player.getNumber()));
							teammatesStates.put(player.getNumber(), playerState.STANDING);
						}
					}
				}
				return playerState.STANDING;
			}else{
				if(isOpponent){
					if(!opponentsStates.containsKey(player.getNumber())){
						opponentsStates.put(player.getNumber(), playerState.LYING);
					}
					else{
						if(!opponentsStates.get(player.getNumber()).equals(playerState.LYING)){
							opponentsStates.remove((player.getNumber()));
							opponentsStates.put(player.getNumber(), playerState.LYING);
						}
					}
				}else{
					if(!teammatesStates.containsKey(player.getNumber())){
						teammatesStates.put(player.getNumber(), playerState.LYING);
					}
					else{
						if(!teammatesStates.get(player.getNumber()).equals(playerState.LYING)){
							teammatesStates.remove((player.getNumber()));
							teammatesStates.put(player.getNumber(), playerState.LYING);
						}
					}
				}
				return playerState.LYING;
			}
		}else
			return null;
	}
	
	/**
	 * Outputs actual received data from server of current agent. 
	 *
	 */
	public void myState(){
		AgentModel me = AgentModel.getInstance();
		System.out.println(me.getLastDataReceived());
	}

//-----------Added  3.12.2013 by Igorko.Homola@gmail.com team 4 (team project RFC MEGATROLL 2013)----
// Return octan where is situated ball (1L,1R,2L,2R,3L,3R,4L,4R)
	public String octanBallPosition (){
		WorldModel worldModel = WorldModel.getInstance();
		String octan = null ;
			if ((worldModel.getBall().getPosition().getX() < -7.5) && (worldModel.getBall().getPosition().getY() >= 0)){
				octan = "1L";
			}
			else if ((worldModel.getBall().getPosition().getX() < -7.5) && (worldModel.getBall().getPosition().getY() < 0)){
				octan = "1R";
			}
			else if ((worldModel.getBall().getPosition().getX() <= 0) && (worldModel.getBall().getPosition().getX() >= -7.5) && (worldModel.getBall().getPosition().getY() >= 0)){
				octan = "2L";
			}
			else if ((worldModel.getBall().getPosition().getX() <= 0) && (worldModel.getBall().getPosition().getX() >= -7.5) && (worldModel.getBall().getPosition().getY() < 0)){
				octan = "2R";
			}
			else if ((worldModel.getBall().getPosition().getX() > 0) && (worldModel.getBall().getPosition().getX() <= 7.5) && (worldModel.getBall().getPosition().getY() >= 0)){
				octan = "3L";
			}
			else if ((worldModel.getBall().getPosition().getX() > 0) && (worldModel.getBall().getPosition().getX() <= 7.5) && (worldModel.getBall().getPosition().getY() < 0)){
				octan = "3R";
			}
			else if ((worldModel.getBall().getPosition().getX() > 7.5) && (worldModel.getBall().getPosition().getY() >= 0)){
				octan = "4L";
			}
			else if ((worldModel.getBall().getPosition().getX() > 7.5) && (worldModel.getBall().getPosition().getY() < 0)){
				octan = "4R";
			}
	return octan;
	}
}


