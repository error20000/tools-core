package com.testAuto.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.testAuto.entity.Menu;
import com.testAuto.dao.MenuDao;
import com.testAuto.service.MenuService;

/**
 * @author liujian
 * @Date  
 */
@Service
public class MenuServiceImpl extends BaseServiceImpl<Menu> implements MenuService {

	@Autowired
	private MenuDao dao;
	
	@Override
	public void initDao() {
		super.baseDao = dao;
	}

}
