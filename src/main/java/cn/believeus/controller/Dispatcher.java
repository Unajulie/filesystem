package cn.believeus.controller;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class Dispatcher extends HttpServlet {
	private static final Logger LOGGER=Logger.getLogger(Dispatcher.class);
	private static final long serialVersionUID = -4757514106369688740L;
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			//  /del.jhtml
			String uri = req.getRequestURI();
			if("/".equals(uri)){
				uri="/index.jhtml";
			}
			LOGGER.trace(uri);
			uri=uri.replaceAll("\\.jhtml", "").replace("/", "");
			//cd.believeus.controller.IndexController
			String packageName=Dispatcher.class.getPackage().getName();
			String clazzname=packageName+"."+uri.substring(0, 1).toUpperCase() + uri.substring(1)+"Controller";
			LOGGER.trace(clazzname);
			Class<?> clazz = Class.forName(clazzname);
			//反射不能调用protected的方法clazz.getMethod不能调用被保护或私有的方法
			///Method method = clazz.getMethod("doPost", new Class[]{HttpServletRequest.class,HttpServletResponse.class});
			
			//getDeclaredMethod方法可以调用私有被保护的方法
			Method method =clazz.getDeclaredMethod("doPost", new Class[]{HttpServletRequest.class,HttpServletResponse.class});
			method.setAccessible(true);//强制设置可以访问
			//创建cd.believeus.controller.IndexController对象
			Object obj = clazz.newInstance();
			method.invoke(obj, new Object[]{req,resp});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		//http://www.iciba.com/dispatcher( url) /dispatcher (uri) www.iciba.com (host)
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
}
