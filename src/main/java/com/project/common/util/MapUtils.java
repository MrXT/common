package com.project.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("rawtypes")
public class MapUtils {
    /**
     * 
     * @param objs
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <V> Map<String, V> createMap(Object ... objs) {
        HashMap map = new HashMap();
        if (objs == null) {
            return map;
        }
        if (objs.length % 2 != 0) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < objs.length; i += 2) {
            map.put(objs[i], objs[(i + 1)]);
        }

        return map;
    }
    public static <T> T mapToBean(Map<?,Object> map,Class<T> clazz){
        return JSONObject.parseObject(JSONObject.toJSONString(map), clazz);
    }
    /**
     * 把list转换 为map  《groupKey,List<Map<K,V>>》，默认删除groupKey
     * @param maps
     * @param groupKey
     * @return
     */
    public static <K, V> Map<V, List<Map<K, V>>> mapGroupByKey(List<Map<K, V>> maps, String groupKey) {
        return mapGroupByKey(maps,groupKey,true);
    }
    
    /**
     * 把list转换 为map  《groupKey,List<Map<K,V>>》
     * @param maps list
     * @param groupKey 
     * @param delGroupKey 是否删除
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<V, List<Map<K, V>>> mapGroupByKey(List<Map<K, V>> maps, String groupKey, boolean delGroupKey) {
        Map groupMap = new HashMap();
        for (Map m : maps) {
            addMapList(groupMap, m.get(groupKey), m);
            if (delGroupKey) {
                m.remove(groupKey);
            }
        }

        return groupMap;
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, List<V>> addMapList(Map<K, List<V>> map, K key, V value) {
        List list = (List) map.get(key);
        if (list == null) {
            list = new ArrayList();
            map.put(key, list);
        }
        list.add(value);

        return map;
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Map<V, Map<K, V>> listMapToMap(List<Map<K, V>> list, String key) {
        Map map = new HashMap();
        for (Map m : list) {
            map.put(m.get(key), m);
        }
        return map;
    }

    public static <K, V> void replaceMapKey(Map<K, V> map, Map<K, K> keyMap) {
        for (Iterator i$ = keyMap.keySet().iterator(); i$.hasNext();) {
            Object key = i$.next();
            map.put(keyMap.get(key), map.get(key));
            map.remove(key);
        }
    }

    @SuppressWarnings("unchecked")
    public static <K, V> void replaceListMapKey(List<Map<K, V>> maps, Map<K, K> keyMap) {
        for (Map map : maps)
            replaceMapKey(map, keyMap);
    }

    public static String formatCode(Map<String, Boolean> map, List<String> fields, String trueCode, String falseCode, String nullCode) {
        StringBuffer buffer = new StringBuffer();
        for (String f : fields) {
            Boolean v = (Boolean) map.get(f);
            if (v == null)
                buffer.append(nullCode);
            else if (v.booleanValue())
                buffer.append(trueCode);
            else {
                buffer.append(falseCode);
            }
        }

        return buffer.toString();
    }
}