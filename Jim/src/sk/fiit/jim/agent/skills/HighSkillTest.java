package sk.fiit.jim.agent.skills;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

import sk.fiit.jim.agent.models.EnvironmentModel;
import sk.fiit.jim.agent.moves.LowSkill;
import sk.fiit.jim.agent.moves.Phase;
import sk.fiit.jim.agent.moves.Phases;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 *  HighSkillTest.java
 *
 *@Title	Jim
 *@author	marosurbanec
 */
public class HighSkillTest{
	
	private FakeHighSkill highSkill;

	@Before
	public void setup(){
		highSkill = new FakeHighSkill();
	}
	
	@Test
	public void construction(){
		assertFalse(highSkill.isEnded());
	}
	
	@Test
	public void noSkill(){
		executeHighSkill();
		assertTrue(highSkill.isEnded());
	}

	private void executeHighSkill(){
		try{
			highSkill.execute();
		}
		catch (Exception e){assertTrue(false);}
	}
	
	@Test
	public void oneSkill(){
		LowSkill turnOnce = new LowSkill("turnOnce", "turnOncePhase");
		Phase phase = new Phase("turnOncePhase", 0.100);
		phase.finalizationPhase = "finalizationPhase";
		phase.isFinal = true;
		Phases.addPhase(phase);
		
		Phase finalizationPhase = new Phase("finalizationPhase", 0.100);
		finalizationPhase.isFinal = true;
		Phases.addPhase(finalizationPhase);
		
		highSkill.skillToChoose = turnOnce;
		executeHighSkill();
		assertThat(highSkill.currentSkill, is(turnOnce));
		
		EnvironmentModel.SIMULATION_TIME += 0.120;
		highSkill.skillToChoose = null;
		executeHighSkill();
		assertFalse(highSkill.isEnded());
		
		EnvironmentModel.SIMULATION_TIME += .100;
		executeHighSkill();
		assertTrue(highSkill.isEnded());
	}
}