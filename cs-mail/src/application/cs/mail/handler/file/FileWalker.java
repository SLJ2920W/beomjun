package application.cs.mail.handler.file;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Consumer;

import application.cs.mail.common.Selection;
import application.cs.mail.handler.search.SearchType;

/**
 * @see http://javapapers.com/java/walk-file-tree-with-java-nio
 * @see http://stackoverflow.com/questions/21733390/directorystream-filter-
 *      example-for-listing-files-that-based-on-certain-date-time
 * @author
 *
 */
public class FileWalker {

	private PathMatcher pattern;
	private boolean isSearch = false;
	private String searchText;
	private SearchType searchType;
	private List<FileItem> files = new ArrayList<FileItem>();
	private List<FileItem> folders = new ArrayList<FileItem>();
	private Path home;
	// private Scanner scanner;

	public static final FileWalker INSTANCE = new FileWalker();

	public void createFileTree() {
		files.clear();
		folders.clear();

		// 보여줄 경로가 널이면 기본 홈 아니면 선택값
		Selection section = Selection.INSTANCE;
		Path sh = section.getDirectory();
		home = sh == null ? Paths.get(section.getSetting().get("home")) : sh;

		// 파일 구조 트리 _ 하위 서브 트리 구조 단계 - 검색시 높은 값 으로
		int depth = 1;
		// null 하면 exception
		Set<FileVisitOption> options = Collections.emptySet();

		// 검색어 존재 함 필터 처리 temp
		if (isSearch()) {
			depth = 99;
			pattern = FileSystems.getDefault().getPathMatcher("glob:" + "*" + searchText + "*");
			searchType = getSearchType();
		} else {
			searchType = SearchType.NONE;
		}

		try {
			// 현재 폴더 및 서브 디렉토리 포함 탐색
			Files.walkFileTree(home, options, depth, new SimpleFileVisitor<Path>() {
				// 선택한 경로 루트
				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					if (!isSearch()) {
						// 검색 아닐 경우 상위로 이동 표시
						folders.add(new FileItem(dir.getParent(), dir.getParent(), attrs));
					}

					if (!Files.isHidden(dir))
						return FileVisitResult.CONTINUE;
					return FileVisitResult.SKIP_SUBTREE;
				}

				// 서브 파일&폴더 접근 시
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

					try {
						// 기본 필터
						if (defaultFilter(file, attrs))
							return FileVisitResult.CONTINUE;

						if (Files.isDirectory(file)) {
							folders.add(new FileItem(home, file, attrs));
						} else {
							// 검색 필터
							if (filter(file, searchType)) {
								files.add(new FileItem(home, file, attrs));
							}
						}

					} catch (IOException | SecurityException e) {
						e.printStackTrace();
					}

					return FileVisitResult.CONTINUE;
				}
			});

		} catch (IOException | SecurityException e) {
			System.err.println(e.toString());
		}
	}

	// 기본 필터
	private boolean defaultFilter(Path file, BasicFileAttributes attrs) throws IOException {
		// 숨김 파일 제외
		if (Files.isHidden(file))
			return true;
		if (!Files.isDirectory(file)) {
			// 파일인 경우 확장자 *.eml만 표시
			if (file.toString().endsWith(".eml"))
				return false;
		}
		if (Files.isDirectory(file)) {
			// 리스트에 제외할 규칙
			long c = Selection.INSTANCE.getMailViewIgnore().stream().filter((e) -> file.getFileName().startsWith(e)).count();
			if (c == 0)
				return false;
		}
		return true;
	}

	// 검색 필터
	private boolean filter(Path file, SearchType searchType) throws IOException {
		switch (searchType) {
		case TITLE:
			return pattern.matches(file.getFileName());
		// case CONTENT:
		// scanner = new Scanner(file);
		// if (scanner.findWithinHorizon(searchText, 0) != null) {
		// return true;
		// }
		default:
			return true;
		}
	}

	public List<FileItem> getFiles() {
		return files;
	}

	public List<FileItem> getFolders() {
		return folders;
	}

	public void setFiles(List<FileItem> files) {
		this.files = files;
	}

	public void setFolders(List<FileItem> folders) {
		this.folders = folders;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public boolean isSearch() {
		if (searchText != null) {
			if (!searchText.equals("")) {
				return true;
			}
		}
		return isSearch;
	}

	public SearchType getSearchType() {
		return searchType;
	}

	public void setSearchType(SearchType searchType) {
		this.searchType = searchType;
	}

}
