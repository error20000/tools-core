package com.jian.auto;

import java.util.List;

import com.jian.auto.db.Table;
import com.jian.auto.db.TableManager;
import com.jian.tools.core.DateTools;
import com.jian.tools.core.Tools;

/**
 * 自动生成管理器。需要配置的参数：
 * <p>{@code config} 数据库配置
 * <p>{@code manager} 数据库管理器
 * @author liujian
 *
 * @see com.jian.auto.ConfigDB
 * @see com.jian.auto.db.TableManager
 */
public abstract class AbstractAutoCreate implements AutoCreate {
	
	public TableManager manager = null;
	public ConfigDB config =  null;
	
	
	public void init() {
		initConfigDB();
		initTableManager();
	}

	public abstract void initConfigDB();
	
	public abstract void initTableManager();
	
	/**
	 * 整库创建
	 */
	@Override
	public void start(){
		init();
		if(manager == null){
			System.out.println(DateTools.formatDate()+":	没有配置数据库地址（dbPath）。。。。。。");
			return;
		}
		createBase();
		createTable();
	} 
	
	/**
	 * 整表创建
	 * @param tableName	表名
	 */
	@Override
	public void start(String tableName){
		init();
		if(manager == null){
			System.out.println(DateTools.formatDate()+":	没有配置数据库地址（dbPath）。。。。。。");
			return;
		}
		if(Tools.isNullOrEmpty(tableName)){
			System.out.println(DateTools.formatDate()+":	表名不能空。。。。。。");
			return;
		}
		createTable(tableName);
	}
	
	
	/**
	 * 创建基本信息
	 */
	public void createBase(){
		System.out.println(DateTools.formatDate()+":	start create base file...");
		createConfig();
		createUtils();
		createResources();
		createStart();
		createAspect();
		createException();
		createException();
		createResult();
		createHandle();
		
		createEntity(null);
		createDao(null);
		createDaoImpl(null);
		createDaoUtil(null);
		createService(null);
		createServiceImpl(null);
		createServlet(null);
		createController(null);
		
		createApi();
		System.out.println(DateTools.formatDate()+":	end create base file.");
	}
	
	/**
	 * 创建entity基本信息
	 */
	public void createTable(){
		if(manager == null){
			return;
		}
		//解析数据库
		System.out.println(DateTools.formatDate()+":	解析数据库...");
		List<Table> list = manager.getDbInfo();
		for (Table table : list) {
			String ename = table.getTableName();
			if(ename.startsWith(config.getPrefix())) {
				ename = ename.replaceFirst(config.getPrefix(), "");
			}
			ename = ename.substring(0, 1).toUpperCase() + ename.substring(1);
			if(!Tools.isNullOrEmpty(config.getSeparator())){
				int index = 0;
				while ((index = ename.indexOf(config.getSeparator())) != -1) {
					ename = ename.substring(0, index)+ename.substring(index+1, index+2).toUpperCase()+ename.substring(index+2);
				}
			}
			table.setEntityName(ename);
			doCreateTable(table);
		}
		System.out.println(DateTools.formatDate()+":	处理完毕...");
	}
	
	public void createTable(String tableName){
		if(manager == null){
			return;
		}
		if(Tools.isNullOrEmpty(tableName)){
			return;
		}
		//解析数据表
		System.out.println(DateTools.formatDate()+":	解析数据表...");
		Table table = manager.getDbTable(tableName);
		String ename = table.getTableName();
		if(ename.startsWith(config.getPrefix())) {
			ename = ename.replaceFirst(config.getPrefix(), "");
		}
		ename = ename.substring(0, 1).toUpperCase() + ename.substring(1);
		if(!Tools.isNullOrEmpty(config.getSeparator())){
			int index = 0;
			while ((index = ename.indexOf(config.getSeparator())) != -1) {
				ename = ename.substring(0, index)+ename.substring(index+1, index+2).toUpperCase()+ename.substring(index+2);
			}
		}
		table.setEntityName(ename);
		doCreateTable(table);
		System.out.println(DateTools.formatDate()+":	处理完毕...");
	}
	
	/**
	 * 
	 * @param table 表信息
	 */
	public void doCreateTable(Table table){
		System.out.println(DateTools.formatDate()+":	start create ..." + table.getEntityName());
		//创建entity
		createEntity(table);
		//创建dao
		createDao(table);
		createDaoImpl(table);
		createDB(table);
		//创建service
		createService(table);
		createServiceImpl(table);
		//创建servlet
		createServlet(table);
		//创建controller
		createController(table);
		System.out.println(DateTools.formatDate()+":	处理完毕..." + table.getEntityName());
	}
	
	/**
	 * 创建entity层接口
	 * @param table 数据表
	 */
	public abstract void createEntity(Table table);
	
	/**
	 * 创建dao层接口
	 * @param table 数据表
	 */
	public abstract void createDao(Table table);
	
	/**
	 * 创建dao层实现
	 * @param table 数据表
	 */
	public abstract void createDaoImpl(Table table);

	/**
	 * 创建dao层工具
	 * @param table 数据表
	 */
	public abstract void createDaoUtil(Table table);
	
	/**
	 * 注册dao层到DB。 例如：public static final UserDao USER_DAO = new UserDaoImpl();
	 * @param table 数据表
	 */
	public abstract void createDB(Table table);

	/**
	 * 创建service层接口
	 * @param table 数据表
	 */
	public abstract void createService(Table table);
	
	/**
	 * 创建service层实现
	 * @param table 数据表
	 */
	public abstract void createServiceImpl(Table table);

	/**
	 * 创建控制层，以Servlet模式
	 * @param table 数据表
	 */
	public abstract void createServlet(Table table);

	/**
	 * 创建控制层，以Controller模式
	 * @param table 数据表
	 */
	public abstract void createController(Table table);
	
	/**
	 * 创建配置文件
	 */
	public abstract void createConfig();
	
	/**
	 * 创建工具文件
	 */
	public abstract void createUtils();
	
	/**
	 * 创建资源文件
	 */
	public abstract void createResources();
	
	/**
	 * 开始文件
	 */
	public abstract void createStart();
	
	/**
	 * 接口文档
	 */
	public abstract void createApi();

	
	/**
	 * aop文件
	 */
	public abstract void createAspect();

	
	/**
	 * 异常处理文件
	 */
	public abstract void createException();

	
	/**
	 * 结果处理文件
	 */
	public abstract void createResult();

	
	/**
	 * 结果处理文件
	 */
	public abstract void createHandle();
}
