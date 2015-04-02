package com.peoplewiki.excel.poi.domain;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.peoplewiki.excel.common.domain.ObjectBean;

public class PoiBean extends ObjectBean {

	private List<MultipartFile> files;
	private MultipartFile file;
	private String a;
	private String b;
	private String c;
	private String d;
	private String e;
	private String f;
	private String g;
	private String h;
	private String i;
	private String flag;

	public PoiBean() {
	}

	public PoiBean(String a, String b, String c, String d, String e, String f, String g, String h, String i) {
		super();
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.e = e;
		this.f = f;
		this.g = g;
		this.h = h;
		this.i = i;
	}

	public PoiBean(String a, String b, String c) {
		super();
		this.a = a;
		this.b = b;
		this.c = c;
	}

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}

	public String getB() {
		return b;
	}

	public void setB(String b) {
		this.b = b;
	}

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public String getD() {
		return d;
	}

	public void setD(String d) {
		this.d = d;
	}

	public String getE() {
		return e;
	}

	public void setE(String e) {
		this.e = e;
	}

	public String getF() {
		return f;
	}

	public void setF(String f) {
		this.f = f;
	}

	public String getG() {
		return g;
	}

	public void setG(String g) {
		this.g = g;
	}

	public String getH() {
		return h;
	}

	public void setH(String h) {
		this.h = h;
	}

	public String getI() {
		return i;
	}

	public void setI(String i) {
		this.i = i;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public List<MultipartFile> getFiles() {
		return files;
	}

	public void setFiles(List<MultipartFile> files) {
		this.files = files;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

}
