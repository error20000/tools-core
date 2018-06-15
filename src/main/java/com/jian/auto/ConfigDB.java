package com.jian.auto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.jian.tools.core.Tools;

public class ConfigDB {
	
	//表字段
	private String prefix = "";
	private String separator = "";
	
	//主库
	private String jdbcUrl = "";
	private String user = "";
	private String password = "";
	private String driverClass = "";
	
	//从库
	private String jdbcUrlSecond = "";
	private String userSecond = "";
	private String passwordSecond = "";
	private String driverClassSecond = "";
	
	public ConfigDB(String jdbcUrl, String user, String password, String driverClass, String prefix, String separator){
		this.jdbcUrl = jdbcUrl;
		this.user = user;
		this.password = password;
		this.driverClass = driverClass;
		this.prefix = prefix;
		this.separator = separator;
	}
	
	public ConfigDB(String jdbcUrl, String user, String password, String prefix, String separator){
		this.jdbcUrl = jdbcUrl;
		this.user = user;
		this.password = password;
		this.driverClass = Tools.getDriverClass(jdbcUrl);
		this.prefix = prefix;
		this.separator = separator;
	}
	
	public ConfigDB(){
		
	}
	
	public String getDBPath(){
		if(Tools.isNullOrEmpty(jdbcUrl)){
			return "";
		}
		return createPropertes(jdbcUrl, user, password, driverClass, "autodb.properties");
	}
	
	public String getDBPathSecond(){
		if(Tools.isNullOrEmpty(jdbcUrlSecond)){
			return "";
		}
		return createPropertes(jdbcUrlSecond, userSecond, passwordSecond, driverClassSecond, "autodb2.properties");
	}
	
	
	private String createPropertes(String jdbcUrl, String user, String password, String driverClass, String fileName){
		System.out.println("start db properties... " + jdbcUrl);
		InputStream in = this.getClass().getResourceAsStream(Config.TEMPLATE_PATH+"/TempDB.properties"); //模版
		String outPath = Tools.getBaseResPath() + fileName; //输出路径
		BufferedWriter bw = null;
		BufferedReader br = null;
		File outFile = new File(outPath);
		//如果文件已存在，并且不开启重写。结束创建。
		if(outFile.exists() && outFile.length() != 0){
			System.out.println(fileName+" 已存在！！！");
			return outPath;
		}
		System.out.println("output file... " +outPath);
		File pfile = outFile.getParentFile();
		if(!pfile.exists()){
			pfile.mkdirs();
		}
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8")); 
			br = new BufferedReader(new InputStreamReader(in, "utf-8")); 
			String line; 
			while((line = br.readLine()) != null){ 
				//db
				if(line.indexOf("{jdbcUrl}") != -1){
					line = line.replace("{jdbcUrl}", jdbcUrl);
				}else if(line.indexOf("{user}") != -1){
					line = line.replace("{user}", user);
				}else if(line.indexOf("{password}") != -1){
					line = line.replace("{password}", password);
				}else if(line.indexOf("{driverClass}") != -1){
					line = line.replace("{driverClass}", driverClass);
				}
				bw.write(line); 
				bw.newLine(); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try { 
				if(br != null){
					br.close(); 
				}
				if(bw != null){
					bw.flush();
					bw.close(); 
				}
			}catch (Exception e) { 
				e.printStackTrace(); 
			}
		}
		System.out.println("end db properties... " + jdbcUrl);
		return outPath;
	}
	
	//TODO get set
	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getJdbcUrlSecond() {
		return jdbcUrlSecond;
	}

	public void setJdbcUrlSecond(String jdbcUrlSecond) {
		this.jdbcUrlSecond = jdbcUrlSecond;
	}

	public String getUserSecond() {
		return userSecond;
	}

	public void setUserSecond(String userSecond) {
		this.userSecond = userSecond;
	}

	public String getPasswordSecond() {
		return passwordSecond;
	}

	public void setPasswordSecond(String passwordSecond) {
		this.passwordSecond = passwordSecond;
	}

	public String getDriverClassSecond() {
		return driverClassSecond;
	}

	public void setDriverClassSecond(String driverClassSecond) {
		this.driverClassSecond = driverClassSecond;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}
	
}
