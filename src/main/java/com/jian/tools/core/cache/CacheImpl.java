package com.jian.tools.core.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheImpl extends CacheAbstract {
	
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

	protected List<String> initKeys(String regex) {
		List<String> res = new ArrayList<String>();
		for (String str : objMap.keySet()) {
			if(regex != null && str.matches(regex)) {
				res.add(str);
			}
		}
		return res;
	}

	public Map<String, CacheObject> getObjMap() {
		return objMap;
	}
	
}
