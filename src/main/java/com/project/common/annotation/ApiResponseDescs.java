
package com.project.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @author xt
 * @version 2017年1月29日
 */
@Target(ElementType.METHOD) //注解class
@Retention(RetentionPolicy.RUNTIME)//程序运行时，也能有用
@Documented//用于javadoc
public @interface ApiResponseDescs {
   public ApiResponseDesc [] value() default {};
}

