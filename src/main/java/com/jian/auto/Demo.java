package com.jian.auto;

public class Demo {

	public static void main(String[] args) {
		/**
		 zeroDateTimeBehavior=convertToNull 
		 	这是因为JDBC不能将'0000-00-00 00:00:00'转化为一个为一个java.sql.Timestamp，
 			在Java中，想创建一个java.util.Date，使其值为 '0000-00-00'也是不可能的，最古老的日期应该是'0001-01-01 00:00:00'。
		 */
		String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/auth?characterEncoding=utf8";
		String user = "root";
		String password = "123456";
		String driverClass = "com.mysql.jdbc.Driver";
		String prefix = "s_";
		String separator = "_";
		//包配置
		Config config = new Config("com.testAuto");
		config.setOverWrite(true);
		//数据库配置
		ConfigDB cdb = new ConfigDB(jdbcUrl, user, password, driverClass, prefix, separator);
		AutoCreateManager test =  new AutoCreateManager(config, cdb, 20); //10 20
		test.start(); //全部数据表
//		test.start(tableName); //指定数据表
		
	}
	
}
