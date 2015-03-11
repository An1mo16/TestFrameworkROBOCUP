package sk.fiit.jim.decision.tactic;

import java.util.List;

/**
 * Interface for defining tactic instances
 * 
 * @author  Samuel Benkovic <sppred@gmail.com>, Vladimir Bosiak <vladimir.bosiak@gmail.com>
 * @year	2013/2014
 * @team	RFC Megatroll
 */
public interface ITactic {

	/**
	 * Getter for initialization condition
	 * 
	 * @param currentSituations List of current situations
	 * @return Status if tactic is usable
	 */
	public boolean getInitCondition(List<String> currentSituations);

	/**
	 * Getter for progress condition
	 * 
	 * @param currentSituations list of current situations
	 * @return Status if tactic ended
	 */
	public boolean getProgressCondition(List<String> currentSituations);

	/**
	 * Execute tactic
	 */
	public void run();

	/**
	 * Get value of usage for tactic
	 * 
	 * @param currentSituations List of current situations
	 * @return Value of useage
	 */
	public int getSuitability(List<String> currentSituations);
	
}
