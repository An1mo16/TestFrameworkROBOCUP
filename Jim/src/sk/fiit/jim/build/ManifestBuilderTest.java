package sk.fiit.jim.build;

import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

import static org.junit.Assert.assertThat;

/**
 *  ManifestBuilderTest.java
 *
 *@Title	Jim
 *@author	marosurbanec
 */
public class ManifestBuilderTest{
	private Manifest manifest;

	@Before
	public void setup(){
		manifest = new ManifestBuilder().buildManifest();
	}
	
	@Test
	public void mainClass(){
		String mainClass = manifest.getMainAttributes().get(Attributes.Name.MAIN_CLASS).toString();
		assertThat(mainClass, is(equalTo("sk.fiit.jim.init.Main")));
	}
	
	@Test
	public void classPath(){
		String classPath = manifest.getMainAttributes().get(Attributes.Name.CLASS_PATH).toString();
		assertThat(classPath, containsString("jruby"));
		assertThat(classPath, containsString("bsf.jar"));
		assertThat(classPath, containsString("aspectj"));
		assertThat(classPath, containsString("logging"));
		
		assertThat(classPath, not(containsString("junit")));
		assertThat(classPath, not(containsString("mockito")));
		assertThat(classPath, not(containsString("hamcrest")));
	}
}