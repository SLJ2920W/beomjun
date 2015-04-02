package com.peoplewiki.security.securityTest.persistence;

import java.util.List;

import com.peoplewiki.security.securityTest.domain.SecurityBean;

public interface SecurityDao {

	// URL 리스트
	List<SecurityBean> urlList(int depth) throws Exception;

	// 권한 URL 리스트
	List<SecurityBean> authUrlList(SecurityBean securityBean) throws Exception;

	// URL 단계 표시
	int urlMaxDepth() throws Exception;

	// 권한 URL 보기
	SecurityBean authUrlView(SecurityBean securityBean) throws Exception;

	// 권한 리스트
	List<SecurityBean> authList() throws Exception;

	// 권한 URL 삭제
	void authDelete(SecurityBean securityBean) throws Exception;

	// 권한 URL 설정 추가
	void authInsert(SecurityBean securityBean) throws Exception;

	// URL 수정
	void urlUpdate(SecurityBean securityBean) throws Exception;

	// 하위 모든 권한 URL 리스트
	List<SecurityBean> authUrlListChildren(SecurityBean securityBean) throws Exception;
	
	// URL 추가
	void urlInsert(SecurityBean securityBean) throws Exception;
	
	// URL 마지막 resources_id 겟
	SecurityBean urlMaxResourceId(SecurityBean securityBean) throws Exception;
	
	// 하위 모든 URL 수정 
	void urlUpdateChildren(SecurityBean securityBean) throws Exception;
	
	

}
