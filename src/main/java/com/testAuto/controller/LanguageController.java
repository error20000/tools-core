package com.testAuto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jian.annotation.API;

import com.testAuto.entity.Language;
import com.testAuto.service.LanguageService;

@Controller
@RequestMapping("/api/language")
@API(info="appId表", entity={Language.class})
public class LanguageController extends BaseController<Language> {

	@Autowired
	private LanguageService service;
	
	@Override
	public void initService() {
		super.service = service;
	}
	

	
	
}
