package com.testAuto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jian.annotation.API;

import com.testAuto.entity.GroupMenu;
import com.testAuto.service.GroupMenuService;

@Controller
@RequestMapping("/api/groupmenu")
@API(info="appId表", entity={GroupMenu.class})
public class GroupMenuController extends BaseController<GroupMenu> {

	@Autowired
	private GroupMenuService service;
	
	@Override
	public void initService() {
		super.service = service;
	}
	

	
	
}
