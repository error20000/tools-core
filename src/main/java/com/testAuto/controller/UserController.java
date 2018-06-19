package com.testAuto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jian.annotation.API;

import com.testAuto.entity.User;
import com.testAuto.service.UserService;

@Controller
@RequestMapping("/api/user")
@API(info="用户表", entity={User.class})
public class UserController extends BaseController<User> {

	@Autowired
	private UserService service;
	
	@Override
	public void initService() {
		super.service = service;
	}
	

	
	
}
