package com.jian.tools.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Deprecated
public class AccessTokenImpl extends AccessTokenAbstract {

	public static Map<String, AccessToken> tokenMap = new ConcurrentHashMap<String, AccessToken>();
	
	
	protected void initSaveToken(AccessToken token) {
		tokenMap.put(token.getKey(), token);
	}

	protected AccessToken initGetToken(String key) {
		return tokenMap.get(key);
	}

	protected void initClearToken(String key) {
		tokenMap.remove(key);
	}

	
	
}
