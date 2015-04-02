package com.peoplewiki.excel.poi.persistence;

import java.util.List;

import com.peoplewiki.excel.poi.domain.PoiBean;

public interface PoiDao {

	void insertForm1(PoiBean p) throws Exception;

	void insertForm2(PoiBean poiBean) throws Exception;
	
	List<PoiBean> listForm1() throws Exception;
	
	List<PoiBean> listForm2() throws Exception;
	
	void delForm1() throws Exception;

	void delForm2() throws Exception;
	
	
}
