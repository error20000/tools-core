package com.testAuto.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.testAuto.entity.GroupLanguage;
import com.testAuto.dao.GroupLanguageDao;
import com.testAuto.service.GroupLanguageService;

/**
 * @author liujian
 * @Date  
 */
@Service
public class GroupLanguageServiceImpl extends BaseServiceImpl<GroupLanguage> implements GroupLanguageService {

	@Autowired
	private GroupLanguageDao dao;
	
	@Override
	public void initDao() {
		super.baseDao = dao;
	}

}
