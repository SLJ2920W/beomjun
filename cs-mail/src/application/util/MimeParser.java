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

import java.io.File;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author : parksungsoo
 */
public interface MimeParser {

	/**
	 * 마임 파싱
	 * @param isTextToHtml 텍스트 형태의 마임 HTML 변환 여부 
	 * @param cidConvertor CID 컨버터
	 */
	public abstract void parse(boolean isTextToHtml, CID cidConvertor);

	/**
	 * 마임 파싱
	 * @param isTextToHtml 텍스트 형태의 마임 HTML 변환 여부 
	 * @param imageDirectory 이미지 디렉토리
	 * @param cidConvertor CID 컨버터
	 */
	public abstract void parse(boolean isTextToHtml, File imageDirectory,
			CID cidConvertor);

	/**
	 * 마임 파싱
	 * @param isTextToHtml 텍스트 형태의 마임 HTML 변환 여부 
	 * @param imageDirectory 이미지 디렉토리
	 * @param cidConvertor CID 컨버터
	 * @param convertBMP2JPEG BMP 이미지의 JPEG 변환 여부
	 */
	public abstract void parse(boolean isTextToHtml, File imageDirectory,
			CID cidConvertor, boolean convertBMP2JPEG);

	/**
	 * 디코딩된 마임파일 저장
	 * @param partOutputter MimeOutputter
	 * @throws Exception
	 */
	public abstract void write(MimeOutputter partOutputter) throws Exception;


	/**
	 * CID 이미지 맵 참조
	 * @return
	 */
	public abstract Map getImages();

}