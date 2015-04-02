package com.peoplewiki.security.common.presentation;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

public class ObjectController {

	@Autowired
	protected ServletContext servletContext;
	@Autowired
	protected HttpServletRequest request;
	@Autowired
	protected HttpServletResponse response;

	protected String contextPath;

	public String getContextPath() {
		return request.getContextPath();
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

}
