package com.jian.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 结果集和数据库连接封装
 *
 */
public class ResultInfo {
    private ResultSet resultSet;
    private boolean isTrancation;

    public ResultInfo(ResultSet resultSet, boolean isTrancation) {
        this.resultSet = resultSet;
        this.isTrancation = isTrancation;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getObjectList(Class<T> t) {
        try{
            return (List<T>) TranslateSQL.getAllRowWithObjectList(this.resultSet, t);
        }catch(SQLException | ReflectiveOperationException | ParseException e){
            e.printStackTrace();
        }finally{
            // 非事物模式执行
            if (!isTrancation) {
                JdbcOperate.closeConnection(resultSet);
            }else{
                JdbcOperate.closeResult(resultSet);
            }
        }
        return new ArrayList<T>();

    }

    public List<Map<String, Object>> getMapList() {
        try{
            return TranslateSQL.getAllRowWithMapList(this.resultSet);
        }catch(SQLException | ReflectiveOperationException e){
        	e.printStackTrace();
        }finally{
            // 非事物模式执行
            if (!isTrancation) {
                JdbcOperate.closeConnection(resultSet);
            }else{
                JdbcOperate.closeResult(resultSet);
            }
        }
        return new ArrayList<Map<String, Object>>();
    }

    @SuppressWarnings("unchecked")
    public <T> Object getObject(Class<T> t){
        try{
            if(resultSet.next()){
                return (T) TranslateSQL.getOneRowWithObject(this.resultSet, t);
            }else{
                return null;
            }
        }catch(SQLException | ReflectiveOperationException | ParseException e){
        	e.printStackTrace();
        }finally{
            // 非事物模式执行
            if (!isTrancation) {
                JdbcOperate.closeConnection(resultSet);
            }else{
                JdbcOperate.closeResult(resultSet);
            }
        }
        return null;
    }

    public Map<String, Object> getMap(){
        try{
            if(resultSet.next()){
                return TranslateSQL.getOneRowWithMap(this.resultSet);
            }else{
                return null;
            }
        }catch(SQLException | ReflectiveOperationException e){
        	e.printStackTrace();
        }finally{
            // 非事物模式执行
            if (!isTrancation) {
                JdbcOperate.closeConnection(resultSet);
            }else{
                JdbcOperate.closeResult(resultSet);
            }
        }
        return null;
    }
}
