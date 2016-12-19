package org.kynosarges.mimebrowser;

import java.util.regex.Pattern;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * Provides interactive controls to search a directory tree.
 * @author Christoph Nahr
 * @version 1.3.3
 */
public class DirSearchView extends HBox {

    private final TextField _filterText = new TextField();
    private final ComboBox<SearchMode> _filterMode = new ComboBox<>();

    /**
     * Creates an empty {@link DirSearchView}.
     */
    public DirSearchView() {
        final Selection selection = Selection.INSTANCE;

        final CheckBox combineToggle = new CheckBox(Global.getString("ctlCombine"));
        combineToggle.setTooltip(new Tooltip(Global.getString("ctlCombineTip")));
        combineToggle.setAllowIndeterminate(false);
        combineToggle.setMnemonicParsing(true);
        combineToggle.selectedProperty().bindBidirectional(selection.combineProperty());

        final ComboBox<Integer> combineLevel = new ComboBox<>();
        combineLevel.setTooltip(new Tooltip(Global.getString("ctlCombineLevelsTip")));
        combineLevel.setMinWidth(42);
        combineLevel.getItems().addAll(2, 3, 4, 5, Integer.MAX_VALUE);
        combineLevel.setValue(2);
        selection.combineLevelsProperty().bind(combineLevel.getSelectionModel().selectedItemProperty());

        // translate magic value into "All" label
        combineLevel.setButtonCell(new CombineLevelCell());
        combineLevel.setCellFactory(p -> new CombineLevelCell());

        final CheckBox filterToggle = new CheckBox(Global.getString("ctlFilter"));
        filterToggle.setTooltip(new Tooltip(Global.getString("ctlFilterTip")));
        filterToggle.setAllowIndeterminate(false);
        filterToggle.setMnemonicParsing(true);
        filterToggle.selectedProperty().bindBidirectional(selection.filterProperty());

        _filterText.setTooltip(new Tooltip(Global.getString("ctlFilterPatternTip")));
        _filterText.setEditable(true);
        _filterText.setPromptText(Global.getString("lblSearch"));
        _filterText.textProperty().addListener((v, o, n) -> updateFilterPattern());
        HBox.setHgrow(_filterText, Priority.ALWAYS);

        _filterMode.setTooltip(new Tooltip(Global.getString("ctlFilterModeTip")));
        _filterMode.setMinWidth(60);
        _filterMode.getItems().addAll(SearchMode.values());
        _filterMode.getSelectionModel().select(0);
        _filterMode.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> updateFilterPattern());

        final ComboBox<SearchTarget> filterTarget = new ComboBox<>();
        filterTarget.setTooltip(new Tooltip(Global.getString("ctlFilterTargetTip")));
        filterTarget.setMinWidth(60);
        filterTarget.getItems().addAll(SearchTarget.values());
        selection.filterTargetProperty().bind(filterTarget.getSelectionModel().selectedItemProperty());
        filterTarget.getSelectionModel().select(0);

        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(4));
        setSpacing(4);

        getChildren().addAll(combineToggle, combineLevel,
                new Separator(Orientation.VERTICAL),
                filterToggle, _filterText, _filterMode, filterTarget);
    }

    /**
     * Updates the filter pattern based on the current user input.
     */
    private void updateFilterPattern() {

        int flags = Pattern.CANON_EQ + Pattern.UNICODE_CASE;
        SearchMode mode = _filterMode.getValue();
        if (mode == null || mode == SearchMode.SIMPLE)
            flags += Pattern.CASE_INSENSITIVE + Pattern.LITERAL;
        else if (mode == SearchMode.CASE)
            flags += Pattern.LITERAL;

        Pattern pattern;
        String color;
        try {
            pattern = Pattern.compile(_filterText.getText(), flags);
            color = "black";
        }
        catch (Exception e) {
            // Pattern.compile can throw anything on error
            pattern = Selection.EMPTY_PATTERN;
            color = "red";
        }

        // highlight invalid pattern in red
        _filterText.setStyle(String.format("-fx-text-fill: %s;", color));
        Selection.INSTANCE.setFilterPattern(pattern);
    }

    /**
     * Formats combined level counts for display.
     */
    private static class CombineLevelCell extends ListCell<Integer> {

        /**
         * Updates the {@link CombineLevelCell} with the specified level count.
         * @param item the new level count to display
         * @param empty {@code true} if the cell is empty, else {@code false}
         */
        @Override
        protected void updateItem(Integer item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null) return;
            setText(item == Integer.MAX_VALUE ?
                    Global.getString("lblAll") : item.toString());
        }
    }
}
