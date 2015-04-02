package com.peoplewiki.common.aop.service;

import java.util.Map;

import org.aspectj.lang.JoinPoint;

import com.peoplewiki.common.aop.domain.AopBean;

public interface AopService {

	// 테스트
	void test() throws Exception;

	// 로그 발생시 파일 저장
	void logExceptionFile(JoinPoint joinPoint, Exception e) throws Exception;

	// 로그 발생시 메일 발송_일반 폼
	void logExceptionMail(AopBean aopBean, JoinPoint joinPoint, Exception e) throws Exception;

	// 로그 발생시 메일 발송_벨로시티 폼
	void logExceptionMail(AopBean aopBean, Map<String, Object> map) throws Exception;
}
