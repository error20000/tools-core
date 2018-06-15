package com.jian.spring.template;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.jian.tools.core.JsonTools;
import com.jian.tools.core.Tools;

public class Base<T> implements Cloneable, Serializable {

	private static final long serialVersionUID = 1L;
	
	public String serialize() {
		return JsonTools.toJsonString(this);
	}
	
	public void unserialize(String str) {
		T base = JsonTools.jsonToObj(str, getObejctClass());
		Field[] fs = Tools.getFields(this.getClass());
		for (Field field : fs) {
			Method[] setMethods = Tools.getMethods(this.getClass(), "set"+field.getName().substring(0, 1).toUpperCase()+field.getName().substring(1));
			Method[] getMethods = Tools.getMethods(this.getClass(), "get"+field.getName().substring(0, 1).toUpperCase()+field.getName().substring(1));
			if(setMethods.length > 0 && getMethods.length > 0){
				try {
					setMethods[0].invoke(this, getMethods[0].invoke(base));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public T clone() {
		T base = null;  
        try{  
        	base = (T)super.clone();  
        }catch(CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return base;
	}
	
	@SuppressWarnings("unchecked")
	private Class<T> getObejctClass(){
		Type type = getClass().getGenericSuperclass();
		Class<T> clss = null;
		try {
			Class<?>[] clsses = Tools.getGenericClass((ParameterizedType) type);
			clss = (Class<T>) clsses[0];
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return clss;
	}
	
}
