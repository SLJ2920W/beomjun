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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.cs.mail.Main;
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
public class TaskChangeToHtml extends Task<Queue<String>> {
	
	private static final Logger log = LoggerFactory.getLogger(Main.class);

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
		updateMessage("변경 내역 확인중");
		if (Files.isDirectory(path)) {

			Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					try {
						// HTML 변경 허용 할 규칙
						if (filter(file)) {
							// 그룹웨어에서 사용 하는 EML -> HTML 변경
							updateMessage("변환 중.. "+file.getFileName());
//							log.info("성공 : {}" + file.toFile());
							MimeUtils.decodeLocalForSearch(file.toFile());
						} else{
//							log.info("확인중 : {}" + file.toFile());
							updateMessage("확인 중.. "+file.getFileName());
						}
					} catch (Exception e) {
//						log.info("실패 : {}" + file.toFile());
						result.add(file.toFile().toString());
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

	/**
	 * 파싱 제외할 규칙 new Selection생성자에서 정의된 규칙으로 시작 하는 폴더
	 * 파일인 경우 같은 파일 이름 확장자 htm이 아닌 경우
	 */
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
