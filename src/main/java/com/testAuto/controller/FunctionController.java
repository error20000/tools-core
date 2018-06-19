package com.testAuto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jian.annotation.API;

import com.testAuto.entity.Function;
import com.testAuto.service.FunctionService;

@Controller
@RequestMapping("/api/function")
@API(info="appId表", entity={Function.class})
public class FunctionController extends BaseController<Function> {

	@Autowired
	private FunctionService service;
	
	@Override
	public void initService() {
		super.service = service;
	}
	

	
	
}
