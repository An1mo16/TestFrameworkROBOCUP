package sk.fiit.jim.highskills;

import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.jim.agent.models.AgentModel;
import sk.fiit.jim.agent.models.TacticalInfo;
import sk.fiit.jim.agent.models.WorldModel;
import sk.fiit.jim.agent.moves.LowSkill;
import sk.fiit.jim.agent.moves.LowSkills;
import sk.fiit.jim.agent.skills.HighSkill;
import sk.fiit.robocup.library.geometry.Angles;
import sk.fiit.robocup.library.geometry.Vector3D;

/**
 * 
 * Reimplemented from Ruby to Java by Stefan Horvath
 *
 */
public class GotoBall extends HighSkill {

	TacticalInfo tacticalInfo = TacticalInfo.getInstance();
	AgentInfo agentInfo = AgentInfo.getInstance();
	AgentModel agentModel = AgentModel.getInstance();
	
	Angles rovno1 = new Angles(0.0, 25.0);
	Angles rovno2 = new Angles(335.0, 360.0);
	Angles vpravo = new Angles(270.0, 335.0);
	Angles vlavo = new Angles(25.0, 90.0);
	Angles vpravoZad = new Angles(180.0, 270.0);
	Angles vlavoZad = new Angles(90.0, 180.0);
	
	double daleko = 4;
	double blizko = 1.0;
	double tesne = 0.2;
	double odLopty = 0.7;
	double kLopte = 0.1;
	
	Vector3D targetPosition = null;
	boolean closeMove = false;
	boolean indirectMove = false;
	boolean finished = false;
	Vector3D ballPosition = null;
	Vector3D agentPosition = null;
	double movePosition_phi;
		
	public GotoBall(Vector3D targetPosition) {
		super();
		this.targetPosition = targetPosition;
	}
	
	@Override
	public LowSkill pickLowSkill() {
		if (finished)
			return LowSkills.get("rollback");
		
		ballPosition =  WorldModel.getInstance().getBall().getPosition();
		agentPosition = agentModel.getPosition();
		
		Vector3D movePosition = null;
		if (!closeMove)
			movePosition = computeMidMovePoint();
		else
			movePosition = computeFinalMovePoint();
		movePosition_phi = movePosition.getPhi();
		
		if (agentModel.isOnGround()) {
			agentInfo.loguj("PADNUTY");
			closeMove = false;
			indirectMove = false;
			return null; // ????????
		}
		else if (distance(movePosition.getX(), movePosition.getY()) > daleko) {
			agentInfo.loguj("DALEKO");
			closeMove = false;
			if (rovno()) {
				agentInfo.loguj("ROVNO");
				return LowSkills.get("walk_forward");
			}
			else if (vlavo()) {
				agentInfo.loguj("VLAVO");
				return LowSkills.get("turn_left_36");
			}
			else if (vpravo()) {
				agentInfo.loguj("VPRAVO");
				return LowSkills.get("turn_right_36");
			}
			else if (vlavoZad()) {
				agentInfo.loguj("VLAVOZAD");
				return LowSkills.get("turn_left_36");
			}
			else if (vpravoZad()) {
				agentInfo.loguj("VPRAVOZAD");
				return LowSkills.get("turn_right_36");
			}
			else {
				return LowSkills.get("rollback");
			}
		}
		else if (distance(movePosition.getX(), movePosition.getY()) > blizko) {
			agentInfo.loguj("BLIZKO");
			if (rovno()) {
				agentInfo.loguj("ROVNO");
				return LowSkills.get("walk_forward");
			}
			else if (vlavo()) {
				agentInfo.loguj("VLAVO");
				return LowSkills.get("turn_left_cont_20");
			}
			else if (vpravo()) {
				agentInfo.loguj("VPRAVO");
				return LowSkills.get("turn_right_cont_20");
			}
			else if (vlavoZad()) {
				agentInfo.loguj("VLAVOZAD");
				return LowSkills.get("turn_left_90");
			}
			else if (vpravoZad()) {
				agentInfo.loguj("VPRAVOZAD");
				return LowSkills.get("turn_right_90");
			}
			else {
				return LowSkills.get("rollback");
			}
		}
		else if ((distance(movePosition.getX(), movePosition.getY()) > tesne) && indirectMove) {
			agentInfo.loguj("TESNE1");
			if (rovno()) {
				agentInfo.loguj("ROVNO");
				return LowSkills.get("walk_slow");
			}
			else if (vlavo()) {
				agentInfo.loguj("VLAVO");
				return LowSkills.get("step_left");
			}
			else if (vpravo()) {
				agentInfo.loguj("VPRAVO");
				return LowSkills.get("step_right");
			}
			else if (vlavoZad()) {
				agentInfo.loguj("VLAVOZAD");
				return LowSkills.get("turn_left_90");
			}
			else if (vpravoZad()) {
				agentInfo.loguj("VPRAVOZAD");
				return LowSkills.get("turn_right_90");
			}
			else {
				return LowSkills.get("stojRovno");
			}
		}
		else if ((distance(movePosition.getX(), movePosition.getY()) > tesne) && !closeMove) {
			agentInfo.loguj("TESNE2");
			if (rovno()) {
				agentInfo.loguj("ROVNO");
				return LowSkills.get("walk_slow");
			}
			else if (vlavo()) {
				agentInfo.loguj("VLAVO");
				return LowSkills.get("turn_left_cont_20");
			}
			else if (vpravo()) {
				agentInfo.loguj("VPRAVO");
				return LowSkills.get("turn_right_cont_20");
			}
			else if (vlavoZad()) {
				agentInfo.loguj("VLAVOZAD");
				return LowSkills.get("turn_left_90");
			}
			else if (vpravoZad()) {
				agentInfo.loguj("VPRAVOZAD");
				return LowSkills.get("turn_right_90");
			}
			else {
				return LowSkills.get("stojRovno");
			}
		}
		else if ((distance(movePosition.getX(), movePosition.getY()) > tesne) && closeMove) {
			agentInfo.loguj("TESNE3");
			if (rovno()) {
				agentInfo.loguj("ROVNO");
				return LowSkills.get("walk_slow");
			}
			else if (vlavo()) {
				agentInfo.loguj("VLAVO");
				return LowSkills.get("turn_left_cont_10");
			}
			else if (vpravo()) {
				agentInfo.loguj("VPRAVO");
				return LowSkills.get("turn_right_cont_10");
			}
			else if (vlavoZad()) {
				agentInfo.loguj("VLAVOZAD");
				return LowSkills.get("turn_left_90");
			}
			else if (vpravoZad()) {
				agentInfo.loguj("VPRAVOZAD");
				return LowSkills.get("turn_right_90");
			}
			else {
				return LowSkills.get("rollback");
			}
		}
		else if (distance(movePosition.getX(), movePosition.getY()) <= tesne) {
			if (closeMove) {
				finished = true;
				return LowSkills.get("step_left");
			}
			else {
				agentInfo.loguj("NA MIESTE");
				closeMove = true;
				return LowSkills.get("rollback");
			}
		}
		else {
			agentInfo.loguj("FINAL ELSE");
			return LowSkills.get("rollback");
		}
	}
	
	public boolean rovno() {
		return (rovno1.include(Angles.normalize(movePosition_phi)) || rovno2.include(Angles.normalize(movePosition_phi)));
	}
	
	public boolean vlavo() {
		return vlavo.include(Angles.normalize(movePosition_phi));
	}
	
	public boolean vpravo() {
		return vpravo.include(Angles.normalize(movePosition_phi));
	}
	
	public boolean vlavoZad() {
		return vlavoZad.include(Angles.normalize(movePosition_phi));
	}
	
	public boolean vpravoZad() {
		return vpravoZad.include(Angles.normalize(movePosition_phi));
	}


	public Vector3D computeMidMovePoint() {
		double dist = Math.abs(targetPosition.getX() - ballPosition.getX()) + Math.abs(targetPosition.getY() - ballPosition.getY());
		double dX = (targetPosition.getX() - ballPosition.getX()) / dist;
		double moveX = ballPosition.getX() - dX * odLopty;
		double moveY = ballPosition.getY() - dX * odLopty;
		Vector3D movePositionTmp = Vector3D.cartesian(moveX, moveY, 0);
		Vector3D movePosition = agentModel.relativize(movePositionTmp);
		
		return movePosition;
	}
	
	public Vector3D computeFinalMovePoint() {
		double dist = computeDistance(targetPosition, ballPosition);
		double dX = (targetPosition.getX() - ballPosition.getX()) / dist;
		double dY = (targetPosition.getY() - ballPosition.getY()) / dist;
		double moveX = ballPosition.getX() - dX * kLopte;
		double moveY = ballPosition.getY() - dY * kLopte;
		
		Vector3D movePositionTmp = Vector3D.cartesian(moveX, moveY, 0);
		Vector3D movePosition = agentModel.relativize(movePositionTmp);
		return movePosition;
	}
	
	public double computeDistance(Vector3D pA, Vector3D pB) {
		double distX = pA.getX() - pB.getX();
		double distY = pA.getY() - pB.getY();
		return Math.sqrt(distX * distX + distY * distY);
	}
	
	public double distance(double x, double y) {
		return Math.sqrt(x*x + y*y);
	}
	
	@Override
	public void checkProgress() throws Exception {
	}

}
