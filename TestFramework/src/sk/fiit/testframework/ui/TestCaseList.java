/**
 * Name:    TestCaseList.java
 * Created: Mar 18, 2012
 * 
 * @author: ivan
 */
package sk.fiit.testframework.ui;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import sk.fiit.testframework.trainer.testsuite.testcases.tournament.DtWalkToBall;
import sk.fiit.testframework.trainer.testsuite.testcases.tournament.KickAccuracy;
import sk.fiit.testframework.trainer.testsuite.testcases.tournament.KickDistance;
import sk.fiit.testframework.trainer.testsuite.testcases.tournament.StandUp;
import sk.fiit.testframework.trainer.testsuite.testcases.tournament.TurnAround;
import sk.fiit.testframework.trainer.testsuite.testcases.tournament.WalkFast;
import sk.fiit.testframework.trainer.testsuite.testcases.tournament.WalkStable;


// Temporary TournamentTestCase List so the GUI has something to show !
// TODO do better
public class TestCaseList {
	
	public static List<TestHolder> testCaseList = Collections.synchronizedList(new LinkedList<TestHolder>());
	
	static {
		testCaseList.add(new TestHolder("DtWalkToBall", 		new DtWalkToBall()));
		testCaseList.add(new TestHolder("Kopanie - vzdialenost", 	new KickDistance()));
		testCaseList.add(new TestHolder("Kopanie - presnost", 		new KickAccuracy()));
		testCaseList.add(new TestHolder("Vstavanie", 				new StandUp()));
		testCaseList.add(new TestHolder("Otocenie", 				new TurnAround()));
		testCaseList.add(new TestHolder("Kracanie - rychle", 		new WalkFast()));
		testCaseList.add(new TestHolder("Kracanie - stabilne", 		new WalkStable()));
	}
	
	public static class TestHolder {
		public String name;
		public Runnable runnable;
		
		public TestHolder(String name, Runnable runnable){
			this.name = name;
			this.runnable = runnable;
		}
		
		public String toString() {
			return name;
		}
	}
	
}
