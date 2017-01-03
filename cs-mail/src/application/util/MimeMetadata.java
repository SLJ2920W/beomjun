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

import application.util.FileNameUtils;
import application.util.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author : Administrator
 */
public class MimeMetadata {
	
	private String filePath;

	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	

//	public void moveToReal() throws Exception {
//		
//		// mht file
//		File tempMhtFile = new File(Module.DIR_TEMP, filePath);
//		File targetMhtFile = new File(Module.DIR_BASE, filePath);
//
//		FileUtils.moveFile(tempMhtFile, targetMhtFile);
//		
//		//htm file
//		File tempHtmFile = FileNameUtils.getFileByExtension(tempMhtFile, "htm");
//		File targetHtmFile = FileNameUtils.getFileByExtension(targetMhtFile, "htm");
//		
//		FileUtils.moveFile(tempHtmFile, targetHtmFile);
//		
//		//meta file
//		File tempTxtFile = FileNameUtils.getFileByExtension(tempMhtFile, "txt");
//		File targetTxtFile = FileNameUtils.getFileByExtension(targetMhtFile, "txt");
//		
//		FileUtils.moveFile(tempTxtFile, targetTxtFile);
//		
//		// img dir
//		File tempImgDir = new File(tempMhtFile.getParentFile(), FileNameUtils.trimExtension(tempMhtFile.getName()));
//		
//		if ( tempImgDir.exists() ) {	// 이미지 폴더 존재할 경우
//			File targetImgDir = new File(targetMhtFile.getParentFile(), FileNameUtils.trimExtension(targetMhtFile.getName()));
//			FileUtils.moveDirectory(tempImgDir, targetImgDir);
//		}
//		
//	}
	
	
//	public void moveToReal(String targetDir) throws Exception {
//		
//		// mht file
//		File tempMhtFile = new File(Module.DIR_TEMP, filePath);
//		File baseDirFile = new File(Module.DIR_BASE, targetDir);
//		File targetMhtFile = new File(baseDirFile.getAbsolutePath(), filePath);
//
//		FileUtils.moveFile(tempMhtFile, targetMhtFile);
//		
//		//htm file
//		File tempHtmFile = FileNameUtils.getFileByExtension(tempMhtFile, "htm");
//		File targetHtmFile = FileNameUtils.getFileByExtension(targetMhtFile, "htm");
//		
//		FileUtils.moveFile(tempHtmFile, targetHtmFile);
//		
//		//meta file
//		File tempTxtFile = FileNameUtils.getFileByExtension(tempMhtFile, "txt");
//		File targetTxtFile = FileNameUtils.getFileByExtension(targetMhtFile, "txt");
//		
//		FileUtils.moveFile(tempTxtFile, targetTxtFile);
//		
//		// img dir
//		File tempImgDir = new File(tempMhtFile.getParentFile(), FileNameUtils.trimExtension(tempMhtFile.getName()));
//		
//		if ( tempImgDir.exists() ) {	// 이미지 폴더 존재할 경우
//			File targetImgDir = new File(targetMhtFile.getParentFile(), FileNameUtils.trimExtension(targetMhtFile.getName()));
//			FileUtils.moveDirectory(tempImgDir, targetImgDir);
//		}
//		
//	}
	
	
	private Map images;
	
	
	private long imageSize = 0;
	private String thumbnailFirst;
	private Map thumbnailImagesMap;
	private List thumbnailDimensionList;	
	
	private JSONObject metaInfo;
	

	/**
	 * 메타데이터 정보를 JSON 객체 형식으로 변환
	 * @return JSON 형식으로 변환된 메타데이터
	 * @throws Exception
	 */
	public JSONObject toJSONObject() throws Exception {
		JSONObject metaInfo = new JSONObject();
		metaInfo.put("thumbnailImagesMap", thumbnailImagesMap);
		metaInfo.put("thumbnailDimensionList", thumbnailDimensionList);
		metaInfo.put("images", images);
		return metaInfo;
	}
	
	public void populate(File jsonMetaFile) throws FileNotFoundException, JSONException, IOException {
		metaInfo = new JSONObject(FileUtils.getCotents(jsonMetaFile));
	}
	

	/**
	 * @return the images
	 */
	public Map getImages() throws Exception {
		if ( images != null ) return images;
		if ( metaInfo == null || !metaInfo.has("images") ) return null;
		JSONObject imagesObj = (JSONObject)metaInfo.get("images");
		images = new LinkedHashMap(imagesObj.length());
		for(Iterator keys = imagesObj.keys(); keys.hasNext(); ) {
			String key = (String)keys.next();
			String value = imagesObj.getString(key);
			images.put(key, value);
		}
		return images;
	}

	/**
	 * @param images the images to set
	 */
	public void setImages(Map images) {
		this.images = images;
	}

//	public long getSize() {
//		File targetMhtFile = new File(Module.DIR_BASE, filePath);
//		return targetMhtFile.length()+getImageSize();
//	}

	/**
	 * @return the imageSize
	 */
	public long getImageSize() {
		return imageSize;
	}

	/**
	 * @param imageSize the imageSize to set
	 */
	public void setImageSize(long imageSize) {
		this.imageSize = imageSize;
	}

	/**
	 * @return the thumbnailFirst
	 */
	public String getThumbnailFirst() {
		if ( thumbnailFirst == null ) return null;
		if ( filePath == null ) return thumbnailFirst;
		String baseFilePath = ( filePath.indexOf('/') > 0 ) ? filePath.substring(0, filePath.lastIndexOf("/")) : filePath;
		return baseFilePath+"/"+thumbnailFirst;
	}

	/**
	 * @param thumbnailFirst the thumbnailFirst to set
	 */
	public void setThumbnailFirst(String thumbnailFirst) {
		this.thumbnailFirst = thumbnailFirst;
	}

	/**
	 * @return the thumbnailImagesMap
	 */
	public Map getThumbnailImagesMap() throws Exception {
		if ( thumbnailImagesMap != null ) return thumbnailImagesMap;
		if ( metaInfo == null || !metaInfo.has("thumbnailImagesMap") ) return null;
		if ( thumbnailImagesMap == null ) {
			thumbnailImagesMap = new HashMap();
		}
		JSONObject tnImgMapObj = (JSONObject)metaInfo.get("thumbnailImagesMap");
		for(Iterator tnImgMapIter = tnImgMapObj.keys(); tnImgMapIter.hasNext(); ) {
			String mapKey = (String)tnImgMapIter.next();
			JSONObject imgMapObj = (JSONObject)tnImgMapObj.get(mapKey);
			Map thumbnailImages = new LinkedHashMap();
			for(Iterator tnImgIter = imgMapObj.keys(); tnImgIter.hasNext(); ) {
				String cidName = (String)tnImgIter.next();
				String imgName = imgMapObj.getString(cidName);
				thumbnailImages.put(cidName, imgName);
			}
			thumbnailImagesMap.put(mapKey, thumbnailImages);
		}
		return thumbnailImagesMap;
	}

	/**
	 * @param thumbnailImagesMap the thumbnailImagesMap to set
	 */
	public void setThumbnailImagesMap(Map thumbnailImagesMap) {
		this.thumbnailImagesMap = thumbnailImagesMap;
	}
	
	/*
	 * @see hanwha.neo.commons.mime.decoder.MimeMetadata#getThumbnailImages(java.lang.String)
	 */
	public Map getThumbnailImages(String key) {
		if ( thumbnailImagesMap.containsKey(key) ) {
			return (Map)thumbnailImagesMap.get(key);
		} else {
			return (Map)thumbnailImagesMap.get("thumbnailImages_"+key);
		}
	}
	
	/**
	 * 썸네일 이미지 Map 추가
	 * @param key 이미지 키 (크기정보)
	 * @param thumbnailImages
	 */
	public void addThumbnailImages(String key, Map thumbnailImages) {
		if ( thumbnailImagesMap == null ) {
			thumbnailImagesMap = new HashMap();
		}
		thumbnailImagesMap.put(key, thumbnailImages);
	}

	public List getThumbnailDimensionList() throws Exception {
		if ( thumbnailDimensionList != null ) return thumbnailDimensionList;
		if ( metaInfo == null || !metaInfo.has("thumbnailDimensionList") ) return null;
		JSONArray jsonArray = metaInfo.getJSONArray("thumbnailDimensionList");
		List thumbnailDimensionList = new ArrayList(jsonArray.length());
		for(int i=0; i<jsonArray.length(); i++) {
			thumbnailDimensionList.add((String)jsonArray.get(i));
		}
		return thumbnailDimensionList;
	}

	/**
	 * @param thumbnailDimensionList the thumbnailDimensionList to set
	 */
	public void setThumbnailDimensionList(List thumbnailDimensionList) {
		this.thumbnailDimensionList = thumbnailDimensionList;
	}
	
//	public File getEncodedFile() {
//		return new File(Module.DIR_BASE, getFilePath());
//	}
	
//	public File getDecodedFile() {
//		return FileNameUtils.getFileByExtension(getEncodedFile(), "htm");
//	}
	
}
