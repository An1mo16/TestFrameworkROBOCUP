package sk.fiit.testframework.worldrepresentation.models;

import java.util.ArrayList;

import sk.fiit.robocup.library.geometry.Vector3;

public class Scene {

	private SceneGraphDescription description;
	private Vector3 ballLocation;
	private Vector3 previousBallLocation;
	private ArrayList<Player> players;

	public Scene()
	{
		this.description= new SceneGraphDescription();
		this.players = new ArrayList<Player>();
	}
	
	public SceneGraphDescription getDescription() {
		return description;
	}
	
	public void setBallLocation(Vector3 ballLocation) {
		this.previousBallLocation=this.ballLocation;
		this.ballLocation = ballLocation;
	}

	public Vector3 getBallLocation() {
		return ballLocation;
	} 
	
	public boolean isBallMoving(){
		return this.previousBallLocation.getXYDistanceFrom(this.ballLocation)>0;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	
}
