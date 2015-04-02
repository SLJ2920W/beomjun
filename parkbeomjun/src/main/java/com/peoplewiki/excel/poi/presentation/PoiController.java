package com.peoplewiki.excel.poi.presentation;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.peoplewiki.excel.common.presentation.ObjectController;
import com.peoplewiki.excel.poi.domain.PoiBean;
import com.peoplewiki.excel.poi.service.PoiService;
import com.peoplewiki.excel.util.FileUpload;

@Controller
@RequestMapping("/excel/*")
public class PoiController extends ObjectController {

	@Autowired
	private PoiService poiService;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public void setpoiService(PoiService poiService) {
		this.poiService = poiService;
	}

	@RequestMapping(value = "/{path}", method = RequestMethod.GET)
	public String home(@PathVariable String path, ModelMap model) {
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/excel/home";
	}

	@RequestMapping(value = "/home/{path}", method = RequestMethod.GET)
	public String home(@PathVariable int path, ModelMap model) {
		try {
			if(path == 1)
				model.put("list", poiService.listForm1());
			else if(path == 2)
				model.put("list", poiService.listForm2());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/excel/home" + path;
	}

	@RequestMapping(value = "/home/up1", method = RequestMethod.POST)
	public String list(ModelMap model, PoiBean poiBean) {
		try {
			
			if(poiBean.getFlag().equals("1")){
				poiService.delForm1();
			}
			
			String root = "D:\\DEV\\tempFile\\parkbeomjun\\fileRoot";
			String folder = "excel";
			List<Map<String, Object>> list = FileUpload.fileUpload(poiBean, root, folder);

			Map<String, Object> map = list.get(0);
			String path = (String) map.get("organizedFilePath");

			poiService.insertForm1(path);
			model.put("list", poiService.listForm1());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/excel/home1";
	}

	@RequestMapping(value = "/home/up2", method = RequestMethod.POST)
	public String list2(ModelMap model, PoiBean poiBean) {
		try {
			if(poiBean.getFlag().equals("1")){
				poiService.delForm2();
			}
			
			String root = "D:\\DEV\\tempFile\\parkbeomjun\\fileRoot";
			String folder = "excel";
			List<Map<String, Object>> list = FileUpload.fileUpload(poiBean, root, folder);

			Map<String, Object> map = list.get(0);
			String path = (String) map.get("organizedFilePath");

			poiService.insertForm2(path);
			model.put("list", poiService.listForm2());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/excel/home2";
	}

}
