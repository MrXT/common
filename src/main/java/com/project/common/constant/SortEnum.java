
package com.project.common.constant;

/**
 * 排序方式
 * ClassName: SortEnum <br/>
 * @author xt(di.xiao@karakal.com.cn)
 * @date 2017年1月1日
 * @version 1.0
 */
public enum SortEnum {
    ASC("asc"),DESC("desc"); 
    private String value;
    private SortEnum(String value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return value;
    }
}

