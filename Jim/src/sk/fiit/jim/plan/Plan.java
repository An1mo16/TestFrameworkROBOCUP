package sk.fiit.jim.plan;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.jim.agent.models.EnvironmentModel;
import sk.fiit.jim.agent.models.WorldModel;
import sk.fiit.jim.agent.skills.HighSkill;
import sk.fiit.robocup.library.geometry.Angles;

/**
 * Rewrited Plan from ruby
 * 
 * @author Benkovic
 */

/* Overall HIGHSKILL NEEDED:
 * Pick a LOWSKILL
 * FormationHelper
 */

public class Plan extends Thread implements IPlan {

	public List<HighSkill> Highskill_Queue = new ArrayList<HighSkill>();
	public boolean turned = false;
	//TODO encapsutalion!!!
	public boolean beamed = false;
	public Angles straight_range1 = new Angles(0.0, 15.0);
	public Angles straight_range2 = new Angles(345.0, 360.0);
	

	public void run() {

		/*
		 * METHOD CAN BE USE ! turned_to_goal (); see_ball(); ball_front();
		 * ball_back(); is_ball_mine(); straight(); near_ball(); ball_unseen();
		 */

		control();
	}

	public void control() {
		
		if (Highskill_Queue.isEmpty() == true) {
			replan();
		} else {
			try {
				Highskill_Queue.get(0).execute();
				if (Highskill_Queue.get(0).isEnded()) {
					Highskill_Queue.remove(0);
				}
			} catch (Exception ex) {
				Logger.getLogger(Plan.class.getName()).log(Level.SEVERE, null,
						ex);

			}

		}

	}

	public void replan() {

	}

	public boolean turned_to_goal() {
		if (straight_range1.include(AgentInfo.getInstance().kickTarget().getPhi())
				|| straight_range2.include(AgentInfo.getInstance().kickTarget().getPhi())) {
			return true;
		}
		return false;
	}
	//TODO presunut tieto metody do prislusnych modelov

	public boolean ball_front() {
		return AgentInfo.getInstance().getWhereIsBall() == "front";
	}

	public boolean ball_back() {
		return AgentInfo.getInstance().getWhereIsBall() == "back";
	}

	public boolean is_ball_mine() {
		return AgentInfo.getInstance().isBallMine();
	}

	public boolean straight() {
		if (straight_range1.include(AgentInfo.getInstance().kickTarget().getPhi())
				|| straight_range2.include(AgentInfo.getInstance().ballControlPosition().getPhi())) {
			return true;
		}
		return false;
	}

	public boolean near_ball() {
		return AgentInfo.getInstance().nearBall();
	}

	public double ball_unseen() {
		return EnvironmentModel.SIMULATION_TIME
				- WorldModel.getInstance().getBall().getLastTimeSeen();
	}

}
