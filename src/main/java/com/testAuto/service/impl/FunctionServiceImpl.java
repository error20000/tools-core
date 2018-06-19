package com.testAuto.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.testAuto.entity.Function;
import com.testAuto.dao.FunctionDao;
import com.testAuto.service.FunctionService;

/**
 * @author liujian
 * @Date  
 */
@Service
public class FunctionServiceImpl extends BaseServiceImpl<Function> implements FunctionService {

	@Autowired
	private FunctionDao dao;
	
	@Override
	public void initDao() {
		super.baseDao = dao;
	}

}
