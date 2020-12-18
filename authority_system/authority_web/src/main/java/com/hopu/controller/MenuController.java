package com.hopu.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hopu.domain.Menu;
import com.hopu.domain.RoleMenu;
import com.hopu.result.PageEntity;
import com.hopu.result.ResponseEntity;
import com.hopu.service.IMenuService;
import com.hopu.service.IRoleMenuService;
import com.hopu.utils.IconFontUtils;
import com.hopu.utils.UUIDUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.hopu.result.ResponseEntity.*;
@Controller
@RequestMapping("menu")
public class MenuController {
    @Autowired
    private IMenuService menuService;

    @Autowired
    private IRoleMenuService roleMenuService;

    @RequiresPermissions("menu:list")
    @RequestMapping("tolistPage")
    public String tomunuList(){

        return "admin/menu/menu_list";
    }


    @RequestMapping("list")
    @ResponseBody
    public PageEntity menuList(){
        //先查询父菜单
//        List<Menu> plist = menuService.list(new QueryWrapper<Menu>().eq("pid",0));
        List<Menu> plist = menuService.list(new QueryWrapper<Menu>().eq("pid","0"));

        ArrayList<Menu> menus = new ArrayList<>();
        //为何这里不把它的值收起来，因为传入的是引用类型，传入的是地址，改变的是值
        findChildMenu(plist,menus);
        PageEntity pageEntity = new PageEntity(menus.size(),menus);
        return pageEntity;



    }

    /**
     *
     * @param pList 传过来的父菜单集合，用来查它的子菜单
     * @param menus  返回的子菜单，为什么它可以被改变，因为List是引用类型，修改的是它的内容而不是地址·
     * @return
     */
    private List<Menu> findChildMenu(List<Menu> pList,List<Menu> menus){
        //先遍历父菜单，分别查它的儿子
        for (Menu menu:pList){
            //判断是否已经添加过menu了，未添加过才将他添加进menus
            if (!menus.contains(menu)){
                menus.add(menu);
            }
            //儿子的pid就是父亲的id，
            String pId = menu.getId();
            //当前父亲的所有儿子
            List<Menu> childList = menuService.list(new QueryWrapper<Menu>().eq("pid",pId));
            //实体类中的方法，个人感觉这种关系比较像树，猜测：就是将它的子树放进去
            menu.setNodes(childList);
            //就如同树一样，如果没有子节点，那么就是遍历到了最底层
            if (childList.size()>0){
                //递归调用
                menus=findChildMenu(childList,menus);
            }
        }

        return menus;
    }

    @RequiresPermissions("menu:add")
    @RequestMapping("toAddPage")
    public String toAddPage(HttpServletRequest request){
        //将所有的menu放到list中
        List<Menu> list = menuService.list(new QueryWrapper<Menu>(new Menu()).eq("pid","0"));
        findChildrens(list);
        request.setAttribute("list",list);
        List<String> iconFont = IconFontUtils.getIconFont();
        request.setAttribute("iconFont",iconFont);
        return "admin/menu/menu_add";
    }

    @PostMapping("save")
    @ResponseBody
    public ResponseEntity addPage(Menu menu){
        menu.setId(UUIDUtils.getID());
        menu.setCreateTime(new Date());
        menuService.save(menu);
        return success();
    }



    /**
     * 获取所有的子节点
     * @param list 顶级目录
     */
    private void findChildrens(List<Menu> list){
        for (Menu menu : list){
            List<Menu> list1 = menuService.list(new QueryWrapper<>(new Menu()).eq("pid",menu.getId()));
            if (list1!=null){
                menu.setNodes(list1);
            }
        }
    }

    @RequiresPermissions("menu:update")
    @RequestMapping("toUpdatePage")
    public String toUpdatePage(HttpServletRequest request,String id){
//        System.out.println(id);
        Menu menu = menuService.getById(id);
        List<Menu> list = menuService.list(new QueryWrapper<Menu>(new Menu()).eq("pid",'0').orderByAsc("seq"));
        findChildrens(list);
        List<String>iconFont = IconFontUtils.getIconFont();
        request.setAttribute("iconFont",iconFont);
        request.setAttribute("list",list);
        request.setAttribute("menu",menu);
        return "admin/menu/menu_update";
    }

    @ResponseBody
    @PostMapping("update")
    public ResponseEntity updateMenu(Menu menu){
        menu.setUpdateTime(new Date());
        menuService.updateById(menu);
        return success();
    }

    @RequiresPermissions("menu:delete")
    @ResponseBody
    @RequestMapping("delete")
    public ResponseEntity deleteMenu(@RequestBody ArrayList<Menu> menus){
        ArrayList<String> ids = new ArrayList<>();
        for (Menu menu:menus){
        ids.add(menu.getId());
    }
        menuService.removeByIds(ids);
        return success();
    }

    /**
     * 角色分配权限页面菜单查询，用来查询已有权限列表
     * @param roleId
     * @return
     */
    @RequestMapping("MenuList")
    @ResponseBody
    public PageEntity menuList(String roleId){
        //通过role_id查询当前角色已经关联了的权限

        List<RoleMenu> roleMenuList = roleMenuService.list(new QueryWrapper<RoleMenu>().eq("role_id",roleId));
        List<Menu> list = menuService.list();

        ArrayList<JSONObject> jsonObjects = new ArrayList<>();
        //判断角色已有权限，
        list.forEach(menu -> {

            //把对象转换为JSON格式
            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(menu));
            //获得List里面的所有id
            List<String> menuIds = roleMenuList.stream().map(roleMenu ->
                roleMenu.getMenuId()
            ).collect(Collectors.toList());

            if(menuIds.contains(menu.getId())){
                jsonObject.put("LAY_CHECKED",true);
            }
            jsonObjects.add(jsonObject);
        });
        return new PageEntity(jsonObjects.size(),jsonObjects);
    }


}
