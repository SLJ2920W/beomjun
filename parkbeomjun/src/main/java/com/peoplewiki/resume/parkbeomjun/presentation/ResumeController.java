package com.peoplewiki.resume.parkbeomjun.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.peoplewiki.resume.common.presentation.ObjectController;
import com.peoplewiki.resume.parkbeomjun.service.ResumeService;

@Controller
@RequestMapping("/resume/*")
public class ResumeController extends ObjectController {

	@Autowired
	private ResumeService resumeService;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public void setResumeService(ResumeService resumeService) {
		this.resumeService = resumeService;
	}


	@RequestMapping(value = "/{path}", method = RequestMethod.GET)
	public String home(@PathVariable String path, ModelMap model) {
		try {
			resumeService.test("홈");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/resume/home";
	}

	@RequestMapping(value = "/home/{list}", method = RequestMethod.GET)
	public String list(@PathVariable String list, ModelMap model) {
		try {
			resumeService.test("리스트");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/resume/list";
	}

}
