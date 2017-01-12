package application.cs.mail.handler.search;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import application.cs.mail.common.Selection;
import application.util.MimeUtils;
import javafx.concurrent.Task;

/**
 * <ul>
 * <li>어플 실행시 선택한 폴더 하위 전부 방문하여 변환 하지 않은 파일들은 변환 한다</li>
 * <li>EML -> HTML 변환 스레드 방식 폴더 변경 및 선택 시 1회 실행 한다.</li>
 * <li>http://palpit.tistory.com/773</li>
 * </ul>
 * 
 * <pre>
 * <code>
 * </code>
 * </pre>
 */
public class HanwhaEml extends Task<Queue<String>> {

	@Override
	protected Queue<String> call() throws Exception {
		Path _sp = Selection.INSTANCE.getDirectory();
		Queue<String> result = null;
		try {
			result = indexDocs(_sp);
		} catch (Exception e) {

		}
		return result;
	}

	Queue<String> indexDocs(Path path) throws IOException {
		Queue<String> result = new ConcurrentLinkedQueue<String>();
		updateMessage(" ");
		if (Files.isDirectory(path)) {

			Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					try {
						// HTML 변경 허용 할 규칙
						if (filter(file)) {
							// 그룹웨어에서 사용 하는 EML -> HTML 변경
							updateMessage("변환 중.. "+file.getFileName());
							MimeUtils.decodeLocalForSearch(file.toFile());
						}
					} catch (Exception e) {
						result.add(file.toFile().toString());
						System.out.println("변환 실패 = " + file.toFile());
					}
					return FileVisitResult.CONTINUE;
				}
			});
		} else {
			return result;
		}
		updateMessage("변경 작업 끝");
		return result;
	}

	// 파싱 제외할 규칙 tmp 이름으로 시작 하는 폴더, 파일인 경우 같은 파일 이름 확장자 htm이 아닌 경우
	private boolean filter(Path file) {
		if (Files.isDirectory(file)) {
			long c = Selection.INSTANCE.getMailViewIgnore().stream().filter((e) -> file.getFileName().startsWith(e)).count();
			if (c == 0)
				return true;
		} else if (file.toString().endsWith(".eml")) {
			boolean f = Files.exists(Paths.get(file.toString().replaceAll(".eml", ".htm")));
			if (!f)
				return true;
		}
		return false;

	}

}
