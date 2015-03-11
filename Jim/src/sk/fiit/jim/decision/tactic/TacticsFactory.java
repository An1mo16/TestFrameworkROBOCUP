package sk.fiit.jim.decision.tactic;


/**
 * Factory class for Tactics
 * @author Matej Badal <matejbadal@gmail.com>
 *
 */
public class TacticsFactory {
    public static ITactic createTactic(String tacticName) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        ITactic tactic = null;
        tactic = (ITactic)Class.forName(tacticName).newInstance();
        return tactic;

    }
}
