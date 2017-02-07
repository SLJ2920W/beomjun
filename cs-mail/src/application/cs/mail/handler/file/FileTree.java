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
import java.util.Set;

import application.cs.mail.common.Selection;
import application.cs.mail.handler.search.SearchType;

/**
 * @see http://javapapers.com/java/walk-file-tree-with-java-nio
 * @see http://stackoverflow.com/questions/21733390/directorystream-filter-
 *      example-for-listing-files-that-based-on-certain-date-time
 * @author
 *
 */
public class FileTree {

	private PathMatcher pattern;
	private boolean isSearch = false;
	private String searchText;
	private SearchType searchType;
	private List<FileBean> files = new ArrayList<FileBean>();
	private List<FileBean> folders = new ArrayList<FileBean>();
	private Path home;

	private volatile static FileTree instance;

	public static FileTree getInstance() {
		if (instance == null) {
			synchronized (FileTree.class) {
				if (instance == null) {
					instance = new FileTree();
				}
			}
		}
		return instance;
	}

	/**
	 * 파일 구조를 생성 한다
	 */
	public void createFileTree() {

		// 보여줄 경로가 널이면 기본 홈 아니면 선택값
		Selection section = Selection.getInstance();
		Path sh = section.getDirectory();
		home = sh == null ? Paths.get(section.getSetting().get("home")) : sh;

		// 시스템 기본 루트 디렉토리면 이동 안함 c드라이브 d드라이브 등등
		if (home.getNameCount() == 0) {
			return;
		}

		files.clear();
		folders.clear();

		// 파일 구조 트리 _ 하위 서브 트리 구조 단계 - 검색시 높은 값 으로
		int depth = 1;
		// null로 하면 exception이 나오네
		Set<FileVisitOption> options = Collections.emptySet();

		if (isSearch()) { // 검색어 존재 함
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
//					if (!isSearch()) {
//						// 검색 아닐 경우 상위로 이동 표시
//						folders.add(new FileBean(dir.getParent(), dir.getParent(), attrs, searchType));
//					}

					if (!Files.isHidden(dir))
						return FileVisitResult.CONTINUE;
					return FileVisitResult.SKIP_SUBTREE;
				}

				// 서브 파일&폴더 접근 시
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					try {
						// 기본 필터 확인 후 아래 조건으로
						if (defaultFilter(file, attrs))
							return FileVisitResult.CONTINUE;

						if (Files.isDirectory(file)) { // 폴더인 경우
//							folders.add(new FileBean(home, file, attrs, searchType));
						} else { // 파일인 경우
							// 검색인 경우 검색 필터 확인
							if (filter(file, searchType)) {
								files.add(new FileBean(home, file, attrs, searchType));
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
			long c = Selection.getInstance().getMailViewIgnore().stream().filter((e) -> file.getFileName().startsWith(e)).count();
			if (c == 0) // 제외 대상에 포함 하지 않으면 false
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

	public List<FileBean> getFiles() {
		return files;
	}

	public List<FileBean> getFolders() {
		return folders;
	}

	public void setFiles(List<FileBean> files) {
		this.files = files;
	}

	public void setFolders(List<FileBean> folders) {
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
