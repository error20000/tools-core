package com.jian.web;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.WatchEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jian.annotation.RequestMapping;
import com.jian.annotation.RequestMethod;
import com.jian.tools.core.CallBack;
import com.jian.tools.core.Tools;

/**
 * 初始化注解为Controller的类。
 * @author liujian
 *
 * @see com.tools.web.ServletContainerInitializerImpl
 * @see com.tools.web.annotation.Controller
 */
public class ServletInitializerController{
	
	public static List<RequestMappingData> mapping = new ArrayList<RequestMappingData>();

	public void onStartup(Set<Class<?>> clsses, ServletContext context) throws ServletException {
		context.log("ServletInitializerController onStartup....");
		//正事
		if(clsses != null){
			//解析path
			for (Class<?> clss : clsses) {
				mapping.addAll(parseMapping(clss, context));
			}
			
			//检测path唯一
			Map<String, Integer> hash = new HashMap<String, Integer>();
			for (RequestMappingData node : mapping) {
				if(node.getPath().length != 0){
					for (int i = 0; i < node.getPath().length; i++) {
						Integer count = hash.get(node.getPath()[i]);
						if(count == null){
							hash.put(node.getPath()[i], 1);
						}else{
							throw new ServletException("parse url mapping error!!", new Throwable());
						}
					}
				}else{
					for (int i = 0; i < node.getValue().length; i++) {
						Integer count = hash.get(node.getValue()[i]);
						if(count == null){
							hash.put(node.getValue()[i], 1);
						}else{
							throw new ServletException("parse url mapping error, "+node.getValue()[i]+" not only.", new Throwable());
						}
					}
				}
			}
			//创建servlet
			parseServlet(mapping, context);
		}
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
			if(m.isAnnotationPresent(RequestMapping.class)){
				String name = m.getAnnotation(RequestMapping.class).name();
				String[] value = m.getAnnotation(RequestMapping.class).value();
				String[] path = m.getAnnotation(RequestMapping.class).path();
				RequestMethod[] method = m.getAnnotation(RequestMapping.class).method();
				String[] params = m.getAnnotation(RequestMapping.class).params();
				String[] headers = m.getAnnotation(RequestMapping.class).headers();
				String[] consumes = m.getAnnotation(RequestMapping.class).consumes();
				String[] produces = m.getAnnotation(RequestMapping.class).produces();
				
				RequestMappingData data = new RequestMappingData();
				if(parentData != null){
					data = JSONObject.parseObject(JSON.toJSONString(parentData), RequestMappingData.class);
				}
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
				
				data.setMethod(m);
				list.add(data);
			}
			
		}
		//保存请求映射
		return list;
	}
	
	/**
	 * 创建servlet
	 * @param mappingList
	 * @param context
	 */
	private void parseServlet(List<RequestMappingData> mappingList, ServletContext context){
		if(mappingList != null){
			List<String> pathList = new ArrayList<String>();
			//servlet
			ServletRegistration.Dynamic sr = context.addServlet("com.tools.web", RequstServlet.class);
			sr.setAsyncSupported(true); //开启异步
			context.log("AsyncSupported true...");
			for (RequestMappingData mappingData : mappingList) {
				String[] path = mappingData.getPath().length != 0 ? mappingData.getPath() : mappingData.getValue();
				for (int i = 0; i < path.length; i++) {
					sr.addMapping(path[i]);
					pathList.add(path[i]);
					context.log(this.getClass().getName()+": Servlet Register Mapping ["+path[i]+"] "+mappingData.getClss().getName());
				}
			}
			//创建servlet开关
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					String rpath = Tools.getBasePath() + AllowReq.getFilePath();
					List<String> content = new ArrayList<String>();
					File file = new File(rpath);
					if(file.exists()){
						content = Tools.fileReader(file);
						//修改
						boolean flag = true;
						for (String str1 : pathList) {
							flag = true;
							//判断如果原文件已存在该path，则不加入写内容。
							for (String str2 : content) {
								if(str1.equals(str2.split(" : ")[0].trim())){
									flag = false;
									break;
								}
							}
							if(flag){
								content.add(str1 + " : true");
							}
						}
					}else{
						for (String str : pathList) {
							content.add(str + " : true");
						}
					}
					//写入文件
					Tools.fileWrite(file, content, false);
					//写入内存
					for (String str : content) {
						AllowReq.setContent(str.split(":")[0].trim(), str.split(":")[1].trim());
					}
					//监控文件变化，10s检测一次
					Tools.fileWatch(file, 10, new CallBack() {
						@Override
						public void execute(Object obj) {
							WatchEvent<?> event = (WatchEvent<?>) obj;
							//TODO 根据事件类型采取不同的操作。。。。。。。  
							context.log(this.getClass().getName()+": ["+event.context()+"]文件发生了["+event.kind()+"]事件, path " + file.getPath()); 
							//如果文件变了，重新载入
							List<String> change = Tools.fileReader(file);
							for (String str : change) {
								String[] tmp = str.split(":");
								if(tmp.length > 1){
									AllowReq.setContent(tmp[0].trim(), tmp[1].trim());
								}
							}
						}
					});
				}
			});
			thread.start();
		}
	}
	

}
