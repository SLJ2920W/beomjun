package jdk8.lucene.one;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Indexer {
	private IndexWriter writer;

	public Indexer(String indexDirectoryPath) throws IOException {
		// this directory will contain the indexes
		Directory indexDirectory = FSDirectory.open(Paths.get(indexDirectoryPath));

		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		iwc.setOpenMode(OpenMode.CREATE);

		
		writer = new IndexWriter(indexDirectory, iwc);
	}

	public void close() throws CorruptIndexException, IOException {
		writer.close();
	}

	private Document getDocument(File file) throws IOException {
		Document document = new Document();

		
		try (InputStream stream = Files.newInputStream(Paths.get(file.toString()))) {
			// index file contents
//			Field contentField = new TextField(LuceneConstants.CONTENTS, new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)));
		
//			Field contentField = new TextField("contents", new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)));

			// index file name		
			Field fileNameField = new StringField(LuceneConstants.FILE_NAME, file.getName(), Field.Store.YES);
			
			// index file path
			Field filePathField = new StringField("path", file.toString(), Field.Store.YES);
	
			
//			document.add(contentField);
			document.add(fileNameField);
			document.add(filePathField);
		}
		return document;
	}

	private void indexFile(File file) throws IOException {
		System.out.println("Indexing " + file.getCanonicalPath());
		Document document = getDocument(file);
		writer.addDocument(document);
	}

	public int createIndex(String dataDirPath, FileFilter filter)
			throws IOException {
		// get all files in the data directory
		File[] files = new File(dataDirPath).listFiles();

		for (File file : files) {
			if (!file.isDirectory()
					&& !file.isHidden()
					&& file.exists()
					&& file.canRead()
					&& filter.accept(file)) {
				indexFile(file);
			}
		}
		return writer.numDocs();
	}
}
