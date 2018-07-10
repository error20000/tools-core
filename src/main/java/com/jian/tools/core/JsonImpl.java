package com.jian.tools.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class JsonImpl implements JsonInterface {
	
	
	public String toJsonString(Object obj) {
        try {
        	if(obj instanceof String){
        		return obj.toString();
        	}else{
        		return new ObjectMapper().writeValueAsString(obj);
        	}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        return null;
    }
	
	public String toXmlString(Object obj) {
        try {
			return new XmlMapper().writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        return null;
    }

	public <T> T jsonToObj(String json, Class<T> clss) {
        try {
        	ObjectMapper objectMapper = new ObjectMapper();
        	objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			return objectMapper.readValue(json, clss);
		} catch (IOException e) {
			e.printStackTrace();
		}
        return null;
    }
	
	@SuppressWarnings("unchecked")
	public <T> T jsonToObj(String json, TypeReference<T> typeReference) {
        try {
        	ObjectMapper objectMapper = new ObjectMapper();
        	objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			return (T) objectMapper.readValue(json, typeReference);
		} catch (IOException e) {
			e.printStackTrace();
		}
        return null;
    }
	
	public <T> T xmlToObj(String xml, Class<T> clss) {
		try {
			XmlMapper mapper = new XmlMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			return mapper.readValue(xml, clss);
		} catch (IOException e) {
			e.printStackTrace();
		}  
        return null;
    }
	

	@SuppressWarnings("unchecked")
	public <T> T xmlToObj(String xml, TypeReference<T> typeReference) {
        try {
			XmlMapper mapper = new XmlMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			return (T) mapper.readValue(xml, typeReference);
		} catch (IOException e) {
			e.printStackTrace();
		}
        return null;
    }
	
	//TODO jsonToObj 实现

	public Map<String, Object> jsonToMap(String json) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JavaType javaType = mapper
					.getTypeFactory()
					.constructParametricType(HashMap.class, String.class, Object.class); 
			return mapper.readValue(json, javaType);
		} catch (IOException e) {
			e.printStackTrace();
		}  
        return null;
    }
	
	public <K, V> Map<K, V> jsonToMap(String json, Class<K> key, Class<V> value) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JavaType javaType = mapper
					.getTypeFactory()
					.constructParametricType(HashMap.class, key, value); 
			return mapper.readValue(json, javaType);
		} catch (IOException e) {
			e.printStackTrace();
		}  
        return null;
    }
	
	public List<Map<String, Object>> jsonToList(String json) {
		try {
			return new ObjectMapper().readValue(json, new TypeReference<List<Map<String, Object>>>(){});
		} catch (IOException e) {
			e.printStackTrace();
		}  
        return null;
    }
	
	public <T> List<T> jsonToList(String json, Class<T> clss) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JavaType javaType = mapper
					.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
					.getTypeFactory()
					.constructParametricType(List.class, clss);  
			return mapper.readValue(json, javaType);
		} catch (IOException e) {
			e.printStackTrace();
		}  
        return null;
    }
	
	
	//TODO xmlToObj 实现

	public Map<String, Object> xmlToMap(String xml) {
		try {
			XmlMapper mapper = new XmlMapper();
			JavaType javaType = mapper
					.getTypeFactory()
					.constructParametricType(HashMap.class, String.class, Object.class); 
			return mapper.readValue(xml, javaType);
		} catch (IOException e) {
			e.printStackTrace();
		}  
        return null;
    }
	
	public <K, V> Map<K, V> xmlToMap(String xml, Class<K> key, Class<V> value) {
		try {
			XmlMapper mapper = new XmlMapper();
			JavaType javaType = mapper
					.getTypeFactory()
					.constructParametricType(HashMap.class, key, value); 
			return mapper.readValue(xml, javaType);
		} catch (IOException e) {
			e.printStackTrace();
		}  
        return null;
    }
	
	public List<Map<String, Object>> xmlToList(String xml) {
		try {
			return new XmlMapper().readValue(xml, new TypeReference<List<Map<String, Object>>>(){});
		} catch (IOException e) {
			e.printStackTrace();
		}  
        return null;
    }
	
	public <T> List<T> xmlToList(String xml, Class<T> clss) {
		try {
			XmlMapper mapper = new XmlMapper();
			JavaType javaType = mapper
					.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
					.getTypeFactory()
					.constructParametricType(List.class, clss);  
			return mapper.readValue(xml, javaType);
		} catch (IOException e) {
			e.printStackTrace();
		}  
        return null;
    }

}

