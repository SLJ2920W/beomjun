package application.sample.tree2;

import java.util.Arrays;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
	List<Employee> employees = Arrays.<Employee> asList(
			new Employee("a1", "A"),
			new Employee("a2", "A"),
			new Employee("e1", "E"));
	TreeItem<String> rootNode = new TreeItem<String>("Root");

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) {
		rootNode.setExpanded(true);
		for (Employee employee : employees) {
			TreeItem<String> empLeaf = new TreeItem<String>(employee.getName());
			boolean found = false;
			for (TreeItem<String> depNode : rootNode.getChildren()) {
				if (depNode.getValue().contentEquals(employee.getDepartment())) {
					depNode.getChildren().add(empLeaf);
					found = true;
					break;
				}
			}
			if (!found) {
				TreeItem depNode = new TreeItem(employee.getDepartment());
				rootNode.getChildren().add(depNode);
				depNode.getChildren().add(empLeaf);
			}
		}
		stage.setTitle("Tree View Sample");
		VBox box = new VBox();
		final Scene scene = new Scene(box, 400, 300);
		scene.setFill(Color.LIGHTGRAY);

		TreeView<String> treeView = new TreeView<String>(rootNode);
		treeView.setShowRoot(true);
		treeView.setEditable(true);
		box.getChildren().add(treeView);
		stage.setScene(scene);
		stage.show();
	}
}
