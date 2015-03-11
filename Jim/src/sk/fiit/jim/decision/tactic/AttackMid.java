package sk.fiit.jim.decision.tactic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sk.fiit.jim.decision.situation.FightForBall;

public class AttackMid implements ITactic {

	private ArrayList<String> prescribedSituations = new ArrayList<String>(
			Arrays.asList(FightForBall.class.getName()));
	
	/*
	 * Condition which must be true when you start tactic
	 */
	public boolean getInitCondition(List<String> currentSituations) {
		return true;
	}

	/*
	 * Condition must be true if the given tactic is currently being executed
	 */
	public boolean getProgressCondition(List<String> currentSituations) {
		return false;
	}

	/*
	 * Implementation of actual tactic
	 */
	public void run() {

	}

	/*
	 * Method is needed if more tactics initcondition evaluates to true
	 * Calculated on the basis HighSkill and current situation.
	 */
	public int getSuitability(List<String> currentSituations) {
		int NumberOfMatch = 0;
		/*
		 * TODO - better check two lists ? Maybe HASH ? NumberOfMatch - one
		 * class for every strategy, not in all strategies
		 */

		for (String a : currentSituations) {
			for (String b : this.prescribedSituations) {
				if (a.equals(b)) {
					NumberOfMatch++;
					break;
				}
			}
		}

		/*
		 * ReturnFit returns number (NumberOfMatch) - 1,2,5 etc. NumberOfMatch
		 * in currentsituations and prescribedsituations
		 */
		return NumberOfMatch;
	}

}
