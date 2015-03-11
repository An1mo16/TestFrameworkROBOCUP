package sk.fiit.jim.init;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

import sk.fiit.jim.agent.moves.Joint;
import sk.fiit.jim.agent.moves.LowSkills;
import sk.fiit.jim.agent.moves.Phase;
import sk.fiit.jim.agent.moves.Phases;
import sk.fiit.jim.agent.moves.SkipFlag;
import sk.fiit.jim.agent.moves.SkipFlags;
import sk.fiit.jim.log.Log;
import sk.fiit.jim.log.LogType;

/**
 *  SkillsFromXmlLoaderTest.java
 *  
 *  >>>> @see fixtures/test_moves.xml <<<<
 *
 *@Title	Jim
 *@author	marosurbanec
 */
public class SkillsFromXmlLoaderTest {
	@BeforeClass
	public static void loadXml(){
		try{
			Phases.reset();
			SkipFlags.reset();
			LowSkills.reset();
			new SkillsFromXmlLoader(new File("./moves")).load();
			Log.log(LogType.INIT, "Skills are loaded.");
		}
		catch (Exception e){e.printStackTrace();}
	}
	
	@Test
	public void shouldCreateLowSkills(){
		assertThat("walk_forward", is(equalTo(LowSkills.get("walk_forward").name)));
		assertThat("walk_forward_1", is(equalTo(LowSkills.get("walk_forward").initialPhase)));
	}
	
	@Test
	public void shouldCreateAndPopulatePhases(){
		assertTrue(Phases.exists("walk_forward_4"));
		assertTrue(Phases.exists("walk_forward_2"));
		assertTrue(Phases.exists("walk_forward_9"));
		assertTrue(Phases.exists("walk_forward_12"));
		assertTrue(Phases.exists("walk_forward_5"));
		assertTrue(Phases.exists("walk_forward_13"));
		assertFalse(Phases.get("walk_forward_4").isFinal);
		assertTrue(Phases.get("walk_forward_5").isFinal);
		assertThat(Phases.get("walk_forward_2").next, is("walk_forward_3"));
		assertTrue(Phases.get("walk_forward_4").duration == 0.200);
		assertThat(Phases.get("walk_forward_9").duration, is(closeTo(0.02, 0.22)));
		assertTrue(Phases.get("walk_forward_12").duration == 0.200);
		assertThat(Phases.get("walk_forward_13").finalizationPhase, is(equalTo("walk_forward_1")));
	}
	
	@Test
	public void shouldCreateAndPopulateEffectorTags(){
		assertTrue(Phases.exists("walk_forward_1"));
		assertTrue(Phases.get("walk_forward_1").effectors.size() == 22);
		assertThat(Phases.get("walk_forward_1").effectors.get(0).effector, is(equalTo(Joint.HE1)));
		assertTrue(Phases.get("walk_forward_1").effectors.get(9).endAngle == -60.0);
	}
	
	@Test
	public void shouldCalculateValuesWithConstants(){
		assertTrue(Phases.exists("walk_forward_8"));
		Phase phaseWithConstants = Phases.get("walk_forward_8");
		double endAngle = phaseWithConstants.effectors.get(0).endAngle;
		assertThat(endAngle, is(closeTo(55.0, .1)));
		double endAngle2 = phaseWithConstants.effectors.get(1).endAngle;
		assertThat(endAngle2, is(closeTo(-75, .01)));
	}
	
	@Test
	public void shouldPopulateSkipFlags(){
		assertTrue(Phases.exists("walk_forward_1"));
		Phase basicStancePosition = Phases.get("walk_forward_1");
		assertThat(basicStancePosition.skipIfFlag, not(is(new SkipFlag("inBasicPosition"))));
		assertThat(basicStancePosition.setFlagFalse, is(nullValue()));
		assertThat(basicStancePosition.setFlagTrue, not(is(new SkipFlag("inBasicPosition"))));
	}
}