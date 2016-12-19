package org.kynosarges.mimebrowser;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.*;
import java.util.regex.Pattern;

import javax.mail.*;
import javax.mail.internet.*;

/**
 * Walks the files and folders comprised by the current {@link Selection}.
 * I/O exceptions are suppressed and merely result in skipped entries, if possible.
 * @author Christoph Nahr
 * @version 1.2.0
 */
public class SelectionWalker extends DocumentReader {

    // files & folders found during current walk
    private final List<DirectoryEntry> _files = new ArrayList<>();
    private final List<DirectoryEntry> _folders = new ArrayList<>();

    // FileVisitor and parameters for current walk
    private final SelectionVisitor _visitor = new SelectionVisitor();
    private Path _dir;
    private boolean _useFilter;
    private Pattern _pattern;
    private SearchTarget _target;

    /**
     * Creates a {@link SelectionWalker}. Private due to singleton class.
     */
    private SelectionWalker() { }

    /**
     * The single instance of the {@link Selection} class.
     */
    public static final SelectionWalker INSTANCE = new SelectionWalker();

    /**
     * Gets all files encountered during the last {@link #walk}.
     * @return a read-only list of all encountered files
     */
    public List<DirectoryEntry> getFiles() {
        return Collections.unmodifiableList(_files);
    }

    /**
     * Walks the files and folders comprised by the current {@link Selection}.
     * The results are available through {@link #getFiles} and {@link #getFolders}.
     */
    public void walk() {
        _files.clear();
        _folders.clear();

        Selection selection = Selection.INSTANCE;
        _dir = selection.getDirectory();
        try {
            if (_dir == null || !Files.exists(_dir) || !Files.isDirectory(_dir))
                return;
        }
        catch (SecurityException e) {
            // should not happen unless concurrently modified
            System.err.println(e.toString());
            return;
        }

        _useFilter = selection.getFilter();
        _pattern = selection.getFilterPattern();
        if (_pattern == null) _pattern = Selection.EMPTY_PATTERN;
        _target = selection.getFilterTarget();
        if (_target == null) _target = SearchTarget.NAME;

        final Set<FileVisitOption> options = Collections.emptySet();
        int depth = (selection.getCombine() ? selection.getCombineLevels() : 1);

        try {
            Files.walkFileTree(_dir, options, depth, _visitor);
        }
        catch (IOException | SecurityException e) {
            // should not happen as we discard all exceptions
            System.err.println(e.toString());
        }
    }

    /**
     * Gets all folders encountered during the last {@link #walk}.
     * @return a read-only list of all encountered folders
     */
    public List<DirectoryEntry> getFolders() {
        return Collections.unmodifiableList(_folders);
    }

    /**
     * Determines whether the specified {@link DirectoryEntry} passes the current filter.
     * @param entry the {@link DirectoryEntry} to examine
     * @return {@code true} if {@code entry} passes the current filter, else {@code false}
     */
    private boolean checkFilter(DirectoryEntry entry) {
        final Path path = entry.getPath();
        final String text = entry.getPathLink().getText();

        try {
            switch (_target) {
                case NAME: return _pattern.matcher(text).find();
                case TEXT: return read(path, false);
                case EITHER: return _pattern.matcher(text).find() || read(path, false);
                case BOTH: return _pattern.matcher(text).find() && read(path, false);
            }
        }
        catch (IOException | MessagingException e) {
            // silently convert errors into "not found"
        }

        return false;
    }

    /**
     * Checks all RFC 822 headers of the specified {@link MimeMessage}.
     * @param message the {@link MimeMessage} whose headers to check
     * @return {@code true} if the RFC 822 header values of {@code message}
     *         contain the search pattern, else {@code false}
     * @throws MessagingException if {@code message} contains invalid data
     * @throws NullPointerException if {@code message} is {@code null}
     */
    @Override
    @SuppressWarnings("rawtypes")
    protected boolean readHeaders(MimeMessage message) throws MessagingException {

        for (Enumeration e = message.getAllHeaders(); e.hasMoreElements();) {
            Header header = (Header) e.nextElement();

            // skip headers that may contain binary data
            String name = header.getName();
            if (name.startsWith("X-") || name.contains("Signature"))
                continue;

            if (_pattern.matcher(header.getValue()).find())
                return true;
        }

        return false;
    }

    /**
     * Checks the text content of the specified MIME {@link Part}.
     * @param part the MIME {@link Part} whose text content to check
     * @return {@code true} if the text content of {@code part} contains the search pattern;
     *         {@code false} if the content is not text or the pattern was not found
     * @throws IOException if {@code part} contains invalid data
     * @throws MessagingException if {@code part} contains invalid data
     * @throws NullPointerException if {@code part} is {@code null}
     */
    @Override
    protected boolean readPart(Part part) throws IOException, MessagingException {

        String desc = part.getDescription();
        if (desc != null && _pattern.matcher(desc).find())
            return true;

        String name = part.getFileName();
        if (name != null && _pattern.matcher(name).find())
            return true;

        if (!(part.getContent() instanceof String))
            return false;

        String text = (String) part.getContent();
        text = DocumentReader.correctUnicode(text);
        return _pattern.matcher(text).find();
    }

    /**
     * Implements the {@link FileVisitor} for the {@link SelectionWalker}.
     */
    private class SelectionVisitor implements FileVisitor<Path> {

        /**
         * Determines whether to visit the specified directory.
         * @param dir the directory to visit
         * @param attrs the basic file attributes of {@code dir}
         * @return a {@link FileVisitResult} indicating how to process {@code dir}
         * @throws IOException never in this implementation
         */
        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            try {
                if (!attrs.isOther() && !Files.isHidden(dir))
                    return FileVisitResult.CONTINUE;
            }
            catch (IOException | SecurityException e) { }

            return FileVisitResult.SKIP_SUBTREE;
        }

        /**
         * Invoked after the specified directory has been visited.
         * @param dir the directory that has been visited
         * @param e the I/O exception that prematurely ended the visit of {@code dir},
         *          or {@code null} if the visit completed without error
         * @return always {@link FileVisitResult#CONTINUE}
         * @throws IOException never in this implementation
         */
        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        /**
         * Visits the specified file or directory.
         * @param file the file or directory to visit
         * @param attrs the basic file attributes of {@code file}
         * @return a {@link FileVisitResult} indicating how to continue
         * @throws IOException never in this implementation
         */
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            try {
                if (!attrs.isOther() && !Files.isHidden(file)) {
                    DirectoryEntry entry = new DirectoryEntry(_dir, file, attrs);
                    if (attrs.isDirectory())
                        _folders.add(entry);
                    else if (!_useFilter || checkFilter(entry))
                        _files.add(entry);
                }
            }
            catch (IOException | SecurityException e) { }

            return FileVisitResult.CONTINUE;
        }

        /**
         * Invoked when a file or directory could not be visited.
         * @param file the file or directory that could not be visited
         * @param e the I/O exception that prevented visiting {@code file}
         * @return always {@link FileVisitResult#CONTINUE}
         * @throws IOException never in this implementation
         */
        @Override
        public FileVisitResult visitFileFailed(Path file, IOException e) throws IOException {
            return FileVisitResult.CONTINUE;
        }
    }
}
