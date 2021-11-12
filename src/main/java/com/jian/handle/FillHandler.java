package com.jian.handle;

import java.util.Map;

/**
 * 	填充策略
 * @author liujian
 *
 */
public interface FillHandler {

    /**
     * 	插入时填充
     *
     * @param data 数据源
     */
    void insertFill(Map<String, Object> data);

    /**
     * 	更新时填充
     *
     * @param data 数据源
     */
    void updateFill(Map<String, Object> data);

}
