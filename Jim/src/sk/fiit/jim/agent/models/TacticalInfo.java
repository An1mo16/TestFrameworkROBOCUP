/**
 * 
 */
package sk.fiit.jim.agent.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.jim.agent.Side;
import sk.fiit.jim.agent.models.prediction.Prophet;
import sk.fiit.robocup.library.geometry.Line2D;
import sk.fiit.robocup.library.geometry.Vector3;
import sk.fiit.robocup.library.geometry.Vector3D;


/**
 * Obsahuje informacie o hernzch situaciach - utocime, branime a podobne a taktiez metody
 * pre vytvorenie herej formacie
 * 
 *  TacticalInfo.java
 *  
 *@Title        Jim
 *@author       $Author: Filip $
 */
public class TacticalInfo {
	
	private boolean isOffensiveSituation = false;
	private boolean isDefSituation = false;
	private boolean isBallOurs = false;
	private boolean isBallTheir = false;
	private boolean isStartAttackSituation = false;
	private boolean isEnemyStartAttack = false;
	
	private Vector3D[] PositionInFormation;
	private Vector3D pos;
	private Vector3D relpos;
	
	private static TacticalInfo instance = new TacticalInfo();
	
	public static TacticalInfo getInstance(){
		return instance;
	}

	/**
	 * @author Tomas Nemecek
	 * @author gitmen
	 * Method chooses who is the nearest player to the ball
	 * @return Player - Returns the nearest player
	 */
	private Player whoIsNearestToBall(List<Player> players){
		double min = AgentInfo.HALF_FIELD_WIDTH*1000; //some relative big number
		
		Player minPlayer = null;
		
		//get minimal distance between ball and some other player
		for(Player p:players){
			if (p.getDistanceFromBall()<min){
				min = p.getDistanceFromBall();
				minPlayer = p;
			}
		}
		
		return minPlayer;
	}
	
	/**
	 * @author Tomas Nemecek
	 * @author gitmen
	 * Method chooses if agent is the nearest player(from all players) to the ball
	 * @return boolean - if I am the nearest player returns true else returns false
	 */
	public boolean isBallNearestToMe() {
		List<Player> players = WorldModel.getInstance().getTeamPlayers();
		players.addAll(WorldModel.getInstance().getOpponentPlayers());
		Player minPlayer = whoIsNearestToBall(players);

		// choose who is nearer to the ball
		boolean amInearest = false;

		if (minPlayer != null) {
			Vector3D ballPosition = WorldModel.getInstance().getBall().getPosition();
			Vector3D agentPosition = AgentModel.getInstance().getPosition();
			double distance = DistanceHelper.computeDistanceBetweenObjects(ballPosition, agentPosition);
			return distance < minPlayer.getDistanceFromBall();
		} else {
			return true;
		}
	}
	
	/**
	 * @author Tomas Nemecek
	 * @author gitmen
	 * Method chooses if agent is the nearest player(from all teammates) to the ball
	 * @return boolean - if I am the nearest from our team returns true else returns false
	 */
	public boolean isBallNearestToMeInMyTeam() {
		List<Player> players = WorldModel.getInstance().getTeamPlayers();
		Player minPlayer = whoIsNearestToBall(players);

		// choose who is nearer to the ball
		boolean amInearest = false;

		if (minPlayer != null) {
			Vector3D ballPosition = WorldModel.getInstance().getBall().getPosition();
			Vector3D agentPosition = AgentModel.getInstance().getPosition();
			double distance = DistanceHelper.computeDistanceBetweenObjects(ballPosition, agentPosition);
			return distance < minPlayer.getDistanceFromBall();
		} else {
			return true;
		}
	}
	
	/**
	 * @author Tomas Nemecek
	 * @author gitmen
	 * Method chooses if our team has the nearest player to the ball
	 * @return boolean - Returns true only if someone from our team is the nearest to the ball
	 */
	public boolean isBallOurs() {
		List<Player> teammates = WorldModel.getInstance().getTeamPlayers();
		Player ourMinPlayer = whoIsNearestToBall(teammates);

		List<Player> opponents = WorldModel.getInstance().getOpponentPlayers();
		Player theirMinPlayer = whoIsNearestToBall(opponents);

		return ourMinPlayer.getDistanceFromBall() < theirMinPlayer.getDistanceFromBall();
	}
	
	/**
	 * @author Tomas Nemecek
	 * @author gitmen
	 * Method chooses if our team has the nearest player to the ball
	 * @return boolean - Returns true only if someone from opposite team is the nearest to the ball
	 */
	public boolean isBallTheir(){
		List<Player> teammates = WorldModel.getInstance().getTeamPlayers();
		Player ourMinPlayer = whoIsNearestToBall(teammates);

		List<Player> opponents = WorldModel.getInstance().getOpponentPlayers();
		Player theirMinPlayer = whoIsNearestToBall(opponents);

		return ourMinPlayer.getDistanceFromBall() > theirMinPlayer.getDistanceFromBall();
	}
	
//--------------------------------------------------------------------------
// Koniec k�du ktor� t�m 17 nem� v kode	
//-------------------------------------------------------------------------------	
	
	public void getPlaySituation(){
		if(!getIsOffensSituation())
			isStartAttackSituation();
		isOffensiveSituation();
		if(!getIsDefSituation());
			isEnemyStartAttack();
		isDefensiveSituation();
		
	}
	
	/*
	 * tato metoda zistuje, ci nas tim zaklada utok
	 * rozlisuje sa, ci zakladame utok pod tlakom alebo nie
	 * ak tim nema loptu v drzani zisti sa pomocou predikcie, ci ju do urciteho casu mat budu
	 */
	private void isStartAttackSituation(){
		
		List<Player> teamPlayers = WorldModel.getInstance().getTeamPlayers();
		String underPressure = "";
		
		for(Player teamPlayer: teamPlayers){
			isBallOurs(teamPlayer);
			if(isTMateOnOwnHalf(teamPlayer) && getIsBallOurs()){
				setIsStartAttackSituation(true);
				if(isUnderPressure(teamPlayer))
					underPressure = "Under pressure";
				else underPressure = "No pressure";
			}
			else {
				for(Player teamPlayer2: teamPlayers){
					if(Math.abs(teamPlayer2.getPrediction().getX() - WorldModel.getInstance().getBall().getPrediction().getX()) < 1 && 
					Math.abs(teamPlayer2.getPrediction().getY() - WorldModel.getInstance().getBall().getPrediction().getY()) < 1){
							setIsStartAttackSituation(true);
							if(isUnderPressure(teamPlayer2))
								underPressure = "Under pressure - predickia";
							else underPressure = "No pressure - predikcia";
						
					}else setIsStartAttackSituation(false);
				}
			}
		}
	
		if(getStartAttackSituation())
			System.out.print("Zakladame utok - " + underPressure + "\n");
	}
/*
 * podobna metoda ako pri isStartAttackSituation(), tentokrat pre superov tim
 * rovnaky princip ako u predchadzajucej
 */
private void isEnemyStartAttack(){
		
		ArrayList<Player> opponentPlayers = WorldModel.getInstance().getOpponentPlayers();
		String underPressure = "";
		
		for(Player opponentPlayer: opponentPlayers){
			isBallTheir(opponentPlayer);
			if(isEnemyOnOwnHalf(opponentPlayer) && getIsBallTheir()){
				setIsEnemyStartAttack(true);
				if(isUnderPressure2(opponentPlayer))
					underPressure = "Under pressure";
				else underPressure = "No pressure";
			}
			else {
				for(Player opponentPlayer2: opponentPlayers){
					if(Math.abs(opponentPlayer2.getPrediction().getX() - WorldModel.getInstance().getBall().getPrediction().getX()) < 1 && 
					Math.abs(opponentPlayer2.getPrediction().getY() - WorldModel.getInstance().getBall().getPrediction().getY()) < 1){
							setIsEnemyStartAttack(true);
							if(isUnderPressure2(opponentPlayer2))
								underPressure = "Under pressure - predickia";
							else underPressure = "No pressure - predikcia";
						
					}else setIsEnemyStartAttack(false);
				}
			}
		}
	
		if(getEnemyStartAttack())
			System.out.print("Super zaklada utok - " + underPressure + "\n");
	}
	
	private boolean isUnderPressure(Player teamPlayer){
		
		boolean isUnderPressure = false;
		
		ArrayList<Player> opponentPlayers = WorldModel.getInstance().getOpponentPlayers();
		
		for(Player opponentPlayer: opponentPlayers)
			if(Math.abs(opponentPlayer.getPosition().getX() - teamPlayer.getPosition().getX()) < 2 && 
			Math.abs(opponentPlayer.getPosition().getY() - teamPlayer.getPosition().getY()) < 2)
				isUnderPressure = true;
		
		return isUnderPressure;
	}
	
private boolean isUnderPressure2(Player opponentPlayer){
		
		boolean isUnderPressure = false;
		
		List<Player> teamPlayers = WorldModel.getInstance().getTeamPlayers();
		
		for(Player teamPlayer: teamPlayers)
			if(Math.abs(opponentPlayer.getPosition().getX() - teamPlayer.getPosition().getX()) < 2 && 
			Math.abs(opponentPlayer.getPosition().getY() - teamPlayer.getPosition().getY()) < 2)
				isUnderPressure = true;
		
		return isUnderPressure;
	}
	
	private boolean getStartAttackSituation(){
		return (this.isStartAttackSituation);
	}
	
	private void setIsEnemyStartAttack(boolean isEnemyStartAttack){
		if(this.isEnemyStartAttack != isEnemyStartAttack)
			this.isEnemyStartAttack = isEnemyStartAttack;
	}
	
	private boolean getEnemyStartAttack(){
		return (this.isEnemyStartAttack);
	}
	
	private void setIsStartAttackSituation(boolean isStartAttackSituation){
		if(this.isStartAttackSituation != isStartAttackSituation)
			this.isStartAttackSituation = isStartAttackSituation;
	}
	/*
	 * metoda zistuje, ci nas tim utoci a taktiez pomer nasich utocnikov k obrancom
	 */
	private void isOffensiveSituation(){
		
		List<Player> teamPlayers = WorldModel.getInstance().getTeamPlayers();
			
		if(getTMatesAttackers() != 0){
			if(getIsBallOurs()){
				if(this.getIsOffensSituation() == false)
					setIsOffensSituation(true);
			}else {
				for(Player teamPlayer: teamPlayers){
					if(Math.abs(teamPlayer.getPrediction().getX() - WorldModel.getInstance().getBall().getPrediction().getX()) < 1 && 
					Math.abs(teamPlayer.getPrediction().getY() - WorldModel.getInstance().getBall().getPrediction().getY()) < 1){
						if(this.getIsOffensSituation() == false)
							setIsOffensSituation(true);
					}else setIsOffensSituation(false);
				}
			}
		}else setIsOffensSituation(false);
		
		if(this.isOffensiveSituation)
			System.out.print("Utocime: " + getAttackType() + "\n");
		
	}
	
	/*
	 * metoda zistuje, ci nas tim brani a taktiez pomer nasich obrancov k superovym utocnikom
	 */
	private void isDefensiveSituation(){
		
		ArrayList<Player> opponentPlayers = WorldModel.getInstance().getOpponentPlayers();
			
		if(getEnemyAttackers() != 0){
			if(getIsBallTheir()){
				if(this.getIsDefSituation() == false)
					setIsDefSituation(true);
			}else setIsDefSituation(false);
		}else {
			for(Player opponentPlayer: opponentPlayers){
				if(Math.abs(opponentPlayer.getPrediction().getX() - WorldModel.getInstance().getBall().getPrediction().getX()) < 1 && 
				Math.abs(opponentPlayer.getPrediction().getY() - WorldModel.getInstance().getBall().getPrediction().getY()) < 1){
					if(this.getIsOffensSituation() == false)
						setIsOffensSituation(true);
				}else setIsOffensSituation(false);
			}
		}
		if(this.isDefSituation)
			System.out.print("Branime:" + getDefType() + "\n");
	}
	
	private String getAttackType(){
		
		return (getTMatesAttackers()+ " vs " + getEnemyDefenders() + "\n");
	}
	
	private String getDefType(){
		return (getTMatesDefenders() + " vs " + getEnemyAttackers() + "\n");
	}
	
	private boolean getIsOffensSituation(){
		return (this.isOffensiveSituation);
	}
	
	private boolean getIsDefSituation(){
		return (this.isDefSituation);
	}
	
	private void setIsOffensSituation(boolean isOffensiveSituation){
		if(this.isOffensiveSituation != isOffensiveSituation)
			this.isOffensiveSituation = isOffensiveSituation;
	}
	
	private void setIsDefSituation(boolean isDefSituation){
		if(this.isDefSituation != isDefSituation)
			this.isDefSituation = isDefSituation;
	}
	
	private int getEnemyDefenders(){
		
		ArrayList<Player> opponentPlayers = WorldModel.getInstance().getOpponentPlayers();
		
		int defenders = 0;
		
		for(Player opponentPlayer: opponentPlayers)
			if(isEnemyOnOwnHalf(opponentPlayer))
					defenders++;
		
		return defenders;
	}
	
	private boolean isEnemyOnOwnHalf(Player opponentPlayer){
		
		boolean onOwnHalf = false;
		
                // FIXME: co to je za hausnumero ze 12?
		if(opponentPlayer.getPosition().getX() < 12 && opponentPlayer.getPosition().getX() > 0)
			onOwnHalf = true;
				
		return onOwnHalf;
	}
	
	private int getTMatesDefenders(){
		
		ArrayList<Player> opponentPlayers = WorldModel.getInstance().getOpponentPlayers();
		List<Player> teamPlayers = WorldModel.getInstance().getTeamPlayers();
		int defenders = 0;
		
		if(AgentModel.getInstance().getPosition().getX() < 0 && AgentModel.getInstance().getPosition().getX() > -12)
			defenders++;
				
		for(Player teamPlayer: teamPlayers)
			for(Player opponentPlayer: opponentPlayers)
				if(isTMateOnOwnHalf(teamPlayer) && (teamPlayer.getPosition().getX() < opponentPlayer.getPosition().getX())){
					defenders++;
				}
		
		return defenders;
	}
	
        // FIXME: co to je za hausnumero ze 12?
	private boolean isTMateOnOwnHalf(Player teamPlayer){
		
		boolean onOwnHalf = false;
		
		if(teamPlayer.getPosition().getX() < 0 && teamPlayer.getPosition().getX() > -12)
			onOwnHalf = true;
				
		return onOwnHalf;
	}
	
        // FIXME: co to je za hausnumero ze 12?
	private int getTMatesAttackers(){
		
		List<Player> teamPlayers = WorldModel.getInstance().getTeamPlayers();
		int attackers = 0;
		
		if(AgentModel.getInstance().getPosition().getX() < 12 && AgentModel.getInstance().getPosition().getX() > 0)
			attackers++;
		
		if(Math.abs(AgentModel.getInstance().getPosition().getX() - WorldModel.getInstance().getBall().getPosition().getX()) < 1 && 
		Math.abs(AgentModel.getInstance().getPosition().getY() - WorldModel.getInstance().getBall().getPosition().getY()) < 1)
			setIsBallOurs(true);
		
		for(Player teamPlayer: teamPlayers){
			if(teamPlayer.getPosition().getX() < 12 && teamPlayer.getPosition().getX() > 0){
				isBallOurs(teamPlayer);	
				attackers++;
			}
		}
		
		return attackers;
	}
	
        // FIXME: co to je za hausnumero ze 12?
	private int getEnemyAttackers(){
		
		List<Player> opponentPlayers = WorldModel.getInstance().getOpponentPlayers();
		int attackers = 0;
		
		for(Player opponentPlayer: opponentPlayers){
			if(opponentPlayer.getPosition().getX() < 0 && opponentPlayer.getPosition().getX() > -12){
				isBallTheir(opponentPlayer);
				attackers++;
			}
		}
		
		return attackers;
	}
	
	public void isBallOurs(Player teamPlayer){
		
		if((Math.abs(teamPlayer.getPosition().getX() - WorldModel.getInstance().getBall().getPosition().getX()) < 1) && 
		(Math.abs(teamPlayer.getPosition().getX() - WorldModel.getInstance().getBall().getPosition().getX()) < 1)){
			if(this.isBallOurs == false)
				setIsBallOurs(true);
			}else setIsBallOurs(false);
	}
		
	
	public void setIsBallOurs(boolean isBallOurs){
		if(this.isBallOurs != isBallOurs)
			this.isBallOurs = isBallOurs;
		
	}
	
	public boolean getIsBallOurs(){
		return (this.isBallOurs);
	}
	
	public void isBallTheir(Player opponentPlayer){

		if((Math.abs(opponentPlayer.getPosition().getX() - WorldModel.getInstance().getBall().getPosition().getX()) < 1) && 
			(Math.abs(opponentPlayer.getPosition().getY() - WorldModel.getInstance().getBall().getPosition().getY()) < 1)) {
					if(this.isBallTheir == false) {
						setIsBallTheir(true);
					}
				} else {
					setIsBallTheir(false);
				}
	}
	
	public void setIsBallTheir(boolean isBallTheir){
		if(this.isBallTheir != isBallTheir)
			this.isBallTheir = isBallTheir;	
	}
	
	public boolean getIsBallTheir(){
		return (this.isBallTheir);
	}
	
	/**
	 * Finds, if the ball is kept by some player from our team. 
	 * Keeping the ball is defined by HAS_BALL_DISTANCE. It is the number, which represent distance ball from player.
	 * 
	 * @author Cervenak
	 * @author A55-Kickers
	 * 
	 * @return the fact about holding ball by us (true, false)
	 */
	public boolean isBallOwnedByUs() {
		boolean isOur = false;
		int HAS_BALL_DISTANCE = 1;
		List<Player> team = WorldModel.getInstance().getTeamPlayers();
		for (int i = 0; i < team.size(); i++) {
			Player teamPlayer = team.get(i);
			if((Math.abs(teamPlayer.getPosition().getX() - WorldModel.getInstance().getBall().getPosition().getX()) < HAS_BALL_DISTANCE) && 
					(Math.abs(teamPlayer.getPosition().getY() - WorldModel.getInstance().getBall().getPosition().getY()) < HAS_BALL_DISTANCE)) {
				isOur = true;
			}
		}
		return isOur;
	}
	
	/**
	 * Finds, if the ball is kept by some player from opponent team. 
	 * Keeping the ball is defined by HAS_BALL_DISTANCE. It is the number, which represent distance ball from player.
	 * 
	 * @author Cervenak
	 * @author A55-Kickers
	 * 
	 * @return the fact about holding ball by them (true, false)
	 */
	public boolean isBallOwnedByThem() {
		boolean isTheir = false;
		int HAS_BALL_DISTANCE = 1;
		List<Player> opponents = WorldModel.getInstance().getOpponentPlayers();
		for (int i = 0; i < opponents.size(); i++) {
			Player opponentPlayer = opponents.get(i);
			if((Math.abs(opponentPlayer.getPosition().getX() - WorldModel.getInstance().getBall().getPosition().getX()) < HAS_BALL_DISTANCE) && 
					(Math.abs(opponentPlayer.getPosition().getY() - WorldModel.getInstance().getBall().getPosition().getY()) < HAS_BALL_DISTANCE)) {
				isTheir = true;
			}
		}
		return isTheir;
	}
	
	/**
	 * Compute chance to get ball by our team from closest our player and closest opponent
	 * player. Computation depends on time prediction to get ball by player.
	 * TODO- Implement computation with rotation agent to ball.
	 * 
	 * @author xgregorm
	 * @author A55-Kickers
	 * 
	 * @return chance to get ball by our team in range <0,1>
	 */
	public double chanceToGetBall() {
		double playerSpeed 	= 0;
		double chance 		= 1;
		Vector3D ball_pos	= WorldModel.getInstance().getBall().getPosition();
		
		ArrayList<Player> players 	= WorldModel.getInstance().getOpponentPlayers();
		List<Double> distances 		= new ArrayList<Double>();
		
		for ( Player player: players) {
			double playerDist 	= player.getDistanceFromBall();
			double playerSpeedX = player.getSpeed().getX();
			double playerSpeedY = player.getSpeed().getY();
			playerSpeed  		=  Math.sqrt(Math.pow(playerSpeedX,2)/Math.pow(playerSpeedY,2));
			double time 		= playerDist / playerSpeed;
			
			distances.add(time);
		}
		
		Collections.sort(distances);
		
		List<Player> ourPlayers = WorldModel.getInstance().getTeamPlayers();
		List<Double> ourDistances = new ArrayList<Double>();
		
		for ( Player player: ourPlayers) {
			double playerDist 	= player.getDistanceFromBall();
			double playerSpeedX = player.getSpeed().getX();
			double playerSpeedY = player.getSpeed().getY();
			playerSpeed  		=  Math.sqrt(Math.pow(playerSpeedX,2)/Math.pow(playerSpeedY,2));
			double time 		= playerDist / playerSpeed;
			
			ourDistances.add(time);
		}
		
		Collections.sort(ourDistances);
		
		if (!players.isEmpty() && isBallNearestToMeInMyTeam()) {
			double myDist 	= AgentModel.getInstance().getPosition().getXYDistanceFrom(ball_pos);
			double mySpeed 	= playerSpeed; 
			double myTime 	= myDist / mySpeed;
			
			if(myDist < 0.75 && distances.get(0) < 0.75) {
				chance = 0.5;
			}
			else {
				chance = 1 - (myTime/(distances.get(0) + myTime));
			}
		}
		
		if (!players.isEmpty() && !isBallNearestToMeInMyTeam()) {
			if(ourDistances.get(0) < 0.75 && distances.get(0) < 0.75) {
				chance = 0.5;
			}
			else {
				chance = 1 - (ourDistances.get(0)/(distances.get(0) + ourDistances.get(0)));	
			}
		}
		
		return chance;
	}
	
	/**
	 * Compute chance to get ball by aget from agent posistion and closest
	 * player (team player or opponent player). Computation depends on time 
	 * prediction to get ball by player.
	 * TODO - Implement computation with rotation agent to ball.
	 * 
	 * @author xgregorm
	 * @author A55-Kickers
	 * 
	 * @return chance to get ball by agent in range <0,1>
	 */
	public double chanceToGetBallByMe() {
		double playerSpeed 	= 0;
		double chance 		= 1;
		Vector3D ball_pos	= WorldModel.getInstance().getBall().getPosition();
		
		ArrayList<Player> players 	= WorldModel.getInstance().getOpponentPlayers();
		List<Double> distances 		= new ArrayList<Double>();
		
		for ( Player player: players) {
			double playerDist 	= player.getDistanceFromBall();
			double playerSpeedX = player.getSpeed().getX();
			double playerSpeedY = player.getSpeed().getY();
			playerSpeed  		=  Math.sqrt(Math.pow(playerSpeedX,2)/Math.pow(playerSpeedY,2));
			double time 		= playerDist / playerSpeed;
			
			distances.add(time);
		}
		
		List<Player> ourPlayers = WorldModel.getInstance().getTeamPlayers();
		List<Double> ourDistances = new ArrayList<Double>();
		
		for ( Player player: ourPlayers) {
			double playerDist 	= player.getDistanceFromBall();
			double playerSpeedX = player.getSpeed().getX();
			double playerSpeedY = player.getSpeed().getY();
			playerSpeed  		=  Math.sqrt(Math.pow(playerSpeedX,2)/Math.pow(playerSpeedY,2));
			double time 		= playerDist / playerSpeed;
			
			distances.add(time);
		}
		
		Collections.sort(distances);
		
		
		if (!players.isEmpty()) {
			double myDist 	= AgentModel.getInstance().getPosition().getXYDistanceFrom(ball_pos);
			double mySpeed 	= playerSpeed; 
			double myTime 	= myDist / mySpeed;
			
			if(myDist < 0.75 && distances.get(0) < 0.75) {
				chance = 0.5;
			}
			else {
				chance = 1 - (myTime/(distances.get(0) + myTime));
			}
		}
		
		return chance;
	}
	
	/**
	 * To string conversion of chance to get ball. Only debug purpose.
	 * 
	 * @author xgregorm
	 * @author A55-Kickers
	 * 
	 * @return chance to get ball by our team in percent.
	 */
	public String chanceToGetBallString() {
		System.out.println("Chance to get ball by our team: " + ((int)(chanceToGetBall()*100)) + "%");
		return "" + chanceToGetBall();
	}
	
	/**
	 * Method return if agent is nearest to his goal.
	 * 
	 * @author xgregorm
	 * @author A55-Kickers
	 * 
	 * @return isNearest - true or false if agent is nearest to his goal
	 */
	public boolean isINearestToGoal() {
		boolean isNearest = true;
		double myDistFromGoal = AgentModel.getInstance().getPosition().getXYDistanceFrom(FixedObject.ourPostMiddle());
		
		List<Player> ourPlayers = WorldModel.getInstance().getTeamPlayers();
		
		for ( Player player: ourPlayers) {
			double playerDist 	= player.getDistanceFromMyGoal();
			
			if (playerDist < myDistFromGoal) 
				return false;
		}
		
		return isNearest;
	}
	
	
	/**
	 * Method compute fitnees of pass to position. Method depends on 
	 * position of other opponent players, which can endanger the ball
	 * on the way to destination point of pass.
	 * 
	 * TODO Compute relative fitness to strength of kick (annotations) 
	 * 
	 * @author xgregorm
	 * @author A55-Kickers
	 * 
	 * @param destinationPoint - destination point to pass
	 * @return fitnessOfPass - range from 0 to 1, that indicates suitability of pass
	 */
	public double getPassFitness(Vector3D destinationPoint) {
		double fitnessOfPass 		= 0;
		
		Vector3D ball_pos	 		= WorldModel.getInstance().getBall().getPosition().setZ(0);
		double myDist 		 		= AgentModel.getInstance().getPosition().getXYDistanceFrom(ball_pos);
		
		Vector3D passVector  		= destinationPoint.subtract(ball_pos);
		double sizeOfPassVector 	= Math.sqrt(Math.pow(passVector.getX(), 2)+Math.pow(passVector.getY(), 2)+Math.pow(passVector.getZ(), 2));
		
		ArrayList<Player> players 	= WorldModel.getInstance().getOpponentPlayers();
		List<Double> angles 		= new ArrayList<Double>();
		
		if (myDist < 0.75 && !players.isEmpty()) {
			for ( Player player: players) {
				Vector3D playerPosition 	= player.getPosition().setZ(0);
				Vector3D playerVector		= playerPosition.subtract(ball_pos);
				double scalar 				= passVector.dotProduct(playerVector);
				double sizeOfPlayerVector 	= Math.sqrt(Math.pow(playerVector.getX(), 2)+Math.pow(playerVector.getY(), 2)+Math.pow(playerVector.getZ(), 2));
				double angle 				= Math.toDegrees(Math.acos(scalar/(sizeOfPassVector * sizeOfPlayerVector)));
				
				angles.add(angle);
			}
			
			Collections.sort(angles);
			
			fitnessOfPass = angles.get(0);
		}
		
		return fitnessOfPass;
	}
		
	/*-------------------------------------------------------------------------------------------
	 * Vytvorenie formacie
	 */
	
	public void setMyFormPosition(){
		
		PositionInFormation = new Vector3D[7];
		double ball_pos = WorldModel.getInstance().getBall().getPosition().getX();
		int flip = 1;
		
		if (AgentInfo.side == Side.RIGHT) {
			flip = -1;
			ball_pos = ball_pos * flip;
		}
		
		if(ball_pos < 5.5 && ball_pos > -4.5){		
			PositionInFormation[0]=Vector3D.cartesian((ball_pos - 5) * flip, 0 * flip , 0);
			PositionInFormation[1]=Vector3D.cartesian((ball_pos - 1) * flip, 0 * flip, 0);
			PositionInFormation[2]=Vector3D.cartesian((ball_pos + 4) * flip, 0 * flip, 0);
			PositionInFormation[3]=Vector3D.cartesian((ball_pos - 5) * flip, -3 * flip, 0);
			PositionInFormation[4]=Vector3D.cartesian((ball_pos - 5) * flip, 3 * flip, 0);
			PositionInFormation[5]=Vector3D.cartesian((ball_pos - 1) * flip, -3 * flip, 0);
			PositionInFormation[6]=Vector3D.cartesian((ball_pos - 1) * flip, +3 * flip, 0);
		}else{
			if(ball_pos <= 0){
				PositionInFormation[0]=Vector3D.cartesian(-9 * flip, 0 * flip, 0);
				PositionInFormation[1]=Vector3D.cartesian(-5 * flip, 0 * flip, 0);
				PositionInFormation[2]=Vector3D.cartesian(0 * flip, 0 * flip, 0);
				PositionInFormation[3]=Vector3D.cartesian(-9 * flip, -4 * flip, 0);
				PositionInFormation[4]=Vector3D.cartesian(-9 * flip, 4 * flip, 0);
				PositionInFormation[5]=Vector3D.cartesian(-5 * flip, -4 * flip, 0);
				PositionInFormation[6]=Vector3D.cartesian(-5 * flip, +4 * flip, 0);
			}
			else {
				PositionInFormation[0]=Vector3D.cartesian(0 * flip, 0 * flip, 0);
				PositionInFormation[1]=Vector3D.cartesian(4 * flip, 0 * flip, 0);
				PositionInFormation[2]=Vector3D.cartesian(8 * flip, 0 * flip, 0);
				PositionInFormation[3]=Vector3D.cartesian(0 * flip, -4 * flip, 0);
				PositionInFormation[4]=Vector3D.cartesian(0 * flip, 4 * flip, 0);
				PositionInFormation[5]=Vector3D.cartesian(4 * flip, -4 * flip, 0);
				PositionInFormation[6]=Vector3D.cartesian(4 * flip, +4 * flip, 0);
			}
		}
		
		for(int i=0;i < 7;i++){
			if(AgentInfo.playerId == i+1) {
				pos = PositionInFormation[i];
			}
		}
	}
	
	public Vector3D getFormPosition(){
		setMyFormPosition();
		
		relpos = AgentModel.getInstance().relativize(pos);
		
		
		return relpos;
	}
	
	/**
	 * Returns global coordinates of player's position in formation.
	 * 
	 * @author xsuchac
	 * @author A55-Kickers
	 * 
	 * @return pos - player's position in formation that is global, not relative to player.
	 */
	public Vector3D getFormPositionGlobal(){
		setMyFormPosition();
		return pos;
	}
	
	public boolean isInPositionArea(){
		if(Math.abs(AgentModel.getInstance().getPosition().getXYDistanceFrom(pos))<1)
			return true;
		else 
			return false;
	}
	
	public boolean isOnPosition(){
		if(Math.abs(AgentModel.getInstance().getPosition().getXYDistanceFrom(pos))<0.5)
			return true;
		else 
			return false;
	}
}
