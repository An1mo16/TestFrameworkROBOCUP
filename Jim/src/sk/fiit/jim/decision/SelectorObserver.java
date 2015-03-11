package sk.fiit.jim.decision;

import sk.fiit.jim.agent.parsing.ParsedData;
import sk.fiit.jim.agent.parsing.ParsedDataObserver;
import sk.fiit.jim.decision.tactic.ITactic;

import java.util.List;

/**
 * Observer for selector, that controls and replans strategies and tactics each time server
 * sends message to agent
 *
 * @author Vlado Bosiak <vladimir.bosiak@gmail.com> and Matej Badal <matejbadal@gmail.com>
 */
public class SelectorObserver implements ParsedDataObserver {
    private Selector selector = new Selector();

    private static SelectorObserver instance = new SelectorObserver();

    public static SelectorObserver getInstance() {
        return SelectorObserver.instance;
    }

    private void controlTactics() throws Exception {
        ITactic currentTactic = this.selector.getSelectedTactic();
        List<String> currentSituations = this.selector.getSituationManager().getListOfCurrentSituations();
		if (currentTactic != null) {
			if (currentTactic.getProgressCondition(currentSituations)) {
				return;
			}
		}
        this.selector.selectTactic();
        currentTactic.run();
    }


    private void controlStrategy() throws Exception {
        this.selector.selectStrategy();
    }

    @Override
    public void processNewServerMessage(ParsedData data) {
        try {
            controlStrategy();
            controlTactics();
        } catch (Exception e) {
            //TODO
        }
    }
}
