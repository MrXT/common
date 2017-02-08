
package com.project.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 排序注解 value=su.name,按照sys_user的name字段排序
 * //排序字段，如果有该注解那么优先按照注解的value来排序
 * ClassName: Sort <br/>
 * @author xt(di.xiao@karakal.com.cn)
 * @date 2017年1月20日
 * @version 1.0
 */
@Target(ElementType.FIELD) //注解属性
@Retention(RetentionPolicy.RUNTIME)//程序运行时，也能有用
@Documented//用于javadoc
public @interface Sort {
    String value() default "";
}

