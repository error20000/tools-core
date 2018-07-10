package com.jian.tools.core;

public abstract class AccessTokenAbstract implements AccessTokenInterface {

	private static long defOutTime = 2 * 3600 * 1000; //默认超时时间。单位（毫秒）
	
	
	protected abstract void initSaveToken(AccessToken token);

	protected abstract AccessToken initGetToken(String key);

	protected abstract void initClearToken(String key);

	
	
	public String createToken(String key, Object value){
		AccessToken token = new AccessToken(key, value);
		token.setMillis(System.currentTimeMillis());
		token.setToken(Tools.md5(key + "_" + token.getMillis())); //md5  key + "_" + millis
		initSaveToken(token);
		return token.getToken();
	}
	
	/**
	 * 获取token。不进行有效性检测。建议先{@code checkToken}，在获取。
	 * @param key 关键字
	 * @return
	 */
	public String getToken(String key){
		AccessToken token = initGetToken(key);
		if(token == null){
			return null;
		}
		return token.getToken();
	}
	/**
	 * 获取保存的对象。不进行有效性检测。建议先{@code checkToken}，在获取。
	 * @param key 关键字
	 * @return
	 */
	public Object getValue(String key){
		AccessToken token = initGetToken(key);
		if(token == null){
			return null;
		}
		return token.getValue();
	}
	
	/**
	 * 检测是否有效。true表示有效。自定义时间内有效(ms)
	 * @param key 关键字
	 * @param token
	 * @param overTime 自定义时间(ms)，-1表示不检查时间。默认时间2小时内有效。
	 * @return
	 */
	public boolean checkToken(String key, String token, long... overTime){
		AccessToken tmp = initGetToken(key);
		if(tmp == null){ 
			return false;
		}
		if(!tmp.getToken().equals(token)){ //验证token
			return false;
		}
		long time = overTime == null ? defOutTime : overTime[0];
		if(time >= 0){ // -1 不验证过期时间
			long cur = System.currentTimeMillis();
			if((tmp.getMillis() + time) < cur){
				clearToken(key); //过期清除
				return false;
			}
		}
		return true;
	}
	
	public void clearToken(String key){
		initClearToken(key);
	}
	
}
