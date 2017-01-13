package application.cs.mail.controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import application.cs.mail.common.App;
import application.cs.mail.common.Selection;
import application.cs.mail.controller.file.FileController;
import application.cs.mail.handler.search.FileConfig;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.DirectoryChooser;

public class MainController {

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
			
			Selection section = Selection.INSTANCE;
			
			// 진행바
			progressbar.progressProperty().unbind();
			progressbar.progressProperty().bind(section.progressProperty());
//			progressIndicator.progressProperty().unbind();
//			progressIndicator.progressProperty().bind(section.progressProperty());
			message.textProperty().unbind();
			message.textProperty().bind(section.messageProperty());

		} catch (NullPointerException e) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING, "_컨트롤러 맵핑 실패_" + e.toString());
		}
	}

	@FXML
	public void setMenuBarHandler() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File selectedDirectory = directoryChooser.showDialog(App.getPrimaryStage());
		if (selectedDirectory == null) {
			// 사용자 취소
//			Platform.exit();
		} else {
			// 경로 보여주자
			Path getPath = Paths.get(selectedDirectory.getAbsolutePath());
			
			// 현재 경로 저장
			Selection.INSTANCE.setDirectory(getPath);
			
			// 기본 인덱스 폴더 생성
			FileConfig fileConfig = new FileConfig();
			
			// 현재 경로를 프로퍼티에 저장
			fileConfig.setHomePath();
			
			fileController.init(this);
		}
	}
	
	

}
