package com.peoplewiki.common.aop.presentation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.peoplewiki.common.aop.domain.AopBean;
import com.peoplewiki.common.aop.service.AopService;
import com.peoplewiki.common.util.ConfigOptions;

/**
 * @see 
 * 	{@link http://www.egovframe.go.kr/wiki/doku.php?id=egovframework:rte:fdl:aop:aspectj }
 *  {@link https://www.google.co.kr/?gws_rd=ssl#newwindow=1&q=aspectj+%ED%91%9C%ED%98%84%EC%8B%9D }
 *  {@link http://knoc720.blogspot.kr/2014/10/spring-aop.html }
 * @author Administrator
 *
 */
@Component
@Aspect
@Order(500)
public class AopLogging {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	protected ApplicationContext context1 = new ClassPathXmlApplicationContext(new String[] { "aopContext.xml" });
	protected AopService aopService = (AopService) context1.getBean("aopService");

	@Before("execution(* com.peoplewiki.*.*.service.*.*(..))")
	public void logBefore(JoinPoint joinPoint) {

		try {
			aopService.test();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@AfterThrowing(pointcut = "execution(* com.peoplewiki..*.*(..))", throwing = "e")
	public void logException(JoinPoint joinPoint, Exception e) {
		try {
			if(ConfigOptions.getOption("exception.is.file.send").equals("true"))
				aopService.logExceptionFile(joinPoint, e);
			if(ConfigOptions.getOption("exception.is.mail.send").equals("true"))
				aopService.logExceptionMail(new AopBean(), joinPoint, e);
			
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}
}
