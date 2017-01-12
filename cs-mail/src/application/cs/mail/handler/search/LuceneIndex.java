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

import application.cs.mail.common.Selection;
import javafx.concurrent.Task;

/**
 * <ul>
 * 	<li> FX는 UI변경시 Runnable이랑 Task 둘중 하나로 스레드 구현 한다. &lt;E&gt;는 리턴 값을 받을수 있음 -> getClass().getValue() -> &lt;E&gt; </li>
 * 	<li> 검색을 위한 인덱스 생성 </li>
 * 	<li> 기본 예제 응용 하여 적용 </li>
 * 	<li> http://lucene.apache.org/core/6_3_0/index.html </li>
 * 	<li> http://palpit.tistory.com/773 </li>
 * </ul>
 * <pre>
 * <code>
 * </code>
 * </pre>
 */
public class LuceneIndex extends Task<Boolean> {

	private Mode mode; // update create
	private String docsPath;
	private String indexPath;

	public LuceneIndex(Mode mode) {
		this.mode = mode;
		// 현재 선택한 폴더 경로
		this.docsPath = Selection.INSTANCE.getDirectory().toString();
		// 현재 선택한 폴더 경로 + index폴더
		this.indexPath = Selection.INSTANCE.getDirectory() + File.separator + FileConfig.INDEX_FOLDER;
		
	}

	@Override
	protected Boolean call() throws Exception {
		
		final Path docDir = Paths.get(docsPath);
		if (!Files.isReadable(docDir)) {
			updateMessage("Document directory '" + docDir.toAbsolutePath() + "' does not exist or is not readable, please check the path");
//			System.out.println("Document directory '" + docDir.toAbsolutePath() + "' does not exist or is not readable, please check the path");
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
//			System.out.println(end.getTime() - start.getTime() + " total milliseconds");

		} catch (IOException e) {
			updateMessage(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
//			System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
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
			

			// [s] progress
//			int _count = this.count;
//			updateProgress(_count, _count + queue.take().getCount());
			// [e] progress

			if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
//				System.out.println("adding " + file);
				updateMessage("adding " + file.getFileName());
				writer.addDocument(doc);
			} else {
//				System.out.println("updating " + file);
				updateMessage("updating " + file.getFileName());
				writer.updateDocument(new Term("path", file.toString()), doc);
			}
		}
	}

	public enum Mode {
		CREATE, UPDATE;
	}

}