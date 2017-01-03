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
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 파일이름 관련 유틸
 * 
 * @author : 박성수
 */
public class FileNameUtils {

	/**
	 * 파일을 저장할 기본 경로
	 * 
	 * @param companyId
	 * @param moduleName
	 *            Module.getName()
	 * @param subDir
	 *            document or attaches
	 * @return
	 */
	public static String createBasePath(String companyId, String moduleName, String subDir) {
		return companyId + "/" + moduleName + "/" + subDir;
	}

	/**
	 * 파일 확장자 얻어냄
	 * 
	 * @param file
	 *            파일
	 * @return 파일 확장자
	 */
	public static String getExtension(File file) {
		return getExtension(file.getName());
	}

	/**
	 * 파일 확장자 얻어냄
	 * 
	 * @param filePath
	 *            파일경로 or 파일명
	 * @return 파일 확장자
	 */
	public static String getExtension(String filePath) {
		String strRet = "";
		if (filePath == null)
			return strRet;
		if (!filePath.startsWith(".") && filePath.lastIndexOf(".") < 1) {
			return "";
		}
		int index = filePath.lastIndexOf(".") + 1;

		if (index > -1) {
			strRet = filePath.substring(index);
		}
		return strRet;
	}

	/**
	 * 확장자만 다른 파일 객체 생성 (동일 경로에 이름이 같고 확장자만 다른 파일 객체)
	 * 
	 * @param orgFile
	 *            원본 파일
	 * @param extension
	 *            생성하고자 하는 파일 확장자
	 * @return 주어진 extension을 갖는 파일 객체
	 * @throws Exception
	 */
	public static File getNewFileByExtension(File orgFile, String extension) throws Exception {
		if (orgFile == null)
			throw new RuntimeException("원본파일이 NULL 입니다.");
		return new File(orgFile.getParentFile(), getFileNameByExtension(orgFile.getName(), extension));
	}

	/**
	 * 확장자만 다른 파일 생성 <br>
	 * ex1) 2009/1/1/1234.xls -> 2009/1/1/1234.pdf <br>
	 * ex2) 1234.xls -> 1234.pdf
	 * 
	 * @param orgFileName
	 *            파일경로 or 파일명
	 * @param extension
	 *            생성하고자 하는 파일 확장자
	 * @return 주어진 extension을 갖는 파일경로(or 파일명)
	 */
	public static File getFileByExtension(File orgFile, String extension) {
		StringBuffer fileName = new StringBuffer(orgFile.getAbsolutePath());
		int fileNameIdx = fileName.lastIndexOf(".");
		if (fileNameIdx > 0) {
			fileName.replace(fileNameIdx + 1, fileName.length(), extension);
		} else {
			fileName.append(".").append(extension);
		}
		return new File(fileName.toString());
	}

	/**
	 * 확장자만 다른 파일 경로 생성 <br>
	 * ex1) 2009/1/1/1234.xls -> 2009/1/1/1234.pdf <br>
	 * ex2) 1234.xls -> 1234.pdf
	 * 
	 * @param orgFileName
	 *            파일경로 or 파일명
	 * @param extension
	 *            생성하고자 하는 파일 확장자
	 * @return 주어진 extension을 갖는 파일경로(or 파일명)
	 */
	public static String getFileNameByExtension(String orgFileName, String extension) {
		StringBuffer fileName = new StringBuffer(orgFileName);
		int fileNameIdx = fileName.lastIndexOf(".");
		if (fileNameIdx > 0) {
			fileName.replace(fileNameIdx + 1, fileName.length(), extension);
		} else {
			fileName.append(".").append(extension);
		}
		return fileName.toString();
	}

	public static File getFileByExtension(File orgFile, String appendix, String extension) throws Exception {
		if (orgFile == null)
			throw new Exception("원본파일이 NULL 입니다.");
		return new File(orgFile.getParentFile(), getFileNameByExtension(orgFile.getName(), appendix, extension));
	}

	public static String getFileNameByExtension(String orgFileName, String appendix, String extension) throws Exception {
		StringBuffer fileName = new StringBuffer(orgFileName);
		int fileNameIdx = fileName.lastIndexOf(".");
		if (fileNameIdx > 0) {
			fileName.replace(fileNameIdx, fileName.length(), appendix + "." + extension);
		}
		return fileName.toString();
	}

	/**
	 * 파일명에서 확장자를 잘라냄 (ex: 1234.xls -> 1234)
	 * 
	 * @param fileName
	 *            파일명
	 * @return 확장자 없어진 파일명
	 * @throws Exception
	 */
	public static String trimExtension(String fileName) {
		return (fileName.indexOf('.') > 0) ? fileName.substring(0, fileName.lastIndexOf(".")) : fileName;
	}

	/**
	 * 파일경로에서 파일명을 얻어냄 (ex: 2009/1/1/1234.xls -> 1234.xls)
	 * 
	 * @param filePath
	 *            파일경로
	 * @return 파일명
	 * @throws Exception
	 */
	public static String getFileName(String filePath) throws Exception {
		filePath = filePath.replace('\\', '/');
		return (filePath.indexOf('/') > 0) ? filePath.substring(filePath.lastIndexOf("/") + 1) : filePath;
	}

	/**
	 * 원본 파일에 appendix가 추가된 파일명 생성 <br>
	 * ex: 1234.xls -> 1234_tn.xls <br>
	 * getNewFileNameWithHypen("1234.xls", "tn");
	 * 
	 * @param orgFileName
	 *            파일명
	 * @param appendix
	 *            추가할 문자열
	 * @return 변경된 파일명
	 * @throws Exception
	 */
	public static String getNewFileNameWithHypen(String orgFileName, String appendix) throws Exception {
		StringBuffer fileName = new StringBuffer(orgFileName);
		int fileNameIdx = fileName.lastIndexOf(".");
		if (fileNameIdx > 0) {
			fileName.insert(fileNameIdx, "_" + appendix);
		} else {
			fileName.append(".").append(appendix);
		}
		return fileName.toString();
	}

}
