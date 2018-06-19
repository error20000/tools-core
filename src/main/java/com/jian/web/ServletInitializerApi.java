package com.jian.web;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jian.annotation.API;
import com.jian.annotation.Controller;
import com.jian.annotation.RequestMapping;
import com.jian.annotation.RequestMethod;
import com.jian.tools.core.Tools;

/**
 * 初始化注解为API的类。
 * @author liujian
 *
 * @see com.jian.web.ServletContainerInitializerImpl
 * @see com.jian.annotation.Controller
 * @see com.jian.annotation.API
 */
public class ServletInitializerApi{
	
	public static List<RequestMappingData> mapping = new ArrayList<RequestMappingData>();

	public void onStartup(Set<Class<?>> clsses, ServletContext context) throws ServletException {
		context.log("ServletInitializerApi onStartup....");
		//正事
		if(clsses != null){
			//解析path
			for (Class<?> clss : clsses) {
				//注解为Controller的类，才加入Servlet。
				if (!clss.isInterface() && !Modifier.isAbstract(clss.getModifiers()) &&
						clss.isAnnotationPresent(Controller.class)) {
					mapping.addAll(parseMapping(clss, context));
				}else{
					mapping.addAll(parseApi(clss, context));
				}
			}
			
		}
	}
	
	
	private List<RequestMappingData> parseApi(Class<?> clss, ServletContext context) throws ServletException{
		//获取类API
		RequestMappingData parentData = new RequestMappingData();
		String name = clss.getAnnotation(API.class).name();
		String[] path = clss.getAnnotation(API.class).path();
		RequestMethod[] method = clss.getAnnotation(API.class).method();
		parentData.setReqMethod(method);
		parentData.setName(name);
		parentData.setPath(path);
		parentData.setClss(clss);
		
		//获取方法RequestMapping
		List<RequestMappingData> list = new ArrayList<>();
		Method[] methods = Tools.getMethods(clss);
		for (Method m : methods) {
			RequestMappingData rmd = parseMethod(parentData, m);
			if(rmd != null){
				list.add(rmd);
			}
		}
		//保存请求映射
		return list;
	}
	
	
	
	/**
	 * 解析path
	 * @param clss
	 * @param context
	 * @return
	 * @throws ServletException
	 */
	private List<RequestMappingData> parseMapping(Class<?> clss, ServletContext context) throws ServletException{
		//获取类RequestMapping
		RequestMappingData parentData = new RequestMappingData();
		if(clss.isAnnotationPresent(RequestMapping.class)){
			String name = clss.getAnnotation(RequestMapping.class).name();
			String[] value = clss.getAnnotation(RequestMapping.class).value();
			String[] path = clss.getAnnotation(RequestMapping.class).path();
			RequestMethod[] method = clss.getAnnotation(RequestMapping.class).method();
			String[] params = clss.getAnnotation(RequestMapping.class).params();
			String[] headers = clss.getAnnotation(RequestMapping.class).headers();
			String[] consumes = clss.getAnnotation(RequestMapping.class).consumes();
			String[] produces = clss.getAnnotation(RequestMapping.class).produces();
			
			parentData.setName(name);
			parentData.setValue(value);
			parentData.setPath(path);
			parentData.setReqMethod(method);
			parentData.setParams(params);
			parentData.setHeaders(headers);
			parentData.setConsumes(consumes);
			parentData.setProduces(produces);
		}
		parentData.setClss(clss);
		
		//获取方法RequestMapping
		List<RequestMappingData> list = new ArrayList<>();
		Method[] methods = Tools.getMethods(clss);
		for (Method m : methods) {
			RequestMappingData rmd = parseMethod(parentData, m);
			if(rmd != null){
				list.add(rmd);
			}
		}
		//保存请求映射
		return list;
	}
	

	private RequestMappingData parseMethod(RequestMappingData parentData, Method m){
		if(!(m.isAnnotationPresent(RequestMapping.class) || m.isAnnotationPresent(API.class))){
			return null;
		}
		RequestMappingData data = new RequestMappingData();
		//继承父类 parentData
		if(parentData != null){
			data = JSONObject.parseObject(JSON.toJSONString(parentData), RequestMappingData.class);
		}
		//RequestMapping
		if(m.isAnnotationPresent(RequestMapping.class)){
			String name = m.getAnnotation(RequestMapping.class).name();
			String[] value = m.getAnnotation(RequestMapping.class).value();
			String[] path = m.getAnnotation(RequestMapping.class).path();
			RequestMethod[] method = m.getAnnotation(RequestMapping.class).method();
			String[] params = m.getAnnotation(RequestMapping.class).params();
			String[] headers = m.getAnnotation(RequestMapping.class).headers();
			String[] consumes = m.getAnnotation(RequestMapping.class).consumes();
			String[] produces = m.getAnnotation(RequestMapping.class).produces();
			
			data.setName(data.getName()+"#"+name);
			//path
			String[] pranetPath = data.getPath().length > 0 ? data.getPath() : data.getValue();
			String[] slefPath = path.length > 0 ? path : value;
			List<String> pathTmp = new ArrayList<String>();
			for (int i = 0; i < pranetPath.length; i++) {
				for (int j = 0; j < slefPath.length; j++) {
					pathTmp.add(pranetPath[i] + slefPath[j]);
				}
			}
			data.setPath(pathTmp.toArray(new String[pathTmp.size()]));
			
			//method
			if(data.getReqMethod().length != 0){ //类
				if(method.length != 0){ //方法
					List<RequestMethod> methodTmp = new ArrayList<RequestMethod>();
					for (int i = 0; i < data.getReqMethod().length; i++) {
						boolean flag = false;
						for (int j = 0; j < method.length; j++) {
							if(data.getReqMethod()[i] == method[j]){
								flag = true;
								break;
							}
						}
						if(flag){
							methodTmp.add(data.getReqMethod()[i]);
						}
					}
					if(methodTmp.size() == 0) methodTmp.add(null);
					data.setReqMethod(methodTmp.toArray(new RequestMethod[methodTmp.size()]));
				}
			}else{
				data.setReqMethod(method);
			}
			
			//以下暂不支持
			data.setParams(params);
			data.setHeaders(headers);
			data.setConsumes(consumes);
			data.setProduces(produces);
			
		}else{
			String name = m.getAnnotation(API.class).name();
			String[] path = m.getAnnotation(API.class).path();
			RequestMethod[] method = m.getAnnotation(API.class).method();
			data.setName(data.getName()+"#"+name);
			//path
			String[] pranetPath = data.getPath().length > 0 ? data.getPath() : data.getValue();
			String[] slefPath = path;
			List<String> pathTmp = new ArrayList<String>();
			for (int i = 0; i < pranetPath.length; i++) {
				for (int j = 0; j < slefPath.length; j++) {
					pathTmp.add(pranetPath[i] + slefPath[j]);
				}
			}
			data.setPath(pathTmp.toArray(new String[pathTmp.size()]));
			
			//method
			data.setReqMethod(method);
		}
		
		return data;
	}
	

}
