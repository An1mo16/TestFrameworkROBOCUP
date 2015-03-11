package sk.fiit.jim.agent.moves;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *  Phases.java
 *  
 *  Cache for phases.
 *
 *@Title	Jim
 *@author	marosurbanec
 */
public class Phases {

	private Phases(){}
	static private Map<String, Phase> skills = new HashMap<String, Phase>();
	/**
	 * Adds specified Phase to the HashMap. 
	 *
	 * @param phase
	 */
	public static void addPhase(Phase phase){
		if (phase != null)
			skills.put(phase.name, phase);
	}
	/**
	 * Returns true if Phase with specified name exists in HashMap. 
	 *
	 * @param name
	 * @return
	 */
	public static boolean exists(String name){
		if (Phase.EMPTY_PHASE.name.equals(name))
			return true;
		return skills.containsKey(name);
	}
	/**
	 * Returns Phase by specified name. 
	 *
	 * @param name
	 * @return
	 */
	public static Phase get(String name){
		if (Phase.EMPTY_PHASE.name.equals(name))
			return Phase.EMPTY_PHASE;
		return skills.get(name);
	}
	/**
	 * Clears the HashMap of phases. 
	 *
	 */
	public static void reset(){
		skills.clear();
	}
	/**
	 * Returns Collection of all Phases from HashMap. 
	 *
	 * @return
	 */
	public static Collection<Phase> getAll() { return skills.values(); }
	
//------------------------------------------------------------------------------
// Zaciatok kodu, ktory tim 17 nema v kode
//--------------------------------------------------------------------------------	
	
	/**
	 * Computes similarity between two Phases based on positions of
	 * Joints at the and of Phase
	 * 
	 * @param a first phase
	 * @param b second phase
	 * @param similarity maximal allowed difference between phases for one Joint
	 * 
	 * @return true only if there is no Joint, which is more diff
	 */
	public static boolean areSimilarPhases(Phase a,Phase b,double similarity){
		//pre kazdy typ klbu klbu
		for(Joint joint : Joint.values()){
			double valueA;
			double valueB;
			
			//pokial je taky typ klbu zapisany vo faze A, zoberie jeho hodnotu
			//ak nie, tak hodnota je 0
			EffectorData eda = a.getEfectorData(joint.name());
			if(eda ==null){
				valueA = 0;
			}else{
				valueA = eda.endAngle;
			}
			
			EffectorData edb = b.getEfectorData(joint.name());
			if(edb == null){
				valueB = 0;
			}else{
				valueB = edb.endAngle;
			}
			
			if (Math.abs(valueA - valueB) > similarity){
				return false;
			}
		}	
		return true;
	}
	
//------------------------------------------------------------------------------
// Koniec kodu, ktory tim 17 nema v kode
//--------------------------------------------------------------------------------		
}