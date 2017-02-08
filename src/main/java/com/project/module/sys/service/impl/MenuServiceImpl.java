package com.project.module.sys.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.project.common.service.impl.BaseServiceImpl;
import com.project.common.spring.SessionHolder;
import com.project.common.util.CommonUtils;
import com.project.common.util.FastJsonUtils;
import com.project.common.util.ListUtils;
import com.project.common.util.ValidateUtils;
import com.project.module.sys.controller.vo.MenuVO;
import com.project.module.sys.dao.MenuMapper;
import com.project.module.sys.dao.RoleMenuMapper;
import com.project.module.sys.service.MenuService;
import com.project.common.annotation.BaseAnnotation;
import com.project.common.constant.MenuTypeEnum;
import com.project.common.exception.BusinessException;
import com.project.entity.Menu;
import com.project.entity.RoleMenu;

/**
 * @author yelinsen
 * @version:2017-1-2
 */
@Service
public class MenuServiceImpl extends BaseServiceImpl<MenuVO> implements MenuService {

    @BaseAnnotation
    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;


    @Override
    public int doInsert(MenuVO record) {
        // 插入父菜单
        MenuVO menu = new MenuVO();
        menu.setParentId(record.getParentId());
        menu.setMenuName(record.getMenuName());
        menu.setQueryRepeat(true);
        if (!ListUtils.isEmpty(menuMapper.queryMapByCondition(menu))) {// 前提是同一个目录下，不会存在同名的菜单
            throw new BusinessException("新增失败，菜单名重复!");
        }
        record.setMenuType(MenuTypeEnum.DIR.getValue());
        record.setMenuName(record.getMenuName().trim());
        String url = record.getMenuUrl();
        if (ValidateUtils.isNotBlank(url)) {
            StringBuilder sb = new StringBuilder(record.getMenuUrl().replace("\\", "/"));
            while (sb.lastIndexOf("/") == sb.length() - 1) {// 去除最后一个特殊符号
                sb.deleteCharAt(sb.length() - 1);
            }
            while (sb.indexOf("/") == 0) {// 去除第一个特殊符号
                sb.deleteCharAt(0);
            }
            url = sb.toString();
            record.setMenuUrl(url);
        } else {
            record.setMenuUrl(null);
        }
        if (super.doInsert(record) > 0) {
            if (record.getHasButton() && ValidateUtils.isNotBlank(record.getMenuUrl())) {// 菜单地址不为空，且有按钮
                // 插入button
                String parentId = record.getMenuId();
                String invalid = "失效";
                menuMapper.insertSelective(new Menu(CommonUtils.get32UUID(), invalid, url.substring(0, url.lastIndexOf("/") + 1) + "invalid",
                    parentId, 3, MenuTypeEnum.BUTTON.getValue()));
                String revalid = "恢复";
                menuMapper.insertSelective(new Menu(CommonUtils.get32UUID(), revalid, url.substring(0, url.lastIndexOf("/") + 1) + "revalid",
                    parentId, 4, MenuTypeEnum.BUTTON.getValue()));
                String update = "修改";
                menuMapper.insertSelective(new Menu(CommonUtils.get32UUID(), update, url.substring(0, url.lastIndexOf("/") + 1) + "update", parentId,
                    2, MenuTypeEnum.BUTTON.getValue()));
                String insert = "新增";
                menuMapper.insertSelective(new Menu(CommonUtils.get32UUID(), insert, url.substring(0, url.lastIndexOf("/") + 1) + "insert", parentId,
                    1, MenuTypeEnum.BUTTON.getValue()));
                String query = "查询";
                menuMapper.insertSelective(new Menu(CommonUtils.get32UUID(), query, url.substring(0, url.lastIndexOf("/") + 1) + "query", parentId,
                    0, MenuTypeEnum.BUTTON.getValue()));
            }
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int doUpdate(MenuVO record) {
        Menu old = queryByPrimaryKey(record.getId());
        if (!CommonUtils.hasBlank(record.getMenuName())) {
            if (CommonUtils.compareString(old.getMenuName(), record.getMenuName(), false)
                && CommonUtils.compareString(old.getParentId(), record.getParentId(), false)) {
            } else {
                MenuVO menu = new MenuVO();
                menu.setParentId(record.getParentId());
                menu.setMenuName(record.getMenuName());
                menu.setQueryRepeat(true);
                if (!ListUtils.isEmpty(menuMapper.queryMapByCondition(menu))) {// 前提是同一个目录下，不会存在同名的菜单
                    throw new BusinessException("修改失败，菜单名重复!");
                }
            }
        }
        MenuVO menu = new MenuVO();
        menu.setMenuName(record.getMenuName());
        menu.setMenuUrl(record.getMenuUrl());
        menu.setMenuOrder(record.getMenuOrder());
        menu.setMenuIcon(record.getMenuIcon());
        menu.setMenuId(record.getMenuId());
        String url = record.getMenuUrl();
        if (ValidateUtils.isNotBlank(url)) {
            StringBuilder sb = new StringBuilder(record.getMenuUrl().replace("\\", "/"));
            while (sb.lastIndexOf("/") == sb.length() - 1) {// 去除最后一个特殊符号
                sb.deleteCharAt(sb.length() - 1);
            }
            while (sb.indexOf("/") == 0) {// 去除最后一个特殊符号
                sb.deleteCharAt(0);
            }
            url = sb.toString();
            menu.setMenuUrl(url);
        }

        if (CommonUtils.compareString(url, old.getMenuUrl(), true) || CommonUtils.allIsBlank(url, old.getMenuUrl())) {// 如果url地址并没有改变
        } else if (ValidateUtils.isBlank(url) && ValidateUtils.isNotBlank(old.getMenuUrl())) {// 如果旧的url不为空，新的为空，删除子菜单
            if (ValidateUtils.isBlank(record.getMenuIcon())) {// 如果修改菜单图标就不删除
                Menu deleteMenu = new Menu();
                deleteMenu.setParentId(menu.getMenuId());
                menuMapper.delete(deleteMenu);// 删除子菜单
            }
        } else if (ValidateUtils.isNotBlank(url) && ValidateUtils.isBlank(old.getMenuUrl())) {// 地址不为空，但是旧的为空，新增子菜单
            if (record.getHasButton() && ValidateUtils.isNotBlank(record.getMenuUrl())) {// 菜单地址不为空，且有按钮
                // 插入button
                String parentId = menu.getMenuId();
                String invalid = "失效";
                menuMapper.insertSelective(new Menu(CommonUtils.get32UUID(), invalid, url.substring(0, url.lastIndexOf("/") + 1) + "invalid",
                    parentId, 3, MenuTypeEnum.BUTTON.getValue()));
                String revalid = "恢复";
                menuMapper.insertSelective(new Menu(CommonUtils.get32UUID(), revalid, url.substring(0, url.lastIndexOf("/") + 1) + "revalid",
                    parentId, 4, MenuTypeEnum.BUTTON.getValue()));
                String update = "修改";
                menuMapper.insertSelective(new Menu(CommonUtils.get32UUID(), update, url.substring(0, url.lastIndexOf("/") + 1) + "update", parentId,
                    2, MenuTypeEnum.BUTTON.getValue()));
                String insert = "新增";
                menuMapper.insertSelective(new Menu(CommonUtils.get32UUID(), insert, url.substring(0, url.lastIndexOf("/") + 1) + "insert", parentId,
                    1, MenuTypeEnum.BUTTON.getValue()));
                String query = "查询";
                menuMapper.insertSelective(new Menu(CommonUtils.get32UUID(), query, url.substring(0, url.lastIndexOf("/") + 1) + "query", parentId,
                    0, MenuTypeEnum.BUTTON.getValue()));
            }
        } else {
            // 查询出下级子菜单
            Menu queryMenu = new Menu();
            queryMenu.setParentId(menu.getMenuId());
            for (Menu subMenu : menuMapper.select(queryMenu)) {
                if (subMenu.getMenuUrl().endsWith("query")) {
                    Menu updateMenu = new Menu();
                    updateMenu.setMenuUrl(url.substring(0, url.lastIndexOf("/") + 1) + "query");
                    updateMenu.setMenuId(subMenu.getMenuId());
                    menuMapper.updateByPrimaryKeySelective(updateMenu);
                }
                if (subMenu.getMenuUrl().endsWith("update")) {
                    Menu updateMenu = new Menu();
                    updateMenu.setMenuUrl(url.substring(0, url.lastIndexOf("/") + 1) + "update");
                    updateMenu.setMenuId(subMenu.getMenuId());
                    menuMapper.updateByPrimaryKeySelective(updateMenu);
                }
                if (subMenu.getMenuUrl().endsWith("insert")) {
                    Menu updateMenu = new Menu();
                    updateMenu.setMenuUrl(url.substring(0, url.lastIndexOf("/") + 1) + "insert");
                    updateMenu.setMenuId(subMenu.getMenuId());
                    menuMapper.updateByPrimaryKeySelective(updateMenu);
                }
                if (subMenu.getMenuUrl().endsWith("invalid")) {
                    Menu updateMenu = new Menu();
                    updateMenu.setMenuUrl(url.substring(0, url.lastIndexOf("/") + 1) + "invalid");
                    updateMenu.setMenuId(subMenu.getMenuId());
                    menuMapper.updateByPrimaryKeySelective(updateMenu);
                }
                if (subMenu.getMenuUrl().endsWith("revalid")) {
                    Menu updateMenu = new Menu();
                    updateMenu.setMenuUrl(url.substring(0, url.lastIndexOf("/") + 1) + "revalid");
                    updateMenu.setMenuId(subMenu.getMenuId());
                    menuMapper.updateByPrimaryKeySelective(updateMenu);
                }
            }
        }
        if (ValidateUtils.isBlank(record.getMenuIcon())) {
            old.setMenuName(record.getMenuName());
            old.setMenuUrl(ValidateUtils.isBlank(url) ? null : url);
            old.setMenuOrder(record.getMenuOrder());
        } else {
            old.setMenuIcon(record.getMenuIcon());
        }
        old.setMenuId(record.getMenuId());
        old.setDefaultValue();
        return menuMapper.updateByPrimaryKey(old);
    }

    @Override
    public Object queryMenuTree(MenuVO menuVO) {
        List<MenuVO> result = new ArrayList<MenuVO>();
        Menu queryMenu = new Menu();
        queryMenu.setValid(menuVO.getValid());
        PageHelper.startPage(1, 1000, "menu_order asc");
        List<Menu> menus = menuMapper.select(queryMenu);
        for (Menu menu : menus) {
            if (menuVO.getMenuType() != null && menuVO.getMenuType() == MenuTypeEnum.DIR.getValue()) {
                if (menu.getMenuType() != MenuTypeEnum.BUTTON.getValue()) {
                    MenuVO menuVo = FastJsonUtils.toBean(FastJsonUtils.toJSONString(menu), MenuVO.class);
                    result.add(menuVo);
                }
            } else {
                MenuVO menuVo = FastJsonUtils.toBean(FastJsonUtils.toJSONString(menu), MenuVO.class);
                result.add(menuVo);
            }
        }
        /**
         * 3.5 ztree不需要树状结构，2.6需要
         */
        // getTree(result,menus);
        RoleMenu roleMenu = new RoleMenu();
        roleMenu.setRoleId(menuVO.getId());
        setChecked(result, roleMenuMapper.select(roleMenu));
        return result;
    }

    /**
     * ztree 树，设置是否选中，设置是否禁用
     * @param result
     * @param roleMenus
     */
    private void setChecked(List<MenuVO> result, List<RoleMenu> roleMenus) {
        for (MenuVO menuVO : result) {
            for (RoleMenu roleMenu : roleMenus) {
                Boolean admin = (Boolean) SessionHolder.getSession().getAttribute("admin");
                if (!admin) {
                    if (menuVO.getMenuType() == MenuTypeEnum.SYSDIR.getValue()) {
                        menuVO.setChkDisabled(true);
                    }
                }
                if (CommonUtils.compareString(roleMenu.getMenuId(), menuVO.getMenuId(), true)) {
                    menuVO.setChecked(true);
                }
            }
            if (menuVO.getChidren().size() > 0) {
                setChecked(menuVO.getChidren(), roleMenus);
            }
        }
    }

    @Override
    public void getTree(List<MenuVO> result, List<Menu> menus) {
        for (MenuVO menuVO : result) {
            for (Menu menu : menus) {
                if (CommonUtils.compareString(menuVO.getMenuId(), menu.getParentId(), true)) {
                    MenuVO menuVo = FastJsonUtils.toBean(FastJsonUtils.toJSONString(menu), MenuVO.class);
                    menuVO.getChidren().add(menuVo);
                }
            }
            getTree(menuVO.getChidren(), menus);
        }
    }
}
