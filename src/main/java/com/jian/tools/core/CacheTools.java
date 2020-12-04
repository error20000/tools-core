package com.jian.tools.core;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import com.jian.tools.core.cache.Cache;
import com.jian.tools.core.cache.CacheAbstract;
import com.jian.tools.core.cache.CacheImpl;
import com.jian.tools.core.cache.CacheObject;

public class CacheTools {

	private static Cache cache = null;

	static{
		List<Class<?>> classes = findClass();
		if(classes == null || classes.size() == 0){
			cache = new CacheImpl();
		}else{
			try {
				cache = (Cache) classes.get(0).newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		if(cache == null){
			cache = new CacheImpl();
		}
	}
	

	/**
	 * 获取接口。返回工具类注册的实现。
	 * @return  Cache 的实现
	 */
	public static Cache getIfs(){
		return cache;
	}

	/**
	 * 设置缓存数据。默认超时时间2小时。其中回收程序默认2小时运行一次。
	 * 
	 * @param key
	 * @param value
	 */
	public static void setCacheObj(String key, Object value) {
		cache.setCacheObj(key, value);
	}

	/**
	 * 设置缓存数据。自定义超时时间。其中回收程序默认2小时运行一次。
	 * 
	 * @param key
	 * @param value
	 * @param timeOut
	 */
	public static void setCacheObj(String key, Object value, long timeOut) {
		cache.setCacheObj(key, value, timeOut);
	}

	/**
	 * 获取缓存数据。超时了返回null。
	 * 
	 * @param key
	 * @return
	 */
	public static CacheObject getCacheObj(String key) {
		return cache.getCacheObj(key);
	}

	/**
	 * 获取缓存数据。先判断自身是否超时，再判断给定的超时时间。
	 * 
	 * @param key
	 * @param outTime
	 * @return
	 */
	public static CacheObject getCacheObj(String key, long outTime) {
		return cache.getCacheObj(key, outTime);
	}

	/**
	 * 检测是否超时。true表示超时。默认超时时间2小时
	 * 
	 * @param key
	 * @return
	 */
	public static boolean isTimeout(String key) {
		return cache.isTimeout(key);
	}

	/**
	 * 检测是否超时。true表示超时。自定义超时时间
	 * 
	 * @param key
	 * @param outTime
	 * @return
	 */
	public static boolean isTimeout(String key, long outTime) {

		return cache.isTimeout(key, outTime);
	}

	/**
	 * 清楚缓存
	 * 
	 * @param key
	 */
	public static void clearCacheObj(String key) {
		cache.clearCacheObj(key);
	}

	/**
	 * 获取keys
	 * 
	 * @param regex
	 * @return	List
	 */
	public static List<String> keys(String regex) {
		return cache.keys(regex);
	}


	private static List<Class<?>> findClass(){
		List<Class<?>> classes = new ArrayList<>();
		try {
			//查找Cache接口的所有实现
			List<Class<?>> total = Tools.findClass("");
			for (Class<?> temp : total) {
				
				if(!temp.isInterface() && !Modifier.isAbstract(temp.getModifiers()) &&
						(temp.getInterfaces().length != 0 && temp.getInterfaces()[0].getName().equals(Cache.class.getName()) 
						|| CacheAbstract.class.isAssignableFrom(temp)) ){
					classes.add(temp);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return classes;
	}

	public static void main(String[] args) throws Exception {
		CacheTools.setCacheObj("", "");
		CacheTools.setCacheObj("123", "456", 10 * 1000);
		CacheTools.setCacheObj("123", "123", 100 * 1000);
		System.out.println(CacheTools.keys(".*").size());
		Thread.sleep(5 * 1000);
		System.out.println(CacheTools.getCacheObj("123").getValue());
		Thread.sleep(10 * 1000);
		System.out.println(CacheTools.getCacheObj("123").getValue());
		System.out.println(((CacheImpl) CacheTools.getIfs()).getClass().getName());
	}

}
