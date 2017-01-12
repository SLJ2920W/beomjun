package application.cs.mail.handler.mime;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javafx.beans.property.ReadOnlyStringWrapper;

public class MetaData {

	private final ReadOnlyStringWrapper metaTabTitle = new ReadOnlyStringWrapper(this, "metaTabTitle");
	private final ReadOnlyStringWrapper metaSubject = new ReadOnlyStringWrapper(this, "metaSubject");
	private final ReadOnlyStringWrapper metaReplyTo = new ReadOnlyStringWrapper(this, "metaReplyTo");
	private final ReadOnlyStringWrapper metaSentDate = new ReadOnlyStringWrapper(this, "metaSentDate");
	private final ReadOnlyStringWrapper metaFrom = new ReadOnlyStringWrapper(this, "metaFrom");
	private final ReadOnlyStringWrapper metaTo = new ReadOnlyStringWrapper(this, "metaTo");
	private final ReadOnlyStringWrapper metaCC = new ReadOnlyStringWrapper(this, "metaCC");
	private final ReadOnlyStringWrapper metaBCC = new ReadOnlyStringWrapper(this, "metaBCC");
	public static final MetaData INSTANCE = new MetaData();

	public MetaData() {

	}

	public MetaData(Path path) {
		setMetaData(path);
	}

	private void setMetaData(Path path) {
		try {
			byte[] buffer = Files.readAllBytes(path);

			Session session = Session.getInstance(System.getProperties(), null);
			try (InputStream stream = new ByteArrayInputStream(buffer)) {
				MimeMessage msg = new MimeMessage(session, stream);

				String sentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(msg.getSentDate());

				// 탭에 표시 하는 탭 제목용
				setMetaTabTitle(formatAddresses(msg.getFrom(), true));
				// 전송 날짜
				setMetaSentDate(sentDate);
				// 보낸 사람
				setMetaFrom(formatAddresses(msg.getFrom()));
				// 제목
				setMetaSubject(msg.getSubject());
				// 회신 받는 메일 주소
				setMetaReplyTo(formatAddresses(msg.getReplyTo()));
				// 받는 사람
				if(msg.getRecipients(Message.RecipientType.TO) != null)
					setMetaTo(formatAddresses(msg.getRecipients(Message.RecipientType.TO)));
				// 참조
				if (msg.getRecipients(Message.RecipientType.CC) != null)
					setMetaCC(formatAddresses(msg.getRecipients(Message.RecipientType.CC)));
				// 숨은 참조
				if (msg.getRecipients(Message.RecipientType.BCC) != null) {
					setMetaBCC(formatAddresses(msg.getRecipients(Message.RecipientType.BCC)));
				}
			} catch (MessagingException e) {
				e.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String formatAddresses(Address[] addresses) {
		return formatAddresses(addresses, false);
	}

	private static String formatAddresses(Address[] addresses, boolean flag) {
		StringBuilder out = new StringBuilder();

		for (int i = 0; i < addresses.length; i++) {
			if (i > 0)
				out.append(", ");
			InternetAddress address = (InternetAddress) addresses[i];

			if (address.getPersonal() == null)
				out.append(address.getAddress());
			else {
				if (flag) {
					// 괄호 안에 글자가 있다면 괄호 포함 하여 삭제 (아이디 삭제 하기 위해)
					out.append(address.getPersonal().replaceAll("\\(\\w.*\\)", ""));
					out.append(System.getProperty("line.separator"));
					out.append("[");
					out.append(address.getAddress().split("@")[0]);
					out.append("]");
				} else {					
					out.append(address.getPersonal());
					out.append("[");
					out.append(address.getAddress());
					out.append("]");
				}
			}
		}

		return out.toString();
	}

	public final javafx.beans.property.ReadOnlyStringProperty metaSubjectProperty() {
		return this.metaSubject.getReadOnlyProperty();
	}

	public final java.lang.String getMetaSubject() {
		return this.metaSubjectProperty().get();
	}

	public final javafx.beans.property.ReadOnlyStringProperty metaReplyToProperty() {
		return this.metaReplyTo.getReadOnlyProperty();
	}

	public final java.lang.String getMetaReplyTo() {
		return this.metaReplyToProperty().get();
	}

	public final javafx.beans.property.ReadOnlyStringProperty metaSentDateProperty() {
		return this.metaSentDate.getReadOnlyProperty();
	}

	public final java.lang.String getMetaSentDate() {
		return this.metaSentDateProperty().get();
	}

	public final javafx.beans.property.ReadOnlyStringProperty metaFromProperty() {
		return this.metaFrom.getReadOnlyProperty();
	}

	public final javafx.beans.property.ReadOnlyStringProperty metaToProperty() {
		return this.metaTo.getReadOnlyProperty();
	}

	public final java.lang.String getMetaTo() {
		return this.metaToProperty().get();
	}

	public final javafx.beans.property.ReadOnlyStringProperty metaCCProperty() {
		return this.metaCC.getReadOnlyProperty();
	}

	public final java.lang.String getMetaCC() {
		return this.metaCCProperty().get();
	}

	public final javafx.beans.property.ReadOnlyStringProperty metaBCCProperty() {
		return this.metaBCC.getReadOnlyProperty();
	}

	public final java.lang.String getMetaBCC() {
		return this.metaBCCProperty().get();
	}

	public final java.lang.String getMetaFrom() {
		return this.metaFromProperty().get();
	}

	public final javafx.beans.property.ReadOnlyStringProperty metaTabTitleProperty() {
		return this.metaTabTitle.getReadOnlyProperty();
	}

	public final java.lang.String getMetaTabTitle() {
		return this.metaTabTitleProperty().get();
	}

	public void setMetaSubject(String metaSubject) {
		this.metaSubject.set(metaSubject);
	}

	public void setMetaReplyTo(String metaReplyTo) {
		this.metaReplyTo.set(metaReplyTo);
	}

	public void setMetaSentDate(String metaSentDate) {
		this.metaSentDate.set(metaSentDate);
	}

	public void setMetaFrom(String metaFrom) {
		this.metaFrom.set(metaFrom);
	}

	public void setMetaTo(String metaTo) {
		this.metaTo.set(metaTo);
	}

	public void setMetaCC(String metaCC) {
		this.metaCC.set(metaCC);
	}

	public void setMetaBCC(String metaBCC) {
		this.metaBCC.set(metaBCC);
	}

	public void setMetaTabTitle(String metaTabTitle) {
		this.metaTabTitle.set(metaTabTitle);
	}

}
