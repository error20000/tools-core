package com.jian.spring.template;

import java.util.List;
import java.util.Map;





public abstract class BaseService<T> {

	protected BaseDao<T> baseDao;
	
	
	public BaseService() {
		super();
		initDao();
	}

	public abstract void initDao();
	
	
	public void init() {
		initDao();
	}
	
	public int add(T obj){
		init();
		return baseDao.add(obj);
	}
	
	public int add(List<T> objs){
		init();
		return baseDao.batchAdd(objs);
	}
	
	public int modify(T obj){
		init();
		return baseDao.modify(obj);
	}
	
	public int modify(List<T> objs){
		init();
		return baseDao.batchModify(objs);
	}
	
	public int modify(Map<String, Object> values, Map<String, Object> condition){
		init();
		return baseDao.modify(values, condition);
	}
	
	public int delete(Map<String, Object> condition){
		init();
		return baseDao.delete(condition);
	}
	
	public int delete(String column, List<String> values){
		init();
		return baseDao.batchDelete(column, values);
	}
	
	public List<T> findAll(){
		init();
		return baseDao.findList();
	}
	
	public List<T>  findList(Map<String, Object> condition){
		init();
		return baseDao.findList(condition);
	}
	
	public T findOne(Map<String, Object> condition){
		init();
		return baseDao.findObject(condition);
	}
	
	public List<T> findPage(Map<String, Object> condition, int start, int rows){
		init();
		return baseDao.findList(condition, start, rows);
	}
	
	public long size(Map<String, Object> condition){
		init();
		return baseDao.size(condition);
	}
	
	
	public BaseDao<T> getDao(){
		return baseDao;
	}
	
	
	public int  baseUpdate(String sql, Map<String, Object> condition){
		init();
		return baseDao.baseUpdate(sql, condition);
	}
	
	public <E> List<E> baseQuery(String sql, Map<String, Object> condition, Class<E> clzz){
		init();
		return baseDao.baseQuery(sql, condition, clzz);
	}

	public <E> List<E> basePage(String sql, Map<String, Object> condition, Class<E> clzz, int start, int rows){
		init();
		return baseDao.basePage(sql, condition, clzz, start, rows);
	}
	
	public long baseSize(String sql, Map<String, Object> condition){
		init();
		return baseDao.baseSize(sql, condition);
	}
	
}
