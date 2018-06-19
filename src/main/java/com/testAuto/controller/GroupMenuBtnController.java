package com.testAuto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jian.annotation.API;

import com.testAuto.entity.GroupMenuBtn;
import com.testAuto.service.GroupMenuBtnService;

@Controller
@RequestMapping("/api/groupmenubtn")
@API(info="appId表", entity={GroupMenuBtn.class})
public class GroupMenuBtnController extends BaseController<GroupMenuBtn> {

	@Autowired
	private GroupMenuBtnService service;
	
	@Override
	public void initService() {
		super.service = service;
	}
	

	
	
}
