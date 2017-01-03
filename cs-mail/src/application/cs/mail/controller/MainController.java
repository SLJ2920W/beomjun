package application.cs.mail.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import application.cs.mail.Main;
import application.cs.mail.common.App;
import application.cs.mail.common.Selection;
import application.cs.mail.controller.file.FileController;
import application.cs.mail.handler.file.PathItem;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.stage.DirectoryChooser;

public class MainController {

	@FXML
	FileController fileController;

	@FXML
	public void initialize() {
		try {
			fileController.init(this);

			setHome(); // 기본 홈으로 설정된 폴더 트리를 보여준다

		} catch (NullPointerException e) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING, "_컨트롤러 맵핑 실패_" + e.toString());
		}
	}

	public void setHome() {
		// 프로퍼티에 저장된 설정 값
		String home = Selection.INSTANCE.getSetting().get("home");
		home = home == null ? Main.getDefaultPath().toString() : home;

		PathItem pathItem = new PathItem(Paths.get(home));
		fileController.setDirPath(pathItem);
	}

	@FXML
	public void setMenuBarHandler() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File selectedDirectory = directoryChooser.showDialog(App.getPrimaryStage());
		if (selectedDirectory == null) {
			// 사용자 취소
			Platform.exit();
		} else {
			// 경로 보여주자
			Path getPath = Paths.get(selectedDirectory.getAbsolutePath());
			PathItem pathItem = new PathItem(getPath);
			
			
			Properties props = new Properties();
			// properties파일에 기본홈 설정
			props.setProperty("home", getPath.toString());
			Path configFilePath = Paths.get(Selection.INSTANCE.getSetting().get("configFilePath"));
			try {
				props.store(Files.newOutputStream(configFilePath), null);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			fileController.setDirPath(pathItem);
		}
	}

}
