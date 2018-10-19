package com.jian.jdbc.sboot;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.jian.tools.core.Tools;

public class SbootPropertiesConfig {

	private DriverManagerDataSource dataSource = null;
	private Properties properties = null;
	
	/**
	 * 使用默认配置。
	 * 必须要自己设置  jdbcUrl、user、password、driverClass 这四个属性
	 * 
	 * @author liujian
	 * 
     */
    public SbootPropertiesConfig(){
    	properties = Tools.getProperties(this.getClass().getResourceAsStream("c3p0.properties"));
    	init();
    }

    /**
     * c3p0配置。
     * @param proPath
     */
    public SbootPropertiesConfig(String proPath){
    	properties = Tools.getProperties(proPath);
    	init();
    }
    
    /**
     * c3p0配置。
     * @param file
     */
    public SbootPropertiesConfig(File file){
    	properties = Tools.getProperties(file);
    	init();
    }
    
    /**
     * c3p0配置。
     * @param in
     */
    public SbootPropertiesConfig(InputStream in){
    	properties = Tools.getProperties(in);
    	init();
    }
    
    
    private void init(){
    	dataSource = new DriverManagerDataSource();
    	if(properties != null){
			dataSource.setUrl(properties.getProperty("jdbcUrl"));
			dataSource.setUsername(properties.getProperty("user"));
			dataSource.setPassword(properties.getProperty("password"));
			dataSource.setDriverClassName(properties.getProperty("driverClass"));
			
			/*DriverManagerDataSource dataSource = new DriverManagerDataSource();
			dataSource.setUrl(ConfigOrder.url);
			dataSource.setUsername(ConfigOrder.username);
			dataSource.setPassword(ConfigOrder.password);
			dataSource.setDriverClassName(ConfigOrder.driverClassName);
			
			jdbcTemplate = new JdbcTemplate(dataSource);*/
    	}
    }
    
    /**
     * 获取DataSource
     * @return DriverManagerDataSource 
     * @see org.springframework.jdbc.datasource.DriverManagerDataSource
     */
    public DriverManagerDataSource getDataSource(){
    	return dataSource;
    }
    
}
