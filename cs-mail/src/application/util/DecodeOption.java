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

import java.awt.Dimension;
import java.util.List;

/**
 * MIME 디코딩 옵션
 * 
 * @author : 박성수
 */
public interface DecodeOption {

	/** 썸네일 이미지 생성 옵션 (생성하지 않음) */
	public static final int ThumbnailOption_NONE = 0;
	/** 썸네일 이미지 생성 옵션 (첫번째 이미지만 변환) */
	public static final int ThumbnailOption_FIRST = 1;
	/** 썸네일 이미지 생성 옵션 (전체 이미지 변환) */
	public static final int ThumbnailOption_ALL = 2;
	
	/**
	 * CID 컨버터 참조
	 * @return CID 컨버터
	 */
	public CID getCidConvertor();
	
	/**
	 * CID 컨버터 세팅
	 * @param cidConvertor CID 컨버터
	 */
	public void setCidConvertor(CID cidConvertor);
	
	/**
	 * 썸네일 옵션 참조
	 * @return 썸네일 옵션
	 */
	public int getThumbnailOption();
	
	/**
	 * 썸네일 옵션 세팅
	 * @param thumbnailOption
	 */
	public void setThumbnailOption(int thumbnailOption);
	
	/**
	 * Footer 참조
	 * @return Footer 문자열
	 */
	public String getFooter();
	
	/**
	 * Footer 세팅
	 * @param footer Footer 문자열
	 */
	public void setFooter(String footer);
	
	/**
	 * Header 참조
	 * @return Header 문자열
	 */
	public String getHeader();

	/**
	 * Header 세팅
	 * @param header Header 문자열
	 */
	public void setHeader(String header);
	
	/**
	 * 메타데이타 생성 여부
	 * @return 메타데이타 생성 여부
	 */
	public boolean createMetadata();
	
	/**
	 * 썸네일 이미지 크기 세팅
	 * @param width 폭
	 * @param height 높이
	 */
	public void setThumbnailSize(int width, int height);
	
	/**
	 * 썸네일 이미지 폭 참조
	 * @return 썸네일 이미지 폭
	 */
	public int getThumbnailWidth();
	
	/**
	 * 썸네일 이미지 높이 참조
	 * @return 썸네일 이미지 높이
	 */
	public int getThumbnailHeight();
	
	/**
	 * 썸네일 이미지 추가 (마임 디코딩시 서로 다른 크기의 여러개 썸네일 생성 가능)
	 * @param width 폭
	 * @param height 높이
	 */
	public void addThumbnailSize(int width, int height);
	
	/**
	 * 생성할 썸네일 이미지 크기정보  
	 * @return 썸네일 이미지 크기정보 
	 */
	public List<Dimension> getThumbnailDimensionList();
	
	/**
	 * HTML 폼 태그 삭제 여부
	 * @param isTrim true : 삭제, false : 삭제 안함
	 */
	public void setTrimFormTag(boolean isTrim);
	
	/**
	 * HTML 폼 태그 삭제 여부
	 * @return true : 삭제, false : 삭제 안함
	 */
	public boolean isTrimFormTag();
	
	/**
	 * 워드 태그 삭제 여부
	 * @param isTrim true : 삭제, false : 삭제 안함
	 */
	public void setTrimWordTag(boolean isTrim);
	
	/**
	 * 워드 태그 삭제 여부
	 * @return true : 삭제, false : 삭제 안함
	 */
	public boolean isTrimWordTag();
	
	/**
	 * HTML 스크립트 태그 삭제 여부
	 * @param isTrim true : 삭제, false : 삭제 안함
	 */
	public void setTrimScriptTag(boolean isTrim);
	
	/**
	 * HTML 스크립트 태그 삭제 여부
	 * @return true : 삭제, false : 삭제 안함
	 */
	public boolean isTrimScriptTag();
	
	/**
	 * 문서 내 포함된 BMP 이미지의 JPEG 변환 여부
	 * @return true : 변환, false : 변환 안함
	 */
	public boolean isCovertBMP2JPEG();
	
	/**
	 * 문서 내 포함된 BMP 이미지의 JPEG 변환 여부
	 * @param covertBMP2JPEG true : 변환, false : 변환 안함
	 */
	public void setCovertBMP2JPEG(boolean covertBMP2JPEG);
	
	/**
	 * 문서 내 명시된 ContentType을 강제로 변경할 ContentType
	 * @return
	 */
	public String getForceReplaceContentType();
	
	public boolean isHtmlFixForm();
	
	public void setHtmlFixForm(boolean htmlFixForm);

}