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
import java.util.ArrayList;
import java.util.List;

/**
 * DecodeOption 인터페이스 공통 구현
 * 
 * @author : 박성수
 */
public class DecodeOptionBase implements DecodeOption {
	
	private String header;
	private String footer;
	
	private int thumbnailOption;
	
	private List thumbnailDimensionList;
	
	private CID cidConvertor;	
	
	private boolean trimFormTag;
	
	private boolean covertBMP2JPEG;
	
	private boolean trimWordTag;
	
	private boolean trimScriptTag;
	
	private String forceReplaceContentType;
	
	private boolean htmlFixForm;
	
	/*
	 * @see hanwha.neo.commons.mime.decoder.DecodeOption#isCovertBMP2JPEG()
	 */
	public boolean isCovertBMP2JPEG() {
		return covertBMP2JPEG;
	}
	
	/*
	 * @see hanwha.neo.commons.mime.decoder.DecodeOption#setCovertBMP2JPEG(boolean)
	 */
	public void setCovertBMP2JPEG(boolean covertBMP2JPEG) {
		this.covertBMP2JPEG = covertBMP2JPEG;
	}
	
	/*
	 * @see hanwha.neo.commons.mime.decoder.DecodeOption#isTrimFormTag()
	 */
	public boolean isTrimFormTag() {
		return trimFormTag;
	}
	
	/*
	 * @see hanwha.neo.commons.mime.decoder.DecodeOption#setTrimFormTag(boolean)
	 */
	public void setTrimFormTag(boolean trimFormTag) {
		this.trimFormTag = trimFormTag;
	}

	/*
	 * @see hanwha.neo.commons.mime.decoder.DecodeOption#getCidConvertor()
	 */
	public CID getCidConvertor() {
		return cidConvertor;
	}
	
	/*
	 * @see hanwha.neo.commons.mime.decoder.DecodeOption#setCidConvertor(hanwha.neo.commons.mime.decoder.CID)
	 */
	public void setCidConvertor(CID cidConvertor) {
		this.cidConvertor = cidConvertor;
	}
	
	/*
	 * @see hanwha.neo.commons.mime.decoder.DecodeOption#getThumbnailOption()
	 */
	public int getThumbnailOption() {
		return thumbnailOption;
	}
	
	/*
	 * @see hanwha.neo.commons.mime.decoder.DecodeOption#setThumbnailOption(int)
	 */
	public void setThumbnailOption(int thumbnailOption) {
		this.thumbnailOption = thumbnailOption;
		if ( thumbnailOption != DecodeOption.ThumbnailOption_NONE ) {
			initThumbnailImage();
		}
	}
	
	public void setForceContentType(String contentType) {
		this.forceReplaceContentType = contentType;
	}
	
	/*
	 * @see hanwha.neo.commons.mime.decoder.DecodeOption#getFooter()
	 */
	public String getFooter() {
		return footer;
	}
	
	/*
	 * @see hanwha.neo.commons.mime.decoder.DecodeOption#setFooter(java.lang.String)
	 */
	public void setFooter(String footer) {
		this.footer = footer;
	}
	
	/*
	 * @see hanwha.neo.commons.mime.decoder.DecodeOption#getHeader()
	 */
	public String getHeader() {
		return header;
	}
	
	/*
	 * @see hanwha.neo.commons.mime.decoder.DecodeOption#setHeader(java.lang.String)
	 */
	public void setHeader(String header) {
		this.header = header;
	}
	
	/*
	 * @see hanwha.neo.commons.mime.decoder.DecodeOption#createMetadata()
	 */
	public boolean createMetadata() {
		return (getThumbnailOption() != DecodeOption.ThumbnailOption_NONE);
	}
	
	/**
	 * 썸네일 이미지 리스트 초기화
	 */
	private void initThumbnailImage() {
		if ( thumbnailDimensionList == null ) {
			thumbnailDimensionList = new ArrayList();
			thumbnailDimensionList.add(new Dimension(78, 78));
		}
	}
	
	/*
	 * @see hanwha.neo.commons.mime.decoder.DecodeOption#setThumbnailSize(int, int)
	 */
	public void setThumbnailSize(int width, int height) {
		Dimension defaultDimension = getDefaultDimension();
		defaultDimension.setSize(width, height);
	}
	
	/**
	 * 기본 썸네일 이미지 Dimension 참조
	 * @return 기본 썸네일 이미지 Dimension
	 */
	private Dimension getDefaultDimension() {
		initThumbnailImage();
		return (Dimension)thumbnailDimensionList.get(0);
	}
	
	/*
	 * @see hanwha.neo.commons.mime.decoder.DecodeOption#getThumbnailWidth()
	 */
	public int getThumbnailWidth() {
		return new Double(getDefaultDimension().getWidth()).intValue();
	}
	
	/*
	 * @see hanwha.neo.commons.mime.decoder.DecodeOption#getThumbnailHeight()
	 */
	public int getThumbnailHeight() {
		return new Double(getDefaultDimension().getHeight()).intValue();
	}
	
	/*
	 * @see hanwha.neo.commons.mime.decoder.DecodeOption#addThumbnailSize(int, int)
	 */
	public void addThumbnailSize(int width, int height) {
		Dimension dimension = new Dimension(width, height);
		thumbnailDimensionList.add(dimension);
	}
	
	/*
	 * @see hanwha.neo.commons.mime.decoder.DecodeOption#getThumbnailDimensionList()
	 */
	public List getThumbnailDimensionList() {
		return thumbnailDimensionList;
	}
	
	/*
	 * @see hanwha.neo.commons.mime.decoder.DecodeOption#isTrimWordTag()
	 */
	public boolean isTrimWordTag() {
		return trimWordTag;
	}

	/*
	 * @see hanwha.neo.commons.mime.decoder.DecodeOption#setTrimWordTag(boolean)
	 */
	public void setTrimWordTag(boolean trimWordTag) {
		this.trimWordTag = trimWordTag;
	}
	
	/*
	 * @see hanwha.neo.commons.mime.decoder.DecodeOption#isTrimScriptTag()
	 */
	public boolean isTrimScriptTag() {
		return trimScriptTag;
	}

	/*
	 * @see hanwha.neo.commons.mime.decoder.DecodeOption#setTrimScriptTag(boolean)
	 */
	public void setTrimScriptTag(boolean trimScriptTag) {
		this.trimScriptTag = trimScriptTag;
	}

	/**
	 * @return the forceReplaceContentType
	 */
	public String getForceReplaceContentType() {
		return forceReplaceContentType;
	}

	/**
	 * @param forceReplaceContentType the forceReplaceContentType to set
	 */
	public void setForceReplaceContentType(String forceReplaceContentType) {
		this.forceReplaceContentType = forceReplaceContentType;
	}

	/**
	 * @return the htmlFixForm
	 */
	public boolean isHtmlFixForm() {
		return htmlFixForm;
	}

	/**
	 * @param htmlFixForm the htmlFixForm to set
	 */
	public void setHtmlFixForm(boolean htmlFixForm) {
		this.htmlFixForm = htmlFixForm;
	}
	
	

}
