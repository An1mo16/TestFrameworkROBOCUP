package sk.fiit.jim.build;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.List;

/**
 *  LibsToReleaseFilter.java
 *  
 *  Filters out libraries not used in production release.
 *
 *@Title	Jim
 *@author	marosurbanec
 */
public class LibsToReleaseFilter implements FileFilter{
	private static final List<String> DONT_MOVE_THOSE = 
		Arrays.asList("junit.jar", "mockito-all-1.8.5.jar", "hamcrest-all-1.3.0RC1.jar");
	
	public boolean accept(File lib){
		if (!"lib".equals(lib.getParentFile().getName())) return false;
		return !DONT_MOVE_THOSE.contains(lib.getName());
	}
}