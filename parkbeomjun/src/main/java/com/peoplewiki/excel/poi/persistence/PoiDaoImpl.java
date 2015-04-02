package com.peoplewiki.excel.poi.persistence;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.peoplewiki.excel.poi.domain.PoiBean;

@Repository("poiDao")
public class PoiDaoImpl implements PoiDao {

	@Autowired
	private SqlSession query;

	public void setQuery(SqlSession query) {
		this.query = query;
	}

	@Override
	public void insertForm1(PoiBean p) throws Exception {
		query.insert("excelPoi.insertForm1", p);
	}

	@Override
	public void insertForm2(PoiBean poiBean) throws Exception {
		query.insert("excelPoi.insertForm2", poiBean);
	}

	@Override
	public List<PoiBean> listForm1() throws Exception {
		return query.selectList("excelPoi.listForm1");
	}

	@Override
	public List<PoiBean> listForm2() throws Exception {
		return query.selectList("excelPoi.listForm2");
	}

	@Override
	public void delForm1() throws Exception {
		query.delete("excelPoi.delForm1");
	}

	@Override
	public void delForm2() throws Exception {
		query.delete("excelPoi.delForm2");
		
	}

}
