package com.project.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
/**
 * list处理工具
 * ClassName: ListUtil <br/>
 * @author xt(di.xiao@karakal.com.cn)
 * @date 2016年12月31日
 * @version 1.0
 */
public class ListUtils {
    /**
     * 判断list是否是null或者list.size() == 0
     * @param list
     * @return
     */
    public static Boolean isEmpty(List<?> list){
        return list == null || list.size() == 0;
    }
    /**
     * 判断list是否是null或者list.size() == 0
     * @param list
     * @return
     */
    public static Boolean isEmpty(Object [] objects){
        return objects == null || objects.length == 0;
    }
    
    /**
     * Array 转list
     * @param es
     * @return
     */
    public static <E> List<E> arrayToList(@SuppressWarnings("unchecked") E ... es) {
        ArrayList<E> list = null;
        if (es != null) {
            list = new ArrayList<E>(es.length);
            for (E e : es)
                list.add( e);
        } else {
            list = new ArrayList<E>();
        }

        return list;
    }
    /**
     * list 转 array
     * @param es
     * @return
     */
    public static Object[] listToArray(List<?> list) {
        Object [] es = null;
        if (!isEmpty(list)) {
            es = new Object[list.size()];
            int i = 0;
            for (Object e : list){
                es[i++] = e;
            }
        }
        return es;
    }
    /**
     * 集合转list
     * @param collection
     * @return
     */
    public static <E> List<E> collectionToArrayList(Collection<E> collection) {
        if (collection != null) {
            List<E> list = new ArrayList<E>(collection.size());
            for (Iterator<E> i$ = collection.iterator(); i$.hasNext();) {
                E e = i$.next();
                list.add(e);
            }

            return list;
        }

        return null;
    }
}