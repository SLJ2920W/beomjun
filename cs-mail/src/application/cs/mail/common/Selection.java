package application.cs.mail.common;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyMapWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;

/**
 * http://www.dummies.com/programming/java/how-to-create-a-read-only-property-in
 * -javafx/
 * https://blog.netopyr.com/2012/02/02/creating-read-only-properties-in-javafx/
 */
public class Selection {
	// 폴더 리스트 표시중에 제외할 패턴
	private List<String> mailViewIgnore = new ArrayList<String>();
	// 메일 이미지 저장 루트 폴더 
	private ReadOnlyStringWrapper mailViewTempFolderName = new ReadOnlyStringWrapper("tmp");
	// 각종 설정 toString으로 확인
	private final ReadOnlyMapWrapper<String, String> setting = new ReadOnlyMapWrapper<String, String>(FXCollections.observableMap(new HashMap<String, String>()));
	// 선택한 디렉토리
	private final ReadOnlyObjectWrapper<Path> directory = new ReadOnlyObjectWrapper<Path>(this, "directory");
	// 선택한 문서
	private final ReadOnlyObjectWrapper<Path> document = new ReadOnlyObjectWrapper<Path>(this, "document");
	// 진행 상태
	private DoubleProperty progress = new SimpleDoubleProperty(0.0);
	private StringProperty message = new SimpleStringProperty("");

	private List<ChangeListener<Path>> directoryListener = new ArrayList<ChangeListener<Path>>();
	private List<ChangeListener<Path>> documentListener = new ArrayList<ChangeListener<Path>>();

	public static final Selection INSTANCE = new Selection();

	public Selection() {
		// 폴더 리스트 표시중에 제외할 패턴
		this.mailViewIgnore.add("tmp");
		this.mailViewIgnore.add("index");
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Set<Entry<String, String>> s = setting.get().entrySet();
		Iterator<Entry<String, String>> i = s.iterator();
		while (i.hasNext()) {
			Entry<String, String> e = i.next();
			sb.append("key : " + e.getKey() + ", value : " + e.getValue() + "\n");
		}
		return "Selection [setting :\n" + sb.toString() + "\n]";
	}

	public final javafx.beans.property.ReadOnlyMapProperty<java.lang.String, java.lang.String> settingProperty() {
		return this.setting.getReadOnlyProperty();
	}

	public final javafx.collections.ObservableMap<java.lang.String, java.lang.String> getSetting() {
		return this.settingProperty().get();
	}

	public void setSetting(String key, String value) {
		this.setting.put(key, value);
	}

	public List<String> getMailViewIgnore() {
		return mailViewIgnore;
	}

	public void setMailViewIgnore(List<String> mailViewIgnore) {
		this.mailViewIgnore = mailViewIgnore;
	}

	public final javafx.beans.property.ReadOnlyStringProperty mailViewTempFolderNameProperty() {
		return this.mailViewTempFolderName.getReadOnlyProperty();
	}

	public final java.lang.String getMailViewTempFolderName() {
		return this.mailViewTempFolderNameProperty().get();
	}

	public void setDirectory(Path dir) {
		this.directory.set(dir);
	}

	public void setDocument(Path dir) {
		this.document.set(dir);
	}

	public final javafx.beans.property.ReadOnlyObjectProperty<java.nio.file.Path> directoryProperty() {
		return this.directory.getReadOnlyProperty();
	}

	public final java.nio.file.Path getDirectory() {
		return this.directoryProperty().get();
	}

	public final javafx.beans.property.ReadOnlyObjectProperty<java.nio.file.Path> documentProperty() {
		return this.document.getReadOnlyProperty();
	}

	public final java.nio.file.Path getDocument() {
		return this.documentProperty().get();
	}

	public List<ChangeListener<Path>> getDirectoryListener() {
		return directoryListener;
	}

	public List<ChangeListener<Path>> getDocumentListener() {
		return documentListener;
	}

	public void setDirectoryListener(ChangeListener<Path> e) {
		directory.addListener(e);
		directoryListener.add(e);
	}

	public void setDocumentListener(ChangeListener<Path> e) {
		document.addListener(e);
		documentListener.add(e);
	}

	public final DoubleProperty progressProperty() {
		return this.progress;
	}

	public final double getProgress() {
		return this.progressProperty().get();
	}

	public final void setProgress(final double progress) {
		this.progressProperty().set(progress);
	}

	public final StringProperty messageProperty() {
		return this.message;
	}

	public final java.lang.String getMessage() {
		return this.messageProperty().get();
	}

	public final void setMessage(final java.lang.String message) {
		this.messageProperty().set(message);
	}

}
