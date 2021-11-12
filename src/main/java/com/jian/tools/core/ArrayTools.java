package com.jian.tools.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;


/**
 * 	Array工具类。使用Builder模式，可以链式调用。
 * <p>使用方式：</p>
 * <pre>
 *	//普通使用
	ArrayTools.custom(Integer.class, 10).build();
	ArrayTools.custom(Integer.class, 10).add(10, 20).build();
	ArrayTools.custom(Integer.class, 10).add(new Integer[10]).build();
	ArrayTools.custom(Integer.class, 10).add(new Integer[10], new Integer[10]).toJSONString();
	ArrayTools.custom(Integer.class, 10).add(new Integer[10]).toList();
	ArrayTools.custom(Integer.class, 10).set(10, 5).fill(-1).build();
	ArrayTools.custom(Integer.class, new Integer[10]).set(10, 5).fill(-1).substring(2,6).build();
	
 	//合并数组
	ArrayTools.concat(Integer.class, new Integer[10], new Integer[10]);
	
	//读写流
	ArrayTools.read();
	ArrayTools.readWrite();
 * </pre>
 */
public class ArrayTools {
	
	public static <T> ArrayTools.Builder<T> custom(Class<T> type, int size){
		return new Builder<T>(type, size);
	}
	
	public static <T> ArrayTools.Builder<T> custom(Class<T> type){
		return new Builder<T>(type, 0);
	}
	
	public static <T> ArrayTools.Builder<T> custom(Class<T> type, T[] array){
		if(array == null) {
			return new Builder<T>(type, 0);
		}
		return new Builder<T>(type, array);
	}
	
	/**
	 * 	拼接
	 * @param <T>
	 * @param type
	 * @param array
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] concat(Class<T> type, T[]... array){
		/*List<T> list = new ArrayList<T>();
		for (int i = 0; i < array.length; i++) {
			list.addAll(Arrays.asList(array[i]));
		}
		T[] dist =  (T[]) Array.newInstance(type, list.size()); 
		return list.toArray(dist);*/
		T[] dest = (T[]) Array.newInstance(type, 0); 
		for (T[] src : array) {
			int start = dest.length;
			if(start == 0) { //第一次，不复制，提升性能
				dest = src;
			}else {
				dest = (T[]) Array.newInstance(type, dest.length + src.length); 
				System.arraycopy(src, 0, dest, start, src.length);
			}
		}
		return dest;
	}
	
	/**
	 * 	读取数据，缓冲大小 1024 * 1024
	 * @param in
	 * @return byte[]
	 * @throws IOException
	 */
	public static byte[] read(InputStream in) throws IOException {
		return read(in, 1024 * 1024);
	}
	
	/**
	 * 	读取数据
	 * @param in	输入流
	 * @param bufferSize 缓冲大小
	 * @return byte[]
	 * @throws IOException
	 */
	public static byte[] read(InputStream in, int bufferSize) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] b = new byte[bufferSize];
		int count = 0;
		while ((count = in.read(b)) != -1) {
			out.write(b, 0, count);
		}
		in.close();
		return out.toByteArray();
	}

	/**
	 * 	边读边写，缓冲大小 1024 * 1024
	 * @param in	输入流
	 * @param out	输出流
	 * @throws IOException
	 */
	public static void readWrite(InputStream in, OutputStream out) throws IOException {
		readWrite(in, out, 1024 * 1024);
	}
	
	/**
	 * 	边读边写
	 * @param in	输入流
	 * @param out	输出流
	 * @param bufferSize	缓冲大小
	 * @throws IOException
	 */
	public static void readWrite(InputStream in, OutputStream out, int bufferSize) throws IOException {
		byte[] b = new byte[bufferSize];
		int count = 0;
		while ((count = in.read(b)) != -1) {
			out.write(b, 0, count);
		}
		in.close();
		out.close();
	}

	@SuppressWarnings("unchecked")
	public static class Builder<T> {

		private T[] dist = null;
		private Class<T> type = null;
		
		Builder(Class<T> type, int size) {
            this.type = type;
            this.dist = (T[]) Array.newInstance(type, size); 
        }
		
		Builder(Class<T> type, T[] a) {
            this.type = type;
            this.dist = a; 
        }
		
		/**
		 * 	填充
		 * @param e	值
		 * @return
		 */
		public Builder<T> fill(T e){
			if(this.dist != null){
				for (int i = 0; i < this.dist.length; i++) {
					this.dist[i] = this.dist[i] == null ? e : this.dist[i];
				}
        	}
            return this;
        }
		
		public Builder<T> add(T[]... a){
			/*List<T> list = new ArrayList<T>();
			list.addAll(Arrays.asList(this.dist));
			list.addAll(Arrays.asList(a));
    		T[] dist =  (T[]) Array.newInstance(type, list.size()); 
    		this.dist = list.toArray(dist);
    		list = null;
            return this;*/
			T[] temp =  this.dist;
			for (T[] src : a) {
				int start = temp.length;
				temp = (T[]) Array.newInstance(this.type, temp.length + src.length); 
				System.arraycopy(src, 0, temp, start, src.length);
			}
			this.dist = temp;
			temp = null;
			return this;
        }
		
		public Builder<T> add(T... a){
			/*List<T> list = new ArrayList<T>();
			list.addAll(Arrays.asList(this.dist));
			list.add(a);
    		T[] dist =  (T[]) Array.newInstance(type, list.size()); 
    		this.dist = list.toArray(dist);
    		list = null;
            return this;*/
			T[] temp = (T[]) Array.newInstance(this.type, this.dist.length + a.length); 
			System.arraycopy(a, 0, temp, this.dist.length, a.length);
			this.dist = temp;
			temp = null;
			return this;
        }
        
		/**
		 * 	设置值
		 * @param e
		 * @param index	位置，从0开始
		 * @return
		 */
        public Builder<T> set(T e, int index){
        	if(this.dist != null){
        		this.dist[index] = e;
        	}
            return this;
        }

        /**
         * 	取子串，范围：[start, length)
         * @param start 起始，从0开始
         * @return
         */
        public  Builder<T> substring(int start){
			return substring(start, this.dist.length);
        }
        
        /**
         * 	取子串，范围：[start, end)
         * @param start 起始，从0开始
         * @param end	结束
         * @return
         */
        public  Builder<T> substring(int start, int end){
        	if(start > end) {
        		throw new IllegalArgumentException(" start > end !");
        	}
        	if(start > this.dist.length) {
        		throw new IllegalArgumentException(" start > length !");
        	}
        	if(end > this.dist.length) {
        		throw new IllegalArgumentException(" end > length !");
        	}
        	int len = end - start;
        	T[] temp = (T[]) Array.newInstance(this.type, len); 
        	System.arraycopy(this.dist, start, temp, 0, len);
			this.dist = temp;
			temp = null;
			return this;
        }

        public Builder<T> clear(){
        	this.dist =  (T[]) Array.newInstance(this.type, 0); 
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
		Integer[] test1 = ArrayTools.custom(Integer.class, 10).add(10, 20).build();
		System.out.println("test1:	"+JsonTools.toJsonString(test1));
		Integer[] test2 = ArrayTools.custom(Integer.class, 10).add(new Integer[10]).build();
		System.out.println("test2:	"+JsonTools.toJsonString(test2));
		String test3 = ArrayTools.custom(Integer.class, 10).add(new Integer[10], new Integer[10]).toJSONString();
		System.out.println("test3:	"+test3);
		List<Integer> test4 = ArrayTools.custom(Integer.class, 10).add(new Integer[10]).toList();
		System.out.println("test4:	"+JsonTools.toJsonString(test4));
		Integer[] test5 = ArrayTools.custom(Integer.class, 10).set(10, 5).fill(-1).build();
		System.out.println("test5:	"+JsonTools.toJsonString(test5));
		Integer[] test6 = ArrayTools.custom(Integer.class, new Integer[10]).set(10, 5).fill(-1).substring(2,6).build();
		System.out.println("test5:	"+JsonTools.toJsonString(test6));
	}
}
