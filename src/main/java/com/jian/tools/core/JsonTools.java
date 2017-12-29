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

public class JsonTools {
	
	private final static ObjectMapper objectMapper = new ObjectMapper();
	
	static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
	
	public static String toJsonString(Object obj) {
        try {
			return objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        return null;
    }

	public static <T> T jsonToObj(String json, Class<T> clss) {
        try {
			return objectMapper.readValue(json, clss);
		} catch (IOException e) {
			e.printStackTrace();
		}
        return null;
    }
	
	@SuppressWarnings("unchecked")
	public static <T> T jsonToObj(String json, TypeReference<T> typeReference) {
        try {
			return (T) objectMapper.readValue(json, typeReference);
		} catch (IOException e) {
			e.printStackTrace();
		}
        return null;
    }
	
	public static <T> T xmlToObj(String xml, Class<T> clss) {
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
	public static <T> T xmlToObj(String xml, TypeReference<T> typeReference) {
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

	@SuppressWarnings("unchecked")
	public static Map<String, String> jsonToMap(String json) {
		try {
			return new ObjectMapper().readValue(json, Map.class);
		} catch (IOException e) {
			e.printStackTrace();
		}  
        return null;
    }
	
	public static <K, V> Map<K, V> jsonToMap(String json, Class<K> key, Class<V> value) {
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
	
	public static List<Map<String, Object>> jsonToList(String json) {
		try {
			return new ObjectMapper().readValue(json, new TypeReference<List<Map<String, Object>>>(){});
		} catch (IOException e) {
			e.printStackTrace();
		}  
        return null;
    }
	
	public static <T> List<T> jsonToList(String json, Class<T> clss) {
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

	@SuppressWarnings("unchecked")
	public static Map<String, String> xmlToMap(String xml) {
		try {
			return new XmlMapper().readValue(xml, Map.class);
		} catch (IOException e) {
			e.printStackTrace();
		}  
        return null;
    }
	
	public static <K, V> Map<K, V> xmlToMap(String xml, Class<K> key, Class<V> value) {
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
	
	public static List<Map<String, Object>> xmlToList(String xml) {
		try {
			return new XmlMapper().readValue(xml, new TypeReference<List<Map<String, Object>>>(){});
		} catch (IOException e) {
			e.printStackTrace();
		}  
        return null;
    }
	
	public static <T> List<T> xmlToList(String xml, Class<T> clss) {
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
		/*String xml = "";
		xml += "<returnsms>";
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
		System.out.println(JsonTools.toJsonString(user));
		
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
		

		List<User> list2 = JsonTools.xmlToList(xml, User.class);
		System.out.println(JsonTools.toJsonString(list2));*/
	}
}

/*class User{
	private String returnstatus;
	private String message;
	private String remainpoint;
	private String taskID;
	private String sex;
	public String getReturnstatus() {
		return returnstatus;
	}
	public void setReturnstatus(String returnstatus) {
		this.returnstatus = returnstatus;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getRemainpoint() {
		return remainpoint;
	}
	public void setRemainpoint(String remainpoint) {
		this.remainpoint = remainpoint;
	}
	public String getTaskID() {
		return taskID;
	}
	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	
}*/
