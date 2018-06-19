package com.testAuto.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.testAuto.entity.Language;
import com.testAuto.dao.LanguageDao;
import com.testAuto.service.LanguageService;

/**
 * @author liujian
 * @Date  
 */
@Service
public class LanguageServiceImpl extends BaseServiceImpl<Language> implements LanguageService {

	@Autowired
	private LanguageDao dao;
	
	@Override
	public void initDao() {
		super.baseDao = dao;
	}

}
