package application.cs.mail.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FileBean {

	private final StringProperty fileName;
	private final StringProperty filePath;

	public FileBean(String fileName) {
		this.fileName = new SimpleStringProperty(fileName);
		this.filePath = new SimpleStringProperty("");
	}
	
	public FileBean(String fileName, String filePath) {
		super();
		this.fileName = new SimpleStringProperty(fileName);
		this.filePath = new SimpleStringProperty(filePath);
	}

	public final StringProperty fileNameProperty() {
		return this.fileName;
	}

	public final java.lang.String getFileName() {
		return this.fileNameProperty().get();
	}

	public final void setFileName(final java.lang.String fileName) {
		this.fileNameProperty().set(fileName);
	}

	public final StringProperty filePathProperty() {
		return this.filePath;
	}

	public final java.lang.String getFilePath() {
		return this.filePathProperty().get();
	}

	public final void setFilePath(final java.lang.String filePath) {
		this.filePathProperty().set(filePath);
	}

}
