package sk.fiit.testframework.parsing.models;

public class EnvironmentPart {

	private double fieldLength =-1;
	private double fieldWidth =-1;
	private double fieldHeight =-1;
	
	private double goalWidth =-1;
	private double goalDepth =-1;
	private double goalHeight =-1;
	
	private double freeKickDistance =-1;
	private double waitBeforeKickOff =-1;
	
	private double agentRadius =-1;
	private double ballRadius =-1;
	private double ballMass =-1;
	
	private double ruleGoalPauseTime =-1;
	private double ruleKickInPauseTime =-1;
	private double ruleHalfTime =-1;
	private PlayMode[] playModes;
	
	public void setFieldLength(double fieldLength) {
		this.fieldLength = fieldLength;
	}
	public double getFieldLength() {
		return fieldLength;
	}
	public void setFieldWidth(double fieldWidth) {
		this.fieldWidth = fieldWidth;
	}
	public double getFieldWidth() {
		return fieldWidth;
	}
	public void setFieldHeight(double fieldHeight) {
		this.fieldHeight = fieldHeight;
	}
	public double getFieldHeight() {
		return fieldHeight;
	}
	public void setGoalWidth(double goalWidth) {
		this.goalWidth = goalWidth;
	}
	public double getGoalWidth() {
		return goalWidth;
	}
	public void setGoalDepth(double goalDepth) {
		this.goalDepth = goalDepth;
	}
	public double getGoalDepth() {
		return goalDepth;
	}
	public void setFreeKickDistance(double freeKickDistance) {
		this.freeKickDistance = freeKickDistance;
	}
	public double getFreeKickDistance() {
		return freeKickDistance;
	}
	public void setWaitBeforeKickOff(double waitBeforeKickOff) {
		this.waitBeforeKickOff = waitBeforeKickOff;
	}
	public double getWaitBeforeKickOff() {
		return waitBeforeKickOff;
	}
	public void setAgentRadius(double agentRadius) {
		this.agentRadius = agentRadius;
	}
	public double getAgentRadius() {
		return agentRadius;
	}
	public void setBallRadius(double ballRadius) {
		this.ballRadius = ballRadius;
	}
	public double getBallRadius() {
		return ballRadius;
	}
	public void setBallMass(double ballMass) {
		this.ballMass = ballMass;
	}
	public double getBallMass() {
		return ballMass;
	}
	public void setRuleGoalPauseTime(double ruleGoalPauseTime) {
		this.ruleGoalPauseTime = ruleGoalPauseTime;
	}
	public double getRuleGoalPauseTime() {
		return ruleGoalPauseTime;
	}
	public void setGoalHeight(double goalHeight) {
		this.goalHeight = goalHeight;
	}
	public double getGoalHeight() {
		return goalHeight;
	}
	public void setRuleKickInPauseTime(double ruleKickInPauseTime) {
		this.ruleKickInPauseTime = ruleKickInPauseTime;
	}
	public double getRuleKickInPauseTime() {
		return ruleKickInPauseTime;
	}
	public void setRuleHalfTime(double ruleHalfTime) {
		this.ruleHalfTime = ruleHalfTime;
	}
	public double getRuleHalfTime() {
		return ruleHalfTime;
	}
	public void setPlayModes(PlayMode[] playModes) {
		this.playModes = playModes;
	}
	public PlayMode[] getPlayModes() {
		return playModes;
	}	
	
}
