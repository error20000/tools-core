package com.jian.auto;

/**
 * 自动生成管理器。需要配置的参数：
 * @author liujian
 *
 */
public interface AutoCreate {
	
	
	/**
	 * 整库创建
	 */
	public void start(); 
	
	/**
	 * 整表创建
	 * @param tableName	表名
	 */
	public void start(String tableName);
	
	
}
