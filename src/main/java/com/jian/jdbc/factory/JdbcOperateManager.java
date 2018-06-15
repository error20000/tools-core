package com.jian.jdbc.factory;

import java.io.File;

import javax.sql.DataSource;

import com.jian.jdbc.JdbcOperate;
import com.jian.jdbc.c3p0.C3P0PropertiesConfig;

public class JdbcOperateManager {
	//主库
	private DataSource dataSource = null;
	private JdbcOperate jdbcOperate = null;
	//从库
	private DataSource dataSourceSecond = null;
	private JdbcOperate jdbcOperateSecond = null;
	//SQL 日志
	
	public JdbcOperateManager(String dbPath){
		//主从库一样
		C3P0PropertiesConfig config = null;
		File file = new File(dbPath);
		if(file.exists()){
			config = new C3P0PropertiesConfig(file);
			dataSource = config.getDataSource();
			dataSourceSecond = config.getDataSource();
		}else{
			config = new C3P0PropertiesConfig(dbPath);
			dataSource = config.getDataSource();
			dataSourceSecond = config.getDataSource();
		}
		jdbcOperate = new JdbcOperate(dataSource);
		jdbcOperateSecond = new JdbcOperate(dataSourceSecond);
	}
	
	public JdbcOperateManager(String dbPath, String dbPathSecond){
		//主从库单独配置
		C3P0PropertiesConfig config = null;
		File file = new File(dbPath);
		File fileSecond = new File(dbPathSecond);
		//主
		if(file.exists()){ 
			config = new C3P0PropertiesConfig(file);
			dataSource = config.getDataSource();
		}else{
			config = new C3P0PropertiesConfig(dbPath);
			dataSource = config.getDataSource();
		}
		jdbcOperate = new JdbcOperate(dataSource);
		//从
		if(fileSecond.exists()){ 
			config = new C3P0PropertiesConfig(fileSecond);
			dataSourceSecond = config.getDataSource();
		}else{
			config = new C3P0PropertiesConfig(dbPathSecond);
			dataSourceSecond = config.getDataSource();
		}
		jdbcOperateSecond = new JdbcOperate(dataSourceSecond);
	}
	
	/**
     * 主库配置，从库默认为主库
     * @param dbPath	主库
     */
	public static JdbcOperateManager newManager(String dbPath){
		return new JdbcOperateManager(dbPath);
	}
	
	/**
     * 主从库配置
     * @param dbPath	主库
     * @param dbPathSecond	从库
     */
	public static JdbcOperateManager newManager(String dbPath, String dbPathSecond){
		return new JdbcOperateManager(dbPath, dbPathSecond);
	}
	
	public JdbcOperate getJdbcOperate() {
		return jdbcOperate;
	}
	
	public DataSource getDataSource() {
		return dataSource;
	}
	
	public JdbcOperate getJdbcOperateSecond() {
		return jdbcOperateSecond;
	}
	
	public DataSource getDataSourceSecond() {
		return dataSourceSecond;
	}

}
