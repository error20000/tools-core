package com.testAuto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jian.annotation.API;

import com.testAuto.entity.GroupLanguage;
import com.testAuto.service.GroupLanguageService;

@Controller
@RequestMapping("/api/grouplanguage")
@API(info="appId表", entity={GroupLanguage.class})
public class GroupLanguageController extends BaseController<GroupLanguage> {

	@Autowired
	private GroupLanguageService service;
	
	@Override
	public void initService() {
		super.service = service;
	}
	

	
	
}
