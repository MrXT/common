
package com.project.common.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import springfox.documentation.annotations.ApiIgnore;

import com.project.common.annotation.ApiResponseDesc;
import com.project.common.annotation.ApiResponseDescs;
import com.project.common.constant.MenuTypeEnum;
import com.project.common.constant.SystemConstant;
import com.project.common.exception.BusinessException;
import com.project.common.spring.SessionHolder;
import com.project.common.util.CommonUtils;
import com.project.common.util.FastJsonUtils;
import com.project.common.util.ValidateCodeUtil;
import com.project.common.util.ValidateUtils;
import com.project.entity.Menu;
import com.project.module.sys.controller.vo.MenuVO;
import com.project.module.sys.controller.vo.UserVO;
import com.project.module.sys.service.MenuService;
import com.project.module.sys.service.UserExtService;

/**
 * 登录使用
 * ClassName: LoginController <br/>
 * @author xt(di.xiao@karakal.com.cn)
 * @date 2017年1月20日
 * @version 1.0
 */
@RequestMapping("login")
@Controller
@Api("登录-接口")
public class LoginController {
    @Autowired
    private UserExtService userService;
    
    @Autowired
    private MenuService menuService;
    /**
     * 主页面
     * @param user
     * @return
     */
    @RequestMapping("/index")
    @ApiIgnore
    public String index(HttpSession session,Map<String,Object> map){
        return main(session,map);
    }
    /**
     * 跳转登录页面
     * @param user
     * @return
     */
    @RequestMapping("/login")
    @ApiIgnore
    public String login(){
        return "login";
    }
    /**
     * 跳转主页面
     * @param user
     * @return
     */
    @RequestMapping("/main")
    @ApiIgnore
    public String main(HttpSession session,Map<String, Object> map){
        if(session == null){
            return login();
        }
        UserVO user = SessionHolder.getUser(UserVO.class);
        if(user == null){
            return login();
        }
        List<MenuVO> result = new ArrayList<MenuVO>();
        List<Menu> menus = new ArrayList<Menu>();
        List<Menu> userMenus = user.getMenus();
        for (Menu menu : userMenus) {
            if(ValidateUtils.isBlank(menu.getParentId())){
                MenuVO menuVo = FastJsonUtils.toBean(FastJsonUtils.toJSONString(menu),MenuVO.class);
                result.add(menuVo);
            }
            if(menu.getMenuType()==MenuTypeEnum.DIR.getValue()){
                menus.add(menu);
            }
        }
        Collections.sort(result, new Comparator<MenuVO>() {

            @Override
            public int compare(MenuVO o1, MenuVO o2) {
                return o1.getMenuOrder()-o2.getMenuOrder();
            }
        });
        Collections.sort(menus, new Comparator<Menu>() {
            @Override
            public int compare(Menu o1, Menu o2) {
                return o1.getMenuOrder()-o2.getMenuOrder();
            }
        });
        menuService.getTree(result, menus);
        map.put("menus",result);
        return "main";
    }
    /**
     * 获取验证码
     * @throws IOException 
     */
    @RequestMapping(value="/vcode",method=RequestMethod.GET)
    @ApiOperation("获取验证码")
    public void code(@ApiIgnore HttpServletResponse response,@ApiIgnore HttpServletRequest request) throws IOException{
        String vcode = ValidateCodeUtil.createRandomCode();
        request.getSession().setAttribute("vcode", vcode);
        response.setContentType("image/jpeg");
        ValidateCodeUtil.output(vcode, response.getOutputStream());
    }
    /**
     * 实现登录逻辑
     * @param user
     * @return
     */
    @RequestMapping(value = "/doLogin",method=RequestMethod.POST)
    @ApiOperation(value="登录",response=Object.class)
    @ApiImplicitParams({
        @ApiImplicitParam(value="获取验证码接口返回的验证码",name="vcode",dataType="String",required=true,paramType="query"),
        @ApiImplicitParam(value="用户名",name="username",dataType="String",required=true,paramType="query"),
        @ApiImplicitParam(value="密码(md5加密)",name="password",dataType="String",required=true,paramType="query")
    })
    @ApiResponseDescs({
        @ApiResponseDesc(description="当前登录用户ID")
    })
    @ResponseBody
    public Object doLogin(@ApiIgnore UserVO user,@ApiIgnore HttpSession session){
        if(session.getAttribute("vcode") == null){
            throw new BusinessException("验证码失效！");
        }
        if(ValidateUtils.isBlank(user.getVcode())){
            throw new BusinessException("验证码不能为空！");
        }
        if(!CommonUtils.compareString(user.getVcode(), session.getAttribute("vcode").toString(), true)){
            throw new BusinessException("验证码错误！");
        }
        if(CommonUtils.hasBlank(user.getUsername(),user.getPassword())){
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        UserVO object = (UserVO) userService.queryUserByCondition(user);
        return object.getUserId();
    }
    /**
     * tab页
     * @return
     */
    @RequestMapping("/tab")
    @ApiIgnore
    public String tab(){
        return "tab";
    }
    /**
     * 默认页（主页）
     * @return
     */
    @RequestMapping("/default")
    @ApiIgnore
    public String defaultpage(){
        return "default";
    }
    
}

