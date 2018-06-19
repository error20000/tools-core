package com.testAuto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jian.annotation.API;

import com.testAuto.entity.MenuBtn;
import com.testAuto.service.MenuBtnService;

@Controller
@RequestMapping("/api/menubtn")
@API(info="appId表", entity={MenuBtn.class})
public class MenuBtnController extends BaseController<MenuBtn> {

	@Autowired
	private MenuBtnService service;
	
	@Override
	public void initService() {
		super.service = service;
	}
	

	
	
}
