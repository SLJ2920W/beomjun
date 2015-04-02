package com.peoplewiki.common.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error/*")
public class ErrorController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	// 에러 페이지
	@RequestMapping(value = "/{path}")
	public String home(@PathVariable String path, ModelMap model) {
		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();
		logger.info("Method Name = {}", methodName);
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "error." + path;
	}

}
