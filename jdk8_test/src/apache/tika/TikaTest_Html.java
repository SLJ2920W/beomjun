package apache.tika;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Message;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.Property;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class TikaTest_Html {

	public static void main(String[] args) throws IOException, SAXException, TikaException {
		// detecting the file type
//		BodyContentHandler handler = new BodyContentHandler();
//		Metadata metadata = new Metadata();
//		FileInputStream inputstream = new FileInputStream(new File("D:\\Hanwha\\메일백업\\2017-01-22 190132_박범준_0 테스트.eml"));
//		ParseContext pcontext = new ParseContext();
//
//		// Html parser
//		HtmlParser htmlparser = new HtmlParser();
//		htmlparser.parse(inputstream, handler, metadata, pcontext);
//		System.out.println("Contents of the document:" + handler.toString());
//		System.out.println("Metadata of the document:");
//		String[] metadataNames = metadata.names();
//
//		for (String name : metadataNames) {
//			System.out.println(name + ":   " + metadata.get(name));
//		}
		
		 //Assume sample.txt is in your current directory
		File file = new File("D:\\Hanwha\\메일백업\\test\\2017-02-01 094050_이현수(Hyunsoo Lee)_안녕하세요 이현수입니다.eml");

		// parse method parameters
		ContentHandler handler = new BodyContentHandler(-1);
		Metadata metadata = new Metadata();
		FileInputStream inputstream = new FileInputStream(file);
		ParseContext context = new ParseContext();

		metadata.set(Metadata.RESOURCE_NAME_KEY, file.toString());
		// parsing the file
		Parser parser = new AutoDetectParser();
		parser.parse(inputstream, handler, metadata, context);
		String[] metadataNames = metadata.names();
		for (String name : metadataNames) {
			System.out.println("key = "+name + ", value = "+metadata.get(name));
		}

	}

}
