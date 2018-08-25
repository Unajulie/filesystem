<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>登录页面</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script src="static/jquery-3.3.1.min.js" ></script>
	<script src="static/jquery.validate.min.js"></script>
	<script>
		$(function(){
			$("#loginForm").validate({
				rules:{
					username:"required",
					passwd:"required",
				},
				messages:{
					username:{
						required:"请输入用户名",
					},
					passwd:{
						required:"请输入密码",
					},
				}
			});
		});
	</script>
	<script>
		 if("${user.username}"=="admin"&&"${user.passwd}"=="admin"){
			 window.location.href="view.jhtml";
		 } ;
	</script>
  </head>
  
  <body style="padding: 0px;margin: 0px">
  		<div id="container" style="width: 100%;height: 100%;">
  			<div style="margin: 0px auto;">
	  			<form id="loginForm" method="post"  action="view.jhtml">
	  				<table border="1px">
		    			<tr><td>用户名:</td><td><input name="username"></td></tr>
		    			<tr><td>密码:</td><td><input name="passwd"type="password"></td></tr>
		    			<tr><td colspan="2"><input value="登录"  type="submit"></td></tr>
	    			</table>
	    		</form>
    		</div>
  		</div>
    	
  </body>
</html>
