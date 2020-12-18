package com.hopu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hopu.domain.Menu;
import com.hopu.mapper.MenuMapper;
import com.hopu.service.IMenuService;
import org.springframework.stereotype.Service;

//同样是增强，部分代码可以不用手写
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

}
