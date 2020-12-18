package com.hopu.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hopu.domain.*;
import com.hopu.result.PageEntity;
import com.hopu.result.ResponseEntity;
import com.hopu.service.IRoleService;
import com.hopu.service.IUserRoleService;
import com.hopu.utils.UUIDUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.hopu.result.ResponseEntity.*;


@Controller
@RequestMapping("role")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IUserRoleService userRoleService;

    @RequiresPermissions("role:list")
    @RequestMapping("tolistPage")
    public String roleList() {
        return "admin/role/role_list";
    }

    @RequestMapping("list")
    @ResponseBody
    public IPage<Role> roleList(@RequestParam(value = "limit") Integer limit,
                                @RequestParam(value = "page") Integer page,
                                Model model, Role role) {
        Page<Role> page1 = new Page<>(page, limit);
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>(new Role());
        if (role != null) {
            if (!StringUtils.isEmpty(role.getRole())) {
                queryWrapper.like("role", role.getRole());
            }

        }
        IPage<Role> roleIPage = roleService.page(page1, queryWrapper);
//        System.out.println(roleIPage.getRecords());
        return roleIPage;

    }

    @RequiresPermissions("role:add")
    @RequestMapping("toAddPage")
    public String toAddPage() {
        return "admin/role/role_add";
    }

    @RequestMapping("save")
    @ResponseBody
    public ResponseEntity addRole(Role role) {

        Role role2 = new Role();
        role2.setRole(role.getRole());
        Role role1 = roleService.getOne(new QueryWrapper<Role>(role2));
        if (role1 != null) {
            //它是静态方法，引包需要静态引包
            return error("角色已经存在");
        }
        role.setId(UUIDUtils.getID());
        role.setCreateTime(new Date());
        roleService.save(role);
        return success();
    }

    @RequiresPermissions("role:update")
    @RequestMapping("toUpdatePage")
    public String updateRole(Model model, String id) {
        Role role = roleService.getById(id);
        model.addAttribute("role", role);
        return "admin/role/role_update";
    }

    @ResponseBody
    @RequestMapping("update")
    public ResponseEntity updateRole(Role role) {
        System.out.println(role.getId());
        role.setUpdateTime(new Date());
        roleService.updateById(role);
        return success();
    }


    @RequiresPermissions("role:delete")
    @ResponseBody
    @RequestMapping("delete")
    //requestBody主要用来接收前端传递给后端的json字符串中的数据的（请求体中的数据的）
    //GET无请求体，必须用post方法提交数据
    public ResponseEntity deleteRole(@RequestBody ArrayList<Role> roles) {
        System.out.println("do it");
        try {
            roles.forEach(role -> {
                System.out.println(role.getId());
                roleService.removeById(role.getId());
            });
        } catch (Exception e) {
            e.printStackTrace();
            error(e.getMessage());
        }
        return success();
    }

    @RequiresPermissions("role:setMenu")
    @RequestMapping("toSetMenuPage")
    public String toSetMenuPage(String id, HttpServletRequest request) {
        request.setAttribute("role_id", id);
        return "admin/role/role_setMenu";
    }

    @PostMapping("setMenu")
    @ResponseBody
    public ResponseEntity setMenu(String id, @RequestBody ArrayList<Menu> menus) {
        roleService.setMenu(id, menus);
        return success();
    }

    @RequestMapping("roleList")
    @ResponseBody
    public PageEntity menuList(String userId, Role role) {
        //通过user_id查询当前角色已经关联了的权限
        List<UserRole> userRoleList = userRoleService.list(new QueryWrapper<UserRole>().eq("user_id", userId));

        QueryWrapper<Role> queryWrapper = new QueryWrapper<Role>();
        if (role != null) {
            if (!StringUtils.isEmpty(role.getRole())) queryWrapper.like("role", role.getRole());
        }
        List<Role> roles = roleService.list(queryWrapper);

        List<JSONObject> list = new ArrayList<JSONObject>();
        for (Role role2 : roles) {
            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(role2));
            boolean rs = false;
            for (UserRole userRole : userRoleList) {
                if (userRole.getRoleId().equals(role2.getId())) {
                    rs = true;
                }
            }

            jsonObject.put("LAY_CHECKED", rs);
            list.add(jsonObject);
        }
        return new PageEntity(list.size(), list);
    }

}
