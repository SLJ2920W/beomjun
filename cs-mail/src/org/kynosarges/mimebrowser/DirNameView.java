package org.kynosarges.mimebrowser;

import java.nio.file.*;

import javafx.beans.value.*;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * Provides an interactive view on the name components of a directory.
 * @author Christoph Nahr
 * @version 1.3.3
 */
public class DirNameView extends HBox {

    private final HBox _nameBox = new HBox();

    /**
     * Creates an empty {@link DirNameView}.
     */
    public DirNameView() {
        Selection.INSTANCE.addDirectoryListener(new DirectoryChangeListener());

        final Button choose = IconControl.BROWSE.create(Button.class);
        choose.setOnAction(e -> Selection.INSTANCE.chooseDirectory());

        final Button refresh = IconControl.REFRESH.create(Button.class);
        refresh.setOnAction(e -> Selection.INSTANCE.refresh());

        final Button about = IconControl.ABOUT.create(Button.class);
        about.setOnAction(e -> new AboutDialog().showAndWait());

        setAlignment(Pos.CENTER);
        setPadding(new Insets(4));
        setSpacing(4);

        HBox.setHgrow(_nameBox, Priority.ALWAYS);
        _nameBox.setAlignment(Pos.CENTER_LEFT);
        _nameBox.setSpacing(1);

        getChildren().addAll(choose, refresh, _nameBox, about);
    }

    /**
     * Selects the parent component of the current selection, if any.
     */
    public void selectParent() {
        final ObservableList<Node> children = _nameBox.getChildren();

        // two children per component: Label/Hyperlink sequence
        final int parentIndex = children.size() - 3;
        if (parentIndex >= 0)
            ((Hyperlink) children.get(parentIndex)).fire();
    }

    /**
     * Listens to directory changes and updates the {@link DirNameView} accordingly.
     */
    private class DirectoryChangeListener implements ChangeListener<Path> {

        /**
         * Updates the {@link DirNameView} with the specified new directory.
         * @param value the {@link ObservableValue} that has changed
         * @param oldDir the old directory
         * @param newDir the new directory
         */
        @Override
        public void changed(ObservableValue<? extends Path> value, Path oldDir, Path newDir) {
            final ObservableList<Node> children = _nameBox.getChildren();
            children.clear();

            if (newDir == null || (newDir.getRoot() == null && newDir.getNameCount() == 0)) {
                children.add(new Label(Global.getString("lblDirNone")));
                return;
            }

            final String separator = newDir.getFileSystem().getSeparator();

            // build component sequence from right to left
            while (newDir != null) {
                Path name = newDir.getFileName();
                if (name == null) name = newDir.getRoot();
                if (name == null) break;

                // HACK: Path annoyingly stores X:\ for Windows drive roots,
                // so we need to check for and remove the trailing separator
                String nameString = name.toString();
                if (nameString.endsWith(separator))
                    nameString = nameString.substring(0, nameString.length() - separator.length());

                // backspace is shortcut for immediate parent
                String tip = newDir.toString();
                if (children.size() == 2)
                    tip = String.format("%s %s", tip, Global.getString("lblAltBackspace"));

                // store path up to current directory level
                final Hyperlink link = new Hyperlink(nameString);
                link.setTooltip(new Tooltip(tip));
                link.setUserData(newDir);
                children.add(0, link);

                link.setOnAction(e -> {
                    final Hyperlink source = (Hyperlink) e.getSource();
                    final Path path = (Path) source.getUserData();
                    Selection.INSTANCE.setDirectory(path);
                });

                // add separator between directory levels
                newDir = newDir.getParent();
                if (newDir != null)
                    children.add(0, IconControl.SUBDIR.create(Label.class));
            }
        }
    }
}
