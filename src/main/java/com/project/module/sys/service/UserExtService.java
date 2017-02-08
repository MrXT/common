package com.project.module.sys.service;

import com.project.module.sys.controller.vo.UserVO;
/**
 * 用户业务逻辑处理
 * ClassName: UserService <br/>
 * @author xt(di.xiao@karakal.com.cn)
 * @date 2017年1月20日
 * @version 1.0
 */
public interface UserExtService{
    
    /**
     * 修改用户对应的角色
     * @param roleVO
     * @return
     */
    Object updateUserRole(UserVO userVO);
    /**
     * 
     * @param user
     * @return
     */
    Object queryUserByCondition(UserVO user);
    /**
     * 重置密码
     * @param userVO
     * @return
     */
    Object updateResetPassword(UserVO userVO);

}