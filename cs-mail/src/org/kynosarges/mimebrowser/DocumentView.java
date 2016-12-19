package org.kynosarges.mimebrowser;

import java.io.*;
import java.nio.file.*;
import java.time.Instant;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;

import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.web.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

/**
 * Provides an interactive view on the contents of a {@link Document}.
 * @author Christoph Nahr
 * @version 1.3.4
 */
public final class DocumentView extends VBox {

    private final Hyperlink _pathLink = new Hyperlink();
    private final ComboBox<DocumentPart> _partsCombo = new ComboBox<>();
    private final Button _showTextButton, _saveButton, _saveAllButton;
    private final Label _zoomLabel = new Label();
    private final TextArea _textView = new TextArea();
    private final WebView _webView = new WebView();

    private int _scaleStep = 0;
    private String _textFont = "", _textSize = "";

    /**
     * Creates a {@link DocumentView}.
     */
    public DocumentView() {
        Selection.INSTANCE.addDocumentListener((v, oldDoc, newDoc) -> read(newDoc, false));

        // file path to entire document, shown as hyperlink
        if (DesktopAction.canOpen()) {
            _pathLink.setTooltip(new Tooltip(Global.getString("ctlOpenWithTip")));
            _pathLink.setOnAction(new HyperlinkHandler());
        } else
            _pathLink.setTooltip(new Tooltip(Global.getString("ctlUnsupportedTip")));

        // document metadata from MIME envelope, if present
        final GridPane grid = new GridPane();
        grid.getColumnConstraints().add(new ColumnConstraints(EnvelopeRow.LABEL_WIDTH));
        grid.getColumnConstraints().add(new ColumnConstraints(1, 1, Double.MAX_VALUE));

        final Document doc = Document.INSTANCE;
        doc.mimeSubjectProperty().addListener(new EnvelopeRow(grid, 0, "envelopeSubject"));
        doc.mimeDateProperty().addListener(new EnvelopeRow(grid, 1, "envelopeDate"));
        doc.mimeFromProperty().addListener(new EnvelopeRow(grid, 2, "envelopeFrom"));
        doc.mimeReplyProperty().addListener(new EnvelopeRow(grid, 3, "envelopeReply"));
        doc.mimeToProperty().addListener(new EnvelopeRow(grid, 4, "envelopeTo"));
        doc.mimeToCcProperty().addListener(new EnvelopeRow(grid, 5, "envelopeCC"));
        doc.mimeToBccProperty().addListener(new EnvelopeRow(grid, 6, "envelopeBCC"));
        doc.mimeFlagsProperty().addListener(new EnvelopeRow(grid, 7, "envelopeFlags"));
        doc.mimeXmailerProperty().addListener(new EnvelopeRow(grid, 8, "envelopeXMailer"));

        // selector for MIME document parts, if any
        final Label partsLabel = new Label(Global.getString("ctlPart"));
        partsLabel.setFont(Global.highlightFont());
        grid.add(partsLabel, 0, 9);
        GridPane.setMargin(partsLabel, EnvelopeRow.LABEL_INSETS);

        _partsCombo.setEditable(false);
        _partsCombo.setTooltip(new Tooltip(Global.getString("ctlPartTip")));
        _partsCombo.setItems(Document.INSTANCE.getParts());
        _partsCombo.getSelectionModel().selectedItemProperty().addListener(
                (v, oldPart, newPart) -> showPart(newPart));
        grid.add(_partsCombo, 1, 9);
        GridPane.setMargin(_partsCombo, new Insets(4, 4, 4, 0));

         // extend combo box to available width
        _partsCombo.prefWidthProperty().bind(
                grid.widthProperty().subtract(EnvelopeRow.LABEL_WIDTH));
        GridPane.setHgrow(_partsCombo, Priority.ALWAYS);

        // controls for currently shown document part
        _showTextButton = IconControl.SHOW_TEXT.create(Button.class);
        _showTextButton.setDisable(true);
        _showTextButton.setOnAction(e -> showUnknownAsText());

        _saveButton = IconControl.SAVE.create(Button.class);
        _saveButton.setDisable(true);
        _saveButton.setOnAction(e -> save());

        _saveAllButton = IconControl.SAVE_ALL.create(Button.class);
        _saveAllButton.setDisable(true);
        _saveAllButton.setOnAction(e -> saveAll());

        _zoomLabel.setAlignment(Pos.CENTER);
        _zoomLabel.setMinWidth(48);
        _zoomLabel.setText("100%");
        _zoomLabel.setTooltip(new Tooltip(Global.getString("ctlZoomTip")));

        final Button zoomIn = IconControl.ZOOM_IN.create(Button.class);
        zoomIn.setOnAction(e -> zoom(+1));

        final Button zoomOut = IconControl.ZOOM_OUT.create(Button.class);
        zoomOut.setOnAction(e -> zoom(-1));

        final ComboBox<String> fontCombo = new ComboBox<>();
        fontCombo.setEditable(false);
        fontCombo.setTooltip(new Tooltip(Global.getString("ctlFontTip")));
        fontCombo.setItems(FXCollections.observableArrayList(Font.getFamilies()));
        fontCombo.getSelectionModel().select(Global.defaultFont().getFamily());
        fontCombo.getSelectionModel().selectedItemProperty().addListener((v, oldFamily, newFamily) -> {
            if (newFamily != null && newFamily.length() != 0) {
                _textFont = String.format("-fx-font-family: \"%s\";", newFamily);
                _textView.setStyle(_textFont + _textSize);
            }
        });

        final HBox fontBox = new HBox();
        fontBox.setAlignment(Pos.CENTER_LEFT);
        fontBox.setPadding(new Insets(4));
        fontBox.setSpacing(4);
        fontBox.getChildren().addAll(_showTextButton,
                _saveButton, _saveAllButton, _zoomLabel, zoomIn, zoomOut, fontCombo);

        _webView.setContextMenuEnabled(false);
        _webView.setFocusTraversable(false);
        _webView.setFontScale(1);
        _webView.getEngine().setJavaScriptEnabled(false);

        _textView.setEditable(false);
        _textView.setFocusTraversable(false);
        _textView.setWrapText(true);
        // TextView automatically extends to available width, but not to height
        _textView.prefHeightProperty().bind(heightProperty().subtract(_textView.getLayoutY()));

        clear();
        getChildren().addAll(_pathLink, new Separator(), grid, fontBox, _textView);
    }

    /**
     * Saves the current {@link DocumentPart} to a user-specified file.
     * Does nothing if there is no current {@link Document}.
     */
    public void save() {

        // first we need a valid document
        final Document doc = Document.INSTANCE;
        if (doc.getPath() == null) return;
        String fileName = doc.getPath().getFileName().toString();

        // MIME messages also need a valid part
        Part part = null;
        if (doc.getType() == DocumentType.MIME_MESSAGE) {
            final DocumentPart docPart = _partsCombo.getSelectionModel().getSelectedItem();
            if (docPart == null) return;

            part = docPart.getMimePart();
            try {
                final String partName = part.getFileName();
                if (partName != null && partName.length() > 0)
                    fileName = partName;
                else {
                    // correct extension if possible
                    fileName = DocumentType.MIME_MESSAGE.trimExtension(fileName);
                    fileName += docPart.getDocumentType().extension();
                }
            }
            catch (MessagingException e) {
                // JavaMail: unclear why getFileName would ever throw here
            }
        }

        // ask user for save location
        final FileChooser chooser = new FileChooser();
        final Path dir = getDirectory(doc.getPath());
        if (dir != null) chooser.setInitialDirectory(dir.toFile());

        chooser.setInitialFileName(fileName);
        chooser.setTitle(String.format(Global.getString("dlgSaveTitle"), fileName));
        final File file = chooser.showSaveDialog(Global.primaryStage());
        if (file == null) return;

        try {
            if (part == null)
                Files.copy(doc.getPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            else if (part instanceof MimeBodyPart)
                ((MimeBodyPart) part).saveFile(file);
            else
                try (OutputStream stream = new FileOutputStream(file)) {
                    part.writeTo(stream);
                }
        }
        catch (IOException | MessagingException e) {
            Global.showAlert(null, Global.getString("dlgSaveError"), e);
        }
    }

    /**
     * Saves all parts of the current {@link Document} to a user-specified directory.
     * Does nothing if there is no current {@link Document},
     * or if it is not a {@link DocumentType#MIME_MESSAGE}.
     */
    public void saveAll() {

        // first we need a valid MIME message
        final Document doc = Document.INSTANCE;
        if (doc.getPath() == null || doc.getParts().isEmpty())
            return;

        // ask user for save location
        final DirectoryChooser chooser = new DirectoryChooser();
        Path dir = getDirectory(doc.getPath());
        if (dir != null) chooser.setInitialDirectory(dir.toFile());

        String fileName = doc.getPath().getFileName().toString();
        chooser.setTitle(String.format(Global.getString("dlgSaveAllTitle"), fileName));

        final File file = chooser.showDialog(Global.primaryStage());
        if (file == null) return;
        dir = file.toPath();

        // trim configured MIME message extensions if present
        fileName = DocumentType.MIME_MESSAGE.trimExtension(fileName);

        for (DocumentPart docPart: doc.getParts()) {
            if (docPart == null) continue;
            final Part part = docPart.getMimePart();
            try {
                // use stored file name, fall back on message name with part extension
                String partName = part.getFileName();
                if (partName == null || partName.length() == 0)
                    partName = fileName + docPart.getDocumentType().extension();

                // resolve file name to selected directory
                String uniquePartName = partName;
                String target = dir.resolve(uniquePartName).toString();

                // disambiguate duplicate file names
                int prefix = 1;
                while (Files.exists(Paths.get(target))) {
                    uniquePartName = String.format("%d_%s", prefix++, partName);
                    target = dir.resolve(uniquePartName).toString();
                }

                if (part instanceof MimeBodyPart)
                    ((MimeBodyPart) part).saveFile(target);
                else
                    try (OutputStream stream = new FileOutputStream(target)) {
                        part.writeTo(stream);
                    }
            }
            catch (IOException | MessagingException e) {
                Global.showAlert(null, Global.getString("dlgSaveError"), e);
            }
        }
    }

    /**
     * Shows the specified content in the {@link DocumentView}.
     * This method replaces the content shown in the {@link DocumentView},
     * but does not change the selected document path.
     *
     * @param content the text content to show
     * @param contentType the MIME type of the content
     */
    public void show(String content, String contentType) {
        _webView.getEngine().loadContent(content, contentType);
    }

    /**
     * Shows an unknown {@link Document} as plain text.
     */
    public void showUnknownAsText() {

        final Path path = Document.INSTANCE.getPath();
        if (path == null || !Files.exists(path)) return;

        final DocumentPart part = _partsCombo.getSelectionModel().getSelectedItem();
        if (part == null) read(path, true);
    }

    /**
     * Zooms the {@link DocumentView} in or out.
     * @param direction Any positive value to increase the zoom level,
     *                  or any negative value to decrease the zoom level.
     */
    public void zoom(int direction) {

        if (direction > 0 && _scaleStep < +4)
            ++_scaleStep;
        else if (direction < 0 && _scaleStep > -3)
            --_scaleStep;

        // change zoom level in 25% steps
        final int percent = 100 + _scaleStep * 25;
        _zoomLabel.setText(String.format("%d%%", percent));

        // TextArea requires size in em, WebView as ratio
        _textSize = String.format("-fx-font-size: %s;", (12 * percent) / 100.0);
        _textView.setStyle(_textFont + _textSize);
        _webView.setZoom(percent / 100.0);
    }

    /**
     * Clears and disables the {@link DocumentView}.
     */
    private void clear() {
        _pathLink.setDisable(true);
        _pathLink.setText(Global.getString("lblDocNone"));
        _pathLink.setUserData(null);

        _showTextButton.setDisable(true);
        _saveButton.setDisable(true);
        _saveAllButton.setDisable(true);

        _textView.setText("");
        _webView.getEngine().loadContent("");
    }

    /**
     * Gets the directory part of the specified {@link Path}.
     * @param path the {@link Path} to examine
     * @return {@code path} itself or its parent, if a valid directory, else {@code null}
     */
    private static Path getDirectory(Path path) {

        if (!Files.exists(path)) return null;
        if (Files.isDirectory(path)) return path;

        path = path.getParent();
        if (Files.exists(path) && Files.isDirectory(path))
            return path;

        return null;
    }

    /**
     * Reads the specified document with the specified type.
     * @param path the {@link Path} to the new document
     * @param unknownAsText {@code true} to read unrecognized files as plain text
     */
    private void read(Path path, boolean unknownAsText) {
        clear();
        Document.INSTANCE.clear();
        if (path == null || !Files.exists(path))
            return;

        _pathLink.setDisable(false);
        _pathLink.setText(path.toString());
        _pathLink.setUserData(path);

        String error = null;
        try {
            Document.INSTANCE.read(path, unknownAsText);
        }
        catch (IOException | MessagingException e) {
            error = String.format("%s%n%n%s", Global.getString("dlgOpenError"), e.toString());
        }

        if (error != null)
            showText(error, false);
        else {
            // take a guess at best content to show first
            final Document doc = Document.INSTANCE;
            if (doc.getParts().isEmpty())
                showPart(null); // show file info
            else if (doc.getFirstPlain() != null)
                _partsCombo.getSelectionModel().select(doc.getFirstPlain());
            else if (doc.getFirstHtml() != null)
                _partsCombo.getSelectionModel().select(doc.getFirstHtml());
            else
                _partsCombo.getSelectionModel().select(0);

            // allow saving document and any parts
            _saveButton.setDisable(false);
            if (doc.getParts().size() > 0)
                _saveAllButton.setDisable(false);
        }
    }

    /**
     * Shows the specified {@link DocumentPart}.
     * Shows information on the current {@link Document} if {@code part} is {@code null}.
     * Shows information on the specified {@code part} if it is not HTML or plain text.
     * @param part the {@link DocumentPart} to show
     */
    private void showPart(DocumentPart part) {
        if (part == null) {
            final Path path = Document.INSTANCE.getPath();
            if (path == null || !Files.exists(path)) {
                showText(Global.getString("lblDocNone"), false);
                _showTextButton.setDisable(true);
            } else {
                long size = 0L;
                Instant instant = Instant.EPOCH;
                try {
                    size = Files.size(path);
                    instant = Files.getLastModifiedTime(path).toInstant();
                }
                catch (IOException e) { }
                final String info = String.format(Global.getString("dlgDocInfo"),
                        path.getFileName(), Global.formatInstant(instant), size);
                showText(info, true);
                _showTextButton.setDisable(false);
            }
        } else {
            String text;
            boolean isHtml = true;
            if (part.getContent() instanceof String) {
                text = (String) part.getContent();
                isHtml = (part.getDocumentType() == DocumentType.TEXT_HTML);
            } else {
                final String name = (Global.hasContent(part.getFileName()) ?
                        part.getFileName() : Global.getString("lblUnnamed"));
                text = String.format(Global.getString("dlgPartInfo"), name, part.getSize());
            }
            showText(text, isHtml);
            _showTextButton.setDisable(true);
        }
    }

    /**
     * Shows the specified HTML or plain text.
     * @param text the text to show
     * @param isHtml {@code true} if {@code text} is HTML, else {@code false}
     */
    private void showText(String text, boolean isHtml) {
        if (isHtml) {
            if (!getChildren().contains(_webView)) {
                getChildren().remove(_textView);
                getChildren().add(_webView);
            }
            _webView.getEngine().loadContent(text, "text/html");
        } else {
            if (!getChildren().contains(_textView)) {
                getChildren().remove(_webView);
                getChildren().add(_textView);
            }
            _textView.setText(text);
        }
    }

    /**
     * Represents one row in the {@link GridPane} of MIME envelope fields.
     */
    private static class EnvelopeRow implements ChangeListener<String> {
        /**
         * Width of the {@link Label} for each MIME envelope field.
         */
        final static double LABEL_WIDTH = 60;
        /**
         * Margin of the {@link Label} for each MIME envelope field.
         */
        final static Insets LABEL_INSETS = new Insets(0, 0, 0, 4);
        /**
         * Margin of the {@link TextField} for each MIME envelope field.
         */
        private final static Insets TEXT_INSETS = new Insets(0, 4, 0, 0);

        private final GridPane _grid;
        private final int _row;
        private final Label _nameLabel = new Label();
        private final TextField _valueText = new TextField();
        private boolean _visible;

        /**
         * Creates an {@link EnvelopeRow} with the specified contents.
         * @param grid the containing {@link GridPane}
         * @param row the row index in the containing {@code grid}
         * @param key the key of the string resource for the field name
         */
        EnvelopeRow(GridPane grid, int row, String key) {
            _grid = grid;
            _row = row;

            _nameLabel.setText(Global.getString(key));
            _nameLabel.setFont(Global.highlightFont());
            GridPane.setMargin(_nameLabel, LABEL_INSETS);

            /*
             * TextField is editable to present visible cursor, also
             * used for horizontal scrolling as there's no scrollbar.
             * Adding and removing envelope fields screws up Tab sequences,
             * so we need to exclude the TextField from focus traversal.
             */
            _valueText.setEditable(true);
            _valueText.setFocusTraversable(false);

            GridPane.setMargin(_valueText, TEXT_INSETS);
            GridPane.setHgrow(_valueText, Priority.ALWAYS);
            GridPane.setVgrow(_valueText, Priority.NEVER);
        }

        /**
         * Handles changes to the MIME envelope field value.
         * @param ov the {@link ObservableValue} that has changed
         * @param oldValue the old {@link String} value
         * @param newValue the new {@link String} value
         */
        @Override
        public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue) {

            if (newValue == null || newValue.length() == 0) {
                _valueText.setText(null);
                if (_visible) {
                    _visible = false;
                    _grid.getChildren().remove(_nameLabel);
                    _grid.getChildren().remove(_valueText);
                }
            } else {
                _valueText.setText(newValue);
                if (!_visible) {
                    _visible = true;
                    _grid.add(_nameLabel, 0, _row);
                    _grid.add(_valueText, 1, _row);
                }
            }
        }
    }

    /**
     * Handles clicks on the {@link Hyperlink} for the document path.
     * Shows a {@link MessageDialog} if an error occurred.
     */
    private static class HyperlinkHandler implements EventHandler<ActionEvent> {

        /**
         * Handles the specified {@link ActionEvent}.
         * @param t the {@link ActionEvent} to handle
         */
        @Override public void handle(ActionEvent t) {
            try {
                final Hyperlink link = (Hyperlink) t.getSource();
                final Path path = (Path) link.getUserData();
                if (path != null) DesktopAction.open(path.toFile());
            }
            catch (IOException e) {
                Global.showAlert(null, Global.getString("dlgOpenWithError"), e);
            }
        }
    }
}
