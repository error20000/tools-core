package com.jian.annotation;

public class PrimaryKeyCondition {

	private String field;
	private String type = "String";
	private PrimaryKeyType keyType = PrimaryKeyType.NORMAL;
	
	public PrimaryKeyCondition(){
		
	}
	
	public PrimaryKeyCondition(String field) {
		this.field = field;
	}
	
	public PrimaryKeyCondition(String field, String type, PrimaryKeyType keyType) {
		this.field = field;
		this.type = type;
		this.keyType = keyType;
	}


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
	public PrimaryKeyType getKeyType() {
		return keyType;
	}
	public void setKeyType(PrimaryKeyType keyType) {
		this.keyType = keyType;
	}
	
}
