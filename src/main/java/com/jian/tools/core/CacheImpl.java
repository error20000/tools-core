package com.jian.tools.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheImpl extends CacheAbstract implements Cache {
	
	private static Map<String, CacheObject> objMap = new ConcurrentHashMap<String, CacheObject>();
	
	
	protected void initSetCacheObj(CacheObject obj) {
		objMap.put(obj.getKey(), obj);
	}

	protected CacheObject initGetCacheObj(String key) {
		return objMap.get(key);
	}

	protected void initClearCacheObj(String key) {
		objMap.remove(key);
	}

	
}
