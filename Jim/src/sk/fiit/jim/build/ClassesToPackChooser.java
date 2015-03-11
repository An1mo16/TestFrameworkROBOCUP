package sk.fiit.jim.build;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 *  ClassesToPackChooser.java
 *
 *	Desides which classes are to be packed into a jar and which are filtered.
 *
 *@Title	Jim
 *@author	marosurbanec
 */
class ClassesToPackChooser{

	private static final FileFilter filter = new FileFilter(){
		public boolean accept(File file){
			if (!file.getName().endsWith(".class") && !file.isDirectory()) return false;
			if (file.getName().contains("Test")) return false;
			if ("build".equals(file.getParentFile().getName())) return false;
			if ("code_review".equals(file.getParentFile().getName())) return false;
			return true;
		}
	};
	private File rootDirectory;

	public ClassesToPackChooser(File parent){
		this.rootDirectory = parent;
	}

	public List<File> getAllClasses(){
		List<File> classesStorage = new ArrayList<File>();
		addRecursively(rootDirectory, classesStorage);
		return classesStorage;
	}

	private void addRecursively(File parent, List<File> classesStorage){
		for (File child : parent.listFiles(filter)){
			if (child.isDirectory()){
				addRecursively(child, classesStorage);
				continue;
			}
			classesStorage.add(child);
		}
	}
}