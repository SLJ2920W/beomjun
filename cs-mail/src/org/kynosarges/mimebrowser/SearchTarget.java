package org.kynosarges.mimebrowser;

/**
 * Specifies what a search term must match.
 * @author Christoph Nahr
 * @version 1.2.0
 */
public enum SearchTarget {

    /**
     * Specifies matching only document name.
     */
    NAME(Global.getString("searchName")),

    /**
     * Specifies matching only document text.
     */
    TEXT(Global.getString("searchText")),

    /**
     * Specifies matching either name or text.
     * Either or both may match to produce a hit.
     */
    EITHER(Global.getString("searchEither")),

    /**
     * Specifies matching both name and text.
     * Both must match to produce a hit.
     */
    BOTH(Global.getString("searchBoth"));

    final private String _label;

    /**
     * Creates a new {@link SearchTarget} with the specified label.
     * @param label the display label for the {@link SearchTarget}
     */
    SearchTarget(String label) {
        _label = label;
    }

    /**
     * Returns a string representation of the {@link SearchTarget}.
     * @return the display label for the {@link SearchTarget}
     */
    @Override
    public String toString() {
        return _label;
    }
}
