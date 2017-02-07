package application.cs.mail.handler.file;

import java.nio.file.Path;

import javafx.beans.property.SimpleStringProperty;

public class PathItem {
	private Path path;
	private SimpleStringProperty fileName;
	private SimpleStringProperty filePath;

	public PathItem(Path path) {
		this.path = path;
	}

	public Path getPath() {
		return path;
	}

	@Override
	public String toString() {
		if (path.getFileName() == null) {
			return path.toString();
		} else {
			return path.getFileName().toString();
		}
	}

	public final SimpleStringProperty fileNameProperty() {
		return this.fileName;
	}

	public final java.lang.String getFileName() {
		return this.fileNameProperty().get();
	}

	public final void setFileName(final java.lang.String fileName) {
		this.fileNameProperty().set(fileName);
	}

	public final SimpleStringProperty filePathProperty() {
		return this.filePath;
	}

	public final java.lang.String getFilePath() {
		return this.filePathProperty().get();
	}

	public final void setFilePath(final java.lang.String filePath) {
		this.filePathProperty().set(filePath);
	}


}
