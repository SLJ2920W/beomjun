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

import java.util.Map;

/**
 * 이미지 CID 처리 인터페이스 
 * 
 * @author : 박성수
 */
public interface CID {
	
	/**
	 * 이미지 CID 변경 
	 * @param contents cid가 포함된 HTML 문서 원문
	 * @param images key:cid, value:이미지경로가 들어있는 Map
	 * @return 변경된 HTML 문자열
	 * @throws Exception
	 */
	public String convertCID(String contents, Map images) throws Exception;
	
}
