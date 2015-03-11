package sk.fiit.jim.highskills;

import sk.fiit.jim.LambdaCallable;
import sk.fiit.jim.agent.models.TacticalInfo;
import sk.fiit.robocup.library.geometry.Vector3D;

/*
 * Helper class (not high skill) with method getHighSkillToGoToFormation,
 * which returns high skill providing to move player to his position in formation.
 * 
 * @author: xsuchac
 * @author: A55-Kickers
 */
public class FormationHelper {
	public GoToPosition getHighSkillToGoToFormation(Vector3D targetPosition, LambdaCallable validityProc){
		TacticalInfo tacticalInfo = TacticalInfo.getInstance();
		Vector3D target_position = tacticalInfo.getFormPositionGlobal();
		
		return new GoToPosition(targetPosition, validityProc);
	}
}
