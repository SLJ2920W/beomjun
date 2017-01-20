package jdk8.test.DuplicationRef;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainApp {

	public static void main(String[] args) {
		Path p = Paths.get("D:");
		System.out.println(p.getNameCount());
		
		File f = new File("D:\\");
		if (f.toPath().getNameCount() == 0)
			System.out.println(f + " is root");
	}

}
