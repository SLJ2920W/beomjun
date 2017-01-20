package jdk8.lucene.other;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class A {
	public static final String FILES_TO_INDEX_DIRECTORY = "D:\\temp";
	public static final String INDEX_DIRECTORY = "D:\\temp\\index2";
	public static final boolean CREATE_INDEX = true; // true: drop existing
														// index and create new
														// one
	// false: add new documents to existing index

	public static void Index() {
		final File docDir = new File(FILES_TO_INDEX_DIRECTORY);
		if (!docDir.exists() || !docDir.canRead()) {
			System.out
					.println("Document directory '"
							+ docDir.getAbsolutePath()
							+ "' does not exist or is not readable, please check the path");

			System.exit(1);
		}

		Date start = new Date();
		try {
			System.out.println("Indexing to directory '" + INDEX_DIRECTORY + "...");

			Directory dir = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
			Analyzer analyzer = new StandardAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

			if (CREATE_INDEX) {
				// Create a new index in the directory, removing any
				// previously indexed documents:
				iwc.setOpenMode(OpenMode.CREATE);
			} else {
				// Add new documents to an existing index:
				iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
			}

			IndexWriter writer = new IndexWriter(dir, iwc);
			indexDocs(writer, docDir);

			writer.close();

			Date end = new Date();
			System.out.println(end.getTime() - start.getTime() + " total milliseconds");

		} catch (IOException e) {
			System.out.println(" caught a " + e.getClass()
					+ "\n with message: " + e.getMessage());
		}
	}

	static void indexDocs(IndexWriter writer, File file) throws IOException {
		if (file.canRead()) {
			if (file.isDirectory()) {
				String[] files = file.list();
				// an IO error could occur
				if (files != null) {
					for (int i = 0; i < files.length; i++) {
						indexDocs(writer, new File(file, files[i]));
					}
				}
			} else {
				FileInputStream fis;
				try {
					fis = new FileInputStream(file);
				} catch (FileNotFoundException fnfe) {
					// at least on windows, some temporary files raise this
					// exception with an ";access denied" message
					// checking if the file can be read doesn't help
					return;
				}

				try {
					// make a new, empty document
					Document doc = new Document();

					Field pathField = new StringField("path", file.getPath(),
							Field.Store.YES);
					doc.add(pathField);
					doc.add(new TextField("filename", file.getName(), Field.Store.YES));

					// doc.add(new LongField("modified", file.lastModified(),
					// Field.Store.NO));

					doc.add(new TextField("contents", new BufferedReader(
							new InputStreamReader(fis, "UTF-8"))));

					if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
						System.out.println("adding " + file);
						// New index, so we just add the document (no old
						// document can be there):
						writer.addDocument(doc);
					} else {
						System.out.println("updating " + file);
						// Existing index (an old copy of this document may have
						// been indexed) so
						// we use updateDocument instead to replace the old one
						// matching the exact
						// path, if present:
						writer.updateDocument(new Term("path", file.getPath()), doc);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					fis.close();
				}
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Index();
	}

}