package org.kynosarges.mimebrowser;

import java.io.*;
import java.util.*;
import javafx.beans.property.*;
import javafx.collections.*;

import javax.mail.*;
import javax.mail.internet.*;

/**
 * Represents one complete MIME message, or any arbitrary file.
 * For MIME messages, {@link Document} contains any (meta-) data extracted by the JavaMail API.
 * For any other file, {@link Document} contains a single {@link DocumentPart} with its contents.
 *
 * @author Christoph Nahr
 * @version 1.2.0
 */
public class Document extends DocumentReader {

    private final ReadOnlyBooleanWrapper _hasEnvelope = new ReadOnlyBooleanWrapper(this, "hasEnvelope");
    private final ReadOnlyStringWrapper _mimeDate = new ReadOnlyStringWrapper(this, "mimeDate");
    private final ReadOnlyStringWrapper _mimeFlags = new ReadOnlyStringWrapper(this, "mimeFlags");
    private final ReadOnlyStringWrapper _mimeFrom = new ReadOnlyStringWrapper(this, "mimeFrom");
    private final ReadOnlyStringWrapper _mimeReply = new ReadOnlyStringWrapper(this, "mimeReply");
    private final ReadOnlyStringWrapper _mimeSubject = new ReadOnlyStringWrapper(this, "mimeSubject");
    private final ReadOnlyStringWrapper _mimeTo = new ReadOnlyStringWrapper(this, "mimeTo");
    private final ReadOnlyStringWrapper _mimeToBcc = new ReadOnlyStringWrapper(this, "mimeToBcc");
    private final ReadOnlyStringWrapper _mimeToCc = new ReadOnlyStringWrapper(this, "mimeToCc");
    private final ReadOnlyStringWrapper _mimeXmailer = new ReadOnlyStringWrapper(this, "mimeXmailer");

    private DocumentPart _firstHtml, _firstPlain;
    private final ObservableList<DocumentPart> _parts = FXCollections.observableArrayList();
    private final ObservableList<DocumentPart> _unmodifiableParts = FXCollections.unmodifiableObservableList(_parts);

    /**
     * Creates a {@link Document}. Private due to singleton class.
     */
    private Document() { }

    /**
     * The single instance of the {@link Document} class.
     */
    public static final Document INSTANCE = new Document();

    /**
     * Gets the first HTML {@link DocumentPart} to display.
     * @return the first {@link DocumentPart} that is not an attachment and
     *         whose "Content-Type" is "text/html", if found, else {@code null}
     */
    public DocumentPart getFirstHtml() {
        return _firstHtml;
    }

    /**
     * Gets the first plain text {@link DocumentPart} to display.
     * @return the first {@link DocumentPart} that is not an attachment and
     *         whose "Content-Type" is "text/plain", if found, else {@code null}
     */
    public DocumentPart getFirstPlain() {
        return _firstPlain;
    }

    /**
     * Gets a list of all parts in the MIME message.
     * The list is empty for a {@link Document} whose type is {@link DocumentType#UNKNOWN},
     * unless its contents were read as plain text.
     * @return a read-only list of all parts in the MIME message
     */
    public ObservableList<DocumentPart> getParts() {
        return _unmodifiableParts;
    }

    /**
     * Indicates whether the {@link Document} has a MIME envelope.
     * @return the value of {@link #hasEnvelopeProperty}
     */
    public final boolean hasEnvelope() { return _hasEnvelope.get(); }
    /**
     * Defines whether the {@link Document} has a MIME envelope.
     * @return the {@link ReadOnlyBooleanProperty} holding the value
     */
    public ReadOnlyBooleanProperty hasEnvelopeProperty() {
        return _hasEnvelope.getReadOnlyProperty();
    }

    /**
     * Gets the sent date of the MIME envelope.
     * @return the value of {@link #mimeDateProperty}
     */
    public final String getMimeDate() { return _mimeDate.get(); }
    /**
     * Defines the sent date of the MIME envelope.
     * @return the {@link ReadOnlyStringProperty} holding the value
     */
    public ReadOnlyStringProperty mimeDateProperty() {
        return _mimeDate.getReadOnlyProperty();
    }

    /**
     * Gets the list of flags of the MIME envelope.
     * @return the value of {@link #mimeFlagsProperty}
     */
    public final String getMimeFlags() { return _mimeFlags.get(); }
    /**
     * Defines the list of flags of the MIME envelope.
     * @return the {@link ReadOnlyStringProperty} holding the value
     */
    public ReadOnlyStringProperty mimeFlagsProperty() {
        return _mimeFlags.getReadOnlyProperty();
    }

    /**
     * Gets the list of sender addresses of the MIME envelope.
     * @return the value of {@link #mimeFromProperty}
     */
    public final String getMimeFrom() { return _mimeFrom.get(); }
    /**
     * Defines the list of sender addresses of the MIME envelope.
     * @return the {@link ReadOnlyStringProperty} holding the value
     */
    public ReadOnlyStringProperty mimeFromProperty() {
        return _mimeFrom.getReadOnlyProperty();
    }

    /**
     * Gets the list of reply-to addresses of the MIME envelope.
     * @return the value of {@link #mimeReplyProperty}
     */
    public final String getMimeReply() { return _mimeReply.get(); }
    /**
     * Defines the list of reply-to addresses of the MIME envelope.
     * @return the {@link ReadOnlyStringProperty} holding the value
     */
    public ReadOnlyStringProperty mimeReplyProperty() {
        return _mimeReply.getReadOnlyProperty();
    }

    /**
     * Gets the subject of the MIME envelope.
     * @return the value of {@link #mimeSubjectProperty}
     */
    public final String getMimeSubject() { return _mimeSubject.get(); }
    /**
     * Defines the subject of the MIME envelope.
     * @return the {@link ReadOnlyStringProperty} holding the value
     */
    public ReadOnlyStringProperty mimeSubjectProperty() {
        return _mimeSubject.getReadOnlyProperty();
    }

    /**
     * Gets the list of To: recipient addresses of the MIME envelope.
     * @return the value of {@link #mimeToProperty}
     */
    public final String getMimeTo() { return _mimeTo.get(); }
    /**
     * Defines the list of To: recipient addresses of the MIME envelope.
     * @return the {@link ReadOnlyStringProperty} holding the value
     */
    public ReadOnlyStringProperty mimeToProperty() {
        return _mimeTo.getReadOnlyProperty();
    }

    /**
     * Gets the list of BCC: recipient addresses of the MIME envelope.
     * @return the value of {@link #mimeToCcProperty}
     */
    public final String getMimeToBcc() { return _mimeToBcc.get(); }
    /**
     * Defines the list of BCC: recipient addresses of the MIME envelope.
     * @return the {@link ReadOnlyStringProperty} holding the value
     */
    public ReadOnlyStringProperty mimeToBccProperty() {
        return _mimeToBcc.getReadOnlyProperty();
    }

    /**
     * Gets the list of CC: recipient addresses of the MIME envelope.
     * @return the value of {@link #mimeToCcProperty}
     */
    public final String getMimeToCc() { return _mimeToCc.get(); }
    /**
     * Defines the list of CC: recipient addresses of the MIME envelope.
     * @return the {@link ReadOnlyStringProperty} holding the value
     */
    public ReadOnlyStringProperty mimeToCcProperty() {
        return _mimeToCc.getReadOnlyProperty();
    }

    /**
     * Gets the X-Mailer header of the MIME envelope.
     * @return the value of {@link #mimeXmailerProperty}
     */
    public final String getMimeXmailer() { return _mimeXmailer.get(); }
    /**
     * Defines the X-Mailer header of the MIME envelope.
     * @return the {@link ReadOnlyStringProperty} holding the value
     */
    public ReadOnlyStringProperty mimeXmailerProperty() {
        return _mimeXmailer.getReadOnlyProperty();
    }

    /**
     * Clears the properties of the {@link Document}.
     */
    @Override
    public void clear() {
        super.clear();
        _firstHtml = _firstPlain = null;
        _hasEnvelope.set(false);
        _parts.clear();

        _mimeDate.set(null);
        _mimeFlags.set(null);
        _mimeFrom.set(null);
        _mimeReply.set(null);
        _mimeSubject.set(null);
        _mimeTo.set(null);
        _mimeToBcc.set(null);
        _mimeToCc.set(null);
        _mimeXmailer.set(null);
    }

    /**
     * Reads the headers of the specified {@link MimeMessage}.
     * @param message the {@link MimeMessage} whose headers to read
     * @return {@code true} to stop reading, {@code false} to continue
     * @throws MessagingException if {@code message} contains invalid data
     * @throws NullPointerException if {@code message} is {@code null}
     */
    @Override
    protected boolean readHeaders(MimeMessage message) throws MessagingException {
        boolean hasEnvelope = false;

        Date date = message.getSentDate();
        if (date != null) {
            _mimeDate.set(date.toString());
            hasEnvelope = true;
        }

        Flags.Flag[] systemFlags = message.getFlags().getSystemFlags();
        String[] userFlags = message.getFlags().getUserFlags();
        String[] flags = new String[systemFlags.length + userFlags.length];

        if (flags.length > 0) {
            for (int i = 0; i < systemFlags.length; i++)  {
                Flags.Flag flag = systemFlags[i];
                if (flag == Flags.Flag.ANSWERED)
                    flags[i] = Global.getString("flagAnswered");
                else if (flag == Flags.Flag.DELETED)
                    flags[i] = Global.getString("flagDeleted");
                else if (flag == Flags.Flag.DRAFT)
                    flags[i] = Global.getString("flagDraft");
                else if (flag == Flags.Flag.FLAGGED)
                    flags[i] = Global.getString("flagFlagged");
                else if (flag == Flags.Flag.RECENT)
                    flags[i] = Global.getString("flagRecent");
                else if (flag == Flags.Flag.SEEN)
                    flags[i] = Global.getString("flagSeen");
            }
            System.arraycopy(userFlags, 0, flags, systemFlags.length, userFlags.length);
            _mimeFlags.set(formatStrings(flags));
            hasEnvelope = true;
        }

        Address[] addresses;
        if ((addresses = message.getFrom()) != null) {
            _mimeFrom.set(formatAddresses(addresses));
            hasEnvelope = true;
        }

        // expose reply-to only if different from sender
        if ((addresses = message.getReplyTo()) != null) {
            String reply = formatAddresses(addresses);
            if (!reply.equals(_mimeFrom.get())) {
                _mimeReply.set(formatAddresses(addresses));
                hasEnvelope = true;
            }
        }

        if (message.getSubject() != null) {
            _mimeSubject.set(message.getSubject());
            hasEnvelope = true;
        }

        /*
         * Any single recipient address may itself be a RFC 822 group address.
         * This would have be checked via isGroup and fetched via getGroup(false).
         * We currently don't bother to do this as group addresses are rarely used.
         */

        if ((addresses = message.getRecipients(Message.RecipientType.TO)) != null) {
            _mimeTo.set(formatAddresses(addresses));
            hasEnvelope = true;
        }

        if ((addresses = message.getRecipients(Message.RecipientType.BCC)) != null) {
            _mimeToBcc.set(formatAddresses(addresses));
            hasEnvelope = true;
        }

        if ((addresses = message.getRecipients(Message.RecipientType.CC)) != null) {
            _mimeToCc.set(formatAddresses(addresses));
            hasEnvelope = true;
        }

        String[] headers = message.getHeader("X-Mailer");
        if (headers != null) {
            _mimeXmailer.set(formatStrings(headers));
            hasEnvelope = true;
        }

        _hasEnvelope.set(hasEnvelope);
        return false; // always keep reading
    }

    /**
     * Reads the specified MIME {@link Part}.
     * @param part the {@link Part} whose contents to read
     * @return {@code true} to stop reading, {@code false} to continue
     * @throws IOException if {@code part} contains invalid data
     * @throws MessagingException if {@code part} contains invalid data
     * @throws NullPointerException if {@code part} is {@code null}
     */
    @Override
    protected boolean readPart(Part part) throws IOException, MessagingException {
        DocumentPart docPart = new DocumentPart(part);
        _parts.add(docPart);

        if (!docPart.isAttachment()) {
            if (_firstHtml == null && part.isMimeType("text/html"))
                _firstHtml = docPart;
            if (_firstPlain == null && part.isMimeType("text/plain"))
                _firstPlain = docPart;
        }

        return false; // always keep reading
    }

    /**
     * Formats the specified {@link Address} array as a single string.
     * @param addresses the {@link Address} array to format
     * @return a {@link String} containing all {@code addresses}
     */
    private static String formatAddresses(Address[] addresses) {
        StringBuilder out = new StringBuilder();

        for (int i = 0; i < addresses.length; i++) {
            if (i > 0) out.append(", ");
            InternetAddress address = (InternetAddress) addresses[i];

            // manual extraction, toString() returns only getPersonal()
            if (address.getPersonal() == null)
                out.append(address.getAddress());
            else {
                out.append(address.getPersonal());
                out.append(" [");
                out.append(address.getAddress());
                out.append("]");
            }
        }

        return out.toString();
    }

    /**
     * Formats the specified {@link String} array as a single string.
     * @param strings the {@link String} array to format
     * @return a {@link String} containing all {@code strings}
     */
    private static String formatStrings(String[] strings) {
        StringBuilder out = new StringBuilder();

        for (int i = 0; i < strings.length; i++) {
            if (i > 0) out.append(", ");
            out.append(strings[i]);
        }

        return out.toString();
    }
}
