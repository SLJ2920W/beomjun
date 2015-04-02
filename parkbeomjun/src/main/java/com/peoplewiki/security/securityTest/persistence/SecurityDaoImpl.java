package com.peoplewiki.security.securityTest.persistence;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.peoplewiki.security.securityTest.domain.SecurityBean;

@Repository("sercurityDao")
public class SecurityDaoImpl implements SecurityDao {

	@Autowired
	private SqlSession query;

	public void setQuery(SqlSession query) {
		this.query = query;
	}

	// URL 리스트
	@Override
	public List<SecurityBean> urlList(int depth) throws Exception {
		return query.selectList("security.urlList", depth);
	}

	// 권한 URL 리스트
	@Override
	public List<SecurityBean> authUrlList(SecurityBean securityBean) throws Exception {
		return query.selectList("security.authUrlList", securityBean);
	}

	// URL 단계 표시
	@Override
	public int urlMaxDepth() throws Exception {
		return query.selectOne("security.urlMaxDepth");
	}

	// 권한 URL 보기
	@Override
	public SecurityBean authUrlView(SecurityBean securityBean) throws Exception {
		return query.selectOne("security.authUrlView", securityBean);
	}

	// 권한 리스트
	@Override
	public List<SecurityBean> authList() throws Exception {
		return query.selectList("security.authList");
	}

	// 권한 URL 삭제
	@Override
	public void authDelete(SecurityBean securityBean) throws Exception {
		query.delete("security.authDelete", securityBean);
	}

	// 권한 URL 설정 추가
	@Override
	public void authInsert(SecurityBean securityBean) throws Exception {
		query.insert("security.authInsert", securityBean);
	}

	// URL 수정
	@Override
	public void urlUpdate(SecurityBean securityBean) throws Exception {
		query.update("security.urlUpdate", securityBean);
	}

	// 하위 모든 권한 URL 리스트
	@Override
	public List<SecurityBean> authUrlListChildren(SecurityBean securityBean) throws Exception {
		return query.selectList("security.authUrlListChildren", securityBean);
	}

	// URL 추가
	@Override
	public void urlInsert(SecurityBean securityBean) throws Exception {
		query.insert("security.urlInsert", securityBean);
	}

	// URL 마지막 resources_id 겟
	@Override
	public SecurityBean urlMaxResourceId(SecurityBean securityBean) throws Exception {
		return query.selectOne("security.urlMaxResourceId", securityBean);
	}

	// 하위 모든 URL 수정 
	@Override
	public void urlUpdateChildren(SecurityBean securityBean) throws Exception {
		query.update("security.urlUpdateChildren", securityBean);
	}

}
