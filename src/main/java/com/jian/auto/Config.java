package com.jian.auto;

import com.jian.tools.core.Tools;

public class Config {
	
	private String basePackge = "com.jian.auto";
	
	//包地址
	private static final String ENTITY_PATH = "entity";
	private static final String DAO_PATH = "dao";
	private static final String DAO_IMPL_PATH = "dao.impl";
	private static final String DAO_UTIL_PATH = "dao.util";
	private static final String SERVICE_PATH = "service";
	private static final String SERVICE_IMPL_PATH = "service.impl";
	private static final String SERVLET_PATH = "servlet";
	private static final String CONTROLLER_PATH = "controller";
	private static final String CONFIG_PATH = "config";
	private static final String UTIL_PATH = "util";
	private static final String ASPECT_PATH = "aspect";
	private static final String ASPECT_ANNOTATION_PATH = "aspect.annotation";
	private static final String EXCEPTION_PATH = "exception";
	
	//配置
	private String dbPropertiesName = "autodb.properties"; //数据库配置文件名
	private String dbSecondPropertiesName = "autodb2.properties"; //数据库配置文件名
	private boolean overWrite = false; //文件已存在是否覆盖
	private String reqPrefix = "/api"; //请求前缀
	private String chartset = "utf-8";//文件字符集
	private static String tempPath = "/com/jian/auto/template/";//模版路径
	
	//自动填充
	//主键
	private String autoFillPrimaryKey = ",自增主键,UUID主键,"; //自动填充主键
	//日期
	private String autoFillDateForAdd = ",日期,创建时间,创建日期,"; //新增日期类型自动填充
	private String autoFillDateForModify = ",修改时间,修改日期,"; //修改日期类型自动填充
	
	
	public Config(){
		
	}
	
	public Config(String basePackge){
		basePackge = basePackge.trim();
		if(basePackge.endsWith(".")){
			basePackge = basePackge.substring(0, basePackge.length() - 1);
		}
		this.basePackge = basePackge;
	}
	
	
	public String getEntityPath(){
		return basePackge+"."+ENTITY_PATH;
	}
	
	public String getDaoPath(){
		return basePackge+"."+DAO_PATH;
	}
	
	public String getDaoImplPath(){
		return basePackge+"."+DAO_IMPL_PATH;
	}
	
	public String getDaoUtilPath(){
		return basePackge+"."+DAO_UTIL_PATH;
	}

	public String getServicePath(){
		return basePackge+"."+SERVICE_PATH;
	}

	public String getServiceImplPath(){
		return basePackge+"."+SERVICE_IMPL_PATH;
	}
	
	public String getServletPath(){
		return basePackge+"."+SERVLET_PATH;
	}

	public String getControllerPath(){
		return basePackge+"."+CONTROLLER_PATH;
	}
	
	public String getConfigPath(){
		return basePackge+"."+CONFIG_PATH;
	}

	public String getUtilPath(){
		return basePackge+"."+UTIL_PATH;
	}

	public String getAspectPath(){
		return basePackge+"."+ASPECT_PATH;
	}

	public String getAspectAnnotationPath(){
		return basePackge+"."+ASPECT_ANNOTATION_PATH;
	}

	public String getExceptionPath(){
		return basePackge+"."+EXCEPTION_PATH;
	}
	
	
	public String getBasePackge() {
		return basePackge;
	}

	/*
	 * 设置包名
	 */
	public void setBasePackge(String basePackge) {
		basePackge = basePackge.trim();
		if(!Tools.isNullOrEmpty(basePackge)){
			this.basePackge = basePackge;
		}
	}

	public String getDbPropertiesName() {
		return dbPropertiesName;
	}
	
	/*
	 * 数据库配置文件名，默认"autodb.properties"
	 */
	public void setDbPropertiesName(String dbPropertiesName) {
		dbPropertiesName = dbPropertiesName.trim();
		if(!Tools.isNullOrEmpty(dbPropertiesName)){
			this.dbPropertiesName = dbPropertiesName;
		}
	}

	public String getDbSecondPropertiesName() {
		return dbSecondPropertiesName;
	}

	/*
	 * 数据库配置文件名，默认"autodb2.properties"
	 */
	public void setDbSecondPropertiesName(String dbSecondPropertiesName) {
		dbSecondPropertiesName = dbSecondPropertiesName.trim();
		if(!Tools.isNullOrEmpty(dbSecondPropertiesName)){
			this.dbSecondPropertiesName = dbSecondPropertiesName;
		}
	}

	public boolean isOverWrite() {
		return overWrite;
	}

	/*
	 * 文件已存在是否覆盖，默认"false"
	 */
	public void setOverWrite(boolean overWrite) {
		this.overWrite = overWrite;
	}

	public String getReqPrefix() {
		return reqPrefix;
	}

	/*
	 * 请求前缀，默认"/api"
	 */
	public void setReqPrefix(String reqPrefix) {
		reqPrefix = reqPrefix.trim();
		if(!Tools.isNullOrEmpty(reqPrefix)){
			reqPrefix = reqPrefix.replace("../", "");
			if(!reqPrefix.startsWith("/")){
				reqPrefix = "/"+reqPrefix;
			}
			if(reqPrefix.endsWith("/")){
				reqPrefix = reqPrefix.substring(0, reqPrefix.length() - 1);
			}
		}
		this.reqPrefix = reqPrefix;
	}

	public String getChartset() {
		return chartset;
	}
	
	/*
	 * 文件字符集，默认"utf-8"
	 */
	public void setChartset(String chartset) {
		chartset = chartset.trim();
		if(!Tools.isNullOrEmpty(chartset)){
			this.chartset = chartset;
		}
	}

	public static String getTempPath() {
		return tempPath;
	}

	public static void setTempPath(String tempPath) {
		tempPath = tempPath.trim();
		if(!Tools.isNullOrEmpty(tempPath)){
			Config.tempPath = tempPath;
		}
	}

	public String getAutoFillPrimaryKey() {
		return autoFillPrimaryKey;
	}

	public void setAutoFillPrimaryKey(String autoFillPrimaryKey) {
		this.autoFillPrimaryKey = autoFillPrimaryKey;
	}

	public String getAutoFillDateForAdd() {
		return autoFillDateForAdd;
	}

	public void setAutoFillDateForAdd(String autoFillDateForAdd) {
		this.autoFillDateForAdd = autoFillDateForAdd;
	}

	public String getAutoFillDateForModify() {
		return autoFillDateForModify;
	}

	public void setAutoFillDateForModify(String autoFillDateForModify) {
		this.autoFillDateForModify = autoFillDateForModify;
	}
	

}
