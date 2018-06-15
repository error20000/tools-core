package com.jian.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.jian.tools.core.Tools;


/**
 * jdbc 操作类
 * 		每个数据库操作函数 开启关闭一次连接. 使用:开始来标识参数, 例如: Map 和对象形式用":arg", List 和 Array 形式用":1"
 * 
 */
public class JdbcOperate {

	private DataSource	dataSource;
//	private Connection	connection; //Connection 不能公有。多线程、多任务，其中一个关闭了连接，剩余的会抛异常。所以注销。
	private boolean		isTrancation;

	/**
	 * 构造函数
	 * @param dataSource	数据源
	 */
	public JdbcOperate(DataSource dataSource) {
		this.dataSource = dataSource;
		this.isTrancation = false;
	}
	
	/**
	 * 构造函数
	 * @param dataSource	数据源
	 * @param isTrancation   是否启用事物支持
	 */
	public JdbcOperate(DataSource dataSource,boolean isTrancation){
		this.dataSource = dataSource;
		this.isTrancation = isTrancation;
	}

	/**
	 * 获取连接
	 * 
	 * @return 获取数据库连接
	 * @throws SQLException SQL 异常
	 */
	public Connection getConnection() throws SQLException {
		
		/*// 非事物模式，获取连接（因为关闭了连接）
		if (!isTrancation) {
			return dataSource.getConnection();
		} else {
			//如果连接不存在,或者连接已关闭则重取一个连接
			if (connection == null || connection.isClosed()) {
				connection = dataSource.getConnection();
				//如果是事务模式,则将自动提交设置为 false
				if (isTrancation) {
					connection.setAutoCommit(false);
				}
			}
			return connection;
		}*/
		
		Connection connection = dataSource.getConnection();
		//如果是事务模式,则将自动提交设置为 false
		if (isTrancation) {
			connection.setAutoCommit(false);
		}
		return connection;
	}

	/**
	 * 提交事物
	 * @param isClose 是否关闭数据库连接
	 * @throws SQLException SQL 异常
	 */
	public void commit(Connection connection, boolean isClose) throws SQLException{
		connection.commit();
		if(isClose) {
			closeConnection(connection);
		}
	}
	
	/**
	 * 回滚事物
	 * @param isClose 是否关闭数据库连接
	 * @throws SQLException SQL 异常
	 */
	public void rollback(Connection connection, boolean isClose) throws SQLException{
		connection.rollback();
		if(isClose) {
			closeConnection(connection);
		}
	}

	/**
	 * 提交事物不关闭连接
	 * @throws SQLException SQL 异常
	 */
	public void commit(Connection connection) throws SQLException{
		connection.commit();
	}

	/**
	 * 回滚事物不关闭连接
	 * @throws SQLException SQL 异常
	 */
	public void rollback(Connection connection) throws SQLException{
		connection.rollback();
	}
	
	/**
	 * 
	 * @param sqlText
	 *            sql字符串
	 * @param mapArg
	 *            参数
	 * @return 结果集信息
	 * @throws SQLException SQL 异常
	 */
	private ResultInfo baseQuery(String sqlText, Map<String, Object> mapArg) throws SQLException {
		Connection conn = getConnection();
		try {
			//构造PreparedStatement
			PreparedStatement preparedStatement = TranslateSQL.createPreparedStatement(conn, sqlText, mapArg);
			//执行查询
			ResultSet rs = preparedStatement.executeQuery();
			return new ResultInfo(rs, this.isTrancation);
		} catch (SQLException e) {
			closeConnection(conn);
			System.out.println("Query excution SQL Error! \n SQL is : \n\t" + sqlText + ": \n\t " + e.getMessage() + "\n");
		}
		return null;
	}

	/**
	 * 执行数据库更新
	 * 
	 * @param sqlText
	 *            sql字符串
	 * @param mapArg
	 *            参数
	 * @return 更新记录数
	 * @throws SQLException SQL 异常
	 */
	private int baseUpdate(String sqlText, Map<String, Object> mapArg) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = TranslateSQL.createPreparedStatement(conn, sqlText, mapArg);
			int stats = preparedStatement.executeUpdate();
			//注销返回自增id
			/*ResultSet rs=preparedStatement.getGeneratedKeys();
			if(rs.next()){
				System.out.println(rs.getObject(1));
			}
			System.out.println(sqlText);
			System.out.println(stats);*/
			return stats;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 非事物模式执行
			if (!isTrancation) {
				closeConnection(preparedStatement);
			}else{
				closeStatement(preparedStatement);
			}
		}
		return -1;
	}

	/**
	 * 执行数据库批量更新
	 * 
	 * @param sqlText
	 *            sql字符串
	 * @param mapArgs
	 *            参数
	 * @return 每条 SQL 更新记录数
	 * @throws SQLException SQL 异常
	 */
	private int[] baseBatch(String sqlText, List<Map<String, Object>> mapArgs) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement preparedStatement = null;
		try {

			// 非事物模式执行
			if (!isTrancation) {
				conn.setAutoCommit(false);
			}

			// 获取 SQL 中的参数列表
			List<String> sqlParams = TranslateSQL.getSqlParams(sqlText);
			preparedStatement = (PreparedStatement) conn.prepareStatement(TranslateSQL.preparedSql(sqlText));
			if (mapArgs != null) {
				for (Map<String, Object> magArg : mapArgs) {
					// 用 sqlParams 对照 给 preparestatement 填充参数
					TranslateSQL.setPreparedParams(preparedStatement, sqlParams, magArg);
					preparedStatement.addBatch();
				}
			}
			int[] result = preparedStatement.executeBatch();

			// 非事物模式执行
			if (!isTrancation) {
				conn.commit();
			} 

			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 非事物模式执行
			if (!isTrancation) {
				closeConnection(preparedStatement);
			} else {
				closeStatement(preparedStatement);
			}
		}
		return new int[0];
	}
	

	private List<Object> baseCall(String sqlText, CallType[] callTypes,Map<String, Object> mapArg) throws SQLException {
		Connection conn = getConnection();
		try {
			CallableStatement callableStatement = TranslateSQL.createCallableStatement(conn, sqlText, mapArg, callTypes);
			callableStatement.executeUpdate();
			List<Object> objList = TranslateSQL.getCallableStatementResult(callableStatement);
			return objList;
		} catch (SQLException e) {
			closeConnection(conn);
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 执行数据库更新
	 * 
	 * @param sqlText
	 *            sql字符串 参数使用":"作为标识,例如where id=:id
	 * @return 更新记录数
	 * @throws SQLException SQL 异常
	 */
	public int update(String sqlText) throws SQLException {
		return this.baseUpdate(sqlText, null);
	}

	/**
	 * 执行数据库更新,Object作为参数 字段名和对象属性名大消息必须大小写一致
	 * 
	 * @param sqlText
	 *            sql字符串 参数使用":"作为标识,例如where id=:id
	 * @param arg
	 *            object为参数的对象
	 * @return SQL 异常
	 * @throws ReflectiveOperationException 反射异常
	 * @throws SQLException SQL 异常
	 */
	public int update(String sqlText, Object arg) throws SQLException, ReflectiveOperationException {
		if(Tools.isBasicType(arg.getClass())){
			return update(sqlText, arg, null);
		}else{
			Map<String, Object> paramsMap = Tools.parseObjectToMap(arg);
			return this.baseUpdate(sqlText, paramsMap);
		}
	}

	/**
	 * 执行数据库更新,Map作为参数,字段名和Map键名大消息必须大小写一致
	 * 
	 * @param sqlText
	 *            sql字符串 参数使用":"作为标识,例如where id=:id
	 * @param mapArg
	 *            map为参数的对象
	 * @return 更新记录数
	 * @throws SQLException SQL 异常
	 */
	public int update(String sqlText, Map<String, Object> mapArg) throws SQLException {
		return this.baseUpdate(sqlText, mapArg);
	}

	/**
	 * 执行数据库更新,Map作为参数,字段名和Map键名大消息必须大小写一致
	 * 
	 * @param sqlText
	 *            sql字符串 参数使用":"作为索引标识,引导一个索引标识,索引标识从1开始,例如where id=:1
	 * @param args
	 *            多参数
	 * @return 更新记录数
	 * @throws SQLException  SQL 异常
	 */
	public int update(String sqlText, Object... args) throws SQLException {
		Map<String, Object> paramsMap = Tools.parseArrayToMap(args);
		return this.baseUpdate(sqlText, paramsMap);
	}

	/**
	 * 查询对象集合,无参数 字段名和对象属性名大消息必须大小写一致
	 * 
	 * @param <T> 范型
	 * 
	 * @param sqlText
	 *            sql字符串
	 * @param t
	 *            对象模型
	 * @return 结果集List
	 * @throws SQLException SQL 异常
	 */
	public <T> List<T> queryObjectList(String sqlText, Class<T> t) throws SQLException{
		ResultInfo resultInfo = this.baseQuery(sqlText, null);
		if(resultInfo!=null){
			return (List<T>) resultInfo.getObjectList(t);
		}
		return new ArrayList<T>();
		
	}

	/**
	 * 查询单个对象,Object作为参数 字段名和对象属性名大消息必须大小写一致
	 * 
	 * @param <T> 范型
	 *
	 * @param sqlText
	 *            sql字符串 参数使用":"作为标识,例如where id=:id
	 * @param arg
	 *            Object参数
	 * @param t
	 *            对象模型
	 * @return 结果集 List
	 * @throws ReflectiveOperationException  反射异常
	 * @throws SQLException  SQL 异常
	 */
	public <T> List<T> queryObjectList(String sqlText, Class<T> t, Object arg) throws SQLException, ReflectiveOperationException{
		if(Tools.isBasicType(arg.getClass())){
			return queryObjectList(sqlText, t, arg,null);
		}else{
			Map<String, Object> paramsMap = Tools.parseObjectToMap(arg);
			ResultInfo resultInfo = this.baseQuery(sqlText, paramsMap);
			if(resultInfo != null){
				return (List<T>) resultInfo.getObjectList(t);
			}
			return new ArrayList<T>();
		}
	}

	/**
	 * 查询对象集合,map作为参数,字段名和Map键名大消息必须大小写一致
	 * 
	 * @param <T>  范型
	 * 
	 * @param sqlText
	 *            sql字符串 参数使用":"作为标识,例如where id=:id
	 * @param mapArg
	 *            map参数
	 * @param t
	 *            对象模型
	 * @return  返回结果集 List
	 * @throws SQLException  SQL 异常
	 */
	public <T> List<T> queryObjectList(String sqlText, Class<T> t, Map<String, Object> mapArg) throws SQLException {
		ResultInfo resultInfo = this.baseQuery(sqlText, mapArg);
		if(resultInfo!=null){
			return (List<T>) resultInfo.getObjectList(t);
		}
		return new ArrayList<T>();
	}

	/**
	 * 查询对象集合,map作为参数,字段名和Map键名大消息必须大小写一致
	 * 
	 * @param <T> 范型
	 * 
	 * @param sqlText
	 *            sql字符串 参数使用":"作为索引标识,引导一个索引标识,索引标识从1开始,例如where id=:1
	 * @param args
	 *            map参数
	 * @param t
	 *            对象模型
	 * @return 返回结果集 List
	 * @throws SQLException SQL 异常
	 */
	public <T> List<T> queryObjectList(String sqlText, Class<T> t, Object... args) throws SQLException{
		Map<String, Object> paramsMap = Tools.parseArrayToMap(args);
		ResultInfo resultInfo = this.baseQuery(sqlText, paramsMap);
		if(resultInfo!=null){
			return (List<T>) resultInfo.getObjectList(t);
		}
		return new ArrayList<T>();
	}

	/**
	 * 查询对象集合,无参数
	 * 
	 * @param sqlText
	 *            sql字符串
	 * @return 返回结果集 List[Map]
	 * @throws SQLException   SQL 异常
	 */
	public List<Map<String, Object>> queryMapList(String sqlText) throws SQLException {
		ResultInfo resultInfo = this.baseQuery(sqlText, null);
		if(resultInfo!=null){
			return resultInfo.getMapList();
		}
		return new ArrayList<Map<String, Object>>();
	}

	/**
	 * 查询单个对象,Object作为参数
	 *
	 * @param sqlText
	 *            sql字符串 参数使用":"作为标识,例如where id=:id
	 * @param arg
	 *            Object参数
	 * @return 返回结果集 List[Map]
	 * @throws ReflectiveOperationException  反射异常
	 * @throws SQLException SQL 异常
	 */
	public List<Map<String, Object>> queryMapList(String sqlText, Object arg) throws SQLException, ReflectiveOperationException  {
		if(Tools.isBasicType(arg.getClass())){
			return queryMapList(sqlText, arg, null);
		}else{
			Map<String, Object> paramsMap = Tools.parseObjectToMap(arg);
			ResultInfo resultInfo = this.baseQuery(sqlText, paramsMap);
			if(resultInfo!=null){
				return resultInfo.getMapList();
			}
			return new ArrayList<Map<String, Object>>();
		}
	}

	/**
	 * 查询对象集合,map作为参数,字段名和Map键名大消息必须大小写一致
	 * 
	 * @param sqlText
	 *            sql字符串 参数使用":"作为标识,例如where id=:id
	 * @param mapArg
	 *            map参数
	 * @return 返回结果集 List[Map]
	 * @throws SQLException SQL 异常
	 */
	public List<Map<String, Object>> queryMapList(String sqlText, Map<String, Object> mapArg) throws SQLException{
		ResultInfo resultInfo = this.baseQuery(sqlText, mapArg);
		if(resultInfo!=null){
			return resultInfo.getMapList();
		}
		return new ArrayList<Map<String, Object>>();
	}

	/**
	 * 查询单个对象,Object作为参数
	 *
	 * @param sqlText
	 *            sql字符串 参数使用":"作为索引标识,引导一个索引标识,索引标识从1开始,例如where id=:1
	 * @param args
	 *            Object参数
	 * @return 返回结果集 List[Map]
	 * @throws SQLException SQL 异常
	 */
	public List<Map<String, Object>> queryMapList(String sqlText, Object... args) throws SQLException {
		Map<String, Object> paramsMap = Tools.parseArrayToMap(args);
		ResultInfo resultInfo = this.baseQuery(sqlText, paramsMap);
		if(resultInfo!=null){
			return resultInfo.getMapList();
		}
		return new ArrayList<Map<String, Object>>();
	}

	/**
	 * 查询单个对象,无参数 
	 *
	 * @param <T> 范型
	 *
	 * @param sqlText
	 *            sql字符串
	 * @param t
	 *            对象模型
	 * @return 返回结果集 List[Map]
	 * @throws SQLException SQL 异常
	 */
	@SuppressWarnings("unchecked")
	public <T> T queryObject(String sqlText, Class<T> t) throws SQLException {
		ResultInfo resultInfo = this.baseQuery(sqlText, null);
		if(resultInfo!=null){
			return (T) resultInfo.getObject(t);
		}
		return null;
	}

	/**
	 * 查询单个对象,Object作为参数 字段名和对象属性名大消息必须大小写一致
	 *
	 * @param <T> 范型
	 *
	 * @param sqlText
	 *            sql字符串 参数使用":"作为标识,例如where id=:id
	 * @param t
	 * 			  对象模型
	 * @param arg
	 *            Object参数
	 * @return 结果对象
	 * @throws ReflectiveOperationException 反射异常
	 * @throws SQLException SQL 异常
	 * @throws ParseException 解析异常
	 */
	@SuppressWarnings("unchecked")
	public <T> T queryObject(String sqlText, Class<T> t, Object arg) throws SQLException, ReflectiveOperationException, ParseException {
		if(Tools.isBasicType(arg.getClass())){
			return queryObject(sqlText,t,arg,null);
		}else{
			Map<String, Object> paramsMap = Tools.parseObjectToMap(arg);
			ResultInfo resultInfo = this.baseQuery(sqlText, paramsMap);
			if(resultInfo!=null){
				return (T) resultInfo.getObject(t);
			}
			return null;
		}
	}

	/**
	 * 查询单个对象,map作为参数,字段名和Map键名大消息必须大小写一致
	 *
	 * @param <T> 范型
	 *
	 * @param sqlText
	 *            sql字符串 参数使用":"作为标识,例如where id=:id
	 * @param mapArg
	 *            map参数
	 * @param t
	 *            对象模型
	 * @return 结果对象
	 * @throws SQLException  SQL 异常
	 */
	@SuppressWarnings("unchecked")
	public <T> T queryObject(String sqlText, Class<T> t, Map<String, Object> mapArg) throws SQLException {
		ResultInfo resultInfo = this.baseQuery(sqlText, mapArg);
		if(resultInfo!=null){
			return (T) resultInfo.getObject(t);
		}
		return null;
	}

	/**
	 * 查询单个对象,map作为参数,字段名和Map键名大消息必须大小写一致
	 *
	 * @param <T> 范型
	 *
	 * @param sqlText
	 *            sql字符串 参数使用":"作为索引标识,引导一个索引标识,索引标识从1开始,例如where id=:1
	 * @param args
	 *            map参数
	 * @param t
	 *            对象模型
	 * @return 结果对象
	 * @throws SQLException SQL 异常
	 */
	@SuppressWarnings("unchecked")
	public <T> T queryObject(String sqlText, Class<T> t, Object... args) throws SQLException{
		Map<String, Object> paramsMap = Tools.parseArrayToMap(args);
		ResultInfo resultInfo = this.baseQuery(sqlText, paramsMap);
		if(resultInfo!=null){
			return (T) resultInfo.getObject(t);
		}
		return null;
	}

	/**
	 * 查询单行,返回 Map,无参数
	 * 
	 * @param sqlText
	 *            sql字符串
	 * @return 结果对象 Map
	 * @throws SQLException SQL 异常
	 */
	public Map<String, Object> queryMap(String sqlText) throws SQLException {
		ResultInfo resultInfo = this.baseQuery(sqlText, null);
		if(resultInfo!=null){
			return resultInfo.getMap();
		}
		return null;
	}



	/**
	 * 查询单行,返回 Map,Object作为参数
	 * 
	 * @param sqlText
	 *            sql字符串 参数使用":"引导一个标识,例如where id=:id,中 id 就是标识。
	 * @param arg
	 *            arg参数 属性指代SQL字符串的标识,属性值用于在SQL字符串中替换标识。
	 * @return 结果对象 Map
	 * @throws SQLException SQL 异常
	 * @throws ReflectiveOperationException 反射异常
	 *
	 */
	public Map<String, Object> queryMap(String sqlText, Object arg) throws SQLException, ReflectiveOperationException {
		if(Tools.isBasicType(arg.getClass())){
			return queryMap(sqlText,arg,null);
		}else{
			Map<String, Object> paramsMap = Tools.parseObjectToMap(arg);
			ResultInfo resultInfo = this.baseQuery(sqlText, paramsMap);
			if(resultInfo!=null){
				return resultInfo.getMap();
			}
			return null;
		}
	}

	/**
	 * 查询单行,返回 Map,map作为参数,字段名和Map键名大消息必须大小写一致
	 * 
	 * @param sqlText
	 *            sql字符串 参数使用":"引导一个标识,例如where id=:id,中 id 就是标识。
	 * @param mapArg
	 *            map参数，key指代SQL字符串的标识,value用于在SQL字符串中替换标识。
	 * @return 结果对象 Map
	 * @throws SQLException SQL 异常
	 */
	public Map<String, Object> queryMap(String sqlText, Map<String, Object> mapArg) throws SQLException {
		ResultInfo resultInfo = this.baseQuery(sqlText, mapArg);
		if(resultInfo != null){
			return resultInfo.getMap();
		}
		return null;
	}

	/**
	 * 查询单行,返回 Map,Array作为参数
	 * 
	 * @param sqlText
	 *            sql字符串 参数使用":"引导一个索引标识,引导一个索引标识,索引标识从1开始,例如where id=:1
	 * @param args
	 *            args参数
	 * @return 结果对象
	 * @throws SQLException SQL 异常
	 */
	public Map<String, Object> queryMap(String sqlText, Object... args) throws SQLException {
		Map<String, Object> paramsMap = Tools.parseArrayToMap(args);
		ResultInfo resultInfo = this.baseQuery(sqlText, paramsMap);
		if(resultInfo!=null){
			return resultInfo.getMap();
		}
		return null;
	}

	/**
	 * 执行数据库批量更新 字段名和对象属性名大消息必须大小写一致
	 * 
	 * @param sqlText
	 *            sql字符串 参数使用":"作为标识,例如where id=:id
	 * @param objects
	 *            模型对象
	 * @return 每条 SQL 更新记录数
	 * @throws SQLException SQL 异常
	 * @throws ReflectiveOperationException 反射异常
	 */
	public int[] batchObject(String sqlText, List<?> objects) throws SQLException, ReflectiveOperationException {
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		for (Object object : objects) {
			mapList.add(Tools.parseObjectToMap(object));
		}
		return this.baseBatch(sqlText, mapList);
	}

	/**
	 * 执行数据库批量更新
	 * 
	 * @param sqlText sql字符串 参数使用":"作为标识,例如where id=:id
	 * @param maps 批量处理SQL的参数
	 * @return 每条 SQL 更新记录数
	 * @throws SQLException SQL 异常
	 */
	public int[] batchMap(String sqlText, List<Map<String, Object>> maps) throws SQLException {
		return this.baseBatch(sqlText, maps);
	}
	
	/**
	 * 执行数据库批量更新 只适用于单个字段，基础类型。
	 * 
	 * @param sqlText
	 *            sql字符串 参数使用":"作为标识,例如where id=:id
	 * @param args
	 *            基础类型 java.lang
	 * @return 每条 SQL 更新记录数
	 * @throws SQLException SQL 异常
	 * @throws ReflectiveOperationException 反射异常
	 */
	public int[] batchBasicType(String sqlText, List<?> args) throws SQLException, ReflectiveOperationException {
		Connection conn = getConnection();
		PreparedStatement preparedStatement = null;
		try {

			// 非事物模式执行
			if (!isTrancation) {
				conn.setAutoCommit(false);
			}

			// 获取 SQL 中的参数列表
			preparedStatement = (PreparedStatement) conn.prepareStatement(TranslateSQL.preparedSql(sqlText));
			if (args != null) {
				for (Object obj : args) {
					// 给 preparestatement 填充参数
					if(Tools.isBasicType(obj.getClass())){
						preparedStatement.setObject(1, obj);
						preparedStatement.addBatch();
					}
					
				}
			}
			int[] result = preparedStatement.executeBatch();

			// 非事物模式执行
			if (!isTrancation) {
				conn.commit();
			} 

			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 非事物模式执行
			if (!isTrancation) {
				closeConnection(preparedStatement);
			} else {
				closeStatement(preparedStatement);
			}
		}
		return new int[0];
	}
	
	/**
	 * 调用存储过程,无参数
	 * @param sqlText sql字符串 参数使用":"作为标识,例如where id=:id
	 * @return 调用结果
	 * @throws SQLException SQL 异常
	 */
	public List<Object> call(String sqlText) throws SQLException {
		return this.baseCall(sqlText,null,null);
	}
	
	/**
	 *  调用存储过程,map作为参数,字段名和Map键名大消息必须大小写一致
	 *            
	 * @param sqlText
	 * 				sql字符串 参数使用":"作为标识,例如where id=:id
	 * @param callTypes
	 * 				参数类型 IN,OUT,INOUT 可选
	 * @param maps
	 * 				map参数
	 * @return 调用结果
	 * @throws SQLException SQL 异常
	 */
	public List<Object> call(String sqlText, CallType[] callTypes, Map<String, Object> maps) throws SQLException {
		return this.baseCall(sqlText ,callTypes ,maps);
	}
	
	/**
	 *  调用存储过程,对象作为参数
	 *            
	 * @param sqlText
	 * 				sql字符串 参数使用":"作为标识,例如where id=:id
	 * @param callTypes
	 * 				参数类型 IN,OUT,INOUT 可选
	 * @param arg
	 * 				对象参数
	 * @return 调用结果
	 * @throws SQLException SQL 异常
	 * @throws ReflectiveOperationException 反射异常
	 */
	public List<Object> call(String sqlText, CallType[] callTypes, Object arg) throws SQLException, ReflectiveOperationException {
		if(Tools.isBasicType(arg.getClass())){
			return call(sqlText, callTypes, arg, null);
		}else{
			Map<String, Object> paramsMap  = Tools.parseObjectToMap(arg);
			return this.baseCall(sqlText,callTypes, paramsMap);
		}
		
	}
	
	/**
	 * 调用存储过程,Array作为参数
	 * @param sqlText
	 * 				sql字符串 参数使用":"作为标识,例如where id=:id
	 * @param callTypes
	 * 				参数类型 IN,OUT,INOUT 可选
	 * @param args
	 * 				多个连续参数
	 * @return 调用结果
	 * @throws SQLException SQL 异常
	 */
	public List<Object> call(String sqlText, CallType[] callTypes, Object ... args) throws SQLException {
		Map<String, Object> paramsMap  = Tools.parseArrayToMap(args);
		return this.baseCall(sqlText,callTypes, paramsMap);
	}
	

	/**
	 * 关闭连接
	 * @param resultSet 结果集
	 */
	protected static void closeConnection(ResultSet resultSet) {
		try {
			if (resultSet != null) {
				Statement statement = resultSet.getStatement();
				resultSet.close();
				closeConnection(statement);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 关闭连接
	 * @param statement Statement 对象
	 */
	protected static void closeConnection(Statement statement) {
		try {
			if(statement!=null) {
				Connection connection = statement.getConnection();
				statement.close();
				closeConnection(connection);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭连接
	 * @param connection 连接对象
	 */
	private static void closeConnection(Connection connection) {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭结果集
	 * @param resultSet 结果集
	 */
	protected static void closeResult(ResultSet resultSet){
		try {
            if(resultSet!=null) {
                Statement statement = resultSet.getStatement();
				resultSet.close();
                closeStatement(statement);
            }
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭 Statement
	 * @param statement Statement 对象
	 */
	protected static void closeStatement(Statement statement){
		try {
			if(statement!=null) {
				statement.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 返回事物支持状态
	 * 
	 */
	public boolean getTrancation(){
		return isTrancation;
	}
}
