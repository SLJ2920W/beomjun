package com.peoplewiki.resume.parkbeomjun.persistence;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.peoplewiki.resume.parkbeomjun.domain.ResumeBean;

@Repository("resumeDao")
public class ResumeDaoImpl implements ResumeDao {

	@Autowired
	private SqlSession query;

	public void setQuery(SqlSession query) {
		this.query = query;
	}

	@Override
	public void test(String msg) throws Exception {
		ResumeBean test = (ResumeBean) query.selectOne("resumeParkbeomjun.test");
	}

}
