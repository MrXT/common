
package com.project.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * swagger api文档response返回json数据格式设置
 * ClassName: ApiResponseDesc <br/>
 *
 * @author xt
 * @version 2017年1月29日
 */
@Target(ElementType.METHOD) //注解class
@Retention(RetentionPolicy.RUNTIME)//程序运行时，也能有用
@Documented//用于javadoc
public @interface ApiResponseDesc {
    /**
     * 返回字段名，如果value()为空，则定义的基本类型数组,例如 @ApiResponseDesc 定义的是字符串数组
     * @author xt
     * @return
     */
    public String value() default "";
    
    public enum ApiResponseType{
        STRING("string"),BOOLEAN("boolean"),DATE("date"),INTEGER("integer"),
        DOUBLE("double"),FLOAT("float"),LONG("long"),ARRAY("array");
        String value;
        public String getValue() {
            return value;
        }
        public void setValue(String value) {
            this.value = value;
        }
        ApiResponseType(String value){
            this.value = value;
        }
    }
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
    public ApiResponseDescItem [] items() default {};
}

