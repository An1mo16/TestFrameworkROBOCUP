package sk.fiit.jim.build;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

/**
 *  JarBuilder.java
 *  
 *  Is responsible for transferring given .class files into JarEntries.
 *
 *@Title	Jim
 *@author	marosurbanec
 *@author	Androids
 */
class JarBuilder{

	private final JarOutputStream out;

	public JarBuilder(JarOutputStream out){
		this.out = out;
	}

	public void addFiles(List<File> allClasses) throws IOException{
		for (File file : allClasses){
			out.putNextEntry(toZipEntry(file));
			copyStream(new FileInputStream(file));
		}
	}

	private void copyStream(FileInputStream is) throws IOException{
		byte[] buffer = new byte[1024];
		int read = 0;
		while ((read = is.read(buffer)) != -1){
			out.write(buffer, 0, read);
		}
		out.closeEntry();
		is.close();
	}

	ZipEntry toZipEntry(File file){
		// a zip entry /sk/fiit/... can't be found by class loader
		// it oughts to be sk/fiit/...
		String fileName = file.getAbsolutePath().replace(new File("./bin").getAbsolutePath(), "");
		fileName = fileName.substring(1);
		fileName = fileName.replace(File.separatorChar, '/');
		return new ZipEntry(fileName);
	}
}