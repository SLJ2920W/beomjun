package org.kynosarges.mimebrowser;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.stage.DirectoryChooser;

/**
 * Represents the current directory and document selection.
 * @author Christoph Nahr
 * @version 1.3.2
 */
public class Selection {

    private final ReadOnlyObjectWrapper<Path> _directory = new ReadOnlyObjectWrapper<>(this, "directory");
    private final ReadOnlyObjectWrapper<Path> _document = new ReadOnlyObjectWrapper<>(this, "document");

    private final BooleanProperty _combine = new SimpleBooleanProperty(this, "combine");
    private final IntegerProperty _combineLevels = new SimpleIntegerProperty(this, "combineLevels");

    private final BooleanProperty _filter = new SimpleBooleanProperty(this, "filter");
    private final ObjectProperty<Pattern> _filterPattern = new SimpleObjectProperty<>(this, "filterPattern");
    private final ObjectProperty<SearchTarget> _filterTarget = new SimpleObjectProperty<>(this, "filterTarget");

    private final List<ChangeListener<Path>> _directoryListeners = new ArrayList<>();
    private final List<ChangeListener<Path>> _documentListeners = new ArrayList<>();

    /**
     * An empty filter pattern that accepts all input.
     */
    public static final Pattern EMPTY_PATTERN = Pattern.compile("", Pattern.LITERAL);

    /**
     * Creates a {@link Selection}. Private due to singleton class.
     */
    private Selection() { }

    /**
     * The single instance of the {@link Selection} class.
     */
    public static final Selection INSTANCE = new Selection();

    /**
     * Gets the currently selected directory.
     * @return the value of {@link #directoryProperty}
     */
    public final Path getDirectory() {
        return _directory.get();
    }
    /**
     * Sets the currently selected directory.
     * {@link #directoryProperty} remains unchanged if {@code value} is invalid.
     * @param value the new value for {@link #directoryProperty}
     */
    public void setDirectory(Path value) {
        if (value == null) return;
        try {
            value = value.toAbsolutePath();

            // try stripping file name if valid path is not a directory
            if (Files.exists(value) && !Files.isDirectory(value))
                value = value.getParent();

            // report error if we still have no valid directory
            if (value == null || !Files.exists(value) || !Files.isDirectory(value))
                throw new IOException(Global.getString("dlgDirNotFound"));

            _directory.set(value);
        }
        catch (IOException e) {
            Global.showAlert(null, Global.getString("dlgDirOpenError"), e);
        }
    }
    /**
     * Defines the currently selected directory.
     * @return the {@link ReadOnlyObjectProperty} holding the value
     */
    public ReadOnlyObjectProperty<Path> directoryProperty() {
        return _directory.getReadOnlyProperty();
    }
    /**
     * Adds the specified {@link ChangeListener} to the {@link #directoryProperty}.
     * The specified {@code listener} is also called by {@link #refresh}.
     * @param listener the {@link ChangeListener} to add
     */
    public void addDirectoryListener(ChangeListener<Path> listener) {
        _directory.addListener(listener);
        _directoryListeners.add(listener);
    }

    /**
     * Gets the currently selected document.
     * @return the value of {@link #documentProperty}
     */
    public final Path getDocument() {
        return _document.get();
    }
    /**
     * Sets the currently selected document.
     * {@link #documentProperty} remains unchanged if {@code value} is invalid.
     * @param value the new value for {@link #documentProperty}
     */
    public void setDocument(Path value) {
        if (value == null) return;
        value = value.toAbsolutePath();

        /*
         * Silently ignore directory paths. We don't attempt to handle I/O
         * errors because these will be shown in the document view proper.
         */
        if (!Files.isDirectory(value))
            _document.set(value);
    }
    /**
     * Defines the currently selected document.
     * @return the {@link ReadOnlyObjectProperty} holding the value
     */
    public ReadOnlyObjectProperty<Path> documentProperty() {
        return _document.getReadOnlyProperty();
    }
    /**
     * Adds the specified {@link ChangeListener} to the {@link #documentProperty}.
     * The specified {@code listener} is also called by {@link #refresh}.
     * @param listener the {@link ChangeListener} to add
     */
    public void addDocumentListener(ChangeListener<Path> listener) {
        _document.addListener(listener);
        _documentListeners.add(listener);
    }

    /**
     * Gets a value indicating whether subdirectories are combined.
     * @return the value of {@link #combineProperty}
     */
    public final boolean getCombine() {
        return _combine.get();
    }
    /**
     * Sets a value indicating whether subdirectories are combined.
     * @param value the new value for {@link #combineProperty}
     */
    public final void setCombine(boolean value) {
        _combine.set(value);
    }
    /**
     * Determines whether subdirectories are combined.
     * @return the {@link BooleanProperty} holding the value
     */
    public BooleanProperty combineProperty() {
        return _combine;
    }

    /**
     * Gets the number of subdirectory levels to combine.
     * @return the value of {@link #combineLevelsProperty}
     */
    public final int getCombineLevels() {
        return _combineLevels.get();
    }
    /**
     * Sets the number of subdirectory levels to combine.
     * @param value the new value for {@link #combineLevelsProperty}
     */
    public final void setCombineLevels(int value) {
        _combineLevels.set(value);
    }
    /**
     * Defines the number of subdirectory levels to combine.
     * Holds either a number or {@link Integer#MAX_VALUE} for all levels.
     * @return the {@link IntegerProperty} holding the value
     */
    public IntegerProperty combineLevelsProperty() {
        return _combineLevels;
    }

    /**
     * Gets a value indicating whether documents are filtered.
     * @return the value of {@link #filterProperty}
     */
    public final boolean getFilter() {
        return _filter.get();
    }
    /**
     * Sets a value indicating whether documents are filtered.
     * @param value the new value for {@link #filterProperty}
     */
    public final void setFilter(boolean value) {
        _filter.set(value);
    }
    /**
     * Determines whether documents are filtered.
     * @return the {@link BooleanProperty} holding the value
     */
    public BooleanProperty filterProperty() {
        return _filter;
    }

    /**
     * Gets the filter pattern for documents.
     * @return the value of {@link #filterPatternProperty}
     */
    public final Pattern getFilterPattern() {
        return _filterPattern.get();
    }
    /**
     * Sets the filter pattern for documents.
     * @param value the new value for {@link #filterPatternProperty}
     */
    public final void setFilterPattern(Pattern value) {
        _filterPattern.set(value);
    }
    /**
     * Defines the filter pattern for documents.
     * @return the {@link ObjectProperty} holding the value
     */
    public ObjectProperty<Pattern> filterPatternProperty() {
        return _filterPattern;
    }

    /**
     * Gets the filter target for documents.
     * @return the value of {@link #filterTargetProperty}
     */
    public final SearchTarget getFilterTarget() {
        return _filterTarget.get();
    }
    /**
     * Sets the filter target for documents.
     * @param value the new value for {@link #filterTargetProperty}
     */
    public final void setFilterTarget(SearchTarget value) {
        _filterTarget.set(value);
    }
    /**
     * Defines the filter target for documents.
     * @return the {@link ObjectProperty} holding the value
     */
    public ObjectProperty<SearchTarget> filterTargetProperty() {
        return _filterTarget;
    }

    /**
     * Shows a {@link DirectoryChooser} to let the user change the selected directory.
     */
    public void chooseDirectory() {
        DirectoryChooser chooser = new DirectoryChooser();

        Path oldDir = getDirectory();
        if (oldDir != null && Files.exists(oldDir) && Files.isDirectory(oldDir))
            chooser.setInitialDirectory(oldDir.toFile());

        File newDir = chooser.showDialog(Global.primaryStage());
        if (newDir != null)
            setDirectory(newDir.toPath());
    }

    /**
     * Refreshes the selected directory and document.
     * Notifies all listeners added through {@link #addDirectoryListener} and
     * {@link #addDocumentListener}, transmitting the respective current property values.
     */
    public void refresh() {
        /*
         * HACK: We separately store attached listeners because JavaFX properties drop set()
         * calls that specify the current value, and fireValueChangedEvent() is protected.
         * (The proper way would be to implement our own PathProperty with read-only wrapper...)
         */
        for (ChangeListener<Path> listener : _directoryListeners)
            listener.changed(_directory, _directory.get(), _directory.get());
        for (ChangeListener<Path> listener : _documentListeners)
            listener.changed(_document, _document.get(), _document.get());
    }
}
