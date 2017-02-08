package com.project.module.sys.service;

import java.util.List;

import com.project.module.sys.controller.vo.MenuVO;
import com.project.common.service.BaseService;
import com.project.entity.Menu;
/**
 * 功能：TODO
 * @author yelinsen
 * @version:2017-1-2
 */
public interface MenuService extends BaseService<MenuVO>{
    /**
     * 查询所有的菜单（树状展示）
     * @return
     */
    Object queryMenuTree(MenuVO menuVO);
    /**
     * 处理菜单结果树
     * @param result
     * @param menus
     * @return
     */
    void getTree(List<MenuVO> result, List<Menu> menus);

}