package application.cs.mail.controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import application.cs.mail.common.App;
import application.cs.mail.controller.file.FileController;
import application.cs.mail.handler.file.PathItem;
import javafx.fxml.FXML;
import javafx.stage.DirectoryChooser;

public class MainController {

	@FXML
	FileController fileController;

	@FXML
	public void initialize() {
		try {
			fileController.init(this);
		} catch (NullPointerException e) {
			System.out.println("메인 컨트롤러 널");
		}
	}

	@FXML
	public void setMenuBarHandler() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File selectedDirectory = directoryChooser.showDialog(App.getPrimaryStage());
		if (selectedDirectory == null) {
			// 폴더를 선택하지 않음
		} else {
			// 경로 보여주자
			Path rootPath = Paths.get(selectedDirectory.getAbsolutePath());
			PathItem pathItem = new PathItem(rootPath);
			fileController.setDirPath(pathItem);
		}
	}

}
