package com.peoplewiki.common.aop.service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.apache.velocity.app.VelocityEngine;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.peoplewiki.common.aop.domain.AopBean;
import com.peoplewiki.common.aop.persistence.AopDao;
import com.peoplewiki.common.util.ConfigOptions;

public class AopServiceImpl implements AopService {

	private AopDao aopDao;
	private JavaMailSender mailSender;
	private VelocityEngine velocityEngine;

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setAopDao(AopDao aopDao) {
		this.aopDao = aopDao;
	}

	@Override
	public void test() throws Exception {
		aopDao.test();
	}

	// 로그 파일 저장
	@Override
	public void logExceptionFile(JoinPoint joinPoint, Exception e) throws Exception {
		msgWrite(joinPoint, e);
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String path = ConfigOptions.getOption("exception.file.path");
		String folder = ConfigOptions.getOption("exception.file.folder");
		path = path + File.separator + folder;

		StringBuilder fileName = new StringBuilder();
		fileName.append(sdf.format(c.getTime()));
		fileName.append(".txt");

		String msg = msgWrite(joinPoint, e);

		try {
			File f = new File(path);
			if (!f.exists()) {
				f.mkdir();
			}

			String file = path + File.separator + fileName.toString();

			f = new File(file);
			if (f.exists())
				msgUpdate(msg, file);
			else
				fileWrite(msg, file);
		} catch (IOException ie) {
			ie.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// 로그 내용 생성
	public String msgWrite(JoinPoint joinPoint, Exception e) throws IOException {
		// Class clazz = joinPoint.getTarget().getClass();
		String className = joinPoint.getTarget().getClass().toString();
		String arguments = Arrays.toString(joinPoint.getArgs());
		Signature signature = joinPoint.getSignature();
		String methodName = signature.getName();
		// String stuff = signature.toString();
		String newLine = System.getProperty("line.separator");
		StringBuilder sb = new StringBuilder();
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

		return sb.toString();

	}

	// 로그 파일 만들기
	public void fileWrite(String msg, String file) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		FileChannel fc = raf.getChannel();

		String message = msg;

		// 쓰기 버퍼
		ByteBuffer writeByteBuffer = ByteBuffer.allocate(message.getBytes().length);
		writeByteBuffer.put(message.getBytes());
		writeByteBuffer.flip();
		fc.write(writeByteBuffer);
		fc.close();
		raf.close();

	}

	// 로그 파일 내용 추가하기
	public void msgUpdate(String msg, String file) throws Exception {
		File f = new File(file);
		long fileLength = f.length();
		RandomAccessFile raf = new RandomAccessFile(f, "rw");
		raf.seek(fileLength);
		raf.write(System.getProperty("line.separator").getBytes());
		raf.write(System.getProperty("line.separator").getBytes());
		raf.write(msg.getBytes());
		raf.close();
	}

	// 구글 메일을 이용한 일반 메일 보내기
	@Override
	public void logExceptionMail(AopBean aopBean, JoinPoint joinPoint, Exception e) throws Exception {

		MimeMessage message = mailSender.createMimeMessage();

		StringBuilder sb = new StringBuilder();
		sb.append("<br/><br/><br/> from.");
		sb.append(aopBean.getMailSendAddress());

		message.setSubject(aopBean.getMailTitle(), "UTF-8");

		// HTML문서 형식으로 전송
		aopBean.setMailBody(joinPoint, e);
		String htmlContent = aopBean.getMailBody() + sb.toString();
		message.setText(htmlContent, "UTF-8", "html");

		InternetAddress[] addressTo = new InternetAddress[aopBean.getMailReceiveAddress().length];
		for (int i = 0; i < aopBean.getMailReceiveAddress().length; i++) {
			addressTo[i] = new InternetAddress(aopBean.getMailReceiveAddress()[i]);
		}
		message.setRecipients(RecipientType.TO, addressTo);
		mailSender.send(message);

	}

	// 구글 메일을 이용한 벨로시티 메일 보내기
	@Override
	public void logExceptionMail(AopBean aopBean, Map<String, Object> map) throws Exception {
		MimeMessage message = mailSender.createMimeMessage();

		StringBuilder sb = new StringBuilder();
		sb.append("<br/><br/><br/> from.");
		sb.append(aopBean.getMailSendAddress());

		message.setSubject(aopBean.getMailTitle(), "UTF-8");

		// 벨로 시티 폼으로 전송
		String body = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, aopBean.getMailFormPath(), "UTF-8", map);
		message.setText(body, "UTF-8", "html");

		InternetAddress[] addressTo = new InternetAddress[aopBean.getMailReceiveAddress().length];
		for (int i = 0; i < aopBean.getMailReceiveAddress().length; i++) {
			addressTo[i] = new InternetAddress(aopBean.getMailReceiveAddress()[i]);
		}
		message.setRecipients(RecipientType.TO, addressTo);
		mailSender.send(message);
	}

}
