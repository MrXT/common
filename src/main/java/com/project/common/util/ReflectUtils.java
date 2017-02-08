package com.project.common.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Table;

import org.apache.log4j.Logger;


/**
 * <p>反射工具类 </p>
 * @author Internet
 * @version 2010-12
 */
public class ReflectUtils {
	private static transient Logger logger = Logger.getLogger(ReflectUtils.class);

    private static Object operate(Object obj, String fieldName, Object fieldVal, String type) {
        Object ret = null;
        try {
            // 获得对象类型
            Class<? extends Object> classType = obj.getClass();
            // 获得对象的所有属性
            Field fields[] = classType.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                if (field.getName().equals(fieldName)) {

                    String firstLetter = fieldName.substring(0, 1).toUpperCase(); // 获得和属性对应的getXXX()方法的名字
                    if ("set".equals(type)) {
                        String setMethodName = "set" + firstLetter + fieldName.substring(1); // 获得和属性对应的getXXX()方法
                        Method setMethod = classType.getMethod(setMethodName, new Class[] { field.getType() }); // 调用原对象的getXXX()方法
                        ret = setMethod.invoke(obj, new Object[] { fieldVal });
                    }
                    if ("get".equals(type)) {
                        String getMethodName = "get" + firstLetter + fieldName.substring(1); // 获得和属性对应的setXXX()方法的名字
                        Method getMethod = classType.getMethod(getMethodName, new Class[] {});
                        ret = getMethod.invoke(obj, new Object[] {});
                    }
                    return ret;
                }
            }
        } catch (Exception e) {
            logger.warn("reflect error:" + fieldName, e);
        }
        return ret;
    }

	public static Object getVal(Object obj, String fieldName) {
		return operate(obj, fieldName, null, "get");
	}

	public static void setVal(Object obj, String fieldName, Object fieldVal) {
		operate(obj, fieldName, fieldVal, "set");
	}

	/**
	 * 循环向上转型, 获取对象的 DeclaredMethod
	 * 
	 * @param object
	 * @param methodName
	 * @param parameterTypes
	 * @return
	 */
	public static Method getDeclaredMethod(Object object, String methodName,
			Class<?>[] parameterTypes) {
		for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				// superClass.getMethod(methodName, parameterTypes);
				return superClass.getDeclaredMethod(methodName, parameterTypes);
			} catch (NoSuchMethodException e) {
				// Method 不在当前类定义, 继续向上转型
			}
		}

		return null;
	}

	/**
	 * 使 filed 变为可访问
	 * 
	 * @param field
	 */
	private static void makeAccessible(Field field) {
		if (!Modifier.isPublic(field.getModifiers())) {
			field.setAccessible(true);
		}
	}

	/**
	 * 循环向上转型, 获取对象的 DeclaredField
	 * 
	 * @param object
	 * @param filedName
	 * @return
	 */
	public static Field getDeclaredField(Object object, String filedName) {
		for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				return superClass.getDeclaredField(filedName);
			} catch (NoSuchFieldException e) {
				// Field 不在当前类定义, 继续向上转型
			}
		}
		return null;
	}

	/**
	 * 直接调用对象方法, 而忽略修饰符(private, protected)
	 * 
	 * @param object
	 * @param methodName
	 * @param parameterTypes
	 * @param parameters
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public static Object invokeMethod(Object object, String methodName,
			Class<?>[] parameterTypes, Object[] parameters)
			throws InvocationTargetException {
		Method method = getDeclaredMethod(object, methodName, parameterTypes);

		if (method == null) {
			throw new IllegalArgumentException("Could not find method ["
					+ methodName + "] on target [" + object + "]");
		}

		method.setAccessible(true);

		try {
			return method.invoke(object, parameters);
		} catch (IllegalAccessException e) {

		}

		return null;
	}

	/**
	 * 直接设置对象属性值, 忽略 private/protected 修饰符, 也不经过 setter
	 * 
	 * @param object
	 * @param fieldName
	 * @param value
	 */
	public static void setFieldValue(Object object, String fieldName,
			Object value) {
		Field field = getDeclaredField(object, fieldName);

		if (field == null)
			throw new IllegalArgumentException("Could not find field ["
					+ fieldName + "] on target [" + object + "]");

		makeAccessible(field);

		try {
			field.set(object, value);
		} catch (IllegalAccessException e) {
		    logger.error(e.getMessage());
		}
	}

	/**
	 * 直接读取对象的属性值, 忽略 private/protected 修饰符, 也不经过 getter
	 * 
	 * @param object
	 * @param fieldName
	 * @return
	 */
	public static Object getFieldValue(Object object, String fieldName) {
		Field field = getDeclaredField(object, fieldName);
		if (field == null)
			throw new IllegalArgumentException("Could not find field ["
					+ fieldName + "] on target [" + object + "]");

		makeAccessible(field);

		Object result = null;
		try {
			result = field.get(object);
		} catch (IllegalAccessException e) {
		    logger.error(e.getMessage());
		}

		return result;
	}

    /**
     * 功能:获取指定对象中指定注解的对象值.
     * @author WJK
     * @version 2015-01
     * @param thisObj    指定的对象
     * @param returnType 返回类型
     * @param annatation 注解类型
     * @return 若指定对象中存在多个相同指定注解，仅返回第一个找到的注解值
     */
    @SuppressWarnings("unchecked")
    public static <K> K getFieldByAnnatation(Object thisObj, Class<K> returnType,Class<? extends Annotation> annatation) {
        for (Class<?> superClass = thisObj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {//如果子类中没有该属性则向上转型
            Field[] fields = superClass.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(annatation)) {
                    try {
                        field.setAccessible(true);
                        K k = (K) field.get(thisObj);
                        field.setAccessible(false);
                        return k;
                    } catch (Exception e) {
                        LogUtils.ERROR(e.getMessage());
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * 功能:为指定对象中注解的属性域赋值.
     * @author WJK
     * @version 2015-08
     * @param thisObj 指定的对象
     * @param value 赋值
     * @param annatation 注解类型
     * @return 若指定对象中存在多个相同指定注解，仅返回第一个找到的注解值
     */
    public static void setFieldByAnnatation(Object thisObj,Object value, Class<? extends Annotation> annatation) {
        for (Class<?> superClass = thisObj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {//如果子类中没有该属性则向上转型
            Field[] fields = superClass.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(annatation)) {
                    try {
                        field.setAccessible(true);
                        field.set(thisObj,value);
                        field.setAccessible(false);
                        return;
                    } catch (Exception e) {
                        LogUtils.ERROR(e.getMessage());
                    }
                }
            }
        }
    }
    
    /**
     * 
     * 功能:通过对象所在路径名查找所有声明的属性名. <br/>
     * @author YXQ
     * @param objectName 对象所在路径名
     * @return
     */
    public static List<String> getFiledsListByObject(String objectName) {
        List<String> result = new ArrayList<String>();
        try {
            Class<?> cls = Class.forName(objectName);
            Field fields[] = cls.getDeclaredFields();
            for(Field f : fields){
                if("serialVersionUID".equals(f.getName())){
                    continue;
                }
                result.add(f.getName());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 向上查询该clazz以及他的父类有没有属性order
     * @param clazz 类
     * @param order 属性名
     * @return
     */
    public static boolean haveProperty(Class<?> clazz, String order) {
        if(clazz.getSimpleName().equalsIgnoreCase("Object")){
            return false;
        }
        try {
            clazz.getDeclaredField(order);
            return true;
        } catch (NoSuchFieldException | SecurityException e) {
            return haveProperty(clazz.getSuperclass(), order);
        }
    }
    /**
     * 向上获取表名的简单字样 sys_user - > su
     * @param clazz 类
     * @return
     */
    public static String getSimpleTable(Class<?> clazz){
        if(clazz.getSimpleName().equalsIgnoreCase("Object")){
            return null;
        }else{
            if(clazz.getAnnotation(Table.class) != null){
                return CommonUtils.tableNameToSimple(clazz.getAnnotation(Table.class).name());
            }else{
                return getSimpleTable(clazz.getSuperclass());
            }
        }
    }
    
}
