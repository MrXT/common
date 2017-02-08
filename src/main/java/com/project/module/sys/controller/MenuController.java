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
import com.project.common.util.CommonUtils;
import com.project.common.util.ListUtils;
import com.project.common.util.ValidateUtils;
import com.project.module.sys.controller.vo.MenuVO;
import com.project.module.sys.service.MenuService;

/**
 * 功能：TODO
 * @author yelinsen
 * @version:2017-1-2
 */
@Controller
@RequestMapping("/sys/menu")
@ApiIgnore
public class MenuController {

    @Autowired
    private MenuService menuService;

    @RequestMapping(value = "/manage", method = RequestMethod.GET)
    public String manage(Map<String, Object> map, MenuVO menuVO) {
        SessionHolder.setMenuMap(map, "sys/menu");
        return "sys/menu_list";
    }

    @RequestMapping(value="/insert",method=RequestMethod.POST)
    @ResponseBody
    public Object insert(MenuVO condition) {
        if (CommonUtils.hasBlank(condition.getMenuName()) || condition.getMenuOrder() == null) {// 菜单名不能为空
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        return menuService.doInsert(condition);
    }

    @RequestMapping(value="/update",method=RequestMethod.POST)
    @ResponseBody
    public Object update(MenuVO condition) {
        if (CommonUtils.hasBlank(condition.getMenuId())) {// 菜单id不能为空
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        if (CommonUtils.allIsBlank(condition.getMenuName(), condition.getMenuUrl(), condition.getMenuOrder(),condition.getMenuIcon())) {
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        return menuService.doUpdate(condition);
    }

    @RequestMapping("/queryMenuTree")
    @ResponseBody
    public Object queryMenuTree(MenuVO menuVO) {
        return menuService.queryMenuTree(menuVO);
    }
    /**
     * 根据条件单表分页与不分页查询（默认不分页）
     * @param condition
     * @return
     */
    @RequestMapping(value="/query",method=RequestMethod.GET)
    @ResponseBody
    public Object query(MenuVO condition) {
        return menuService.queryMapByCondition(condition);
    }

    /**
     * 新增与保存
     * @param condition
     * @return
     */

    /**
     * 失效
     * @param condition
     * @return
     */
    @RequestMapping(value="/invalid",method=RequestMethod.POST)
    @ResponseBody
    public Object doInvalidate(MenuVO condition) {
        if (ValidateUtils.isBlank(condition.getId())) {
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        return menuService.doInvalidate(condition);
    }

    /**
     * 逻辑删除（批量）
     * @param condition
     * @return
     */
    @RequestMapping(value="/invalidBatch",method=RequestMethod.POST)
    @ResponseBody
    public Object doBatchInvalid(MenuVO condition) {
        if (ListUtils.isEmpty(condition.getIds())) {
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        return menuService.doBatchInvalidate(condition);
    }

    /**
     * 批量有效
     * @param condition
     * @return
     */
    @RequestMapping(value="/revalidBatch",method=RequestMethod.POST)
    @ResponseBody
    public Object batchRevalid(MenuVO condition) {
        if (ListUtils.isEmpty(condition.getIds())) {
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        return menuService.doBatchRevalidate(condition);
    }

    /**
     * 有效
     * @param condition
     * @return
     */
    @RequestMapping(value="/revalid",method=RequestMethod.POST)
    @ResponseBody
    public Object revalid(MenuVO condition) {
        if (ValidateUtils.isBlank(condition.getId())) {
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        return menuService.doRevalidate(condition);
    }

}