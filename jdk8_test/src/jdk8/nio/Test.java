package jdk8.nio;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.spi.FileTypeDetector;

public class Test {

	public static void main(String[] args) throws IOException {
//		DirectoryStream.Filter<Path> filter = new DirectoryStream.Filter<Path>() {
//	        @Override
//	        public boolean accept(Path file) throws IOException {
//	            return (Files.isDirectory(file));
//	        }
//	    };
	    
		DirectoryStream.Filter<Path> filter2 = (Path file) -> {
			return file.toString().endsWith(".eml") && !Files.isDirectory(file);
		};
		
	    Path dir = FileSystems.getDefault().getPath("D:\\Hanwha\\매일 백업\\16060801@hanwha.com_받은 편지함_2016-12-18");
	    
	    DirectoryStream<Path> s = Files.newDirectoryStream(dir, filter2);
	    s.forEach(sdf ->{
	    	System.out.println(sdf.getFileName().toString());
	    	if(sdf.toString().endsWith(".eml")){
	    		System.out.println(sdf.getFileName().toString());
	    	}
	    });
	    
	    
	    
		
	    /*
	    try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, filter)) {
	        for (Path path : stream) {
	            System.out.println(path.getFileName());
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }*/
	}

}
