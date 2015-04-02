package com.peoplewiki.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

public class InitWeb extends javax.servlet.http.HttpServlet {

	private static final long serialVersionUID = 1L;

	private String getRealPath(ServletConfig config, String name) {
		return config.getServletContext().getRealPath(config.getInitParameter(name));
	}

	// 초기 설정값 WAS로딩시 프로퍼티에 잇는 정보를 읽어 들인다 필요하면 사용
	public void init(ServletConfig config) throws ServletException {
		try {
			String sUrl = getRealPath(config, "configOption");
			Properties props = new Properties();
			props.load(new InputStreamReader(new FileInputStream(sUrl), "UTF-8"));

			HashMap<String, String> configMap = new HashMap<String, String>();
			Enumeration<Object> en = props.keys();
			while (en.hasMoreElements()) {
				String key = (String) en.nextElement();
				configMap.put(key, props.getProperty(key));
			}

			String rootPath = config.getServletContext().getRealPath("/");
			configMap.put("rootPath", rootPath);

			ConfigOptions.setConfigOptions(configMap);

		} catch (Exception e) {
			e.printStackTrace();
		}


		// 웹루트외 장소에 저장해놓은 파일들을 웹루트 밑으로 복사함
		String isFileBackupCopy = ConfigOptions.getOption("file.is.backup.copy");
		if (isFileBackupCopy.equals("true")) {
			String bakFolder = ConfigOptions.getOption("file.backup.path.name");

			String webRoot = config.getServletContext().getRealPath("/");
			String webFolder = ConfigOptions.getOption("file.upload.folder.name");

			webRoot = webRoot + File.separator + webFolder;

			File srcFolder = new File(bakFolder);
			File destFolder = new File(webRoot);
			try {
				if (!destFolder.isDirectory()) {
					copyFolder(srcFolder, destFolder);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static void copyFolder(File src, File dest) throws IOException {
		if (src.isDirectory()) {

			if (!dest.exists()) {
				dest.mkdir();
				System.out.println("Directory copied from " + src + "  to " + dest);
			}

			String files[] = src.list();

			for (String file : files) {
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				copyFolder(srcFile, destFile);
			}

		} else {
			FileInputStream in = new FileInputStream(src);
			FileOutputStream out = new FileOutputStream(dest);
			FileChannel fcin = null;
			FileChannel fcout = null;

			// 채널 생성
			fcin = in.getChannel();
			fcout = out.getChannel();

			// 채널을 통한 스트림 전송
			// long size = fcin.size();
			// fcin.transferTo(0, size, fcout);
			fcout.transferFrom(fcin, 0, Long.MAX_VALUE);

			fcout.close();
			fcin.close();
			in.close();
			out.close();
			System.out.println("File copied from " + src + " to " + dest);
		}
	}

}
