package com.peoplewiki.map.gmap.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.peoplewiki.map.common.presentation.ObjectController;
import com.peoplewiki.map.gmap.domain.GMapBean;
import com.peoplewiki.map.gmap.service.GMapService;

@Controller()
@RequestMapping("/gmap/*")
public class GMapController extends ObjectController {

//	@Value("#{prop['webRoot']}")
//	private String webRoot;

	@Autowired
	private GMapService gMapService;

	public void setgMapService(GMapService gMapService) {
		this.gMapService = gMapService;
	}

	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(value = "/{path}", method = RequestMethod.GET)
	public String home(@PathVariable String path, ModelMap model) {

		try {
//			gMapService.test(100, 0);
			System.out.println("gmap..");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "gmap.sub." + path;
	}
}
