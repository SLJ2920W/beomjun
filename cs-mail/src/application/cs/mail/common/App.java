package application.cs.mail.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Properties;

import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App {

	private static Stage primaryStage;
	private static Parent primaryPane;
	private static Image applicationIcon;
	private static final Properties props = new Properties();
	public static final String INDEX_FOLDER = "index";

	public App() {
	}

	/**
	 * 선택할 폴더에 인덱스 폴더 존재 유무 확인후 없다면 생성
	 */
	public static void isIndexFolder() {
		String indexPath = Selection.INSTANCE.getDirectory() + File.separator + INDEX_FOLDER;
		Path _sp = Paths.get(indexPath);
		if (Files.notExists(_sp)) {
			try {
				Files.createDirectories(_sp);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 선택한 폴더를 properties파일에 기본 홈으로 설정
	 */
	public static void setHome() {
		props.setProperty("home", Selection.INSTANCE.getDirectory().toString());
		Path configFilePath = Paths.get(Selection.INSTANCE.getSetting().get("configFilePath"));
		try {
			props.store(Files.newOutputStream(configFilePath), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 내문서/메일뷰어/setting.properties 파일 없다면 생성 있다면 설정값 맵핑함
	 * @throws Exception
	 */
	public static void init() throws Exception {
		String iniFolderPath = getDefaultPath().toString();
		String iniFolderName = "메일뷰어";
		String iniFileName = "setting.properties";
		Path iniFilePath = Paths.get(iniFolderPath + File.separator + iniFolderName + File.separator + iniFileName);
		Selection.INSTANCE.setSetting("configFilePath", iniFilePath.toString());

		// 내문서에 메일뷰어/setting 파일이 없다면..
		if (Files.notExists(iniFilePath)) {
			Files.createDirectories(iniFilePath.getParent());
			// properties파일에 기본홈을 내문서로 초기화
			props.setProperty("home", iniFolderPath);
			props.store(Files.newOutputStream(iniFilePath), null);

			Selection.INSTANCE.setSetting("home", iniFolderPath);
		} else { // 메일 뷰어 폴더가 있다면 설정값을 변수에 넣음
			try (InputStream stream = Files.newInputStream(iniFilePath)) {
				props.load(stream);
			}
			Enumeration<Object> en = props.keys();
			while (en.hasMoreElements()) {
				String key = (String) en.nextElement();
				// 설정을 초기화
				Selection.INSTANCE.setSetting(key, props.getProperty(key));
			}
		}

		// 폴더 및 파일 선택 이벤트 처리를 위해 추가
		Selection.INSTANCE.setDirectory(Paths.get(Selection.INSTANCE.getSetting().get("home")));
		Selection.INSTANCE.setDocument(Paths.get(Selection.INSTANCE.getSetting().get("home")));

	}

	/**
	 * 기본 경로 설정 -> 내문서 폴더
	 * @return Path
	 */
	public static Path getDefaultPath() {
		String home = System.getProperty("user.home") + File.separator + "Documents";
		Path path = Paths.get(home);

		if (Files.notExists(path)) {
			path = Paths.get(System.getProperty("user.home"));
		}

		return path;
	}

	/**
	 * 앱 아이콘 생성
	 * @return Image
	 */
	public static Image applicationIcon() {
		applicationIcon = new Image(App.class.getResourceAsStream("/resources/cs/mail/img/app.png"));
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
