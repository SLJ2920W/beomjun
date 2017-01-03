package application.cs.mail.handler.file;

import javafx.beans.property.SimpleStringProperty;

public class FileItem {

	private final SimpleStringProperty fileName;
	private final SimpleStringProperty filePath;

	public FileItem(String fileName, String filePath) {
		this.fileName = new SimpleStringProperty(fileName);
		this.filePath = new SimpleStringProperty(filePath);
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
