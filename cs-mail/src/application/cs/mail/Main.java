package application.cs.mail;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.cs.mail.common.App;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	
	public static final Logger log = LoggerFactory.getLogger(Main.class);

	@Override
	public void start(Stage primaryStage) {
		
//        log.trace("trace");
//        log.debug("debug");
//        log.info("info");
//        log.warn("warn");
//        log.error("error");
		
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

	@Override
	public void init() {

		try {
			App.init();
			
			// 파일 감시 스레드
//			new WatchDir(Selection.INSTANCE.getDirectory().toString()).call();
			
//			DaemonThreadFactory dtf = new DaemonThreadFactory();
//			ExecutorService service = Executors.newCachedThreadPool(dtf);
//			service.submit(new HanwhaEml());
			
		} catch (Exception e) {
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
