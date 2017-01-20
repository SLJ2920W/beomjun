package jdk8.nio;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Pathss {

	public static void main(String[] args) {

		Path p1 = Paths.get("D:\\DE565WPZ.E15");
		Path p3 = Paths.get("D:\\Hanwha\\0. 프로그램\\MSOffice_2013");
		// Result is sally/bar
		Path p1_to_p3 = p1.relativize(p3);
		System.out.println(p1_to_p3);
		// Result is ../..
		Path p3_to_p1 = p3.relativize(p1);
		System.out.println(p3_to_p1);
	}

}
