package com.jian.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Excel {

	public String name() default "";
	
	public int sort() default 0; //排序
	
	public String value() default "";
	
	public int ignore() default 0; //是否忽略  0--否，1--是
	
	public int encrypt() default 0; //是否加密  0--否，1--是 （ 替换为*）

	public int isNull() default 0; //是否为空  0--否，1--是
	
	public String length() default ""; //数据长度
}
