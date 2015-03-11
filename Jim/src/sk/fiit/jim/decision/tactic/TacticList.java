package sk.fiit.jim.decision.tactic;

import java.util.ArrayList;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

public class TacticList {
    public static String[] tacticsNames = {
            AttackLeft.class.getSimpleName(),
            AttackMid.class.getSimpleName(),
            AttackRight.class.getSimpleName(),
            Goalie.class.getSimpleName(),
    };

    public static void intitializeTactics(){
    	 Reflections reflections = new Reflections("sk.fiit.jim.decision.tactic",new SubTypesScanner() );

         Set<Class<? extends ITactic>> subTypes = 
                   reflections.getSubTypesOf(ITactic.class);
         
         System.out.println(subTypes.size());

    }
    
    
    //Todo #Task(Dynamic loading of Tactic objects) #Priority(Low) | vladimir.bosiak@gmail.com 2013-11-26T15:44:21.9640000Z
    public static ArrayList<ITactic> getTacticsList() {
    	intitializeTactics();
    	
        ArrayList<ITactic> tactics = new ArrayList<ITactic>();
        for (String tactic : TacticList.tacticsNames) {
            try {
                tactics.add(TacticsFactory.createTactic(tactic));
            } catch (Exception e) {
                //TODO

            }
        }
        return tactics;

    }

}
