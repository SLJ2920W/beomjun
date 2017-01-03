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

//import hanwha.neo.commons.file.IFile;
//import hanwha.neo.commons.file.IFile4EP;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * 파일 관련 유틸
 * 
 * @author : 박성수
 */
public class FileUtils {
	
	/**
	 * 파일 복사
	 * @param srcFile 원본 파일
	 * @param destFile 복사할 파일
	 * @throws IOException
	 */
	public static void copyFile(File srcFile, File destFile) throws IOException {
		if ( !destFile.getParentFile().exists() ) destFile.getParentFile().mkdirs();
		org.apache.commons.io.FileUtils.copyFile(srcFile, destFile);
	}
	
	/**
	 * 파일 이동 (byffer size : 1024 * 4 = 4096)
	 * @param srcFile 원본 파일
	 * @param destFile 이동할 경로
	 * @throws IOException
	 */
	public static void moveFile(File srcFile, File destFile) throws IOException {
		if ( !destFile.getParentFile().exists() ) destFile.getParentFile().mkdirs();
		org.apache.commons.io.FileUtils.moveFile(srcFile, destFile);
	}
	/**
	public static void moveFile(File srcFile, File destFile) throws IOException {
		if ( !destFile.getParentFile().exists() ) destFile.getParentFile().mkdirs();
		if ( !destFile.exists() ){
			org.apache.commons.io.FileUtils.moveFile(srcFile, destFile);
		}
	}
	**/
	
	/**
	 * 디렉 이동 (byffer size : 1024 * 4 = 4096)
	 * @param srcFile 원본 파일
	 * @param destFile 이동할 경로
	 * @throws IOException
	 */
	public static void moveDirectory(File srcDir, File destDir) throws IOException {
		if ( destDir.exists() ) {
			File[] files = srcDir.listFiles();
			for(int i=0; i<files.length; i++) {
				File file = files[i];
				org.apache.commons.io.FileUtils.moveFile(file, new File(destDir, file.getName()));
			}
		} else {
			//org.apache.commons.io.FileUtils.moveDirectory(srcDir, destDir);
			//대생사용자일 경우 디렉토리를 삭제할 수 없다는 에러가 발생하므로 copy만 하도록 변경, kimwk 120206			
			org.apache.commons.io.FileUtils.copyDirectory(srcDir, destDir);
		}
	}
	
	/**
	 * 디렉 이동 (byffer size : 1024 * 4 = 4096)
	 * @param srcFile 원본 파일
	 * @param destFile 이동할 경로
	 * @throws IOException
	 */
	public static void copyDirectory(File srcDir, File destDir) throws IOException {
		if ( !srcDir.exists() ) return;
		if ( destDir.exists() ) {
			File[] files = srcDir.listFiles();
			for(int i=0; i<files.length; i++) {
				File file = files[i];
				org.apache.commons.io.FileUtils.copyFile(file, new File(destDir, file.getName()));
			}
		} else {
			org.apache.commons.io.FileUtils.copyDirectory(srcDir, destDir);
		}
	}
	
	/**
	 * 문자열을 파일로 저장 (UTF-8로 저장)
	 * @param file 저장될 파일
	 * @param contents 저장할 문자열
	 * @throws IOException
	 */
	public static void saveContents(File file, String contents) throws IOException {
		saveContents(file, contents, "UTF-8");
	}	
	
	/**
	 * 문자열을 파일로 저장 
	 * NOTE: 예외적인 경우를 제외하고는 encoding을 직접 지정하는 이 메소드를 사용하지 말고 
	 * UTF-8 인코딩을 디폴트로 사용하는 saveContents(File file, String contents) 메소드 사용할 것
	 * @param file 저장될 파일
	 * @param contents 저장할 문자열
	 * @param encoding 저장하고자 하는 인코딩
	 * @throws IOException
	 */
	public static void saveContents(File file, String contents, String encoding) throws IOException {	
		if ( ! file.getParentFile().exists() ) {
			file.getParentFile().mkdirs();
		}
		OutputStreamWriter writer = null;
		OutputStream outputStream = null; 
		try {
			outputStream = new BufferedOutputStream(new FileOutputStream(file), 4096);
			writer = new OutputStreamWriter(outputStream, encoding);
			writer.write(contents);
			writer.flush();
		} finally {
			if ( outputStream != null ) {
				try {
					outputStream.close();
				} catch (IOException e) {
				}
			}
			if ( writer != null ) {
				try {
					writer.close();
				} catch (IOException e) {
				}
			}
		}			
	}
	
	/**
	 * 파일 내용을 문자열로 얻어냄 (UTF-8로 저장된 파일)
	 * @param file 읽을 파일
	 * @return 파일 문자열 스트링
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String getCotents(File file) throws FileNotFoundException, IOException {
		return IOUtils.getString(new BufferedInputStream(new FileInputStream(file), 4096));
	}
	
	/**
	 * 파일 내용을 문자열로 얻어냄 
	 * @param file 읽을 파일
	 * @param encoding 저장된 파일 인코딩
	 * @return 파일 문자열 스트링
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String getCotents(File file, String encoding) throws FileNotFoundException, IOException {
		return IOUtils.getString(new BufferedInputStream(new FileInputStream(file), 4096), encoding);
	}
	
  
	
	/**
	 * 파일삭제
	 */
	public static void delFile(File file) {
        if (file.exists()) {
        	file.delete();
        }

        // 상위 Directory 삭제(파일없을경우..)
        if(file != null){
            String sParentFilePath = file.getParent();
            File dir = new File(sParentFilePath);
            if(dir.isDirectory()) {
                if(dir.list().length == 0){
                    dir.delete();
                }
            }
        }
	}
	
	/**
	 * 지정된 path 기준으로 하위 풀더와 풀더에 포함되어 있는 화일을 다 삭제합니다.
	 */
	public static boolean deleteDir(String path) { 
		return deleteDir(new File(path)); 
	} 

	public static boolean deleteDir(File file) { 
		if (!file.exists()) 
			return true; 
	
		File[] files = file.listFiles(); 
		for (int i = 0; i < files.length; i++) { 
			if (files[i].isDirectory()) { 
				deleteDir(files[i]); 
			} else { 
				files[i].delete(); 
			} 
		}
		return file.delete(); 
	}
	
	public static String getFileEncoding(File file) throws Exception {
		String encType = "UTF-8";
		if (file.exists()) {
			FileInputStream fis = new FileInputStream(file);
			byte[] b = new byte[4];
			fis.read(b, 0, 4);
			
			if( (b[0] & 0xFF) == 0xEF && (b[1] & 0xFF) == 0xBB && (b[2] & 0xFF) == 0xBF )
				encType = "UTF-8";
			else if( (b[0] & 0xFF) == 0xFE && (b[1] & 0xFF) == 0xFF )
				encType = "UTF-16BE";
			else if( (b[0] & 0xFF) == 0xFF && (b[1] & 0xFF) == 0xFE )
				encType = "UTF-16LE";
			else if( (b[0] & 0xFF) == 0x00 && (b[1] & 0xFF) == 0x00 && 
			         (b[0] & 0xFF) == 0xFE && (b[1] & 0xFF) == 0xFF )
				encType = "UTF-32BE";
			else if( (b[0] & 0xFF) == 0xFF && (b[1] & 0xFF) == 0xFE && 
			         (b[0] & 0xFF) == 0x00 && (b[1] & 0xFF) == 0x00 )
				encType = "UTF-32LE";
			else
				encType = "EUC-KR";
		}
		return encType;
	}

	// 모바일용 Htm/Html파일 인코딩 방식(charset) 구하는 함수
	public static String getHtmlFileEncoding(File file) throws Exception {
		String encType = "UTF-8";
		if (file.exists()) {
			try {
				FileReader fileReader = new FileReader(file);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				String line;
				
				String upperLine = null;
				while ((line = bufferedReader.readLine()) != null) {
					upperLine = line.toUpperCase();
					if(line.contains("charset")) {
						if(line.toUpperCase().contains("UTF-8")) encType = "UTF-8";
						else if(upperLine.contains("EUC-KR")) encType = "EUC-KR";
						else if(upperLine.contains("UTF-16BE")) encType = "UTF-16BE";
						else if(upperLine.contains("UTF-16LE")) encType = "UTF-16LE";
						else if(upperLine.contains("UTF-32BE")) encType = "UTF-32BE";
						else if(upperLine.contains("UTF-32LE")) encType = "UTF-32LE";			
						break;
					}
				}
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
		return encType;
	}
	
	public static void main(String args[]) throws Exception {
		File inFile = new File("D:\\temp\\test.txt");
		String inFileString = FileUtils.getCotents(inFile);
		
		for(int i=0; i<10; i++) {
			File outFile = new File("D:\\temp\\test_"+i+".txt");
			long stdTime = System.currentTimeMillis();
			
			FileUtils.saveContents(outFile, inFileString);
			
			System.out.println( (System.currentTimeMillis()-stdTime) + "" );
		}
	}
}
