package application.cs.mail.controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.cs.mail.common.App;
import application.cs.mail.common.Selection;
import application.cs.mail.controller.file.FileController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.DirectoryChooser;

public class MainController {

	private static final Logger log = LoggerFactory.getLogger(MainController.class);

	@FXML
	FileController fileController;
	@FXML
	private ProgressBar progressbar;
	@FXML
	private ProgressIndicator progressIndicator;
	@FXML
	private Label message;

	@FXML
	public void initialize() {
		try {
			fileController.init(this);

			Selection section = Selection.getInstance();

			// 진행바
			progressbar.progressProperty().unbind();
			progressbar.progressProperty().bind(section.progressProperty());
			// progressIndicator.progressProperty().unbind();
			// progressIndicator.progressProperty().bind(section.progressProperty());
			message.textProperty().unbind();
			message.textProperty().bind(section.messageProperty());

		} catch (NullPointerException e) {
			log.error(getClass().getName() + "_컨트롤러 맵핑 실패_" + e.toString());
		}
	}

	@FXML
	public void setMenuBarHandler() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File selectedDirectory = directoryChooser.showDialog(App.getPrimaryStage());
		if (selectedDirectory != null) {
			// 선택한 경로
			Path getPath = Paths.get(selectedDirectory.getAbsolutePath());

			// 현재 경로 저장
			Selection.getInstance().setDirectory(getPath);

			// 기본 인덱스 폴더 생성
			App.isIndexFolder();

			// 현재 경로를 프로퍼티에 저장
			App.setHome();

			fileController.init(this);
		}
	}

}
