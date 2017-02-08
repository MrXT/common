package com.project.module.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import springfox.documentation.annotations.ApiIgnore;

import com.project.common.constant.SystemConstant;
import com.project.common.exception.BusinessException;
import com.project.common.util.CommonUtils;
import com.project.common.util.ValidateUtils;
import com.project.module.sys.controller.vo.UserVO;
import com.project.module.sys.service.RoleService;
import com.project.module.sys.service.UserExtService;

/**
 * 功能：TODO
 * @author yelinsen
 * @version:2017-1-2
 */
@Controller
@RequestMapping("/sys/user")
@ApiIgnore
public class UserExtController{
	@Autowired
	private UserExtService userService;
	@Autowired
	private RoleService roleService;
	
	@Value("${defaultPassword}")
	private String password;//后台页面管理员添加用户，与重置密码时使用的默认密码
	
	/**
	 * 重置密码
	 * @param condition
	 * @return
	 */
    @ResponseBody
    @RequestMapping("updateResetPassword")
    public Object updateResetPassword(UserVO condition) {
        if(CommonUtils.hasBlank(condition.getUserId())){
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        UserVO userVO = new UserVO();
        userVO.setPassword(password);
        userVO.setUserId(condition.getUserId());
        condition.setPassword(password);
        return userService.updateResetPassword(userVO);
    }
	/**
     * 修改用户的角色
     * @param roleVo
     * @return
     */
    @RequestMapping("/updateUserRole")
    @ResponseBody
    public Object updateUserRole(UserVO userVO){
        if(ValidateUtils.isBlank(userVO.getUserId()) ){
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        return userService.updateUserRole(userVO);
    }

}