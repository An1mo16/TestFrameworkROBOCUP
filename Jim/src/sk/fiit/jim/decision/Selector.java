package sk.fiit.jim.decision;

import java.util.ArrayList;
import java.util.List;

import sk.fiit.jim.decision.situation.SituationManager;
import sk.fiit.jim.decision.strategy.IStrategy;
import sk.fiit.jim.decision.strategy.OffensiveStrategy;
import sk.fiit.jim.decision.strategy.StrategyList;
import sk.fiit.jim.decision.tactic.ITactic;

/**
 * Class for selecting strategy and tactic according to detected situations
 * 
 * @author 	Vlado Bosiak <vladimir.bosiak@gmail.com>
 * @year	2013/2014
 * @team	RFC Megatroll
 * @version 1.0.0
 */
public class Selector {

	/** Constant for initial fitness */
	private final static int INITIAL_FITTNESS = 0;
	/** Constant for default strategy */
    private final static IStrategy DEFAULT_STRATEGY = new OffensiveStrategy();
	
    /** Selected strategy */
    private IStrategy selectedStrategy;
    /** Selected tactic */
    private ITactic selectedTactic;
    /** List of tactics from selected strategy */
    private ArrayList<IStrategy> strategyList = StrategyList.getStrategiesList();
    /** Situation manager */
    private SituationManager situationManager = new SituationManager();

    /**
     * Select best strategy
     * 
     * @throws Exception
     */
    void selectStrategy() throws Exception {
        IStrategy bestStrategy = this.getBestStrategyForSituation(this.situationManager.getListOfCurrentSituations());
		this.setSelectedStrategy(bestStrategy);
    }
    
    /**
     * Method for getting best strategy according to situations
     * 
     * @param currentSituations
     * @return Best strategy or default strategy
     */
    private IStrategy getBestStrategyForSituation(List<String> currentSituations) {
        int bestSuitability = Selector.INITIAL_FITTNESS;
        int actualSuitability = Selector.INITIAL_FITTNESS;
        IStrategy bestStrategy = Selector.DEFAULT_STRATEGY;

        for (IStrategy strategy : this.strategyList) {
        	actualSuitability = Selector.INITIAL_FITTNESS;
            try {
                actualSuitability = strategy.getSuitability(currentSituations);
            } catch (Exception e) {
            	System.err.println("GETTING BEST STRATEGY:"+e.getMessage());
            }
            if (actualSuitability > bestSuitability) {
                bestSuitability = actualSuitability;
                bestStrategy = strategy;
            }
        }
        return bestStrategy;
    }

    /**
     * Select best tactic
     * 
     * @throws Exception
     */
    void selectTactic() throws Exception {
    	ITactic bestTactic = this.getBestTacticForSituations(this.situationManager.getListOfCurrentSituations());
    	this.setSelectedTactic(bestTactic);
    }

    /**
     * Method for getting best tactic according to situations 
     * 
     * @param currentSituations
     * @return Best tactic or default tactic
     */
    private ITactic getBestTacticForSituations(List<String> currentSituations) {
    	ITactic bestTactic = this.selectedStrategy.getDefaultTactic();
    	List<ITactic> tacticList = this.selectedStrategy.getPrescribedTactics();
    	int bestSuitability = Selector.INITIAL_FITTNESS;
    	int actualSuitability = Selector.INITIAL_FITTNESS;
    	for(ITactic tactic : tacticList) {
    		if(!tactic.getInitCondition(currentSituations)) {
    			continue;
    		}
    		actualSuitability = Selector.INITIAL_FITTNESS;
            try {
                actualSuitability = tactic.getSuitability(currentSituations);
            } catch (Exception e) {
                System.err.println("GETTING BEST TACTIC:"+e.getMessage());
            }
            if (actualSuitability > bestSuitability) {
                bestSuitability = actualSuitability;
                bestTactic = tactic;
            }
    	}
    	//System.out.println(bestTactic.getClass().getSimpleName());
    	return bestTactic;
    }

    /**
     * Setter for selected strategy
     * 
     * @param strategy Strartegy which have to be selected
     * @return Selector instance for chaining
     */
    Selector setSelectedStrategy(IStrategy strategy) {
        this.selectedStrategy = strategy;
        return this;
    }

    /**
     * Setter for selected tactic
     * 
     * @param tactic Tactic which have to be selected
     * @return Selector instance for chaining
     */
    Selector setSelectedTactic(ITactic tactic) {
        this.selectedTactic = tactic;
        return this;
    }

    /**
     * Getter for selected strategy
     * 
     * @return Selected strategy.
     */
    IStrategy getSelectedStrategy() {
        return this.selectedStrategy;
    }

    /**
     * Getter for selected tactic
     * 
     * @return Selected tactic
     */
    ITactic getSelectedTactic() {
        return this.selectedTactic;
    }
    
    /**
     * Getter for situation manager
     * 
     * @return Instance of SituationManager
     */
    SituationManager getSituationManager() {
    	return this.situationManager;
    }

}
