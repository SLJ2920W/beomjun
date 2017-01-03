package application.cs.mail.handler.file;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * http://docs.oracle.com/javafx/2/api/index.html
 * http://www.java2s.com/Tutorials/Java/JavaFX/0650__JavaFX_TableView.htm
 */
public class FileItemList {

	private final ObservableList<FileItem> fileItemList = FXCollections.observableArrayList();
	public static final FileItemList INSTANCE = new FileItemList();

	public ObservableList<FileItem> getFileItemList() {
		return fileItemList;
	}

	public void createNode(Path path) {
		fileItemList.clear();
		setFilePath(path);
	}

	private void setFilePath(Path path) {
		try {

			DirectoryStream.Filter<Path> filter = (Path file) -> {
				return file.toString().endsWith(".eml") && !Files.isDirectory(file);
			};
			DirectoryStream<Path> p = Files.newDirectoryStream(Paths.get(path.toString()), filter);

			p.forEach(el -> {
				fileItemList.add(new FileItem(el.getFileName().toString(), el.toFile().getPath()));
			});

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}