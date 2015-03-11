package sk.fiit.jim.decision.situation;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class SituationManagerTest {
	private SituationManager situationManager = new SituationManager();

	@Before
	public void setup() {
		ArrayList<ISituation> SituationsMock = new ArrayList<ISituation>();

		// / Create Mock For FightForBall
		FightForBall fightForBall = mock(FightForBall.class);
		when(fightForBall.checkSituation()).thenReturn(true);
		SituationsMock.add(fightForBall);

		// / Create Mock For FarFromEnemyGoal
		FarFromEnemyGoal farFromEnemyGoal = mock(FarFromEnemyGoal.class);
		when(farFromEnemyGoal.checkSituation()).thenReturn(true);
		SituationsMock.add(farFromEnemyGoal);
		situationManager.setSituations(SituationsMock);

	}

	@Test
	public void TestSituationManager() throws Exception {

		// check if FightForBall and FarFromEnemyGoal is in current list situation
		Assert.assertEquals(true, situationManager.getListOfCurrentSituations()
				.get(0).equals("Mock for FightForBall"));
		Assert.assertEquals(true, situationManager.getListOfCurrentSituations()
				.get(1).equals("Mock for FarFromEnemyGoal"));
	}

}
