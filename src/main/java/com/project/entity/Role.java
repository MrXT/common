package com.project.entity;

import com.project.common.bean.BaseEntity;
import java.util.Date;
import javax.persistence.*;

@Table(name = "sys_role")
public class Role extends BaseEntity {
    /**
     * 角色id
     */
    @Id
    @Column(name = "ROLE_ID")
    private String roleId;

    /**
     * 角色名
     */
    @Column(name = "ROLE_NAME")
    private String roleName;

    public String getOnlyRoleName(){
        if(roleName == null){ 
            return null;      
        }                     
        return "%"+roleName.trim()+"%";
    }

    /**
     * 创建时间
     */
    private Date ctime;

    /**
     * 获取角色id
     *
     * @return ROLE_ID - 角色id
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * 设置角色id
     *
     * @param roleId 角色id
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    /**
     * 获取角色名
     *
     * @return ROLE_NAME - 角色名
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * 设置角色名
     *
     * @param roleName 角色名
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * 获取创建时间
     *
     * @return ctime - 创建时间
     */
    public Date getCtime() {
        return ctime;
    }

    /**
     * 设置创建时间
     *
     * @param ctime 创建时间
     */
    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }
}