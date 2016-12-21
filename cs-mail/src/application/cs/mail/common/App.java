package application.cs.mail.common;

import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App {

	private static Stage primaryStage;
	private static Parent primaryPane;

	public static Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void setPrimaryStage(Stage primaryStage) {
		App.primaryStage = primaryStage;
	}

	public static BorderPane getPrimaryPane() {
		return (BorderPane) primaryPane;
	}

	public static void setPrimaryPane(Parent primaryPane) {
		App.primaryPane = (BorderPane) primaryPane;
	}

}
