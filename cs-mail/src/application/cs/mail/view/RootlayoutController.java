package application.cs.mail.view;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

import application.cs.mail.Main;
import application.sample.filetreeviewsample.PathItem;
import application.sample.filetreeviewsample.PathTreeItem;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;

public class RootlayoutController implements Initializable {

	@FXML
	private TreeView<PathItem> folderTree;

	@FXML
	private TreeView<PathItem> fileTree;
	
	@FXML
	private WebView webView;

	private Main mainApp;

	public void setMain(Main mainApp) {
		this.mainApp = mainApp;
	}

	public RootlayoutController() {
		// folderTree = new TreeView<PathItem>();
		// folderTree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		// ExecutorService service = Executors.newFixedThreadPool(3);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		folderTree.setRoot(null);

		folderTree.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> showDetails(oldValue, newValue));

		fileTree.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> showWebview(newValue));
	}

	@FXML
	public void setMenuBarHandler() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File selectedDirectory = directoryChooser.showDialog(mainApp.getPrimaryStage());
		if (selectedDirectory == null) {
			// 폴더를 선택하지 않음
		} else {
			// 경로 보여주자
			Path rootPath = Paths.get(selectedDirectory.getAbsolutePath());
			PathItem pathItem = new PathItem(rootPath);
			folderTree.setRoot(createNode(pathItem));
			folderTree.setEditable(false);
		}
	}

	// 파일 트리 상세
	public void showDetails(TreeItem<PathItem> oldValue, TreeItem<PathItem> newValue) {
		
		fileTree.setRoot(newValue);
		
	}
	
	// 파일 트리 상세
	public void showWebview(TreeItem<PathItem> newValue) {
		TreeItem<PathItem> s = newValue;
		s.getValue().getPath();
		System.out.println(newValue);
		
		WebEngine engine = webView.getEngine();
//        engine.load(s.getValue().getPath().toString());
        engine.load("D:\\Hanwha\\매일 백업\\2016-07-12 224805_남궁선(Namkung Seon)_FWvip접수 한화토탈 김희철 부사장님 일정이 일괄적으로 중복등록된 증상으로 접수되었….eml");

//		final TextField locationField = new TextField(DEFAULT_URL);
//
//		webEngine.locationProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
//
//			locationField.setText(newValue);
//
//		});
//
//		EventHandler<ActionEvent> goAction = (ActionEvent event) -> {
//
//			webEngine.load(locationField.getText().startsWith("http://")
//
//					? locationField.getText()
//
//					: "http://" + locationField.getText());
//
//		};
//
//		locationField.setOnAction(goAction);
//		Button goButton = new Button("Go");
//		goButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
//		goButton.setDefaultButton(true);
//		goButton.setOnAction(goAction);
//
//		// Layout logic
//
//		HBox hBox = new HBox(5);
//		hBox.getChildren().setAll(locationField, goButton);
//		HBox.setHgrow(locationField, Priority.ALWAYS);
//		VBox vBox = new VBox(5);
//		vBox.getChildren().setAll(hBox, webView);
//		vBox.setPrefSize(800, 400);
//		VBox.setVgrow(webView, Priority.ALWAYS);
//		return vBox;

	}
	
	private Object showDetails(Number newValue) {
		return null;
	}
	
	private TreeItem<PathItem> createNode(PathItem pathItem) {
		return PathTreeItem.createNode(pathItem);
	}
	
}
