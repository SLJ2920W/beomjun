package application.cs.mail.handler.search;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import application.cs.mail.common.Selection;

/**
 * <br> 1. 선택한 폴더에 인덱스 폴더 있는지 없는지 보고 없으면 생성
 * <br> 2. properties파일에 기본홈 설정 (어플 재 실행 할 경우 시작 위치)
 */
public class FileConfig {

	public static final String INDEX_FOLDER = "index";
	private String indexPath;
	private String homePath;
	// private String selectedPath;
	private Properties props = new Properties();


	public FileConfig() {
		// 선택할 폴더에 기본적 으로 인덱스 폴더 있는지 없는지 보고 없으면 생성
		indexPath = Selection.INSTANCE.getDirectory() + File.separator + INDEX_FOLDER;
		Path _sp = Paths.get(indexPath);
		if (Files.notExists(_sp)) {
			try {
				Files.createDirectories(_sp);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String getIndexPath() {
		return indexPath;
	}

	public String getHomePath() {
		return homePath;
	}

	// properties파일에 기본홈 설정
	public void setHomePath() {
		props.setProperty("home", Selection.INSTANCE.getDirectory().toString());
		Path configFilePath = Paths.get(Selection.INSTANCE.getSetting().get("configFilePath"));
		try {
			props.store(Files.newOutputStream(configFilePath), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
