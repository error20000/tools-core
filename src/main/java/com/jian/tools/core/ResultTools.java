package com.jian.tools.core;

import java.util.HashMap;
import java.util.Map;


public class ResultTools {
	
	public static final Map<String, Object> SUCCESS = new Builder(Tips.ERROR1).build();
	public static final Map<String, Object> FAILED = new Builder(Tips.ERROR0).build();
	
	public static ResultTools.Builder custom(){
		return new Builder();
	}
	
	public static ResultTools.Builder custom(Tips tips){
		return custom(tips, "");
	}
	
	public static ResultTools.Builder custom(Tips tips, String param){
		Map<String, Object> map =  new HashMap<String, Object>();
		map.put(ResultKey.CODE, tips.getCode());
		map.put(ResultKey.MSG, tips.getDesc(param));
		return new Builder(map);
	}
	
	public static class Builder {

		private Map<String, Object> map = null;

        Builder() {
            super();
            this.map =  new HashMap<String, Object>();
        }
        
        Builder(Map<String, Object> map) {
            super();
            this.map =  map;
        }
        
        Builder(Tips tips) {
            super();
            this.map =  new HashMap<String, Object>();
            this.map.put(ResultKey.CODE, tips.getCode());
            this.map.put(ResultKey.MSG, tips.getDesc());
        }
        
        public Builder put(String key, Object value){
    		this.map.put(key, value);
            return this;
        }
        
        public Builder put(Tips tips){
            return this.put(tips, "");
        }
        
        public Builder put(Tips tips, String param){
            this.map.put(ResultKey.CODE, tips.getCode());
            this.map.put(ResultKey.MSG, tips.getDesc(param));
            return this;
        }
        
        public Builder put(Object data){
            this.map.put(ResultKey.DATA, data);
            return this;
        }
        
        public Builder remove(String key){
    		this.map.remove(key);
            return this;
        }
        
        public Builder clear(){
    		this.map.clear();
            return this;
        }
        
        public Map<String, Object> build() {
        	return this.map;
        }
        
        public String toJSONString() {
        	return JsonTools.toJsonString(this.map);
        }
	}
	
}
