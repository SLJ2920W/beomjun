package org.kynosarges.mimebrowser;

import javafx.scene.control.*;
import javafx.scene.text.Font;

/**
 * Specifies all controls with icon labels.
 * Combines an icon taken from Font Awesome, a plain text substitute,
 * and an optional {@link Tooltip} for controls derived from {@link Labeled}.
 *
 * @author Christoph Nahr
 * @version 1.3.3
 * @see <a href="http://fortawesome.github.io/Font-Awesome/">Font Awesome</a>
 */
public enum IconControl {
    /**
     * Specifies the About {@link Button} (fa-info).
     */
    ABOUT("\uf129", "ctlAbout", "ctlAboutTip"),
    /**
     * Specifies the Browse {@link Button} (fa-folder-open).
     */
    BROWSE("\uf07c", "ctlBrowse", "ctlBrowseTip"),
    /**
     * Specifies a folder {@link Label} (fa-folder-open).
     */
    FOLDER("\uf07c", "lblFolder", null),
    /**
     * Specifies the Refresh {@link Button} (fa-refresh).
     */
    REFRESH("\uf021", "ctlRefresh", "ctlRefreshTip"),
    /**
     * Specifies the Save {@link Button} (fa-floppy-o).
     */
    SAVE("\uf0c7", "ctlSave", "ctlSaveTip"),
    /**
     * Specifies the Save All {@link Button} (fa-files-o).
     */
    SAVE_ALL("\uf0c5", "ctlSaveAll", "ctlSaveAllTip"),
    /**
     * Specifies the Show Text {@link Button} (fa-file-text).
     */
    SHOW_TEXT("\uf15c", "ctlShowText", "ctlShowTextTip"),
    /**
     * Specifies a subdirectory {@link Label} (fa-caret-right).
     */
    SUBDIR("\uf0da", "lblFolder", null),
    /**
     * Specifies the Zoom In {@link Button} (fa-search-plus).
     */
    ZOOM_IN("\uf00e", "ctlZoomIn", "ctlZoomInTip"),
    /**
     * Specifies the Zoom Out {@link Button} (fa-search-minus).
     */
    ZOOM_OUT("\uf010", "ctlZoomOut", "ctlZoomOutTip");

    private static Font _font;
    private final String _icon, _text, _tooltip;

    /**
     * Creates an {@link IconControl} with the specified labels.
     * {@code text} and {@code tooltip} must be specified as resource identifiers.
     *
     * @param icon the icon glyph to show if {@link Symbol#font} is valid
     * @param text the plain text to show otherwise
     * @param tooltip an optional {@link Tooltip} to show
     */
    IconControl(String icon, String text, String tooltip) {
        _icon = icon;
        _text = Global.getString(text);
        _tooltip = (tooltip == null ? null : Global.getString(tooltip));
    }

    /**
     * Creates a {@link Labeled} control from the {@link IconControl}.
     * @param <T> the subtype of {@link Labeled} to instantiate
     * @param labeled the {@link Class} to instantiate
     * @return the new instance of {@code labeled}, or {@code null} if instantiation failed
     */
    public <T extends Labeled> T create(Class<T> labeled) {
        T control;
        try {
            control = labeled.newInstance();
        }
        catch (IllegalAccessException | InstantiationException e) {
            return null;
        }

        control.setMnemonicParsing(false);
        if (control instanceof Button)
            control.setMinWidth(36);

        if (getFont() == null)
            control.setText(_text);
        else {
            control.setText(_icon);
            control.setFont(getFont());
        }

        if (_tooltip != null)
            control.setTooltip(new Tooltip(_tooltip));

        return control;
    }

    /**
     * Gets the {@link Font} for all icon glyphs.
     * @return the {@link Font} for all icon glyphs
     * @see <a href="http://fortawesome.github.io/Font-Awesome/">Font Awesome</a>
     */
    private static Font getFont() {
        if (_font == null) {
            final java.net.URL url = IconControl.class.getResource("/resources/FontAwesome.ttf");
            _font = Font.loadFont(url.toExternalForm(), 12);
        }
        return _font;
    }
}
