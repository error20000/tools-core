package com.jian.jdbc.c3p0;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Properties;

import com.jian.tools.core.Tools;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class C3P0PropertiesConfig {

	private ComboPooledDataSource dataSource = null;
	private Properties properties = null;
	
	/**
	 * 使用默认配置。
	 * 必须要自己设置  jdbcUrl、user、password、driverClass 这四个属性
	 * 
	 * @author liujian
	 * 
     */
    public C3P0PropertiesConfig(){
    	properties = Tools.getProperties(this.getClass().getResourceAsStream("c3p0.properties"));
    	init();
    }

    /**
     * c3p0配置。
     * @param proPath
     */
    public C3P0PropertiesConfig(String proPath){
    	properties = Tools.getProperties(proPath);
    	init();
    }
    
    /**
     * c3p0配置。
     * @param file
     */
    public C3P0PropertiesConfig(File file){
    	properties = Tools.getProperties(file);
    	init();
    }
    
    /**
     * c3p0配置。
     * @param in
     */
    public C3P0PropertiesConfig(InputStream in){
    	properties = Tools.getProperties(in);
    	init();
    }
    
    
    private void init(){
    	dataSource = new ComboPooledDataSource();
    	if(properties != null){
    		try {
    			Enumeration<?> keys = properties.keys();
    			while(keys.hasMoreElements()){
    				String key = (String) keys.nextElement();
    				if(key.startsWith("c3p0.")){
    					key = key.replace("c3p0.", "");
    				}else if(key.startsWith("c3p0_")){
    					key = key.replace("c3p0_", "");
    				}else if(key.startsWith("test.")){
    					continue;
    				}else if(key.startsWith("test_")){
    					continue;
    				}
    				//method
    				Method[] methods = dataSource.getClass().getDeclaredMethods();
    				for (Method method : methods) {
    					String tmp = method.getName();
    					if(tmp.startsWith("set") && tmp.substring("set".length()).equalsIgnoreCase(key)){
    						switch (method.getParameterTypes()[0].getSimpleName()) {
    						case "int":
    							method.invoke(dataSource, Tools.parseInt(properties.getProperty(key)));
    							break;
    						case "long":
    							method.invoke(dataSource, Tools.parseLong(properties.getProperty(key)));
    							break;
    						case "float":
    							method.invoke(dataSource, Tools.parseFloat(properties.getProperty(key)));
    							break;
    						case "double":
    							method.invoke(dataSource, Tools.parseDouble(properties.getProperty(key)));
    							break;
    						case "boolean":
    							method.invoke(dataSource, Tools.parseBoolean(properties.getProperty(key)));
    							break;
    						default:
    							method.invoke(dataSource, properties.getProperty(key));
    							break;
    						}
    						break;
    					}
    				}
    			}
    			properties.clear();
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    }
    
    /**
     * 获取DataSource
     * @return ComboPooledDataSource 
     * @see com.mchange.v2.c3p0.ComboPooledDataSource
     */
    public ComboPooledDataSource getDataSource(){
    	return dataSource;
    }
    
}
