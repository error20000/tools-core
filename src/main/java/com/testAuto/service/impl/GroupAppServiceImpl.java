package com.testAuto.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.testAuto.entity.GroupApp;
import com.testAuto.dao.GroupAppDao;
import com.testAuto.service.GroupAppService;

/**
 * @author liujian
 * @Date  
 */
@Service
public class GroupAppServiceImpl extends BaseServiceImpl<GroupApp> implements GroupAppService {

	@Autowired
	private GroupAppDao dao;
	
	@Override
	public void initDao() {
		super.baseDao = dao;
	}

}
