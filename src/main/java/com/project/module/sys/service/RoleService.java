package com.project.module.sys.service;

import com.project.module.sys.controller.vo.RoleVO;
import com.project.common.service.BaseService;
/**
 * 功能：TODO
 * @author yelinsen
 * @version:2017-1-2
 */
public interface RoleService extends BaseService<RoleVO>{
    /**
     * 修改角色对应的菜单权限
     * @param roleVO
     * @return
     */
    Object updateRoleMenu(RoleVO roleVO);

}