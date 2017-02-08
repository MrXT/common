
package com.project.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.project.common.annotation.ApiResponseDesc.ApiResponseType;
/**
 * ApiResponseDesc子注解
 * ClassName: ApiResponseDesc <br/>
 *
 * @author xt
 * @version 2017年1月29日
 */
@Target(ElementType.METHOD) //注解class
@Retention(RetentionPolicy.RUNTIME)//程序运行时，也能有用
@Documented//用于javadoc
public @interface ApiResponseDescItem{
    /**
     * 返回字段名，如果value()为空，则定义的基本类型数组,例如 @ApiResponseDescItem 定义的是字符串数组
     * @author xt
     * @return
     */
    public String value() default "";
    /**
     * 返回字段类型 默认 string
     * @author xt
     * @return
     */
    public ApiResponseType type() default ApiResponseType.STRING;
    /**
     * 字段描述
     * @author xt
     * @return
     */
    public String description() default "";
    /**
     * 
     * @author xt
     * @return
     */
    public ApiResponseDescItemItem [] items() default {};
    
}

