package com.jian.tools.core;


public class Tips  {

	/**成功*/
	public static final Tips ERROR1 = new Tips(1, "成功 ");
	/**失败*/
	public static final Tips ERROR0 = new Tips(0, "失败 ");
	
	//失败类型
	/**系统错误*/
	public static final Tips ERROR100 = new Tips(-100, "系统错误");
	/**保存失败，{param}*/
	public static final Tips ERROR101 = new Tips(-101, "保存失败，{param}");
	/**修改失败，{param}*/
	public static final Tips ERROR102 = new Tips(-102, "修改失败，{param}");
	/**查询失败，{param}*/
	public static final Tips ERROR103 = new Tips(-103, "查询失败，{param}");
	/**删除失败，{param}*/
	public static final Tips ERROR104 = new Tips(-104, "删除失败，{param}");
	/**{param}已存在*/
	public static final Tips ERROR105 = new Tips(-105, "{param}已存在");
	/**{param}不存在*/
	public static final Tips ERROR106 = new Tips(-106, "{param}不存在");
	/**{param}被封禁*/
	public static final Tips ERROR107 = new Tips(-107, "{param}被封禁");
	/**{param}登录失败*/
	public static final Tips ERROR108 = new Tips(-108, "{param}登录失败");
	/**用户名错误*/
	public static final Tips ERROR109 = new Tips(-109, "用户名错误");
	/**密码错误*/
	public static final Tips ERROR110 = new Tips(-110, "密码错误");
	/**未登录*/
	public static final Tips ERROR111 = new Tips(-111, "未登录");
	/**文件上传失败*/
	public static final Tips ERROR112 = new Tips(-112, "文件上传失败");
	/**{param}生成失败*/
	public static final Tips ERROR113 = new Tips(-113, "{param}生成失败");
	/**{param}失败*/
	public static final Tips ERROR114 = new Tips(-114, "{param}失败");
	
	//验证类型
	/**{param}无效/不合法*/
	public static final Tips ERROR200 = new Tips(-200, "{param}无效/不合法"); 
	/**没有权限*/
	public static final Tips ERROR201 = new Tips(-201, "没有权限");
	/**{param}参数错误*/
	public static final Tips ERROR202 = new Tips(-202, "{param}参数错误"); 
	/**{param}效验失败*/
	public static final Tips ERROR203 = new Tips(-203, "{param}效验失败"); 
	/**图形验证码不正确*/
	public static final Tips ERROR204 = new Tips(-204, "图形验证码不正确");
	/**短信验证码不正确*/
	public static final Tips ERROR205 = new Tips(-205, "短信验证码不正确");
	/**缺少{param}参数 */
	public static final Tips ERROR206 = new Tips(-206, "缺少{param}参数 ");
	/**图形验证码已过期*/
	public static final Tips ERROR207 = new Tips(-207, "图形验证码已过期");
	/**短信验证码已过期*/
	public static final Tips ERROR208 = new Tips(-208, "短信验证码已过期");
	/**{param}超时*/
	public static final Tips ERROR209 = new Tips(-209, "{param}超时");
	/**{param}超过限制*/
	public static final Tips ERROR210 = new Tips(-210, "{param}超过限制");
	/**{param}不能为空*/
	public static final Tips ERROR211 = new Tips(-211, "{param}不能为空");
	/**不支持该格式*/
	public static final Tips ERROR212 = new Tips(-212, "不支持该格式");
	/**{param}不正确*/
	public static final Tips ERROR213 = new Tips(-213, "{param}不正确");
	/**{param}已过期*/
	public static final Tips ERROR214 = new Tips(-214, "{param}已过期");
	
	//提示类型
	/**不在活动时间段*/
	public static final Tips ERROR300 = new Tips(-300, "不在活动时间段");
	/**活动未开始*/
	public static final Tips ERROR301 = new Tips(-301, "活动未开始");
	/**活动已结束*/
	public static final Tips ERROR302 = new Tips(-302, "活动已结束"); 
	/**已参加过*/
	public static final Tips ERROR303 = new Tips(-303, "已参加过"); 
	/**次数已用完*/
	public static final Tips ERROR304 = new Tips(-304, "次数已用完");
	/**解析JSON/XML内容错误*/
	public static final Tips ERROR305 = new Tips(-305, "解析JSON/XML内容错误");
	/**解析错误*/
	public static final Tips ERROR306 = new Tips(-306, "解析错误");
	/**需提供图形验证码*/
	public static final Tips ERROR307 = new Tips(-307, "需提供图形验证码");
	/**已发送，稍后再试*/
	public static final Tips ERROR308 = new Tips(-308, "已发送，稍后再试");
	/**礼包码已用完*/
	public static final Tips ERROR309 = new Tips(-309, "礼包码已用完");
	/**没有配置邮件*/
	public static final Tips ERROR310 = new Tips(-310, "没有配置邮件");
	/**邮件内容没有配置,或没启用*/
	public static final Tips ERROR311 = new Tips(-311, "邮件内容没有配置,或没启用");
	/**没有配置短信*/
	public static final Tips ERROR312 = new Tips(-312, "没有配置短信");
	/**短信内容没有配置,或没启用*/
	public static final Tips ERROR313 = new Tips(-313, "短信内容没有配置,或没启用");
	/**短信验证码没有配置,或没启用*/
	public static final Tips ERROR314 = new Tips(-314, "短信验证码没有配置,或没启用");
	
	
	
	
	private int code;
	private String desc;
	
	public Tips(int code, String desc) {
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
