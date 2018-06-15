package com.jian.jdbc.proxy;

import javax.sql.DataSource;

import com.jian.jdbc.JdbcOperate;

public class JdbcOperateManagerProxy {
	
	private static DataSource dataSource = null;
	private static JdbcOperate jdbcOperate = null;
	
	
	public static JdbcOperate getJdbcOperate() {
		return jdbcOperate;
	}
	
	public static DataSource getDataSource() {
		return dataSource;
	}
	
	

}
