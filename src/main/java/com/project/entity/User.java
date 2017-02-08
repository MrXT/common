package com.project.entity;

import com.project.common.bean.BaseEntity;
import java.util.Date;
import javax.persistence.*;

@Table(name = "sys_user")
public class User extends BaseEntity {
    @Id
    @Column(name = "user_id")
    private String userId;

    /**
     * 用户名
     */
    private String username;

    public String getOnlyUsername(){
        if(username == null){ 
            return null;      
        }                     
        return "%"+username.trim()+"%";
    }

    /**
     * 真实名称
     */
    private String name;

    public String getOnlyName(){
        if(name == null){ 
            return null;      
        }                     
        return "%"+name.trim()+"%";
    }

    private Date ctime;

    /**
     * 密码
     */
    private String password;

    private String salt;

    private String phone;

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
     * 获取用户名
     *
     * @return username - 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     *
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取真实名称
     *
     * @return name - 真实名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置真实名称
     *
     * @param name 真实名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return ctime
     */
    public Date getCtime() {
        return ctime;
    }

    /**
     * @param ctime
     */
    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    /**
     * 获取密码
     *
     * @return password - 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return salt
     */
    public String getSalt() {
        return salt;
    }

    /**
     * @param salt
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }

    /**
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
}