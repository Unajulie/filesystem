<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE>
<html>
<head>
<base href="<%=basePath%>">

<title>编程大典</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script>
		 if("${user.username}"!="admin"&&"${user.passwd}"!="admin"){
			 window.location.href="/";
		 } ;
	</script>
<script>
//判断当前浏览器是否为IE浏览器，若是就返回-1，则跳转页面到该浏览器界面表示不支持
var browser=navigator.userAgent;
if(browser.indexOf("compatible")!=-1){
	window.location.href="inhell.jhtml";//重定向跳转页面网址或设置路径
	
}
</script>
<script src="static/jquery-3.3.1.min.js"></script>
<script>
//待所有页面执行完后再去加载
$(function(){
	//按鼠标右键，让系统自带提示窗口消失
		$("body").contextmenu(function(){
			return false;
		});	
	//这里有一个js冒泡事件传递 比如父div中有一个子div 
	//如果同时给父和子绑定click，那么当我们单击子div的时候
	//先执行子div的click事件然后在执行父div(body)的click事件
	    $("body").click(function(e){
	    	$("div[name=menu]").remove();
	    });
	//隐藏浏览器滚动条
	document.body.parentNode.style.overflowY="hidden";
	//获得右边编辑文本区的高度
	$("#right-data").css("height",$(document).height());
	//获得左边目录栏高度
	//双击编辑
	$("div[id=box]").dblclick(function(){
		$(this).attr("contenteditable",true).css("background-color","white").css("color","#1b3749").css("border","1px solid grey");
	});
	
	//添加键盘按压enter键时，获取值13，并还原按压之前的效果
	$("div[id=box]").keydown(function(e){
		var key=e.keyCode;
		console.info(key);
		switch(key){
		case 13:
		$(this).attr("contenteditable",false).css("background-color","#1b3749").css("border","none").css("color","white").blur();
		break;
		}
	});
	
	//给第一节课添加双击编辑跟enter退出编辑模式
	/* $("div[id=itembox]").dblclick(function(){
		$(this).attr("contenteditable",true).css("background-color","white").css("color","#1b3749").css("border","1px solid grey");
	}); */
	//给新添加的元素添加双击编辑跟，enter结束编辑事件
	$("body").on("dblclick","div[id=itembox]",function(){
		$(this).attr("contenteditable",true).css("background-color","white").css("color","#1b3749").css("border","1px solid grey");
	});
	$("body").on("keydown","div[id=itembox]",function(e){
		var key=e.keyCode;
		switch(key){
		case 13:
			$(this).attr("contenteditable",false);
			var data={};//定义一个jason格式对象
			var mid=$(this).attr("mid");
			//如果我们能获取到id
			if(mid!=undefined){
				data.mid=mid;
			}
			data.title=$(this).text();
			data.locid=$(this).attr("locid");
			data.pid=$(this).attr("pid");
			console.info(data);
			//ajax把数据发送到sddmenu.jhtml执行结束就返回success
			//重定向post方法传递数据(回调函数并不会刷新该页面)
			var oThis=$(this);
			//{id:"1",title:"24324",locid:"0"}
			//并没有把pid发送过来
			$.post("addMenu.jhtml",data,function(id){
					oThis.css("background-color","#1b3749").css("border","none").css("color","white").blur();
					oThis .attr("mid",id);
			});
		break;
		}
	});
	//给最大目录添加双击编辑跟enter退出编辑模式
	//给新添加的元素添加双击编辑跟，enter结束编辑事件
	$("body").on("dblclick","div[id=box-sub]",function(){
		$(this).attr("contenteditable",true).css("background-color","white").css("color","#1b3749").css("border","1px solid grey");
	});
	$("body").on("keydown","div[id=box-sub]",function(e){
		var key=e.keyCode;
		switch(key){
		case 13:
			$(this).attr("contenteditable",false).css("background-color","#1b3749").css("border","none").css("color","white").blur();
		break;
		}
	});
	
	//监听itembox的鼠标右键事件
	$("div[id=itembox]").contextmenu(function(e){
		$("div[name=menu]").remove();//把原来的右键菜单删除，再点击就不会重复出现多个提示栏
		var div="<div name='menu' style='display:none;box-sizing: border-box;position: absolute;width: 80px;border-radius: 5px;background-color: white;font-weight: bold;border:1px solid #2d3e50;font-size:12px;line-height:25px;color:#2d3e50;'>"+
			"<div style='text-align:center;cursor:pointer;'><span name='itemAddChild'>添加子项</span></div>"+
			"<div style='text-align:center;cursor:pointer;'><span name='itemdel'>删除该项</span></div>"+
	  	   "</div>";
		//把提示框插入到页面中
		$(div).appendTo("body");
		var x=e.pageX;//右键点击获得x跟y点坐标
		var y=e.pageY;
		$("div[name=menu]").css("left",x).css("top",y).show();
		$("div[name=menu]").children("div").mouseover(function(){
			$(this).css("background-color","#ccc");
		}).mouseout(function(){
			$(this).css("background-color","white");
		});
		//获得父类(第一节课)的元素
		var oThis=$(this);
		$("div[name=menu]").find("span").click(function(e){
			var option=$(this).attr("name");
			switch(option){
				case "itemAddChild":
					//获得最后一个兄弟节点的第一个子元素
					var locid=oThis.siblings().last().children("div:last-child").attr("locid");
					if(locid==undefined){
						locid=0;
					}else{
						locid=(parseInt(locid)+1);
					}
					var id=oThis.attr("mid");
					
		var div="<div style='width:100%' locid="+locid+" pid="+id+">"+
        "<div style='float:left;width:70px;height:22px;border:0px solid grey'></div>"+
       "<div locid="+locid+"  pid="+id+" id='mname' style='float:left;width:60%;border:1px solid grey;color:white;font-weight:bold;font-size:14px;text-overflow:ellipsis;color:#1b3749;overflow:hidden;white-space:nowrap;cursor:pointer;background-color:white;'contenteditable='true'>请输入...</div>"+
       "<div style='clear:both;height:5px;'></div>"+
       "</div>";
              	 oThis.siblings().last().append(div);//获得最后一个兄弟节点
					break;
				case "itemdel":
					$.post("del.jhtml",{mid:oThis.attr("mid")},function(msg){
						oThis.parent().remove();
					});
					break;
			}
		});
		
	});
	//click keydown只对已存在的便签有效，对新添加的元素无效，故要在前面标明html,或是body实体
	//对新添加的元素一定要加 on 才起作用
	//给所有name=mname的标签下添加keydown事件（也就是enter键）
	$("body").on("dblclick","div[id=mname]",function(e){
		$(this).attr("contenteditable",true).css("background-color","white").css("color","#1b3749").css("border","1px solid grey");
	});
	$("body").on("keydown","div[id=mname]",function(e){
		var key=e.keyCode;
		switch(key){
		case 13:
			$(this).attr("contenteditable", false);
			var data={};//定义一个json格式对象
			var mid=$(this).attr("mid");
			var pid=$(this).attr("pid");
			if(mid!=undefined){
				data.id=$(this).attr("mid");
			}
			var locid=$(this).attr("locid");
			var title=$(this).text();
		    data.title=title;
		    data.pid=pid;
		    data.locid=locid;
		    var oThis=$(this);
		    $.post("addMenu.jhtml",data,function(id){
		    	oThis.attr("pid",pid);
		    	oThis.attr("mid",id);
 		    });
		    console.info(pid);
			$(this).css("background-color", "#1b3749").css("border","none").css("color", "white").blur();
		break;
		}
	});
	//给子节点添加右键事件
	$("body").on("contextmenu","div[id=mname]",function(e){
		$("div[name=menu]").remove();//把原来的右键菜单删除，再点击就不会重复出现多个提示栏
		var div="<div name='menu' style='display:none;box-sizing: border-box;position: absolute;width: 80px;border-radius: 5px;background-color: white;font-weight: bold;border:1px solid #2d3e50;font-size:12px;line-height:25px;color:#2d3e50;'>";
		if($(this).attr("locid")!=0){
			div+="<div style='text-align:center;cursor:pointer;'><span name='itemup'>向上移动</span></div>";
		}
			div+="<div style='text-align:center;cursor:pointer;'><span name='itemdel'>删除该项</span></div></div>";
		//把提示框插入到页面中
		$(div).appendTo("body"); 
		var x=e.pageX;//右键点击获得x跟y点坐标
		var y=e.pageY;
		$("div[name=menu]").css("left",x).css("top",y).show();
		var oThis=$(this);
		//绑定click方法
		$("div[name=menu]").find("span").click(function(){
			var option=$(this).attr("name");
		
	                      switch (option) {
								case "itemup"://实现添加div向上移动效果
									var curlocid=oThis.attr("locid");
									var prevlocid=oThis.parent().prev().find("div[id=mname]").attr("locid");
									oThis.attr("locid",prevlocid);
									oThis.parent().prev().find("div[id=mname]").attr("locid",curlocid);
									oThis.parent().attr("locid",prevlocid);
									oThis.parent().prev().attr("locid",curlocid);
									var div=oThis.parent().clone();//克隆整个oThis,让它向上移动，然后再删除自身整个oThis
									oThis.parent().prev().before(div);
									oThis.parent().remove();
									break;
								case "itemdel":
									oThis.parent().remove();
									break;
								}
							});
						});
	$("body").on("contextmenu","div[id=mname]",function(e){
		$(this).attr("contenteditable",false).css("background-color","#1b3749").css("border","none").css("color","white").blur();
	});
	$("div[id=box-sub]").click(function(){
		if($(this).attr("click")=="false"){
		$(this).next().slideUp();
		$(this).attr("click","true");			
		}else{
			$(this).next().slideDown();
			$(this).attr("click","false");
		}
	});
	//点击显示子类
	var ajax=true;
	$("body").on("click","div[id=itembox]",function(){
		console.info($(this));
		var id=$(this).attr("mid");
		var data={};
		data.id=id;
		var oThis=$(this);
		//第一次点击就发送ajax请求
		if(ajax==true){
			$.post("getMenu.jhtml",data,function(msg){
				console.info(msg);
				 msg = $.parseJSON(msg);//才能在js中遍历
				 for(var i=0;i<msg.length;i++){//遍历里面的json数组
						var div="<div name='subChild'  mid='"+msg[i].id+"' pid='"+msg[i].oid+"' locid='"+msg[i].locid+"' style='text-overflow:ellipsis;overflow:hidden;white-space:nowrap;background-color: #1b3749;margin-top:5px;margin-left:20px;font-size:15px;color: #ccc;width:75%;cursor:pointer;margin-left:80px;' contenteditable='false'>"+msg[i].title+"</div>";
						//在js这里遍历json数据，把他插入div中
						oThis.next().next().append(div);
				 }
			});
			ajax=false;
		}else if(ajax==false){
			if($(this).attr("click")=="false"){
				$(this).next().next().slideUp();
				$(this).attr("click","true");
			}else{
				$(this).next().next().slideDown();
				$(this).attr("click","false");
			}
		}
		
	});
	//只要是新添加的对象都需要on绑定
	$("body").on("contextmenu","div[id=box-sub]",function(e){
		$("div[name=menu]").remove();//把原来的右键菜单删除
		var menu="<div name='menu' style='display:none;box-sizing: border-box;position: absolute;width: 80px;border-radius: 5px;background-color: white;font-weight: bold;border:1px solid #2d3e50;font-size:12px;line-height:25px;color:#2d3e50;'>"+
			"<div style='text-align:center;cursor:pointer;'><span name='itemAddChild'>添加子项</span></div>"+
			"<div style='text-align:center;cursor:pointer;'><span name='itemdel'>删除该项</span></div>"+
	  	   "</div>";
		//把提示框插入到页面中
		$(menu).appendTo("body");
		var x=e.pageX;//右键点击获得x跟y点坐标
		var y=e.pageY;
		$("div[name=menu]").css("left",x).css("top",y).show();
		
		var oThis=$(this);
		$("div[name=menu]").find("span").click(function(e){
			var option=$(this).attr("name");
			switch(option){
			case "itemAddChild":
				var locid=oThis.next().children("div:last-child").attr("locid");
				if(locid==undefined){
					locid=0;
				}else{
					locid=(window.parseInt(locid)+1);
				}
				
				var div="<div locid="+locid+">"+
			 "<div style='clear:both;height:5px;width:100%;'></div>"+
				"<div style='width:100%;border:0px solid grey;'>"+
					"<div style='float:left;width:50px;height:22px;border:0px solid grey'></div>"+
					"<div   locid="+locid+" click='false' id='itembox' style='float:left;width:60%;border:0px solid grey;color:#2d3e50;background-color:white;font-weight:bold;font-size:16px;text-overflow:ellipsis;overflow:hidden;white-space:nowrap;cursor:pointer;' contenteditable='true'>请输入...</div>"+
					"<div style='clear:both;height:5px;'></div>"+
					"<div id='ibox'></div>"+
			   "</div>"+
			"</div>";
			oThis.next().append(div);
				break;
			}
		}); 
	});
	$("body").on("contextmenu","div[id=box]",function(e){
		$("div[name=menu]").remove();//把原来的右键菜单删除
		var menu="<div name='menu' style='display:none;box-sizing: border-box;position: absolute;width: 80px;border-radius: 5px;background-color: white;font-weight: bold;border:1px solid #2d3e50;font-size:12px;line-height:25px;color:#2d3e50;'>"+
			"<div style='text-align:center;cursor:pointer;'><span name='itemAddChild'>添加子项</span></div>"+
			"<div style='text-align:center;cursor:pointer;'><span name='itemdel'>删除该项</span></div>"+
	  	   "</div>";
	  	//把提示框插入到页面中
			$(menu).appendTo("body");
			var x=e.pageX;//右键点击获得x跟y点坐标
			var y=e.pageY;
			$("div[name=menu]").css("left",x).css("top",y).show();
			
			var oThis=$(this);
			$("div[name=menu]").find("span").click(function(e){
				var option=$(this).attr("name");
				switch(option){
				case "itemAddChild":
					var locid=oThis.next().children("div:last-child").find("div[id=box-sub]").attr("locid");
					if(locid==undefined){
						locid=0;
					}else{
						locid=(window.parseInt(locid)+1);
					}
				
					var div="<div locid="+locid+">"+
					        "<div style='width:100%'>"+
					            "<div style='clear:both;height:10px;width:100%;'></div>"+
					        	"<div style='float:left;width:30px;height:22px;border:0px solid grey;'></div>"+
					         	"<div click='false' locid="+locid+" id='box-sub' style='float:left;width:60%;height:22px;color:#2d3e50;background-color:white;font-weight:bold;font-size:18px;border:0px solid grey;text-overflow:ellipsis;overflow:hidden;white-space:nowrap;cursor:pointer;' contenteditable='true'>请输入……</div>"+
								"<div style='clear:both;'></div>"+
						    "</div>"+
					   "</div>";
					oThis.next().append(div);
				}
			});
		});
	 });
</script>
</head>
<body style="margin: 0px;padding: 0px;">
	<div id="container" style="width:100% height:100%;">
	  <div id="left-menu" style="width:60%;height:100%;float:left;background-color:#1b3749;">
	   		<div id="title" style="line-height:100px;width:100%;height:100px;background-color:#1b3749;border-bottom:0px solid grey;color:white;text-align:center;font-size:60px;font-weight:bold;text-overflow:ellipsis;overflow:hidden;white-space:nowrap;cursor:pointer;">编程大典</div>
	   		<div id="menu" style="width:100%;height:600px;border-bottom:0px solid grey;border-left:0px solid grey;overflow-x:hidden;">
			       <div id="box" style="width:100%;height:30px;color:white;font-weight:bold;font-size:18px;text-overflow:ellipsis;overflow:hidden;white-space:nowrap;cursor:pointer;"contenteditable="false">目录索引结构树</div>
			     	<div>
					      <div style="width:100%">
						        <div style="clear:both;height:10px;width:100%;"></div>
						        <div style="float:left;width:30px;height:22px;border:0px solid grey;"></div>
						         <div  click="false" locid="0"  id="box-sub" style="float: left; width: 60%; height: 22px; color: white; background-color: rgb(27, 55, 73); font-weight: bold; font-size: 18px; border: medium none; text-overflow: ellipsis; overflow: hidden; white-space: nowrap; cursor: pointer;" contenteditable="false">请输入……</div>
						           <div style="clear:both;">
						               <c:forEach var="menu" items="${menubox }">
						                 <div locid="${menu.locid }"  id="${menu.id }"> 
						                             <div style="clear:both;height:5px;width:100%;"></div>
						                             <div style="width:100%;border:0px solid grey;"><div style="float:left;width:50px;height:22px;border:0px solid grey"></div>
						                                      <div   mid="${menu.id }" locid="${menu.locid }" click="false"  id="itembox" style="float: left; width: 60%; border: medium none; color: white; background-color:#1b3749; font-weight: bold; font-size: 16px;  text-overflow: ellipsis; overflow: hidden; white-space: nowrap; cursor: pointer;height: 20px;" contenteditable="false">${menu.title}</div>
						                                       <div style="clear:both;height:5px;"></div>
						                                       <div name="ibox"></div>
						                            </div>
						                  </div>
						              </c:forEach>
						            </div>
					    	</div>
			 		</div>
			</div>
	   		<div id="time" style="width:100% ;height:30px;color:white;text-align:center;line-height:30px;font-weight:bold;text-overflow:ellipsis;overflow:hidden;white-space:nowrap;cursor:pointer;border-top: 1px solid grey;">微信：13530987654</div>
	  </div>
	  <div id="handle" style="width:0.2%;height:100%;float:left;background-color:#CCC;cursor:e-resize;"></div>
	  <div id="right-data" style="width:39.8%;height:100%;float:left;">
		 <div id="toolbar" style="width:100%;height:30px;"></div>
		 <div id="editor" style="width:100%;height:0px;border:1px solid grey;"></div>
	  </div>
	</div>
</body>
<script src="<%=basePath%>static/editor/wangEditor.min.js"></script>
    <script type="text/javascript">
        //创建网页编辑器
        var E = window.wangEditor;
        var editor = new E("#toolbar","#right-data");
        editor.customConfig.menus = [
                                     'head',  // 标题
                                     'bold',  // 粗体
                                     'fontSize',  // 字号
                                     'fontName',  // 字体
                                     'italic',  // 斜体
                                     'underline',  // 下划线
                                     'strikeThrough',  // 删除线
                                     'foreColor',  // 文字颜色
                                     'backColor',  // 背景颜色
                                     'link',  // 插入链接
                                     'list',  // 列表
                                     'justify',  // 对齐方式
                                     'quote',  // 引用
                                     'image',  // 插入图片
                                     'table',  // 表格
                                     'code',  // 插入代码
                                 ]
        editor.create();
        editor.$textElem.attr('contenteditable',false);//默认关闭编辑器
    </script>
<script type="text/javascript">
    //拖动效果
	//获得客户端的总宽度
	//获取menu的宽度=menu60%*总宽度
	var menu = document.getElementById("left-menu");
	var rdata = document.getElementById("right-data");
	var handle = document.getElementById("handle");
	
	handle.onmousedown = function(e) {
		//设置监听鼠标移动事件
	document.onmousemove = function(e) {
       $("#left-menu").css("width",e.clientX);
       $("#right-data").css("width",$(document).width()-$("#left-menu").outerWidth()-$("#handle").outerWidth());
			
	};
	document.onmouseup=function(e){
		document.onmousemove=null;
	};
	};
	
	//获取总高度
	//var clientHeight=document.body.clientHeight;
	//获取主标题的高度
	//var titleH=parseInt(document.getElementById("title").style.height);
	//获取联系方式栏的高度
	//var contactH=parseInt(document.getElementById("contact").style.height);
	//获取menu的高度值
	//document.getElementById("menu").style.height=(clientHeight-titleH-contactH)+"px";
</script>
<script>
	<%-- window.setInterval(function(){
		  $.post("<%=basePath%>time.jhtml",function(msg){
        	 $("#time").text(msg);
          }); 
	},1000); --%>
</script>
</html>
