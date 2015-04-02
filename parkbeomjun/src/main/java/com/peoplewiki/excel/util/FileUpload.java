package com.peoplewiki.excel.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.peoplewiki.excel.poi.domain.PoiBean;

public class FileUpload {

	// 파일 업로드
	public static List<Map<String, Object>> fileUpload(PoiBean uploadForm, String path, String saveFolder) {
		List<MultipartFile> files = uploadForm.getFiles();
		String uploadPath = path;

		File realUploadDir = new File(uploadPath);
		if (!realUploadDir.exists())
			realUploadDir.mkdirs();

		String fileName, fileHead, organizedFilePath = "";
		InputStream inputStream = null;
		OutputStream outputStream = null;
		int readBytes = 0;
		byte[] buffer = new byte[8192];
		MultipartFile multipartFile = null;
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();

		// 스트림, 채널 선언
		FileInputStream fileInputStream = null;
		FileOutputStream fileOutputStream = null;
		FileChannel fcin = null;
		FileChannel fcout = null;
		String savePath = "D:\\DEV\\tempFile\\parkbeomjun\\fileRoot\\bak";

		if (null != files && files.size() > 0) {

			try {
				for (int i = 0; i < files.size(); i++) {
					multipartFile = files.get(i);

					if (multipartFile.getSize() <= 0)
						continue;

					// 파일명 바꾸기
					UUID randomUUID = UUID.randomUUID();
					fileName = multipartFile.getOriginalFilename();
					fileHead = fileName.substring(0, fileName.indexOf("."));
					fileName = fileName.replace(fileHead, String.valueOf(randomUUID));
					// 파일 스트림
					organizedFilePath = realUploadDir + File.separator + fileName;

					// 파일 리스트에 담기
					map = new HashMap<String, Object>();

					map.put("saveFolder", saveFolder);
					map.put("real_name", multipartFile.getOriginalFilename());
					map.put("parse_name", fileName);
					map.put("path", String.valueOf(realUploadDir));
					map.put("size", String.valueOf(multipartFile.getSize()));
					map.put("organizedFilePath", organizedFilePath);
					list.add(map);
					
					outputStream = new FileOutputStream(organizedFilePath);
					inputStream = multipartFile.getInputStream();
					while ((readBytes = inputStream.read(buffer, 0, 8192)) != -1) {
						outputStream.write(buffer, 0, readBytes);
					}

					// BAKUP START
					try {
						File bakFolder = new File(savePath);
						if (!bakFolder.exists())
							bakFolder.mkdir();

						String backupFilePath = savePath + File.separator + fileName;
						// 스트림 생성
						System.out.println(organizedFilePath);
						fileInputStream = new FileInputStream(organizedFilePath);
						fileOutputStream = new FileOutputStream(backupFilePath);
						// 채널 생성
						fcin = fileInputStream.getChannel();
						fcout = fileOutputStream.getChannel();

						// 채널을 통한 스트림 전송
						long size = fcin.size();
						fcin.transferTo(0, size, fcout);
					} catch (IOException ie) {
						ie.printStackTrace();
					} finally {
						fcout.close();
						fcin.close();
						fileOutputStream.close();
						fileInputStream.close();
					}

				}


			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (inputStream != null)
						inputStream.close();
					if (outputStream != null)
						outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return list;

	}

}
