package sk.fiit.jim.decision.strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sk.fiit.jim.decision.situation.FightForBall;
import sk.fiit.jim.decision.tactic.AttackLeft;
import sk.fiit.jim.decision.tactic.AttackMid;
import sk.fiit.jim.decision.tactic.AttackRight;
import sk.fiit.jim.decision.tactic.ITactic;

/**
 * Team 04 - RFC Megatroll Prototype of one strategy
 * 
 * @author michal petras + samo benkovic
 * 
 */
public class OffensiveStrategy implements IStrategy {

	private ArrayList<String> prescribedSituations = new ArrayList<String>(
			Arrays.asList(FightForBall.class.getName()));
	private ArrayList<ITactic> prescribedTactics = new ArrayList<ITactic>(Arrays.asList(new AttackMid(),new AttackLeft(),new AttackRight()));
	private ITactic defaultTactic = new AttackMid();

	/*
	 * returnSuitability - check list of currentSituation and list of prescribed
	 * situations
	 */
	public int getSuitability(List<String> currentSituations)
			throws Exception {
		int NumberOfMatch = 0;

		//Todo #Task(better check two lists ? Maybe HASH ? NumberOfMatch - one.) #Solver() #Priority() | xmarkech 2013-12-10T20:38:13.4560000Z
		/*
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

	public ArrayList<ITactic> getPrescribedTactics() {
		return prescribedTactics;
	}

	public ITactic getDefaultTactic() {
		return defaultTactic;
	}
	
	

}
