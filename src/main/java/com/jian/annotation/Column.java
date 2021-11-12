package com.jian.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Column {
	
	/** 数据库字段名  */
	public String value() default "";

    /**  字段自动填充策略 */
	public FillType fill() default FillType.DEFAULT;
	
    /** 是否为数据库表字段  默认 true 是，false 否  */
	public boolean exist() default true;
}
