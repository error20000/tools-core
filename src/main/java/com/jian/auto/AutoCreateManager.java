package com.jian.auto;

import com.jian.tools.core.Tools;

/**
 * 自动生成管理器。
 * <p>{@code config} 包配置
 * <p>{@code dbConfig} 数据库配置
 * <p>{@code autoCreate} 自动生成管理器接口，可自定义
 * <p>{@code mode} 生成模式，默认：自己的 , 1： spring boot
 * @author liujian
 *
 * @see com.jian.auto.AutoCreateNormal
 * @see com.jian.auto.AutoCreateSpringBoot
 */
public class AutoCreateManager {
	
	private AutoCreate autoCreate = null;
	
	public AutoCreateManager(Config config, ConfigDB dbConfig){
		this(config, dbConfig, 0);
	}
	
	public AutoCreateManager(Config config, ConfigDB dbConfig, int mode){
		switch (mode) {
		case 10: //spring + jdbc + mysql
			Config.setTempPath("/com/jian/sboot/template/");
			autoCreate = new AutoCreateSpringBoot(config, dbConfig);
			break;
		case 20: //spring + mybatis + mysql
			Config.setTempPath("/com/jian/sboot/template/");
			autoCreate = new AutoCreateSpringBootMybatis(config, dbConfig);
			break;

		default:
			Config.setTempPath("/com/jian/auto/template/");
			autoCreate = new AutoCreateNormal(config, dbConfig);
			break;
		}
		
	}
	
	public AutoCreateManager(AutoCreate autoCreate){
		this.autoCreate = autoCreate;
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
