package sk.fiit.jim.build;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 *  ManifestBuilder.java
 *  
 *  Builds a manifest file, containing 2 vital attributes:
 *		main class: sk.fiit.jim.init.Main
 *		classpath: all libraries in ./lib directory with the exception of
 *				   libraries utilitized in testing
 *
 *@Title	Jim
 *@author	marosurbanec
 */
class ManifestBuilder{

	private StringBuilder contents;

	public ManifestBuilder(){
		contents = new StringBuilder();
	}
	
	public Manifest buildManifest(){
		try{
			appendManifestVersion();
			appendMainClass();
			appendClassPath();
		
			Manifest manifest = new Manifest();
			manifest.read(new ByteArrayInputStream(contents.toString().getBytes("UTF-8")));
			return manifest;
		}
		catch (IOException e){
			return null;
		}
	}

	private void appendManifestVersion(){
		contents.append(Attributes.Name.MANIFEST_VERSION.toString());
		contents.append(": 1.0").append('\n');
	}

	private void appendMainClass(){
		contents.append(Attributes.Name.MAIN_CLASS.toString()).append(": ").append("sk.fiit.jim.init.Main").append('\n');
	}

	private void appendClassPath(){
		contents.append(Attributes.Name.CLASS_PATH.toString()).append(": ");
		for (File lib : new File("./lib").listFiles(new LibsToReleaseFilter())){
			contents.append("./lib/").append(lib.getName()).append(' ');
		}
		contents.append('\n');
	}
}