package com.hopu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hopu.domain.Role;
import com.hopu.domain.User;
import com.hopu.result.ResponseEntity;
import com.hopu.service.IUserService;
import com.hopu.utils.ShiroUtils;
import com.hopu.utils.UUIDUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.hopu.result.ResponseEntity.*;

@Controller
@RequestMapping("user")
public class UserController {
	@Autowired
	private IUserService userService;

//	@ResponseBody
//	@RequestMapping("list")
//	public ResponseEntity<List<User>> userList(){
//        List<User> userList = userService.list();
//        return new ResponseEntity<List<User>>(userList, HttpStatus.FOUND);
//	}


	@RequiresPermissions("user:list")
	@RequestMapping("toListPage")
	public String userList(){
		return "admin/user/user_list";
	}


	@RequestMapping("list")
	@ResponseBody
	public IPage<User> userList(@RequestParam(value = "page") int page,
								@RequestParam(value = "limit") int limit,
								User user,
								Model model){
		//利用mybatis-plus 框架自带的分页工具
		Page<User> page2 = new Page<User>(page,limit);

		QueryWrapper<User> queryWrapper = new QueryWrapper<>(new User());
		if (user!=null){
			if (!StringUtils.isEmpty(user.getUserName())){
				queryWrapper.like("user_name",user.getUserName());
			}
			if (!StringUtils.isEmpty(user.getTel())){
				queryWrapper.like("tel",user.getTel());
			}
			if (!StringUtils.isEmpty(user.getEmail())){
				queryWrapper.like("email",user.getEmail());
			}
		}
		//在page2的基础上进行模糊查询的，当我们让page2为null，就都没有数据了
		IPage<User> userIPage = userService.page(page2,queryWrapper);
		return userIPage;

	}

	@RequiresPermissions("user:add")
	@RequestMapping("toAddPage")
	public String toAddPage(){
		return "admin/user/user_add";
	}


	@RequestMapping("save")
	@ResponseBody
	public ResponseEntity addUser(User user
			,String userImg

	)throws IOException {

		//判断是否有重复名
		System.out.println(user);
		System.out.println(userImg);
		User user1 = userService.getUserByUserName(user.getUserName());
		if (user1!=null){
			return error("用户名已存在");
		}
//		String name = makeImg(userImg);
//		System.out.println(name);

		ShiroUtils.encPass(user);
		user.setUserImg(userImg);
		user.setId(UUIDUtils.getID());
		user.setSalt(UUIDUtils.getID());
		user.setCreateTime(new Date());

		userService.save(user);
		return success();
	}

	@RequestMapping("test")
	@ResponseBody
	public ResponseEntity test(@RequestParam("upload") MultipartFile multipartFile){
		File file = new File("D:\\mynginx\\nginx-1.18.0\\html");
		ResponseEntity responseEntity = new ResponseEntity();
		User user = new User();
		String newFileName = UUID.randomUUID()+ multipartFile.getOriginalFilename();
		try {
			multipartFile.transferTo(new File(file,newFileName));

		}catch (IOException e) {
			e.printStackTrace();
		}
		user.setUserImg(newFileName);
		responseEntity.setData(user);
		responseEntity.setMsg("操作成功");
		responseEntity.setResult(true);
		System.out.println(user.getUserImg());
		return responseEntity;
	}

	@RequiresPermissions("user:update")
	@RequestMapping("toUpdatePage")
	public String toUpdatePage(String id,Model model){
		User user = userService.getById(id);
		model.addAttribute("user",user);
		return "admin/user/user_update";
	}

	@ResponseBody
	@RequestMapping("update")
	public ResponseEntity updateUser(User user,@RequestParam("userImg") String userImg){
		System.out.println(userImg);
		ShiroUtils.encPass(user);
		user.setUpdateTime(new Date());
		user.setUserImg(userImg);
		userService.updateById(user);
		return success();
	}

//	@RequiresPermissions("user:delete")
	@ResponseBody
	@RequestMapping("delete")
		public ResponseEntity deleteUser(@RequestBody ArrayList<User> users){
		try {
			users.forEach(user ->{

				if (!"root".equals(user.getUserName()))
				userService.removeById(user.getId());
			});
		}catch (Exception e){

			e.printStackTrace();
			return error(e.getMessage());
		}

		return success();
	}

	@RequestMapping("toSetRolePage")
	public String toSetRolePage(String id , HttpServletRequest request){
		request.setAttribute("user_id",id);
		return "admin/user/user_setRole";
	}

	@RequiresPermissions("user:setRole")
	@RequestMapping("setRole")
	@ResponseBody
	public ResponseEntity setRole(String id,@RequestBody ArrayList<Role> roles){
		
		userService.setRole(id,roles);
		return success();
	}






}

