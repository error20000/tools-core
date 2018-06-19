package com.testAuto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jian.annotation.API;

import com.testAuto.entity.Menu;
import com.testAuto.service.MenuService;

@Controller
@RequestMapping("/api/menu")
@API(info="appId表", entity={Menu.class})
public class MenuController extends BaseController<Menu> {

	@Autowired
	private MenuService service;
	
	@Override
	public void initService() {
		super.service = service;
	}
	

	
	
}
