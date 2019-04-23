package com.jian.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface API {
	
	@Deprecated
	public String[] path() default {};	//地址
	@Deprecated
	public RequestMethod[] method() default {};	//请求方式
	@Deprecated
	public ParamsInfo[] request() default {};	//请求参数
	@Deprecated
	public ParamsInfo[] response() default {};	//响应参数
	@Deprecated
	public Class<?>[] entity() default {};	//字段（bean）

	public String id() default "";	//id
	public String name() default "";	//名称
	public String info() default "";	//描述
	public APIType type() default APIType.MI;	//分类  
}
