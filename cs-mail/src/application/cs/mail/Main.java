package application.cs.mail;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import application.cs.mail.model.FileBean;
import application.cs.mail.view.MailViewController;
import application.cs.mail.view.RootlayoutController;
import application.util.Msg;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {

	private Stage primaryStage;
	private BorderPane rootPane;

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	private ObservableList<TreeItem<FileBean>> treeData = FXCollections.observableArrayList();
//	private TreeItem<File> treeData = new TreeItem<File>();

	public ObservableList<TreeItem<FileBean>> getTreeData() {
		return treeData;
	}

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

			rootLayout();

//			content();

		} catch (Exception e) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, Msg.MSG_TXT_001, e);
		}
	}

	// 레이아웃 및 메뉴바
	public void rootLayout() throws Exception {
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

	// 내용
	public void content() throws Exception {
		// FXML 로드
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("view/MailView.fxml"));

		// rootLayout 센터에 삽입
		HBox hBoxPane = (HBox) loader.load();
		rootPane.setCenter(hBoxPane);

		MailViewController poc = loader.getController();
		poc.setMain(this);
	}
	
	
   
	public static void main(String[] args) {
		System.out.println("Main()");
		Logger.getLogger(Main.class.getName()).log(Level.INFO, Thread.currentThread().getName() + " sdfsdfdfs123123");
		launch(args);
	}
}
