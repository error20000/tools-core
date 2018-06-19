package com.testAuto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jian.annotation.API;

import com.testAuto.entity.Group;
import com.testAuto.service.GroupService;

@Controller
@RequestMapping("/api/group")
@API(info="appId表", entity={Group.class})
public class GroupController extends BaseController<Group> {

	@Autowired
	private GroupService service;
	
	@Override
	public void initService() {
		super.service = service;
	}
	

	
	
}
