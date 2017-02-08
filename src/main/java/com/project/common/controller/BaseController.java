package com.project.common.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import springfox.documentation.annotations.ApiIgnore;

import com.project.common.bean.BaseEntity;
import com.project.common.constant.SystemConstant;
import com.project.common.exception.BusinessException;
import com.project.common.service.BaseService;
import com.project.common.util.ListUtils;
import com.project.common.util.ValidateUtils;

/**
 * 超类controller 封装增删改 ClassName: BaseController <br/>
 * @author xt(di.xiao@karakal.com.cn)
 * @date 2016年12月29日
 * @version 1.0
 * @param <T>
 */
public class BaseController <T extends BaseEntity> {

    @Autowired
    private BaseService<T> baseService;

    // @RequestMapping("/delete")
    // @ResponseBody
    // public Object delete(T condition){
    // if(ValidateUtils.isBlank(condition.getId())){
    // throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
    // }
    // return baseService.doDelete(condition);
    // }
    /**
     * 失效
     * @param condition
     * @return
     */
    @RequestMapping(value="/invalid",method=RequestMethod.POST)
    @ApiOperation(value="逻辑删除")  
    @ApiImplicitParams(value = {  
            @ApiImplicitParam(name = "id", value = "实体id",required=true,paramType="query", dataType = "String")
    })  
    @ResponseBody
    public Object doInvalidate(@ApiIgnore T condition) {
        if (ValidateUtils.isBlank(condition.getId())) {
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        return baseService.doInvalidate(condition);
    }

    /**
     * 逻辑删除（批量）
     * @param condition
     * @return
     */
    @RequestMapping(value="/invalidBatch",method=RequestMethod.POST)
    @ResponseBody
    @ApiIgnore
    public Object doBatchInvalid(T condition) {
        if (ListUtils.isEmpty(condition.getIds())) {
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        return baseService.doBatchInvalidate(condition);
    }

    /**
     * 批量有效
     * @param condition
     * @return
     */
    @RequestMapping(value="/revalidBatch",method=RequestMethod.POST)
    @ResponseBody
    @ApiIgnore
    public Object batchRevalid(T condition) {
        if (ListUtils.isEmpty(condition.getIds())) {
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        return baseService.doBatchRevalidate(condition);
    }

    /**
     * 有效
     * @param condition
     * @return
     */
    @RequestMapping(value="/revalid",method=RequestMethod.POST)
    @ApiOperation(value="逻辑恢复")  
    @ApiImplicitParams(value = {  
        @ApiImplicitParam(name = "id", value = "实体id",required=true,paramType="query", dataType = "String")
    })  
    @ResponseBody
    public Object revalid(@ApiIgnore T condition) {
        if (ValidateUtils.isBlank(condition.getId())) {
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        return baseService.doRevalidate(condition);
    }

    /**
     * 通过id查询实体
     * @param condition
     * @return
     */
    /*@RequestMapping(value="/queryById",method=RequestMethod.GET)
    @ResponseBody
    public Object queryById(T condition) {
        if (ValidateUtils.isBlank(condition.getId())) {
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        return baseService.queryByPrimaryKey(condition.getId());
    }*/
}
