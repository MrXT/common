package com.project.module.sys.controller.vo;

import java.util.ArrayList;
import java.util.List;

import com.project.entity.Menu;

/**
 * 接受请求参数
 * @author yelinsen
 * @version:2017-1-2
 */
public class MenuVO extends Menu{
	//TODO 可以扩展参数
    private Boolean hasButton = false;//是否添加 button，默认否

    private Boolean queryRepeat = false;//是否查询重复（根据名字与父iD）
    
    private List<MenuVO> chidren = new ArrayList<MenuVO>();//孩子节点
    
    private Boolean queryRoot = false;//查询根目录
    
    private Boolean queryDir=false;//查询菜单目录，过滤掉按钮
    
    public Boolean getQueryDir() {
        return queryDir;
    }
    
    public void setQueryDir(Boolean queryDir) {
        this.queryDir = queryDir;
    }

    public Boolean getQueryRoot() {
        return queryRoot;
    }
    
    public void setQueryRoot(Boolean queryRoot) {
        this.queryRoot = queryRoot;
    }

    public List<MenuVO> getChidren() {
        return chidren;
    }
    
    public void setChidren(List<MenuVO> chidren) {
        this.chidren = chidren;
    }


    public Boolean getQueryRepeat() {
        return queryRepeat;
    }

    
    public void setQueryRepeat(Boolean queryRepeat) {
        this.queryRepeat = queryRepeat;
    }

    public Boolean getHasButton() {
        return hasButton;
    }
    
    public void setHasButton(Boolean hasButton) {
        this.hasButton = hasButton;
    }
    
    /**
     * 封装ztree格式
     */
    public List<MenuVO> getNodes(){
        return chidren;
    }
    
    public String getName(){
        return getMenuName();
    }
    public String getPId(){
        return getParentId();
    }
    /**
     * 是否选中
     */
    private Boolean checked = false;
    /**
     * 禁用节点
     */
    private Boolean chkDisabled = false;
    
    
    public Boolean getChkDisabled() {
        return chkDisabled;
    }

    
    public void setChkDisabled(Boolean chkDisabled) {
        this.chkDisabled = chkDisabled;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
    
    
}