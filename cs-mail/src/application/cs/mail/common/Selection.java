package application.cs.mail.common;

import java.util.HashMap;

import javafx.beans.property.ReadOnlyMapWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;

public class Selection {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private final ReadOnlyMapWrapper<String, String> setting = new ReadOnlyMapWrapper(FXCollections.observableMap(new HashMap<String, Object>()));
	private final ReadOnlyStringProperty mailViewTempFolderName = new ReadOnlyStringWrapper("tmp");

	public static final Selection INSTANCE = new Selection();


	public final javafx.beans.property.ReadOnlyMapProperty<java.lang.String, java.lang.String> settingProperty() {
		return this.setting.getReadOnlyProperty();
	}

	public final javafx.collections.ObservableMap<java.lang.String, java.lang.String> getSetting() {
		return this.settingProperty().get();
	}

	public void setSetting(String key, String value) {
		this.setting.put(key, value);
	}

	public final ReadOnlyStringProperty mailViewTempFolderNameProperty() {
		return this.mailViewTempFolderName;
	}

	public final java.lang.String getMailViewTempFolderName() {
		return this.mailViewTempFolderNameProperty().get();
	}

}
