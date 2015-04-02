package com.peoplewiki.security.securityTest.domain;

import java.util.Arrays;

import com.peoplewiki.security.common.domain.ObjectBean;

public class SecurityBean extends ObjectBean {

	// 테이블 PK
	private int idx;

	// URL 구분 카테고리
	private String resource_id;

	// URL 설명
	private String resource_name;

	// URL 주소(ANT패턴)
	private String resource_pattern;

	// 리소스 타입(URL만 쓰기 때문에 큰 의미 없음)
	private String resource_type;

	// ANT패턴의 속할 경우 권한 순서를 정하게 됨 (/security/main/** 와 /security/main/privatePage
	// 일경우 먼저 적용할 것을 분류함)
	private int sort_order;

	// URL 트리 구조로 보여주기 위해서
	private String sort_group;

	// URL 트리 단계 구분값
	private String sort_depth;

	// URL 트리 그룹값
	private String sort_parent;

	// URL 사용 여부
	private String useYN;

	// 해당 URL의 메뉴 표시 유무
	private String menuYN;

	// 권한 키값
	private String authority;

	// 권한 키값 배열
	private String[] authority_arr;

	// 권한 구분 변수
	private String authority_children_apply;

	// 권한 설명
	private String authority_name;

	public String getSort_group() {
		return sort_group;
	}

	public void setSort_group(String sort_group) {
		this.sort_group = sort_group;
	}

	public String getSort_depth() {
		return sort_depth;
	}

	public void setSort_depth(String sort_depth) {
		this.sort_depth = sort_depth;
	}

	public String getUseYN() {
		return useYN == null ? "N" : useYN;
	}

	public void setUseYN(String useYN) {
		this.useYN = useYN;
	}

	public String getMenuYN() {
		return menuYN == null ? "N" : menuYN;
	}

	public void setMenuYN(String menuYN) {
		this.menuYN = menuYN;
	}

	public String getAuthority_name() {
		return authority_name;
	}

	public void setAuthority_name(String authority_name) {
		this.authority_name = authority_name;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public String getResource_id() {
		return resource_id;
	}

	public void setResource_id(String resource_id) {
		this.resource_id = resource_id;
	}

	public String getResource_name() {
		return resource_name;
	}

	public void setResource_name(String resource_name) {
		this.resource_name = resource_name;
	}

	public String getResource_pattern() {
		return resource_pattern;
	}

	public void setResource_pattern(String resource_pattern) {
		this.resource_pattern = resource_pattern;
	}

	public String getResource_type() {
		return resource_type;
	}

	public void setResource_type(String resource_type) {
		this.resource_type = resource_type;
	}

	public int getSort_order() {
		return sort_order;
	}

	public void setSort_order(int sort_order) {
		this.sort_order = sort_order;
	}

	public String[] getAuthority_arr() {
		return authority_arr;
	}

	public void setAuthority_arr(String[] authority_arr) {
		this.authority_arr = authority_arr;
	}

	public String getAuthority_children_apply() {
		return authority_children_apply;
	}

	public void setAuthority_children_apply(String authority_children_apply) {
		this.authority_children_apply = authority_children_apply;
	}

	public String getSort_parent() {
		return sort_parent;
	}

	public void setSort_parent(String sort_parent) {
		this.sort_parent = sort_parent;
	}

	@Override
	public String toString() {
		return "SecurityBean [idx=" + idx + ", resource_id=" + resource_id + ", resource_name=" + resource_name + ", resource_pattern="
				+ resource_pattern + ", resource_type=" + resource_type + ", sort_order=" + sort_order + ", sort_group=" + sort_group
				+ ", sort_depth=" + sort_depth + ", sort_parent=" + sort_parent + ", useYN=" + useYN + ", menuYN=" + menuYN
				+ ", authority=" + authority + ", authority_arr=" + Arrays.toString(authority_arr) + ", authority_children_apply="
				+ authority_children_apply + ", authority_name=" + authority_name + "]";
	}

}
