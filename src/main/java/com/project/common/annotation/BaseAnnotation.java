
package com.project.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD) //注解属性
@Retention(RetentionPolicy.RUNTIME)//程序运行时，也能有用
@Documented//用于javadoc
public @interface BaseAnnotation {
    String value() default "";
}

