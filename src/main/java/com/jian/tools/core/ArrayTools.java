package com.jian.tools.core;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;



public class ArrayTools {
	
	public static <T> ArrayTools.Builder<T> custom(Class<T> type, int size){
		return new Builder<T>(type, size);
	}
	
	public static <T> ArrayTools.Builder<T> custom(Class<T> type){
		return new Builder<T>(type, 0);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T[] concat(Class<T> type, T[]... array){
		List<T> list = new ArrayList<T>();
		for (int i = 0; i < array.length; i++) {
			list.addAll(Arrays.asList(array[i]));
		}
		T[] dist =  (T[]) Array.newInstance(type, list.size()); 
		return list.toArray(dist);
	}

	@SuppressWarnings("unchecked")
	public static class Builder<T> {

		private T[] dist = null;
		private Class<T>  type = null;
		
		Builder(Class<T>  type, int size) {
            this.type = type;
            this.dist =  (T[]) Array.newInstance(type, size); 
        }

		
		public Builder<T> add(T[] a){
			List<T> list = new ArrayList<T>();
			list.addAll(Arrays.asList(this.dist));
			list.addAll(Arrays.asList(a));
    		T[] dist =  (T[]) Array.newInstance(type, list.size()); 
    		this.dist = list.toArray(dist);
    		list = null;
            return this;
        }
		
		public Builder<T> add(T a){
			List<T> list = new ArrayList<T>();
			list.addAll(Arrays.asList(this.dist));
			list.add(a);
    		T[] dist =  (T[]) Array.newInstance(type, list.size()); 
    		this.dist = list.toArray(dist);
    		list = null;
            return this;
        }
        
        public Builder<T> set(T e, int index){
        	if(this.dist != null){
        		this.dist[index] = e;
        	}
            return this;
        }
        
        
		public T[] build() {
        	return this.dist;
        }
		
		public List<T> toList() {
        	return Arrays.asList(this.dist);
        }
        
        public String toJSONString() {
        	return JsonTools.toJsonString(this.dist);
        }
	}
	
	
	public static void main(String[] args) {
		Integer[] test = ArrayTools.custom(Integer.class, 10).build();
		System.out.println("test:	"+JsonTools.toJsonString(test));
		Integer[] test1 = ArrayTools.custom(Integer.class, 10).add(10).build();
		System.out.println("test1:	"+JsonTools.toJsonString(test1));
		Integer[] test2 = ArrayTools.custom(Integer.class, 10).add(new Integer[10]).build();
		System.out.println("test2:	"+JsonTools.toJsonString(test2));
		String test3 = ArrayTools.custom(Integer.class, 10).add(new Integer[10]).toJSONString();
		System.out.println("test3:	"+test3);
		List<Integer> test4 = ArrayTools.custom(Integer.class, 10).add(new Integer[10]).toList();
		System.out.println("test3:	"+JsonTools.toJsonString(test4));
	}
}
