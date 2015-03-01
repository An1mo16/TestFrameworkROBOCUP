/**
 * Name:    Range.java
 * Created: 30.4.2011
 * 
 * @author: Roman Kovac
 */
package sk.fiit.testframework.agenttrainer.models;

/**
 * TODO: Replace with a brief purpose of class / interface.
 * 
 * @author Roman Kovac
 *
 */
public class Range {

	private int from;
	private int to;
	private int step;
	
	public Range()
	{
		
	}
	
	public Range(int from, int to, int step)
	{
		this.from=from;
		this.to=to;
		this.step=step;
	}
	
	public void setFrom(int from) {
		this.from = from;
	}
	public int getFrom() {
		return from;
	}
	public void setTo(int to) {
		this.to = to;
	}
	public int getTo() {
		return to;
	}
	public void setStep(int step) {
		this.step = step;
	}
	public int getStep() {
		return step;
	}
	
	
}
