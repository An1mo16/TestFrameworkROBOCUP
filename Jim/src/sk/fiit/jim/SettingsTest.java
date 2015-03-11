package sk.fiit.jim;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

/**
 *  SettingsTest.java
 *
 *@Title	Jim
 *@author	marosurbanec
 *@author	Androids
 */
public class SettingsTest{
	
	@Test
	public void setAndPull(){
		Settings.setValue("someKey", true);
		assertTrue(Settings.getBoolean("someKey"));
	}

	@Test
	public void setAndPullDouble(){
		Settings.setValue("otherKey", 5.0);
		assertThat(Settings.getDouble("otherKey"), is(5.0));
	}
	
	@Test(expected=java.lang.IllegalStateException.class)
	public void retrieveNotExistingKey(){
		Settings.getBoolean("thisIsSurelyNotSet");
	}
}