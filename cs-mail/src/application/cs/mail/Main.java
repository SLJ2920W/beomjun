package application.cs.mail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Properties;

import application.cs.mail.common.App;
import application.cs.mail.common.Selection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		// [s]
		primaryStage.getIcons().add(App.applicationIcon()); // 아이콘
		primaryStage.setTitle("메일 뷰어"); // 타이틀
		App.setPrimaryStage(primaryStage); // 기본..
		// [e]
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/resources/cs/mail/fxml/Main.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/resources/cs/mail/css/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		Alert alert = new Alert(AlertType.ERROR);
//    	alert.setTitle("Error");
//    	alert.setHeaderText("Could not load data");
//    	alert.setContentText("Could not load data from file:\n");
//    	
//    	alert.showAndWait();
		
//		Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Welcome");
//        alert.setHeaderText("Welcome to XLTSearch!");
//        alert.setContentText("Please choose a folder to search.");
//        alert.showAndWait();

	}

	// 기본 경로 설정 -> 내문서 폴더
	public static Path getDefaultPath() {
		String home = System.getProperty("user.home") + File.separator + "Documents";
		Path path = Paths.get(home);

		if (Files.notExists(path)) {
			path = Paths.get(System.getProperty("user.home"));
		}

		return path;
	}

	@Override
	public void init() {
		Properties props = new Properties();

		String iniFolderPath = getDefaultPath().toString();
		String iniFolderName = "메일뷰어";
		String iniFileName = "setting.properties";
		Path iniFilePath = Paths.get(iniFolderPath + File.separator + iniFolderName + File.separator + iniFileName);
		Selection.INSTANCE.setSetting("configFilePath", iniFilePath.toString());

		try {
			// 내문서에 메일뷰어/setting 파일이 없다면..
			if (Files.notExists(iniFilePath)) {
				Files.createDirectories(iniFilePath.getParent());
				// properties파일에 기본홈을 내문서로 초기화
				props.setProperty("home", iniFolderPath);
				props.store(Files.newOutputStream(iniFilePath), null);
				
				Selection.INSTANCE.setSetting("home", iniFolderPath);
			} else { // 메일 뷰어 폴더가 있다면 설정값을 맵핑
				try (InputStream stream = Files.newInputStream(iniFilePath)) {
					props.load(stream);
				}
				Enumeration<Object> en = props.keys();
				while (en.hasMoreElements()) {
					String key = (String) en.nextElement();
					// 설정을 초기화
					Selection.INSTANCE.setSetting(key, props.getProperty(key));
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		

	}

	@Override
	public void stop() throws Exception {
		super.stop();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
