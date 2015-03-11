package sk.fiit.jim.annotation.data;

import java.util.ArrayList;

import sk.fiit.jim.agent.models.AgentModel;
import sk.fiit.jim.agent.moves.Joint;


public class MoveValidator {

	/**
	 * Moves list used for validation.
	 */
	ArrayList<Annotation> moves = new ArrayList<Annotation>();
	/**
	 * Shows report in words of validation calculation if conflict is present.
	 */
	String report = new String();
	/**
	 * Validation attribute. True if sequence of moves in list are valid, false otherwise.
	 */
	boolean validity = true;
	
	// metoda umozni skontrolovat sekvenciu zoradeneho zoznamu pohybo
	public void sequenceCheckFull(){
		for(int k=0; k<(moves.size()-1); k++){
			
			// kontrola ci robot lezi 
			if(moves.get(k).end.lying.compareTo(moves.get(k+1).preconditions.lying) != 0){
				validity = false;
				report = report.concat("Conflict in lying: move" + (k+1) + " (end state: " + moves.get(k).end.lying + ") and move" + (k+2) + " (precondition: " + moves.get(k+1).preconditions.lying + ")\n");
			}
			
			// kontrola pozicie lopty voci hracovi
			// rozsah polohy na konci pohybu sa musi vmestit do rozsahu na zaciatku dalsieho pohybu
			if((moves.get(k+1).preconditions.b_pos) && (moves.get(k).end.b_pos)){
				if(!((moves.get(k).end.ball.getX().getMin() >= moves.get(k+1).preconditions.ball.getX().getMin()) &&
					(moves.get(k).end.ball.getX().getMax() <= moves.get(k+1).preconditions.ball.getX().getMax()) &&
					(moves.get(k).end.ball.getY().getMin() >= moves.get(k+1).preconditions.ball.getY().getMin()) &&
					(moves.get(k).end.ball.getY().getMax() <= moves.get(k+1).preconditions.ball.getY().getMax()))){
					validity = false;
					
					if(moves.get(k).end.ball.getX().getMin() < moves.get(k+1).preconditions.ball.getX().getMin()){
						report = report.concat("Conflict in ball position: move" + (k+1) + " (end state minimal x-axis: " + moves.get(k).end.ball.getX().getMin() + ") and move" + (k+2) + " (precondition minimal x-axis: " + moves.get(k+1).preconditions.ball.getX().getMin() + ")\n");
					}
					
					if(moves.get(k).end.ball.getX().getMax() > moves.get(k+1).preconditions.ball.getX().getMax()){
						report = report.concat("Conflict in ball position: move" + (k+1) + " (end state maximal x-axis: " + moves.get(k).end.ball.getX().getMax() + ") and move" + (k+2) + " (precondition maximal x-axis: " + moves.get(k+1).preconditions.ball.getX().getMax() + ")\n");
					}
					
					if(moves.get(k).end.ball.getY().getMin() < moves.get(k+1).preconditions.ball.getY().getMin()){
						report = report.concat("Conflict in ball position: move" + (k+1) + " (end state minimal y-axis: " + moves.get(k).end.ball.getY().getMin() + ") and move" + (k+2) + " (precondition minimal y-axis: " + moves.get(k+1).preconditions.ball.getY().getMin() + ")\n");
					}
					
					if(moves.get(k).end.ball.getY().getMax() > moves.get(k+1).preconditions.ball.getY().getMax()){
						report = report.concat("Conflict in ball position: move" + (k+1) + " (end state maximal y-axis: " + moves.get(k).end.ball.getY().getMax() + ") and move" + (k+2) + " (precondition maximal y-axis: " + moves.get(k+1).preconditions.ball.getY().getMax() + ")\n");
					}
				}
			}
			
			// kontrola ohnutia klbov
			for(int i=0; i<moves.get(k+1).preconditions.joints.size(); i++){
				for(int j=0; j<moves.get(k).end.joints.size(); j++){
					if(moves.get(k+1).preconditions.joints.get(i).name.compareTo(moves.get(k).end.joints.get(j).name) == 0){
						if(moves.get(k+1).preconditions.joints.get(i).value != moves.get(k).end.joints.get(j).value){
							validity = false;
							report = report.concat("Conflict in joint angle: move" + (k+1) + " (end state " + moves.get(k).end.joints.get(j).name + ": " + moves.get(k).end.joints.get(j).value + ") and move" + (k+2) + " (precondition " + moves.get(k+1).preconditions.joints.get(i).name + ": " + moves.get(k+1).preconditions.joints.get(i).value + ")\n");
						}
					}
				}
			}
		}
	}
	
	// staticka metoda umozni kontrolovat sekvenciu dvoch pohybov
	/**
	 * Validation for sequence of two annotation moves. True if the second move can be executed 
	 * after first move execution. False if cannot execute second move after first move. 
	 *
	 * @param first
	 * @param second
	 * @return
	 */
	public static boolean sequenceCheck(Annotation first, Annotation second){
		boolean valid = true;
		
		// kontrola ci robot lezi
		if(first.end.lying.compareTo(second.preconditions.lying) != 0){
			valid = false;
		}
		
		// kontrola pozicie lopty voci hracovi
		// rozsah polohy na konci pohybu sa musi vmestit do rozsahu na zaciatku dalsieho pohybu
		if((second.preconditions.b_pos) && (first.end.b_pos)){
			if(!((first.end.ball.getX().getMin() >= second.preconditions.ball.getX().getMin()) &&
				(first.end.ball.getX().getMax() <= second.preconditions.ball.getX().getMax()) &&
				(first.end.ball.getY().getMin() >= second.preconditions.ball.getY().getMin()) &&
				(first.end.ball.getY().getMax() <= second.preconditions.ball.getY().getMax()))){
				valid = false;
			}
		}
		
		// kontrola ohnutia klbov
		for(int i=0; i<second.preconditions.joints.size(); i++){
			for(int j=0; j<first.end.joints.size(); j++){
				if(second.preconditions.joints.get(i).name.compareTo(first.end.joints.get(j).name) == 0){
					if(second.preconditions.joints.get(i).value != first.end.joints.get(j).value){
						valid = false;
					}
				}
			}
		}
		
		return valid;
	}
	
	//overenie pohybu vzhladom na aktualny stav sveta
	/**
	 * Validation for annotation move. Returns true if required preconditions 
	 * are fulfilled. False if precondition expectations were not met.  
	 *
	 * @param model
	 * @param annotation
	 * @return
	 */
	public static boolean checkAgentState(AgentModel model, Annotation annotation){
		boolean valid = true;
		
		if(annotation.getPrec() == false){
			return true;
		}
		else{
			State preconditions = annotation.getPreconditions();
			
			//overenie, ci je robot v spravnej polohe
			if((preconditions.getLying().compareTo("false") == 0) && !(model.isStanding())){
				valid = false;
			}
			
			if((preconditions.getLying().compareTo("on_back") == 0) && !(model.isLyingOnBack())){
				valid = false;
			}
			
			if((preconditions.getLying().compareTo("on_belly") == 0) && !(model.isLyingOnBelly())){
				valid = false;
			}
			
			if(!(((preconditions.getLying().compareTo("on_side") == 0) && !(model.isOnGround())) && (!model.isLyingOnBack() && !model.isLyingOnBelly()))){
				valid = false;
			}
			
			//overenie, ci je lopta v spravnej polohe
			if(preconditions.isB_pos() == true){
				if((model.getLastDataReceived().ballRelativePosition.getX() < preconditions.getBall().getX().getMin()) || (model.getLastDataReceived().ballRelativePosition.getX() > preconditions.getBall().getX().getMax())){
					valid = false;
				}
				
				if((model.getLastDataReceived().ballRelativePosition.getY() < preconditions.getBall().getY().getMin()) || (model.getLastDataReceived().ballRelativePosition.getY() > preconditions.getBall().getY().getMax())){
					valid = false;
				}
			}
			
			//overenie, ci su klby v spravnych uhloch
			if(preconditions.getJoints().size() > 0){
				for(int i=0; i<preconditions.getJoints().size(); i++){
					for (Joint joint : Joint.values()){
						if(joint.toString().compareTo(preconditions.getJoints().get(i).getName().toUpperCase()) == 0){
							if(model.getJointAngle(joint) != preconditions.getJoints().get(i).getValue()){
								valid = false;
							}
						}
					}
				}
			}
			
			return valid;
		}
	}
	
	public void setMoves(ArrayList<Annotation> moves){
		this.moves = moves;
	}
	
	/**
	 * Adds move to sequence of moves. 
	 *
	 * @param move
	 */
	public void addMove(Annotation move){
		this.moves.add(move);
	}
	
	/**
	 * Returns current validity. 
	 *
	 * @return
	 */
	public boolean getValidity(){
		return this.validity;
	}
	
	/**
	 * Returns report of collisions.  
	 *
	 * @return
	 */
	/**
	 * TODO: Replace with purpose of method. Start with verb. 
	 *
	 * @return
	 */
	public String getReport(){
		return this.report;
	}
	
	/**
	 * Resets values, sets validity to true and report to empty string.
	 *
	 */
	public void reset(){
		this.validity = true;
		this.report = new String();
	}
}
