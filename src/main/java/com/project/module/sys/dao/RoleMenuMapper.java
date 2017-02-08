package com.project.module.sys.dao;

import java.util.List;

import com.project.common.util.MyMapper;
import com.project.entity.Menu;
import com.project.entity.RoleMenu;
import com.project.module.sys.controller.vo.UserVO;

public interface RoleMenuMapper extends MyMapper<RoleMenu> {
    /**
     * 通过用户来查询他的菜单
     * @param resultUser
     * @return
     */
    List<Menu> queryMenuByUser(UserVO resultUser);
}