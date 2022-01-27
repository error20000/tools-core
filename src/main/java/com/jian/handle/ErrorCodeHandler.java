package com.jian.handle;

/**
 * 	错误码转译
 * @author liujian
 *
 */
public interface ErrorCodeHandler {

    /**
     * 	转译
     * 
     * @param code 错误码
     * @return
     */
    String translate(int code);
    
    /**
     * 转译（多语言）
     * 
     * @param code 错误码
     * @param language 语言标识
     * @return
     */
    String translate(int code, String language);

}
