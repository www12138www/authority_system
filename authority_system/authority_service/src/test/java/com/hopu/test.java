package com.hopu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hopu.domain.User;
import com.hopu.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class test {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void test01(){
        Map<String,Object> columnMap = new HashMap<>();
        columnMap.put("user_name","root");
        List<User> userList = userMapper.selectByMap(columnMap);
        System.out.println("0000000000"+userList);

    }

    @Test
    public void test02(){
        User user =new User();
        user.setUserName("root");
        System.out.println(user);
        User userList = userMapper.selectOne( new QueryWrapper<User>(user));
        System.out.println(userList);

    }
}
