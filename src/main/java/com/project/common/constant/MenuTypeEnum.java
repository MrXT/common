
package com.project.common.constant;

/**
 * 业务菜单（DIR），按钮(BUTTON),系统菜单(SYSDIR)
 * ClassName: MenuTypeEnum <br/>
 * @author xt(di.xiao@karakal.com.cn)
 * @date 2017年1月4日
 * @version 1.0
 */
public enum MenuTypeEnum {
    BUTTON("按钮",2),DIR("业务菜单",1),SYSDIR("系统菜单",0);
    private String name;
    private int value;
    private MenuTypeEnum(String name,int value) {
        this.name = name;
        this.value = value;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getValue() {
        return value;
    }
    
    public void setValue(int value) {
        this.value = value;
    }
    
}

