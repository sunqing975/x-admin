package com.admin.sys.service.impl;

import com.admin.sys.entity.Menu;
import com.admin.sys.mapper.MenuMapper;
import com.admin.sys.service.IMenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sunqing
 * @since 2023-03-16
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Resource
    private MenuMapper menuMapper;

    @Override
    public List<Menu> getAllMenu() {
        // 查询一级菜单
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId, 0);
        List<Menu> rootMenus = menuMapper.selectList(queryWrapper);
        // 查询二级菜单
        setMenuChildren(rootMenus);
        return rootMenus;

    }

    @Override
    public List<Menu> getMenuListByUserId(Integer userId) {
        // 查询一级子菜单
        List<Menu> menuList = menuMapper.getMenuListByUserId(userId, 0);

        setMenuChildrenByUserId(userId, menuList);
        return menuList;
    }

    private void setMenuChildrenByUserId(Integer userId, List<Menu> menuList) {
        if (menuList != null) {
            for (Menu menu : menuList) {
                List<Menu> subMenuList = menuMapper.getMenuListByUserId(userId, menu.getMenuId());
                menu.setChildren(subMenuList);
                // 递归
                setMenuChildrenByUserId(userId, subMenuList);
            }
        }
    }

    private void setMenuChildren(List<Menu> rootMenus) {
        if (rootMenus != null) {
            for (Menu menu : rootMenus) {
                LambdaQueryWrapper<Menu> subQueryWrapper = new LambdaQueryWrapper<>();
                subQueryWrapper.eq(Menu::getParentId, menu.getMenuId());
                List<Menu> subMenus = menuMapper.selectList(subQueryWrapper);
                menu.setChildren(subMenus);
                // 如果是多级菜单，就使用递归的方式
                setMenuChildren(subMenus);
            }
        }
    }
}
