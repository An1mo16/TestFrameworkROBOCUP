package sk.fiit.jim.decision.tactic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sk.fiit.jim.decision.situation.Ball1R;
import sk.fiit.jim.decision.situation.Ball2R;
import sk.fiit.jim.decision.situation.Ball3R;
import sk.fiit.jim.decision.situation.Ball4R;

public class AttackRight implements ITactic {

	private ArrayList<String> prescribedSituations = new ArrayList<String>(
			Arrays.asList(Ball4R.class.getName(), Ball3R.class.getName(), Ball2R.class.getName(), Ball1R.class.getName()));
	private ArrayList<String> initSituations = new ArrayList<String>(
			Arrays.asList(Ball1R.class.getName()));
	private ArrayList<String> progressSituations = new ArrayList<String>(
			Arrays.asList(Ball2R.class.getName()));
	
	/*
	 * Condition which must be true when you start tactic
	 */
	public boolean getInitCondition(List<String> currentSituations) {
		boolean isStart = false;

		for (String a : currentSituations) {
			for (String b : this.initSituations) {
				if (a.equals(b)) {
					isStart = true;
					break;
				}
			}
		}
		return isStart;
	}

	/*
	 * Condition must be true if the given tactic is currently being executed
	 */
	public boolean getProgressCondition(List<String> currentSituations) {
		boolean isContinue = false;

		for (String a : currentSituations) {
			for (String b : this.progressSituations) {
				if (a.equals(b)) {
					isContinue = true;
					break;
				}
			}
		}
		return isContinue;
	}

	/*
	 * Implementation of actual tactic
	 */
	public void run() {
//		Vector3D position_to_go = Vector3D.cartesian(10, 1, 0.4);
//		Planner.getPlan().Highskill_Queue.add(new GoToPosition(position_to_go,
//				new LambdaCallable() {
//			public boolean call() {
//				return EnvironmentModel.beamablePlayMode();
//			}
//		}));
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
