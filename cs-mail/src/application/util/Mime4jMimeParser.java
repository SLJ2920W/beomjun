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
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.parser.MimeStreamParser;

/**
 * @author : parksungsoo
 * 
 */
public class Mime4jMimeParser implements MimeParser {
	
	private boolean isTextToHtml = true;
	private boolean saveImage;
	private File imageDirectory;
	private boolean convertBMP2JPEG;
	private CID cidConvertor;
	
	private InputStream mimeInputStream;
	
	private MimeHandler handler;
	
	
	public Mime4jMimeParser(InputStream mimeInputStream) {
		this.mimeInputStream = mimeInputStream;
	}
	

	/* 
	 * @see hanwha.neo.commons.mime.decoder.MimeParser#parse(boolean, hanwha.neo.commons.mime.decoder.CID)
	 */
	@Override
	public void parse(boolean isTextToHtml, CID cidConvertor) {
		parse(isTextToHtml, null, null);
	}

	/* 
	 * @see hanwha.neo.commons.mime.decoder.MimeParser#parse(boolean, java.io.File, hanwha.neo.commons.mime.decoder.CID)
	 */
	@Override
	public void parse(boolean isTextToHtml, File imageDirectory,
			CID cidConvertor) {
		parse(isTextToHtml, imageDirectory, cidConvertor, false);
	}

	/* 
	 * @see hanwha.neo.commons.mime.decoder.MimeParser#parse(boolean, java.io.File, hanwha.neo.commons.mime.decoder.CID, boolean)
	 */
	@Override
	public void parse(boolean isTextToHtml, File imageDirectory, CID cidConvertor, boolean convertBMP2JPEG) {
		this.isTextToHtml = isTextToHtml;
		this.saveImage = (imageDirectory != null);
		this.imageDirectory = imageDirectory;
		this.cidConvertor = cidConvertor; 
		this.convertBMP2JPEG = convertBMP2JPEG;
		try {
			process();
		} catch (MimeException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void process() throws MimeException, IOException {
		MimeStreamParser parser = new MimeStreamParser();
        
        handler = new MimeHandler(imageDirectory);
        handler.setCidConvertor(cidConvertor);
        parser.setContentHandler(handler);
        
        parser.parse(mimeInputStream);
	}

	/* 
	 * @see hanwha.neo.commons.mime.decoder.MimeParser#write(hanwha.neo.commons.mime.decoder.MimeOutputter)
	 */
	@Override
	public void write(MimeOutputter partOutputter) throws Exception {
		partOutputter.write( handler.toString(), null);
	}


	/* 
	 * @see hanwha.neo.commons.mime.decoder.MimeParser#getImages()
	 */
	@Override
	public Map getImages() {
		return handler.getImages();
	}


}
