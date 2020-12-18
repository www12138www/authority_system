package com.hopu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hopu.domain.Menu;
import com.hopu.domain.Role;
import com.hopu.domain.RoleMenu;
import com.hopu.mapper.RoleMapper;
import com.hopu.service.IRoleMenuService;
import com.hopu.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    //role和menu连接
    @Autowired
    private IRoleMenuService roleMenuService;


    @Override
    public void setMenu(String id, List<Menu> menus) {
        //RoleMenu是将role和menu绑定在一起的，通过role_id找到之前绑定的，并删除它,防止重复
    roleMenuService.remove(new QueryWrapper<RoleMenu>(new RoleMenu()).eq("role_id",id));
    for (Menu menu:menus){
        RoleMenu roleMenu = new RoleMenu();
        roleMenu.setRoleId(id);
        roleMenu.setMenuId(menu.getId());
        //保存
        roleMenuService.save(roleMenu);
    }
    }
}
