package com.jian.tools.core;

public class DebugTools {
	
	
	/**
	 * 调试输出
	 * @param info 输出信息
	 * @param debug 调试开关    true System.out false log4j
	 */
	public static void println(String info, boolean debug){
		if(debug) {
			System.out.println(DateTools.formatDate()+"	"+info);
		}else {
			//TODO 输出日志
		}
	}
	
	
	
}
