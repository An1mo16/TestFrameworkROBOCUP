package sk.fiit.jim.highskills;

import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.jim.agent.models.AgentModel;
import sk.fiit.jim.agent.moves.LowSkill;
import sk.fiit.jim.agent.moves.LowSkills;
import sk.fiit.jim.agent.skills.HighSkill;
import sk.fiit.jim.agent.trajectory.TrajectoryRealTime;

/**
 * Otazne je ci by high skill nemal pouzivat Vector3D, 
 * Z povodnej implementacie to nie je jasne a nikde sa tento high skill nepouziva
 * Reimplemented from Ruby to Java by Filip Blanarik
 */

public class Trajectory extends HighSkill {

	private double x;
	private double y;
	private double z;
	private double phi;
	
	public Trajectory(double x, double y, double z, double phi) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.phi = phi;		
	}
	
	
	@Override
	public LowSkill pickLowSkill() {
		
		AgentInfo agentInfo = AgentInfo.getInstance();
		AgentModel agentModel = AgentModel.getInstance();
		TrajectoryRealTime tp = TrajectoryRealTime.getInstance();
		
		tp.plan(x, y, z, phi);
		
		if(agentModel.isOnGround()) {
			
				agentInfo.loguj("Fallen");
				//provizorne riesenie
				//tu by sa mal skill ukoncit, pretoze pri pade sa moze agent vychylit z trajektorie
				if(agentModel.isLyingOnBack())	{
					return LowSkills.get("stand_back");	
				} else if (agentModel.isLyingOnBelly())	{
					return LowSkills.get("stand_front");
				} else {		
					return null;
				}
		} else {
				agentInfo.loguj("abc :" + tp.peekName().toString() );
				
				if(tp.peekName().equalsIgnoreCase("null")) {
					return null;
				}
				else {
					return LowSkills.get(tp.getMoveName());
				}
		}

	}
    
    @Override
    public void checkProgress() {
    }
}
