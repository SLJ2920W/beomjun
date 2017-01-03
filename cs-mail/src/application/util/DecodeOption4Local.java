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

/**
 * 로컬용 마임 디코딩 옵션
 * 
 * @author : 박성수
 */
public class DecodeOption4Local extends DecodeOptionBase {
	
	/**
	 * 생성자
	 */
	public DecodeOption4Local() {
		setThumbnailOption(DecodeOption.ThumbnailOption_NONE);
		setCidConvertor(new CID4Local());
	}
	
	public DecodeOption4Local(String contentType) {
		setForceContentType(contentType);
		setThumbnailOption(DecodeOption.ThumbnailOption_NONE);
		setCidConvertor(new CID4Local());	
	}

}
