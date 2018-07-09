package com.jian.tools.core;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import com.jian.annotation.Controller;

public class CacheTools {

	private static Cache cache = null;

	public static Cache getInstance() {
		return getInstance(null);
	}

	public static Cache getInstance(Cache impl) {
		if (impl == null) {
			impl = new CacheImpl();
		}
		if (cache == null) {
			cache = impl;
		}
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
	public static final void setCacheObj(String key, Object value, long timeOut) {
		cache.setCacheObj(key, value, timeOut);
	}

	/**
	 * 获取缓存数据。超时了返回null。
	 * 
	 * @param key
	 * @return
	 */
	public static final CacheObject getCacheObj(String key) {
		return cache.getCacheObj(key);
	}

	/**
	 * 获取缓存数据。先判断自身是否超时，再判断给定的超时时间。
	 * 
	 * @param key
	 * @param outTime
	 * @return
	 */
	public static final CacheObject getCacheObj(String key, long outTime) {
		return cache.getCacheObj(key, outTime);
	}

	/**
	 * 检测是否超时。true表示超时。默认超时时间2小时
	 * 
	 * @param key
	 * @return
	 */
	public static final boolean isTimeout(String key) {
		return cache.isTimeout(key);
	}

	/**
	 * 检测是否超时。true表示超时。自定义超时时间
	 * 
	 * @param key
	 * @param outTime
	 * @return
	 */
	public static final boolean isTimeout(String key, long outTime) {

		return cache.isTimeout(key, outTime);
	}

	/**
	 * 清楚缓存
	 * 
	 * @param key
	 */
	public static final void clearCacheObj(String key) {
		cache.clearCacheObj(key);
	}

	public static List<Class> getClasses(String packageName) throws ClassNotFoundException, IOException {

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		String path = packageName.replace(".", "/");
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {

			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		ArrayList<Class> classes = new ArrayList<Class>();
		for (File directory : dirs) {
			classes.addAll((Collection<? extends Class>) findClass(directory, packageName));
		}
		return classes;
	}

	public static List<Class> findClass(File directory, String packageName) throws ClassNotFoundException {
		List<Class> classes = new ArrayList<Class>();
		if (!directory.exists()) {
			return classes;
		}
		if(packageName != null && !"".equals(packageName)){
			packageName = packageName + ".";
		}

		File[] files = directory.listFiles();

		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClass(file, packageName + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				Class temp = Class.forName(packageName + file.getName().substring(0, file.getName().length() - 6));
				if(!temp.isInterface() && !Modifier.isAbstract(temp.getModifiers()) &&
						temp.getInterfaces().length != 0 && temp.getInterfaces()[0].getName().equals(Cache.class.getName())){
					System.out.println("-----"+temp.getInterfaces()[0].getName());
					classes.add(temp);
				}
			}
		}
		return classes;
	}

	public static void main(String[] args) {
		List<Class> list;
		try {
			list = CacheTools.getClasses("");
			for (Class clzz : list) {
				System.out.println(clzz.getName());
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
