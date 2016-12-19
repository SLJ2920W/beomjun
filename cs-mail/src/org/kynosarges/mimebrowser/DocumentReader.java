package org.kynosarges.mimebrowser;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;

/**
 * Reads one complete MIME message, or any arbitrary file.
 * {@link DocumentReader} recurses through the parts of a multi-part MIME message. Two protected
 * methods are called to handle the header fields of a MIME message and any found content parts.
 * The return values of these methods indicate whether reading should stop prematurely.
 *
 * @author Christoph Nahr
 * @version 1.2.0
 */
public abstract class DocumentReader {

    private Path _path;
    private DocumentType _type = DocumentType.UNKNOWN;

    /**
     * Gets the {@link Path} of the latest document.
     * @return the {@link Path} last supplied to {@link #read}, if any, else {@code null}
     */
    public Path getPath() {
        return _path;
    }

    /**
     * Gets the {@link DocumentType} of the latest document.
     * The default value {@link DocumentType#UNKNOWN} indicates that no file contents were loaded.
     * {@link DocumentType#MIME_MESSAGE} indicates that the contents are split across envelope and
     * one or more parts. Any other value indicates that the entire file contents were loaded into
     * a single {@link DocumentPart} of the same MIME type.
     *
     * @return the {@link DocumentType} of the {@link Path} last supplied to {@link #read}
     */
    public DocumentType getType() {
        return _type;
    }

    /**
     * Clears the properties of the {@link DocumentReader}.
     */
    protected void clear() {
        _path = null;
        _type = DocumentType.UNKNOWN;
    }

    /**
     * Corrects the specified {@link String} if an ignored Unicode BOM is found.
     * @param s the {@link String} to examine
     * @return a correctly re-encoded version of {@code s} if necessary, else {@code s} itself
     */
    public static String correctUnicode(String s) {
        if (s == null || s.length() < 3)
            return s;

        // Java cannot handle UTF-32 encodings so we omit those BOMs
        Charset actual;
        if (s.charAt(0) == 0xFE && s.charAt(1) == 0xFF)
            actual = StandardCharsets.UTF_16BE;
        else if (s.charAt(0) == 0xFF && s.charAt(1) == 0xFE)
            actual = StandardCharsets.UTF_16LE;
        else if (s.charAt(0) == 0xEF && s.charAt(1) == 0xBB && s.charAt(2) == 0xBF)
            actual = StandardCharsets.UTF_8;
        else
            return s;

        /*
         * JavaMail failed to recognize the Unicode BOM and instead read the text as ASCII.
         * We need to manually convert each character to a byte, then re-encode the resulting
         * byte array back into a string using the BOM-specified encoding. If any character
         * has its high byte set we assume an unknown encoding and return the original string.
         */
        byte[] buffer = new byte[s.length()];

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c > 255) return s; // oops!
            buffer[i] = (byte) c;
        }

        return new String(buffer, actual);
    }

    /**
     * Reads the document at the specified {@link Path}.
     * @param path the {@link Path} to the document to read
     * @param unknownAsText {@code true} to read unrecognized files as plain text
     * @return {@code true} if {@link #readHeaders} or {@link #readPart} signaled to stop reading;
     *         {@code false} if nothing was read or if both methods always returned {@code false}
     * @throws IOException if {@code path} could not be read
     * @throws MessagingException if {@code path} is not a valid MIME {@link Message}
     */
    public boolean read(Path path, boolean unknownAsText) throws IOException, MessagingException {
        /*
         * Check for known file extensions. JavaMail reads some non-MIME files correctly,
         * but sometimes produces empty MIME body parts. We take no chances and manually
         * construct a single MIME part from the file contents. The envelope remains empty,
         * and the only header field we set is Content-Type based on the file extension.
         */
        _path = path;
        _type = DocumentType.fromPath(path);
        if (!unknownAsText && _type == DocumentType.UNKNOWN)
            return false;

        byte[] buffer = Files.readAllBytes(path);

        // entire file is of known MIME type: read directly
        if (_type.isKnownMime() || (unknownAsText && _type == DocumentType.UNKNOWN)) {
            InternetHeaders headers = new InternetHeaders();
            headers.addHeader("Content-Type", _type.contentType());
            Part part = new MimeBodyPart(headers, buffer);
            return readPart(part);
        }

        Properties props = System.getProperties();
        Session session = Session.getInstance(props, null);
        Queue<Part> queue = new LinkedList<>();

        try (InputStream stream = new ByteArrayInputStream(buffer)) {
            MimeMessage message = new MimeMessage(session, stream);
            if (readHeaders(message))
                return true;

            queue.add(message);
            while (!queue.isEmpty()) {
                Part part = queue.remove();

                // recurse into wrappers but don't store them
                if (part.isMimeType("multipart/*")) {
                    Multipart mp = (Multipart) part.getContent();
                    for (int i = 0; i < mp.getCount(); i++)
                        queue.add(mp.getBodyPart(i));
                } else if (part.isMimeType("message/rfc822"))
                    queue.add((Part) part.getContent());
                else if (readPart(part))
                    return true;
            }
        }

        return false;
    }

    /**
     * Reads the headers of the specified {@link MimeMessage}.
     * @param message the {@link MimeMessage} whose headers to read
     * @return {@code true} to stop reading, {@code false} to continue
     * @throws MessagingException if {@code message} contains invalid data
     * @throws NullPointerException if {@code message} is {@code null}
     */
    protected abstract boolean readHeaders(MimeMessage message) throws MessagingException;

    /**
     * Reads the specified MIME {@link Part}.
     * @param part the {@link Part} whose contents to read
     * @return {@code true} to stop reading, {@code false} to continue
     * @throws IOException if {@code part} contains invalid data
     * @throws MessagingException if {@code part} contains invalid data
     * @throws NullPointerException if {@code part} is {@code null}
     */
    protected abstract boolean readPart(Part part) throws IOException, MessagingException;
}
