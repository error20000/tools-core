package com.jian.tools.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * 	Map工具类。使用Builder模式，可以链式调用。
 * <p>使用方式：</p>
 * <pre>
	Map<String, Object> map = MapTools.custom()
			.put("test1", "test1")
			.put("test2", "test2")
			.put("test3", "test3")
			.putIf("test4", null) //当value = null时，不会put
			.putIf("test5", null, "test5") //当value = null时，取def值
			.build();
	Map<String, String> map2 = MapTools.custom(String.class).build();
	Map<String, Object> map5 = null;
	MapTools.custom(map5).build().toJSONString()
	MapTools.custom(new HashMap<Integer, Integer>()).putIf(null, null, 2).build();
		
	MapTools.custom().put("test1", "test1").build();
	MapTools.custom(String.class).put("test1", "test1").remove("ssd").build();
	MapTools.custom(Object.class, Object.class).build();
 * </pre>
 */
public class MapTools {
	
	public static final Map<String, Object> DEFAULT = new Builder<String, Object>().build(); //单例的
	
	public static <V> MapTools.Builder<String, V> custom(){
		return new Builder<String, V>();
	}
	
	public static <V> MapTools.Builder<String, V> custom(Class<V> value){
		return new Builder<String, V>();
	}
	
	public static <K, V> MapTools.Builder<K, V> custom(Class<K> key, Class<V> value){
		return new Builder<K, V>();
	}
	
	public static <K, V> MapTools.Builder<K, V> custom(Map<K, V> map){
		if(map == null) {
			return new Builder<K, V>();
		}
		return custom(map, true);
	}
	
	public static <K, V> MapTools.Builder<K, V> custom(Map<K, V> map, boolean newMap){
		if(map == null) {
			return new Builder<K, V>();
		}
		if(newMap){
			Map<K, V> tmp = new HashMap<K, V>();
			Set<K> set = map.keySet();
        	Iterator<K> it = set.iterator();
        	while (it.hasNext()) {
				K str = it.next();
				tmp.put(str, map.get(str));
			}
			return new Builder<K, V>(tmp);
		}else{
			return new Builder<K, V>(map);
		}
	}
	
	public static String toJSONString(Map<?, ?> map) {
    	return JsonTools.toJsonString(map);
    }

	public static class Builder<K, V> {

		private Map<K, V> map = null;

        Builder() {
            this.map =  new HashMap<K, V>();
        }
        
        Builder(Class<V> type) {
            this.map =  new HashMap<K, V>();
        }
        
        Builder(Map<K, V> map) {
            this.map =  map;
        }
        
        public Builder<K, V> put(K key, V value){
    		this.map.put(key, value);
            return this;
        }
        
        /**
         * 	当value = null时，不会put。
         * @param key
         * @param value
         * @return
         */
        public Builder<K, V> putIf(K key, V value){
    		if(value != null) {
    			this.map.put(key, value);
    		}
            return this;
        }
        
        /**
         * 	当value = null时，取def值
         * @param key
         * @param value
         * @param def
         * @return
         */
        public Builder<K, V> putIf(K key, V value, V def){
    		if(value == null) {
    			value = def;
    		}
    		this.map.put(key, value);
            return this;
        }
        
        public Builder<K, V> remove(K key){
    		this.map.remove(key);
            return this;
        }
        
        public Builder<K, V> clear(){
    		this.map.clear();
            return this;
        }
        
        public Map<K, V> build() {
        	return this.map;
        }
        
        /**
         * 	key=value&key=value...
         * @return
         */
        public String toURL() {
        	StringBuilder sb = new StringBuilder();
        	Set<K> set = this.map.keySet();
        	Iterator<K> it = set.iterator();
        	while (it.hasNext()) {
				K str = it.next();
				sb.append("&");
				sb.append(str);
				sb.append("=");
				sb.append(this.map.get(str));
			}
        	return Tools.isNullOrEmpty(sb.toString()) ? "" : sb.toString().substring(1);
        }
        
        public String toJSONString() {
        	return JsonTools.toJsonString(this.map);
        }
	}
	

	public static void main(String[] args) {
		Map<String, Object> map = MapTools.custom()
				.put("test1", "test1")
				.put("test2", "test2")
				.put("test3", "test3")
				.put("test1", "test4")
				.build();
		Map<String, String> map2 = MapTools.custom(String.class).build();
		MapTools.DEFAULT.put("2", "3");
		Map<String, Object> map3 = MapTools.DEFAULT;
		map3.put("1", "2");
		Map<String, Object> map4 = MapTools.DEFAULT;
		System.out.println(map.toString());
		System.out.println(map2.toString());
		System.out.println(map3.toString());
		System.out.println(map4.toString());
		Map<String, Object> map5 = null;
		System.out.println(MapTools.custom(map5).build().toString());		
		Map<Integer, Integer> map6 = MapTools.custom(new HashMap<Integer, Integer>()).putIf(null, null, 2).build();	
		System.out.println(map6.get(null));	
		MapTools.custom().put("test1", "test1").build();
		MapTools.custom(String.class).put("test1", "test1").remove("ssd").build();
		MapTools.custom(Object.class, Object.class).build();
	}
}
