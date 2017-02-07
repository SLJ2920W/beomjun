package application.cs.mail.controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.cs.mail.common.App;
import application.cs.mail.common.Selection;
import application.cs.mail.controller.file.FileController;
import application.cs.mail.handler.DaemonThreadFactory;
import application.cs.mail.handler.search.TaskLuceneIndex;
import application.cs.mail.handler.search.TaskLuceneIndex.Mode;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
	
	// 인덱스 관련
	@FXML
	public void updateIndex() {
		// HTML만 인덱스 잡음
		TaskLuceneIndex task = new TaskLuceneIndex(Mode.UPDATE);
		ExecutorService service = Executors.newFixedThreadPool(App.MAX_THREAD, new DaemonThreadFactory(task));
		service.submit(task);
	}
	
	// 스레드 정지 할라고 만들래
	public void threadStop(){
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText("Indexing in progress");
		alert.setContentText("Opening a new folder will cancel indexing. Continue?");
		Optional<ButtonType> result = alert.showAndWait();
		if (!result.isPresent() || result.get() != ButtonType.OK) {
			Platform.exit();
		}
	}

}
