package com.jian.tools.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
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

	public static void main(String[] args) {
		try {
			
			ObjectMapper mapper = new ObjectMapper();
			
			/*// 美化输出
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			// 允许序列化空的POJO类
			// （否则会抛出异常）
			mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
			// 把java.util.Date, Calendar输出为数字（时间戳）
			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

			// 在遇到未知属性的时候不抛出异常
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			// 强制JSON 空字符串("")转换为null对象值:
			mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

			// 在JSON中允许C/C++ 样式的注释(非标准，默认禁用)
			mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
			// 允许没有引号的字段名（非标准）
			mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
			// 允许单引号（非标准）
			mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
			// 强制转义非ASCII字符
			mapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			// 将内容包裹为一个JSON属性，属性名由@JsonRootName注解指定
			mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);*/
			
			/*//注册这些模块
			ObjectMapper mapper = new ObjectMapper()
		                .registerModule(new JavaTimeModule())
		                .registerModule(new ParameterNamesModule())
		                .registerModule(new Jdk8Module());
			 //自动搜索所有模块
			 mapper.findAndRegisterModules();*/
			
			Map<String, Object> map = new HashMap<>();
			map.put("age", 25);
			map.put("name", "yitian");
			map.put("string", new String[]{"test", "ad test"});
			map.put("interests", new byte[]{12, -12});
			
			String text = mapper.writeValueAsString(map);
			System.out.println(text);
			
			Map<String, Object> map2 = mapper.readValue(text, new TypeReference<Map<String, Object>>() {
			});
			System.out.println(map2);
			
			JsonNode root = mapper.readTree(text);
			String name = root.get("name").asText();
			int age = root.get("age").asInt();
			String age2 = root.get("string").get(1).asText();
			byte[] age3 = root.get("interests").binaryValue();
			
			System.out.println("name:" + name + " age:" + age);
			System.out.println(age2);
			System.out.println(age3[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

