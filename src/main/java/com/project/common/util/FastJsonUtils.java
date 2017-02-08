
package com.project.common.util;

import java.util.List;
import java.util.Map;




import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;


public class FastJsonUtils {  
    
    private static final SerializeConfig config;  
  
    static {  
        config = new SerializeConfig();  
        //config.put(java.util.Date.class, new JSONLibDataFormatSerializer()); // 使用和json-lib兼容的日期输出格式  
        //config.put(java.sql.Date.class, new JSONLibDataFormatSerializer()); // 使用和json-lib兼容的日期输出格式  
    }  
  
    @SuppressWarnings("unused")
    private static final SerializerFeature[] features = {SerializerFeature.WriteMapNullValue, // 输出空置字段  
            SerializerFeature.WriteNullListAsEmpty
//            , // list字段如果为null，输出为[]，而不是null  
//            SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是null  
//            SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null  
//            SerializerFeature.WriteNullStringAsEmpty // 字符类型字段如果为null，输出为""，而不是null  
    };  
      
  
    public static String toJSONString(Object object) {  
        return JSONObject.toJSONString(object);  
    }  
    public static JSONObject toJSONObject(Object object) {  
        return JSONObject.parseObject(JSON.toJSONString(object));  
    } 
    
    /**
     * 转换为jsonobject 
     * @param text 字符串
     * @return
     */
    public static JSONObject toJSONObject(String text) {  
        return JSONObject.parseObject(text);  
    } 
    
      
    public static String toJSONNoFeatures(Object object) {  
        return JSON.toJSONString(object, config);  
    }  
    /**
     * 类型强转（采用fastjson）
     * @param <T>
     * @param object
     * @param clazz
     * @return 
     */
    public static <T> T beanToBean(Object object,Class<T> clazz){
        return toBean(toJSONString(object),clazz);
    }
  
    public static Object toBean(String text) {  
        return JSONObject.parse(text);  
    }  
  
    public static <T> T toBean(String text, Class<T> clazz) {  
        return JSONObject.parseObject(text, clazz);  
    }  
  
    // 转换为数组  
    public static <T> Object[] toArray(String text) {  
        return toArray(text, null);  
    }  
  
    // 转换为数组  
    public static <T> Object[] toArray(String text, Class<T> clazz) {  
        return JSONObject.parseArray(text, clazz).toArray();  
    }  
  
    // 转换为List  
    public static <T> List<T> toList(String text, Class<T> clazz) {  
        return JSONObject.parseArray(text, clazz);  
    }  
  
      
    /**  
     * 将string转化为序列化的json字符串  
     * @param keyvalue  
     * @return  
     */  
    public static Object textToJson(String text) {  
        Object objectJson  = JSONObject.parse(text);  
        return objectJson;  
    }  
      
    /**  
     * json字符串转化为map  
     * @param s  
     * @return  
     */  
    public static Map<String, Object> stringToCollect(String s) {  
        Map<String, Object> m = JSONObject.parseObject(s);  
        return m;  
    }  
      
    /**  
     * 将map转化为string  
     * @param m  
     * @return  
     */  
    public static String collectToString(Map<String, Object> m) {  
        String s = JSONObject.toJSONString(m);  
        return s;  
    }  
      
}  
