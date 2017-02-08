
package com.project.common.bean.excel.bean;

import com.project.common.bean.excel.annotation.Excel;
/**
 * excel文件上传规范
 * ClassName: ExcelRequestBean <br/>
 * @author xt(di.xiao@karakal.com.cn)
 * @date 2016年12月28日
 * @version 1.0
 */
public class ExcelRequestBean {
    @Excel("歌曲ID")
    private Long songId;
    @Excel("歌曲名称")
    private String name;
    
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public Long getSongId() {
        return songId;
    }
    public void setSongId(Long songId) {
        this.songId = songId;
    }
    
}

