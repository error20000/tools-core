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
	public String id() default "";	//id
	public String name() default "";	//名称
	public String[] path() default {};	//地址
	public RequestMethod[] method() default {};	//请求方式
	public ParamsInfo[] request() default {};	//请求参数
	public ParamsInfo[] response() default {};	//响应参数
	public String info() default "";	//描述
	public Class<?>[] entity() default {};	//字段（bean）
}
