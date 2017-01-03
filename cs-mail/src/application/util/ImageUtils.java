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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;

import net.sf.image4j.codec.bmp.BMPDecoder;

/**
 * 이미지 관련 공통 유틸리티
 * 
 * @author 박성수
 */
public class ImageUtils {

	/**
	 * 썸네일 이미지 생성 
	 * @param inputFile 원본 이미지 파일 경로
	 * @param outputFile 생성될 썸네일 이미지 파일 경로
	 * @param width 폭
	 * @param height 높이
	 * @return 성공 여부
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static boolean createThumbnailImage(String inputFile, String outputFile, int width, int height) throws FileNotFoundException, IOException {
		return createThumbnailImage(new File(inputFile), new File(outputFile), width, height);
	}
	
	/**
	 * 썸네일 이미지 생성 
	 * @param inputFile 원본 이미지 파일
	 * @param outputFile 생성될 썸네일 이미지 파일
	 * @param width 폭
	 * @param height 높이
	 * @return 성공 여부
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static boolean createThumbnailImage(File inputFile, File outputFile, int width, int height) throws FileNotFoundException, IOException {
		BufferedImage sourceImage = ImageIO.read(new BufferedInputStream(new FileInputStream(inputFile)));
		// width만 주고 height를 주지 않으면 width에 맞게 height scale이 자동조정됨 
		Image thumbnail = sourceImage.getScaledInstance(width, -1, Image.SCALE_SMOOTH);
		BufferedImage bufferedThumbnail = new BufferedImage(thumbnail.getWidth(null), height, BufferedImage.TYPE_INT_RGB);
		Graphics g = bufferedThumbnail.getGraphics();
		int thumbnailHeight = thumbnail.getHeight(null);
		// 1. bufferedThumbnail의 Graphics에 이미지를 drawing한 후, 여백이 검은색으로 채워지는 문제가 있음.
		// ->  남는 여백을 white로 채워야 함.
		// 2. 가로 비율이 더 큰 경우 이미지가 중앙에 위치하지 못하고 상단에 붙어버림.
		// -> 이미지 사이즈를 계산하여 세로축인 y position을 동적 계산해야 함. 
		
		// 가로 비율이 더 큰 경우 y position 계산   
		int yPosition = 0;
		if ( thumbnailHeight < height ) {
			yPosition = (int)(height-thumbnailHeight)/2;
		}
		g.setColor(Color.WHITE);
		// BufferedImage에 축소된 이미지 drawing (이 때 y position은 가운데 위치하도록 함)
		g.drawImage(thumbnail, 0, yPosition, thumbnail.getWidth(null), thumbnail.getHeight(null), Color.WHITE, null);
		// 가로 비율이 더 큰 경우 남는 여백을 white로 채움
		if ( thumbnailHeight < height ) {
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, width, yPosition);
			g.fillRect(0, yPosition+thumbnailHeight, width, (height-yPosition-thumbnailHeight));
		}
		boolean success = false;
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(outputFile));
			success = ImageIO.write(bufferedThumbnail, "jpeg", bos);
		} finally {
			try { bos.flush(); } catch (Exception e) {}
			try { bos.close(); } catch (Exception e) {}
		}
		return success;
	}

	/**
	 * 모바일 변환 이미지 생성 
	 * @param inputFile 원본 이미지 파일
	 * @return BufferedImage 원본 파일
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static BufferedImage getMobileBufferedImage(File inputFile) throws FileNotFoundException, IOException{

		return ImageIO.read(new BufferedInputStream(new FileInputStream(inputFile)));
	}
	
	/**
	 * 모바일 변환 이미지 생성 
	 * @param sourceImage 원본 이미지 파일
	 * @param outputFile 생성될 썸네일 이미지 파일
	 * @param width 폭
	 * @param height 높이
	 * @return 성공 여부
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static boolean createMobileImage(BufferedImage sourceImage, File outputFile, int width, int height) throws FileNotFoundException, IOException {

		// width만 주고 height를 주지 않으면 width에 맞게 height scale이 자동조정됨 
		Image thumbnail = sourceImage.getScaledInstance(width, -1, Image.SCALE_SMOOTH);
		BufferedImage bufferedThumbnail = new BufferedImage(thumbnail.getWidth(null), height, BufferedImage.TYPE_INT_RGB);
		Graphics g = bufferedThumbnail.getGraphics();
		//int thumbnailHeight = thumbnail.getHeight(null);
		// 1. bufferedThumbnail의 Graphics에 이미지를 drawing한 후, 여백이 검은색으로 채워지는 문제가 있음.
		// ->  남는 여백을 white로 채워야 함.
		// 2. 가로 비율이 더 큰 경우 이미지가 중앙에 위치하지 못하고 상단에 붙어버림.
		// -> 이미지 사이즈를 계산하여 세로축인 y position을 동적 계산해야 함. 
		
		// 가로 비율이 더 큰 경우 y position 계산   
		int yPosition = 0;
//		if ( thumbnailHeight < height ) {
//			yPosition = (int)(height-thumbnailHeight)/2;
//		}
//		g.setColor(Color.WHITE);
//		// BufferedImage에 축소된 이미지 drawing (이 때 y position은 가운데 위치하도록 함)
		g.drawImage(thumbnail, 0, yPosition, thumbnail.getWidth(null), thumbnail.getHeight(null), Color.WHITE, null);
//		// 가로 비율이 더 큰 경우 남는 여백을 white로 채움
//		if ( thumbnailHeight < height ) {
//			g.setColor(Color.WHITE);
//			g.fillRect(0, 0, width, yPosition);
//			g.fillRect(0, yPosition+thumbnailHeight, width, (height-yPosition-thumbnailHeight));
//		}
		boolean success = false;
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(outputFile));
			success = ImageIO.write(bufferedThumbnail, "jpeg", bos);
		} finally {
			try { bos.flush(); } catch (Exception e) {}
			try { bos.close(); } catch (Exception e) {}
		}
		return success;
	}
	
	
	/**
	 * 썸네일 이미지 생성 
	 * @param inputFile 원본 이미지 파일
	 * @param outputFile 생성될 썸네일 이미지 파일
	 * @param width 폭
	 * @param height 높이
	 * @return 성공 여부
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static boolean createOptimizeImage(File inputFile, File outputFile, int width, int height) throws FileNotFoundException, IOException {
		BufferedImage sourceImage = ImageIO.read(new BufferedInputStream(new FileInputStream(inputFile)));
		
		System.out.println("w:"+sourceImage.getWidth(null)+",h:"+sourceImage.getHeight(null));
		
		System.out.println("w:"+width+",h:"+height);
		
//		width = sourceImage.getWidth(null);
//		height = sourceImage.getHeight(null);
		
		System.out.println("w:"+width+",h:"+height);
		
		// width만 주고 height를 주지 않으면 width에 맞게 height scale이 자동조정됨 
		Image thumbnail = sourceImage.getScaledInstance(width, -1, Image.SCALE_SMOOTH);
		
//		if ( thumbnail.getWidth(null) < width ) return false;
		
		
		
		BufferedImage bufferedThumbnail = new BufferedImage(thumbnail.getWidth(null), height, BufferedImage.TYPE_INT_RGB);
		Graphics g = bufferedThumbnail.getGraphics();
		int thumbnailHeight = thumbnail.getHeight(null);
		// 1. bufferedThumbnail의 Graphics에 이미지를 drawing한 후, 여백이 검은색으로 채워지는 문제가 있음.
		// ->  남는 여백을 white로 채워야 함.
		// 2. 가로 비율이 더 큰 경우 이미지가 중앙에 위치하지 못하고 상단에 붙어버림.
		// -> 이미지 사이즈를 계산하여 세로축인 y position을 동적 계산해야 함. 
		
		// 가로 비율이 더 큰 경우 y position 계산   
		int yPosition = 0;
		if ( thumbnailHeight < height ) {
			yPosition = (int)(height-thumbnailHeight)/2;
		}
		g.setColor(Color.WHITE);
		// BufferedImage에 축소된 이미지 drawing (이 때 y position은 가운데 위치하도록 함)
		g.drawImage(thumbnail, 0, yPosition, thumbnail.getWidth(null), thumbnail.getHeight(null), Color.WHITE, null);
		// 가로 비율이 더 큰 경우 남는 여백을 white로 채움
		if ( thumbnailHeight < height ) {
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, width, yPosition);
			g.fillRect(0, yPosition+thumbnailHeight, width, (height-yPosition-thumbnailHeight));
		}
		boolean success = false;
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(outputFile));
			success = ImageIO.write(bufferedThumbnail, "jpeg", bos);
		} finally {
			try { bos.flush(); } catch (Exception e) {}
			try { bos.close(); } catch (Exception e) {}
		}
		return success;
	}
	
	/**
	 * 이미지를 jpeg 형태로 변환
	 * @param inputFile 원본 파일
	 * @param outputFile 변환될 jpeg 파일 경로
	 * @return 성공 여부
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static boolean saveToJPEG(File inputFile, File outputFile) throws FileNotFoundException, IOException {
		if ( "bmp".equalsIgnoreCase(FileNameUtils.getExtension(inputFile)) ) {
			return saveBMPToJPEG(new FileInputStream(inputFile), outputFile);
		} else {
			return saveToJPEG(new FileInputStream(inputFile), outputFile);
		}
	}
	
	/**
	 * 이미지를 jpeg 형태로 변환
	 * @param imgInputStream 원본 이미지 InputStream
	 * @param outputFile 변환될 jpeg 파일 경로
	 * @return 성공 여부
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static boolean saveToJPEG(InputStream imgInputStream, File outputFile) throws FileNotFoundException, IOException {
		BufferedImage sourceImage = ImageIO.read(new BufferedInputStream(imgInputStream));
		
		boolean success = false;
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(outputFile));
			success = ImageIO.write(sourceImage, "jpeg", bos);
		} finally {
			try { bos.flush(); } catch (Exception e) {}
			try { bos.close(); } catch (Exception e) {}
		}
		return success;
	}
	
	/**
	 * 이미지를 jpeg 형태로 변환 (jdk 1.4 버전에서 bmp 이미지를 ImageIO로 읽어들이지 못하는 버그 수정0
	 * @param imgInputStream 원본 이미지 InputStream
	 * @param outputFile 변환될 jpeg 파일 경로
	 * @return 성공 여부
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static boolean saveBMPToJPEG(InputStream imgInputStream, File outputFile) throws FileNotFoundException, IOException {
		BufferedImage sourceImage = null;
		Iterator bmpIter = ImageIO.getImageReadersByMIMEType("image/bmp");
		if ( bmpIter.hasNext()) {
			sourceImage = ImageIO.read(new BufferedInputStream(imgInputStream));
		} else {
			sourceImage = BMPDecoder.read(imgInputStream); 
		}
		
		boolean success = false;
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(outputFile));
			success = ImageIO.write(sourceImage, "jpeg", bos);
		} finally {
			try { bos.flush(); } catch (Exception e) {}
			try { bos.close(); } catch (Exception e) {}
		}
		return success;
	}
	
	/**
	 * 이미지를 jpeg 형태로 변환 (jdk 1.4 버전에서 bmp 이미지를 ImageIO로 읽어들이지 못하는 버그 수정0
	 * @param imgInputStream 원본 이미지 InputStream
	 * @param outputFile 변환될 jpeg 파일 경로
	 * @return 성공 여부
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public static boolean saveEMFToJPEG(InputStream imgInputStream, File outputFile) throws FileNotFoundException, IOException, InterruptedException {
		BufferedImage sourceImage = null;
		Iterator bmpIter = ImageIO.getImageReadersByMIMEType("image/bmp");
		if ( bmpIter.hasNext()) {
			sourceImage = ImageIO.read(new BufferedInputStream(imgInputStream));
		} else {
			sourceImage = BMPDecoder.read(imgInputStream); 
		}
		
		boolean success = false;
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(outputFile));
			success = ImageIO.write(sourceImage, "jpeg", bos);
		} finally {
			try { bos.flush(); } catch (Exception e) {}
			try { bos.close(); } catch (Exception e) {}
		}
		return success;
	}

	/**
	 * 썸네일 이미지 생성 - 세로 > 가로(증명사진과 같은) 이미지의 썸네일 처리
	 * @param inputFile
	 * @param outputFile
	 * @param width
	 * @param height
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static boolean createThumbnailImageBasedHeight(String inputFile, String outputFile, int width, int height) throws FileNotFoundException, IOException{
		BufferedImage sourceImage = ImageIO.read(new BufferedInputStream(new FileInputStream(inputFile)));
		// height만 주고 width를 주지 않으면 height에 맞게 width scale이 자동조정됨
		Image thumbnail = sourceImage.getScaledInstance(-1, height, Image.SCALE_SMOOTH);
		BufferedImage bufferedThumbnail = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = bufferedThumbnail.getGraphics();
		int thumbnailWidth = thumbnail.getWidth(null);
		// 1. bufferedThumbnail의 Graphics에 이미지를 drawing한 후, 여백이 검은색으로 채워지는 문제가 있음.
		// ->  남는 여백을 white로 채워야 함.
		// -> 이미지 사이즈를 계산하여 가로축인 x position을 동적 계산해야 함. 
		
		// 세로 비율이 더 큰 경우 x position 계산   
		int xPosition = 0;
		if ( thumbnailWidth < width ) {
			xPosition = (int)(width-thumbnailWidth)/2;
		}
		
		g.setColor(Color.WHITE);
		// BufferedImage에 축소된 이미지 drawing (이 때 x position은 가운데 위치하도록 함)
		g.drawImage(thumbnail, xPosition, 0, thumbnail.getWidth(null), thumbnail.getHeight(null), Color.WHITE, null);
		// 세로 비율이 더 큰 경우 남는 여백을 white로 채움
		if ( thumbnailWidth < width ) {
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, xPosition, height);
			g.fillRect(xPosition+thumbnailWidth, 0, (width-xPosition-thumbnailWidth), height);
		}
		boolean success = false;
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(outputFile));
			success = ImageIO.write(bufferedThumbnail, "jpeg", bos);
		} finally {
			try { bos.flush(); } catch (Exception e) {}
			try { bos.close(); } catch (Exception e) {}
		}
		return success;
	}

	/**
	 * 썸네일 이미지 생성 -> 메신저용 정방형 썸네일 이미지 생성
	 * @param inputFile
	 * @param outputFile
	 * @param length
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static boolean createThumbnailImageForMsn(String inputFile, String outputFile, int length) throws FileNotFoundException, IOException {
		BufferedImage sourceImage = ImageIO.read(new BufferedInputStream(new FileInputStream(inputFile)));
		if (sourceImage.getWidth()>=sourceImage.getHeight()) {
			return createThumbnailImage(inputFile, outputFile, length, length);
		} else {
			return createThumbnailImageBasedHeight(inputFile, outputFile, length, length);
		}
	}

	public static void main(String args[]) throws FileNotFoundException, IOException {
		// 87*87 썸네일 이미지 생성
//		System.out.println(new File("C:/f8a1541fac10df7de730bb2283f5f4ba_0.jpg").length());
//		System.out.println(new File("C:/summit_sub02.jpg").length());
//		File inputFile = new File("C:/it005.png");
//		BufferedImage sourceImage = ImageIO.read(new BufferedInputStream(new FileInputStream(inputFile)));
//		System.out.println("w:"+sourceImage.getWidth(null)+",h:"+sourceImage.getHeight(null));
		
		// iPhone (320*480) : 스마트폰의  경우 300을 넘지 않도록  이미지 생성 
		// iPad   (1024*768): 태블릿의 경우 600을 넘지 않도록 이미지 생성 (왼쪽 메뉴가 있으므로 이미지 축소)
		
		ImageUtils.createThumbnailImageForMsn("C:/200755616.jpg", "C:/thumb.jpg", 32);
	}

}
