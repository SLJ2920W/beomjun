package application.cs.mail.common;

import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App {

	private static Stage primaryStage;
	private static Parent primaryPane;
	private static Image applicationIcon;

	private App() {
	}

	public static Image applicationIcon() {
		
		if (applicationIcon == null)
			applicationIcon = new Image(
					App.class.getResourceAsStream("/resources/cs/mail/img/AppIcon.png"));

		return applicationIcon;
	}

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
