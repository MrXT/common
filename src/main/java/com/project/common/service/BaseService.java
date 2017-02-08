package com.project.common.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.project.common.bean.QueryResult;
import com.project.common.bean.excel.bean.ExcelSort;


/**
 * ClassName: BaseService <br/>
 * Function: 基础Service. <br/>
 *
 * @author XT
 * @version 2015-01
 */
public interface BaseService<T> {
    /** 主键查询 */
    T queryByPrimaryKey(String id);
    /** 根据条件进行查询 ,返回map*/
    QueryResult<Map<String,Object>> queryMapByCondition(T condition);
    
    /** 根据条件进行查询导出excel文件*/
    void export(T condition,HttpServletResponse response,List<ExcelSort> excelSorts);
    /** 新增 */
    int doInsert(T entity);
    /** 修改 */
    int doUpdate(T entity);
    /** 删除 */
    int doDelete(T entity);
    /** 逻辑删除 */
    int doInvalidate(T entity);
    /** 逻辑恢复 */
	int doRevalidate(T entity);
	/** 保存 */
	int doSave(T record);
	/**
	 * 批量失效（逻辑删除）
	 * @param string
	 * @return
	 */
    int doBatchInvalidate(T entity);
    /**
     * 批量有效
     * @param string
     * @return
     */
    int doBatchRevalidate(T entity);
}

