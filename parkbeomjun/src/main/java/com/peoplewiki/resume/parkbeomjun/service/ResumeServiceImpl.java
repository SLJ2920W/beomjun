package com.peoplewiki.resume.parkbeomjun.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peoplewiki.resume.parkbeomjun.persistence.ResumeDao;

@Service("resumeService")
public class ResumeServiceImpl implements ResumeService {

	@Autowired
	private ResumeDao resumeDao;

	public void setResumeDao(ResumeDao resumeDao) {
		this.resumeDao = resumeDao;
	}

	@Override
	public void test(String msg) throws Exception {
		resumeDao.test("test");
	}

}
