package com.testAuto.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.testAuto.entity.GroupMenu;
import com.testAuto.dao.GroupMenuDao;
import com.testAuto.service.GroupMenuService;

/**
 * @author liujian
 * @Date  
 */
@Service
public class GroupMenuServiceImpl extends BaseServiceImpl<GroupMenu> implements GroupMenuService {

	@Autowired
	private GroupMenuDao dao;
	
	@Override
	public void initDao() {
		super.baseDao = dao;
	}

}
