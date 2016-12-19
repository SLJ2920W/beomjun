package application.cs.mail;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import application.cs.mail.view.RootlayoutController;
import application.util.Msg;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

	private Stage primaryStage;
	private BorderPane rootPane;

	public Stage getPrimaryStage() {
		return primaryStage;
	}

//	private ObservableList<TreeItem<FileBean>> treeData = FXCollections.observableArrayList();
//
//	public ObservableList<TreeItem<FileBean>> getTreeData() {
//		return treeData;
//	}

	public Main() {
	}

	@Override
	public void init() throws Exception {
		System.out.println("init()");

		// [s] 실행 옵션 확인
		List<String> paramList = getParameters().getRaw();
		Logger.getLogger(Main.class.getName()).log(Level.INFO, "실행 옵션 : ", paramList);
		// [e] 실행 옵션 확인

		super.init();
	}

	@Override
	public void stop() throws Exception {
		System.out.println("stop()");
		super.stop();
	}

	@Override
	public void start(Stage primaryStage) {
		System.out.println("start()");
		try {

			this.primaryStage = primaryStage;

			layout();

		} catch (Exception e) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, Msg.MSG_TXT_001, e);
		}
	}

	// 레이아웃 및 메뉴바
	public void layout() throws Exception {
		// fxml 로드
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("view/RootLayout.fxml"));
		rootPane = (BorderPane) loader.load();

		// 스테이지 삽입 및 기본 실행 환경 초기화
		Scene scene = new Scene(rootPane);
		scene.getStylesheets().add(getClass().getResource("view/application.css").toExternalForm());
		 
		
		RootlayoutController controller = loader.getController();
		controller.setMain(this);

		// show
		primaryStage.setScene(scene);
		primaryStage.show();
 
	}

   
	public static void main(String[] args) {
		System.out.println("Main()");
		Logger.getLogger(Main.class.getName()).log(Level.INFO, Thread.currentThread().getName());
		launch(args);
	}
}
