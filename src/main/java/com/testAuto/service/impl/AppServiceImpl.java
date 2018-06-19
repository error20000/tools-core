package com.testAuto.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.testAuto.entity.App;
import com.testAuto.dao.AppDao;
import com.testAuto.service.AppService;

/**
 * @author liujian
 * @Date  
 */
@Service
public class AppServiceImpl extends BaseServiceImpl<App> implements AppService {

	@Autowired
	private AppDao dao;
	
	@Override
	public void initDao() {
		super.baseDao = dao;
	}

}
