package com.project.common.bean;

import io.swagger.annotations.ApiModelProperty;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.Transient;

import com.project.common.bean.order.OrderSort;
import com.project.common.exception.BusinessException;
import com.project.common.spring.SessionHolder;
import com.project.common.util.DateUtils;

/**
 * 集成超类 ClassName: BaseEntity <br/>
 * @author xt(di.xiao@karakal.com.cn)
 * @date 2016年12月28日
 * @version 1.0
 */
public class BaseEntity {

    public static int ROWS = 0;
    
    public static String ADMIN_ID = "1";//超级管理员的ID

    // transient fastjson不会处理
    @Transient
    private transient int pageNo = 1;// 页码

    /**
     * 页面大小
     */
    @Transient
    private transient int pageSize;// 页面大小

    @Transient
    private transient Boolean page = true;// 是否分页(默认分页)
    @Transient
    private transient Boolean app = false;// APP访问
    @Transient
    private transient Boolean requstForward = false;//是否重构请求，可以判断当前登录用户是否有权限
    
    public Boolean getRequstForward() {
        return requstForward;
    }
    public void setRequstForward(Boolean requstForward) {
        this.requstForward = requstForward;
    }
    @Transient
    private transient List<String> ids;//实体的id数组，批量逻辑删除时使用
    @Transient
    private transient String keyword;// 查询关键字
    @Transient
    private transient Date startDate;// 开始时间
    @Transient
    private transient Date endDate;// 结束时间
    @Transient
    private transient List<OrderSort> orderSorts;// 排序
    private String uid;// 最后操作人
    private Date utime;// 最后操作时间
    private Date ctime;// 记录创建时间
    private Boolean valid;// 有效,默认查询有效数据
    
    public String getKeyword() {
        return keyword;
    }
    @ApiModelProperty(hidden=true)
    public String getOnlyKeyword(){
        if(keyword == null){
            return null;
        }
        return "%"+keyword.trim()+"%";
    }
    
    public Boolean getApp() {
        return app;
    }
    
    public void setApp(Boolean app) {
        this.app = app;
    }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    public List<OrderSort> getOrderSorts() {
        return orderSorts;
    }
    public void setOrderSorts(List<OrderSort> orderSorts) {
        this.orderSorts = orderSorts;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Date getUtime() {
        return utime;
    }

    public void setUtime(Date utime) {
        this.utime = utime;
    }

    
    public Date getCtime() {
        return ctime;
    }

    
    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public Boolean getPage() {
        return page;
    }

    public void setPage(Boolean page) {
        this.page = page;
    }

    public void setDefaultValue() {
        this.utime = new Date();
        this.uid = SessionHolder.getId() == null ? ADMIN_ID :SessionHolder.getId();
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize == 0 ? BaseEntity.ROWS : pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    @ApiModelProperty(hidden=true)
    public String getId() {
        return getFieldByAnnatation(this, String.class, Id.class);
    }

    public void setId(String id) {
        setFieldByAnnatation(this, id, Id.class);
    }
    
    
    public Date getStartDate() {
        return startDate;
    }

    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    
    public Date getEndDate() {
        if(endDate != null){
            if(DateUtils.toMailDateString(endDate).endsWith("0000")){
                return DateUtils.addDay(endDate, 1);
            }else{
                return endDate;
            }
        }else{
            return null;
        }
        
    }

    public static int getROWS() {
        return ROWS;
    }
    
    public static void setROWS(int rOWS) {
        ROWS = rOWS;
    }
    
    public List<String> getIds() {
        return ids;
    }
    
    public void setIds(List<String> ids) {
        this.ids = ids;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * 功能:获取指定对象中指定注解的对象值.
     * @author XT
     * @version 2015-01
     * @param thisObj 指定的对象
     * @param returnType 返回类型
     * @param annatation 注解类型
     * @return 若指定对象中存在多个相同指定注解，仅返回第一个找到的注解值
     */
    @SuppressWarnings("unchecked")
    public static <K> K getFieldByAnnatation(Object thisObj, Class<K> returnType, Class<? extends Annotation> annatation) {
        for (Class<?> superClass = thisObj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {// 如果子类中没有该属性则向上转型
            Field[] fields = superClass.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(annatation)) {
                    try {
                        field.setAccessible(true);
                        K k = (K) field.get(thisObj);
                        field.setAccessible(false);
                        return k;
                    } catch (Exception e) {
                        throw new BusinessException(e);
                    }
                }
            }
        }
        return null;
    }

    /**
     * 功能:为指定对象中注解的属性域赋值.
     * @author WJK
     * @version 2015-08
     * @param thisObj 指定的对象
     * @param value 赋值
     * @param annatation 注解类型
     * @return 若指定对象中存在多个相同指定注解，仅返回第一个找到的注解值
     */
    public static void setFieldByAnnatation(Object thisObj, Object value, Class<? extends Annotation> annatation) {
        for (Class<?> superClass = thisObj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {// 如果子类中没有该属性则向上转型
            Field[] fields = superClass.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(annatation)) {
                    try {
                        field.setAccessible(true);
                        field.set(thisObj, value);
                        field.setAccessible(false);
                        return;
                    } catch (Exception e) {
                        throw new BusinessException(e);
                    }
                }
            }
        }
    }
}
