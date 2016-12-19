package org.kynosarges.mimebrowser;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;

import javafx.beans.property.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * Provides an interactive representation of a directory entry.
 * @author Christoph Nahr
 * @version 1.3.4
 */
public class DirectoryEntry {

    private final Path _path;
    private static final PathLinkActionHandler ON_PATH_LINK_ACTION = new PathLinkActionHandler();

    private final ReadOnlyObjectWrapper<Hyperlink> _pathLink = new ReadOnlyObjectWrapper<>(this, "pathLink");
    private final ReadOnlyObjectWrapper<HBox> _pathBox = new ReadOnlyObjectWrapper<>(this, "pathBox");
    private final ReadOnlyLongWrapper _pathSize = new ReadOnlyLongWrapper(this, "pathSize");
    private final ReadOnlyObjectWrapper<FileTime> _pathTime = new ReadOnlyObjectWrapper<>(this, "pathTime");

    /**
     * Creates a {@link DirectoryEntry} for the specified {@link Path}.
     * @param dir the directory relative to which {@code path} is shown
     * @param path the {@link Path} to convert into a {@link DirectoryEntry}
     * @param attrs the basic file attributes of {@code path}
     */
    public DirectoryEntry(Path dir, Path path, BasicFileAttributes attrs) {
        _path = path;

        final Hyperlink link = new Hyperlink(dir.relativize(path).toString());
        link.setOnAction(ON_PATH_LINK_ACTION);
        link.setTooltip(new Tooltip(path.toString()));
        link.setUserData(path);
        _pathLink.set(link);

        /*
         * We could place the Hyperlink directly in a TableView cell,
         * but we use an HBox instead so that we can add a folder icon.
         */
        final HBox box = new HBox();
        box.setAlignment(Pos.CENTER_LEFT);
        if (attrs.isDirectory()) {
            final Label label = IconControl.FOLDER.create(Label.class);
            box.getChildren().add(label);
        }
        box.getChildren().add(link);
        _pathBox.set(box);

        long size = 0L;
        FileTime time;
        try {
            // directories are assumed to have no size
            if (!attrs.isDirectory()) size = Files.size(path);
            time = Files.getLastModifiedTime(path);
        }
        catch (IOException | SecurityException e) {
            size = 0L;
            time = FileTime.fromMillis(0L);
        }
        _pathSize.set(size);
        _pathTime.set(time);
    }

    /**
     * Gets the original {@link Path} of the {@link DirectoryEntry}.
     * @return the original {@link Path} on which other property values are based
     */
    public Path getPath() { return _path; }

    /**
     * Gets the {@link Hyperlink} to the wrapped {@link Path}.
     * @return the value of {@link #pathLinkProperty}
     */
    public final Hyperlink getPathLink() { return _pathLink.get(); }
    /**
     * Defines the {@link Hyperlink} to the wrapped {@link Path}.
     * @return the {@link ReadOnlyObjectProperty} holding the value
     */
    public ReadOnlyObjectProperty<Hyperlink> pathLinkProperty() {
        return _pathLink.getReadOnlyProperty();
    }

    /**
     * Gets the visual representation of the wrapped {@link Path}.
     * @return the value of {@link #pathBoxProperty}
     */
    public final HBox getPathBox() { return _pathBox.get(); }
    /**
     * Defines the visual representation of the wrapped {@link Path}.
     * @return the {@link ReadOnlyObjectProperty} holding the value
     */
    public ReadOnlyObjectProperty<HBox> pathBoxProperty() {
        return _pathBox.getReadOnlyProperty();
    }

    /**
     * Gets the size in bytes of the wrapped {@link Path}.
     * @return the value of {@link #pathSizeProperty}
     */
    public final long getPathSize() { return _pathSize.get(); }
    /**
     * Defines the size in bytes of the wrapped {@link Path}.
     * @return the {@link ReadOnlyLongProperty} holding the value
     */
    public ReadOnlyLongProperty pathSizeProperty() {
        return _pathSize.getReadOnlyProperty();
    }

    /**
     * Gets the newest {@link FileTime} of the wrapped {@link Path}.
     * @return the value of {@link #pathTimeProperty}
     */
    public final FileTime getPathTime() { return _pathTime.get(); }
    /**
     * Defines the newest {@link FileTime} of the wrapped {@link Path}.
     * @return the {@link ReadOnlyObjectProperty} holding the value
     */
    public ReadOnlyObjectProperty<FileTime> pathTimeProperty() {
        return _pathTime.getReadOnlyProperty();
    }

    /**
     * Handles action events for file links in a {@link DirectoryEntry}.
     */
    private static class PathLinkActionHandler implements EventHandler<ActionEvent> {

        /**
         * Handles the specified {@link ActionEvent} sent from a {@link Hyperlink}.
         * @param e the {@link ActionEvent} to handle
         */
        @Override
        public void handle(ActionEvent e) {
            final Hyperlink link = (Hyperlink) e.getSource();
            final Path path = (Path) link.getUserData();
            if (Files.isDirectory(path))
                Selection.INSTANCE.setDirectory(path);
            else
                Selection.INSTANCE.setDocument(path);
        }
    }
}
