package sk.fiit.jim.agent.models.prediction;

import java.util.List;
import java.util.ArrayList;
import sk.fiit.jim.agent.models.AgentModel;
import sk.fiit.jim.agent.models.DynamicObject;
import sk.fiit.jim.agent.models.EnvironmentModel;
import sk.fiit.jim.agent.models.Player;
import sk.fiit.jim.agent.models.WorldModel;
import sk.fiit.jim.agent.parsing.ParsedData;
import sk.fiit.jim.agent.parsing.ParsedDataObserver;
import sk.fiit.jim.annotation.data.AnnotationManager;
import sk.fiit.jim.code_review.Problem;
import sk.fiit.robocup.library.geometry.Vector3D;

/**
 *  Prophet.java
 *  
 *  Calculates the most probable outcome of events in a given time.
 *  TODO: implement the prediction module correctly - not sure it works.
 *
 *@Title	Jim
 *@author	marosurbanec
 *@author	Androids
 */
public class Prophet implements ParsedDataObserver {
	private Prophecy prophecy;
	private final AgentModel agent = AgentModel.getInstance();
	private final WorldModel world = WorldModel.getInstance();
	private static Prophet instance = new Prophet();
	private Vector3D position = Vector3D.cartesian(0, 0, 0);
	private Vector3D old_position = Vector3D.cartesian(0, 0, 0);
	private Vector3D speed = Vector3D.cartesian(0, 0, 0);
	private Vector3D predikciaL = Vector3D.cartesian(0, 0, 0);
	private double old_time = 0;
	private Vector3D predikciaHrac = Vector3D.cartesian(0, 0, 0);
	private Vector3D predikciaBall = Vector3D.ZERO_VECTOR;
	private double ballOldTime=0, now=0;
	private Vector3D ballOldPosition = Vector3D.ZERO_VECTOR;
	private Vector3D ballposition = Vector3D.ZERO_VECTOR;
	private Vector3D ballSpeed = Vector3D.ZERO_VECTOR;
	
	private Prophet() {
		prophecy = new Prophecy();
	}
	
	public static Prophet getInstance() {
		return instance;
	}
	
	@Override
	public void processNewServerMessage(ParsedData data) {
		now = EnvironmentModel.SIMULATION_TIME;
	
		//tato metoda sa bude volat pravidelne
		//sem treba davat vsetky funkcie, napr. calculateBallPosition, atd.
		calcballPrediction(3);
		calcOpponentPlayerPrediction(1);
		calcTeamPlayerPrediction(5);
	}

	
	private void calcOpponentPlayerPrediction(int time){
			
			
			ArrayList<Player> opponentPlayers = WorldModel.getInstance().getOpponentPlayers();
			
			for(Player opponentPlayer: opponentPlayers){
						
			if(EnvironmentModel.GAME_TIME != 0){
					if( (EnvironmentModel.GAME_TIME % 1) == 0){
						old_time = now;
						now = EnvironmentModel.GAME_TIME;
						this.old_position = position;
						position = opponentPlayer.getPosition();
						speed = position.subtract(old_position);
						predikciaHrac = opponentPlayer.getPosition().add(speed.multiply(time)); 
						//System.out.print("//////////////////za 5 sekund budeme: "+predikciaHrac+"\n");
						opponentPlayer.setPrediction(predikciaHrac);
					}					
				}
			}
		}
		
		
	private void calcTeamPlayerPrediction(int time){		
			List<Player> teamPlayers = world.getTeamPlayers();
			for(Player teamPlayer: teamPlayers){
				if(EnvironmentModel.GAME_TIME != 0){
					if( (EnvironmentModel.GAME_TIME % 1) == 0){
						old_time = now;
						now = EnvironmentModel.GAME_TIME;
						this.old_position = position;
						position = teamPlayer.getPosition();
						speed = position.subtract(old_position);
						predikciaHrac = teamPlayer.getPosition().add(speed.multiply(time)); 
						//System.out.print("//////////////////za 5 sekund budeme: "+predikciaHrac+"\n");
						teamPlayer.setPrediction(predikciaHrac);
					}								
				}
			}
	}
	
	public void calcballPrediction(int time){
		double CON_ERTL = 1.17;
		if( (EnvironmentModel.GAME_TIME % 1) == 0){
			//System.out.print("game time: "+EnvironmentModel.GAME_TIME+"\n");
			//System.out.print("pozicia lopty: "+world.getBall().getPosition()+"\n");
			ballOldTime = now;
			now = EnvironmentModel.GAME_TIME;
			this.ballOldPosition = ballposition;
			ballposition = world.getBall().getPosition();
			ballSpeed = ballposition.subtract(ballOldPosition);
			//predikciaBall2 = ball.getPosition().add(ballSpeed.multiply(time));    bez trenia
			predikciaBall = ballposition.add(ballSpeed.divide(CON_ERTL).multiply(1-(Math.pow(Math.E, -1*CON_ERTL*time))));    //vzorec podla bc ertla studenta fiit
			world.getBall().setPrediction(predikciaBall);
			//System.out.print("lopta bude na:"+predikciaBall+"\n");
		}
	}
	/* old from androdis
	 * 
	@Problem("do not ignore friction")
	private void calculateBallPosition(double offset){
		DynamicObject ball = world.getBall();
		if (ball.getSpeed() == null){
			prophecy.setBallRelativePosition(agent.relativize(Vector3D.ZERO_VECTOR));
			prophecy.setBallPosition(Vector3D.ZERO_VECTOR);
			prophecy.setBallPositionRelativized(agent.relativize(Vector3D.ZERO_VECTOR));
			return;
		}
		
		Vector3D whereItIsNow = ball.getSpeed().multiply(now - ball.getLastTimeSeen()).add(ball.getPosition());
		Vector3D whereWillItBe = ball.getSpeed().multiply(offset).add(whereItIsNow);
		Vector3D whereWillItBeBasedOnRelativeSpeed = ball.getRelativeSpeed().multiply(now - ball.getLastTimeSeen() + offset).add(ball.getRelativePosition());
		
		prophecy.setBallPosition(whereWillItBe);
		prophecy.setBallPositionRelativized(agent.relativize(whereWillItBe));
		prophecy.setBallRelativePosition(whereWillItBeBasedOnRelativeSpeed);
	}  
	
	*/
}