package org.kynosarges.mimebrowser;

/**
 * Specifies how a search term is applied.
 * @author Christoph Nahr
 * @version 1.2.0
 */
public enum SearchMode {

    /**
     * Specifies literal case-insensitive matching.
     */
    SIMPLE(Global.getString("searchSimple")),

    /**
     * Specifies literal case-sensitive matching.
     */
    CASE(Global.getString("searchCase")),

    /**
     * Specifies regular expression matching.
     */
    REGEX(Global.getString("searchRegex"));

    final private String _label;

    /**
     * Creates a new {@link SearchMode} with the specified label.
     * @param label the display label for the {@link SearchMode}
     */
    SearchMode(String label) {
        _label = label;
    }

    /**
     * Returns a string representation of the {@link SearchMode}.
     * @return the display label for the {@link SearchMode}
     */
    @Override
    public String toString() {
        return _label;
    }
}
