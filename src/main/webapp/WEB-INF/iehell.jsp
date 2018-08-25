<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <script src="static/jquery-3.3.1.min.js"></script>
    <script>
    	$(function(){
    		$("div[id=p]").click(function(e){
        		alert("p");
        	});
        	$("div[id=c]").click(function(e){
        		alert("c");
        	});
    	});
    	
    </script>
    <title>编程攻略</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body style="padding:0px;margin:0px;">
  <div style="width:100%;height:100%;color:white;font-weight:bold;background-color:#2d3e50;text-align:center;line-height:500px;">
  您当前浏览器是<span style="font-size:50px;">IE</span>浏览器，本站不支持IE浏览器！
  建议使用<span style="font-size:60px;color:orange;">Firefox</span>或<span style="font-size:60px;color:red">Chrome</span>浏览器！
  </div>
    <div id="p" style="border: 1px solid grey;width: 200px;height: 200px">
    	<div id="c" style="border: 1px solid grey;width:100px;height: 100px;ss"></div>
    </div>
  </body>
</html>
