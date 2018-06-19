package com.jian.service.impl;

import java.util.List;
import java.util.Map;

import com.jian.annotation.PrimaryKeyCondition;
import com.jian.dao.AbstractBaseDao;
import com.jian.service.AbstractBaseService;


/**
 * service层基础接口
 * @author liujian
 *
 * @param <T>
 * @see com.tools.db.dao.AbstractBaseDao
 */
public abstract class AbstractBaseServiceImpl<T> implements AbstractBaseService<T> {

	protected AbstractBaseDao<T> baseDao;
	
	public AbstractBaseServiceImpl() {
		initBaseDao();
	}
	

	/**
	 * 如果需要使用事物，要初始化{@code AbstractBaseDao}
	 */
	public abstract void initBaseDao();

	public int save(T object) {
		return baseDao.save(object);
	}

	public int save(T object, String tableName) {
		return baseDao.save(object, tableName);
	}

	public int save(T object, List<PrimaryKeyCondition> pkeys, String tableName) {
		return baseDao.save(object, pkeys, tableName);
	}

	public int batchSave(List<T> objects) {
		return baseDao.batchSave(objects);
	}

	public int batchSave(List<T> objects, String tableName) {
		return baseDao.batchSave(objects, tableName);
	}

	public int batchSave(List<T> objects, List<PrimaryKeyCondition> pkeys, String tableName) {
		return baseDao.batchSave(objects, pkeys, tableName);
	}

	public int modify(T object) {
		return baseDao.modify(object);
	}

	public int modify(T object, String tableName) {
		return baseDao.modify(object, tableName);
	}

	public int modify(T object, List<PrimaryKeyCondition> pkeys, String tableName) {
		return baseDao.modify(object, pkeys, tableName);
	}

	public int batchModify(List<T> objects) {
		return baseDao.batchModify(objects);
	}

	public int batchModify(List<T> objects, String tableName) {
		return baseDao.batchModify(objects, tableName);
	}

	public int batchModify(List<T> objects, List<PrimaryKeyCondition> pkeys, String tableName) {
		return baseDao.batchModify(objects, pkeys, tableName);
	}

	public int modify(Map<String, Object> updateValue, Map<String, Object> updateCondition) {
		return baseDao.modify(updateValue, updateCondition);
	}

	public int modify(Map<String, Object> updateValue, Map<String, Object> updateCondition, String tableName) {
		return baseDao.modify(updateValue, updateCondition, tableName);
	}

	public int delete(Map<String, Object> deleteCondition) {
		return baseDao.delete(deleteCondition);
	}

	public int delete(Map<String, Object> deleteCondition, String tableName) {
		return baseDao.delete(deleteCondition, tableName);
	}

	public int delete(String wsql, Map<String, Object> deleteCondition) {
		return baseDao.delete(wsql, deleteCondition);
	}

	public int delete(String wsql, Map<String, Object> deleteCondition, String tableName) {
		return baseDao.delete(wsql, deleteCondition, tableName);
	}

	public int batchDelete(String column, List<String> columnValues) {
		return baseDao.batchDelete(column, columnValues);
	}

	public int batchDelete(String column, List<String> columnValues, String tableName) {
		return baseDao.batchDelete(column, columnValues, tableName);
	}

	public List<T> findList() {
		return baseDao.findList();
	}

	public List<T> findList(String tableName) {
		return baseDao.findList(tableName);
	}

	public List<T> findList(int start, int rows) {
		return baseDao.findList(start, rows);
	}

	public List<T> findList(int start, int rows, String tableName) {
		return baseDao.findList(start, rows, tableName);
	}

	public List<T> findList(Map<String, Object> queryCondition) {
		return baseDao.findList(queryCondition);
	}

	public List<T> findList(Map<String, Object> queryCondition, String tableName) {
		return baseDao.findList(queryCondition, tableName);
	}

	public List<T> findList(Map<String, Object> queryCondition, int start, int rows) {
		return baseDao.findList(queryCondition, start, rows);
	}

	public List<T> findList(Map<String, Object> queryCondition, int start, int rows, String tableName) {
		return baseDao.findList(queryCondition, start, rows, tableName);
	}

	public List<T> findList(String wsql, Map<String, Object> queryCondition) {
		return baseDao.findList(wsql, queryCondition);
	}

	public List<T> findList(String wsql, Map<String, Object> queryCondition, String tableName) {
		return baseDao.findList(wsql, queryCondition, tableName);
	}

	public List<T> findList(String wsql, Map<String, Object> queryCondition, int start, int rows) {
		return baseDao.findList(wsql, queryCondition, start, rows);
	}

	public List<T> findList(String wsql, Map<String, Object> queryCondition, int start, int rows, String tableName) {
		return baseDao.findList(wsql, queryCondition, start, rows, tableName);
	}

	public T findObject(Map<String, Object> queryCondition) {
		return baseDao.findObject(queryCondition);
	}

	public T findObject(Map<String, Object> queryCondition, String tableName) {
		return baseDao.findObject(queryCondition, tableName);
	}

	public T findObject(String wsql, Map<String, Object> queryCondition) {
		return baseDao.findObject(wsql, queryCondition);
	}

	public T findObject(String wsql, Map<String, Object> queryCondition, String tableName) {
		return baseDao.findObject(wsql, queryCondition, tableName);
	}

	public List<Map<String, Object>> findMapList(List<String> columns, Map<String, Object> queryCondition) {
		return baseDao.findMapList(columns, queryCondition);
	}

	public List<Map<String, Object>> findMapList(List<String> columns, Map<String, Object> queryCondition,
			String tableName) {
		return baseDao.findMapList(columns, queryCondition, tableName);
	}

	public List<Map<String, Object>> findMapList(List<String> columns, Map<String, Object> queryCondition, int start,
			int rows) {
		return baseDao.findMapList(columns, queryCondition, start, rows);
	}

	public List<Map<String, Object>> findMapList(List<String> columns, Map<String, Object> queryCondition, int start,
			int rows, String tableName) {
		return baseDao.findMapList(columns, queryCondition, start, rows, tableName);
	}

	public List<Map<String, Object>> findMapList(List<String> columns, String wsql,
			Map<String, Object> queryCondition) {
		return baseDao.findMapList(columns, wsql, queryCondition);
	}

	public List<Map<String, Object>> findMapList(List<String> columns, String wsql, Map<String, Object> queryCondition,
			String tableName) {
		return baseDao.findMapList(columns, wsql, queryCondition, tableName);
	}

	public List<Map<String, Object>> findMapList(List<String> columns, String wsql, Map<String, Object> queryCondition,
			int start, int rows) {
		return baseDao.findMapList(columns, wsql, queryCondition, start, rows);
	}

	public List<Map<String, Object>> findMapList(List<String> columns, String wsql, Map<String, Object> queryCondition,
			int start, int rows, String tableName) {
		return baseDao.findMapList(columns, wsql, queryCondition, start, rows, tableName);
	}

	public Map<String, Object> findMap(List<String> columns, Map<String, Object> queryCondition) {
		return baseDao.findMap(columns, queryCondition);
	}

	public Map<String, Object> findMap(List<String> columns, Map<String, Object> queryCondition, String tableName) {
		return baseDao.findMap(columns, queryCondition, tableName);
	}

	public Map<String, Object> findMap(List<String> columns, String wsql, Map<String, Object> queryCondition) {
		return baseDao.findMap(columns, wsql, queryCondition);
	}

	public Map<String, Object> findMap(List<String> columns, String wsql, Map<String, Object> queryCondition,
			String tableName) {
		return baseDao.findMap(columns, wsql, queryCondition, tableName);
	}

	public long size() {
		return baseDao.size();
	}

	public long size(String tableName) {
		return baseDao.size(tableName);
	}

	public long size(Map<String, Object> queryCondition) {
		return baseDao.size(queryCondition);
	}

	public long size(Map<String, Object> queryCondition, String tableName) {
		return baseDao.size(queryCondition, tableName);
	}

	public long size(String wsql, Map<String, Object> queryCondition) {
		return baseDao.size(wsql, queryCondition);
	}

	public long size(String wsql, Map<String, Object> queryCondition, String tableName) {
		return baseDao.size(wsql, queryCondition, tableName);
	}
	
	
	
}
