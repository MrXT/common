
package com.project.common.bean.excel.bean;

/**
 * excel列的顺序
 * ClassName: ExcelSort <br/>
 * @author xt(di.xiao@karakal.com.cn)
 * @date 2016年10月24日
 * @version 1.0
 */
public class ExcelSort {
    private String key;//列表头对应的文字
    private String value;//数据对应的字段名
    
    public ExcelSort() {

    }
    
    public ExcelSort(String key, String value) {
        super();
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }
    
    public void setKey(String key) {
        this.key = key;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
}

