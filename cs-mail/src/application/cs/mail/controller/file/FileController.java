package application.cs.mail.controller.file;

import java.net.URL;
import java.util.ResourceBundle;

import application.cs.mail.controller.MainController;
import application.cs.mail.handler.file.PathItem;
import application.cs.mail.handler.file.PathTreeItem;
import application.sample.tree2.Employee;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.TreeView;

public class FileController implements Initializable {

	private MainController main;

	@FXML
	private TreeView<PathItem> folderTree;
	
	@FXML
	private TreeTableColumn<PathItem, String> fileName;

	@FXML
	private TreeTableColumn<PathItem, String> filePath;

	private TreeItem<PathItem> root;

	@FXML
	private TreeTableView<PathItem> fileTree;

	public void init(MainController mainController) {
		main = mainController;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		folderTree.setRoot(null);
		folderTree.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> showDetails(oldValue, newValue));

		// fileTree.getSelectionModel().selectedItemProperty().addListener(
		// (observable, oldValue, newValue) -> showWebview(newValue));
	}

	// 폴더 트리 뿌려주기
	public void setDirPath(PathItem pathItem) {
		folderTree.setRoot(createNode(pathItem));
	}

	// 폴더 트리 선택 후 파일 트리
	@SuppressWarnings("unchecked")
	public void showDetails(TreeItem<PathItem> oldValue, TreeItem<PathItem> newValue) {
		root = new TreeItem<PathItem>(newValue.getValue());
		
		fileName = new TreeTableColumn<>("이름");
		fileName.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<PathItem, String> param) -> new ReadOnlyStringWrapper("TEST"));		

		filePath = new TreeTableColumn<>("경로");
		filePath.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<PathItem, String> param) -> new ReadOnlyStringWrapper("test"));
		
		fileTree.getColumns().setAll(fileName, filePath);
		fileTree.setRoot(root);
		
	}

	private TreeItem<PathItem> createNode(PathItem pathItem) {
		return PathTreeItem.createNode(pathItem);
	}

}
