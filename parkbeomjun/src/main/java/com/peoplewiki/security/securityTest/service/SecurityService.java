package com.peoplewiki.security.securityTest.service;

import java.util.List;
import java.util.Map;

import com.peoplewiki.security.securityTest.domain.SecurityBean;

public interface SecurityService {

	// URL 리스트
	Map<String, Object> urlList() throws Exception;

	// 권한 URL 리스트
	List<SecurityBean> authUrlList(SecurityBean securityBean) throws Exception;

	// 권한 URL 보기
	SecurityBean authUrlView(SecurityBean securityBean) throws Exception;

	// 권한 URL 수정
	void authUrlUpdate(SecurityBean securityBean) throws Exception;

	// 권한 URL 수정
	void urlUpdate(SecurityBean securityBean) throws Exception;

	// URL 추가
	void urlInsert(SecurityBean securityBean) throws Exception;

	// 권한 리스트
	List<SecurityBean> authList() throws Exception;

}
