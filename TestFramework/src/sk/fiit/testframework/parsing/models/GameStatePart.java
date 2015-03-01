package sk.fiit.testframework.parsing.models;

public class GameStatePart {

	private double time = -1;
	private String teamLeft;
	private String teamRight;
	private int half = -1;
	private int scoreLeft = -1;
	private int scoreRight = -1;
	private int playMode = -1;

	public void setTime(double time) {
		this.time = time;
	}

	public double getTime() {
		return time;
	}

	public void setTeamLeft(String teamLeft) {
		this.teamLeft = teamLeft;
	}

	public String getTeamLeft() {
		return teamLeft;
	}

	public void setTeamRight(String teamRight) {
		this.teamRight = teamRight;
	}

	public String getTeamRight() {
		return teamRight;
	}

	public void setHalf(int half) {
		this.half = half;
	}

	public int getHalf() {
		return half;
	}

	public void setScoreLeft(int scoreLeft) {
		this.scoreLeft = scoreLeft;
	}

	public int getScoreLeft() {
		return scoreLeft;
	}

	public void setScoreRight(int scoreRight) {
		this.scoreRight = scoreRight;
	}

	public int getScoreRight() {
		return scoreRight;
	}

	public void setPlayMode(int playMode) {
		this.playMode = playMode;
	}

	public int getPlayMode() {
		return playMode;
	}
}
