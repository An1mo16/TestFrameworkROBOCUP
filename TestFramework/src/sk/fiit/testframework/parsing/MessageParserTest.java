package sk.fiit.testframework.parsing;


import junit.framework.Assert;

import org.junit.*;

import sk.fiit.testframework.parsing.models.*;
import sk.fiit.testframework.parsing.models.messages.*;

public class MessageParserTest {
	
	private String enviromentTestMessage = "((FieldLength 18)(FieldWidth 12)(FieldHeight 40)(GoalWidth 2.1)(GoalDepth 0.6)(GoalHeight 0.8)(FreeKickDistance 1.3)(WaitBeforeKickOff 2)(AgentRadius 0.4)(BallRadius 0.042)(BallMass 0.026)(RuleGoalPauseTime 3)(RuleKickInPauseTime 1)(RuleHalfTime 300)(play_modes BeforeKickOff KickOff_Left KickOff_Right PlayOn KickIn_Left KickIn_Right corner_kick_left corner_kick_right goal_kick_left goal_kick_right offside_left offside_right GameOver Goal_Left Goal_Right free_kick_left free_kick_right))(RSG 0 1)((nd TRF (SLT 0.984808 -0.173648 -4.91926e-010 0 0.17101 0.969846 -0.173648 0 0.0301537 0.17101 0.984808 0 0 -4 0.6 1))(nd TRF (SLT 1 0 0 0 0 1 0 0 0 0 1 0 -10 10 10 1)(nd Light (setDiffuse 1 1 1 1) (setAmbient 0.8 0.8 0.8 1) (setSpecular 0.1 0.1 0.1 1)))(nd TRF (SLT 1 0 0 0 0 1 0 0 0 0 1 0 10 -10 10 1)(nd Light (setDiffuse 1 1 1 1) (setAmbient 0 0 0 1) (setSpecular 0.1 0.1 0.1 1)))(nd TRF (SLT -1 -8.74228e-008 -3.82137e-015 0 0 -4.37114e-008 1 0 -8.74228e-008 1 4.37114e-008 -0 0 0 0 1)(nd StaticMesh (load models/naosoccerfield.obj ) (sSc 1.5 1 1.5)(resetMaterials None_rcs-naofield.png)))(nd TRF (SLT -1 -8.74228e-008 -3.82137e-015 0 0 -4.37114e-008 1 0 -8.74228e-008 1 4.37114e-008 -0 0 0 0 1)(nd StaticMesh (load models/skybox.obj ) (sSc 10 10 10)(resetMaterials Material_skyrender_0001.tif Material_skyrender_0002.tif Material_skyrender_0003.tif Material_skyrender_0004.tif Material_skyrender_0005.tif Material_skyrender_0006.tif)))(nd TRF (SLT 1 0 0 0 0 1 0 0 0 0 1 0 -9.3 0 0.4 1)(nd TRF (SLT -4.37114e-008 1 4.37114e-008 0 0 -4.37114e-008 1 0 1 4.37114e-008 1.91069e-015 0 0.3 0 -0.4 1)(nd StaticMesh (setTransparent) (load models/leftgoal.obj) (sSc 2.18 0.88 0.68)(resetMaterials grey_naogoalnet.png yellow)))(nd TRF (SLT 1 0 0 0 0 1 0 0 0 0 1 0 0 -1.07 0 1))(nd TRF (SLT 1 0 0 0 0 1 0 0 0 0 1 0 0 1.07 0 1))(nd TRF (SLT 0.866025 0 -0.5 0 0 1 0 0 0.5 0 0.866025 0 -0.06 0 0 1))(nd TRF (SLT 1 0 0 0 0 1 0 0 0 0 1 0 0.3 1.05 0.4 1))(nd TRF (SLT 1 0 0 0 0 1 0 0 0 0 1 0 0.3 -1.05 0.4 1)))(nd TRF (SLT -1 -8.74228e-008 -0 -0 8.74228e-008 -1 0 0 0 0 1 0 9.3 0 0.4 1)(nd TRF (SLT -4.37114e-008 1 4.37114e-008 0 0 -4.37114e-008 1 0 1 4.37114e-008 1.91069e-015 0 0.3 0 -0.4 1)(nd StaticMesh (setTransparent) (load models/rightgoal.obj) (sSc 2.18 0.88 0.68)(resetMaterials grey_naogoalnet.png sky-blue white)))(nd TRF (SLT 1 0 0 0 0 1 0 0 0 0 1 0 0 -1.07 0 1))(nd TRF (SLT 1 0 0 0 0 1 0 0 0 0 1 0 0 1.07 0 1))(nd TRF (SLT 0.866025 0 -0.5 0 0 1 0 0 0.5 0 0.866025 0 -0.06 0 0 1))(nd TRF (SLT 1 0 0 0 0 1 0 0 0 0 1 0 0.3 1.05 0.4 1))(nd TRF (SLT 1 0 0 0 0 1 0 0 0 0 1 0 0.3 -1.05 0.4 1)))(nd TRF (SLT 1 0 0 0 0 1 0 0 0 0 1 0 -14.994 0 0 1)(nd SMN (load StdUnitBox) (sSc 1 31 1) (sMat matGrey)))(nd TRF (SLT 1 0 0 0 0 1 0 0 0 0 1 0 14.994 0 0 1)(nd SMN (load StdUnitBox) (sSc 1 31 1) (sMat matGrey)))(nd TRF (SLT 1 0 0 0 0 1 0 0 0 0 1 0 0 15 0 1)(nd SMN (load StdUnitBox) (sSc 30.988 1 1) (sMat matGrey)))(nd TRF (SLT 1 0 0 0 0 1 0 0 0 0 1 0 0 -15 0 1)(nd SMN (load StdUnitBox) (sSc 30.988 1 1) (sMat matGrey)))(nd TRF (SLT 1 0 0 0 0 1 0 0 0 0 1 0 -9 6 0 1))(nd TRF (SLT 1 0 0 0 0 1 0 0 0 0 1 0 -9 -6 0 1))(nd TRF (SLT 1 0 0 0 0 1 0 0 0 0 1 0 9 6 0 1))(nd TRF (SLT 1 0 0 0 0 1 0 0 0 0 1 0 9 -6 0 1))(nd TRF (SLT 1 0 0 0 0 1 0 0 0 0 1 0 0 0 0.0402764 1)(nd StaticMesh (load models/soccerball.obj ) (sSc 0.042 0.042 0.042)(resetMaterials soccerball_rcs-soccerball.png))))";
	
	@Before
	public void setup() {
	
	}
	
	@Test
	public void environmentMessageParserTest() {
		EnvironmentMessageParser parser = new EnvironmentMessageParser();
		
		Message message = parser.parse(enviromentTestMessage);
		
		EnvironmentPart envPart = ((EnvironmentMessage) message).getEnvironmentPart();
		
		Assert.assertEquals(((double)18), envPart.getFieldLength());
		Assert.assertEquals(((double)12), envPart.getFieldWidth());
		Assert.assertEquals(((double)40), envPart.getFieldHeight());
		Assert.assertEquals(((double)2.1), envPart.getGoalWidth());
		Assert.assertEquals(((double)0.6), envPart.getGoalDepth());
		Assert.assertEquals(((double)0.8), envPart.getGoalHeight());
		Assert.assertEquals(((double)1.3), envPart.getFreeKickDistance());
		Assert.assertEquals(((double)2), envPart.getWaitBeforeKickOff());
		Assert.assertEquals(((double)0.4), envPart.getAgentRadius());
		Assert.assertEquals(((double)0.042), envPart.getBallRadius());
		Assert.assertEquals(((double)0.026), envPart.getBallMass());
		Assert.assertEquals(((double)3), envPart.getRuleGoalPauseTime());
		Assert.assertEquals(((double)1), envPart.getRuleKickInPauseTime());
		Assert.assertEquals(((double)300), envPart.getRuleHalfTime());
		Assert.assertEquals(17,envPart.getPlayModes().length);
		
		SceneGraphHeaderPart sceneGraphHeaderPart = ((EnvironmentMessage) message).getSceneGraphHeaderPart();
		Assert.assertEquals(SceneGraphType.RSG, sceneGraphHeaderPart.getSceneGraphType());
		Assert.assertEquals(0,sceneGraphHeaderPart.getSceneGraphMainVersion());
		Assert.assertEquals(1,sceneGraphHeaderPart.getSceneGraphSubVersion());
	}
}
