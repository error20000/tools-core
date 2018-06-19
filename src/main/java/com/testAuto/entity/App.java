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
@Table("s_app")
public class App  extends Base<App> {
	
	//field
	@PrimaryKey(type=PrimaryKeyType.UUID)
	@Excel(name="主键", sort=0 )
	private String pid;
	@Excel(name="app名称", sort=1 )
	private String name;
	@Excel(name="标识符", sort=2 )
	private String marks;
	@Excel(name="密码", sort=3 )
	private String secretKey;
	@Excel(name="安全域名", sort=4 )
	private String secretUrl;
	@Excel(name="状态： 0 -- 禁用， 1--启用", sort=5, value="0" )
	private int status;
	@Excel(name="备注", sort=6 )
	private String info;
	@Excel(name="创建时间", sort=7 )
	private String createTime;
	@Excel(name="修改时间", sort=8 )
	private String updateTime;
	@Excel(name="权限范围  json", sort=9 )
	private String scope;
	
	//get set
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMarks() {
		return marks;
	}
	public void setMarks(String marks) {
		this.marks = marks;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public String getSecretUrl() {
		return secretUrl;
	}
	public void setSecretUrl(String secretUrl) {
		this.secretUrl = secretUrl;
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
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}

}
