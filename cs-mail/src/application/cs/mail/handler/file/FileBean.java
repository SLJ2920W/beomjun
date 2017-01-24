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

public class FileBean implements EventHandler<ActionEvent> {

	private ReadOnlyObjectWrapper<Hyperlink> filePath = new ReadOnlyObjectWrapper<>(this, "filePath");
	private ReadOnlyObjectWrapper<HBox> filePathIcon = new ReadOnlyObjectWrapper<HBox>(this, "filePathIcon");
	private ReadOnlyStringWrapper fileName = new ReadOnlyStringWrapper(this, "fileName");
	private ReadOnlyLongWrapper fileSize = new ReadOnlyLongWrapper(this, "fileSize");
	private ReadOnlyObjectWrapper<FileTime> fileTime = new ReadOnlyObjectWrapper<>(this, "fileTime");

	public FileBean(Path home, Path file, BasicFileAttributes attrs) {

		String text = "";
		if (home == null) { // 검색인 경우 전체 경로 보여짐
			text = file.toString();
		} else if (home.equals(file)) { // 상위 폴더 이동 기능
			text = "..";
		} else { // 그외 상대경로 형태로 폴더 및 파일 보여짐
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

		// [s] 파일 및 폴더 이미지 설정
		String i = attrs.isDirectory() ? "/resources/cs/mail/img/folder.png" : "/resources/cs/mail/img/file.png";
		Label label = new Label();
		ImageView bg = new ImageView(new Image(getClass().getResourceAsStream(i)));
		bg.setFitWidth(20);
		bg.setPreserveRatio(true);
		bg.setSmooth(true);
		bg.setCache(true);
		label.setGraphic(bg);
		// [e] 파일 및 폴더 이미지 설정

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
		fileName.set("");
		fileSize.set(size);
		fileTime.set(time);
		// [e] 기타

	}

	// 하이퍼 링크 선택시 이벤트 처리
	@Override
	public void handle(ActionEvent e) {
		final Hyperlink link = (Hyperlink) e.getSource();
		final Path path = (Path) link.getUserData();
		// 현재 경로에 해당 하는 파일 및 폴더를 맵핑 함 (이벤트 처리 하기 위함)
		if (Files.isDirectory(path))
			Selection.getInstance().setDirectory(path);
		else
			Selection.getInstance().setDocument(path);

	}

}
