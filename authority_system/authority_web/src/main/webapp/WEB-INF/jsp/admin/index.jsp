<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<title>后台管理平台</title>
<%@ include file="/static/base/common.jspf" %>
<script type="text/javascript">
	$(function (){
		$('.layui-nav-child>dd>a').on('click', function(){
			var url = $(this).attr("data-url");
			$('.layui-body>div').show();
			$('.layui-body>div').load(url);
		})
	})
</script>
</head>
<body class="layui-layout-body">
	<div class="layui-layout layui-layout-admin">
		<div class="layui-header">
			<div class="layui-logo" style="font-weight: bold; font-size: 20px;">后台管理平台</div>
			<ul class="layui-nav layui-layout-right">
				<li class="layui-nav-item"><a href="javascript:;"> <img
						src="http://t.cn/RCzsdCq" class="layui-nav-img">
					<label>${sessionScope.user.nickname}</label>
				</a>
				</li>
				<li class="layui-nav-item"><a href="${pageContext.request.contextPath}/logout">注销</a></li>
			</ul>
		</div>

		<div class="layui-side layui-bg-black">
			<div class="layui-side-scroll">
				<!-- 左侧导航区域（可配合layui已有的垂直导航） -->
				<ul class="layui-nav layui-nav-tree" lay-filter="test">
						<li class="layui-nav-item layui-nav-itemed"><a class=""
							href="javascript:;"><span class='iconfont icon-setup'></span>&nbsp;&nbsp;系统管理</a>
							<dl class="layui-nav-child">
									<dd>
<%--										能以模板化的方式简、高效的添加网页内容，能快速1找到这个位置--%>
										<a href="javascript:;" style="padding-left: 40px;" data-url="${ctx}/user/toListPage">
										<span class='iconfont icon-people'></span>&nbsp;&nbsp;用户管理</a>
									</dd>
								</dl>
							<dl class="layui-nav-child">
								<dd>
									<a href="javascript:;" style="padding-left: 40px;" data-url="${ctx}/role/tolistPage">
										<span class='iconfont icon-group_fill'></span>&nbsp;&nbsp;角色管理</a>
								</dd>
							</dl>
							<dl class="layui-nav-child">
								<dd>
									<a href="javascript:;" style="padding-left: 40px;" data-url="${ctx}/menu/tolistPage">
										<span class='iconfont icon-createtask'></span>&nbsp;&nbsp;菜单管理</a>
								</dd>
							</dl>
						</li>
				</ul>
			</div>
		</div>

		<div class="layui-body">
			<!-- 内容主体区域 -->
			<div style="padding: 15px; display: none"></div>
		</div>

		<div class="layui-footer">
			<!-- 底部固定区域 -->
			© myhopu.com - Copyright © 2020 Adobe Systems Incorporated. All rights reserved.
		</div>
	</div>
	<script>
		layui.use('element', function() {
			var element = layui.element;
		});
	</script>
</body>
</html>
