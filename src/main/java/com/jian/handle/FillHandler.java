package com.jian.handle;

import java.util.Map;

/**
 * 	填充策略
 * @author liujian
 *
 */
public interface FillHandler {

    /**
     * 	插入时填充。 key: 数据库字段 
     *
     * @param data 数据源 key : value
     */
    void insertFill(Map<String, Object> data);

    /**
     * 	更新时填充。 key: 数据库字段 
     *
     * @param data 数据源 key : value
     */
    void updateFill(Map<String, Object> data);

}
