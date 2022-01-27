package com.jian.exception;

/**
 * 统一的 Json 异常
 */
public class JsonException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public JsonException() {
		super();
	}
	
	public JsonException(String msg){
		super(msg);
	}

	public JsonException(Throwable cause){
		super(cause);
	}

}
