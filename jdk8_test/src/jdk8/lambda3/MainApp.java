package jdk8.lambda3;

import java.io.File;
import java.io.FileFilter;
import java.util.Collections;

public class MainApp {

	public static void main(String[] args) {

		File[] fileList = new File(".").listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isFile();
			}
		});

		for (File a : fileList) {
			System.out.println(a.getName());
		}
		
		System.out.println("=============================");
		
		
		File[] f = new File(".").listFiles(File::isFile);
		for (File a : f) {
			System.out.println(a.getName());
		}
		
		System.out.println("=============================");
		
		
		File[] fL = new File(".").listFiles((File f2) -> f2.isFile());
		for (File a : fL) {
			System.out.println(a.getName());
		}
		
	}

}
