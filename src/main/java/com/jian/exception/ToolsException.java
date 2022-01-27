package com.jian.exception;

/**
 * 统一的 工具类 异常
 */
public class ToolsException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public ToolsException() {
		super();
	}
	
	public ToolsException(String msg){
		super(msg);
	}

	public ToolsException(Throwable cause){
		super(cause);
	}

}
