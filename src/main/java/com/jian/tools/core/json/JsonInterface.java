package com.jian.tools.core.json;

import java.util.List;
import java.util.Map;

/**
 * json 工具类接口
 * @author liujian
 *
 */
public interface JsonInterface {
	
	public String toJsonString(Object obj);
	
	public String toXmlString(Object obj);

	public <T> T jsonToObj(String json, Class<T> clss);
	
	public <T> T xmlToObj(String xml, Class<T> clss);
	
	
	//TODO jsonToObj 实现

	public Map<String, Object> jsonToMap(String json);
	
	public <K, V> Map<K, V> jsonToMap(String json, Class<K> key, Class<V> value);
	
	public List<Map<String, Object>> jsonToList(String json);
	
	public <T> List<T> jsonToList(String json, Class<T> clss);
	
	
	//TODO xmlToObj 实现

	public Map<String, Object> xmlToMap(String xml);
	
	public <K, V> Map<K, V> xmlToMap(String xml, Class<K> key, Class<V> value);
	
	public List<Map<String, Object>> xmlToList(String xml);
	
	public <T> List<T> xmlToList(String xml, Class<T> clss);

}
