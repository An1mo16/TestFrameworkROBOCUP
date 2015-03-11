package sk.fiit.jim.agent.skills;

import sk.fiit.jim.agent.moves.LowSkill;

/**
 *  FakeHighSkill.java
 *  
 *  High skill created for testing purposes only. Should not be instantiated
 *  outside of a test case.
 *
 *@Title	Jim
 *@author	marosurbanec
 *@author	Androids
 */
public class FakeHighSkill extends HighSkill{

	public boolean shouldThrowException;
	public LowSkill skillToChoose;

	@Override
	public void checkProgress() throws Exception{
		if (shouldThrowException)
			throw new Exception("thrown");
	}

	@Override
	public LowSkill pickLowSkill(){
		return skillToChoose;
	}	
}