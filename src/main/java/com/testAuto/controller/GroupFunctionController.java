package com.testAuto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jian.annotation.API;

import com.testAuto.entity.GroupFunction;
import com.testAuto.service.GroupFunctionService;

@Controller
@RequestMapping("/api/groupfunction")
@API(info="appId表", entity={GroupFunction.class})
public class GroupFunctionController extends BaseController<GroupFunction> {

	@Autowired
	private GroupFunctionService service;
	
	@Override
	public void initService() {
		super.service = service;
	}
	

	
	
}
