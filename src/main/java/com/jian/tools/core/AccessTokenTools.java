package com.jian.tools.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AccessTokenTools {

	public static Map<String, AccessToken> tokenMap = new ConcurrentHashMap<String, AccessToken>();
	private static long defOutTime = 2 * 3600 * 1000; //默认超时时间。单位（毫秒）
	
	
	public static void initSaveToken(AccessToken token) {
		tokenMap.put(token.getKey(), token);
	}

	public static AccessToken initGetToken(String key) {
		return tokenMap.get(key);
	}

	public static AccessToken initClearToken(String key) {
		return tokenMap.remove(key);
	}

	
	
	public static final String createToken(String key, Object value){
		AccessToken token = new AccessToken(key, value);
		token.setMillis(System.currentTimeMillis());
		token.setToken(Tools.md5(key + token.getMillis())); //md5  key + millis
		initSaveToken(token);
		return token.getToken();
	}
	
	public static final AccessToken getToken(String key){
		return initGetToken(key);
	}
	
	public static final boolean checkToken(String key, String token){
		return checkToken(key, token, true, defOutTime );
	}
	
	public static final boolean checkToken(String key, String token, boolean point){
		return checkToken(key, token, point, defOutTime );
	}
	
	public static final boolean checkToken(String key, String token, boolean point, long overTime){
		AccessToken tmp = getToken(key);
		if(tmp == null){ 
			return false;
		}
		if(point && !tmp.getToken().equals(token)){ //验证token
			return false;
		}
		long cur = System.currentTimeMillis();
		if((tmp.getMillis() + overTime) < cur){
			initClearToken(key);
			return false;
		}
		return true;
	}
	
	public static final void clearToken(String key){
		initClearToken(key);
	}
	
	
}
