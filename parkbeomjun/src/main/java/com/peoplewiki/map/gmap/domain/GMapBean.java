package com.peoplewiki.map.gmap.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.peoplewiki.map.common.domain.ObjectBean;

@Scope("step")
@Component
public class GMapBean extends ObjectBean {
	@Value("#{prop['webRoot']}")
	private String webRoot;

	@Override
	public String toString() {
		return "GMapBean [webRoot=" + webRoot + "]";
	}

	@Autowired
	public GMapBean(@Value("#{prop['webRoot']}") String webRoot) {
		this.webRoot = webRoot;
	}

	public GMapBean() {
	}

}
