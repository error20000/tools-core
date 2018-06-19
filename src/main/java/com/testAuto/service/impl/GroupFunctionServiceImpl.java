package com.testAuto.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.testAuto.entity.GroupFunction;
import com.testAuto.dao.GroupFunctionDao;
import com.testAuto.service.GroupFunctionService;

/**
 * @author liujian
 * @Date  
 */
@Service
public class GroupFunctionServiceImpl extends BaseServiceImpl<GroupFunction> implements GroupFunctionService {

	@Autowired
	private GroupFunctionDao dao;
	
	@Override
	public void initDao() {
		super.baseDao = dao;
	}

}
