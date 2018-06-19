package com.jian.web;

import java.util.HashMap;
import java.util.Map;

public class AllowReq {

	private static String filePath = "allows/req.txt";
	
	private static Map<String, String> content = new HashMap<String, String>();
	
	

	public static String getFilePath() {
		return filePath;
	}

	public static void setFilePath(String filePath) {
		AllowReq.filePath = filePath;
	}

	public static Map<String, String> getContent() {
		return content;
	}

	public static void setContent(String key, String value) {
		AllowReq.content.put(key, value);
	}
	
}
