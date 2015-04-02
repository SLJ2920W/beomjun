package com.peoplewiki.common.aop.domain;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;

import com.peoplewiki.common.util.ConfigOptions;

public class AopBean extends ObjectBean {
	// 메일 받는 사람 다수 가능 "," 표로 구분
	private String mailSendAddress = ConfigOptions.getOption("exception.mail.send.address");
	// 메일 보내는 사람
	private String[] mailReceiveAddress = ConfigOptions.getOption("exception.mail.receive.address").split(",");
	// 메일 제목
	private String mailTitle = ConfigOptions.getOption("exception.mail.form.title");
	// 메일 내용 (벨로시티에서는 사용 안함)
	private String mailBody;
	// 메일 폼 경로 (벨로시티폼 사용시만 사용)
	private String mailFormPath = ConfigOptions.getOption("exception.mail.form.path");

	public AopBean() {
	}

	public String getMailSendAddress() {
		return mailSendAddress;
	}

	public void setMailSendAddress(String mailSendAddress) {
		this.mailSendAddress = mailSendAddress;
	}

	public String[] getMailReceiveAddress() {
		return mailReceiveAddress;
	}

	public void setMailReceiveAddress(String[] mailReceiveAddress) {
		this.mailReceiveAddress = mailReceiveAddress;
	}

	public String getMailTitle() {
		return mailTitle;
	}

	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}

	public String getMailBody() {
		return mailBody;
	}

	public void setMailBody(JoinPoint joinPoint, Exception e) {
		String className = joinPoint.getTarget().getClass().toString();
		String arguments = Arrays.toString(joinPoint.getArgs());
		Signature signature = joinPoint.getSignature();
		String methodName = signature.getName();
		StringBuilder sb = new StringBuilder();
		String newLine = "<br/>";
		String now = DateFormat.getDateTimeInstance().format(new Date());
		sb.append("### 에러 정보 ###");
		sb.append(newLine);
		sb.append("발생일 : ");
		sb.append(now);
		sb.append(newLine);
		sb.append("에러 위치 1 : ");
		sb.append(joinPoint.toString());
		sb.append(newLine);
		sb.append("에러 위치 2 : ");
		sb.append(className);
		sb.append(newLine);
		sb.append("에러 메소드 : ");
		sb.append(methodName);
		sb.append(newLine);
		sb.append("파라미터 : ");
		sb.append(arguments);
		sb.append(newLine);
		sb.append("상세내용 : ");
		sb.append(e.toString());
		sb.append(newLine);

		this.mailBody = sb.toString();
	}

	public String getMailFormPath() {
		return mailFormPath;
	}

	public void setMailFormPath(String mailFormPath) {
		this.mailFormPath = mailFormPath;
	}

}
