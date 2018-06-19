package com.testAuto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jian.annotation.API;

import com.testAuto.entity.GroupApp;
import com.testAuto.service.GroupAppService;

@Controller
@RequestMapping("/api/groupapp")
@API(info="appId表", entity={GroupApp.class})
public class GroupAppController extends BaseController<GroupApp> {

	@Autowired
	private GroupAppService service;
	
	@Override
	public void initService() {
		super.service = service;
	}
	

	
	
}
