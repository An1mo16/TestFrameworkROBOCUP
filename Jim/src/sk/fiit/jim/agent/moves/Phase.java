package sk.fiit.jim.agent.moves;

import java.util.ArrayList;
import java.util.List;

/**
 *  Phase.java
 *  
 *  A structure encompassing data of a movement phase in a sequence of phases.
 *  Together with other phases it creates a whole LOW SKILL. Thus,
 *  LOW SKILL => PHASE relation is of type 1:n.
 *  
 *@Title	Jim
 *@author	marosurbanec
 */
public class Phase{
	/**
	 * Name of the Phase, has to be unique.
	 */
	public String name;
	/**
	 * Next phase in sequence of move execution. If isFinal is set to true
	 * it is omitted.
	 */
	public String next;
	/**
	 * List of elementary joint moves executed in parallel. 
	 */
	public List<EffectorData> effectors = new ArrayList<EffectorData>();
	/**
	 * Time in miliseconds that the move should take. Rounded to the nearest multiple of 20ms (one time quantum on server).
	 */
	public double duration = 0.0;
	/**
	 * Denotes whether this phase is the last phase of a cycele. If so, the execution of a cycle may be broken.
	 */
	public String finalizationPhase;
	/**
	 * Valid only if isFinal is set to true. Works as next attribute in case a finalization is requested.
	 */
	public boolean isFinal;
	/**
	 * Skips the execution of this phase if a such a flag is set to true.
	 */
	public SkipFlag skipIfFlag;
	/**
	 * LowSkill sets a flag to true AFTER successfully executing this phase.
	 */
	public SkipFlag setFlagTrue;
	/**
	 * LowSkill sets a flag to false PRIOR to executing this phase.
	 */
	public SkipFlag setFlagFalse;
	
	public Phase(){}
	public Phase(String name, double duration){
		this.name = name;
		this.duration = duration;
	}

	public static final Phase EMPTY_PHASE = new Phase(){{
		isFinal = true;
		name = "EMPTY_PHASE";
		next = "EMPTY_PHASE";
		finalizationPhase = "EMPTY_PHASE"; 
		//we can't set 0.0, that would cause ZeroDivisionException 
		duration = 20.0;
	}};
	
	@Override
	public String toString(){
		return name;
	}
	
//------------------------------------------------------------------------------
// Zaciatok kodu, ktory tim 17 nema v kode
//--------------------------------------------------------------------------------	
	//TODO: effectors into HashMap
	public EffectorData getEfectorData(String name){
		for (EffectorData e : effectors){
			if(e.effector.equals(Joint.valueOf(name))){
				return e;
			}
		}
		return null;
	}
//------------------------------------------------------------------------------
// Koniec kodu, ktory tim 17 nema v kode
//--------------------------------------------------------------------------------	
}