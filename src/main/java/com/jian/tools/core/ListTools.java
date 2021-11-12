package com.jian.tools.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 	List工具类。使用Builder模式，可以链式调用。
 * <p>使用方式：</p>
 * <pre>
	List<String> list = ListTools.custom(String.class)
			.add("test1")
			.add("test2")
			.add("test3")
			.add("test4")
			.build();
			
	List<String> list2 = ListTools.custom(String.class).build();
	List<String> list5 = ListTools.custom(list).add("test5").build();
	
	List<String> list6 = null;
	ListTools.custom(list6).build().toString();
	
	ListTools.custom().add(1).add("2").build();
	ListTools.customMap().build();
 * </pre>
 */
public class ListTools {
	
	public static final List<Object> DEFAULT = new Builder<Object>().build(); //单例的
	
	public static <T> ListTools.Builder<T> custom(){
		return new Builder<T>();
	}
	
	public static <T> ListTools.Builder<T> custom(Class<T> type){
		return new Builder<T>();
	}
	
	public static <T> ListTools.Builder<T> custom(List<T> list){
		if(list == null) {
			list =  new ArrayList<>();
		}
		return new Builder<T>(list);
	}
	
	public static <V> ListTools.Builder<Map<String, V>> customMap(){
		return new Builder<Map<String, V>>();
	}
	
	public static <V> ListTools.Builder<Map<String, V>> customMap(Class<V> value){
		return new Builder<Map<String, V>>();
	}
	
	public static <K, V> ListTools.Builder<Map<K, V>> customMap(Class<K> key, Class<V> value){
		return new Builder<Map<K, V>>();
	}
	
	public static String toJSONString(List<?> list){
		return  JsonTools.toJsonString(list);
	}
	
	public static class Builder<T> {

		private List<T> list = null;

        Builder() {
            this.list =  new ArrayList<>();
        }
		
        Builder(List<T> list) {
            this.list = list;
        }
        
        public Builder<T> add(T e){
    		this.list.add(e);
            return this;
        }
        
		public Builder<T> remove(T e){
    		this.list.remove(e);
            return this;
        }
        
        public Builder<T> clear(){
    		this.list.clear();
            return this;
        }
        
        public List<T> build() {
        	return this.list;
        }
        
        public String toJSONString() {
        	return JsonTools.toJsonString(this.list);
        }
	}
	
	public static void main(String[] args) {
		List<String> list = ListTools.custom(String.class)
				.add("test1")
				.add("test2")
				.add("test3")
				.add("test4")
				.build();
		List<String> list2 = ListTools.custom(String.class).build();
		List<String> list5 = ListTools.custom(list).add("test5").build();
		ListTools.DEFAULT.add("2");
		List<Object> list3 = ListTools.DEFAULT;
		list3.add("3");
		List<Object> list4 = ListTools.DEFAULT;
		System.out.println(list.toString());
		System.out.println(list2.toString());
		System.out.println(list3.toString());
		System.out.println(list4.toString());
		System.out.println(list5.toString());
		List<String> list6 = null;
		System.out.println(ListTools.custom(list6).build().toString());
		ListTools.custom().add(1).add("2").build();
		ListTools.customMap().build();
	}
}
