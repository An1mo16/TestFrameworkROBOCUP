/**
 * 
 */
package sk.fiit.jim.agent.skills;

import sk.fiit.jim.agent.models.AgentModel;
import sk.fiit.jim.agent.models.EnvironmentModel;
import sk.fiit.jim.agent.moves.LowSkill;

/**
 * 
 *  ComplexHighSkill.java
 *  
 *@Title        Jim
 *@author       $Author: Bimbo $
 */
public abstract class ComplexHighSkill extends HighSkill implements IComplexHighSkill {

	enum ComplexHighSkillState{
		INIT,EXEC,END;
	}
	
	public ComplexHighSkillState state = ComplexHighSkillState.INIT;
	private HighSkill hs;
	private LowSkill ls;
	
	@Override
	public LowSkill pickLowSkill() {
		
		//By default, complex high skill will end, 
		//when player fall or it is beamable play mode
		//It is in responsibility of planner 
		if(AgentModel.getInstance().isOnGround() || EnvironmentModel.beamablePlayMode()){
			state = ComplexHighSkillState.END;
			return null;
		}
		
		//difference of behavior between INIT and EXEC is very small:
		//INIT -> try to get HighSkill and return its LowSkill
		//EXEC -> try to get HighSkill and return its LowSkill only if returned LowSkill from actual HighSkill is null
		switch (state) {
		case INIT:
			//INIT state may occur only once, at begin
			state = ComplexHighSkillState.EXEC;
			return getLowSkillFromNewHighSkill();
		
		case EXEC:
			ls = hs.pickLowSkill();
			if(ls==null){
				return getLowSkillFromNewHighSkill();
			} else{
				return ls;
			}
		
		case END:
			return null;
		}
		
		return null;
	}
	
	private LowSkill getLowSkillFromNewHighSkill(){
		hs = pickHighSkill();
		if(hs==null){
			//If returned highSkill is null, then complex high skill is at the end
			//This is how does it works -> if you want to end complex high skill,
			//just return null in pickHighSkill method -> null LowSkill will be returned
			//-> highSkill will be ended -> planner will do his work
			state = ComplexHighSkillState.END;
			return null;
		}else{
			//There is some HighSkill to run
			//pick low skill
			ls = hs.pickLowSkill();
			if(ls==null){
				//second possibility of stopping complex high skill is this:
				//when picked highSkill return null lowSkill  
				state = ComplexHighSkillState.END;
				return null;
			} else{
				return ls;	
			}
		}
	}

	/* (non-Javadoc)
	 * @see sk.fiit.jim.agent.skills.IComplexHighSkill#pickHighSkill()
	 */
	public abstract HighSkill pickHighSkill();

	
	/* (non-Javadoc)
	 * @see sk.fiit.jim.agent.skills.IHighSkill#checkProgress()
	 */
	@Override
	public void checkProgress() throws Exception {
		// checkProgress is not used in complex high skill
	}
	
}
