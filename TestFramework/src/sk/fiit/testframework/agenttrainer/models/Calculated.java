/**
 * Name:    ICalculated.java
 * Created: 8.5.2011
 * 
 * @author: Roman Kovac
 */
package sk.fiit.testframework.agenttrainer.models;


public abstract class Calculated {
	
	private boolean isCalculated;
	private String synPhase;
	private String synEffector;
	private int constant;
	
	public boolean getIsCalculated() {
		return isCalculated;
	}
	
	public void setIsCalculated(boolean isCalculated) {
		this.isCalculated=isCalculated;
	}

	public void setSynPhase(String synPhase) {
		this.synPhase = synPhase;
	}

	public String getSynPhase() {
		return synPhase;
	}

	public void setSynEffector(String synEffector) {
		this.synEffector = synEffector;
	}

	public String getSynEffector() {
		return synEffector;
	}

	public void setConstant(int constant) {
		this.constant = constant;
	}

	public int getConstant() {
		return constant;
	}
	
	
	
	
}
