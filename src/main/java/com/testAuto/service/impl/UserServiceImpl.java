package com.testAuto.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.testAuto.entity.User;
import com.testAuto.dao.UserDao;
import com.testAuto.service.UserService;

/**
 * @author liujian
 * @Date  
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

	@Autowired
	private UserDao dao;
	
	@Override
	public void initDao() {
		super.baseDao = dao;
	}

}
