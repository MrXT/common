package com.project.common.util;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.common.exception.BusinessException;


/**
 * Jackson工具类
 * @author WJK
 * @version 2015-07
 */
public class JacksonUtils {
    private static final ObjectMapper mapper = new ObjectMapper().setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm"));//ObjectMapper线程安全
    
    /**
     * 饿汉式单例获取ObjectMapper实例
     * @return
     */
    public static ObjectMapper getObjectMapperInstance(){
        return mapper;   
    }
    
    /**
     * 将javabean转换成json格式的字符串
     * @param bean
     * @return
     */
    public static String beanToJson(Object bean) {
        ObjectMapper mapper = getObjectMapperInstance();
        try {
            //mapper.writerWithDefaultPrettyPrinter().writeValueAsString(bean); //将字符串美化成多行
            return mapper.writeValueAsString(bean);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }
    
    /**
     * 将json格式的字符串转换为指定对象的List集合对象
     * @param jsonStr json格式字符串
     * @param t 对象类实例数组
     * @return
     */
    public static <T> T jsonToBean(String jsonStr,Class<T> t) {
        try {
            return getObjectMapperInstance().readValue(jsonStr, t);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }
    
    /**
     * 将json格式的字符串转换为指定对象的List集合对象
     * @param jsonStr json格式字符串
     * @param clazz 对象类实例
     * @return
     * @deprecated 需要两次解析影响效率
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> jsonToListByClass(String jsonStr,Class<T> clazz) {
        List<T> list = new ArrayList<T>();
        try {
            list = new ArrayList<T>();
            List<LinkedHashMap<String, Object>> rawList = getObjectMapperInstance().readValue(jsonStr, List.class);
            // mapToBean时不太好处理来自父类的属性且反射效率也不高，因此采用解析两次的方式
            T[] param = (T[]) Array.newInstance(clazz, rawList.size());
            T[] array = (T[]) getObjectMapperInstance().readValue(jsonStr, param.getClass());
            for(T t : array){
                list.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
        return list;
    }
    
    /**
     * 将json格式的字符串转换为指定对象的List集合对象
     * @param jsonStr json格式字符串
     * @param clazzArray 对象类实例数组
     * @return
     */
    public static <T> List<T> jsonToList(String jsonStr,Class<T[]> clazzArray) {
        List<T> list = new ArrayList<T>();
        try {
            list = new ArrayList<T>();
            T[] array = (T[]) getObjectMapperInstance().readValue(jsonStr, clazzArray);
            list.addAll(Arrays.asList(array));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
        return list;
    }
}
