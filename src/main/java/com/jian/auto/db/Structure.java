package com.jian.auto.db;

public class Structure {

	private String field; //字段
	private String type; //类型
	private String length; //长度
	private String isNull; //是否为空
	private String key; //主键
	private String defaultValue; //默认值
	private String extra; //例外，如自增等等
	private String comment; //备注
	
	
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getIsNull() {
		return isNull;
	}
	public void setIsNull(String isNull) {
		this.isNull = isNull;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getExtra() {
		return extra;
	}
	public void setExtra(String extra) {
		this.extra = extra;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	@Override
	public String toString() {
		return "Structure [defaultValue=" + defaultValue + ", extra=" + extra
				+ ", field=" + field + ", isNull=" + isNull + ", key=" + key
				+ ", type=" + type + ", length=" + length + ", comment=" + comment + "]";
	}
	
	
	
}
