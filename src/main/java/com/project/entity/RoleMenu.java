package com.project.entity;

import javax.persistence.*;

@Table(name = "sys_role_menu_rel")
public class RoleMenu {
    @Id
    @Column(name = "role_menu_id")
    private Integer roleMenuId;

    @Column(name = "role_id")
    private String roleId;

    @Column(name = "menu_id")
    private String menuId;

    /**
     * @return role_menu_id
     */
    public Integer getRoleMenuId() {
        return roleMenuId;
    }

    /**
     * @param roleMenuId
     */
    public void setRoleMenuId(Integer roleMenuId) {
        this.roleMenuId = roleMenuId;
    }

    /**
     * @return role_id
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * @param roleId
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    /**
     * @return menu_id
     */
    public String getMenuId() {
        return menuId;
    }

    /**
     * @param menuId
     */
    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}