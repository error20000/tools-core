package com.jian.web;

import java.lang.reflect.Method;

import com.jian.annotation.RequestMethod;


public class RequestMappingData {

	private String name = "";
	private String[] value = {};
	private String[] path = {};
	private RequestMethod[] reqMethod = {};
	@Deprecated
	private String[] params = {};
	@Deprecated
	private String[] headers = {};
	@Deprecated
	private String[] consumes = {};
	@Deprecated
	private String[] produces = {};
	private Method method = null;
	private Class<?> clss = null;
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String[] getValue() {
		return value;
	}
	public void setValue(String[] value) {
		this.value = value;
	}
	public String[] getPath() {
		return path;
	}
	public void setPath(String[] path) {
		this.path = path;
	}
	public RequestMethod[] getReqMethod() {
		return reqMethod;
	}
	public void setReqMethod(RequestMethod[] reqMethod) {
		this.reqMethod = reqMethod;
	}
	public String[] getParams() {
		return params;
	}
	public void setParams(String[] params) {
		this.params = params;
	}
	public String[] getHeaders() {
		return headers;
	}
	public void setHeaders(String[] headers) {
		this.headers = headers;
	}
	public String[] getConsumes() {
		return consumes;
	}
	public void setConsumes(String[] consumes) {
		this.consumes = consumes;
	}
	public String[] getProduces() {
		return produces;
	}
	public void setProduces(String[] produces) {
		this.produces = produces;
	}
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	public Class<?> getClss() {
		return clss;
	}
	public void setClss(Class<?> clss) {
		this.clss = clss;
	}

}
