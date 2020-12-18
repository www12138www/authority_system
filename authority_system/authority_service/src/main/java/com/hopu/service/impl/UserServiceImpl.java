package com.hopu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hopu.domain.*;
import com.hopu.mapper.UserMapper;
import com.hopu.service.IUserRoleService;
import com.hopu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IUserRoleService userRoleService;

    @Override
    public User getUserByUserName(String userName) {
        User user =new User();
        user.setUserName(userName);
        User userList = userMapper.selectOne( new QueryWrapper<User>(user));
        return userList;
    }

    @Override
    public void setRole(String id, List<Role> roles) {
        //RoleMenu是将role和menu绑定在一起的，通过role_id找到之前绑定的，并删除它,防止重复
        userRoleService.remove(new QueryWrapper<UserRole>(new UserRole()).eq("user_id",id));
        for (Role role:roles){
            UserRole userRole = new UserRole();
            userRole.setUserId(id);
            userRole.setRoleId(role.getId());
            //保存
            userRoleService.save(userRole);
        }
    }
}
