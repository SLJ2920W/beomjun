package application.cs.mail;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import application.cs.mail.common.App;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		// [s] 구동시 설정
		App.setPrimaryStage(primaryStage);
		// [e] 구동시 설정

		try {
			Parent root = FXMLLoader.load(getClass().getResource("view/Main.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void init() throws Exception {
		List<String> paramList = getParameters().getRaw();
		Logger.getLogger(Main.class.getName()).log(Level.INFO, "실행 옵션 : ", paramList);
		super.init();
	}

	@Override
	public void stop() throws Exception {
		super.stop();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
