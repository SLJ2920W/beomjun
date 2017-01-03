package application.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.io.IOUtils;
import org.apache.james.mime4j.codec.Base64InputStream;
import org.apache.james.mime4j.codec.QuotedPrintableInputStream;
import org.apache.james.mime4j.parser.ContentHandler;
import org.apache.james.mime4j.stream.BodyDescriptor;
import org.apache.james.mime4j.stream.Field;
import org.apache.james.mime4j.util.ContentUtil;



/**
 * http://en.wikipedia.org/wiki/MIME
 *  
 * @author parksungsoo
 *
 */
public class MimeHandler implements ContentHandler {
	
	private boolean DEBUG = false;

	private StringBuilder debugBuffer = new StringBuilder();
	
	private StringBuilder htmlBuffer = new StringBuilder();
	private Map images;
	private File imageDirectory;
	private CID cidConvertor;
	
	public CID getCidConvertor() {
		return cidConvertor;
	}

	public void setCidConvertor(CID cidConvertor) {
		this.cidConvertor = cidConvertor;
	}

	public MimeHandler(File imageDirectory) {
		this.imageDirectory = imageDirectory;
	}

	private String escape(char c) {
		if (c == '&') {
			return "&amp;";
		}
		if (c == '>') {
			return "&gt;";
		}
		if (c == '<') {
			return "&lt;";
		}
		return "" + c;
	}

	private String escape(String s) {
		s = s.replaceAll("&", "&amp;");
		s = s.replaceAll(">", "&gt;");
		s = s.replaceAll("<", "&lt;");
		return s;
	}

	public void epilogue(InputStream is) throws IOException {
		if (DEBUG) {
			debugBuffer.append("<epilogue>\r\n");
			int b = 0;
			while ((b = is.read()) != -1) {
				debugBuffer.append(escape((char) b));
			}
			debugBuffer.append("</epilogue>\r\n");
		}
	}

	public void preamble(InputStream is) throws IOException {
		if (DEBUG) {
			debugBuffer.append("<preamble>\r\n");
			int b = 0;
			while ((b = is.read()) != -1) {
				debugBuffer.append(escape((char) b));
			}
			debugBuffer.append("</preamble>\r\n");
		}
	}

	public void startMessage() {
		startNode("message");
		
		if (DEBUG) {
			debugBuffer.append("<message>\r\n");
		}
	}

	public void endMessage() {
		endNode();
		
		if (DEBUG) {
			debugBuffer.append("</message>\r\n");
		}
	}

	public void startMultipart(BodyDescriptor bd) {
		startNode("multipart");
		
		if (DEBUG) {
			debugBuffer.append("<multipart>\r\n");
		}
	}

	public void endMultipart() {
		endNode();
		
		if (DEBUG) {
			debugBuffer.append("</multipart>\r\n");
		}
	}

	public void startBodyPart() {
		startNode("body-part");
		
		if (DEBUG) {
			debugBuffer.append("<body-part>\r\n");
		}
	}

	public void endBodyPart() {
		endNode();
		
		if (DEBUG) {
			debugBuffer.append("</body-part>\r\n");
		}
	}

	public void startHeader() {
		if (DEBUG) {
			debugBuffer.append("<header>\r\n");
		}
	}

	public void endHeader() {
		if (DEBUG) {
			debugBuffer.append("</header>\r\n");
		}
	}

	public void body(BodyDescriptor bd, InputStream is) throws IOException {
		if (DEBUG) {
			debugBuffer.append("<body>\r\n");
		}

		MimeHelperNode parentNode = getCurrent();
		
		// http://en.wikipedia.org/wiki/MIME 참고할 것
		
		String contentTransferEncoding = parentNode.getContentTransferEncoding();
		
		// 과거 MIME의 경우 이미지에 contentTransferEncoding이 없는 경우가 있어 SKIP 함. (단순 링크일 경우 마임 정보가 잘못 들어가 잇음)
		if ( contentTransferEncoding == null ) return;
		
		// 7bit, base64, quoted-printable, 8bit, binary 값 중 ActiveSquare로 작성된 내용은  7bit, base64, quoted-printable 데이터만 처리.
		
		InputStream bodyStream = getSuitableInputStream(is, contentTransferEncoding);
		
		String contentType = parentNode.getContentType();
		if ( contentType.startsWith("text/html") || contentType.startsWith("text/plain") ) {
			String charsetName = MimeDecodeUtil.getValue(contentType, "charset");
			if ( charsetName != null ) charsetName = charsetName.toUpperCase().trim();
			try {
				if ( charsetName == null || !Charset.isSupported(charsetName) ) {
					charsetName = "EUC-KR";
				}
			} catch (IllegalCharsetNameException e) {
				charsetName = "EUC-KR";
			} catch (IllegalArgumentException e) {
				charsetName = "EUC-KR";
			}
			if ( contentType.startsWith("text/html") ) {
				htmlBuffer.append( MimeDecodeUtil.getString(bodyStream, charsetName) );
			} else if ( contentType.startsWith("text/plain") ) {
				htmlBuffer.append( MimeDecodeUtil.textToHtml( MimeDecodeUtil.getString(bodyStream, charsetName) ) );
			}
		} else if ( contentType.startsWith("image") ) {
			String contentId = parentNode.getContentId();
			if ( contentId != null ) {
				contentId = contentId.trim();
				contentId = contentId.replace('<', '\0');
				contentId = contentId.replace('>', '\0');
				contentId = contentId.trim();
				
				if ( images == null ) images = new LinkedHashMap();
				
				String imgfileName = MimeDecodeUtil.getValue(contentType, "name");
				String fileExt = MimeDecodeUtil.getExtension(imgfileName);
				
				boolean isBMP = false;
				
				if ( fileExt.length() > 3 ) {
					if ( contentType.indexOf("gif") > 0 ) {
						fileExt = "gif";
					} else if ( contentType.indexOf("jpeg") > 0 ) {
						fileExt = "jpg";
					} else if ( contentType.indexOf("png") > 0 ) {
						fileExt = "png";
					} else if ( contentType.indexOf("bmp") > 0 ) {
						fileExt = "bmp";
					} 
				}
				
				String newImg = imageDirectory.getName()+"/"+imageDirectory.getName()+"_"+images.size() + "." + fileExt;
				File imgFile = new File(imageDirectory.getParentFile(), newImg);
				if ( !imageDirectory.exists() ) {
					imageDirectory.mkdirs();
				}
				
				// ImageUtils.saveBMPToJPEG(bodyStream, imgFile); 함수가 이미지를 읽어 들일 때 메모리에 모두 올리는지 아닌지 여부가 불투명하여 BMP 이미지도 그냥 jpg로 변환하지 않고 그대로 저장 
				OutputStream output = null;
				try {
					output = new BufferedOutputStream(new FileOutputStream(imgFile));
					IOUtils.copy(bodyStream, output);
				} finally {
					 try { if ( output != null ) output.flush(); } catch (Exception e) {}
					 try { if ( output != null ) output.close(); } catch (Exception e) {}
				}				
				
				images.put(contentId, newImg);
				
			} else {
				while (bodyStream.read() != -1) {
					;
				}
			}
			
		} else if ( contentType.startsWith("application/octet-stream") ) {
			String fileName = MimeDecodeUtil.getValue(contentType, "name");
			String fileExt = MimeDecodeUtil.getExtension(fileName);
			// 2014.07.16 skkim : emf 파일 파싱을 위해 추가
			if( fileExt.trim().equals("emf") || fileExt.trim().equals("bmp") )  {
				String contentId = parentNode.getContentId();
				if ( contentId != null ) {
					contentId = contentId.trim();
					contentId = contentId.replace('<', '\0');
					contentId = contentId.replace('>', '\0');
					contentId = contentId.trim();

					if ( images == null ) images = new LinkedHashMap();

					String newImg = imageDirectory.getName()+"/"+imageDirectory.getName()+"_"+images.size() + "." + fileExt.trim();
					File imgFile = new File(imageDirectory.getParentFile(), newImg);
					if ( !imageDirectory.exists() )
						imageDirectory.mkdirs();

					OutputStream output = null;
					try {
						output = new BufferedOutputStream(new FileOutputStream(imgFile));
						IOUtils.copy(bodyStream, output);
					} finally {
						 try { if ( output != null ) output.flush(); } catch (Exception e) {}
						 try { if ( output != null ) output.close(); } catch (Exception e) {}
					}
					images.put(contentId, newImg);

				} else {
					while (bodyStream.read() != -1) {;}
				}
				
			}else {
				while (bodyStream.read() != -1) {;}
			}
			
		} else {
			while (bodyStream.read() != -1) {
				;
			}
		}
		
		if (DEBUG) {
			int b = 0;
			while ((b = is.read()) != -1) {
				debugBuffer.append(escape((char) b));
			}
		} else {
			
		}

		if (DEBUG) {
			debugBuffer.append("</body>\r\n");
		}
	}

	/**
	 * @param is
	 * @param contentTransferEncoding
	 * @return
	 */
	private InputStream getSuitableInputStream(InputStream is, String contentTransferEncoding) {
		if ( contentTransferEncoding == null ) {
			throw new RuntimeException("Content transfer encoding is null");
//			return new Base64InputStream( ( is instanceof BufferedInputStream ) ? is : new BufferedInputStream(is) );
		} else if ( "base64".equals(contentTransferEncoding) ) {
			return new Base64InputStream( ( is instanceof BufferedInputStream ) ? is : new BufferedInputStream(is) );
		} else if ( "quoted-printable".equals(contentTransferEncoding) ) {
			return new QuotedPrintableInputStream( ( is instanceof BufferedInputStream ) ? is : new BufferedInputStream(is) );
		} else if ( "7bit".equals(contentTransferEncoding) || "8bit".equals(contentTransferEncoding) ) {
			return ( is instanceof BufferedInputStream ) ? is : new BufferedInputStream(is);
		} else {
			throw new RuntimeException("Unknown content transfer encoding: " + contentTransferEncoding);
		}
	}

	public void field(Field field) {
		MimeHelperNode parentNode = getCurrent();
		parentNode.putAttribute(field.getName(), field.getBody());

		if (DEBUG) {
			debugBuffer.append("<field>\r\n" + escape(ContentUtil.decode(field.getRaw())) + "</field>\r\n");
		}
	}

	public void raw(InputStream is) throws IOException {
		throw new RuntimeException("raw should never be called");
	}

	public String toString() {
		try {
			return (cidConvertor == null) ? htmlBuffer.toString() : cidConvertor.convertCID(htmlBuffer.toString(), images);
		} catch (Exception e) {
			return htmlBuffer.toString();
		}
	}

	
	
	
	
	
	private Stack<MimeHelperNode> nodeStack = new Stack<MimeHelperNode>();
	private Stack<MimeHelperNode> freedStack = new Stack<MimeHelperNode>();

	private MimeHelperNode startNode(String tagName) {
		MimeHelperNode parentNode = getCurrent();
		MimeHelperNode curNode = getNewNode(parentNode, tagName);
		if (parentNode != null) {
			parentNode.appendChild(curNode);
		}
		push(curNode);
		return curNode;
	}

	private void endNode() {
		MimeHelperNode curNode = pop();
		free(curNode);
	}

	private MimeHelperNode getNewNode(MimeHelperNode parentNode,
			String localName) {
		MimeHelperNode node = null;

		if (freedStack.empty()) {
			node = new MimeHelperNode();
		} else {
			node = freedStack.pop();
		}

		node.setParent(parentNode);
		node.setTagName(localName);

		return node;
	}

	private boolean empty() {
		return nodeStack.empty();
	}

	private MimeHelperNode getCurrent() {
		if (empty()) {
			return null;
		}
		return nodeStack.peek();
	}

	private MimeHelperNode pop() {
		if (empty()) {
			return null;
		}
		return nodeStack.pop();
	}

	private void push(MimeHelperNode node) {
		nodeStack.push(node);
	}

	private void free(MimeHelperNode node) {
		for (int i = node.getChildLength() - 1; i >= 0; i--) {
			free(node.getChildAt(i));
		}

		MimeHelperNode parent = node.getParent();

		if (parent != null) {
			parent.removeChild(node);
		}

		node.clear();

		freedStack.push(node);
	}

	public Map getImages() {
		return images;
	}
	
	
}
