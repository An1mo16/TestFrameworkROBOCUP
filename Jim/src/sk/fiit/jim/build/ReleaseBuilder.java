package sk.fiit.jim.build;

import java.io.*;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

/**
 *  BuildRelease.java
 *  
 * 	Makes use of classes in this package to create a deployable application,
 * 	currently in ./release directory.
 *
 *@Title	Jim
 *@author	marosurbanec
 */
public class ReleaseBuilder{
	private final File targetDirectory;

	public ReleaseBuilder(File targetDirectory){
		this.targetDirectory = targetDirectory;
	}

	public static void main(String[] args) throws FileNotFoundException, IOException{
		new ReleaseBuilder(new File("./release")).build();
	}

	private void build() throws IOException{
		targetDirectory.mkdirs();
		buildJarFile();
		moveScripts();
		moveLibs();
		moveMoves();
		System.out.println("Build successful");
	}

	private void buildJarFile() throws IOException, FileNotFoundException{
		Manifest manifest = new ManifestBuilder().buildManifest();
		JarOutputStream out = new JarOutputStream(new FileOutputStream(new File(targetDirectory, "release.jar")), manifest);
		new JarBuilder(out).addFiles(new ClassesToPackChooser(new File("./bin")).getAllClasses());
		out.finish();
		out.close();
	}
	
	private void moveScripts() throws IOException{
		new DirectoryMover(new File("./scripts"), new File(targetDirectory, "scripts")).move();
	}
	
	private void moveLibs() throws IOException{
		new DirectoryMover(new File("./lib"), new File(targetDirectory, "lib"), new LibsToReleaseFilter()).move();
	}
	
	private void moveMoves() throws IOException{
		new DirectoryMover(new File("./moves"), new File(targetDirectory, "moves")).move();
	}
}