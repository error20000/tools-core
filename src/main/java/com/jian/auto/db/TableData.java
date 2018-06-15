package com.jian.auto.db;

public class TableData {

	private String key; //字段
	private Object value; //对应值
	
	public TableData(){
		
	}
	
	public TableData(String key, Object value) {
		this.key = key;
		this.value = value;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
	
	
}
