package com.jian.tools.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class CacheTools {
	
	private static Map<String, CacheObject> objMap = new ConcurrentHashMap<String, CacheObject>();
	private static ReentrantLock lock = new ReentrantLock(); 
	private static boolean timerStart = false; 
	private static AtomicInteger count = new AtomicInteger(1);
	private static Timer timer = null;
	private static int runTime = 2 * 3600; //定时清理时间。单位（秒）
	private static long outTime = 2 * 3600 * 1000; //资源超时时间。单位（毫秒）
	
	private static List<Map<String, String>> sortMap = new ArrayList<Map<String, String>>();
	
	static{
		autoClear();
	}
	
	public static void initSetCacheObj(CacheObject obj) {
		objMap.put(obj.getKey(), obj);
	}

	public static CacheObject initGetCacheObj(String key) {
		return objMap.get(key);
	}

	public static void initClearCacheObj(String key) {
		objMap.remove(key);
	}

	
	public static void initAutoClear() {
		System.out.println("clear ing ...");
		long cur = System.currentTimeMillis();
		String toKey = cur + "" + 1000;
		//待清理数据
		List<Map<String, String>> clearList = sortMap.stream()
			.filter(e -> e.get("sortKey").compareTo(toKey) < 0)
			.collect(Collectors.toList());
		//剩余数据
		synchronized (sortMap) {
			sortMap = sortMap.stream()
					.filter(e -> e.get("sortKey").compareTo(toKey) >= 0)
					.collect(Collectors.toList());
		}
		//清理
		for (Map<String, String> map : clearList) {
			String key = map.get("key");
			initClearCacheObj(key);
		}
		clearList = null;
		System.out.println("clear end ...");
	}
	
	public static final void autoClear() {
		if(!timerStart){
			System.out.println("start cache clear...");
			runTime = runTime <= 0 ? 2 * 3600 * 1000 : runTime * 1000;
			timer = new Timer(true); 
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					lock.lock();
					initAutoClear();
					lock.unlock();
				}
			}, 0, runTime);
			timerStart = true;
		}
	}
	
	
	
	/**
	 * 设置缓存数据。默认超时时间2小时。其中回收程序默认2小时运行一次。
	 * @param key
	 * @param value
	 */
	public static final void setCacheObj(String key, Object value){
		setCacheObj(key, value, outTime);
	}
	
	/**
	 * 设置缓存数据。自定义超时时间。其中回收程序默认2小时运行一次。
	 * @param key
	 * @param value
	 * @param timeOut
	 */
	public static final void setCacheObj(String key, Object value, long timeOut){
		CacheObject obj = new CacheObject(key, value);
		long cur = System.currentTimeMillis();
		obj.setMillis(cur); //设置缓存时间
		obj.setTimeOut(cur + timeOut); //设置超时时间
		initSetCacheObj(obj);
		//清理：排序key
		int index = count.getAndAdd(1);
		count.compareAndSet(1000, 1);
		cur += timeOut; //加上超时时间
		String sortKey = cur +""+(index < 10 ? "000"+index : index < 100 ? "00"+index : index < 1000 ? "0"+index : index);
		Map<String, String> node = new HashMap<String, String>();
		node.put("sortKey", sortKey);
		node.put("key", key);
		sortMap.add(node);
	}
	
	/**
	 * 获取缓存数据。只要还没有回收，即使超时了也可以获取到。
	 * @param key
	 * @return
	 */
	public static final CacheObject getCacheObjOrigin(String key){
		return initGetCacheObj(key);
	}
	
	/**
	 * 获取缓存数据。超时了返回null。
	 * @param key
	 * @return
	 */
	public static final CacheObject getCacheObj(String key){
		CacheObject tmp = getCacheObjOrigin(key);
		if(tmp == null){ 
			return tmp;
		}
		long cur = System.currentTimeMillis();
		if(tmp.getTimeOut() < cur){
			//超时移除
			clearCacheObj(key);
			return null;
		}
		return tmp;
	}
	
	/**
	 * 获取缓存数据。先判断自身是否超时，再判断给定的超时时间。
	 * @param key
	 * @param outTime
	 * @return
	 */
	public static final CacheObject getCacheObj(String key, long outTime){
		CacheObject tmp = getCacheObj(key);
		if(tmp == null){ 
			return tmp;
		}
		long cur = System.currentTimeMillis();
		if((tmp.getMillis() + outTime) < cur){
			//超时移除
			clearCacheObj(key);
			return null;
		}
		return tmp;
	}
	
	/**
	 * 检测是否超时。true表示超时。默认超时时间2小时
	 * @param key
	 * @return
	 */
	public static final boolean checkTimeout(String key){
		return checkTimeout(key, outTime);
	}
	
	/**
	 * 检测是否超时。true表示超时。自定义超时时间
	 * @param key
	 * @param outTime
	 * @return
	 */
	public static final boolean checkTimeout(String key, long outTime){
		CacheObject tmp = getCacheObj(key);
		if(tmp == null){ 
			return true;
		}
		long cur = System.currentTimeMillis();
		if((tmp.getMillis() + outTime) < cur){
			//超时移除
			clearCacheObj(key);
			return true;
		}
		return false;
	}
	
	/**
	 * 清楚缓存
	 * @param key
	 */
	public static final void clearCacheObj(String key){
		initClearCacheObj(key);
	}
	
	
	public static void main(String[] args) {
		/*CacheTools.setCacheObj("1", "2");
		try {
			Thread.sleep(10 * 3600 * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		for (int i = 0; i < 2000; i++) {
			int count = i;
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					setCacheObj("key_"+count, "value_"+count);
				}
			}).start();
		}
		
	}
}
