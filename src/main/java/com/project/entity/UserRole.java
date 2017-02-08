package com.project.entity;

import javax.persistence.*;

@Table(name = "sys_user_role_rel")
public class UserRole {
    @Id
    @Column(name = "user_role_id")
    private Integer userRoleId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "role_id")
    private String roleId;

    /**
     * @return user_role_id
     */
    public Integer getUserRoleId() {
        return userRoleId;
    }

    /**
     * @param userRoleId
     */
    public void setUserRoleId(Integer userRoleId) {
        this.userRoleId = userRoleId;
    }

    /**
     * @return user_id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
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
}