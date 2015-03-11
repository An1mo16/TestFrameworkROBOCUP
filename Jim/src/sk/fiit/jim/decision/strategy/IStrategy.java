package sk.fiit.jim.decision.strategy;

import java.util.ArrayList;
import java.util.List;

import sk.fiit.jim.decision.tactic.ITactic;

public interface IStrategy {

	public int getSuitability(List<String> currentSituations) throws Exception;
	public ArrayList<ITactic> getPrescribedTactics();
	public ITactic getDefaultTactic();
	
}
