package sk.fiit.jim.build;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.*;

/**
 *  JarFileBuilderTest.java
 *
 *@Title	Jim
 *@author	marosurbanec
 */
public class JarFileBuilderTest{
	private JarOutputStream out;
	private JarBuilder builder;

	@Before
	public void setup(){
		out = mock(JarOutputStream.class);
		builder = new JarBuilder(out);
	}
	
	@Test
	public void manifestAdded(){
		List<File> allClasses = new ClassesToPackChooser(new File("./bin")).getAllClasses();
		try{
			builder.addFiles(allClasses);
			verify(out, times(allClasses.size())).putNextEntry(any(ZipEntry.class));
			verify(out, times(allClasses.size())).closeEntry();
			verify(out, atLeast(allClasses.size())).write(any(byte[].class), eq(0), anyInt());
		}
		catch (IOException e){assertTrue(false);}
	}
}