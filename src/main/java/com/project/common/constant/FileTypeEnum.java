package com.project.common.constant;
/**
 * 文件类型的定义
 * ClassName: FileTypeEnum <br/>
 * @author xt(di.xiao@karakal.com.cn)
 * @date 2016年12月30日
 * @version 1.0
 */
public enum FileTypeEnum {
    PIC("pic"), FILE("file"),APK("apk");
    private String value;  
    private FileTypeEnum(String value) {  
        this.value = value;  
    }  
    @Override  
    public String toString() {  
        return this.value;  
    } 
}
