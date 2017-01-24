package application.cs.mail.handler.search;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.cs.mail.common.App;
import application.cs.mail.common.Selection;
import javafx.concurrent.Task;

/**
 * <ul>
 * <li>검색을 위한 인덱스 생성</li>
 * <li>http://lucene.apache.org/core/6_3_0/index.html</li>
 * <li>http://palpit.tistory.com/773</li>
 * </ul>
 * 
 * <pre>
 * <code>
 * </code>
 * </pre>
 */
public class TaskLuceneIndex extends Task<Boolean> {

	private static final Logger log = LoggerFactory.getLogger(TaskLuceneIndex.class);

	private Mode mode; // update create
	private String docsPath;
	private String indexPath;

	public TaskLuceneIndex(Mode mode) {
		this.mode = mode;
		// 현재 선택한 폴더 경로
		this.docsPath = Selection.getInstance().getDirectory().toString();
		// 현재 선택한 폴더 경로 + index폴더
		this.indexPath = Selection.getInstance().getDirectory() + File.separator + App.INDEX_FOLDER;

	}

	@Override
	protected Boolean call() throws Exception {

		final Path docDir = Paths.get(docsPath);
		if (!Files.isReadable(docDir)) {
			updateMessage("디렉토리를 다시 '" + docDir.toAbsolutePath() + "' 확인 해주시기 바랍니다.");
			System.exit(1);
		}

		boolean flag = false;
		try {
			updateMessage("디렉토리 색인 작업 시작 '" + indexPath + "'...");

			Directory dir = FSDirectory.open(Paths.get(indexPath));
			Analyzer analyzer = new StandardAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

			if (mode.equals(Mode.CREATE)) {
				iwc.setOpenMode(OpenMode.CREATE);
			} else {
				iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
			}

			IndexWriter writer = new IndexWriter(dir, iwc);
			flag = indexDocs(writer, docDir);

			// temp
			writer.forceMerge(1);

			writer.close();

			updateMessage("색인 작업 완료");

		} catch (IOException e) {
			log.error(e.toString());
		}

		return flag;
	}

	/**
	 * 
	 * @param writer
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	boolean indexDocs(final IndexWriter writer, Path path) throws IOException, InterruptedException {
		if (Files.isDirectory(path)) {
			Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					try {
						// 인덱스 허용 할 규칙
						if (file.toString().endsWith(".htm") || file.toString().endsWith(".html")) {
							indexDoc(writer, file, attrs.lastModifiedTime().toMillis());
						}
					} catch (IOException | InterruptedException ignore) {
						log.error(ignore.toString());
					}
					return FileVisitResult.CONTINUE;
				}
			});
		} else {
			indexDoc(writer, path, Files.getLastModifiedTime(path).toMillis());
		}
		return true;
	}

	/**
	 * 
	 * @param writer
	 * @param file
	 * @param lastModified
	 * @throws IOException
	 * @throws InterruptedException
	 */
	void indexDoc(IndexWriter writer, Path file, long lastModified) throws IOException, InterruptedException {
		try (InputStream stream = Files.newInputStream(file)) {
			// make a new, empty document
			Document doc = new Document();

			Field pathField = new StringField("path", file.toString(), Field.Store.YES);
			doc.add(pathField);

			doc.add(new LongPoint("modified", lastModified));

			doc.add(new TextField("contents", new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))));

			Field d = new StringField("filename", file.getFileName().toString(), Field.Store.YES);
			doc.add(d);

			// [s] progress
			// int _count = this.count;
			// updateProgress(_count, _count + queue.take().getCount());
			// [e] progress

			if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
				updateMessage("adding " + file.getFileName());
				writer.addDocument(doc);
			} else {
				updateMessage("updating " + file.getFileName());
				writer.updateDocument(new Term("path", file.toString()), doc);
			}
		}
	}

	public enum Mode {
		CREATE, UPDATE;
	}

}