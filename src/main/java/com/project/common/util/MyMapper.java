package com.project.common.util;

import java.util.List;
import java.util.Map;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
    //FIXME 特别注意，该接口不能被扫描到，否则会出错
    /**
     * 根据扩展条件来查询数据库返回map
     * @param condition
     * @return
     */
    List<Map<String,Object>> queryMapByCondition(T condition);
}
