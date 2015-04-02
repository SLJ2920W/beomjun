package com.peoplewiki.common.util;

import java.util.HashMap;

@SuppressWarnings("serial")
public class ConfigOptions extends HashMap<String, String> {

	/**
	 * web.xml에 주소 설정 WAS 올라올때 InitWebApp.java 설정값 세팅함
	 */
	private static HashMap<String, String> INIT_WEB = new HashMap<String, String>();

	public static void setConfigOptions(HashMap<String, String> param) {
		INIT_WEB = param;
	}

	public static String getOption(String param) {

		return INIT_WEB.get(param);

	}

	public static void setOption(String key, String value) {
		INIT_WEB.put(key, value);
	}

}
