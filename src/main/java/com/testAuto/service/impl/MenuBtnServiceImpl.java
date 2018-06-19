package com.testAuto.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.testAuto.entity.MenuBtn;
import com.testAuto.dao.MenuBtnDao;
import com.testAuto.service.MenuBtnService;

/**
 * @author liujian
 * @Date  
 */
@Service
public class MenuBtnServiceImpl extends BaseServiceImpl<MenuBtn> implements MenuBtnService {

	@Autowired
	private MenuBtnDao dao;
	
	@Override
	public void initDao() {
		super.baseDao = dao;
	}

}
