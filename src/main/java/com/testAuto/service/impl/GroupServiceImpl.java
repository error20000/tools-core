package com.testAuto.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.testAuto.entity.Group;
import com.testAuto.dao.GroupDao;
import com.testAuto.service.GroupService;

/**
 * @author liujian
 * @Date  
 */
@Service
public class GroupServiceImpl extends BaseServiceImpl<Group> implements GroupService {

	@Autowired
	private GroupDao dao;
	
	@Override
	public void initDao() {
		super.baseDao = dao;
	}

}
