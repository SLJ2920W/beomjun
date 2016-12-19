package org.kynosarges.mimebrowser;

import java.nio.file.*;
import java.util.*;

import javafx.application.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;

/**
 * Defines the MIME Browser application.
 * @author Christoph Nahr
 * @version 1.3.1
 */
public class MimeBrowser extends Application {

    private DirNameView _dirNames;
    private DirSearchView _dirSearch;
    private DirEntryView _dirEntries;
    private DocumentView _docView;

    /**
     * Starts the MIME Browser application.
     * @param primaryStage the primary {@link Stage} for the application
     */
    @Override
    public void start(final Stage primaryStage) {

        Global.setPrimaryStage(primaryStage);
        primaryStage.getIcons().add(Global.applicationIcon());
        DocumentType.readConfig("MimeBrowser.cfg");

        _dirNames = new DirNameView();
        _dirSearch = new DirSearchView();
        _dirEntries = new DirEntryView();
        _docView = new DocumentView();

        final SplitPane split = new SplitPane();
        split.setOrientation(Orientation.VERTICAL);
        split.getItems().addAll(_dirEntries, _docView);
        split.setDividerPosition(0, 0.33);

        final GridPane grid = new GridPane();
        grid.add(_dirNames, 0, 0);
        grid.add(_dirSearch, 0, 1);
        grid.add(split, 0, 2);

        GridPane.setHgrow(_dirNames, Priority.ALWAYS);
        GridPane.setHgrow(split, Priority.ALWAYS);
        GridPane.setVgrow(split, Priority.ALWAYS);

        final Scene scene = new Scene(grid);
        scene.getStylesheets().add(Global.customStyles());
        scene.setOnKeyPressed(new KeyEventHandler());

        primaryStage.setTitle("MIME Browser");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();

        // accept files dragged anywhere over the window
        scene.setOnDragOver(t -> {
            if (t.getDragboard().hasFiles())
                t.acceptTransferModes(TransferMode.COPY);
            t.consume();
        });
        scene.setOnDragDropped(new DragDropHandler());

        final Path path = getInitialPath(getParameters().getUnnamed());
        Selection.INSTANCE.setDirectory(path);
        Selection.INSTANCE.setDocument(path);

        primaryStage.centerOnScreen();
        primaryStage.show();

        // reliable sizes available after show()
        _dirEntries.initColumnWidths();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be launched through
     * deployment artifacts, e.g., in IDEs with limited FX support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Gets the initial {@link Path} for the application.
     * @param params the unnamed parameters supplied on the command line
     * @return the {@link Path} to the initial file or directory if found, else {@code null}
     */
    private static Path getInitialPath(List<String> params) {
        Path path = null;

        // first try: check first parameter if present
        if (params != null && params.size() > 0) {
            try {
                path = Paths.get(params.get(0));
            } catch (InvalidPathException e) {
                path = null; // ignore invalid specified path
            }
        }

        // second try: check user's home directory
        if (path == null) {
            final String home = System.getProperty("user.home");
            try {
                path = Paths.get(home);
            } catch (InvalidPathException e) {
                path = null; // unlikely but handled gracefully
            }
        }

        return path;
    }

    /**
     * Handles drag &amp; drop operations from the operating system.
     * Sets the first transmitted {@link Path}, if any, as the new {@link Selection}.
     */
    private static class DragDropHandler implements EventHandler<DragEvent> {

        /**
         * Handles the specified {@link DragEvent}.
         * @param event the {@link DragEvent} to handle
         */
        @Override public void handle(DragEvent event) {
            boolean success = false;
            final Dragboard board = event.getDragboard();

            if (board.hasFiles()) {
                final List<java.io.File> files = board.getFiles();
                if (files != null && files.size() > 0) {
                    final Path path = files.get(0).toPath();
                    Selection.INSTANCE.setDirectory(path);
                    Selection.INSTANCE.setDocument(path);
                    success = true;
                }
            }

            event.setDropCompleted(success);
            event.consume();
        }
    }

    /**
     * Handles shortcut keys for the {@link MimeBrowser} application.
     */
    private class KeyEventHandler implements EventHandler<KeyEvent> {

        /**
         * Handles the specified {@link KeyEvent}.
         * @param e the {@link KeyEvent} to handle
         */
        @Override
        public void handle(KeyEvent e) {
            final Selection selection = Selection.INSTANCE;
            if (e.isAltDown()) {
                /*
                 * Even though mnemonic parsing is enabled for these toggle buttons,
                 * we still need to manually toggle their states since the built-in
                 * JavaFX mnemonic parsing merely sets input focus on them.
                 */
                switch (e.getCode()) {
                    case C:
                        selection.setCombine(!selection.getCombine()); break;
                    case F:
                        selection.setFilter(!selection.getFilter()); break;
                    case BACK_SPACE:
                        _dirNames.selectParent(); break;
                }
            } else if (e.isControlDown()) {
                switch (e.getCode()) {
                    case O:
                        selection.chooseDirectory(); break;
                    case S:
                        if (e.isShiftDown())
                            _docView.saveAll();
                        else
                            _docView.save();
                        break;
                    case T:
                        _docView.showUnknownAsText(); break;
                    case SUBTRACT: case MINUS:
                        _docView.zoom(-1); break;
                    case ADD: case PLUS:
                        _docView.zoom(+1); break;
                }
            } else if (!e.isMetaDown() && !e.isShiftDown()) {
                switch (e.getCode()) {
                    case F1:
                        new AboutDialog().showAndWait(); break;
                    case F5:
                        selection.refresh(); break;
                }
            }
        }
    }
}
