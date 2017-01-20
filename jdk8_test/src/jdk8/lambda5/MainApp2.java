package jdk8.lambda5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class MainApp2 {

	public static void main(String[] args) {
		// [s] file stream
		try {
			// modern
//			Path file = Paths.get("C://dev//jdk1.8.0_92_sample//sample//lambda//DefaultMethods//SimplestUsage.java");
//			Stream<String> fileStream = null;
//			fileStream = Files.lines(file, Charset.defaultCharset());
//			fileStream.forEach(System.out::println);
//			fileStream.close();
//			System.out.println();
//			
//			// classic
//			File file2 = new File("C://dev//jdk1.8.0_92_sample//sample//lambda//DefaultMethods//SimplestUsage.java");
//			BufferedReader bRead = new BufferedReader(new FileReader(file2));
//			String str = "";
//			while ((str = bRead.readLine()) != null) {
//
//				System.out.println(str);
//			}
//			bRead.close();
			
			Path path = Paths.get("D:\\새 폴더");
			Stream<Path> dirList = Files.list(path);
			dirList.forEach(System.out::println);
			
			dirList.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		// [e] file stream

	}

}
