package application.cs.mail.handler.search;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.FSDirectory;

import application.cs.mail.common.Selection;
import application.cs.mail.handler.file.FileItem;
import javafx.concurrent.Task;

/**
 * <ul>
 * <li>생성된 인덱스 기반 검색 html만 검색함</li>
 * <li>http://lucene.apache.org/core/6_3_0/index.html</li>
 * <li>http://palpit.tistory.com/773</li>
 * </ul>
 * 
 * <pre>
 * <code>
 * </code>
 * </pre>
 */
public class TaskLuceneSearch extends Task<Queue<FileItem>> {
	private static final String LUCENE_CONTENTS = "contents";
	private static final String LUCENE_FILE_NAME = "filename";
	private static final String LUCENE_FILE_PATH = "path";
	private String searchQuery;
	private String indexPath;
	private static int SEARCH_LIMIT = 999;

	public TaskLuceneSearch(String searchQuery) {
		this.searchQuery = searchQuery;
		this.indexPath = Selection.INSTANCE.getDirectory().toString() + File.separator + FileConfig.INDEX_FOLDER;
	}

	@Override
	protected Queue<FileItem> call() throws Exception {
		Queue<FileItem> result = null;
		try (IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)))) {
			IndexSearcher searcher = new IndexSearcher(reader);
			Analyzer analyzer = new StandardAnalyzer();

			BufferedReader in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));

			// Term term = new Term(LUCENE_CONTENTS, "*abc*");
			// Query query = new WildcardQuery(term);

			QueryParser parser = new QueryParser(LUCENE_CONTENTS, analyzer);

			if (searchQuery == null || "".equals(searchQuery)) {
				System.out.println("??");
				reader.close();
			}

			searchQuery = searchQuery.trim();

			Query query = parser.parse(searchQuery);
			updateMessage("검색중 : " + query.toString(LUCENE_CONTENTS));
			// System.out.println("Searching for: " +
			// query.toString(LUCENE_CONTENTS));

			result = doSearch(in, searcher, query);

			// reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static Queue<FileItem> doSearch(BufferedReader in, IndexSearcher searcher, Query query) throws IOException {

		Queue<FileItem> result = new ConcurrentLinkedQueue<FileItem>();

		TopDocs docs = searcher.search(query, SEARCH_LIMIT);
		ScoreDoc[] hits = docs.scoreDocs;

		// int numTotalHits = docs.totalHits;
		// System.out.println(numTotalHits + " total matching documents");

		for (ScoreDoc hit : hits) {
			Document document = searcher.doc(hit.doc);
			String title = document.get("path");
			if (title == null) {
				title = "";
			}
			StringBuilder sb = new StringBuilder();
			for (IndexableField field : document.getFields()) {
				if (field.stringValue() != null) {
					sb.append(field.name() + ": " + field.stringValue());
				}
			}
			// System.out.println(sb.toString());
			Path path = Paths.get(title.replaceAll(".htm", ".eml"));
			result.add(new FileItem(null, path, Files.readAttributes(path, BasicFileAttributes.class)));
		}
		return result;
	}

}
