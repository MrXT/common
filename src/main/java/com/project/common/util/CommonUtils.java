package com.project.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.util.CollectionUtils;

import com.project.common.exception.ServerException;

/**
 * <p>
 * 通用工具类
 * </p>
 * @author WJK
 * @version 2015-08
 */
public class CommonUtils {

    private final static boolean IS_AUTOLOAD_RESOURCE = false;

    /**
     * 从配置文件中读取静态常量的值(默认从 application.properties读取)
     * @param key
     * @return
     */
    public static String readResource(String key) {
        return readResource(key, "application.properties", IS_AUTOLOAD_RESOURCE);
    }

    /**
     * 从配置文件中读取静态常量的值
     * @param key
     * @param resource 资源文件的编译路径
     * @return
     */
    public static String readResource(String key, String resource) {
        return readResource(key, resource, IS_AUTOLOAD_RESOURCE);
    }

    /**
     * 从配置文件中读取静态常量数组的值
     * @param key
     * @return
     */
    public static String[] readResourceArray(String key) {
        return readResourceArray(key, IS_AUTOLOAD_RESOURCE);
    }

    /**
     * 从配置文件中读取静态常量的值
     * @param key
     * @param reloading 是否加载最新配置文件
     * @return
     */
    public static String readResource(String key, String resource, boolean reloading) {
        // String str = null;
        // if(reloading){
        // str = ResourceBundle.getBundle("properties/system").getString(key);
        // try {
        // if(str.equals(new String(str.getBytes("ISO-8859-1"), "ISO-8859-1"))){
        // str = new String(str.getBytes("ISO-8859-1"),
        // "UTF-8");//解决UTF-8格式的properties的中文乱码问题
        // }
        //
        // } catch (UnsupportedEncodingException e) {
        // throw new SystemException(e.getMessage());
        // }
        // }
        // return str;
        String value = null;
        PropertiesConfiguration cfg;
        try {
            cfg = new PropertiesConfiguration(resource);
            if (reloading) {
                cfg.setReloadingStrategy(new FileChangedReloadingStrategy());
            }
            value = cfg.getString(key);
            if (value.equals(new String(value.getBytes("ISO-8859-1"), "ISO-8859-1"))) {
                value = new String(value.getBytes("ISO-8859-1"), "UTF-8");// 解决UTF-8格式的properties的中文乱码问题
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 从配置文件中读取静态常量的值
     * @param key
     * @param reloading 是否加载最新配置文件
     * @return
     */
    public static String[] readResourceArray(String key, boolean reloading) {
        String[] value = null;
        PropertiesConfiguration cfg;
        try {
            cfg = new PropertiesConfiguration("application.properties");
            if (reloading) {
                cfg.setReloadingStrategy(new FileChangedReloadingStrategy());
            }
            value = cfg.getStringArray(key);

            for (String str : value) {
                if (str.equals(new String(str.getBytes("ISO-8859-1"), "ISO-8859-1"))) {
                    str = new String(str.getBytes("ISO-8859-1"), "UTF-8");// 解决UTF-8格式的properties的中文乱码问题
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 通用toString方法
     * @param bean 需要进行toString的对象bean
     * @return 返回对象原有toString值及对象属性
     */
    public static String toString(Object bean) {
        return ToStringBuilder.reflectionToString(bean, ToStringStyle.MULTI_LINE_STYLE);
    }

    /**
     * 获得指定包下的所有类
     * @param dir
     * @param pk
     * @return
     * @throws ClassNotFoundException
     */
    public static Class<?>[] getClasses(String packageName) {
        String path = packageName.replace('.', '/');
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        URL url = classloader.getResource(path);
        List<Class<?>> list = new ArrayList<Class<?>>();
        try {
            list = getClasses(new File(url.getFile()), packageName);
        } catch (ClassNotFoundException e) {
            System.out.println("找不到" + packageName);
        }
        return list.toArray(new Class[list.size()]);
    }

    /**
     * 迭代查找类
     * @param dir
     * @param pk
     * @return
     * @throws ClassNotFoundException
     */
    private static List<Class<?>> getClasses(File dir, String pk) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        if (!dir.exists()) {
            return classes;
        }
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                classes.addAll(getClasses(f, pk + "." + f.getName()));
            }
            String name = f.getName();
            if (name.endsWith(".class")) {
                classes.add(Class.forName(pk + "." + name.substring(0, name.length() - 6)));
            }
        }
        return classes;
    }

    /**
     * 生成n~m之间的随机整数(包括m、n)
     */
    public static int random(int n, int m) {
        Random rand = new Random();
        int r = rand.nextInt(m) % (m - n + 1) + n;
        return r;
    }

    public static final String substring(String str, int start, int end) {
        return StringUtils.substring(str, start, end);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> arrayToList(T[] source) {
        return CollectionUtils.arrayToList(source);
    }

    /** 深度复制 */
    @SuppressWarnings("unchecked")
    public static <T> T copy(T t) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(t);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
        return (T) ois.readObject();
    }

    /**
     * 功能:是否包含空字符串包含 空格
     * @author XT
     * @param strings
     * @return
     */
    public static Boolean hasBlank(Object... strings) {
        Boolean result = Boolean.FALSE;
        for (Object string : strings) {
            if (string instanceof String) {
                if (StringUtils.isBlank(string.toString())) {
                    result = Boolean.TRUE;
                    break;
                }
            } else {
                if (string == null) {// 不是String的对象
                    result = Boolean.TRUE;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 功能:所有都是空
     * @author XT
     * @param strings
     * @return
     */
    public static Boolean allIsBlank(Object... strings) {
        Boolean result = Boolean.TRUE;
        for (Object string : strings) {
            if (string instanceof String) {
                if (!StringUtils.isBlank(string.toString())) {
                    result = Boolean.FALSE;
                    break;
                }
            } else {
                if (string != null) {// 不是String的对象
                    result = Boolean.FALSE;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 功能:是否包含空字符串
     * @author XT
     * @param strings
     * @return
     */
    public static Boolean hasEmpty(String... strings) {
        Boolean result = Boolean.FALSE;
        for (String string : strings) {
            if (StringUtils.isEmpty(string)) {
                result = Boolean.TRUE;
                break;
            }
        }
        return result;
    }

    /**
     * @description: 添加url问号
     * @author ZZW
     * @param url
     * @return
     */
    public static String assemblyURLQuestionMark(String url) {
        if (StringUtils.isNotEmpty(url) && url.indexOf("?") == -1) {
            url = url.trim() + "?";
        }
        return url;
    }

    /**
     * @description: 拼装url参数格式
     * @author ZZW
     * @param param
     * @param value
     * @return
     */
    public static String assemblyURLParams(String param, String value) {
        return "&" + param + "=" + value;
    }

    public static Boolean parseSQLBoolean(String param) {
        return "1".equals(param) ? true : "0".equals(param) ? false : Boolean.parseBoolean(param);
    }

    /**
     * 将指定字符的首字母小写
     * @param str 需要转换的字符
     */
    public static String toLowerInitial(String str) {
        char ch[];
        ch = str.toCharArray();
        if (ch[0] >= 'A' && ch[0] <= 'Z') {
            ch[0] = (char) (ch[0] + 32);
        }
        return new String(ch);
    }

    /**
     * 将指定字符的首字母大写
     * @param str 需要转换的字符
     */
    public static String toUpperInitial(String str) {
        char ch[];
        ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

    /**
     * 功能:根据map参数构造url参数
     * @param param
     * @return
     */
    public static String getUrlParam(Map<String, Object> param) {
        if (param.size() == 0) {
            return "";
        }
        StringBuffer result = new StringBuffer();
        result.append("?");
        boolean firstLoop = true;
        for (String key : param.keySet()) {
            Object value = param.get(key) == null ? "" : key + "=" + param.get(key).toString();
            if (firstLoop) {
                firstLoop = false;
            } else {
                value = "&" + value;
            }
            result.append(value);
        }
        return result.toString();
    }

    /**
     * list 转字符串,为数据库使用
     * @param list
     * @param patten
     * @return
     */
    public static <T> String join(List<T> list, String patten) {
        if (list == null || list.size() == 0) {
            return "";
        } else {
            T t = list.get(0);
            if (t instanceof String) {
                StringBuilder sb = new StringBuilder();
                for (T s : list) {
                    sb.append("'");
                    sb.append(s);
                    sb.append("'");
                    sb.append(patten);
                }
                sb.deleteCharAt(sb.length() - 1);
                return sb.toString();
            } else {
                StringBuilder sb = new StringBuilder();
                for (T s : list) {
                    sb.append(s);
                    sb.append(patten);
                }
                sb.deleteCharAt(sb.length() - 1);
                return sb.toString();
            }
        }
    }

    /**
     * 生成32位的uuid
     * @return
     */
    public static String get32UUID() {
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
        return uuid;
    }

    /**
     * 表名转换为简写 把sys_user 转换为su工具方法
     * @tableName 表名
     */
    public static String tableNameToSimple(String tableName) {
        return tableNameToSimple(tableName, "_");
    }

    /**
     * 表名转换为简写 把sys_user 转换为su工具方法
     * @tableName 表名
     * @pattern 表名分隔符号
     */
    private static String tableNameToSimple(String tableName, String pattern) {
        String[] tns = tableName.split(pattern);
        StringBuilder sb = new StringBuilder();
        for (String string : tns) {
            sb.append(string.toCharArray()[0]);
        }
        return sb.toString().toLowerCase();
    }
    /**
     * 比较两个String 值是否相同
     * @param ignore 是否忽略大小写
     */
    public static Boolean compareString(String a,String b,Boolean ignore){
        if(a == null && b == null){
            return true;
        }else if(a==null && b!= null){
            return false;
        }else if(b==null && a!= null){
            return false;
        }else{
            if(ignore){
                return a.equalsIgnoreCase(b);
            }else{
                return a.equals(b);
            }
        }
    }
    /**
     * 获取密码
     * @param username 用户名
     * @param password md5密码
     * @param salt 颜值
     * @return
     * @throws Exception 
     */
    public static String getPasswordBySalt(String username, String password, String salt) {
        try {
            return EncodeDecodeUtils.md5Digest(username+password+salt);
        } catch (Exception e) {
            throw new ServerException(e);
        }
    }
    /**
     * 校验密码是否正确
     * @param adminPassword
     * @param username
     * @param password
     * @param salt
     * @return
     */
    public static Boolean checkLoginPassword(String adminPassword,String username,String password,String salt){
        return CommonUtils.compareString(adminPassword,
            CommonUtils.getPasswordBySalt(username, password, salt), false);
    }
    
    /**
     * 处理表名变为驼峰，首字母大写
     * @param entityNameStr
     * @return
     */
    public static String handleEntityName(String entityNameStr) {
        String []entityNameStrs = entityNameStr.split("_");
        StringBuilder sb = new StringBuilder();
        for (String entityName : entityNameStrs) {
            sb.append(toUpperInitial(entityName));
        }
        return sb.toString();
    }
    
}
