package com.jian.auto.db;

import java.io.File;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.sql.DataSource;

import com.jian.jdbc.JdbcOperate;
import com.jian.jdbc.c3p0.C3P0PropertiesConfig;
import com.jian.jdbc.sboot.SbootPropertiesConfig;
import com.jian.tools.core.Attacks;
import com.jian.tools.core.Tools;

public class TableManager {

	private String dbName = null;
	private JdbcOperate jdbcOperate = null;
	
	public TableManager(){
		
	}
	public TableManager(String dbpath){
		//测试是否有c3p0
		Class<?> test = null;
		try {
			test = Class.forName("com.mchange.v2.c3p0.ComboPooledDataSource");
		} catch (ClassNotFoundException e) {
			System.out.println("com.mchange.v2.c3p0.ComboPooledDataSource not found, use org.springframework.jdbc.datasource.DriverManagerDataSource!");
		}
		File file = new File(dbpath);
		if(file.exists()){
			//this(file);  调用当前对象的其它构造函数    构造方法之间进行调用时this语句只能出现在第一行
			DataSource dataSource = null;
			if(test != null){
				dataSource = new C3P0PropertiesConfig(file).getDataSource();
			}else{
				dataSource = new SbootPropertiesConfig(file).getDataSource();
			}
			jdbcOperate = new JdbcOperate(dataSource);
			dbName = getDBName(Tools.getProperties(file).getProperty("jdbcUrl"));
		}else{
			DataSource dataSource = null;
			if(test != null){
				dataSource = new C3P0PropertiesConfig(dbpath).getDataSource();
			}else{
				dataSource = new SbootPropertiesConfig(dbpath).getDataSource();
			}
			jdbcOperate = new JdbcOperate(dataSource);
			dbName = getDBName(Tools.getProperties(dbpath).getProperty("jdbcUrl"));
		}
	}
	
	public TableManager(File file){
		//测试是否有c3p0
		Class<?> test = null;
		try {
			test = Class.forName("com.mchange.v2.c3p0.ComboPooledDataSource");
		} catch (ClassNotFoundException e) {
			System.out.println("com.mchange.v2.c3p0.ComboPooledDataSource not found, use org.springframework.jdbc.datasource.DriverManagerDataSource!");
		}
		DataSource dataSource = null;
		if(test != null){
			dataSource = new C3P0PropertiesConfig(file).getDataSource();
		}else{
			dataSource = new SbootPropertiesConfig(file).getDataSource();
		}
		jdbcOperate = new JdbcOperate(dataSource);
		dbName = getDBName(Tools.getProperties(file).getProperty("jdbcUrl"));
	}
	
	public TableManager(DataSource dataSource, String dbName){
		this.dbName = dbName;
		this.jdbcOperate = new JdbcOperate(dataSource);
	}
	
	public List<Table> getDbInfo() {
		List<Map<String, Object>> tableNames = getTableInfos();
		Table table = null;
		List<Table> tables = null;
		if(tableNames!=null && tableNames.size()>0){
			tables = new ArrayList<Table>();
			for(int i = 0; i < tableNames.size(); i++){
				String name = tableNames.get(i).get("TABLE_NAME")+"";
				String comment = tableNames.get(i).get("TABLE_COMMENT")+"";
				table = new Table();
				table.setTableInfo(getTableStructure(name));
				table.setIndexInfo(getTableIndex(name));
				table.setTableName(name);
				table.setComment(comment);
				tables.add(table);
			}
		}
		return tables;
	}
	
	public Table getDbTable(String tableName) {
		Table table = new Table();
		List<Map<String, Object>> tableNames = getTableInfo(tableName);
		if(tableNames != null && tableNames.size() > 0){
			table.setTableInfo(getTableStructure(tableName));
			table.setIndexInfo(getTableIndex(tableName));
			table.setTableName(tableName);
			String comment = tableNames.get(0).get("TABLE_COMMENT")+"";
			table.setComment(comment);
		}
		return table;
	}
	
	private String getDBName(String jdbcUrl){
		if(Tools.isNullOrEmpty(jdbcUrl)){
			return "";
		}
		if(jdbcUrl.indexOf("?") != -1){
			jdbcUrl = jdbcUrl.substring(0, jdbcUrl.indexOf("?"));
		}
		return jdbcUrl.substring(jdbcUrl.lastIndexOf("/")+1);
	}
	
	private List<Map<String, Object>> getTableInfos() {
		if(Tools.isNullOrEmpty(dbName)){
			System.out.println("数据库名称解析错误。。。。。。");
			return null;
		}
		String sql = "select * from information_schema.TABLES where TABLE_SCHEMA='"+dbName+"' and TABLE_TYPE='BASE TABLE';"; //"show tables";
		List<Map<String, Object>> tables = new ArrayList<Map<String, Object>>();
		try {
			tables = jdbcOperate.queryMapList(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tables;
	}
	
	private List<Map<String, Object>> getTableInfo(String tableName) {
		if(Tools.isNullOrEmpty(dbName) || Tools.isNullOrEmpty(tableName)){
			System.out.println("数据库名称（表名）解析错误。。。。。。");
			return null;
		}
		String sql = "select * from information_schema.TABLES where TABLE_SCHEMA='"+dbName+"' and TABLE_NAME='"+tableName+"';"; //"show tables";
		List<Map<String, Object>> tables = new ArrayList<Map<String, Object>>();
		try {
			tables = jdbcOperate.queryMapList(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tables;
	}
	
	private List<Structure> getTableStructure(String table) {
		String sql = "show full columns from " + table;
		List<Structure> structures = new ArrayList<Structure>();
		try {
			List<Map<String, Object>> map = jdbcOperate.queryMapList(sql);
			Structure structure = null;
			for (Map<String, Object> node : map) {
				String stype = node.get("Type")+"";
				structure = new Structure();
				structure.setField(node.get("Field")+"");
				structure.setType(getType(stype.split("[(]")[0], "YES".equals(node.get("Null"))));
				structure.setLength(stype.split("[(]").length < 2 ? "" : stype.split("[(]")[1].replace(")", ""));
				structure.setIsNull(node.get("Null")+"");
				structure.setKey(node.get("Key")+"");
				structure.setDefaultValue(node.get("Default")== null ? "" : node.get("Default")+"");
				structure.setExtra(node.get("Extra")+"");
				structure.setComment(node.get("Comment")+"");
				structures.add(structure);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return structures;
	}
	
	private List<Index> getTableIndex(String table) {
		String sql = "show index from " + table;
		List<Index> indexs = new ArrayList<Index>();
		try {
			List<Map<String, Object>> map = jdbcOperate.queryMapList(sql);
			Index index = null;
			for (Map<String, Object> node : map) {
				index = new Index();
				index.setIndexName(node.get("Key_name")+"");
				index.setColumnName(node.get("Column_name")+"");
				index.setIndexType(node.get("Index_type")+"");
				index.setIsUnique(Tools.parseInt(node.get("Non_unique")+"") == 0 ? 1 : 0);
				indexs.add(index);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return indexs;
	}
	
	private String getType(String type, boolean isNull){
		String str = "";
		switch (type) {
		case "varchar":
			str = "String";
			break;
		case "char":
			str = "String";
			break;
		case "text":
			str = "String";
			break;
		case "int":
			str = isNull ? "int" : "int";
			break;
		case "tinyint":
			str = isNull ? "int" : "int";
			break;
		case "bigint":
			str = isNull ? "long" : "long";
			break;
		case "float":
			str = isNull ? "float" : "float";
			break;
		case "double":
			str = isNull ? "double" : "double";
			break;
		case "decimal":
			str = isNull ? "double" : "double";
			break;
		case "date":
			str = "Timestamp";
			break;
		case "datetime":
			str = "Timestamp";
			break;
		case "timestamp":
			str = "Timestamp";
			break;
		default:
			str = "String";
			break;
		}
		return str;
	}
	
	public static void main(String[] args) {
//		TableManager manager = new TableManager("db.properties");
//		Table table = manager.getDbTable("s_app");
//		System.out.println(JSON.toJSONString(table));
		
		String ename = "s_group_menu_btn".replace("s_", "");
		ename = ename.substring(0, 1).toUpperCase() + ename.substring(1);
		System.out.println(ename);
		int index = 0;
		while ((index = ename.indexOf("_")) != -1) {
			ename = ename.substring(0, index)+ename.substring(index+1, index+2).toUpperCase()+ename.substring(index+2);
		}
		System.out.println(ename);
		
		String[] tmp = "0001000100021001|90001|小小邮差Ⅰ|1-1N|夜战||".split("\\|", -1);
		System.out.println(tmp.length);
		
		String basePackge = "  com.auto.   ";
		basePackge = basePackge.trim();
		if(basePackge.endsWith(".")){
			basePackge = basePackge.substring(0, basePackge.length() - 1 );
		}
		System.out.println(basePackge);
		
		List<String> list = Stream.of("one", "two", "three", "four")
		 .filter(e -> e.length() > 3)
		 .peek(e -> System.out.println("Filtered value: " + e))
		 .map(String::toUpperCase)
		 .peek(e -> System.out.println("Mapped value: " + e))
		 .collect(Collectors.toList());
		System.out.println(list);

		System.out.println("1=1and ".toLowerCase().matches(Attacks.sqlAnd));
		
	}
	
}
