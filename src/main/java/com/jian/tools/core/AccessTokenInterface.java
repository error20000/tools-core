package com.jian.tools.core;

/**
 * AccessToken 工具类接口
 * @author liujian
 *
 */
@Deprecated
public interface AccessTokenInterface {

	
	/**
	 * 创建token
	 * @param key 关键字
	 * @param value 被保存对象
	 * @return  token
	 */
	public String createToken(String key, Object value);
	
	/**
	 * 获取token
	 * @param key 关键字
	 * @return
	 */
	public String getToken(String key);
	
	/**
	 * 获取保存的对象
	 * @param key 关键字
	 * @return
	 */
	public Object getValue(String key);
	
	/**
	 * 检测是否有效。true表示有效。自定义时间内有效(ms)
	 * @param key 关键字
	 * @param token
	 * @param overTime 自定义时间(ms)，-1表示不检查时间
	 * @return
	 */
	public boolean checkToken(String key, String token, long... overTime);
	
	/**
	 * 清除token
	 * @param key
	 */
	public void clearToken(String key);
	
}
