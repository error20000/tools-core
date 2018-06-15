package com.jian.auto;

import com.jian.tools.core.Tools;

/**
 * 自动生成管理器。
 * <p>{@code config} 包配置
 * <p>{@code dbConfig} 数据库配置
 * <p>{@code mode} 生成模式，默认 jdbc+c3p0 , 1 -- spring boot
 * @author liujian
 *
 * @see com.tools.auto.AutoCreateNormal
 * @see com.tools.auto.AutoCreateSpringBoot
 */
public class AutoCreateManager {
	
	//数据库配置
	private AutoCreate autoCreate = null;
	
	
	public AutoCreateManager(Config config, ConfigDB dbConfig, int mode){
		switch (mode) {
		case 1:
			autoCreate = new AutoCreateSpringBoot(config, dbConfig);
			break;

		default:
			autoCreate = new AutoCreateNormal(config, dbConfig);
			break;
		}
		
	}
	
	/**
	 * 整库创建
	 */
	public void start(){
		if(autoCreate == null){
			return;
		}
		autoCreate.start();
	} 
	
	/**
	 * 整表创建
	 * @param tableName	表名
	 */
	public void start(String tableName){
		if(autoCreate == null){
			return;
		}
		if(Tools.isNullOrEmpty(tableName)){
			return;
		}
		autoCreate.start(tableName);
	}

}
