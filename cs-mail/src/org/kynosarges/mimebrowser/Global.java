package org.kynosarges.mimebrowser;

import java.io.*;
import java.time.*;
import java.util.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;

/**
 * Provides global resources for the application.
 * @author Christoph Nahr
 * @version 1.3.3
 */
class Global {

    private static Image _applicationIcon;
    private static Font _defaultFont, _highlightFont;
    private static Stage _primaryStage;
    private static ResourceBundle _stringBundle;
    private static String _styles;

    /**
     * Creates a {@link Global} instance. Private to prevent instantiation.
     */
    private Global() { }

    /**
     * Gets the embedded icon for the application.
     * @return the embedded icon for the application
     */
    static Image applicationIcon() {
        if (_applicationIcon == null)
            _applicationIcon = new Image(
                    Global.class.getResourceAsStream("/resources/AppIcon.png"));

        return _applicationIcon;
    }

   /**
     * The standard {@link Button} width for the application, in pixels.
     */
    static final double BUTTON_WIDTH = 50;

    /**
     * Gets the custom CSS style sheet for the application.
     * @return the URL for the custom CSS style sheet
     */
    static String customStyles() {
        if (_styles == null)
            _styles = Global.class.getResource("/resources/styles.css").toExternalForm();

        return _styles;
    }

    /**
     * Gets the default {@link Font} for the application.
     * @return the default {@link Font} for the application.
     */
    static Font defaultFont() {
        if (_defaultFont == null)
            _defaultFont = Font.getDefault();

        return _defaultFont;
    }

    /**
     * Formats the specified {@link Instant} for display.
     * The format is YYYY-MM-DD HH:MM:SS using the date counting
     * of the current {@link ZoneId} and a 24-hour clock.
     *
     * @param instant the {@link Instant} to format
     * @return a formatted string that represents {@code instant}
     */
    static String formatInstant(Instant instant) {
        final LocalDateTime zoned = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return String.format("%04d-%02d-%02d %02d:%02d:%02d",
                zoned.getYear(), zoned.getMonthValue(), zoned.getDayOfMonth(),
                zoned.getHour(), zoned.getMinute(), zoned.getSecond());
    }

    /**
     * Gets the highlight {@link Font} for the application.
     * Same as {@link #defaultFont} but bold and italicized.
     * @return the highlight {@link Font} for the application.
     */
    static Font highlightFont() {
        if (_highlightFont == null) {
            final Font font = defaultFont();
            _highlightFont = Font.font(font.getFamily(),
                    FontWeight.BOLD, FontPosture.ITALIC, font.getSize());
        }

        return _highlightFont;
    }

    /**
     * Gets the string resource with the specified key.
     * @param key the key for the desired string resource
     * @return the string resource for {@code key}
     * @exception MissingResourceException if {@code key} was not found
     */
    static String getString(String key) {
        if (_stringBundle == null)
            _stringBundle = ResourceBundle.getBundle("resources/Strings");

        return _stringBundle.getString(key);
    }

    /**
     * Determines whether the specified {@link String} has any content.
     * @param s the {@link String} to examine
     * @return {@code true} if {@code s} is neither {@code null} nor empty
     */
    static boolean hasContent(String s) {
        return (s != null && !s.isEmpty());
    }

    /**
     * Gets the primary {@link Stage} of the application.
     * Returns {@code null} until {@link #setPrimaryStage} is called.
     *
     * @return the primary {@link Stage} of the application
     */
    static Stage primaryStage() {
        return _primaryStage;
    }

    /**
     * Sets the primary {@link Stage} of the application.
     * @param primaryStage the primary {@link Stage} of the application
     * @throws IllegalStateException if the method has already been called
     * @throws NullPointerException if {@code primaryStage} is {@code null}
     */
    static void setPrimaryStage(Stage primaryStage) {
        if (_primaryStage != null)
            throw new IllegalStateException("method already called");
        if (primaryStage == null)
            throw new NullPointerException("primaryStage");

        _primaryStage = primaryStage;
    }

    /**
     * Shows a modal {@link Alert} with the specified error message.
     * @param owner the {@link Window} that owns the {@link Alert}
     * @param header the header text of the {@link Alert}
     * @param e the {@link Exception} providing content for the {@link Alert}
     */
    public static void showAlert(Window owner, String header, Exception e) {

        String content = getString("dlgErrorUnknown");
        String stackTrace = getString("dlgErrorStack");
        if (e != null) {
            content = e.toString();
            final StringWriter writer = new StringWriter();
            try (PrintWriter print = new PrintWriter(writer)) {
                e.printStackTrace(print);
                stackTrace = writer.toString();
            }
        }

        final TextArea textArea = new TextArea(stackTrace);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);

        final Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(owner == null ? primaryStage() : owner);
        alert.setTitle(getString("dlgErrorTitle"));
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.getDialogPane().setExpandableContent(new StackPane(textArea));
        alert.showAndWait();
    }
}
