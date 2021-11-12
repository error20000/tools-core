package com.jian.tools.core;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public class AccessTokenTools {

	private static AccessTokenInterface tokenIfs = null;

	static{
		List<Class<?>> classes = findClass();
		if(classes == null || classes.size() == 0){
			tokenIfs = new AccessTokenImpl();
		}else{
			try {
				tokenIfs = (AccessTokenInterface) classes.get(0).newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		if(tokenIfs == null){
			tokenIfs = new AccessTokenImpl();
		}
	}

	/**
	 * 获取接口。返回工具类注册的实现。
	 * @return  AccessTokenInterface 的实现
	 */
	public static AccessTokenInterface getIfs(){
		return tokenIfs;
	}
	
	/**
	 * 创建token
	 * @param key 关键字
	 * @param value 被保存对象
	 * @return  token
	 */
	public static String createToken(String key, Object value){
		return tokenIfs.createToken(key, value);
	}
	
	/**
	 * 获取token。
	 * @param key 关键字
	 * @return
	 */
	public static String getToken(String key){
		return tokenIfs.getToken(key);
	}
	
	/**
	 * 获取保存的对象。
	 * @param key 关键字
	 * @return
	 */
	public static Object getValue(String key){
		return tokenIfs.getValue(key);
	}
	
	/**
	 * 检测是否有效。true表示有效。
	 * @param key 关键字
	 * @param token
	 * @return
	 */
	public boolean checkToken(String key, String token){
		return tokenIfs.checkToken(key, token);
	}
	
	/**
	 * 检测是否有效。true表示有效。自定义时间内有效(ms)
	 * @param key 关键字
	 * @param token
	 * @param overTime 自定义时间(ms)，-1表示不检查时间
	 * @return
	 */
	public boolean checkToken(String key, String token, long overTime){
		return tokenIfs.checkToken(key, token, overTime);
	}

	/**
	 * 清除token
	 * @param key
	 */
	public void clearToken(String key){
		tokenIfs.clearToken(key);
	}
	
	private static List<Class<?>> findClass(){
		List<Class<?>> classes = new ArrayList<>();
		try {
			//查找Cache接口的所有实现
			List<Class<?>> total = Tools.findClass("");
			for (Class<?> temp : total) {
				
				if(!temp.isInterface() && !Modifier.isAbstract(temp.getModifiers()) &&
						(temp.getInterfaces().length != 0 && temp.getInterfaces()[0].getName().equals(AccessTokenInterface.class.getName()) 
						|| AccessTokenAbstract.class.isAssignableFrom(temp)) ){
					classes.add(temp);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return classes;
	}

	public static void main(String[] args) {
		AccessTokenTools.createToken("", "");
		System.out.println(((AccessTokenImpl) AccessTokenTools.getIfs()).getClass().getName());
	}
}
