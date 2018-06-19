package com.testAuto.config;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jian.tools.core.JsonTools;
import com.jian.tools.core.ResultTools;
import com.jian.tools.core.Tips;

public class LoginConfig {
	
	public static String loginUserKey = "login_user";
	
	public static Map<String, Object> verifyLogin(HttpServletRequest req){
		HttpSession session = req.getSession();
		Object temp = session.getAttribute(loginUserKey);
		if(temp == null){
			return ResultTools.custom(Tips.ERROR111).build();
		}
		return null;
	}
	
	public static String getUserInfo(HttpServletRequest req){
		//获取用户信息
		HttpSession session = req.getSession();
		Object user = session.getAttribute(loginUserKey);
		if(user == null){
			return null;
		}
		return JsonTools.toJsonString(user);
	}
	
}
