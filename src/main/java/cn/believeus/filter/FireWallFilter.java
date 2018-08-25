package cn.believeus.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cn.believeus.entity.Tuser;

public class FireWallFilter implements Filter {
private static final Logger LOGGER=Logger.getLogger(FireWallFilter.class);
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// 转型
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String refer = req.getHeader("Referer");
		// 设置防火墙，不能从 浏览器直接输入我们其他页面地址
		// 只能完成从主页一个入口进入后内部跳转
		String uri = req.getRequestURI();
		LOGGER.trace(uri);
		// 判断是否是输入的主页地址
		// http:://localhost:8080/add.xxx
		if (refer == null) {
			// 是首页
			if (uri.equals("/")) {
				chain.doFilter(request, response);// 放行filter
			} else {
				resp.sendRedirect("/");// 打回主页 重定向找谁？！！web.xml
			}
		} else {
				chain.doFilter(request, response);
			
		}
	}

	public void destroy() {

	}

}
