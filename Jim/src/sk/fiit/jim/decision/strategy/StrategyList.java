package sk.fiit.jim.decision.strategy;

import java.util.ArrayList;

/**
 * Team 04 - RFC Megatroll List of strategy which exist
 * 
 * @author michal petras TODO - better ADD?
 */
public class StrategyList {
    public static String[] strategyNames = {
            OffensiveStrategy.class.getSimpleName()
    };

	//Todo #Task(Dynamic loading of Strategy objects) #Priority(Low) | vladimir.bosiak@gmail.com 2013-11-26T15:41:33.6760000Z 
	public static ArrayList<IStrategy> getStrategiesList(){
		ArrayList<IStrategy> strategies = new ArrayList<IStrategy>();
		//strategies.add(new OffensiveStrategy());
        for (String strategy: StrategyList.strategyNames) {
            try {
            strategies.add(StrategyFactory.createStrategy(strategy));
            } catch (Exception e) {
                //TODO
            }
        }

		return strategies;
	}

}
