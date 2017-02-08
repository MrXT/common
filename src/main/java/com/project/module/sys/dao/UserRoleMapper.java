package com.project.module.sys.dao;

import java.util.List;
import java.util.Map;

import com.project.common.util.MyMapper;
import com.project.entity.UserRole;
import com.project.module.sys.controller.vo.UserVO;

public interface UserRoleMapper extends MyMapper<UserRole> {
    /**
     * 查询角色
     * @param resultUser
     * @return
     */
    List<Map<String,Object>> queryRoleByUser(UserVO resultUser);

}