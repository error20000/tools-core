package com.jian.tools.core;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class ArrayTools {
	
	public static <T> ArrayTools.Builder<T> custom(Class<T> type, int size){
		return new Builder<T>(type, size);
	}
	
	public static <T> ArrayTools.Builder<T> custom(Class<T> type){
		return new Builder<T>(type, -1);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T[] concat(Class<T> type, T[]... array){
		List<T> list = new ArrayList<T>();
		for (int i = 0; i < array.length; i++) {
			list.addAll(Arrays.asList(array[i]));
		}
		T[] dist =  (T[]) Array.newInstance(type, list.size()); 
        for (int i = 0; i < list.size(); i++) {
        	dist[i] = list.get(i);
		}
		return dist;
	}
	
	public static <T> void concat(T[] dest, T[]... array){
		int index = 0;
		for (int i = 0; i < array.length; i++) {
			System.arraycopy(array[i], 0, dest, index, array[i].length);
			index += array[i].length;
		}
	}
	
	public static <T> T[] concat(T[]... array){
		int newlength = 0;
		Class<? extends T[]> type = null;
		for (int i = 0; i < array.length; i++) {
			newlength += array[i].length;
			
		}
		T[] dist =  (T[]) Array.newInstance(type, newlength);
		
		int index = 0;
		for (int i = 0; i < array.length; i++) {
			System.arraycopy(array[i], 0, dist, index, array[i].length);
			index += array[i].length;
		}
		return dist;
	}
	
	public static class Builder<T> {

		private T[] dist = null;
		private Class<T>  type = null;
		private int size = -1;
		
		Builder(Class<T>  type, int size) {
            this.type = type;
            this.size = size;
            this.dist =  (T[]) Array.newInstance(type, size); 
        }

        @SuppressWarnings("unchecked")
        private void parser(List<T> list){
        	this.dist =  (T[]) Array.newInstance(type, list.size());
        	for (int i = 0; i < list.size(); i++) {
        		this.dist[i] = list.get(i);
    		}
        }
		
		public Builder<T> add(T[] a){
    		List<T> list = new ArrayList<T>();
    		if(this.dist != null){
    			list.addAll(Arrays.asList(this.dist));
    		}
    		if(a != null){
    			list.addAll(Arrays.asList(a));
    		}
    		parser(list);
            return this;
        }
        

        @SuppressWarnings("unchecked")
		public Builder<T> addInt(int[] a){
    		List<T> list = new ArrayList<T>();
    		if(this.dist != null){
    			list.addAll(Arrays.asList(this.dist));
    		}
    		for (int tmp : a) {
    			list.add((T) (Integer) tmp);
			}
    		parser(list);
            return this;
        }
        
        @SuppressWarnings("unchecked")
		public Builder<T> addByte(byte[] a){
    		List<T> list = new ArrayList<T>();
    		if(this.dist != null){
    			list.addAll(Arrays.asList(this.dist));
    		}
    		for (byte tmp : a) {
    			list.add((T) (Byte) tmp);
			}
    		parser(list);
            return this;
        }
        
        @SuppressWarnings("unchecked")
		public Builder<T> addShort(short[] a){
    		List<T> list = new ArrayList<T>();
    		if(this.dist != null){
    			list.addAll(Arrays.asList(this.dist));
    		}
    		for (short tmp : a) {
    			list.add((T) (Short) tmp);
			}
    		parser(list);
            return this;
        }
        
        @SuppressWarnings("unchecked")
		public Builder<T> addLong(long[] a){
    		List<T> list = new ArrayList<T>();
    		if(this.dist != null){
    			list.addAll(Arrays.asList(this.dist));
    		}
    		for (long tmp : a) {
    			list.add((T) (Long) tmp);
			}
    		parser(list);
            return this;
        }
        
        @SuppressWarnings("unchecked")
		public Builder<T> addFloat(float[] a){
    		List<T> list = new ArrayList<T>();
    		if(this.dist != null){
    			list.addAll(Arrays.asList(this.dist));
    		}
    		for (float tmp : a) {
    			list.add((T) (Float) tmp);
			}
    		parser(list);
            return this;
        }
        
        @SuppressWarnings("unchecked")
		public Builder<T> addDouble(double[] a){
    		List<T> list = new ArrayList<T>();
    		if(this.dist != null){
    			list.addAll(Arrays.asList(this.dist));
    		}
    		for (double tmp : a) {
    			list.add((T) (Double) tmp);
			}
    		parser(list);
            return this;
        }
        
        @SuppressWarnings("unchecked")
		public Builder<T> addChar(char[] a){
    		List<T> list = new ArrayList<T>();
    		if(this.dist != null){
    			list.addAll(Arrays.asList(this.dist));
    		}
    		for (char tmp : a) {
    			list.add((T) (Character) tmp);
			}
    		parser(list);
            return this;
        }
        
        @SuppressWarnings("unchecked")
		public Builder<T> addBoolean(boolean[] a){
    		List<T> list = new ArrayList<T>();
    		if(this.dist != null){
    			list.addAll(Arrays.asList(this.dist));
    		}
    		for (boolean tmp : a) {
    			list.add((T) (Boolean) tmp);
			}
    		parser(list);
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
        
        public int[] buildInt() {
        	int[] tmp = new int[this.dist.length];
        	for (int i = 0; i < this.dist.length; i++) {
        		if(this.dist[i] == null){
        			tmp[i] = 0;
        		}else{
        			tmp[i] = (int) this.dist[i];
        		}
    		}
        	return tmp;
        }
        
        public long[] buildLong() {
        	long[] tmp = new long[this.dist.length];
        	for (int i = 0; i < this.dist.length; i++) {
        		if(this.dist[i] == null){
        			tmp[i] = 0L;
        		}else{
        			tmp[i] = (long) this.dist[i];
        		}
    		}
        	return tmp;
        }
        
        public float[] buildFloat() {
        	float[] tmp = new float[this.dist.length];
        	for (int i = 0; i < this.dist.length; i++) {
        		if(this.dist[i] == null){
        			tmp[i] = 0F;
        		}else{
        			tmp[i] = (float) this.dist[i];
        		}
    		}
        	return tmp;
        }
        
        public double[] buildDouble() {
        	double[] tmp = new double[this.dist.length];
        	for (int i = 0; i < this.dist.length; i++) {
        		if(this.dist[i] == null){
        			tmp[i] = 0D;
        		}else{
        			tmp[i] = (double) this.dist[i];
        		}
    		}
        	return tmp;
        }
        
        public short[] buildShort() {
        	short[] tmp = new short[this.dist.length];
        	for (int i = 0; i < this.dist.length; i++) {
        		if(this.dist[i] == null){
        			tmp[i] = 0;
        		}else{
        			tmp[i] = (short) this.dist[i];
        		}
    		}
        	return tmp;
        }
        
        public char[] buildChar() {
        	char[] tmp = new char[this.dist.length];
        	for (int i = 0; i < this.dist.length; i++) {
        		if(this.dist[i] == null){
        			tmp[i] = '0';
        		}else{
        			tmp[i] = (char) this.dist[i];
        		}
    		}
        	return tmp;
        }
        
        public boolean[] buildBoolean() {
        	boolean[] tmp = new boolean[this.dist.length];
        	for (int i = 0; i < this.dist.length; i++) {
        		if(this.dist[i] == null){
        			tmp[i] = false;
        		}else{
        			tmp[i] = (boolean) this.dist[i];
        		}
    		}
        	return tmp;
        }
        
        public byte[] buildByte() {
        	byte[] tmp = new byte[this.dist.length];
        	for (int i = 0; i < this.dist.length; i++) {
        		if(this.dist[i] == null){
        			tmp[i] = 0;
        		}else{
        			tmp[i] = (byte) this.dist[i];
        		}
    		}
        	return tmp;
        }
        
        public String toJSONString() {
        	return JsonTools.toJsonString(this.dist);
        }
	}
	
	
	public static void main(String[] args) {
		ArrayTools.custom(Long.class, 10).add(new Integer[10]).build();
	}
}
