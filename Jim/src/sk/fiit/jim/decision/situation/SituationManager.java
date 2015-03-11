package sk.fiit.jim.decision.situation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Samuel Benkovic, Michal Petras
 */
public class SituationManager {

	private List<String> currentSituations = new ArrayList<String>();
	private List<ISituation> situations = new ArrayList<ISituation>();

	// Recommendation #Type(Other) Create SituationException |
	// vladimir.bosiak@gmail.com 2013-11-26T15:30:16.1070000Z
	public List<String> getListOfCurrentSituations()
			throws Exception {

		/*
		 * Load list of situation which exist Iterate situations Invoke method
		 * checkSituation Check if is true If is true -> add to CurrentSituation
		 */
		currentSituations.clear();
		situations = SituationList.getSituationsList();
		Iterator<ISituation> it = this.situations.iterator();
		while (it.hasNext()) {
			ISituation TestSituation = it.next();
			if (TestSituation.checkSituation() == true) {
				String part = TestSituation.getClass().getName();
				currentSituations.add(part);
			}
		}
		return currentSituations;
	}

	// Set Situations for JUnit Test Mock
	public void setSituations(ArrayList<ISituation> situations) {
		this.situations = situations;
	}

}
