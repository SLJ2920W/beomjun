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

import java.util.Iterator;
import java.util.Map;

/**
 * 로컬 경로에 MIME을 디코딩 할 경우를 위한 CID 컨버터
 * 
 * @author : 박성수
 */
public class CID4Local implements CID {
	
	/*
	 * @see hanwha.neo.commons.mime.decoder.CID#convertCID(java.lang.String, java.util.Map)
	 */
	public String convertCID(String contents, Map images) throws Exception {
		if ( images == null || images.size() == 0 ) return contents;
		String cidKey = "cid:";
		StringBuffer strRet = new StringBuffer(contents);
		
		int startPos = 0;
		for(Iterator entrys = images.entrySet().iterator(); entrys.hasNext(); ) {
			Map.Entry entry = (Map.Entry)entrys.next();
			String curCID = cidKey+entry.getKey();
			
			while(true) {
				if ( startPos > 0 ) {
					startPos = strRet.indexOf(curCID);
				} else {
					startPos = strRet.indexOf(curCID, startPos);
				}
				strRet.replace(startPos, startPos+curCID.length(), (String)entry.getValue());
				if ( strRet.indexOf(curCID,startPos+curCID.length()) > 0 ) {
					continue;
				}
				break;
			}
		}
		return strRet.toString();
	}

}
