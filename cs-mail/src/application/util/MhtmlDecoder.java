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
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import application.cs.mail.common.Selection;

/**
 * MIME 디코더
 * 
 * @author : 박성수
 */
public class MhtmlDecoder {

	private DecodeOption options;
	// private MimeMetadata result;

	/**
	 * 생성자
	 */
	public MhtmlDecoder() {
		// this.result = new MimeMetadata();
	}

	/**
	 * 마임 디코딩
	 * 
	 * @param encFile
	 *            디코딩할 마임 파일
	 * @return 디코딩된 HTM 파일 경로
	 * @throws Exception
	 */
	public File save(File encFile) throws Exception {
		File targetFile = FileNameUtils.getNewFileByExtension(encFile, "htm");
		save(encFile, targetFile);
		return targetFile;
	}

	/**
	 * 2016-12 파싱된 html 파일에 이미지 경로를 {@link mailViewTempFolderName} 폴더 하위를 바라보게
	 * 하기 위해 추가
	 * 
	 * @param imageMap
	 */
	public Map<String, String> changeImagePath(Map<String, String> imageMap) {
		if (imageMap == null)
			return null;

		Map<String, String> changeImageMap = imageMap.entrySet().stream()
				.collect(Collectors.toMap(p -> p.getKey(), p -> Selection.getInstance().getMailViewTempFolderName() + File.separator + p.getValue()));

		return changeImageMap;
	}

	/**
	 * 마임 디코딩 2016-12 replaceContentType에 변경 내역 추가
	 * 
	 * @param encIFile
	 *            디코딩할 마임 파일
	 * @param decIFile
	 *            디코딩될 HTM 파일 경로
	 * @return 디코딩 정보를 담고 있는 메타데이타
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public MimeOutputter save(File encFile, File decFile) throws Exception {
		File imageDir = getImageDirectory(decFile);
		MimeOutputter output = decode(encFile, imageDir);

		// 2016-12 추가
		output.getMetadata().setImages(changeImagePath(output.getMetadata().getImages()));
		StringBuffer contents = output.getContents();

		// 문서 내 강제 변경할 contentType이 지정되었을 경우
		String replaceContentType = this.options.getForceReplaceContentType();
		if (replaceContentType != null) {
			contents = replaceContentType(contents, replaceContentType);
		}

		if (options.getHeader() != null)
			contents.insert(0, options.getHeader());
		if (options.getFooter() != null)
			contents.append(options.getFooter());

		/**
		 * 일정이 첨부된 메일은 contents 가 null 이 나옴
		 */
		// HTML 내용을 교정함
		String fixedHTML = contents == null ? "fail" : contents.toString();
		if (options.isHtmlFixForm()) {
			boolean delScript = false;
			fixedHTML = ApFormHtmlFixer.fixFormFieldName(ApFormHtmlFixer.fixDocument(contents.toString(), delScript));
		}

		FileUtils.saveContents(decFile, fixedHTML);

		// Module module = decIFile.getModule();
		long imgSize = 0;
		long thumbnailImgSize = 0;
		try {
			if (output.getMetadata().getImages() != null && output.getMetadata().getImages().size() > 0) {
				// cid 이미지 경로 정보를 메타파일로 저장
				// result.setThumbnailOption(options.getThumbnailOption());
				// 썸네일 이미지 생성일 경우 해당 이미지 생성하고 정보를 메타파일로 저장
				if (options.getThumbnailOption() != DecodeOption.ThumbnailOption_NONE) {
					List<String> thumbnailDimensionList = new ArrayList<String>();
					int i = 0;
					for (Iterator<Dimension> thumbnailIter = options.getThumbnailDimensionList().iterator(); thumbnailIter.hasNext(); i++) {
						Dimension dimension = thumbnailIter.next();
						String dimensionInfo = new Double(dimension.getWidth()).intValue() + "x" + new Double(dimension.getHeight()).intValue();
						thumbnailDimensionList.add(dimensionInfo);
						Map<String, String> thumbnailImages = new LinkedHashMap<String, String>();
						for (Iterator<?> imgIter = output.getMetadata().getImages().entrySet().iterator(); imgIter.hasNext();) {
							@SuppressWarnings("rawtypes")
							Map.Entry img = (Map.Entry) imgIter.next();
							String imgCID = (String) img.getKey();
							String imgName = FileNameUtils.getNewFileNameWithHypen((String) img.getValue(), "tn" + dimensionInfo);
							if (imgName.endsWith("emf"))
								continue;
							File inputFile = new File(imageDir.getParentFile(), (String) img.getValue());
							File outputFile = new File(imageDir.getParentFile(), imgName);
							ImageUtils.createThumbnailImage(inputFile, outputFile,
									new Double(dimension.getWidth()).intValue(),
									new Double(dimension.getHeight()).intValue());
							thumbnailImages.put(imgCID, imgName);
							output.getMetadata().setThumbnailFirst(imgName);
							if (i == 0)
								imgSize += inputFile.length();
							thumbnailImgSize += outputFile.length();

							// 첫번째 이미지만 생성하는 경우에는 바로 break
							if (options.getThumbnailOption() == DecodeOption.ThumbnailOption_FIRST)
								break;
						}
						output.getMetadata().addThumbnailImages(dimensionInfo, thumbnailImages);
					}
					output.getMetadata().setThumbnailDimensionList(thumbnailDimensionList);
				} else {
					for (Iterator<?> imgIter = output.getMetadata().getImages().entrySet().iterator(); imgIter.hasNext();) {
						@SuppressWarnings("rawtypes")
						Map.Entry img = (Map.Entry) imgIter.next();
						File inputFile = new File(imageDir.getParentFile(), (String) img.getValue());
						imgSize += inputFile.length();
					}
				}
			}

			output.getMetadata().setImageSize(imgSize + thumbnailImgSize);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// if ( options.createMetadata() ) {
		// File metaFile = FileNameUtils.getNewFileByExtension(decFile, "txt");
		// FileUtils.saveContents(metaFile,
		// output.getMetadata().toJSONObject().toString());
		// }

		return output;
	}

	/**
	 * 마임 디코딩
	 * 
	 * @param encFile
	 *            디코딩할 마임 파일
	 * @return 디코딩된 정보를 담고 있는 MimeOutputter
	 * @throws Exception
	 */
	public MimeOutputter decode(File encFile) throws Exception {
		return decode(encFile, null);
	}

	/**
	 * 마임 디코딩
	 * 
	 * @param encFile
	 *            디코딩할 마임 파일
	 * @param imageDir
	 *            디코딩시 이미지를 풀어 놓을 디렉토리 위치
	 * @return 디코딩된 정보를 담고 있는 MimeOutputter
	 * @throws Exception
	 */
	public MimeOutputter decode(File encFile, File imageDir) throws Exception {
		MimeOutputter outputter = new MimeOutputter();
		BufferedInputStream in = null;
		try {
			// encFile = new
			// File("C:/temp/64119724ac10068a4faca67a2a88da8e.mht");
			// imageDir = new File("C:/temp");

			if (imageDir == null)
				imageDir = getImageDirectory(encFile);
			in = new BufferedInputStream(new FileInputStream(encFile));
			// MimeParser decoder = new JavaMailDecoder(new MimeMessage(null,
			// in));
			MimeParser decoder = createMimeParser(in);
			boolean textToHtml = true;
			// long stdTiem = System.currentTimeMillis();
			decoder.parse(textToHtml, imageDir, options.getCidConvertor(), options.isCovertBMP2JPEG());
			decoder.write(outputter);
			// System.out.println( (System.currentTimeMillis()-stdTiem) );
			if (options.isTrimFormTag()) {
				trimTag(outputter.getContents(), "<form");
				trimTag(outputter.getContents(), "</form>");
			}

			if (options.isTrimScriptTag()) {
				trimTag(outputter.getContents(), "<script", "</script>");
			}

			if (options.isTrimWordTag()) {
				trimTag(outputter.getContents(), "<!--[if", "<![endif]-->");
				trimTag(outputter.getContents(), "<o:SmartTagType");
				trimTag(outputter.getContents(), "<![if");
				trimTag(outputter.getContents(), "<![endif]>");
			}

			// 엑셀에서 붙여넣기 할 경우 DIV HEIGHT가 0px로 나오는 버그가 있음. DIV HEIGHT가 0px로 세팅되면
			// 내용이 보이지 않음
			fixContentHeight(outputter.getContents(), "HEIGHT: 0px", "HEIGHT: 100%");

			outputter.getMetadata().setImages(decoder.getImages());
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (in != null)
				in.close();
		}
		return outputter;
	}

	private MimeParser createMimeParser(InputStream in) throws Exception {
		try {
			return new Mime4jMimeParser(in);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Error e) {
			e.printStackTrace();
		}
		// return new JavaMailDecoder(new MimeMessage(null, in));
		return null;
	}

//	public List<AttachFile> saveAttachFiles(File encFile, String attachDirPath, boolean isSaveToReal) throws Exception {
//		List<AttachFile> attachFileList = null;
//		BufferedInputStream in = null;
//		try {
//			if (attachDirPath == null)
//				return null;
//
//			in = new BufferedInputStream(new FileInputStream(encFile));
//			JavaMailDecoder decoder = new JavaMailDecoder(new MimeMessage(null, in));
//			attachFileList = decoder.extreactAttachFiles(attachDirPath, isSaveToReal);
//		} catch (Exception e) {
//			throw e;
//		} finally {
//			if (in != null)
//				in.close();
//		}
//		return attachFileList;
//	}

	/**
	 * 디코딩 옵션 세팅
	 * 
	 * @param options
	 *            디코딩 옵션
	 */
	public void setOptions(DecodeOption options) {
		this.options = options;
	}

	/**
	 * 이미지 디렉토리 참조
	 * 
	 * @param targetFile
	 *            디코딩 파일 위치
	 * @return 이미지 디렉토리
	 * @throws Exception
	 */
	private File getImageDirectory(File targetFile) throws Exception {
		if (!targetFile.getParentFile().exists()) {
			targetFile.getParentFile().mkdirs();
		}
		return new File(targetFile.getParentFile() + File.separator + Selection.getInstance().getMailViewTempFolderName(), FileNameUtils.trimExtension(targetFile.getName()));
	}

	/**
	 * 문서 내 정의된 contenType 강제 변경
	 * 
	 * @param contents
	 *            문서 내용
	 * @param contentType
	 *            변경할 contentType
	 * @return
	 */
	private StringBuffer replaceContentType(StringBuffer contents, String contentType) {
		if ("".equals(contents.toString()))
			return null;
		String tmpContents = contents.toString();
		// [s] 2016-12 ~ 2017-01 추가
		int bodyIdx = tmpContents.toUpperCase().indexOf("<BODY");
		bodyIdx = bodyIdx < 0 ? 0 : bodyIdx;
		String headContents = tmpContents.substring(0, bodyIdx);
		String footContents = tmpContents.substring(bodyIdx, tmpContents.length());
		headContents = headContents.replaceAll("^<html.*?>", "<html><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">");
		if (headContents.equals("")) {
			footContents = footContents.replaceAll("<meta.*?>", "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">");
		}
		footContents = footContents.replaceAll("ks_c_5601-1987", "utf-8");
		Matcher m = Pattern.compile("<img.*?src=\"").matcher(footContents);
		if (m.find()) {
			footContents = m.replaceAll(m.group() + Selection.getInstance().getMailViewTempFolderName() + "/");
		}
		// [e] 2016-12 ~ 2017-01 추가
		String tempHeadContents = headContents.replaceAll("\"", "'").toUpperCase();
		int sIdx = tempHeadContents.indexOf("CONTENT='") + 9;
		int eIdx = tempHeadContents.indexOf("'", sIdx);
		if (sIdx > 0 && eIdx > sIdx) {
			String contentValue = headContents.substring(sIdx, eIdx);
			String[] attrs = contentValue.split(";");
			String res = "";
			for (int i = 0; i < attrs.length; i++) {
				if (attrs[i].toUpperCase().indexOf("CHARSET=") > 0) {
					String[] tmp = attrs[i].split("=");
					if (tmp.length > 1) {
						tmp[1] = contentType; // replace
						attrs[i] = tmp[0] + "=" + tmp[1];
					}
				}
				res += attrs[i] + ";";
			}
			if (res != null && res.length() > 1)
				res = res.substring(0, res.length() - 1);
			else
				res = contentValue;

			headContents = headContents.substring(0, sIdx) + res
					+ headContents.substring(eIdx, headContents.length());
		}
		return new StringBuffer(headContents + footContents);
	}

	/**
	 * 태그 삭제
	 * 
	 * @param viewContentBuffer
	 *            HTML 원문
	 * @param idom
	 *            삭제할 태그 문자열
	 */
	private void trimTag(StringBuffer viewContentBuffer, String idom) {
		int len = idom.length();
		int pos = 0;
		int oldpos = 0;
		while ((pos = viewContentBuffer.indexOf(idom, oldpos)) >= 0) {
			int srcIdx = pos;
			int srcStdIdx = srcIdx;
			int srcEndIdx = viewContentBuffer.indexOf(">", srcStdIdx + 1);
			viewContentBuffer.delete(srcStdIdx, srcEndIdx + 1);
			oldpos = pos + len;
		}
	}

	/**
	 * 태그 삭제
	 * 
	 * @param viewContentBuffer
	 *            HTML 원문
	 * @param startTag
	 *            태그 시작 문자열
	 * @param endTag
	 *            태그 종료 문자열
	 */
	private void trimTag(StringBuffer viewContentBuffer, String startTag, String endTag) {
		int len = startTag.length();
		int pos = 0;
		int oldpos = 0;
		while ((pos = viewContentBuffer.indexOf(startTag, oldpos)) >= 0) {
			int srcIdx = pos;
			int srcStdIdx = srcIdx;
			int srcEndIdx = viewContentBuffer.indexOf(endTag, srcStdIdx + 1);
			viewContentBuffer.delete(srcStdIdx, srcEndIdx + endTag.length() + 1);
			oldpos = pos + len;
		}
	}

	/**
	 * 태그 삭제
	 * 
	 * @param viewContentBuffer
	 *            HTML 원문
	 * @param idom
	 *            삭제할 태그 문자열
	 */
	private void fixContentHeight(StringBuffer viewContentBuffer, String idom, String idom2) {
		int len = idom.length();
		int pos = 0;
		int oldpos = 0;
		while ((pos = viewContentBuffer.indexOf(idom, oldpos)) >= 0) {
			int srcIdx = pos;
			int srcStdIdx = srcIdx;
			int srcEndIdx = srcStdIdx + len;
			viewContentBuffer.replace(srcStdIdx, srcEndIdx, idom2);
			oldpos = pos + idom2.length();
		}
	}

}
