package com.project.module.sys.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.common.annotation.BaseAnnotation;
import com.project.common.service.impl.BaseServiceImpl;
import com.project.entity.RoleMenu;
import com.project.module.sys.controller.vo.RoleVO;
import com.project.module.sys.dao.RoleMapper;
import com.project.module.sys.dao.RoleMenuMapper;
import com.project.module.sys.service.RoleService;
/**
 * @author yelinsen
 * @version:2017-1-2
 */
 @Service
public class RoleServiceImpl extends BaseServiceImpl<RoleVO> implements RoleService{
	@BaseAnnotation
	@Autowired
	private RoleMapper roleMapper;
	
	@Autowired
    private RoleMenuMapper roleMenuMapper;

    @Override
    public Object updateRoleMenu(RoleVO roleVO) {
        //删除以前的
        RoleMenu roleMenu = new RoleMenu();
        roleMenu.setRoleId(roleVO.getRoleId());
        roleMenuMapper.delete(roleMenu);
        //添加最新的
        List<RoleMenu> roleMenus = new  ArrayList<RoleMenu>();
        if(roleVO.getMenuIds() != null){
            for (String menuId : roleVO.getMenuIds()) {
                RoleMenu addRoleMenu = new RoleMenu();
                addRoleMenu.setRoleId(roleVO.getRoleId());
                addRoleMenu.setMenuId(menuId);
                roleMenus.add(addRoleMenu);
            }
        }
        return roleMenus.size()!=0?roleMenuMapper.insertList(roleMenus):0;
    }
    
}
