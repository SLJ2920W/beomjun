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

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

/**
 * 마임을 출력정보를 담고 있는 클래스
 * <br>메일의 경우 : ServletOutputStream으로 직접 출력
 * <br>보통의 경우 : StringBuffer로 출력한 후 파일로 저장 
 * 
 * @author : 박성수
 */
public class MimeOutputter {
	private OutputStream response;
	private StringBuffer contents;
	private List charsetList;
	
	MimeMetadata metadata;
	
	/**
	 * 생성자
	 * @param response 출력스트림
	 */
	public MimeOutputter(OutputStream response) {
		this.response = response;
		metadata = new MimeMetadata();
	}
	
	/**
	 * 생성자 (출력시 StringBuffer 사용)
	 */
	public MimeOutputter() {
		this.response = null;
		this.contents = new StringBuffer();
		metadata = new MimeMetadata();
	}
	
	
	/**
	 * 출력
	 * @param content 출력할 문자열
	 * @param charset 문자셋
	 * @throws Exception
	 */
	public void write(String content, String charset) throws Exception {
		if ( charset != null ) {
			addCharset(charset);
		}
		if ( response != null && charset != null ) {
			response.write(content.getBytes(charset));
		} else {
			contents.append(content);
		}
	}
	
	/**
	 * MIME 문서의 charset 추가
	 * @param charset 문자셋 
	 */
	private void addCharset(String charset) {
		if ( charsetList == null ) {
			charsetList = new ArrayList();
		}
		charsetList.add(charset);
	}
	
	/**
	 * OutputStream 사용여부
	 * @return true : 사용, false : 사용안함
	 */
	public boolean isStream() {
		return response != null;
	}
	
	/**
	 * OutputStream 
	 * @return OutputStream
	 */
	public OutputStream getOutputStream() {
		return response;
	}
	
	/**
	 * HTML 문자열
	 * @return HTML 문자열
	 */
	public StringBuffer getContents() {
		return contents;
	}
	
	/**
	 * HTML 문자셋
	 * @return 문자셋 스트링
	 */
	public String getCharset() {
		return (charsetList!=null) ? (String)charsetList.get(0) : null;
	}
	
	/**
	 * 문자셋 리스트
	 * @return 문자셋 리스트
	 */
	public List getCharsetList() {
		return charsetList;
	}

	/**
	 * @return the metadata
	 */
	public MimeMetadata getMetadata() {
		return metadata;
	}

	/**
	 * @param metadata the metadata to set
	 */
	public void setMetadata(MimeMetadata metadata) {
		this.metadata = metadata;
	}

	
}