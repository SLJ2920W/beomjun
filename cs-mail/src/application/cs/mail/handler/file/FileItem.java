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
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class FileItem implements EventHandler<ActionEvent> {

	private ReadOnlyObjectWrapper<Hyperlink> filePath = new ReadOnlyObjectWrapper<>(this, "filePath");
	private ReadOnlyObjectWrapper<HBox> filePathIcon = new ReadOnlyObjectWrapper<HBox>(this, "filePathIcon");
	private ReadOnlyStringWrapper fileName = new ReadOnlyStringWrapper(this, "fileName");
	private ReadOnlyLongWrapper fileSize = new ReadOnlyLongWrapper(this, "fileSize");
	private ReadOnlyObjectWrapper<FileTime> fileTime = new ReadOnlyObjectWrapper<>(this, "fileTime");

	public FileItem(Path home, Path file, BasicFileAttributes attrs) {

		String text = "";
		if (home == null) { // 검색인 경우
			text = file.toString();
		} else if (home.equals(file)) { // 상위 폴더
			text = "..";
		} else { // 그외 폴더 및 파일
			text = home.relativize(file).toString();
		}

		// [s] 링크 텍스트
		Hyperlink link = new Hyperlink(text);
		link.setOnAction(this);
		link.setTooltip(new Tooltip(file.toString()));
		link.setUserData(file);
		filePath.set(link);
		filePath.get().getText();
		// [e] 링크 텍스트

		// [s] 이미지 설정
		String i = attrs.isDirectory() ? "/resources/cs/mail/img/folder.png" : "/resources/cs/mail/img/file.png";
		Label label = new Label();
		ImageView bg = new ImageView(new Image(getClass().getResourceAsStream(i)));
		bg.setFitWidth(20);
		bg.setPreserveRatio(true);
		bg.setSmooth(true);
		bg.setCache(true);
		label.setGraphic(bg);
		// [e] 이미지 설정

		HBox box = new HBox();
		box.setAlignment(Pos.CENTER_LEFT);
		box.getChildren().add(label);
		box.getChildren().add(link);
		filePathIcon.set(box);

		// [s] 기타
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
		// [e] 기타

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

	public HBox getFilePathIcon() {
		return filePathIcon.get();
	}

	public void setFilePathIcon(ReadOnlyObjectWrapper<HBox> filePathIcon) {
		this.filePathIcon = filePathIcon;
	}

}
