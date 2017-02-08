
package com.project.common.spring.bean;

/**
 * 请求参数扩展
 * 该类通用list 作为参数传递时的处理(格式如下)
 * &list=[{"id":"素材id","keyword":"素材关键字","idExt":"扩展id"}]
 * ClassName: ParamBean <br/>
 * @author xt(di.xiao@karakal.com.cn)
 * @date 2017年1月1日
 * @version 1.0
 */
public class ParamExt {
    private String id;//素材id
    private String keyword;//关键字
    private String idExt;//id 扩展
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getKeyword() {
        return keyword;
    }
    
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    
    public String getIdExt() {
        return idExt;
    }
    
    public void setIdExt(String idExt) {
        this.idExt = idExt;
    }
}

