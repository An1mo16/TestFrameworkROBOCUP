package sk.fiit.jim.init;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.bsf.util.IOUtils;
import org.junit.Test;

import sk.fiit.robocup.library.init.Script;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 *  ScriptTest.java
 *
 *@Title	Jim
 *@author	marosurbanec
 */
public class ScriptTest{
	
	public ScriptTest toBeSetByScript = null;
	public String inherited1 = "notNull";
	public String inherited2 = null;
	
	@Test
	public void javaBridging(){
		List<String> toBeEmptied = new ArrayList<String>();
		toBeEmptied.add("something");
		Script script = Script.createScriptFrom("./fixtures/test_script.rb");
		script.registerBean("toBeEmptied", toBeEmptied);
		script.execute();
		assertTrue(toBeEmptied.isEmpty());
	}
	
	@Test
	public void performance() throws FileNotFoundException, IOException{
		//one meaningless script to offshore the costs of start-up
		String script = IOUtils.getStringFromReader(new FileReader("./fixtures/sophisticated_script.rb"));
		Script.createScript(script).execute();
		
		long start = System.nanoTime();
		Script script2 = Script.createScript("Something.new(5,6).y**Something.new(4,5).x");
		for (int i = 0; i < 100 ; i++){
			script2.execute();
		}
		long end = System.nanoTime();
		assertTrue(String.format("100 script executions took %d", end - start), end - start < 400000000);
	}
	
	@Test
	public void testInheritance(){
		ScriptTest test = new ScriptTest();
		Script script = Script.createScriptFrom("./fixtures/inheritance_test.rb");
		script.registerBean("script", test);
		script.execute();
		assertThat(test.toBeSetByScript, is(notNullValue()));
		assertThat(test.toBeSetByScript.inherited2, is(equalTo(test.toBeSetByScript.inherited1)));
	}
}