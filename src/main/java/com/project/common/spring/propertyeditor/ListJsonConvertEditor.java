package com.project.common.spring.propertyeditor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.project.common.bean.order.OrderSort;
import com.project.common.constant.SortEnum;
import com.project.common.spring.bean.ParamExt;
import com.project.common.util.FastJsonUtils;
import com.project.common.util.LogUtils;

/**
 * List格式转化
 * 参数格式为&list=[]  [{},{}]
 * @author XT
 * @version:1.0 2014-09
 */
public class ListJsonConvertEditor extends CustomCollectionEditor {

    @SuppressWarnings("rawtypes")
    public ListJsonConvertEditor(Class<? extends Collection> collectionType) {
        
        super(collectionType);
        
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.hasText(text)) {
            try {
                if (text.startsWith("[{") && text.endsWith("}]")) {// json数组
                    List<OrderSort> orderSorts = new ArrayList<OrderSort>();
                    Object[] objects = FastJsonUtils.toArray(text);
                    for (Object object : objects) {
                        JSONObject json = (JSONObject) object;
                        if (json.containsKey("sort") && json.containsKey("order")) {
                            if ("asc".equalsIgnoreCase(json.getString("sort"))) {
                                orderSorts.add(new OrderSort(json.getString("order"), SortEnum.ASC,json.getBoolean("convert")==null?true:json.getBoolean("convert")));
                            }
                            if ("desc".equalsIgnoreCase(json.getString("sort"))) {
                                orderSorts.add(new OrderSort(json.getString("order"), SortEnum.DESC,json.getBoolean("convert")==null?true:json.getBoolean("convert")));
                            }
                        }else{
                            setValue(FastJsonUtils.toArray(text, ParamExt.class));
                            return;
                        }
                    }
                    setValue(orderSorts);
                } else if (text.startsWith("[") && text.endsWith("]")) {
                    StringBuilder sb = new StringBuilder(text);
                    sb.deleteCharAt(0);
                    sb.deleteCharAt(sb.length()-1);
                    String result = sb.toString().replaceAll("\"", "").replaceAll("'", "");
                    super.setValue(result.split(","));
                } else {
                    super.setValue(text.split(","));
                }
            } catch (Exception e) {
                IllegalArgumentException iae = new IllegalArgumentException(text + e.getMessage());
                iae.initCause(e);
                LogUtils.ERROR(iae.getMessage());
                throw iae;
            }
        } else {
            setValue(null);
        }
    }
}
