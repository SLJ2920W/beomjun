package application.cs.mail.common;

import java.nio.file.Path;

import application.cs.mail.handler.file.PathItem;
import javafx.beans.property.ReadOnlyObjectWrapper;

public class Selection {

	private ReadOnlyObjectWrapper<PathItem> _directory = new ReadOnlyObjectWrapper<>(this, "directory");

	public static final Selection INSTANCE = new Selection();

	public final javafx.beans.property.ReadOnlyObjectProperty<application.cs.mail.handler.file.PathItem> _directoryProperty() {
		return this._directory.getReadOnlyProperty();
	}

	public final application.cs.mail.handler.file.PathItem get_directory() {
		return this._directoryProperty().get();
	}

	public void set_directory(ReadOnlyObjectWrapper<PathItem> _directory) {
		this._directory = _directory;
	}

}
