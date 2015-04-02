package com.peoplewiki.common.handler;

import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

public class MappingUrlHandler {

	private RequestMappingHandlerMapping handlerMapping;

	public void setHandlerMapping(RequestMappingHandlerMapping handlerMapping) {
		this.handlerMapping = handlerMapping;
	}

	public void test() {
	}
}
