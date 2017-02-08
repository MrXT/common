package com.project.module.sys.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import springfox.documentation.annotations.ApiIgnore;

import com.project.common.constant.SystemConstant;
import com.project.common.exception.BusinessException;
import com.project.common.spring.SessionHolder;
import com.project.common.util.ListUtils;
import com.project.common.util.ValidateUtils;
import com.project.module.sys.controller.vo.RoleVO;
import com.project.module.sys.service.RoleService;

/**
 * 功能：TODO
 * @author yelinsen
 * @version:2017-1-2
 */
@Controller
@RequestMapping("/sys/role")
@ApiIgnore
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 修改角色的权限菜单
     * @param roleVo
     * @return
     */
    @RequestMapping("/updateRoleMenu")
    @ResponseBody
    public Object updateRoleMenu(RoleVO roleVO) {
        if (ValidateUtils.isBlank(roleVO.getRoleId())) {
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        return roleService.updateRoleMenu(roleVO);
    }

    @RequestMapping(value = "/manage", method = RequestMethod.GET)
    public String manage(Map<String, Object> map, RoleVO roleVO) {
        //把增删改查权限放入map中
        SessionHolder.setMenuMap(map,"sys/role");
        return "sys/role_list";
    }
    /**
     * 根据条件单表分页与不分页查询（默认不分页）
     * @param condition
     * @return
     */
    @RequestMapping(value="/query",method=RequestMethod.GET)
    @ResponseBody
    public Object query(RoleVO condition) {
        return roleService.queryMapByCondition(condition);
    }

    /**
     * 新增与保存
     * @param condition
     * @return
     */
    @RequestMapping(value="/update",method=RequestMethod.POST)
    @ResponseBody
    public Object update(RoleVO condition) {
        if (ValidateUtils.isBlank(condition.getId())) {
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        return roleService.doUpdate(condition);
    }

    @RequestMapping(value="/insert",method=RequestMethod.POST)
    @ResponseBody
    public Object insert(RoleVO condition) {
        return roleService.doInsert(condition);
    }

    // @RequestMapping("/delete")
    // @ResponseBody
    // public Object delete(RoleVO condition){
    // if(ValidateUtils.isBlank(condition.getId())){
    // throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
    // }
    // return roleService.doDelete(condition);
    // }
    /**
     * 失效
     * @param condition
     * @return
     */
    @RequestMapping(value="/invalid",method=RequestMethod.POST)
    @ResponseBody
    public Object doInvalidate(RoleVO condition) {
        if (ValidateUtils.isBlank(condition.getId())) {
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        return roleService.doInvalidate(condition);
    }

    /**
     * 逻辑删除（批量）
     * @param condition
     * @return
     */
    @RequestMapping(value="/invalidBatch",method=RequestMethod.POST)
    @ResponseBody
    public Object doBatchInvalid(RoleVO condition) {
        if (ListUtils.isEmpty(condition.getIds())) {
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        return roleService.doBatchInvalidate(condition);
    }

    /**
     * 批量有效
     * @param condition
     * @return
     */
    @RequestMapping(value="/revalidBatch",method=RequestMethod.POST)
    @ResponseBody
    public Object batchRevalid(RoleVO condition) {
        if (ListUtils.isEmpty(condition.getIds())) {
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        return roleService.doBatchRevalidate(condition);
    }

    /**
     * 有效
     * @param condition
     * @return
     */
    @RequestMapping(value="/revalid",method=RequestMethod.POST)
    @ResponseBody
    public Object revalid(RoleVO condition) {
        if (ValidateUtils.isBlank(condition.getId())) {
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        return roleService.doRevalidate(condition);
    }

}