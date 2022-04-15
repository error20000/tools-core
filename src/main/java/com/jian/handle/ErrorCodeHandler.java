package com.jian.handle;

/**
 * 	错误码转译
 * @author liujian
 *
 */
public interface ErrorCodeHandler {

    /**
     * 	转译（用户友好提示）
     * 
     * @param code 错误码
     * @param msg 错误描述
     * @return
     */
    String translate(int code, String msg);
    
    /**
     * 转译（用户友好提示、多语言等）
     * 
     * @param code 错误码
     * @param msg 错误描述
     * @param language 语言标识
     * @return
     */
    String translate(int code, String msg, String language);

}
