package org.kynosarges.mimebrowser;

import java.io.*;
import javafx.beans.property.*;
import javax.mail.*;

/**
 * Represents one content part of a {@link Document}.
 * {@link DocumentPart} holds the content and most metadata of a single MIME {@link Part},
 * or the raw file content of any parent {@link Document} that is not a MIME message.
 *
 * @author Christoph Nahr
 * @version 1.2.0
 */
public class DocumentPart {

    private final Object _content;
    private final Part _mimePart;

    private final ReadOnlyStringWrapper _contentType = new ReadOnlyStringWrapper(this, "contentType");
    private final ReadOnlyObjectWrapper<DocumentType> _documentType = new ReadOnlyObjectWrapper<>(this, "documentType");
    private final ReadOnlyStringWrapper _description = new ReadOnlyStringWrapper(this, "description");
    private final ReadOnlyStringWrapper _fileName = new ReadOnlyStringWrapper(this, "fileName");

    private final ReadOnlyBooleanWrapper _isAttachment = new ReadOnlyBooleanWrapper(this, "isAttachment");
    private final ReadOnlyBooleanWrapper _isInline = new ReadOnlyBooleanWrapper(this, "isInline");
    private final ReadOnlyIntegerWrapper _size = new ReadOnlyIntegerWrapper(this, "size");

    /**
     * Creates a new {@link DocumentPart} from the specified MIME {@link Part}.
     * The conditions under which exceptions are thrown are unclear, as the JavaMail library
     * does not document them. Sorry!
     *
     * @param part the MIME {@link Part} providing data
     * @throws IOException if {@code part} contains invalid data
     * @throws MessagingException if {@code part} contains invalid data
     * @throws NullPointerException if {@code part} is {@code null}
     */
    public DocumentPart(Part part) throws IOException, MessagingException {

        _mimePart = part;
        Object content = part.getContent();
        _content = (content instanceof String ?
                DocumentReader.correctUnicode((String) content) : content);

        _description.set(part.getDescription());
        _fileName.set(part.getFileName());
        _size.set(part.getSize());

        String contentType = part.getContentType();
        _contentType.set(contentType);
        _documentType.set(content instanceof String ?
                DocumentType.fromContentType(contentType) : DocumentType.UNKNOWN);

        String disposition = part.getDisposition();
        _isAttachment.set(Part.ATTACHMENT.equalsIgnoreCase(disposition));
        _isInline.set(Part.INLINE.equalsIgnoreCase(disposition));
    }

    /**
     * Gets the content of the MIME part.
     * Returns the content in its preferred Java type, as defined by {@link
     * javax.activation.DataHandler#getContent}. This is usually a {@link String} translated
     * from the encoding specified by the header field "Content-Transfer-Encoding".
     * Returns an {@link java.io.InputStream} if the content does not match any Java type.
     *
     * @return the content of the MIME part
     */
    public final Object getContent() {
        return _content;
    }

    /**
     * Gets the original MIME part.
     * @return the {@link Part} wrapped by the {@link DocumentPart}
     */
    public final Part getMimePart() {
        return _mimePart;
    }

    /**
     * Gets the content type of the MIME part.
     * @return the value of {@link #contentTypeProperty}
     */
    public final String getContentType() { return _contentType.get(); }
    /**
     * Defines the content type of the MIME part.
     * Contains the value of the "Content-Type" header field. Should always be valid.
     *
     * @return the {@link ReadOnlyStringProperty} holding the value
     */
    public ReadOnlyStringProperty contentTypeProperty() {
        return _contentType.getReadOnlyProperty();
    }

    /**
     * Gets the {@link DocumentType} of the MIME part.
     * @return the value of {@link #documentTypeProperty}
     */
    public final DocumentType getDocumentType() { return _documentType.get(); }
    /**
     * Defines the {@link DocumentType} of the MIME part.
     * Contains an indication how the content should be displayed, based on the primary type and
     * subtype of the "Content-Type" header field. Always contains {@link DocumentType#UNKNOWN}
     * if the content itself is not an instance of {@link String}.
     *
     * @return the {@link ReadOnlyObjectProperty} holding the value
     */
    public ReadOnlyObjectProperty<DocumentType> documentTypeProperty() {
        return _documentType.getReadOnlyProperty();
    }

    /**
     * Gets the description of the MIME part.
     * @return the value of {@link #descriptionProperty}
     */
    public String getDescription() {
        return _description.get();
    }
    /**
     * Defines the description of the MIME part.
     * Contains the value of the "Content-Description" header field. I have yet to see a MIME
     * message that actually uses this field, so the application currently ignores it completely.
     *
     * @return the {@link ReadOnlyStringProperty} holding the value
     */
    public ReadOnlyStringProperty descriptionProperty() {
        return _description.getReadOnlyProperty();
    }

    /**
     * Gets the file name of the MIME part.
     * @return the value of {@link #fileNameProperty}
     */
    public String getFileName() {
        return _fileName.get();
    }
    /**
     * Defines the file name of the MIME part.
     * Contains the value of the "filename" parameter in the "Content-Disposition" header field.
     * Usually valid for attachments, meaning {@link #isAttachment} returns {@code true}.
     *
     * @return the {@link ReadOnlyStringProperty} holding the value
     */
    public ReadOnlyStringProperty fileNameProperty() {
        return _fileName.getReadOnlyProperty();
    }

    /**
     * Indicates whether the MIME part represents an attachment.
     * @return the value of {@link #isAttachmentProperty}
     */
    public boolean isAttachment() {
        return _isAttachment.get();
    }
    /**
     * Defines whether the MIME part represents an attachment.
     * Contains {@code true} if the "Content-Disposition" header field is present and specifies
     * "ATTACHMENT" (case-insensitive).
     *
     * @return the {@link ReadOnlyBooleanProperty} holding the value
     */
    public ReadOnlyBooleanProperty isAttachmentProperty() {
        return _isAttachment.getReadOnlyProperty();
    }

    /**
     * Indicates whether the MIME part should be presented inline.
     * @return the value of {@link #isInlineProperty}
     */
    public boolean isInline() {
        return _isInline.get();
    }
    /**
     * Defines whether the MIME part should be presented inline.
     * Contains {@code true} if the "Content-Disposition" header field is present and specifies
     * "INLINE" (case-insensitive).
     *
     * @return the {@link ReadOnlyBooleanProperty} holding the value
     */
    public ReadOnlyBooleanProperty isInlineProperty() {
        return _isInline.getReadOnlyProperty();
    }

    /**
     * Gets the size in bytes of the MIME part.
     * @return the value of {@link #sizeProperty}
     */
    public int getSize() {
        return _size.get();
    }
    /**
     * Defines the size in bytes of the MIME part.
     * Contains -1 if no size information is present.
     *
     * @return the {@link ReadOnlyStringProperty} holding the value
     */
    public ReadOnlyIntegerProperty sizeProperty() {
        return _size.getReadOnlyProperty();
    }

    /**
     * Returns a string representation of the {@link DocumentPart}.
     * @return the file name and content type, if present, followed by the size in bytes
     */
    @Override
    public String toString() {

        String name = (Global.hasContent(_fileName.get()) ? _fileName.get():
                _isAttachment.get() ? Global.getString("lblUnnamed") :
                _isInline.get() ? Global.getString("lblInline") : null);

        // Content-Type may include spaces & line breaks
        String type = (Global.hasContent(_contentType.get()) ?
                _contentType.get().replaceAll("\\s+", " ") :
                Global.getString("lblUnknown"));

        return (name == null ? type : String.format("%s – %s", name, type));
    }
}
