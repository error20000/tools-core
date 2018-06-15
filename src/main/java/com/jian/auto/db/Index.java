package com.jian.auto.db;

public class Index {

	private String indexName; //索引名，key_name
	private String columnName; //字段名, column_name
	private int isUnique; //是否是唯一索引, non_unique
	private String indexType; //索引类型, index_type
	
	
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public int getIsUnique() {
		return isUnique;
	}
	public void setIsUnique(int isUnique) {
		this.isUnique = isUnique;
	}
	public String getIndexType() {
		return indexType;
	}
	public void setIndexType(String indexType) {
		this.indexType = indexType;
	}
	@Override
	public String toString() {
		return "Index [columnName=" + columnName + ", indexName=" + indexName
				+ ", indexType=" + indexType + ", isUnique=" + isUnique + "]";
	}
	
	
	
	
}
