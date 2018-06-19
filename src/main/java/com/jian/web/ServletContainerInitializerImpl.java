package com.jian.web;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;

import com.jian.annotation.API;
import com.jian.annotation.Controller;


/**
 * 注解为Controller的类，才加入Servlet。
 * @author liujian
 *
 * @see com.jian.annotation.Controller
 */
@HandlesTypes({Controller.class, API.class})
public class ServletContainerInitializerImpl implements ServletContainerInitializer {
	
	@Override
	public void onStartup(Set<Class<?>> clsses, ServletContext context) throws ServletException {
		context.log("ServletContainerInitializerImpl onStartup....");
		Set<Class<?>> ctrlClsses = new HashSet<Class<?>>();
		Set<Class<?>> apiClsses = new HashSet<Class<?>>();
		//listener
		listener(context);
		//servlet、api
		if(clsses != null){
			for (Class<?> clss : clsses) {
				//继承AbstractBaseController的类、或者注解为Controller的类，才加入Servlet。
				if (!clss.isInterface() && !Modifier.isAbstract(clss.getModifiers()) &&
						clss.isAnnotationPresent(Controller.class)) {
					ctrlClsses.add(clss);
					apiClsses.add(clss);
				//api
				}else if (!clss.isInterface() && !Modifier.isAbstract(clss.getModifiers()) || clss.isAnnotationPresent(API.class)) {
					apiClsses.add(clss);
				}
			}
			//解析
			new ServletInitializerController().onStartup(ctrlClsses, context);
			new ServletInitializerApi().onStartup(apiClsses, context);
		}
	}
	
	private void listener(ServletContext context) throws ServletException{
		context.addListener(new ServletContextListenerImpl());
	}
    
}
