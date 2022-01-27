package com.jian.exception;

/**
 * 统一的 http 异常
 */
public class HttpException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public HttpException() {
		super();
	}
	
	public HttpException(String msg){
		super(msg);
	}

	public HttpException(Throwable cause){
		super(cause);
	}

}
