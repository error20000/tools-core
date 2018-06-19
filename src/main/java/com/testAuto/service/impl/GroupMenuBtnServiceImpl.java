package com.testAuto.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.testAuto.entity.GroupMenuBtn;
import com.testAuto.dao.GroupMenuBtnDao;
import com.testAuto.service.GroupMenuBtnService;

/**
 * @author liujian
 * @Date  
 */
@Service
public class GroupMenuBtnServiceImpl extends BaseServiceImpl<GroupMenuBtn> implements GroupMenuBtnService {

	@Autowired
	private GroupMenuBtnDao dao;
	
	@Override
	public void initDao() {
		super.baseDao = dao;
	}

}
