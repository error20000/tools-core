package com.testAuto.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.testAuto.entity.UserGroup;
import com.testAuto.dao.UserGroupDao;
import com.testAuto.service.UserGroupService;

/**
 * @author liujian
 * @Date  
 */
@Service
public class UserGroupServiceImpl extends BaseServiceImpl<UserGroup> implements UserGroupService {

	@Autowired
	private UserGroupDao dao;
	
	@Override
	public void initDao() {
		super.baseDao = dao;
	}

}
