package com.project.module.sys.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.project.common.annotation.BaseAnnotation;
import com.project.common.constant.SystemConstant;
import com.project.common.exception.BusinessException;
import com.project.common.spring.SessionHolder;
import com.project.common.util.CommonUtils;
import com.project.common.util.FastJsonUtils;
import com.project.common.util.ListUtils;
import com.project.common.util.MapUtils;
import com.project.entity.Menu;
import com.project.entity.Role;
import com.project.entity.User;
import com.project.entity.UserRole;
import com.project.module.sys.controller.vo.UserVO;
import com.project.module.sys.dao.RoleMenuMapper;
import com.project.module.sys.dao.UserMapper;
import com.project.module.sys.dao.UserRoleMapper;
import com.project.module.sys.service.UserExtService;

/**
 * @author yelinsen
 * @version:2017-1-2
 */
@Service
public class UserExtServiceImpl implements UserExtService {

    @BaseAnnotation
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Value("${admin}")
    private String admin;

    @Value("${salt}")
    private String salt;

    @Value("${adminPassword}")
    private String adminPassword;

    @Override
    public Object updateUserRole(UserVO userVO) {
        // 删除以前的
        UserRole userRole = new UserRole();
        userRole.setUserId(userVO.getUserId());
        userRoleMapper.delete(userRole);
        // 添加最新的
        List<UserRole> userRoles = new ArrayList<UserRole>();
        if (userVO.getRoleIds() != null) {
            for (String roleId : userVO.getRoleIds()) {
                UserRole addUserRole = new UserRole();
                addUserRole.setUserId(userVO.getUserId());
                addUserRole.setRoleId(roleId);
                userRoles.add(addUserRole);
            }
        }
        return userRoles.size() != 0 ? userRoleMapper.insertList(userRoles) : 0;
    }

    /**
     * 登录使用
     * @see com.project.module.sys.service.UserExtService#queryUserByCondition(com.project.module.sys.controller.vo.UserVO)
     */
    @Override
    public Object queryUserByCondition(UserVO user) {
        UserVO resultUser = new UserVO();
        if (CommonUtils.compareString(user.getUsername(), admin, true)) {// 如果是超级管理员
            resultUser.setUsername(admin);
            resultUser.setName(admin);
            resultUser.setPassword(adminPassword);
            resultUser.setSalt(salt);//查询有效的角色，有效的菜单
        } else {
            UserVO queryUser = new UserVO();
            queryUser.setUsername(user.getUsername());
            queryUser.setValid(true);
            List<User> users = userMapper.select(queryUser);
            if (ListUtils.isEmpty(users)) {
                throw new BusinessException("用户不存在！");
            }
            resultUser = FastJsonUtils.beanToBean(users.get(0), UserVO.class);
            resultUser.setValid(true);//查询有效的角色，有效的菜单
        }
        if (!CommonUtils.checkLoginPassword(resultUser.getPassword(), user.getUsername(), user.getPassword(), resultUser.getSalt())) {
            throw new BusinessException("密码错误！");
        }
        if (user.getIsCascade()) {
            List<Map<String, Object>> roleMaps = userRoleMapper.queryRoleByUser(resultUser);
            List<Role> roles = new ArrayList<Role>();
            for (Map<String, Object> map : roleMaps) {
                roles.add(MapUtils.mapToBean(map, Role.class));
            }
            resultUser.setRoles(roles);
            resultUser.setMenus(roleMenuMapper.queryMenuByUser(resultUser));
            List<String> passUrls = new ArrayList<String>();
            for (Menu menu : resultUser.getMenus()) {
                passUrls.add(menu.getMenuUrl());
            }
            // 将url放进session
            SessionHolder.getSession().setAttribute(SystemConstant.PASS_URLS, passUrls);
        }
     // 将id放进session
        SessionHolder.getSession().setAttribute(SystemConstant.DEFAULT_ID, resultUser.getUserId());
        resultUser.setPassword(null);
     // 将user放进session
        SessionHolder.getSession().setAttribute(SystemConstant.DEFAULT_SESSION_USER, resultUser);
        SessionHolder.getSession().setAttribute("admin",CommonUtils.compareString(user.getUsername(), admin, true)?true:false);
        return resultUser;
    }

    @Override
    public Object updateResetPassword(UserVO userVO) {
        User old = userMapper.selectByPrimaryKey(userVO.getUserId());
        if (old == null) {
            throw new BusinessException(SystemConstant.NULL_OBJECT);
        }
        userVO.setPassword(CommonUtils.getPasswordBySalt(old.getUsername(), userVO.getPassword(), old.getSalt()));
        userVO.setDefaultValue();
        return userMapper.updateByPrimaryKeySelective(userVO);
    }
}
