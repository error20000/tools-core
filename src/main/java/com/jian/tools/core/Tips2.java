package com.jian.tools.core;


public class Tips2  {
	
	//-3000 以后自定义
	
	/**成功*/
	public static final Tips2 ERROR1 = new Tips2(1, "成功 ");
	/**失败*/
	public static final Tips2 ERROR0 = new Tips2(0, "失败 ");
	
	//不建议直接展示
	/**系统错误*/
	public static final Tips2 ERROR1000 = new Tips2(-1000, "系统错误");
	
	/**{param}失败*/
	public static final Tips2 ERROR1100 = new Tips2(-1100, "{param}失败");
	/**{param}生成失败*/
	public static final Tips2 ERROR1101 = new Tips2(-1101, "{param}生成失败");
	/**{param}效验失败*/
	public static final Tips2 ERROR1102 = new Tips2(-1102, "{param}效验失败"); 

	/**缺少{param}参数 */
	public static final Tips2 ERROR1200 = new Tips2(-1200, "缺少{param}参数 ");
	/**{param}无效/不合法*/
	public static final Tips2 ERROR1201 = new Tips2(-1201, "{param}无效/不合法"); 
	/**{param}参数错误*/
	public static final Tips2 ERROR1202 = new Tips2(-1202, "{param}参数错误"); 
	
	
	//可以直接展示
	/**{param}不能为空*/
	public static final Tips2 ERROR2000 = new Tips2(-2000, "{param}不能为空");
	/**{param}超过限制*/
	public static final Tips2 ERROR2001 = new Tips2(-2001, "{param}超过限制");
	/**文件上传失败*/
	public static final Tips2 ERROR2002 = new Tips2(-2002, "文件上传失败");
	/**不支持该格式*/
	public static final Tips2 ERROR2003 = new Tips2(-2003, "不支持该格式");
	
	/**{param}保存失败*/
	public static final Tips2 ERROR2100 = new Tips2(-2100, "{param}保存失败");
	/**{param}修改失败*/
	public static final Tips2 ERROR2101 = new Tips2(-2101, "{param}修改失败");
	/**{param}查询失败*/
	public static final Tips2 ERROR2102 = new Tips2(-2102, "{param}查询失败");
	/**{param}删除失败*/
	public static final Tips2 ERROR2103 = new Tips2(-2103, "{param}删除失败");
	/**{param}已存在*/
	public static final Tips2 ERROR2104 = new Tips2(-2104, "{param}已存在");
	/**{param}不存在*/
	public static final Tips2 ERROR2105 = new Tips2(-2105, "{param}不存在");
	/**{param}被禁用*/
	public static final Tips2 ERROR2106 = new Tips2(-2106, "{param}被禁用");
	/**{param}暂不可保存*/
	public static final Tips2 ERROR2107 = new Tips2(-2107, "{param}暂不可保存");
	/**{param}暂不可修改*/
	public static final Tips2 ERROR2108 = new Tips2(-2108, "{param}暂不可修改");
	/**{param}暂不可查询*/
	public static final Tips2 ERROR2109 = new Tips2(-2109, "{param}暂不可查询");
	/**{param}暂不可删除*/
	public static final Tips2 ERROR2110 = new Tips2(-2110, "{param}暂不可删除");
	/**{param}暂不可编辑*/
	public static final Tips2 ERROR2111 = new Tips2(-2111, "{param}暂不可编辑");
	/**{param}进行中...暂不可操作*/
	public static final Tips2 ERROR2112 = new Tips2(-2112, "{param}进行中...暂不可操作");
	/**{param}不匹配*/
	public static final Tips2 ERROR2113 = new Tips2(-2113, "{param}不匹配");
	
	/**{param}登录失败*/
	public static final Tips2 ERROR2200 = new Tips2(-2200, "{param}登录失败");
	/**用户名错误*/
	public static final Tips2 ERROR2201 = new Tips2(-2201, "用户名错误");
	/**密码错误*/
	public static final Tips2 ERROR2202 = new Tips2(-2202, "密码错误");
	/**未登录*/
	public static final Tips2 ERROR2203 = new Tips2(-2203, "未登录");
	/**没有权限*/
	public static final Tips2 ERROR2204 = new Tips2(-2204, "没有权限");
	
	/**{param}不正确*/
	public static final Tips2 ERROR2300 = new Tips2(-2300, "{param}不正确");
	/**{param}已过期*/
	public static final Tips2 ERROR2301 = new Tips2(-2301, "{param}已过期");
	/**{param}未配置*/
	public static final Tips2 ERROR2302 = new Tips2(-2302, "{param}未配置");
	/**{param}未启用*/
	public static final Tips2 ERROR2303 = new Tips2(-2303, "{param}未启用");
	
	/**不在活动时间段*/
	public static final Tips2 ERROR2400 = new Tips2(-2400, "不在活动时间段");
	/**活动未开始*/
	public static final Tips2 ERROR2401 = new Tips2(-2401, "活动未开始");
	/**活动已结束*/
	public static final Tips2 ERROR2402 = new Tips2(-2402, "活动已结束"); 
	/**已参加过*/
	public static final Tips2 ERROR2403 = new Tips2(-2403, "已参加过"); 
	/**{param}次数已用完*/
	public static final Tips2 ERROR2404 = new Tips2(-2404, "{param}次数已用完");
	/**{param}未领取*/
	public static final Tips2 ERROR2405 = new Tips2(-2405, "{param}未领取"); 
	/**{param}已领取*/
	public static final Tips2 ERROR2406 = new Tips2(-2406, "{param}已领取"); 
	/**礼包码已用完*/
	public static final Tips2 ERROR2407 = new Tips2(-2407, "{param}已用完");
	/**{param}解析错误*/
	public static final Tips2 ERROR2408 = new Tips2(-2408, "{param}解析错误");
	
	/**没有配置邮件*/
	public static final Tips2 ERROR2500 = new Tips2(-2500, "没有配置邮件");
	/**邮件内容没有配置,或没启用*/
	public static final Tips2 ERROR2501 = new Tips2(-2501, "邮件内容没有配置,或没启用");
	/**邮件已发送，稍后再试*/
	public static final Tips2 ERROR2502 = new Tips2(-2502, "邮件已发送，稍后再试");
	/**没有配置短信*/
	public static final Tips2 ERROR2503 = new Tips2(-2503, "没有配置短信");
	/**短信内容没有配置,或没启用*/
	public static final Tips2 ERROR2504 = new Tips2(-2504, "短信内容没有配置,或没启用");
	/**短信验证码没有配置,或没启用*/
	public static final Tips2 ERROR2505 = new Tips2(-2505, "短信验证码没有配置,或没启用");
	/**短信已发送，稍后再试*/
	public static final Tips2 ERROR2506 = new Tips2(-2506, "短信已发送，稍后再试");
	
	
	private int code;
	private String desc;
	
	public Tips2(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDesc() {
		return desc.replace("{param}", "");
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDesc(String param) {
		return desc.replace("{param}", param);
	}
	
	public String getDescOriginal() {
		return desc;
	}
	
}
