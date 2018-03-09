package apache.tika;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

public class Tika_dig {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		File f = new File("D:\\Hanwha\\메일백업\\2017-01-22 190132_박범준_0 테스트.eml");
		MessageDigest digest = MessageDigest.getInstance("MD5");
		byte[] bytes = new byte[8192];
		computeHashSum(f, digest, bytes);

	}

	public static String computeHashSum(File file, MessageDigest digest, byte[] bytes) {
		String hashSum = "";
		int bytesRead;
		try (FileInputStream stream = new FileInputStream(file)) {
			digest.reset();
			while ((bytesRead = stream.read(bytes)) != -1) {
				digest.update(bytes, 0, bytesRead);
			}
			hashSum = DatatypeConverter.printHexBinary(digest.digest());
		} catch (IOException ex) {
		}
		return hashSum;
	}

}
