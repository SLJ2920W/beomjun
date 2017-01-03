/**
 * Copyright 2009 by HANWHA S&C Corp.,
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HANWHA S&C Corp. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with HANWHA S&C Corp.
 */
package application.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * IO 관련 유틸
 * 
 * @author : 박성수
 */
public class IOUtils {
	
	private static final int BUFFER_SIZE = 1024*4;
	
	/**
	 * 주어진 스트림으로부터 문자열을 얻어냄 (문자열을 UTF-8 형태로 읽어 들임)
	 * @param inputStream 입력 스트림
	 * @return 스트림으로부터 읽어들인 문자열
	 * @throws IOException
	 */
	public static String getString(InputStream inputStream)
	throws IOException {
		return getString(inputStream, "UTF-8");
	}
	
	/**
	 * 주어진 스트림으로부터 문자열을 얻어냄
	 * @param inputStream 입력 스트림
	 * @param encoding 읽어들일 문자열 인코딩
	 * @return 스트림으로부터 읽어들인 문자열
	 * @throws IOException
	 */
	public static String getString(InputStream inputStream, String encoding)
		throws IOException {
		if (inputStream == null) {
			return null;
		}
	
		BufferedReader in = new BufferedReader(new InputStreamReader(
					inputStream, encoding), BUFFER_SIZE);
			int charsRead;
			char[] copyBuffer = new char[BUFFER_SIZE];
			StringBuffer sb = new StringBuffer();
		
			while ((charsRead = in.read(copyBuffer, 0, BUFFER_SIZE)) != -1) {
				sb.append(copyBuffer, 0, charsRead);
			}
		
			in.close();
		
			return sb.toString();
		}
	
	
	/**
	 * InputStream을 OutputStream으로 전달한다.
	 * @param is
	 * @param os
	 * @throws Exception
	 */
	public static void download( InputStream is, OutputStream os, int bufferSize ) throws Exception {
		byte[]		bufferByte	= new byte[bufferSize];

		BufferedInputStream		bis	= null;
		BufferedOutputStream	bos	= null;

		try {
			bis = new BufferedInputStream( is );
			bos = new BufferedOutputStream(os);
			int read = 0;
			while ((read = bis.read(bufferByte)) != -1) {
				bos.write(bufferByte, 0, read);
			}
		}
		catch ( IOException ioe) {
		} finally {
			if ( bis != null ) {
				try {
					bis.close();
				}
				catch ( Exception e ) {
				}
			}

			if ( bos != null ) {
				try {
					bos.flush();
				}
				catch ( Exception e ) {
				}
				try {
					bos.close();
				}
				catch ( Exception e ) {
				}
			}
		}
	}
	
	/**
	 * Text 형태의 InputStream을 형식에 맞게 수정한 후 OutputStream으로 전달한다.
	 * @param is
	 * @param os
	 * @throws Exception
	 */
	public static void downloadForText( InputStream is, OutputStream os ) throws Exception {
		BufferedInputStream		bis	= null;
		BufferedOutputStream	bos	= null;

		try {
			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(os);
			StringBuffer isToStrBuff = new StringBuffer();
			int c;
		    while ((c = bis.read()) != -1) {
		    	isToStrBuff.append((char)c );
	        }
		    
		    String strBuffToStr = new String(isToStrBuff.toString().getBytes("ISO-8859-1"), "UTF-8");
		    // &lt; &gt; &#xD; &nbsp; &amp; &quot; &apos; 치환
		    strBuffToStr = strBuffToStr.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&#xD;", "").replaceAll("&nbsp;", " ").replaceAll("&amp;", "&").replaceAll("&quot;", "\"").replaceAll("&apos;", "'");
		    int lastIndex1 = strBuffToStr.lastIndexOf("</html>");
		    int lastIndex2 = strBuffToStr.lastIndexOf("</HTML>");
		    int lastIndex3 = strBuffToStr.lastIndexOf("</t:Body>");
		    if ( lastIndex1 > -1 )
		    	strBuffToStr = strBuffToStr.substring(0, lastIndex1+7);
		    else if ( lastIndex2 > -1 )
		    	strBuffToStr = strBuffToStr.substring(0, lastIndex2+7);
		    else if ( lastIndex3 > -1 )
		    	strBuffToStr = strBuffToStr.substring(0, lastIndex3);
		    
		    byte[] byteArray = strBuffToStr.getBytes();
        	bos.write(byteArray);
        	
		} catch ( IOException ioe) {
		} finally {
			if ( bis != null ) {
				try {
					bis.close();
				}
				catch ( Exception e ) {
				}
			}

			if ( bos != null ) {
				try {
					bos.flush();
				}
				catch ( Exception e ) {
				}
				try {
					bos.close();
				}
				catch ( Exception e ) {
				}
			}
		}
	}
	
	/**
	 * InputStream을 OutputStream으로 전달한다.
	 * @param is
	 * @param os
	 * @throws Exception
	 */
	public static void send( InputStream is, OutputStream os, int bufferSize ) throws Exception {
		byte[]		bufferByte	= new byte[bufferSize];

		BufferedInputStream		bis	= null;
		BufferedOutputStream	bos	= null;

		try {
			bis = new BufferedInputStream( is );
			bos = new BufferedOutputStream(os);
			int read = 0;
			while ((read = bis.read(bufferByte)) != -1) {
				bos.write(bufferByte, 0, read);
			}
		}
		catch ( IOException ioe) {
		} finally {
			if ( bis != null ) {
				try {
					bis.close();
				}
				catch ( Exception e ) {
				}
			}

			if ( bos != null ) {
				try {
					bos.flush();
				}
				catch ( Exception e ) {
				}
				try {
					bos.close();
				}
				catch ( Exception e ) {
				}
			}
		}
	}
}
