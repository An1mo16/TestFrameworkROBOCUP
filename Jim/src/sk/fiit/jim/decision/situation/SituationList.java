package sk.fiit.jim.decision.situation;

import java.util.ArrayList;

/**
 * Team RFC Megatroll Class for List of situation which exist
 * 
 * @author michal petras
 * 
 */
public class SituationList {

	//Todo #Task(Dynamic loading of situation objects) #Priority(Low) | vladimir.bosiak@gmail.com 2013-11-26T15:21:36.0130000Z 
	public static ArrayList<ISituation> getSituationsList() {
		ArrayList<ISituation> situations = new ArrayList<ISituation>();
		situations.add(new FarFromEnemyGoal());
		situations.add(new FightForBall());
		situations.add(new Ball1L());
		situations.add(new Ball1R());
		situations.add(new Ball2L());
		situations.add(new Ball2R());
		situations.add(new Ball3L());
		situations.add(new Ball3R());
		situations.add(new Ball4L());
		situations.add(new Ball4R());
		return situations;
	}

}
