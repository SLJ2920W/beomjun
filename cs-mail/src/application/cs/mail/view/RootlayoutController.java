package application.cs.mail.view;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import application.cs.mail.Main;
import application.cs.mail.model.FileBean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

public class RootlayoutController {

	private Main mainApp;

	@FXML
	private TreeView<File> treeView;

	public void setMain(Main mainApp) {
		this.mainApp = mainApp;
	}

	@FXML
	public void menuBar_file() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File selectedDirectory = directoryChooser.showDialog(mainApp.getPrimaryStage());
		if (selectedDirectory == null) {
			// 폴더를 선택하지 않음
		} else {
			// 경로 보여주자
			Path path = Paths.get(selectedDirectory.toString());
			Stream<Path> dirList = null;
			try {
				dirList = Files.list(path);
				String rootDir = path.getFileName().toString();

				TreeItem<File> treeItem = new TreeItem<File>();
				
//				TreeItem<File> treeItem = createNode(new File(selectedDirectory.toString()));
				
//				boolean okClicked = mainApp.showTree(treeItem);
//		        if (okClicked) {
//		            mainApp.getPersonData().add(tempPerson);
//		        }				
				
				System.out.println(1);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				dirList.close();
			}
		}
	}
	
	
	private TreeItem<File> createNode(final File f) {
		return new TreeItem<File>(f) {
			private boolean isLeaf;
			private boolean isFirstTimeChildren = true;
			private boolean isFirstTimeLeaf = true;

			@Override
			public ObservableList<TreeItem<File>> getChildren() {
				if (isFirstTimeChildren) {
					isFirstTimeChildren = false;
					super.getChildren().setAll(buildChildren(this));
				}
				return super.getChildren();
			}

			@Override
			public boolean isLeaf() {
				if (isFirstTimeLeaf) {
					isFirstTimeLeaf = false;
					File f = (File) getValue();
					isLeaf = f.isFile();
				}
				return isLeaf;
			}

			private ObservableList<TreeItem<File>> buildChildren(
					TreeItem<File> TreeItem) {
				File f = TreeItem.getValue();
				if (f == null) {
					return FXCollections.emptyObservableList();
				}
				if (f.isFile()) {
					return FXCollections.emptyObservableList();
				}
				File[] files = f.listFiles();
				if (files != null) {
					ObservableList<TreeItem<File>> children = FXCollections
							.observableArrayList();
					for (File childFile : files) {
						children.add(createNode(childFile));
					}
					return children;
				}
				return FXCollections.emptyObservableList();
			}
		};
	}

}
