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
@Table("s_group_app")
public class GroupApp  extends Base<GroupApp> {
	
	//field
	@PrimaryKey(type=PrimaryKeyType.UUID)
	@Excel(name="主键", sort=0 )
	private String pid;
	@Excel(name="分组", sort=1 )
	private String groupId;
	@Excel(name="应用", sort=2 )
	private String appId;
	
	//get set
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}

}
