package sk.fiit.jim;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


import sk.fiit.jim.agent.AgentInfoTest;
import sk.fiit.jim.agent.AgentInfo;
import sk.fiit.jim.agent.ParserToAgentModelIntegrationTest;
import sk.fiit.jim.agent.communication.CommunicationTest;
import sk.fiit.jim.agent.models.AgentModelTest;
import sk.fiit.jim.agent.models.KalmanAdjusterTest;
import sk.fiit.jim.agent.models.TacticalInfoTest;
import sk.fiit.jim.agent.moves.LowSkillTest;
import sk.fiit.jim.agent.parsing.ParserTest;
import sk.fiit.jim.agent.skills.HighSkillTest;
import sk.fiit.jim.build.ClassesToPackTest;
import sk.fiit.jim.build.DirectoryMoverTest;
import sk.fiit.jim.build.JarFileBuilderTest;
import sk.fiit.jim.build.ManifestBuilderTest;
import sk.fiit.jim.init.ScriptTest;
import sk.fiit.jim.init.SkillsFromXmlLoaderTest;
import sk.fiit.jim.log.LogTest;
import sk.fiit.robocup.library.geometry.AnglesTest;
import sk.fiit.robocup.library.geometry.Vector3DTest;
import sk.fiit.robocup.library.math.KalmanTest;
import sk.fiit.robocup.library.math.MathExpressionEvaluatorTest;
import sk.fiit.robocup.library.review.ReviewOk;



/**
 *  AllTests.java
 *  
 *  Executes all tests.
 *
 *@Title	Jim
 *@author	marosurbanec
 *@author	Androids
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
	AnglesTest.class,
	KalmanTest.class,
	Vector3DTest.class,
	LogTest.class,
	ParserTest.class,
	AgentModelTest.class,
	TacticalInfoTest.class,
	SettingsTest.class,
	KalmanAdjusterTest.class,
	ClassesToPackTest.class,
	DirectoryMoverTest.class,
	JarFileBuilderTest.class,
	ManifestBuilderTest.class,
	HighSkillTest.class,
	ParserToAgentModelIntegrationTest.class,
	ScriptTest.class,
	SkillsFromXmlLoaderTest.class,
	LowSkillTest.class,
	MathExpressionEvaluatorTest.class,
	CommunicationTest.class,
	AgentInfoTest.class,
	AgentInfo.class
})
@ReviewOk

public class AllTests{}
