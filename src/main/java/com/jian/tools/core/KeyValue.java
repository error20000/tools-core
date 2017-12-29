package com.jian.tools.core;

public class KeyValue {

	private String key;
	private Object condition;
	private Object value;
	
	public KeyValue() {
	}
	
	
	public KeyValue(String key, Object value) {
		this.key = key;
		this.value = value;
	}

	public KeyValue(String key, Object condition, Object value) {
		this.key = key;
		this.condition = condition;
		this.value = value;
	}


	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Object getCondition() {
		return condition;
	}
	public void setCondition(Object condition) {
		this.condition = condition;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
}
