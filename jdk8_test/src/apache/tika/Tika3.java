package apache.tika;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.ws.handler.Handler;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

public class Tika3 {

	public static void main(final String[] args) throws IOException, TikaException, SAXException {

		// Assume sample.txt is in your current directory
		File file = new File("D:\\Hanwha\\메일백업\\test\\2016-06-26 120400_이상헌(Sangheon Lee)_그룹웨어 패치 관련 요청 드립니다.eml");

		// parse method parameters
		Parser parser = new AutoDetectParser();
		BodyContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		FileInputStream inputstream = new FileInputStream(file);
		ParseContext context = new ParseContext();

		// parsing the file
		parser.parse(inputstream, handler, metadata, context);
		System.out.println("File content : " + handler.toString());
	}
}