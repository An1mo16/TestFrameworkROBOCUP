package sk.fiit.testframework.annotator.serialization;

import java.util.ArrayList;

/** 
 * @author: Roman Bilevic
 */

public class MoveValidator {

	ArrayList<Annotation> moves = new ArrayList<Annotation>();
	String report = new String();
	boolean validity = true;
	
	// metoda umozni skontrolovat serializaciu zoradeneho zoznamu pohybo
	public void serializationCheckFull(){
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
	
	// staticka metoda umozni kontrolovat serializaciu dvoch pohybov
	public static boolean serializationCheck(Annotation first, Annotation second){
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
	
	public void setMoves(ArrayList<Annotation> moves){
		this.moves = moves;
	}
	
	public void addMove(Annotation move){
		this.moves.add(move);
	}
	
	public boolean getValidity(){
		return this.validity;
	}
	
	public String getReport(){
		return this.report;
	}
}
