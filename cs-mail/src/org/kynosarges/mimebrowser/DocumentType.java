package org.kynosarges.mimebrowser;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Specifies the type of a {@link Document} or {@link DocumentPart}.
 * @author Christoph Nahr
 * @version 1.2.0
 */
public enum DocumentType {

    /**
     * Specifies any unknown type.
     * This value maps to MIME type "text/plain" to match our available views.
     */
    UNKNOWN("text/plain", ".txt"),

    /**
     * Specifies an entire MIME message.
     * This value applies to {@link Document} but not {@link DocumentPart}.
     */
    MIME_MESSAGE("multipart/mixed", ".eml"),

    /**
     * Specifies any "text/*" other than "text/html" or "text/xml".
     */
    TEXT_PLAIN("text/plain", ".txt"),

    /**
     * Specifies "text/html".
     */
    TEXT_HTML("text/html", ".html"),

    /**
     * Specifies "text/xml".
     */
    TEXT_XML("text/xml", ".xml");

    private final String _contentType;
    private final String _extension;
    private final static Map<String, DocumentType> _extensionMap = new TreeMap<>();

    /**
     * Initializes the extension map with a set of default extensions.
     * These extensions may later be overwritten by {@link #readConfig}.
     */
    static {
        _extensionMap.put(".eml", MIME_MESSAGE);
        _extensionMap.put(".htm", TEXT_HTML);
        _extensionMap.put(".html", TEXT_HTML);
        _extensionMap.put(".txt", TEXT_PLAIN);
        _extensionMap.put(".xml", TEXT_XML);
    }

    /**
     * Creates a new {@link DocumentType} with the specified MIME type and file extension.
     * @param contentType the value of the MIME header field "Content-Type"
     * @param extension the default file extension for the {@link DocumentType}
     * @throws IllegalArgumentException if {@code contentType} or {@code extension} is
     *         {@code null} or empty, or if {@code extension} does not start with a dot
     */
    DocumentType(String contentType, String extension) {

        if (contentType == null || contentType.length() == 0)
            throw new IllegalArgumentException("contentType");
        if (extension == null || !extension.startsWith("."))
            throw new IllegalArgumentException("extension");

        _contentType = contentType;
        _extension = extension;
    }

    /**
     * Gets the MIME content type for the {@link DocumentType}.
     * @return the value of the MIME header field "Content-Type"
     */
    public String contentType() {
        return _contentType;
    }

    /**
     * Gets the default file extension for the {@link DocumentType}.
     * @return the default file extension for the {@link DocumentType}, including a leading dot
     */
    public String extension() {
        return _extension;
    }

    /**
     * Determines whether the {@link DocumentType} is a known MIME type.
     * @return {@code true} if the {@link DocumentType} is neither {@link #UNKNOWN}
     *         nor {@link #MIME_MESSAGE}, else {@code false}
     */
    public boolean isKnownMime() {
        return (this != UNKNOWN && this != MIME_MESSAGE);
    }

    /**
     * Converts the specified "Content-Type" value into a {@link DocumentType} value.
     * Returns {@link #UNKNOWN} if {@code contentType} is {@code null} or empty,
     * and {@link #TEXT_PLAIN} for any "text/" other than "text/html" or "text/xml".
     *
     * @param contentType the value of the MIME header field "Content-Type"
     * @return the {@link DocumentType} that matches {@code contentType}
     */
    public static DocumentType fromContentType(String contentType) {
        if (contentType == null || contentType.isEmpty())
            return UNKNOWN;

        final String lower = contentType.toLowerCase(Locale.ENGLISH);
        if (lower.startsWith("text/html"))
            return TEXT_HTML;
        if (lower.startsWith("text/xml"))
            return TEXT_XML;
        if (lower.startsWith("text/"))
            return TEXT_PLAIN;

        return UNKNOWN;
    }

    /**
     * Converts the specified {@link Path} into a {@link DocumentType} value.
     * Returns {@link #UNKNOWN} if {@code path} is {@code null} or empty.
     *
     * @param path the {@link Path} whose extension to examine
     * @return the {@link DocumentType} that matches {@code path}
     */
    public static DocumentType fromPath(Path path) {
        if (path == null || !Files.exists(path))
            return UNKNOWN;

        final  String lower = path.toString().toLowerCase(Locale.ENGLISH);
        for (Map.Entry<String, DocumentType> entry: _extensionMap.entrySet())
            if (lower.endsWith(entry.getKey()))
                return entry.getValue();

        return UNKNOWN;
    }

    /**
     * Reads the contents of the specified configuration file.
     * The specified file should contain extension mappings for {@link #fromPath}.
     *
     * @param config the path to the configuration file to read
     */
    static void readConfig(String config) {

        try (InputStream stream = new FileInputStream(config)) {
            final Scanner scanner = new Scanner(stream, "UTF-8");

            while (scanner.hasNextLine()) {
                final String line = scanner.nextLine().trim();
                if (line.startsWith("#") || !line.contains("="))
                    continue;

                // require two non-empty strings separated by =
                final String[] lineParts = line.split("=");
                if (lineParts.length != 2 || lineParts[0].isEmpty() || lineParts[1].isEmpty())
                    continue;

                DocumentType type = DocumentType.valueOf(lineParts[0]);
                final String[] extensions = lineParts[1].split(";");

                for (String extension: extensions)
                    if (!extension.isEmpty()) {
                        final String lower = extension.toLowerCase(Locale.ENGLISH);
                        _extensionMap.put(lower, type);
                    }
            }
        }
        catch (IllegalArgumentException | IOException e) {
            System.err.println(Global.getString("dlgConfigError"));
            System.err.println(e.toString());
        }
    }

    /**
     * Trims any extension of the {@link DocumentType} from the specified file name.
     * @param fileName the file name to trim
     * @return {@code fileName} without the trailing extension if present, shorter than
     *         {@code fileName}, and matching one of the extensions configured for the
     *         {@link DocumentType}; else the unchanged {@code fileName} itself
     * @throws NullPointerException if {@code fileName} is {@code null}
     */
    public String trimExtension(String fileName) {
        final String lower = fileName.toLowerCase(Locale.ENGLISH);

        for (Map.Entry<String, DocumentType> entry: _extensionMap.entrySet()) {
            if (this != entry.getValue()) continue;

            final int end = lower.length() - entry.getKey().length();
            if (end > 0 && lower.endsWith(entry.getKey()))
                return fileName.substring(0, end);
        }

        return fileName;
    }
}
