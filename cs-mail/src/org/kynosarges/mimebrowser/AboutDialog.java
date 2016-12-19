package org.kynosarges.mimebrowser;

import java.io.IOException;
import java.net.*;
import java.nio.file.*;

import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;

/**
 * Shows a modal dialog with information about the application.
 * @author Christoph Nahr
 * @version 1.3.4
 */
public class AboutDialog extends Stage {

    private final static String TITLE, VERSION, AUTHOR, DATE;
    private final static String MAIL = "webmaster@kynosarges.org", MAIL_URI;
    private final static String SITE = "kynosarges.org/MimeBrowser.html";
    private final static String SITE_URI = "http://kynosarges.org/MimeBrowser.html";

    static {
        final Package pack = AboutDialog.class.getPackage();

        TITLE = pack.getSpecificationTitle();
        VERSION = pack.getSpecificationVersion();
        AUTHOR = pack.getSpecificationVendor();
        DATE = pack.getImplementationVersion();

        MAIL_URI = String.format("mailto:%s?subject=%s %s", MAIL, TITLE, VERSION)
                .replace(" ", "%20"); // embedded space in title!
    }

    /**
     * Creates an {@link AboutDialog}.
     */
    public AboutDialog() {

        initOwner(Global.primaryStage());
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.DECORATED);
        getIcons().add(Global.applicationIcon());

        final Text title = new Text(TITLE);
        final Font regular = title.getFont();
        title.setFont(Font.font(regular.getFamily(), FontWeight.BOLD, 2 * regular.getSize()));

        final Text version = new Text(
                String.format(Global.getString("dlgVersion"), VERSION, DATE));
        version.setTextAlignment(TextAlignment.CENTER);

        final HyperlinkHandler handler = new HyperlinkHandler();
        final Hyperlink author = new Hyperlink(
                String.format(Global.getString("dlgAuthor"), AUTHOR));

        if (DesktopAction.canMail()) {
            author.setTooltip(new Tooltip(Global.getString("dlgAuthorTip")));
            author.setUnderline(true);
            author.setUserData(MAIL_URI);
            author.setOnAction(handler);
        } else
            author.setTooltip(new Tooltip(Global.getString("ctlUnsupportedTip")));

        final Hyperlink website = new Hyperlink(SITE);
        if (DesktopAction.canBrowse()) {
            website.setTooltip(new Tooltip(Global.getString("dlgWebsiteTip")));
            website.setUnderline(true);
            website.setUserData(SITE_URI);
            website.setOnAction(handler);
        } else
            website.setTooltip(new Tooltip(Global.getString("ctlUnsupportedTip")));

        final Hyperlink readme = new Hyperlink(Global.getString("dlgShowDocs"));
        if (DesktopAction.canBrowse()) {
            readme.setTooltip(new Tooltip(Global.getString("dlgShowDocsTip")));
            readme.setUnderline(true);
            readme.setUserData("ReadMe.html");
            readme.setOnAction(handler);
        } else
            readme.setTooltip(new Tooltip(Global.getString("ctlUnsupportedTip")));

        final Button okButton = new Button(Global.getString("ctlOK"));
        okButton.setCancelButton(true);
        okButton.setDefaultButton(true);
        okButton.setPrefWidth(Global.BUTTON_WIDTH);
        okButton.setOnAction(e -> close());

        final VBox column = new VBox();
        column.setAlignment(Pos.CENTER);
        column.setPadding(new Insets(4));
        VBox.setMargin(author, new Insets(12, 0, 0, 0));
        VBox.setMargin(okButton, new Insets(12, 0, 0, 0));
        column.getChildren().addAll(title, version, author, website, readme, okButton);

        setResizable(false);
        setTitle(Global.getString("dlgAboutTitle"));

        final Scene scene = new Scene(column);
        scene.getStylesheets().add(Global.customStyles());
        setScene(scene);
        sizeToScene();

        // must set after scene built
        okButton.requestFocus();
    }

    /**
     * Handles clicks on any {@link Hyperlink} control.
     * Shows a modal dialog if an error occurred.
     */
    private class HyperlinkHandler implements EventHandler<ActionEvent> {

        /**
         * Handles the specified {@link ActionEvent}.
         * @param t the {@link ActionEvent} to handle
         */
        @Override public void handle(ActionEvent t) {
            try {
                final Hyperlink source = (Hyperlink) t.getSource();
                final String target = (String) source.getUserData();

                if (target.startsWith("mailto:")) {
                    final URI uri = new URI(target);
                    DesktopAction.mail(uri);
                } else {
                    final URI uri = (target.contains("://") ?
                            new URI(target) : Paths.get(target).toUri());
                    DesktopAction.browse(uri);
                }
            }
            catch (IOException | URISyntaxException e) {
                Global.showAlert(AboutDialog.this, Global.getString("dlgAboutError"), e);
            }
        }
    }
}
