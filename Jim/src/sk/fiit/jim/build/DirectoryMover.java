package sk.fiit.jim.build;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 *  DirectoryMover.java
 *  
 * 	Copies a directory into another one.
 *
 *@Title	Jim
 *@author	marosurbanec
 */
class DirectoryMover{

	private static final FileFilter ALL_FILES = new FileFilter(){
		public boolean accept(File pathname){return true;}
	};
	private final File source;
	private final File target;
	private FileFilter filter;
	
	public DirectoryMover(File source, File target){
		this(source, target, ALL_FILES);
	}
	
	public DirectoryMover(File source, File target, FileFilter filter){
		this.source = source;
		this.target = target;
		this.filter = filter;
	}

	public void move() throws IOException{
		target.mkdirs();
		move(source, target);
	}
	
	private void move(File sourceDir, File targetDir) throws IOException{
		targetDir.mkdirs();
		for (File file : sourceDir.listFiles(filter)){
			if (file.isDirectory()){
				move(file, new File(targetDir, file.getName()));
				continue;
			}
			copyFile(file, new File(targetDir, file.getName()));
		}
	}

	private static void copyFile(File in, File out) throws IOException{
		FileChannel inChannel = new FileInputStream(in).getChannel();
		FileChannel outChannel = new FileOutputStream(out).getChannel();
		try {
			inChannel.transferTo(0, inChannel.size(), outChannel);
		}
		finally {
			if (inChannel != null) inChannel.close();
			if (outChannel != null) outChannel.close();
		}
	}
}