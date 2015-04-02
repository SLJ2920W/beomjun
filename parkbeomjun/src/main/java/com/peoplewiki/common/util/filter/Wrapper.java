package com.peoplewiki.common.util.filter;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class Wrapper extends HttpServletRequestWrapper {
	public Wrapper(HttpServletRequest servletRequest) {
		super(servletRequest);
	}

	public String[] getParameterValues(String parameter) {
		String[] values = super.getParameterValues(parameter);

		if (values == null) {
			return null;

		}

		int count = values.length;

		String[] encodedValues = new String[count];

		for (int i = 0; i < count; i++) {
			encodedValues[i] = cleanXSS(values[i]);
		}

		return encodedValues;
	}

	public String getParameter(String parameter) {

		String value = super.getParameter(parameter);
		if (value == null) {

			return null;

		}

		return cleanXSS(value);

	}

	public String getHeader(String name) {

		String value = super.getHeader(name);

		if (value == null) {
			return null;
		}

		return cleanXSS(value);
	}

	private String cleanXSS(String value) {
		UUID ran = UUID.randomUUID();

		value = value.replaceAll("\\<[\\w\\W]+\\>[\\w\\W]+\\<\\/[\\w\\W]+\\>", "특정 코드로 의심되어 치환되었습니다<br>(" + ran + ")");
		// value = value.replaceAll("<", "& lt;").replaceAll(">", "& gt;");
		// value = value.replaceAll("\\(", "& #40;").replaceAll("\\)",
		// "& #41;");
		// value = value.replaceAll("'", "& #39;");
		// value = value.replaceAll("eval\\((.*)\\)", "");
		// value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']",
		// "\"\"");
		return value;
	}
}
