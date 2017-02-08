package com.project.entity;

import com.project.common.bean.BaseEntity;

import java.util.Date;

import javax.persistence.*;

@Table(name = "sys_menu")
public class Menu extends BaseEntity {
    @Id
    @Column(name = "MENU_ID")
    private String menuId;

    @Column(name = "MENU_NAME")
    private String menuName;

    
    public String getOnlyMenuName(){
        if(menuName == null){ 
            return null;      
        }                     
        return "%"+menuName.trim()+"%";
    }

    @Column(name = "MENU_URL")
    private String menuUrl;

    @Column(name = "PARENT_ID")
    private String parentId;

    @Column(name = "MENU_ORDER")
    private Integer menuOrder;

    @Column(name = "MENU_ICON")
    private String menuIcon;

    private Date ctime;

    /**
     * 0:系统菜单，1：业务菜单，2：按钮
     */
    @Column(name = "MENU_TYPE")
    private Integer menuType;

    /**
     * @return MENU_ID
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

    /**
     * @return MENU_NAME
     */
    public String getMenuName() {
        return menuName;
    }

    /**
     * @param menuName
     */
    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    /**
     * @return MENU_URL
     */
    public String getMenuUrl() {
        return menuUrl;
    }

    /**
     * @param menuUrl
     */
    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    /**
     * @return PARENT_ID
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * @param parentId
     */
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    /**
     * @return MENU_ORDER
     */
    public Integer getMenuOrder() {
        return menuOrder;
    }

    /**
     * @param menuOrder
     */
    public void setMenuOrder(Integer menuOrder) {
        this.menuOrder = menuOrder;
    }

    /**
     * @return MENU_ICON
     */
    public String getMenuIcon() {
        return menuIcon;
    }

    /**
     * @param menuIcon
     */
    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
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
     * @return MENU_TYPE
     */
    public Integer getMenuType() {
        return menuType;
    }

    
    public Menu() {

    }
    public Menu(String menuId,String menuName, String menuUrl, String parentId, Integer menuOrder, Integer menuType) {
        super();
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuUrl = menuUrl;
        this.parentId = parentId;
        this.menuOrder = menuOrder;
        this.menuType = menuType;
    }

    /**
     * @param menuType
     */
    public void setMenuType(Integer menuType) {
        this.menuType = menuType;
    }

    public Menu(String menuName,String parentId) {
        super();
        this.menuName = menuName;
        this.parentId = parentId;
    }
    
}