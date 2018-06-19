package com.testAuto.entity;

//import
import com.jian.annotation.PrimaryKey;
import com.jian.annotation.PrimaryKeyType;
import com.jian.annotation.Table;
import com.jian.annotation.Excel;

/**
 * @author liujian
 * @Date 
 */
@Table("s_user")
public class User  extends Base<User> {
	
	//field
	@PrimaryKey(type=PrimaryKeyType.UUID)
	@Excel(name="主键", sort=0 )
	private String pid;
	@Excel(name="登录名", sort=1 )
	private String username;
	@Excel(name="密码 （md5）", sort=2 )
	private String password;
	@Excel(name="第三方ID", sort=3 )
	private String thridId;
	@Excel(name="昵称", sort=4 )
	private String nick;
	@Excel(name="状态： 0 -- 禁用， 1--启用", sort=5, value="0" )
	private int status;
	@Excel(name="备注", sort=6 )
	private String info;
	@Excel(name="创建时间", sort=7 )
	private String createTime;
	@Excel(name="修改时间", sort=8 )
	private String updateTime;
	@Excel(name="管理员 0 -- 普通，1 --超级，2--其他，......", sort=9, value="0" )
	private int admin;
	
	//get set
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getThridId() {
		return thridId;
	}
	public void setThridId(String thridId) {
		this.thridId = thridId;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public int getAdmin() {
		return admin;
	}
	public void setAdmin(int admin) {
		this.admin = admin;
	}

}
