package com.jian.tools.core;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonTools {
	
	private static JsonInterface ifs = null;

	static{
		List<Class<?>> classes = findClass();
		if(classes == null || classes.size() == 0){
			ifs = new JsonImpl();
		}else{
			try {
				ifs = (JsonInterface) classes.get(0).newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		if(ifs == null){
			ifs = new JsonImpl();
		}
	}
	

	/**
	 * 获取接口。返回工具类注册的实现。
	 * @return  JsonInterface 的实现
	 */
	public static JsonInterface getIfs(){
		return ifs;
	}
	
	public static String toJsonString(Object obj) {
        return ifs.toJsonString(obj);
    }
	
	public static String toXmlString(Object obj) {
        return ifs.toXmlString(obj);
    }

	public static <T> T jsonToObj(String json, Class<T> clss) {
        return ifs.jsonToObj(json, clss);
    }
	
	public static <T> T xmlToObj(String xml, Class<T> clss) {
        return ifs.xmlToObj(xml, clss);
    }
	

	//TODO jsonToObj 实现

	public static Map<String, Object> jsonToMap(String json) {
        return ifs.jsonToMap(json);
    }
	
	public static <K, V> Map<K, V> jsonToMap(String json, Class<K> key, Class<V> value) {
        return ifs.jsonToMap(json, key, value);
    }
	
	public static List<Map<String, Object>> jsonToList(String json) {
        return ifs.jsonToList(json);
    }
	
	public static <T> List<T> jsonToList(String json, Class<T> clss) {
        return ifs.jsonToList(json, clss);
    }
	
	
	//TODO xmlToObj 实现

	public static Map<String, Object> xmlToMap(String xml) {
        return ifs.xmlToMap(xml);
    }
	
	public static <K, V> Map<K, V> xmlToMap(String xml, Class<K> key, Class<V> value) {
        return ifs.xmlToMap(xml, key, value); 
    }
	
	public static List<Map<String, Object>> xmlToList(String xml) {
        return ifs.xmlToList(xml);
    }
	
	public static <T> List<T> xmlToList(String xml, Class<T> clss) {
        return ifs.xmlToList(xml, clss); 
    }

	
	private static List<Class<?>> findClass(){
		List<Class<?>> classes = new ArrayList<>();
		try {
			//查找Cache接口的所有实现
			List<Class<?>> total = Tools.findClass("");
			for (Class<?> temp : total) {
				
				if(!temp.isInterface() && !Modifier.isAbstract(temp.getModifiers()) &&
						temp.getInterfaces().length != 0 && temp.getInterfaces()[0].getName().equals(JsonInterface.class.getName()) ){
					classes.add(temp);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return classes;
	}
	
	
	public static void main(String[] args) {
		String xml = "";
		/*xml += "<returnsms>";
		xml += "	<returnstatus>Success</returnstatus>";
		xml += "	<message>ok</message>";
		xml += "	<remainpoint>11032</remainpoint>";
		xml += "	<taskID>4354699</taskID>";
		xml += "	<successCounts>1</successCounts>";

		xml += "	<chlend>";
		xml += "		<returnstatus>Success</returnstatus>";
		xml += "		<message>ok</message>";
		xml += "		<remainpoint>11032</remainpoint>";
		xml += "		<taskID>4354699</taskID>";
		xml += "		<successCounts>1</successCounts>";
		xml += "	</chlend>";
		xml += "</returnsms>";
		Map<String, Object> map = JsonTools.xmlToMap(xml, String.class, Object.class);
		System.out.println(JsonTools.toJsonString(map));
		

		User user = JsonTools.xmlToObj(xml, User.class);
		System.out.println(JsonTools.toJsonString(user));*/
		
		xml = "";
		xml += "<root>";
		xml += "<returnsms>";
		xml += "	<returnstatus>Success</returnstatus>";
		xml += "	<message>ok</message>";
		xml += "	<remainpoint>11032</remainpoint>";
		xml += "	<taskID>4354699</taskID>";
		xml += "	<successCounts>1</successCounts>";
		xml += "</returnsms>";
		xml += "<returnsms>";
		xml += "	<returnstatus>Success2</returnstatus>";
		xml += "	<message>ok2</message>";
		xml += "	<remainpoint>110322</remainpoint>";
		xml += "	<taskID>43546992</taskID>";
		xml += "	<successCounts>12</successCounts>";
		xml += "	<chlend>";
		xml += "		<returnstatus>Success</returnstatus>";
		xml += "		<message>ok</message>";
		xml += "		<remainpoint>11032</remainpoint>";
		xml += "		<taskID>4354699</taskID>";
		xml += "		<successCounts>1</successCounts>";
		xml += "	</chlend>";
		xml += "</returnsms>";
		xml += "</root>";
		List<Map<String, Object>> list = JsonTools.xmlToList(xml);
		System.out.println(JsonTools.toJsonString(list));
		Map<String, Object> map = new HashMap<>();
		map.put("test", "list");
		map.put("test1", "list1");
		map.put("test2", "list2");

		System.out.println(JsonTools.toJsonString(map));
		Map<String, Object> test = JsonTools.jsonToMap(JsonTools.toJsonString(map), String.class, Object.class);
		Object data = test.get("test");
		System.out.println(JsonTools.toJsonString(data));
		System.out.println(data instanceof List<?>);
		/*
		List<User> list2 = JsonTools.xmlToList(xml, User.class);
		System.out.println(JsonTools.toJsonString(list2));*/

		System.out.println("=================================================");
		
		Map<String, Object> test2 = JsonTools.jsonToMap(JsonTools.toJsonString("{}"), String.class, Object.class);
		System.out.println(JsonTools.toJsonString(test2));
		
		List<String> test3 = JsonTools.jsonToList(JsonTools.toJsonString("[]"), String.class);
		System.out.println(JsonTools.toJsonString(test3));
		
		System.out.println("-------------------------------------------------");
		
		System.out.println(JsonTools.toXmlString("[]"));
		System.out.println(JsonTools.toXmlString("{}"));
	}
}

