package com.jian.handle;

import javax.servlet.http.HttpServletRequest;

/**
 * 	登录工具类
 * @author liujian
 */
public interface TokenHandler {
	

	//TODO ----------------------------------------------------------------------------------------------------- token
	
	/**
	 * 	获取token字符串
	 * @return
	 */
	public String getLoginToken();

	/**
	 * 	获取token字符串
	 * @param req
	 * @return
	 */
	public String getLoginToken(HttpServletRequest req);
	
	/**
	 * 	检查token是否有效
	 * @param tokenStr	token字符串
	 * @return
	 */
	public boolean checkLoginToken(String tokenStr);
	
	/**
	 * 	新建token
	 * @param userId	用户id
	 * @return	token字符串
	 */
	public String newToken(String userId);
	
	/**
	 * 	新建token
	 * @param userId	用户id
	 * @param platform	平台标识
	 * @return	token字符串
	 */
	public String newToken(String userId, String platform);
	
	/**
	 * 	新建token
	 * @param userId	用户id
	 * @param expireTime	token有效期（毫秒）
	 * @param platform	平台标识
	 * @return	token字符串
	 */
	public String newToken(String userId, int expireTime, String platform);
	
	

	//TODO ----------------------------------------------------------------------------------------------------- login

	/**
	 * 	缓存登录信息
	 * @param <T>
	 * @param tokenStr	token字符串
	 * @param obj	用户信息
	 */
	public <T> void setLogin(String tokenStr, T obj);
	
	/**
	 * 	判断是否登录
	 * @return
	 */
	public boolean isLogin();
	
	/**
	 * 	判断是否登录
	 * @param req
	 * @return
	 */
	public boolean isLogin(HttpServletRequest req);
	
	/**
	 * 	判断是否登录
	 * @param tokenStr
	 * @return
	 */
	public boolean isLogin(String tokenStr);
	
	/**
	 * 	获取登录用户
	 * @param <T>
	 * @param clss	转换类
	 * @return
	 */
	public <T> T getLoginUser(Class<T> clss);
	
	/**
	 * 	获取登录用户
	 * @param <T>
	 * @param req
	 * @param clss	转换类
	 * @return
	 */
	public <T> T getLoginUser(HttpServletRequest req, Class<T> clss);
	
	/**
	 * 	清除登录信息
	 */
	public void clearLogin();
	
	/**
	 * 	清除登录信息
	 * @param req
	 */
	public void clearLogin(HttpServletRequest req);
	
	/**
	 * 	清除登录信息
	 * @param tokenStr
	 */
	public void clearLogin(String tokenStr);
	
	
	//TODO ----------------------------------------------------------------------------------------------------- password
	
	/**
	 * 	检查用户登录密码
	 * @param userId	用户id
	 * @param password	加密后的密码（保存在数据库的密码）
	 * @param salt	盐
	 * @param pwd	明文密码（登录时输入的密码）
	 */
	public void checkLoinPassword(String userId, String password, String salt, String pwd);

	/**
	 * 	检查用户登录密码（直接密码密文匹配）
	 * @param userId	用户id
	 * @param password	加密后的密码（保存在数据库的密码）
	 * @param password2	加密后的密码（登录时输入的密码加密后）
	 */
	public void checkLoinPassword(String userId, String password, String password2);

	/**
	 * 	密码md5
	 * @param str	密码
	 * @return
	 */
	public String pwdMd5(String str);
	
	/**
	 * 	密码md5
	 * @param str	密码
	 * @param salt	盐
	 * @return
	 */
	public String pwdMd5(String str, String salt);
	
	/**
	 * 	生产盐
	 * @return
	 */
    public String getSalt();

    /**
     * 	密码加密
     * @param data
     * @return
     */
	public byte[] pwdEncode(byte[] data);

    /**
     * 	密码解密
     * @param data
     * @return
     */
	public byte[] pwdDecode(byte[] data);
	
}
