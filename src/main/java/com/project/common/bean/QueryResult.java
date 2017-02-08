
package com.project.common.bean;


import com.github.pagehelper.PageInfo;

import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
/**
 * 分页信息
 * ClassName: QueryResult <br/>
 * @author xt(di.xiao@karakal.com.cn)
 * @date 2017年1月1日
 * @version 1.0
 * @param <T>
 */
public class QueryResult<T>
{
    @ApiModelProperty("页码")
  private Integer pageNo;
    @ApiModelProperty("页面大小")
  private Integer pageSize;
  private List<T> list = new ArrayList<T>();
  @ApiModelProperty("总记录数")
  private Long totalCount = Long.valueOf(0L);
  @ApiModelProperty("总页数")
  private Integer pageTotal = Integer.valueOf(0);
  @ApiModelProperty("开始行")
  private Integer startRow = Integer.valueOf(0);

  public QueryResult() {
  }
  public void addRow(T row) {
    if (row == null) {
      return;
    }
    this.list.add(row);
  }

  public QueryResult(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public QueryResult(PageInfo<T> pageInfo) {
    this.pageNo = Integer.valueOf(pageInfo.getPageNum());
    this.pageSize = Integer.valueOf(pageInfo.getPageSize());
    this.list = pageInfo.getList();
    this.totalCount = Long.valueOf(pageInfo.getTotal());
    this.pageTotal = Integer.valueOf(pageInfo.getPages());
    this.startRow = Integer.valueOf(pageInfo.getStartRow());
  }

  public QueryResult(Integer pageNo, Integer pageSize, Integer pageTotal, Integer startRow) {
    this.pageNo = pageNo;
    this.pageSize = pageSize;
    this.pageTotal = pageTotal;
    this.startRow = startRow;
  }

  public Integer getPageNo() {
    return this.pageNo;
  }

  public void setPageNo(Integer pageNo) {
    this.pageNo = pageNo;
  }

  public Integer getPageSize() {
    return this.pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public List<T> getList() {
    return this.list;
  }

  public void setList(List<T> list) {
    this.list = list;
  }

  public Long getTotalCount() {
    return this.totalCount;
  }

  public void setTotalCount(Long totalCount) {
    this.totalCount = totalCount;
  }

  public Integer getPageTotal() {
    return this.pageTotal;
  }

  public void setPageTotal(Integer pageTotal) {
    this.pageTotal = pageTotal;
  }

  public Integer getStartRow() {
    return this.startRow;
  }

  public void setStartRow(Integer startRow) {
    this.startRow = startRow;
  }
}