package org.kynosarges.mimebrowser;

import java.nio.file.*;
import java.nio.file.attribute.*;

import javafx.beans.value.*;
import javafx.collections.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;

/**
 * Provides an interactive view on the entries of a directory.
 * @author Christoph Nahr
 * @version 1.3.3
 */
public class DirEntryView extends TableView<DirectoryEntry> {

    private static final double TIME_WIDTH = 144;
    private static final double SIZE_WIDTH = 96;

    private TableColumn<DirectoryEntry, HBox> _pathColumn;

    /**
     * Creates an empty {@link DirEntryView}.
     */
    public DirEntryView() {

        getSelectionModel().setCellSelectionEnabled(false);
        getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.SPACE) {
                final DirectoryEntry entry = getSelectionModel().getSelectedItem();
                if (entry != null) entry.getPathLink().fire();
            }
        });

        addListeners();
        createColumns();
    }

    /**
     * Initialize variable {@link TableColumn} widths.
     * Separate method because {@link TableView} provides no reliable size
     * information until after the stage has been fully built and shown.
     */
    void initColumnWidths() {

        // path column fills space not occupied by other columns plus scrollbar
        final double otherSpace = TIME_WIDTH + SIZE_WIDTH + 16;
        _pathColumn.setPrefWidth(getWidth() - otherSpace);

        // automatically shrink & expand path column with table width
        widthProperty().addListener((v, oldWidth, newWidth) -> {
            if (oldWidth == null || newWidth == null)
                return;

            double oldDouble = (double) oldWidth;
            double newDouble = (double) newWidth;
            if (oldDouble == 0 || newDouble == 0)
                return;

            _pathColumn.setPrefWidth(_pathColumn.getWidth() + newDouble - oldDouble);
        });
    }

    /**
     * Attaches listeners to {@link Selection} properties.
     * Unchecked to allow the reuse of untyped listeners.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void addListeners() {

        Selection selection = Selection.INSTANCE;
        selection.addDocumentListener(new DocumentChangeListener());

        // always update entries
        final ChangeListener update = (v, oldValue, newValue) -> showSelection();
        selection.addDirectoryListener(update);
        selection.combineProperty().addListener(update);
        selection.filterProperty().addListener(update);

        // update entries only when combining enabled
        selection.combineLevelsProperty().addListener((v, oldValue, newValue) -> {
            if (Selection.INSTANCE.getCombine()) showSelection();
        });

        // update entries only when filtering enabled
        final ChangeListener filterUpdate = (v, oldValue, newValue) -> {
            if (Selection.INSTANCE.getFilter()) showSelection();
        };
        selection.filterTargetProperty().addListener(filterUpdate);
        selection.filterPatternProperty().addListener(filterUpdate);
    }

    /**
     * Creates the columns of the {@link DirEntryView}.
     * Unchecked to allow the reuse of untyped cell value factories.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void createColumns() {

        _pathColumn = new TableColumn<>(Global.getString("lblName"));
        _pathColumn.setCellValueFactory(new PropertyValueFactory("pathBox"));
        _pathColumn.setMinWidth(TIME_WIDTH);

        // extract sortable strings from HBox values of Path column
        _pathColumn.setComparator((HBox x, HBox y) -> {

            // always sort folders before files
            final int xlast = x.getChildren().size() - 1;
            final int ylast = y.getChildren().size() - 1;
            if (xlast != ylast) return ylast - xlast;

            // sort alphabetically within files or folders
            final Hyperlink xl = (Hyperlink) x.getChildren().get(xlast);
            final Hyperlink yl = (Hyperlink) y.getChildren().get(ylast);
            return String.CASE_INSENSITIVE_ORDER.compare(xl.getText(), yl.getText());
        });

        final TableColumn<DirectoryEntry, FileTime> timeColumn = new TableColumn<>(Global.getString("lblModified"));
        timeColumn.setCellValueFactory(new PropertyValueFactory("pathTime"));
        timeColumn.setCellFactory(p -> new FileTimeCell());
        timeColumn.setMinWidth(TIME_WIDTH - 24);
        timeColumn.setPrefWidth(TIME_WIDTH);

        final TableColumn<DirectoryEntry, Long> sizeColumn = new TableColumn<>(Global.getString("lblSize"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory("pathSize"));
        sizeColumn.setCellFactory(p -> new FileSizeCell());
        sizeColumn.setMinWidth(SIZE_WIDTH);
        sizeColumn.setPrefWidth(SIZE_WIDTH);

        getColumns().setAll(_pathColumn, timeColumn, sizeColumn);
    }

    /**
     * Shows the currently selected directory entries.
     */
    private void showSelection() {
        SelectionWalker.INSTANCE.walk();

        // show folders before files (lost on manual re-sorting!)
        ObservableList<DirectoryEntry> list = FXCollections.observableArrayList();
        list.addAll(SelectionWalker.INSTANCE.getFolders());
        list.addAll(SelectionWalker.INSTANCE.getFiles());
        setItems(list);

        getSelectionModel().clearSelection();
    }

    /**
     * Listens to document changes and updates the {@link DirEntryView} accordingly.
     */
    private class DocumentChangeListener implements ChangeListener<Path> {

        /**
         * Updates the {@link DirEntryView} with the specified new document.
         * @param value the {@link ObservableValue} that has changed
         * @param oldDoc the old document
         * @param newDoc the new document
         */
        @Override
        public void changed(ObservableValue<? extends Path> value, Path oldDoc, Path newDoc) {
            getSelectionModel().clearSelection();

            for (DirectoryEntry entry: getItems())
                if (entry.getPath().equals(newDoc)) {
                    getSelectionModel().select(entry);
                    return;
                }
        }
    }

    /**
     * Formats file sizes in bytes for display.
     */
    private static class FileSizeCell extends TableCell<DirectoryEntry, Long> {

        /**
         * Updates the {@link FileSizeCell} with the specified file size in bytes.
         * @param item the new file size in bytes to display
         * @param empty {@code true} if the cell is empty, else {@code false}
         */
        @Override
        protected void updateItem(Long item, boolean empty) {
            super.updateItem(item, empty);

            if (item == null || ((long) item) == 0L) {
                setText("—");
                setAlignment(Pos.CENTER);
            } else {
                long size = (long) item;
                setText(String.format("%,d", size));
                setAlignment(Pos.CENTER_RIGHT);
            }
        }
    }

    /**
     * Formats {@link FileTime} items for display.
     */
    private static class FileTimeCell extends TableCell<DirectoryEntry, FileTime> {

        /**
         * Updates the {@link FileTimeCell} with the specified {@link FileTime}.
         * @param item the new {@link FileTime} to display
         * @param empty {@code true} if the cell is empty, else {@code false}
         */
        @Override
        protected void updateItem(FileTime item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                setText(Global.formatInstant(item.toInstant()));
                setAlignment(Pos.CENTER);
            }
        }
    }
}
