package application.cs.mail.handler.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

import application.cs.mail.common.Selection;
import application.cs.mail.handler.mime.MetaData;
import application.cs.mail.handler.search.SearchType;
import javafx.beans.property.ReadOnlyBooleanWrapper;
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

	// 메일 파일 첨부 여부
	private ReadOnlyObjectWrapper<Label> fileAttach = new ReadOnlyObjectWrapper<Label>(this, "fileAttach");
	// 메일 보낸 사람
	private ReadOnlyStringWrapper fileFrom = new ReadOnlyStringWrapper(this, "fileFrom");
	// 메일 경로와 아이콘 포함
	private ReadOnlyObjectWrapper<HBox> filePathIcon = new ReadOnlyObjectWrapper<HBox>(this, "filePathIcon");
	// 메일 경로
	private ReadOnlyObjectWrapper<Hyperlink> filePath = new ReadOnlyObjectWrapper<>(this, "filePath");
	// 메일 전송일
	private ReadOnlyStringWrapper fileDate = new ReadOnlyStringWrapper(this, "fileDate");
	// 메일 사이즈
	private ReadOnlyLongWrapper fileSize = new ReadOnlyLongWrapper(this, "fileSize");
	// 윈도우 탐색기 에서 파일 수정된 날짜 (메일 내용과 관련 없음)
	private ReadOnlyObjectWrapper<FileTime> fileTime = new ReadOnlyObjectWrapper<>(this, "fileTime");

	/**
	 * @param home
	 *            선택된 경로 최상위
	 * @param file
	 *            현재 경로
	 * @param attrs
	 *            현재 경로의 파일 속성
	 * @param searchType
	 *            검색 여부
	 */
	public FileBean(Path home, Path file, BasicFileAttributes attrs, SearchType searchType) {

		String text = "";
		if (home == null || !searchType.equals(SearchType.NONE)) { // 검색인 경우 전체
																	// 경로 보여짐
			text = file.toString();
		} else if (home.equals(file)) { // 상위 폴더 이동 기능
			// text = "..";
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
//		String i = attrs.isDirectory() ? "/resources/cs/mail/img/folder.png" : "/resources/cs/mail/img/file.png";
//		Label label = new Label();
//		ImageView bg = new ImageView(new Image(getClass().getResourceAsStream(i)));
//		bg.setFitWidth(20);
//		bg.setPreserveRatio(true);
//		bg.setSmooth(true);
//		bg.setCache(true);
//		label.setGraphic(bg);
		// [e] 파일 및 폴더 이미지 설정

		HBox box = new HBox();
		box.setAlignment(Pos.CENTER_LEFT);
//		box.getChildren().add(label);
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

		MetaData meta = new MetaData(file);
		
		// [s] 첨부파일..
		String i = "/resources/cs/mail/img/attach.png";
		if (meta.getMetaAttach() != null) {
			Label label = new Label();
			ImageView bg = new ImageView(new Image(getClass().getResourceAsStream(i)));
			bg.setFitWidth(15);
			bg.setPreserveRatio(true);
			bg.setSmooth(true);
			bg.setCache(true);
			label.setGraphic(bg);
			fileAttach.setValue(label);
		}
		// [e] 첨부파일..


		fileFrom.setValue(meta.getMetaFrom());
		fileDate.setValue(meta.getMetaSentDate());

		fileSize.set(size);
		fileTime.set(time); // ex 파일 수정 시간
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

	public final javafx.beans.property.ReadOnlyObjectProperty<javafx.scene.control.Hyperlink> filePathProperty() {
		return this.filePath.getReadOnlyProperty();
	}

	public final javafx.scene.control.Hyperlink getFilePath() {
		return this.filePathProperty().get();
	}

	public final javafx.beans.property.ReadOnlyObjectProperty<javafx.scene.layout.HBox> filePathIconProperty() {
		return this.filePathIcon.getReadOnlyProperty();
	}

	public final javafx.scene.layout.HBox getFilePathIcon() {
		return this.filePathIconProperty().get();
	}

	public final javafx.beans.property.ReadOnlyObjectProperty<java.nio.file.attribute.FileTime> fileTimeProperty() {
		return this.fileTime.getReadOnlyProperty();
	}

	public final java.nio.file.attribute.FileTime getFileTime() {
		return this.fileTimeProperty().get();
	}

	public final javafx.beans.property.ReadOnlyStringProperty fileFromProperty() {
		return this.fileFrom.getReadOnlyProperty();
	}

	public final java.lang.String getFileFrom() {
		return this.fileFromProperty().get();
	}

	public final javafx.beans.property.ReadOnlyStringProperty fileDateProperty() {
		return this.fileDate.getReadOnlyProperty();
	}

	public final java.lang.String getFileDate() {
		return this.fileDateProperty().get();
	}

	public final javafx.beans.property.ReadOnlyObjectProperty<javafx.scene.control.Label> fileAttachProperty() {
		return this.fileAttach.getReadOnlyProperty();
	}

	public final javafx.scene.control.Label getFileAttach() {
		return this.fileAttachProperty().get();
	}

	public final javafx.beans.property.ReadOnlyLongProperty fileSizeProperty() {
		return this.fileSize.getReadOnlyProperty();
	}

	public final long getFileSize() {
		return this.fileSizeProperty().get();
	}

}
