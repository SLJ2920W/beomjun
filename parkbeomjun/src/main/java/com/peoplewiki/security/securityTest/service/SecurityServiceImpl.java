package com.peoplewiki.security.securityTest.service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peoplewiki.security.securityTest.domain.SecurityBean;
import com.peoplewiki.security.securityTest.persistence.SecurityDao;

@Service("sercurityService")
public class SecurityServiceImpl implements SecurityService {

	@Autowired
	private SecurityDao securityDao;

	public void setSecurityDao(SecurityDao securityDao) {
		this.securityDao = securityDao;
	}

	// URL 리스트
	@Override
	public Map<String, Object> urlList() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// URL 단계 표시
		int depth = securityDao.urlMaxDepth();

		for (int i = 1; i <= depth; i++) {
			map.put("urlList_" + String.valueOf(i), securityDao.urlList(i));
		}

		map.put("maxDepth", depth);

		return map;
	}

	// 권한 URL 리스트
	@Override
	public List<SecurityBean> authUrlList(SecurityBean securityBean) throws Exception {
		return securityDao.authUrlList(securityBean);
	}

	// 권한 URL 보기
	@Override
	public SecurityBean authUrlView(SecurityBean securityBean) throws Exception {
		return securityDao.authUrlView(securityBean);
	}

	// 권한 리스트
	@Override
	public List<SecurityBean> authList() throws Exception {
		return securityDao.authList();
	}

	// 권한 URL 수정
	@Override
	public void authUrlUpdate(SecurityBean s) throws Exception {

		if (s.getAuthority_children_apply() != null) {
			// 하위 모든 권한 URL 리스트
			List<SecurityBean> list = securityDao.authUrlListChildren(s);
			for (SecurityBean sb : list) {
				securityDao.authDelete(sb);
			}
			for (SecurityBean sb : list) {
				s.setResource_id(sb.getResource_id());
				securityDao.authInsert(s);
			}

			// 하위 모든 URL 수정
			securityDao.urlUpdateChildren(s);
		} else {
			// 하나의 권한 적용
			securityDao.authDelete(s);
			securityDao.authInsert(s);

			// URL 수정
			securityDao.urlUpdate(s);
		}

	}

	// 권한 URL 수정
	@Override
	public void urlUpdate(SecurityBean securityBean) throws Exception {
		securityDao.urlUpdate(securityBean);
	}

	// URL 추가
	@Override
	public void urlInsert(SecurityBean securityBean) throws Exception {

		String resource_id = securityDao.urlMaxResourceId(securityBean).getResource_id();

		String strNumber = resource_id.split("-")[1];
		int intNumber = Integer.parseInt(strNumber) + 1;
		DecimalFormat d = new DecimalFormat("000000");
		resource_id = resource_id.split("-")[0] + "-" + d.format(intNumber);
		securityBean.setResource_id(resource_id);

		// URL 추가
		securityDao.urlInsert(securityBean);

	}

}