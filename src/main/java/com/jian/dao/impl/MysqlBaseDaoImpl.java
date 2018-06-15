package com.jian.dao.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import com.jian.annotation.Column;
import com.jian.annotation.PrimaryKey;
import com.jian.annotation.PrimaryKeyCondition;
import com.jian.annotation.Table;
import com.jian.dao.MysqlBaseDao;
import com.jian.jdbc.JdbcOperate;
import com.jian.tools.core.JsonTools;
import com.jian.tools.core.LogsTools;
import com.jian.tools.core.Tools;

/**
 * 适用于mysql， 是{@code MysqlBaseDao} 基本实现，基于  {@code JdbcOperate} 的再封装
 * @author liujian
 *
 * @param <T>
 * @see com.jian.dao.MysqlBaseDao
 * @see com.tools.jdbc.JdbcOperate
 */
public abstract class MysqlBaseDaoImpl<T> implements MysqlBaseDao<T> {

	/**
	 * dataSource 事务处理相关（非必须），默认：null
	 */
	protected DataSource dataSource = null;
	/**
	 * jdbc 主要方法（必须）初始化，默认：null
	 */
	protected JdbcOperate jdbcOperate = null;
	/**
	 * 从库 dataSource 事务处理相关（非必须），默认：dataSource
	 */
	protected DataSource dataSourceSecond = null;
	/**
	 * 从库 jdbc 主要方法（从库必须）初始化，默认：jdbcOperate
	 */
	protected JdbcOperate jdbcOperateSecond = null;
	/**
	 * sql 日志打印（非必须），默认：false
	 */
	protected boolean log = false;
	
	private String logPath = "sql";
	private static Map<String, List<PrimaryKeyCondition>> pkArrays = new HashMap<String ,List<PrimaryKeyCondition>>();
	
	
	public MysqlBaseDaoImpl() {
		initJdbcOperate();
		//默认从库为主库
		if(dataSourceSecond == null){
			dataSourceSecond = dataSource;
		}
		if(jdbcOperateSecond == null){
			jdbcOperateSecond = jdbcOperate;
		}
	}
	
	public MysqlBaseDaoImpl(boolean log) {
		this();
		this.log = log;
	}

	public MysqlBaseDaoImpl(boolean log, String logPath) {
		this();
		this.log = log;
		this.logPath = logPath;
	}

	/**
	 * 必须初始化 {@code jdbcOperate}，其他根据需要选择。
	 * <p>如果需要使用事物，要初始化dataSource
	 */
	public abstract void initJdbcOperate();
	
	//TODO save
	/**
	 * 保存对象，返回int受影响条数。
	 * @param object 被保存对象
	 * @return int 1 成功，-1 失败
	 */
	@Override
	public int save(T object) {
		if(object == null){
			return 0;
		}
		String tableName = getTableName();
		return save(object, tableName);
	}
	
	/**
	 * 保存对象，返回int受影响条数。
	 * @param object 被保存对象。
	 * @param tableName 表名。
	 * @return int 1 成功，-1 失败。
	 */
	@Override
	public int save(T object, String tableName) {
		if(object == null){
			return 0;
		}
		List<PrimaryKeyCondition> pkeys = getPrimaryKeys(object.getClass());
		return save(object, pkeys, tableName);
	}

	/**
	 * 保存对象，返回int受影响条数。
	 * @param object 被保存对象。
	 * @param pkeys 对象主键信息，list集合。
	 * @param tableName 表名。
	 * @return int 1 成功，-1 失败。
	 */
	@Override
	public int save(T object, List<PrimaryKeyCondition> pkeys, String tableName) {
		int res = 0;
		if(object == null){
			return res;
		}
		if(Tools.isNullOrEmpty(tableName)){
			return res;
		}
		String sql = parseInsert(object, tableName);
		//解析主键
		parsePrimateKey(object, pkeys);
		
		//DEBUG
		debug(tableName, "SAVE SQL", sql);
		debug(tableName, "SAVE PARAMS", JsonTools.toJsonString(object));
		
		try {
			res = jdbcOperate.update(sql, object);
			//DEBUG
			debug(tableName, "SAVE RESULT", JsonTools.toJsonString(res));
		} catch (Exception e) {
			e.printStackTrace();
			debug(tableName, "SAVE ERROR", e.getMessage());
		}
		return res;
	}

	/**
	 * 批量保存对象，返回int总受影响条数。
	 * @param objects 被保存对象，list集合。
	 * @return int 1 成功，0 失败。
	 */
	@Override
	public int batchSave(List<T> objects) {
		if(objects == null || objects.size() == 0){
			return 0;
		}
		String tableName = getTableName();
		return batchSave(objects, tableName);
	}

	/**
	 * 批量保存对象，返回int总受影响条数。
	 * @param objects 被保存对象，list集合。
	 * @param tableName 表名。
	 * @return int 1 成功，0 失败。
	 */
	@Override
	public int batchSave(List<T> objects, String tableName) {
		if(objects == null || objects.size() == 0){
			return 0;
		}
		List<PrimaryKeyCondition> pkeys = getPrimaryKeys(objects.get(0).getClass());
		return batchSave(objects, pkeys, tableName);
	}

	/**
	 * 批量保存对象，返回int总受影响条数。
	 * @param objects 被保存对象，list集合。
	 * @param pkeys 对象主键信息，list集合。
	 * @param tableName 表名。
	 * @return int 1 成功，0 失败。
	 */
	@Override
	public int batchSave(List<T> objects, List<PrimaryKeyCondition> pkeys, String tableName) {
		int res = 0;
		if(objects == null || objects.size() == 0){
			return res;
		}
		if(Tools.isNullOrEmpty(tableName)){
			return res;
		}
		T object = objects.get(0);
		String sql = parseInsert(object, tableName);
		//解析主键
		for (T obj : objects) {
			parsePrimateKey(obj, pkeys);
		}
		
		//DEBUG
		debug(tableName, "BATCH SAVE SQL", sql);
		debug(tableName, "BATCH SAVE PARAMS", JsonTools.toJsonString(objects));
		try {
			int[] tmp = jdbcOperate.batchObject(sql, objects);
			for (int i : tmp) {
				res += i;
			}
			//DEBUG
			debug(tableName, "BATCH SAVE RESULT", JsonTools.toJsonString(res));
		} catch (Exception e) {
			e.printStackTrace();
			debug(tableName, "BATCH SAVE ERROR", e.getMessage());
		}
		return res;
	}
	
	private String parseInsert(T object, String tableName){
		String sql = "INSERT INTO `"+tableName+"` (SQLC) VALUES (SQLV)";
		String sqlC = "";
		String sqlV = "";
		Field[] fields = Tools.getFields(object.getClass());
		Method[] methods = Tools.getMethods(object.getClass());
		//遍历属性，只有有get/set方法的属性，才加入insert.
		for (Field f : fields) {
			String fname = f.getName();
			boolean get = false;
			boolean set = false;
			for (Method m : methods) {
				if(("get"+fname).equalsIgnoreCase(m.getName())){
					get = true;
				}
				if(("set"+fname).equalsIgnoreCase(m.getName())){
					set = true;
				}
			}
			if(get && set){
				String oname = getOtherName(f);
				sqlC += ",`"+oname+"`";
				sqlV += ",:"+fname;
			}
		}
		if(!Tools.isNullOrEmpty(sqlC)){
			sql = sql.replace("SQLC", sqlC.substring(1));
		}
		if(!Tools.isNullOrEmpty(sqlV)){
			sql = sql.replace("SQLV", sqlV.substring(1));
		}
		return sql;
	}
	
	private void parsePrimateKey(T object, List<PrimaryKeyCondition> pkeys){
		for (PrimaryKeyCondition node : pkeys) {
			switch (node.getKeyType()) {
			case UUID:
				//处理主键为uuid、并且String类型的
				if("String".equals(node.getType())){
					String nameGet = "get" + node.getField().substring(0, 1).toUpperCase() + node.getField().substring(1);
					Method methdGet = null;
					Object valueGet = null;
					String nameSet = "set" + node.getField().substring(0, 1).toUpperCase() + node.getField().substring(1);
					Method methdSet = null;
					try {
						methdGet  = Tools.findMethod(object.getClass(), nameGet, 0)[0];
						valueGet = methdGet.invoke(object);
						methdSet  = Tools.findMethod(object.getClass(), nameSet, 1)[0];
						//如果主键为空自动创建一个md5的uuid
						if(Tools.isNullOrEmpty(valueGet)){
							methdSet.invoke(object, Tools.md5(UUID.randomUUID().toString()));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
				
			case AUTO_INCREMENT:
				
				break;

			default:
				break;
			}
		}
	}

	//TODO modify
	/**
	 * 修改对象，返回int受影响条数。
	 * @param object 被修改对象。
	 * @return int 1 成功，0 失败
	 */
	@Override
	public int modify(T object) {
		if(object == null){
			return 0;
		}
		String tableName = getTableName();
		return modify(object, tableName);
	}
	
	/**
	 * 修改对象，返回int受影响条数。
	 * @param object 被修改对象。
	 * @param tableName 表名。
	 * @return int 1 成功，0 失败。
	 */
	@Override
	public int modify(T object, String tableName) {
		if(object == null){
			return 0;
		}
		List<PrimaryKeyCondition> pkeys = getPrimaryKeys(object.getClass());
		return modify(object, pkeys, tableName);
	}

	/**
	 * 修改对象，返回int受影响条数。
	 * @param object 被修改对象。
	 * @param pkeys 对象主键信息，list集合。
	 * @param tableName 表名。
	 * @return int 1 成功，0 失败。
	 */
	@Override
	public int modify(T object, List<PrimaryKeyCondition> pkeys, String tableName) {
		int res = 0;
		if(object == null){
			return res;
		}
		if(Tools.isNullOrEmpty(tableName)){
			return res;
		}
		String sql = parseUpdate(object, pkeys, tableName);
		//DEBUG
		debug(tableName, "MODIFY SQL", sql);
		debug(tableName, "MODIFY PARAMS", JsonTools.toJsonString(object));
		try {
			res = jdbcOperate.update(sql, object);
			//DEBUG
			debug(tableName, "MODIFY RESULT", JsonTools.toJsonString(res));
		} catch (Exception e) {
			e.printStackTrace();
			//DEBUG
			debug(tableName, "MODIFY ERROR",  e.getMessage());
		}
		return res;
	}
	
	/**
	 * 批量修改对象，返回int总受影响条数。
	 * @param objects 被修改对象，list集合
	 * @return int 1 成功，0 失败
	 */
	@Override
	public int batchModify(List<T> objects) {
		if(objects == null || objects.size() == 0){
			return 0;
		}
		String tableName = getTableName();
		return batchModify(objects, tableName);
	}
	
	/**
	 * 批量修改对象，返回int总受影响条数。
	 * @param objects 被修改对象，list集合。
	 * @param tableName 表名。
	 * @return int 1 成功，0 失败。
	 */
	@Override
	public int batchModify(List<T> objects, String tableName) {
		if(objects == null || objects.size() == 0){
			return 0;
		}
		List<PrimaryKeyCondition> pkeys = getPrimaryKeys(objects.get(0).getClass());
		return batchModify(objects, pkeys, tableName);
	}
	
	/**
	 * 批量修改对象，返回int总受影响条数。
	 * @param objects 被修改对象，list集合。
	 * @param pkeys 对象主键信息，list集合。
	 * @param tableName 表名。
	 * @return int 1 成功，0 失败。
	 */
	@Override
	public int batchModify(List<T> objects, List<PrimaryKeyCondition> pkeys, String tableName) {
		int res = 0;
		if(objects == null || objects.size() == 0){
			return res;
		}
		if(Tools.isNullOrEmpty(tableName)){
			return res;
		}
		T object = objects.get(0);
		String sql = parseUpdate(object, pkeys, tableName);
		//DEBUG
		debug(tableName, "BATCH MODIFY SQL", sql);
		debug(tableName, "BATCH MODIFY PARAMS", JsonTools.toJsonString(objects));
		try {
			int[] tmp = jdbcOperate.batchObject(sql, objects);
			for (int i : tmp) {
				res += i;
			}
			//DEBUG
			debug(tableName, "BATCH MODIFY RESULT", JsonTools.toJsonString(res));
		} catch (Exception e) {
			e.printStackTrace();
			//DEBUG
			debug(tableName, "BATCH MODIFY ERROR", e.getMessage());
		}
		return res;
	}
	
	private String parseUpdate(T object, List<PrimaryKeyCondition> pkeys, String tableName){
		String sql = "UPDATE `"+tableName+"` SET SQLS WHERE SQLW";
		String sqlS = "";
		String sqlW = "";
		Field[] fields = Tools.getFields(object.getClass());
		Method[] methods = Tools.getMethods(object.getClass());
		//遍历属性，只有有get/set方法的属性，才加入update.
		for (Field f : fields) {
			String fname = f.getName();
			boolean get = false;
			boolean set = false;
			for (Method m : methods) {
				if(("get"+fname).equalsIgnoreCase(m.getName())){
					get = true;
				}
				if(("set"+fname).equalsIgnoreCase(m.getName())){
					set = true;
				}
			}
			if(get && set){
				String oname = getOtherName(f);
				sqlS += ",`"+oname+"`=:"+fname;
			}
		}
		
		for (PrimaryKeyCondition condition : pkeys) {
			String fieldName = condition.getField();
			String otherName = getOtherName(fieldName);
			sqlW += " and `"+otherName+"`=:"+fieldName;
		}
		
		if(!Tools.isNullOrEmpty(sqlS)){
			sql = sql.replace("SQLS", sqlS.substring(1));
		}
		if(!Tools.isNullOrEmpty(sqlW)){
			sql = sql.replace("SQLW", sqlW.substring(" and ".length()));
		}else{
			sql = sql.replace("SQLW", "");
		}
		return sql;
	}

	/**
	 * 条件修改，返回int受影响条数。
	 * @param updateValue 修改值
	 * @param updateCondition 修改条件
	 * @return int 1 成功，0 失败
	 */
	@Override
	public int modify(Map<String, Object> updateValue, Map<String, Object> updateCondition) {
		//Type: MysqlBaseDaoImpl<T>, Class: MysqlBaseDaoImpl;
//		Type type = getClass().getGenericSuperclass();
////		Type[] params = ((ParameterizedType) type).getActualTypeArguments();
////		Class<?> clss = (Class<?>) params[0];
//		String tableName = "";
//		try {
//			Class<?>[] clsses = Tools.getGenericClass((ParameterizedType) type);
//			Class<?> clss = clsses[0];
//			if(clss.isAnnotationPresent(Table.class)){
//				tableName = clss.getAnnotation(Table.class).value();
//			}
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//			LogsTool.logSet(logPath+"/"+tableName, "【ERROR】exception: " + e.getMessage());
//		}
		String tableName = getTableName();
		return modify(updateValue, updateCondition, tableName);
	}

	/**
	 * 条件修改，返回int受影响条数。
	 * @param updateValue 修改值
	 * @param updateCondition 修改条件
	 * @param tableName 表名
	 * @return int 1 成功，0 失败
	 */
	@Override
	public int modify(Map<String, Object> updateValue, Map<String, Object> updateCondition, String tableName) {
		int res = 0;
		if(Tools.isNullOrEmpty(tableName)){
			return res;
		}
		if(updateValue == null || updateValue.isEmpty()){
			return res;
		}
		String sql = "UPDATE `"+tableName+"` SET SQLS WHERE SQLW";
		String sqlS = "";
		String sqlW = "";
		Map<String, Object> params = new HashMap<String, Object>();
		
		for (String key : updateValue.keySet()) {
			sqlS += ",`"+key+"`=:"+key;
			params.put(key, updateValue.get(key));
		}
		if(updateCondition != null){
			//key+"#"+key 是为了兼容  UPDATE table SET column=:column WHERE column=:column
			for (String key : updateCondition.keySet()) {
				sqlW += " and `"+key+"`=:"+key+"#"+key;
				params.put(key+"#"+key, updateCondition.get(key));
			}
		}
		if(!Tools.isNullOrEmpty(sqlS)){
			sql = sql.replace("SQLS", sqlS.substring(1));
		}
		if(!Tools.isNullOrEmpty(sqlW)){
			sql = sql.replace("SQLW", sqlW.substring(" and ".length()));
		}else{
			sql = sql.replace("SQLW", " 1 = 1 ");
		}
		//DEBUG
		debug(tableName, "CONDITION MODIFY SQL", sql);
		debug(tableName, "CONDITION MODIFY PARAMS", JsonTools.toJsonString(params));
		try {
			res = jdbcOperate.update(sql, params);
			//DEBUG
			debug(tableName, "CONDITION MODIFY RESULT", JsonTools.toJsonString(res));
		} catch (Exception e) {
			e.printStackTrace();
			//DEBUG
			debug(tableName, "CONDITION MODIFY ERROR", e.getMessage());
		}
		return res;
	}

	//TODO delete
	/**
	 * 条件删除，返回int受影响条数。
	 * @param deleteCondition 删除条件
	 * @return int 1 成功，0 失败
	 */
	@Override
	public int delete(Map<String, Object> deleteCondition) {
		String tableName = getTableName();
		return delete(deleteCondition, tableName);
	}

	/**
	 * 条件删除，返回int受影响条数。
	 * @param deleteCondition 删除条件
	 * @param tableName 表名
	 * @return int 1 成功，0 失败
	 */
	@Override
	public int delete(Map<String, Object> deleteCondition, String tableName) {
		int res = 0;
		if(Tools.isNullOrEmpty(tableName)){
			return res;
		}
		if(deleteCondition == null || deleteCondition.isEmpty()){
			return res;
		}
		String sql = "DELETE FROM `"+tableName+"` WHERE SQLW";
		String sqlW = "";
		
		for (String key : deleteCondition.keySet()) {
			sqlW += " and `"+key+"`=:"+key;
		}
		if(!Tools.isNullOrEmpty(sqlW)){
			sql = sql.replace("SQLW", sqlW.substring(" and ".length()));
		}
		//DEBUG
		debug(tableName, "DELETE SQL", sql);
		debug(tableName, "DELETE PARAMS", JsonTools.toJsonString(deleteCondition));
		try {
			res = jdbcOperate.update(sql, deleteCondition);
			//DEBUG
			debug(tableName, "DELETE RESULT", JsonTools.toJsonString(res));
		} catch (Exception e) {
			e.printStackTrace();
			//DEBUG
			debug(tableName, "DELETE ERROR", e.getMessage());
		}
		return res;
	}

	/**
	 * 条件删除，返回int受影响条数。
	 * @param wsql 删除条件的where语句，不包括where。如 id=:id
	 * @param deleteCondition 删除条件赋值，不参与拼接sql语句，不能为null。如 key：id，value：xxx
	 * @return int 1 成功，0 失败
	 */
	@Override
	public int delete(String wsql, Map<String, Object> deleteCondition) {
		String tableName = getTableName();
		return delete(wsql, deleteCondition, tableName);
	}

	/**
	 * 条件删除，返回int受影响条数。
	 * @param wsql 删除条件的where语句，不包括where。如 id=:id
	 * @param deleteCondition 删除条件赋值，不参与拼接sql语句，不能为null。如 key：id，value：xxx
	 * @param tableName 表名。
	 * @return int 1 成功，0 失败
	 */
	@Override
	public int delete(String wsql, Map<String, Object> deleteCondition, String tableName) {
		int res = 0;
		if(Tools.isNullOrEmpty(wsql)){
			return res;
		}
		if(Tools.isNullOrEmpty(tableName)){
			return res;
		}
		if(deleteCondition == null || deleteCondition.isEmpty()){
			return res;
		}
		String sql = "DELETE FROM `"+tableName+"` where " + wsql;
		
		//DEBUG
		debug(tableName, "WSQL DELETE SQL", sql);
		debug(tableName, "WSQL DELETE PARAMS", JsonTools.toJsonString(deleteCondition));
		try {
			res = jdbcOperate.update(sql, deleteCondition);
			//DEBUG
			debug(tableName, "WSQL DELETE RESULT", JsonTools.toJsonString(res));
		} catch (Exception e) {
			e.printStackTrace();
			//DEBUG
			debug(tableName, "WSQL DELETE ERROR", e.getMessage());
		}
		return res;
	}

	/**
	 * 批量删除，返回int受影响条数。
	 * @param column 数据表列名。如 id
	 * @param columnValues 值，list集合。
	 * @return int 1 成功，0 失败
	 */
	@Override
	public int batchDelete(String column, List<String> columnValues) {
		String tableName = getTableName();
		return batchDelete(column, columnValues, tableName);
	}

	/**
	 * 批量删除，返回int受影响条数。
	 * @param column 数据表列名。如 id
	 * @param columnValues 值，list集合。
	 * @param tableName 表名。
	 * @return int 1 成功，0 失败
	 */
	@Override
	public int batchDelete(String column, List<String> columnValues, String tableName) {
		int res = 0;
		if(Tools.isNullOrEmpty(column)){
			return res;
		}
		if(columnValues == null || columnValues.size() == 0){
			return res;
		}
		if(Tools.isNullOrEmpty(tableName)){
			return res;
		}
		String sql = "DELETE FROM `"+tableName+"` WHERE `"+column+"`=:" + column;
		//DEBUG
		debug(tableName, "BATCH DELETE SQL", sql);
		debug(tableName, "BATCH DELETE PARAMS", JsonTools.toJsonString(columnValues));
		try {
			int[] tmp = jdbcOperate.batchBasicType(sql, columnValues);
			for (int i : tmp) {
				res += i;
			}
			//DEBUG
			debug(tableName, "BATCH DELETE RESULT", JsonTools.toJsonString(res));
		} catch (Exception e) {
			e.printStackTrace();
			//DEBUG
			debug(tableName, "BATCH DELETE ERROR", e.getMessage());
		}
		return res;
	}
	

	//TODO find
	/**
	 * 查询数量，返回对象list集合。
	 * @return list 对象集合。
	 */
	@Override
	public List<T> findList() {
		String tableName = getTableName();
		return findList(tableName);
	}

	/**
	 * 查询数量，返回对象list集合。
	 * @param tableName 表名。
	 * @return list 对象集合。
	 */
	@Override
	public List<T> findList(String tableName) {
		return findList(0, -1, tableName);
	}

	/**
	 * 查询数量，返回对象list集合。
	 * @param start 起始条数。
	 * @param rows 查询条数。
	 * @return list 对象集合。
	 */
	@Override
	public List<T> findList(int start, int rows) {
		String tableName = getTableName();
		return findList(start, rows, tableName);
	}

	/**
	 * 查询数量，返回对象list集合。
	 * @param start 起始条数。
	 * @param rows 查询条数。
	 * @param tableName 表名。
	 * @return list 对象集合。
	 */
	@Override
	public List<T> findList(int start, int rows, String tableName) {
		return findList(new HashMap<String, Object>(), start, rows, tableName);
	}

	/**
	 * 查询数量，返回对象list集合。
	 * @param queryCondition 查询条件。
	 * @return list 对象集合。
	 */
	@Override
	public List<T> findList(Map<String, Object> queryCondition) {
		String tableName = getTableName();
		return findList(queryCondition, tableName);
	}

	/**
	 * 查询数量，返回对象list集合。
	 * @param queryCondition 查询条件。
	 * @param tableName 表名。
	 * @return list 对象集合。
	 */
	@Override
	public List<T> findList(Map<String, Object> queryCondition, String tableName) {
		return findList(queryCondition, 0, -1, tableName);
	}

	/**
	 * 查询数量，返回对象list集合。
	 * @param queryCondition 查询条件。
	 * @param start 起始条数。
	 * @param rows 查询条数。
	 * @return list 对象集合。
	 */
	@Override
	public List<T> findList(Map<String, Object> queryCondition, int start, int rows) {
		String tableName = getTableName();
		return findList(queryCondition, start, rows, tableName);
	}

	/**
	 * 查询数量，返回对象list集合。
	 * @param queryCondition 查询条件。
	 * @param start 起始条数。
	 * @param rows 查询条数。
	 * @param tableName 表名。
	 * @return list 对象集合。
	 */
	@Override
	public List<T> findList(Map<String, Object> queryCondition, int start, int rows, String tableName) {
		String wsql = " 1=1 ";
		for (String key : queryCondition.keySet()) {
			wsql += " and `"+key+"`=:"+key;
		}
		return findList(wsql, queryCondition, start, rows, tableName);
	}

	/**
	 * 查询数量，返回对象list集合。
	 * @param wsql 查询条件的where语句，不包括where。如 id=:id
	 * @param queryCondition 查询条件赋值，不参与拼接sql语句，不能为null。如 key：id，value：xxx
	 * @return list 对象集合。
	 */
	@Override
	public List<T> findList(String wsql, Map<String, Object> queryCondition) {
		String tableName = getTableName();
		return  findList(wsql, queryCondition, tableName);
	}

	/**
	 * 查询数量，返回对象list集合。
	 * @param wsql 查询条件的where语句，不包括where。如 id=:id
	 * @param queryCondition 查询条件赋值，不参与拼接sql语句，不能为null。如 key：id，value：xxx
	 * @param tableName 表名。
	 * @return list 对象集合。
	 */
	@Override
	public List<T> findList(String wsql, Map<String, Object> queryCondition, String tableName) {
		return findList(wsql, queryCondition, 0, -1, tableName);
	}

	/**
	 * 查询数量，返回对象list集合。
	 * @param wsql 查询条件的where语句，不包括where。如 id=:id
	 * @param queryCondition 查询条件赋值，不参与拼接sql语句，不能为null。如 key：id，value：xxx
	 * @param start 起始条数。
	 * @param rows 查询条数。
	 * @return list 对象集合。
	 */
	@Override
	public List<T> findList(String wsql, Map<String, Object> queryCondition, int start, int rows) {
		String tableName = getTableName();
		return findList(wsql, queryCondition, start, rows, tableName);
	}

	/**
	 * 查询数量，返回对象list集合。
	 * @param wsql 查询条件的where语句，不包括where。如 id=:id
	 * @param queryCondition 查询条件赋值，不参与拼接sql语句，不能为null。如 key：id，value：xxx。
	 * @param start 起始条数。
	 * @param rows 查询条数。
	 * @param tableName 表名。
	 * @return list 对象集合。
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<T> findList(String wsql, Map<String, Object> queryCondition, int start, int rows, String tableName) {
		List<T> res = new ArrayList<T>();
		if(Tools.isNullOrEmpty(tableName)){
			return res;
		}
		if(queryCondition == null){
			return res;
		}
		if(Tools.isNullOrEmpty(wsql)){
			return res;
		}
		String sql = "SELECT * FROM `"+tableName+"` WHERE " + wsql;
		//-1 不参与分页
		if(rows != -1){
			sql += " limit " + start + ", " + rows;
		}
		//DEBUG
		debug(tableName, "QUERY SQL", sql);
		debug(tableName, "QUERY PARAMS", JsonTools.toJsonString(queryCondition));
		try {
			Type type = getClass().getGenericSuperclass();
			Class<?>[] clsses = Tools.getGenericClass((ParameterizedType) type);
			Class<?> clss = clsses[0];
			res = (List<T>) jdbcOperateSecond.queryObjectList(sql, clss, queryCondition);
			//DEBUG
			debug(tableName, "QUERY RESULT", JsonTools.toJsonString(res));
		} catch (Exception e) {
			e.printStackTrace();
			//DEBUG
			debug(tableName, "QUERY ERROR", e.getMessage());
		}
		return res;
	}
	
	/**
	 * 查询对象，返回对象。
	 * @param queryCondition 查询条件。
	 * @return T 对象。
	 */
	@Override
	public T findObject(Map<String, Object> queryCondition) {
		String tableName = getTableName();
		return findObject(queryCondition, tableName);
	}
	
	/**
	 * 查询对象，返回对象。
	 * @param queryCondition 查询条件。
	 * @param tableName 表名。
	 * @return T 对象。
	 */
	@Override
	public T findObject(Map<String, Object> queryCondition, String tableName) {
		String wsql = " 1=1 ";
		for (String key : queryCondition.keySet()) {
			wsql += " and `"+getOtherName(key)+"`=:"+key;
		}
		return findObject(wsql, queryCondition, tableName);
	}
	
	/**
	 * 查询对象，返回对象。
	 * @param wsql 查询条件的where语句，不包括where。如 id=:id
	 * @param queryCondition 查询条件赋值，不参与拼接sql语句，不能为null。如 key：id，value：xxx
	 * @return T 对象。
	 */
	@Override
	public T findObject(String wsql, Map<String, Object> queryCondition) {
		String tableName = getTableName();
		return findObject(wsql, queryCondition, tableName);
	}

	/**
	 * 查询对象，返回对象。
	 * @param wsql 查询条件的where语句，不包括where。如 id=:id
	 * @param queryCondition 查询条件赋值，不参与拼接sql语句，不能为null。如 key：id，value：xxx
	 * @param tableName 表名。
	 * @return T 对象。
	 */
	@Override
	@SuppressWarnings("unchecked")
	public T findObject(String wsql, Map<String, Object> queryCondition, String tableName) {
		if(Tools.isNullOrEmpty(tableName)){
			return null;
		}
		if(queryCondition == null){
			return null;
		}
		if(Tools.isNullOrEmpty(wsql)){
			return null;
		}
		String sql = "SELECT * FROM `"+tableName+"` WHERE " + wsql;
		
		//DEBUG
		debug(tableName, "QUERY SQL", sql);
		debug(tableName, "QUERY PARAMS", JsonTools.toJsonString(queryCondition));
		try {
			Type type = getClass().getGenericSuperclass();
			Class<?>[] clsses = Tools.getGenericClass((ParameterizedType) type);
			Class<?> clss = clsses[0];
			T res = (T) jdbcOperateSecond.queryObject(sql, clss, queryCondition);
			//DEBUG
			debug(tableName, "QUERY RESULT", JsonTools.toJsonString(res));
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			//DEBUG
			debug(tableName, "QUERY ERROR", e.getMessage());
		}
		return null;
	}
	
	/**
	 * 查询数量，返回maplist集合。
	 * @param columns 需要返回的列名，默认*。 如 id、name、count(1)
	 * @param queryCondition 查询条件.
	 * @return list maplist集合。
	 */
	@Override
	public List<Map<String, Object>> findMapList(List<String> columns, Map<String, Object> queryCondition) {
		String tableName = getTableName();
		return findMapList(columns, queryCondition, tableName);
	}
	
	/**
	 * 查询数量，返回maplist集合。
	 * @param columns 需要返回的列名，默认*。 如 id、name、count(1)
	 * @param queryCondition 查询条件.
	 * @param tableName 表名。
	 * @return list maplist集合。
	 */
	@Override
	public List<Map<String, Object>> findMapList(List<String> columns, Map<String, Object> queryCondition, String tableName) {
		return findMapList(columns, queryCondition, 0, -1, tableName);
	}
	
	/**
	 * 查询数量，返回maplist集合。
	 * @param columns 需要返回的列名，默认*。 如 id、name、count(1)
	 * @param queryCondition 查询条件.
	 * @param start 起始条数。
	 * @param rows 查询条数。
	 * @return list maplist集合。
	 */
	@Override
	public List<Map<String, Object>> findMapList(List<String> columns, Map<String, Object> queryCondition, int start, int rows) {
		String tableName = getTableName();
		return findMapList(columns, queryCondition, start, rows, tableName);
	}

	/**
	 * 查询数量，返回maplist集合。
	 * @param columns 需要返回的列名，默认*。 如 id、name、count(1)
	 * @param queryCondition 查询条件.
	 * @param start 起始条数。
	 * @param rows 查询条数。
	 * @param tableName 表名。
	 * @return list maplist集合。
	 */
	@Override
	public List<Map<String, Object>> findMapList(List<String> columns, Map<String, Object> queryCondition, int start, int rows, String tableName) {
		String wsql = " 1=1 ";
		for (String key : queryCondition.keySet()) {
			wsql += " and `"+key+"`=:"+key;
		}
		return findMapList(columns, wsql, queryCondition, start, rows, tableName);
	}

	/**
	 * 查询数量，返回maplist集合。
	 * @param columns 需要返回的列名，默认*。 如 id、name、count(1)
	 * @param wsql 查询条件的where语句，不包括where。如 id=:id
	 * @param queryCondition 查询条件赋值，不参与拼接sql语句，不能为null。如 key：id，value：xxx。
	 * @return list maplist集合。
	 */
	@Override
	public List<Map<String, Object>> findMapList(List<String> columns, String wsql, Map<String, Object> queryCondition) {
		String tableName = getTableName();
		return findMapList(columns, wsql, queryCondition, tableName);
	}

	/**
	 * 查询数量，返回maplist集合。
	 * @param columns 需要返回的列名，默认*。 如 id、name、count(1)
	 * @param wsql 查询条件的where语句，不包括where。如 id=:id
	 * @param queryCondition 查询条件赋值，不参与拼接sql语句，不能为null。如 key：id，value：xxx。
	 * @param tableName 表名。
	 * @return list maplist集合。
	 */
	@Override
	public List<Map<String, Object>> findMapList(List<String> columns, String wsql, Map<String, Object> queryCondition, String tableName) {
		return findMapList(columns, wsql, queryCondition, 0, -1, tableName);
	}

	/**
	 * 查询数量，返回maplist集合。
	 * @param columns 需要返回的列名，默认*。 如 id、name、count(1)
	 * @param wsql 查询条件的where语句，不包括where。如 id=:id
	 * @param queryCondition 查询条件赋值，不参与拼接sql语句，不能为null。如 key：id，value：xxx。
	 * @param start 起始条数。
	 * @param rows 查询条数。
	 * @return list maplist集合。
	 */
	@Override
	public List<Map<String, Object>> findMapList(List<String> columns, String wsql, Map<String, Object> queryCondition,int start, int rows) {
		String tableName = getTableName();
		return findMapList(columns, wsql, queryCondition, start, rows, tableName);
	}

	/**
	 * 查询数量，返回maplist集合。
	 * @param columns 需要返回的列名，默认*。 如 id、name、count(1)
	 * @param wsql 查询条件的where语句，不包括where。如 id=:id
	 * @param queryCondition 查询条件赋值，不参与拼接sql语句，不能为null。如 key：id，value：xxx。
	 * @param start 起始条数。
	 * @param rows 查询条数。
	 * @param tableName 表名。
	 * @return list maplist集合。
	 */
	@Override
	public List<Map<String, Object>> findMapList(List<String> columns, String wsql, Map<String, Object> queryCondition, int start, int rows, String tableName) {
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		if(Tools.isNullOrEmpty(tableName)){
			return res;
		}
		if(queryCondition == null){
			return res;
		}
		if(Tools.isNullOrEmpty(wsql)){
			return res;
		}
		String sql = "SELECT FIELDS FROM `"+tableName+"` WHERE " + wsql;
		String fields = "";
		if(columns != null){
			for (String str : columns) {
				fields += ","+str;
			}
		}
		if(!Tools.isNullOrEmpty(fields)){
			sql = sql.replace("FIELDS", fields.substring(1));
		}else{
			sql = sql.replace("FIELDS", "*");
		}
		//-1 不参与分页
		if(rows != -1){
			sql += " limit " + start + ", " + rows;
		}
		
		//DEBUG
		debug(tableName, "QUERY SQL", sql);
		debug(tableName, "QUERY PARAMS", JsonTools.toJsonString(queryCondition));
		try {
			res = jdbcOperateSecond.queryMapList(sql, queryCondition);
		} catch (Exception e) {
			e.printStackTrace();
			//DEBUG
			debug(tableName, "QUERY ERROR", e.getMessage());
		}
		return res;
	}

	/**
	 * 查询对象，返回map。
	 * @param columns 需要返回的列名，默认*。 如 id、name、count(1)
	 * @param queryCondition 查询条件。
	 * @return map map对象。
	 */
	@Override
	public Map<String, Object> findMap(List<String> columns, Map<String, Object> queryCondition) {
		String tableName = getTableName();
		return findMap(columns, queryCondition, tableName);
	}

	/**
	 * 查询对象，返回map。
	 * @param columns 需要返回的列名，默认*。 如 id、name、count(1)
	 * @param queryCondition 查询条件。
	 * @param tableName 表名。
	 * @return map map对象。
	 */
	@Override
	public Map<String, Object> findMap(List<String> columns, Map<String, Object> queryCondition, String tableName) {
		String wsql = " 1=1 ";
		for (String key : queryCondition.keySet()) {
			wsql += " and `"+key+"`=:"+key;
		}
		return findMap(columns, wsql, queryCondition, tableName);
	}
	
	/**
	 * 查询对象，返回map。
	 * @param columns 需要返回的列名，默认*。 如 id、name、count(1)
	 * @param wsql 查询条件的where语句，不包括where。如 id=:id
	 * @param queryCondition 查询条件赋值，不参与拼接sql语句，不能为null。如 key：id，value：xxx。
	 * @return map map对象。
	 */
	@Override
	public Map<String, Object> findMap(List<String> columns, String wsql, Map<String, Object> queryCondition) {
		String tableName = getTableName();
		return findMap(columns, wsql, queryCondition, tableName);
	}

	/**
	 * 查询对象，返回map。
	 * @param columns 需要返回的列名，默认*。 如 id、name、count(1)
	 * @param wsql 查询条件的where语句，不包括where。如 id=:id
	 * @param queryCondition 查询条件赋值，不参与拼接sql语句，不能为null。如 key：id，value：xxx。
	 * @param tableName 表名。
	 * @return map map对象。
	 */
	@Override
	public Map<String, Object> findMap(List<String> columns, String wsql, Map<String, Object> queryCondition, String tableName) {
		if(Tools.isNullOrEmpty(tableName)){
			return null;
		}
		if(queryCondition == null){
			return null;
		}
		if(Tools.isNullOrEmpty(wsql)){
			return null;
		}
		String sql = "SELECT FIELDS FROM `" + tableName + "` WHERE " + wsql;
		String fields = "";
		if(columns != null){
			for (String str : columns) {
				fields += ","+str;
			}
		}
		if(!Tools.isNullOrEmpty(fields)){
			sql = sql.replace("FIELDS", fields.substring(1));
		}else{
			sql = sql.replace("FIELDS", "*");
		}
		
		//DEBUG
		debug(tableName, "QUERY SQL", sql);
		debug(tableName, "QUERY PARAMS", JsonTools.toJsonString(queryCondition));
		try {
			Map<String, Object> res = jdbcOperateSecond.queryMap(sql, queryCondition);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			//DEBUG
			debug(tableName, "QUERY ERROR", e.getMessage());
		}
		return null;
	}

	//TODO size
	/**
	 * 查询条数，返回long总条数。
	 * @return long 总条数。
	 */
	@Override
	public long size() {
		String tableName = getTableName();
		return size(tableName);
	}

	/**
	 * 查询条数，返回long总条数。
	 * @param tableName 表名。
	 * @return long 总条数。
	 */
	@Override
	public long size(String tableName) {
		return size(new HashMap<String, Object>(), tableName);
	}

	/**
	 * 查询条数，返回long总条数。
	 * @param queryCondition 查询条件。
	 * @return long 总条数。
	 */
	@Override
	public long size(Map<String, Object> queryCondition) {
		String tableName = getTableName();
		return size(queryCondition, tableName);
	}

	/**
	 * 查询条数，返回long总条数。
	 * @param queryCondition 查询条件。
	 * @param tableName 表名。
	 * @return long 总条数。
	 */
	@Override
	public long size(Map<String, Object> queryCondition, String tableName) {
		String wsql = " 1=1 ";
		for (String key : queryCondition.keySet()) {
			wsql += " and `"+key+"`=:"+key;
		}
		return size(wsql, queryCondition, tableName);
	}

	/**
	 * 查询条数，返回long总条数。
	 * @param wsql 查询条件的where语句，不包括where。如 id=:id
	 * @param queryCondition 查询条件赋值，不参与拼接sql语句，不能为null。如 key：id，value：xxx
	 * @return long 总条数。
	 */
	@Override
	public long size(String wsql, Map<String, Object> queryCondition) {
		String tableName = getTableName();
		return size(wsql, queryCondition, tableName);
	}

	/**
	 * 查询条数，返回long总条数。
	 * @param wsql 查询条件的where语句，不包括where。如 id=:id
	 * @param queryCondition 查询条件赋值，不参与拼接sql语句，不能为null。如 key：id，value：xxx
	 * @param tableName 表名
	 * @return long 总条数。
	 */
	@Override
	public long size(String wsql, Map<String, Object> queryCondition, String tableName) {
		long res = 0;
		if(Tools.isNullOrEmpty(tableName)){
			return res;
		}
		if(queryCondition == null){
			return res;
		}
		if(Tools.isNullOrEmpty(wsql)){
			return res;
		}
		String sql = "SELECT count(1) FROM `"+tableName+"` WHERE " + wsql;
		//DEBUG
		debug(tableName, "SIZE SQL", sql);
		debug(tableName, "SIZE PARAMS", JsonTools.toJsonString(queryCondition));
		try {
			res = jdbcOperateSecond.queryObject(sql, Long.class, queryCondition);
		} catch (Exception e) {
			e.printStackTrace();
			debug(tableName, "SIZE ERROR", e.getMessage());
		}
		return res;
	}
	
	//获取泛型的Class类型。
	private Class<?> getObejctClass(){
		Type type = getClass().getGenericSuperclass();
		Class<?> clss = null;
		try {
			Class<?>[] clsses = Tools.getGenericClass((ParameterizedType) type);
			clss = clsses[0];
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return clss;
	}
	
	//获取泛型注解的table name。
	private String getTableName(){
		String tableName = "";
		Class<?> clss = getObejctClass();
		if(clss != null){
			if(clss.isAnnotationPresent(Table.class)){
				tableName = clss.getAnnotation(Table.class).value();
			}
		}
		return tableName;
	}
	
	//获取Entity类注解的PrimaryKey。
	private  List<PrimaryKeyCondition> getPrimaryKeys(Class<?> clss){
		//弃用
		/*List<PrimaryKeyCondition> list = new ArrayList<PrimaryKeyCondition>();
		Field[] fields = Tools.getFields(clss);
		for (Field f : fields) {
			if(f.isAnnotationPresent(PrimaryKey.class)){
				PrimaryKeyCondition node = new PrimaryKeyCondition();
				node.setField(f.getName());
				node.setType(f.getType().getSimpleName());
				node.setKeyType(f.getAnnotation(PrimaryKey.class).type());
				list.add(node);
			}
		}
		return list;*/
		String mark = clss.getCanonicalName();
		List<PrimaryKeyCondition> list = null;
		if(pkArrays.containsKey(mark)){
			list = pkArrays.get(mark);
		}else {
			list = new ArrayList<PrimaryKeyCondition>();
			Field[] fields = Tools.getFields(clss);
			for (Field f : fields) {
				if(f.isAnnotationPresent(PrimaryKey.class)){
					PrimaryKeyCondition node = new PrimaryKeyCondition();
					node.setField(f.getName());
					node.setType(f.getType().getSimpleName());
					node.setKeyType(f.getAnnotation(PrimaryKey.class).type());
					list.add(node);
				}
			}
			pkArrays.put(mark, list);
		}
		return list;
	}

	//获取泛型注解的字段别名。
	private String getOtherName(String fieldName){
		String value = fieldName;
		try {
			Field f = Tools.findField(getObejctClass(), fieldName);
			if(f != null){
				//获取字段别名，如果注解PrimaryKey、Column都有，取Column注解。
    			if(f.isAnnotationPresent(Column.class)){
    				value = f.getAnnotation(Column.class).value();
				}else if(f.isAnnotationPresent(PrimaryKey.class)){
    				value = f.getAnnotation(PrimaryKey.class).value();
				}
    			value = Tools.isNullOrEmpty(value) ? fieldName : value;
			}
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
		return value;
	}
	
	//获取泛型注解的字段别名。
	private String getOtherName(Field f){
		String value = "";
		if(f != null){
			value = f.getName();
			//获取字段别名，如果注解PrimaryKey、Column都有，取Column注解。
			if(f.isAnnotationPresent(Column.class)){
				value = f.getAnnotation(Column.class).value();
			}else if(f.isAnnotationPresent(PrimaryKey.class)){
				value = f.getAnnotation(PrimaryKey.class).value();
			}
			value = Tools.isNullOrEmpty(value) ? f.getName() : value;
		}
		return value;
	}

	/**
	 * DEBUG
	 * @param tableName
	 * @param name
	 * @param content
	 */
	private void debug(String tableName, String name, String content){
		if(log){
			LogsTools.logSet(logPath+"/"+tableName, "【"+name+"】  -- " + content);
		}
	}
}
