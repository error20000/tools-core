package com.testAuto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jian.annotation.API;

import com.testAuto.entity.App;
import com.testAuto.service.AppService;

@Controller
@RequestMapping("/api/app")
@API(info="appId表", entity={App.class})
public class AppController extends BaseController<App> {

	@Autowired
	private AppService service;
	
	@Override
	public void initService() {
		super.service = service;
	}
	

	
	
}
