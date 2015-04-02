package com.peoplewiki.common.aop.persistence;

import org.apache.ibatis.session.SqlSession;

import com.peoplewiki.common.aop.domain.AopBean;

public class AopDaoImpl implements AopDao {

	private SqlSession query;

	public void setSqlSession(SqlSession query) {
		this.query = query;
	}

	@Override
	public void test() throws Exception {
		int result = query.selectOne("commonAop.testQuery");

	}

	@Override
	public void test(String test) throws Exception {
		// query.selectList("");

	}

}
