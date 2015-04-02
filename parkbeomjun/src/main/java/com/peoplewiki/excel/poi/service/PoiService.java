package com.peoplewiki.excel.poi.service;

import java.util.List;

import com.peoplewiki.excel.poi.domain.PoiBean;

public interface PoiService {

	void insertForm1(String path) throws Exception;

	void insertForm2(String path) throws Exception;

	List<PoiBean> listForm1() throws Exception;

	List<PoiBean> listForm2() throws Exception;

	void delForm1() throws Exception;

	void delForm2() throws Exception;

}
