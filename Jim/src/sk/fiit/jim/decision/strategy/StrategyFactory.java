package sk.fiit.jim.decision.strategy;


/**
 * Factory class for strategies
 *
 * @author Matej Badal <matejbadal@gmail.com>
 */
public final class StrategyFactory {
    public static IStrategy createStrategy(String strategyName) throws  ClassNotFoundException, IllegalAccessException, InstantiationException {
        IStrategy strategy = null;
        strategy = (IStrategy) Class.forName(strategyName).newInstance();
        return strategy;
    }
}
