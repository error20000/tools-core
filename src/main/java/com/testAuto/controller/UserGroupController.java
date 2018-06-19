package com.testAuto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jian.annotation.API;

import com.testAuto.entity.UserGroup;
import com.testAuto.service.UserGroupService;

@Controller
@RequestMapping("/api/usergroup")
@API(info="appId表", entity={UserGroup.class})
public class UserGroupController extends BaseController<UserGroup> {

	@Autowired
	private UserGroupService service;
	
	@Override
	public void initService() {
		super.service = service;
	}
	

	
	
}
