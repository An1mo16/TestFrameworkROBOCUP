package sk.fiit.jim.build;

import java.io.File;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 *  DirectoryMoverTest.java
 *
 *@Title	Jim
 *@author	marosurbanec
 */
public class DirectoryMoverTest{
	@Test
	public void testMoveFixtures(){
		File target = new File("./directory_move");
		File source = new File("./fixtures");
		DirectoryMover mover = new DirectoryMover(source, target);
		try{
			mover.move();
		}
		catch (Exception e){e.printStackTrace();assertTrue(false);}
		for (File file : target.listFiles())
			assertTrue(new File(source, file.getName()).exists());
		deleteDirectory(target);
	}

	void deleteDirectory(File parent) {
		for (File file : parent.listFiles()){
			if(file.isDirectory()) {
				deleteDirectory(file);
				continue;
			}
			file.delete();
		}
		parent.delete();
	}
}