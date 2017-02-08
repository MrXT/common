package com.project.common.service.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.project.common.annotation.BaseAnnotation;
import com.project.common.annotation.Sort;
import com.project.common.bean.Async;
import com.project.common.bean.BaseEntity;
import com.project.common.bean.QueryResult;
import com.project.common.bean.excel.bean.ExcelSort;
import com.project.common.bean.order.OrderSort;
import com.project.common.constant.SystemConstant;
import com.project.common.exception.BusinessException;
import com.project.common.exception.ServerException;
import com.project.common.service.BaseService;
import com.project.common.util.CommonUtils;
import com.project.common.util.DateUtils;
import com.project.common.util.ExcelUtils;
import com.project.common.util.ListUtils;
import com.project.common.util.MapUtils;
import com.project.common.util.MyMapper;
import com.project.common.util.ReflectUtils;

/**
 * ClassName: BaseService <br/>
 * Function: Service实现类基类. <br/>
 * @author XT
 * @version 2015-08
 */
@Service
public class BaseServiceImpl <T extends BaseEntity> implements BaseService<T> {

    @Autowired
    private Async async;

    @SuppressWarnings("unchecked")
    public MyMapper<T> getBaseDAO() {
        Class<? extends Annotation> annatation = BaseAnnotation.class;
        return (MyMapper<T>) BaseEntity.getFieldByAnnatation(this, BaseAnnotation.class, annatation);
    }

    @Override
    public QueryResult<Map<String, Object>> queryMapByCondition(T condition) {
        if (condition.getPage()) {
            if (!ListUtils.isEmpty(condition.getOrderSorts())) {
                PageHelper.startPage(condition.getPageNo(), condition.getPageSize(), getOrderBy(condition));
            } else {// 默认
                PageHelper.startPage(condition.getPageNo(), condition.getPageSize());
            }
        }
        List<Map<String, Object>> list = getBaseDAO().queryMapByCondition(condition);
        return new QueryResult<Map<String, Object>>(new PageInfo<Map<String, Object>>(list));
    }

    /**
     * 组装排序字段
     * @param condition
     * @return
     */
    private String getOrderBy(T condition) {
        String simpleTableName = ReflectUtils.getSimpleTable(condition.getClass());
        if (CommonUtils.hasEmpty(simpleTableName)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (OrderSort orderSort : condition.getOrderSorts()) {
            if(orderSort.getConvert()){
                Field field = ReflectUtils.getDeclaredField(condition, orderSort.getOrder());
                if (field != null) {
                    if (field.getAnnotation(Sort.class) != null) {
                        sb.append(field.getAnnotation(Sort.class).value()+" "+orderSort.getSort()+",");
                    }else{
                        if (field.getAnnotation(Column.class) == null) {
                            sb.append(simpleTableName + "." + orderSort.getOrder() + " " + orderSort.getSort() + ",");
                        } else {
                            sb.append(simpleTableName + "." + field.getAnnotation(Column.class).name() + " " + orderSort.getSort() + ",");
                        }
                    }
                }
            }else{
                sb.append(orderSort.getOrder()+" "+orderSort.getSort()+",");
            }
        }
        if (sb.length() > 1) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    @Override
    public T queryByPrimaryKey(String id) {
        return id == null ? null : getBaseDAO().selectByPrimaryKey(id);
    }

    /**
     * 加上事物@Transactional，子类重写该方法，才能进行事物回滚
     * @see com.project.common.service.BaseService#doInsert(java.lang.Object)
     */
    @Transactional
    @Override
    public int doInsert(T record) {
        record.setDefaultValue();
        record.setId(CommonUtils.get32UUID());
        return getBaseDAO().insertSelective(record);
    }

    @SuppressWarnings("unchecked")
    @Transactional
    @Override
    public int doUpdate(T record) {
        T queryRecord;
        try {
            queryRecord = (T) record.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ServerException(e);
        }
        queryRecord.setId(record.getId());
        List<Map<String, Object>> records = new ArrayList<Map<String,Object>>();
        if(record.getApp() && record.getRequstForward()){//如果是app请求且请求重构
            if(ReflectUtils.getDeclaredField(record, "userId") == null){//如果该对象没有userId，表示不能增删改
                throw new BusinessException(SystemConstant.NO_AUTH,"没有权限！");
            }
            ReflectUtils.setFieldValue(queryRecord, "userId", ReflectUtils.getFieldValue(record, "userId"));
        }
        records = getBaseDAO().queryMapByCondition(queryRecord);
        if (ListUtils.isEmpty(records)) {
            throw new BusinessException(SystemConstant.NULL_OBJECT);
        }
        record.setDefaultValue();
        return getBaseDAO().updateByPrimaryKeySelective(record);
    }

    @Transactional
    @Override
    public int doDelete(T entity) {
        T record = update(entity);
        return getBaseDAO().deleteByPrimaryKey(record.getId());
    }

    @Transactional
    @Override
    public int doInvalidate(T entity) {
        T record = update(entity);
        record.setValid(false);
        return getBaseDAO().updateByPrimaryKeySelective(record);
    }

    @Transactional
    @Override
    public int doRevalidate(T entity) {
        T record = update(entity);
        record.setValid(true);
        return getBaseDAO().updateByPrimaryKeySelective(record);
    }
    /**
     * 如果通过app接口访问，参数会添加app=true&userId=当前登录用户ID
     * @param condition
     * @return
     */
    private T update(T condition) {
        if(condition.getApp()&& condition.getRequstForward()){
            if(ReflectUtils.getDeclaredField(condition, "userId") == null){//如果该对象没有userId，表示不能增删改
                throw new BusinessException(SystemConstant.NO_AUTH,"没有权限！");
            }
        }
        List<Map<String, Object>> records = getBaseDAO().queryMapByCondition(condition);
        if (ListUtils.isEmpty(records)) {
            throw new BusinessException(SystemConstant.NULL_OBJECT);
        }
        @SuppressWarnings("unchecked")
        T record = (T) MapUtils.mapToBean(records.get(0), condition.getClass());
        record.setDefaultValue();
        return record;
    }

    @Transactional
    @Override
    public int doSave(T record) {
        T old = queryByPrimaryKey(record.getId());
        // TODO 可以根据业务进行调整
        if (old == null) {
            return doInsert(record);
        } else {
            return doUpdate(record);
        }
    }

    @Override
    public void export(T condition, HttpServletResponse response, List<ExcelSort> excelSorts) {
        List<Map<String, Object>> users = queryMapByCondition(condition).getList();
        ExcelUtils.export(users, excelSorts, DateUtils.toMailDateString(new Date()), response);
    }

    @Transactional
    @Override
    public int doBatchInvalidate(T entity) {
        for (String id : entity.getIds()) {
            entity.setId(id);
            doInvalidate(entity);
        }
        return entity.getIds().size();
    }

    @Transactional
    @Override
    public int doBatchRevalidate(T entity) {
        for (String id : entity.getIds()) {
            entity.setId(id);
            doRevalidate(entity);
        }
        return entity.getIds().size();
    }

}
