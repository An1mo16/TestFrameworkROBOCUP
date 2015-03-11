package sk.fiit.jim.build;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

import static org.hamcrest.Matchers.greaterThan;

import static org.junit.Assert.assertThat;

/**
 *  ClassesToPackTest.java
 *
 *@Title	Jim
 *@author	marosurbanec
 */
public class ClassesToPackTest{
	
	ClassesToPackChooser chooser;
	List<File> classes;
	
	@Before
	public void setup(){
		chooser = new ClassesToPackChooser(new File("./bin"));
		classes = chooser.getAllClasses();
	}
	
	@Test
	public void moreThanOneFile(){
		assertThat(classes.size(), is(greaterThan(0)));
	}
	
	@Test
	public void noTests(){
		for (File clazz : classes)
			assertThat(clazz.getName(), not(containsString("Test")));
	}
	
	@Test
	public void onlyClasses(){
		for (File clazz : classes)
			assertThat(clazz.getName(), endsWith(".class"));
	}
	
	@Test
	public void noBuildClasses(){
		for (File clazz : classes)
			assertThat(clazz.getParentFile().getName(), not(equalTo("build")));
	}
}