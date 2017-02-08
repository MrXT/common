package com.project.module.sys.controller.vo;

import java.util.List;

import com.project.entity.Role;

/**
 * 接受请求参数
 * @author yelinsen
 * @version:2017-1-2
 */
public class RoleVO extends Role{
	//TODO 可以扩展参数
    private List<String> menuIds;//菜单ID数组
    
    public List<String> getMenuIds() {
        return menuIds;
    }
    
    public void setMenuIds(List<String> menuIds) {
        this.menuIds = menuIds;
    }
    
    
    
}