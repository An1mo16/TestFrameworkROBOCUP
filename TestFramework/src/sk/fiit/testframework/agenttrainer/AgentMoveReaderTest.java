package sk.fiit.testframework.agenttrainer;

import org.junit.Test;


import sk.fiit.testframework.agenttrainer.models.*;

public class AgentMoveReaderTest {

	@Test
	public void test1() throws Exception {
//		AgentMoveReader reader = new AgentMoveReader();
//		AgentMove agentMove = reader
//				.read("C:\\Projects\\Internal\\TP\\Jim\\moves\\fall_left.xml");
//
//		agentMove.getPhases().get(0).setDuration(150);
//		AgentMoveWriter writer = new AgentMoveWriter();
//		writer.write(agentMove,"C:\\Projects\\fall_left.xml");
		
		AgentMoveConfigReader reader = new AgentMoveConfigReader();
		AgentMoveConfiguration config = reader.read("C:\\Projects\\Internal\\TP\\TestFramework\\config\\walk_fine_fast1_config2.xml");
	}
}
