package com.jian.tools.core;

public class AccessToken {

	private String key;
	private long millis;
	private String token;
	private Object value;
	
	public AccessToken(String key, Object value) {
		this.key = key;
		this.value = value;
	}
	
	public AccessToken() {
	}
	
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public long getMillis() {
		return millis;
	}
	public void setMillis(long millis) {
		this.millis = millis;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
}
