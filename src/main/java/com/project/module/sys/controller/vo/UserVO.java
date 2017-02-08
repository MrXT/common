package com.project.module.sys.controller.vo;

import java.util.List;

import com.project.entity.Menu;
import com.project.entity.Role;
import com.project.entity.User;

/**
 * 接受请求参数
 * @author yelinsen
 * @version:2017-1-2
 */
public class UserVO extends User {

    private Boolean isCascade = true;//是否级联查询role与menu 默认true

    public Boolean getIsCascade() {
        return isCascade;
    }

    public void setIsCascade(Boolean isCascade) {
        this.isCascade = isCascade;
    }

    private String vcode;// 验证码

    private List<Role> roles;// 封装用户角色对象

    private List<Menu> menus;// 封装用户菜单对象

    public String getVcode() {
        return vcode;
    }

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    private List<String> roleIds;// 为用户分配角色

    public List<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<String> roleIds) {
        this.roleIds = roleIds;
    }

}