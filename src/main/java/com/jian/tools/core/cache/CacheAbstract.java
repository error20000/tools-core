package com.jian.tools.core.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.jian.tools.core.DateTools;

public abstract class CacheAbstract implements Cache {
	
	private static boolean timerStart = false; 
	private static AtomicInteger count = new AtomicInteger(1);
	private static Timer timer = null;
	private static int runTime = 2 * 3600; //定时清理时间。单位（秒）
	private static long outTime = 2 * 3600 * 1000; //资源超时时间。单位（毫秒）
	
	private static List<Map<String, String>> sortMap = new ArrayList<Map<String, String>>();
	private static String lock = new String(); 
	
	public CacheAbstract(){
		this(true);
	}
	public CacheAbstract(boolean startAutoClear){
		if(startAutoClear){
			autoClear();
		}
	}
	
	protected abstract void initSetCacheObj(CacheObject obj);

	protected abstract CacheObject initGetCacheObj(String key);

	protected abstract void initClearCacheObj(String key);

	protected abstract List<String> initKeys(String regex);

	
	protected void initAutoClear() {
		System.out.println(DateTools.formatDate()+":	clear ing ..." + sortMap.size());
		long cur = System.currentTimeMillis();
		String toKey = cur + "" + 1000;
		//待清理数据
		List<Map<String, String>> clearList = sortMap.stream()
			.filter(e -> e.get("sortKey").compareTo(toKey) < 0)
			.collect(Collectors.toList());
		//剩余数据
		synchronized (lock) {
			sortMap = sortMap.stream()
					.filter(e -> e.get("sortKey").compareTo(toKey) >= 0)
					.collect(Collectors.toList());
		}
		//清理
		for (Map<String, String> map : clearList) {
			String key = map.get("key");
			//判断是否真的超时(同key覆盖超时时间的情况)
			CacheObject tmp = initGetCacheObj(key);
			if(tmp == null){ 
				continue;
			}
			long curss = System.currentTimeMillis();
			if(tmp.getTimeOut() < curss){
				//超时移除
				initClearCacheObj(key);
			}
			//initClearCacheObj(key);
		}
		clearList = null;
		System.out.println(DateTools.formatDate()+":	clear end " + sortMap.size());
	}
	
	
	
	
	public void setCacheObj(String key, Object value){
		setCacheObj(key, value, outTime);
	}
	
	public void setCacheObj(String key, Object value, long timeOut){
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
	
	public CacheObject getCacheObj(String key){
		CacheObject tmp = initGetCacheObj(key);
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
	
	public CacheObject getCacheObj(String key, long outTime){
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
	
	public boolean isTimeout(String key){
		return isTimeout(key, outTime);
	}
	
	public boolean isTimeout(String key, long outTime){
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
	
	public void clearCacheObj(String key){
		initClearCacheObj(key);
	}
	
	
	public List<String> keys(String regex) {
		return initKeys(regex);
	}
	
	
	/**
	 * 自动清理缓存
	 */
	public void autoClear() {
		if(!timerStart){
			System.out.println(DateTools.formatDate()+":	start cache clear...");
			runTime = runTime <= 0 ? 2 * 3600 * 1000 : runTime * 1000;
			timer = new Timer(true); 
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					try {
						initAutoClear();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, 0, runTime);
			timerStart = true;
		}
	}
	
	public void closeClear() {
		if(timer != null){
			timer.cancel();
			timer = null;
			timerStart = false;
		}
		System.out.println(DateTools.formatDate()+":	stop cache clear...");
	}
	
}
