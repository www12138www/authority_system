<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%--	<meta http-equiv="Content-Type" content="multipart/form-data; charset=utf-8" />--%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户添加</title>
<%@ include file="/static/base/common.jspf"%>
</head>
<body>
	<div class="body_main">
		<form class="layui-form layui-form-pane" action="${ctx}/user/save" method="post" enctype="multipart/form-data">
			<div class="layui-form-item">
				<label class="layui-form-label">昵称</label>
				<div class="layui-input-block">
					<input type="text" name="nickname" autocomplete="off"
						placeholder="请输入昵称" class="layui-input">
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">用户名</label>
				<div class="layui-input-block">
					<input type="text" name="userName" autocomplete="off"
						placeholder="请输入用户名" class="layui-input">
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">密码</label>
				<div class="layui-input-block">
					<input type="text" name="password" autocomplete="off"
						placeholder="请输入密码" class="layui-input">
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">电话</label>
				<div class="layui-input-block">
					<input type="text" id="tel" name="tel" lay-verify="required" autocomplete="off"
						placeholder="请输入电话" class="layui-input"
<%--						   onblur="checkPhone()"--%>
					>
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">性别</label>
				<div class="layui-input-block">
					<select name="sex" lay-verify="required">
						<option value=""></option>
						<option value="1">男</option>
						<option value="-1">女</option>
					</select>
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">邮箱</label>
				<div class="layui-input-block">
					<input type="text" name="email" autocomplete="off"
						placeholder="请输入邮箱" class="layui-input">
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">头像</label>
				<div class="layui-input-block">
					<input type="file" name="upload" autocomplete="off" id="upload"
						placeholder="请输入头像" class="layui-input">
					<input type="text" id="userImg" name="userImg">
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">状态</label>
				<div class="layui-input-block">
					<input type="checkbox" checked="" name="status" lay-skin="switch"
						lay-filter="switchTest" lay-text="可用|禁用">
				</div>
			</div>
			<div class="layui-form-item">
				<div class="layui-input-block">
					<button class="layui-btn" lay-submit="" lay-filter="demo1">立即提交</button>
					<button type="reset" class="layui-btn layui-btn-primary">重置</button>
				</div>
			</div>
		</form>
	</div>
</body>
<script type="text/javascript">
	// function checkPhone() {
	// 	let phone=$("#tel").val();
	// 	console.log("hhh");
	// 	console.log(phone)
	// 	let regx=/^1[3456789]\d{9}$/g;
	// 	if (regx.test(phone)){
	// 		return true
	// 	}else {
	// 		layer.msg("手机号格式错误",{icon:2,time:2000})
	// 		return  false;
	// 	}
	//
	// }

</script>

<script>
	layui.use(['form','layer','upload'], function() {
		var form = layui.form;
		var upload=layui.upload;


		//执行实例
		var uploadInst = upload.render({
			elem: '#upload' //绑定元素
			,url: '${ctx}/user/test' //上传接口

			,done: function(res){
				console.log(res);
				//上传完毕回调
				console.log(res.data.userImg);
				$("#userImg").attr("value",res.data.userImg)

			}
			,error: function(){
				//请求异常回调
			}
		});


		//通用弹出层表单提交方法
		form.on('submit(demo1)', function(data){
			// if (!checkPhone()){
			// 	return false;
			// }

			console.log(data.field);
			// data.field.upload = "555";
					// $("#upload")[0].files[0];
			 // console.log(data.field);

			$.post($('form').attr("action"),data.field, function (e){
				console.log(e)
				if (e.result==true) {
					parent.closeLayer(e.msg);
				}else {
					layer.msg('操作失败：' + e.msg, {icon: 2, time: 2000});
				}
			})
			return false;
		})

	});
</script>









</html>
