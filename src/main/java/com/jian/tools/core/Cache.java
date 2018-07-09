package com.jian.tools.core;

public interface Cache {
	
	/**
	 * 设置缓存数据。默认超时时间。
	 * @param key
	 * @param value
	 */
	public void setCacheObj(String key, Object value);
	
	/**
	 * 设置缓存数据。自定义超时时间。
	 * @param key
	 * @param value
	 * @param timeOut
	 */
	public void setCacheObj(String key, Object value, long timeOut);
	
	/**
	 * 获取缓存数据。超时了返回null。
	 * @param key
	 * @return
	 */
	public CacheObject getCacheObj(String key);
	
	/**
	 * 获取缓存数据。先判断自身是否超时，再判断给定的超时时间。
	 * @param key
	 * @param outTime
	 * @return
	 */
	public CacheObject getCacheObj(String key, long outTime);
	
	/**
	 * 检测是否超时。true表示超时。
	 * @param key
	 * @return
	 */
	public boolean isTimeout(String key);
	
	/**
	 * 检测是否超时。true表示超时。自定义超时时间
	 * @param key
	 * @param outTime
	 * @return
	 */
	public boolean isTimeout(String key, long outTime);
	
	/**
	 * 清楚缓存
	 * @param key
	 */
	public void clearCacheObj(String key);
	
}
