package application.cs.mail.handler.deprecated;
//package application.cs.mail.handler.file;
//
//import java.io.IOException;
//import java.nio.file.FileSystems;
//import java.nio.file.FileVisitResult;
//import java.nio.file.FileVisitor;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.attribute.BasicFileAttributes;
//import java.util.Scanner;
//import java.util.regex.Pattern;
//
//import application.cs.mail.handler.search.SearchType;
//
//public abstract class FileWalkerTree implements FileVisitor<Path> {
//	
//	private Pattern pattern = FileSystems.getDefault().getPathMatcher("glob:" + "*" + searchText + "*" + ".eml");
//	
//	public FileWalkerTree(String searchText){
//		
//	}
//
//	@Override
//	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	// 기본 필터
//	private boolean defaultFilter(Path file, BasicFileAttributes attrs) throws IOException {
//		// 숨김 파일 제외
//		if (Files.isHidden(file))
//			return true;
//		if (!Files.isDirectory(file)) {
//			// 파일인 경우 확장자 *.eml만 표시
//			if (file.toString().endsWith(".eml"))
//				return false;
//		}
//		if (Files.isDirectory(file)) {
//			// 리스트에 제외할 규칙
//			if (!file.getFileName().startsWith(ignore)) {
//				return false;
//			}
//		}
//		return true;
//	}
//
//	// 검색 필터
//	private boolean filter(Path file, SearchType searchType) throws IOException {
//		switch (searchType) {
//		case NONE:
//			return file.toString().endsWith(".eml");
//		case TITLE:
//			return pattern.matches(file.getFileName());
//		case CONTENT:
//			scanner = new Scanner(file);
//			if (scanner.findWithinHorizon(searchText, 0) != null) {
//				return true;
//			}
//		default:
//			break;
//		}
//		return false;
//
//	}
//
//}
