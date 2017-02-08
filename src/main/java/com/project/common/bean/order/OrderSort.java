package com.project.common.bean.order;

import com.project.common.constant.SortEnum;

/**
 * 排序实体 ClassName: OrderSort <br/>
 * @author xt(di.xiao@karakal.com.cn)
 * @date 2017年1月1日
 * @version 1.0
 */
public class OrderSort {

    private SortEnum sort;

    private String order;
    
    private Boolean convert = true;//true：查询语句对应的table的字段排序，false：不转换，直接拼接查询的排序sql
    
    
    public Boolean getConvert() {
        return convert;
    }

    
    public void setConvert(Boolean convert) {
        this.convert = convert;
    }


    public SortEnum getSort() {
        return sort;
    }

    
    public void setSort(SortEnum sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public OrderSort() {

    }

    public OrderSort(String order, SortEnum sortEnum,Boolean convert) {
        super();
        this.sort = sortEnum;
        this.order = order;
        this.convert = convert;
    }

}
