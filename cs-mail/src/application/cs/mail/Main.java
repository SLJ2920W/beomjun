package application.cs.mail;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.cs.mail.common.App;
import application.cs.mail.common.Selection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class Main extends Application {

	public static final Logger log = LoggerFactory.getLogger(Main.class);

	@Override
	public void start(Stage primaryStage) {

		// log.trace("trace");
		// log.debug("debug");
		// log.info("info");
		// log.warn("warn");
		// log.error("error");

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
			log.error(e.toString());
		}
	}

	@Override
	public void init() {

		try {
			App.init();
			Selection.getInstance().setDefaultBrowser(App.getDefaultBrowser());

			// 파일 감시 스레드
			// new
			// WatchDir(Selection.getInstance().getDirectory().toString()).call();

			// DaemonThreadFactory dtf = new DaemonThreadFactory();
			// ExecutorService service = Executors.newCachedThreadPool(dtf);
			// service.submit(new HanwhaEml());

		} catch (Exception e) {
			log.error(e.toString());
		}

	}

	@Override
	public void stop() throws Exception {
		// executorservice 확장 해서 스레드 체크
		// 스레드 확인
		// 살아 있는 스레드가 있다면..
		// 종료 의사 묻기
//		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//		alert.setTitle("알림");
//		alert.setHeaderText("진행중인 작업이 있습니다");
//		alert.setContentText("강제 종료 하시겠습니까?");
//		Optional<ButtonType> result = alert.showAndWait();
//		if (!result.isPresent() || result.get() != ButtonType.OK) {
//			Platform.exit();
//		}
		
		super.stop();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
