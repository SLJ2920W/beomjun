package application.cs.mail.handler.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

import application.cs.mail.common.Selection;
import javafx.beans.property.ReadOnlyLongWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Tooltip;

public class FileItem implements EventHandler<ActionEvent> {

	private ReadOnlyObjectWrapper<Hyperlink> filePath = new ReadOnlyObjectWrapper<>(this, "filePath");
	private ReadOnlyStringWrapper fileName = new ReadOnlyStringWrapper(this, "fileName");
	private ReadOnlyLongWrapper fileSize = new ReadOnlyLongWrapper(this, "fileSize");
	private ReadOnlyObjectWrapper<FileTime> fileTime = new ReadOnlyObjectWrapper<>(this, "fileTime");
	
	
	public FileItem(Path home, Path file, BasicFileAttributes attrs) {

		String text = "";
		if (home == null) {
			text = file.toString();
		} else if (home.equals(file)) {
			text = "..";
		} else {
			text = home.relativize(file).toString();
		}
		
		final Hyperlink link = new Hyperlink(text);
		link.setOnAction(this);
		link.setTooltip(new Tooltip(file.toString()));
		link.setUserData(file);
		filePath.set(link);
		filePath.get().getText();
		fileName.set(file.getFileName() == null ? "/" : file.getFileName().toString());

//		Image image = new Image(getClass().getResourceAsStream("labels.jpg"));
//		Label label3 = new Label("Search", new ImageView(image));

		long size = 0L;
		FileTime time;
		try {
			if (!attrs.isDirectory())
				size = Files.size(file);
			time = Files.getLastModifiedTime(file);
		} catch (IOException | SecurityException e) {
			size = 0L;
			time = FileTime.fromMillis(0L);
		}
		fileSize.set(size);
		fileTime.set(time);

	}

	@Override
	public void handle(ActionEvent e) {
		final Hyperlink link = (Hyperlink) e.getSource();
		final Path path = (Path) link.getUserData();
		if (Files.isDirectory(path))
			Selection.INSTANCE.setDirectory(path);
		else
			Selection.INSTANCE.setDocument(path);

	}

	public Hyperlink getFilePath() {
		return filePath.get();
	}

	public String getFileName() {
		return fileName.get();
	}

	public Long getFileSize() {
		return fileSize.get();
	}

	public FileTime getFileTime() {
		return fileTime.get();
	}

	public void setFilePath(ReadOnlyObjectWrapper<Hyperlink> filePath) {
		this.filePath = filePath;
	}

	public void setFileName(ReadOnlyStringWrapper fileName) {
		this.fileName = fileName;
	}

	public void setFileSize(ReadOnlyLongWrapper fileSize) {
		this.fileSize = fileSize;
	}

	public void setFileTime(ReadOnlyObjectWrapper<FileTime> fileTime) {
		this.fileTime = fileTime;
	}

}
