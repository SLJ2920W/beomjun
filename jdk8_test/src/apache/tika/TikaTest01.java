package apache.tika;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;

import javax.xml.bind.DatatypeConverter;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mail.RFC822Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * https://tika.apache.org/1.9/formats.html#Mail_formats
 * @author park
 */
public class TikaTest01 {

	public static void main(String[] args) {
		try {

			Path home = Paths.get("D:\\Hanwha\\메일백업\\");
			Files.walkFileTree(home, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					if (file.toString().endsWith(".eml")) {
						RFC822Parser mp = new RFC822Parser();

						AutoDetectParser parser = new AutoDetectParser();
						ContentHandler content = new BodyContentHandler(-1);
						Metadata metadata = new Metadata();
						// System.out.println(file.toString());
						metadata.set(Metadata.RESOURCE_NAME_KEY, home.relativize(file).toString());
						ParseContext context = new ParseContext();
						try (FileInputStream stream = new FileInputStream(file.toString())) {

							mp.parse(stream, content, metadata, context);

							String[] metadataNames = metadata.names();

							for (String name : metadataNames) {
								System.out.println(name + ":   " + metadata.get(name));
							}

							// System.out.println(metadata.get(Property.internalText(Message.MESSAGE_TO)));
							System.out.println("\n");
						} catch (SAXException | TikaException e) {
							e.printStackTrace();
						}
					}
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (Exception e) {

		}
	}

	public static String computeHashSum(Path file, MessageDigest digest, byte[] bytes) {
		String hashSum = "";
		int bytesRead;
		try (FileInputStream stream = new FileInputStream(file.toFile())) {
			digest.reset();
			while ((bytesRead = stream.read(bytes)) != -1) {
				digest.update(bytes, 0, bytesRead);
			}
			hashSum = DatatypeConverter.printHexBinary(digest.digest());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return hashSum;
	}

}
