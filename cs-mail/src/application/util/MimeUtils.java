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

/**
 * MIME 관련 유틸
 * 
 * @author : 박성수
 */
public class MimeUtils {
	
	/**
	 * 마임파일 디코딩하여 htm 파일 생성
	 * Note : decodeLocal에서는 아래아 같은 제약 조건이 있음 
	 * <br> 1. 썸네일 생성 불가함(DecodeOption을 넘길 수 없으므로)
	 * <br> 2. MIME 파일이 있는 경로에 디코딩 파일 생성
	 * <br> 3. MIME 파일이 있는 경로에 파일명으로 이미지 디렉토리 생성하여 이미지 생성됨
	 * @param encFile 디코딩할 MIME 파일
	 * @return 디코딩된 htm 파일 경로
	 * @throws Exception
	 */
	public static File decodeLocal(File encFile) throws Exception {
		MhtmlDecoder decoder = new MhtmlDecoder();
		decoder.setOptions(new DecodeOption4Local());
		return decoder.save(encFile);
	}
	
	/**
	 * 마임파일 디코딩하여 htm 파일 생성
	 * Note : decodeLocal에서는 아래아 같은 제약 조건이 있음 
	 * <br> 1. 썸네일 생성 불가함(DecodeOption을 넘길 수 없으므로)
	 * <br> 2. MIME 파일이 있는 경로에 디코딩 파일 생성
	 * <br> 3. MIME 파일이 있는 경로에 파일명으로 이미지 디렉토리 생성하여 이미지 생성됨
	 * 
	 * <pre>
	 * *변경 사항
	 *  1. HTML 파일에 이미지 경로를  "tmp" 폴더 하위를 바라보게 하기 위해 변경 (리스트 출력시 제외할려고)
	 * 	1-2. 파일 내용에 이미지 태그 경로도 tmp 경로 추가 
	 * 	2. &lt;body&gt; 태그 없을 경우 대비
	 * 	3. html 태그 다음에 기존 메타 태그 삭제 하고 새로운 메타 태그 삽업 -> CHARSET=UTF-8 
	 * 	4. ks_c_5601-1987 인코딩 되어 있는 부분 utf=8 일괄 변경
	 * </pre>
	 * 
	 * @param encFile 디코딩할 MIME 파일
	 * @return 디코딩된 htm 파일 경로
	 * @throws Exception
	 */
	public static File decodeLocalForSearch(File encFile) throws Exception {
		MhtmlDecoder decoder = new MhtmlDecoder();
		decoder.setOptions(new DecodeOption4Local("UTF-8"));
		return decoder.save(encFile);
	}	
	
	public static void main(String args[]) throws Exception {
		MimeUtils.decodeLocalForSearch(new File("D:\\Hanwha\\매일 백업\\이미지\\1.eml"));
		
//		MimeMetadata s= MimeUtils.getMetadata(new File("D:\\Hanwha\\매일 백업\\이미지\\test.txt"));
//		System.out.println(f);
//		System.out.println(s);
		
	}
}
