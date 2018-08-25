package cn.believeus.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class EncondingFilter implements Filter {

	public void init(FilterConfig filterConfig) throws ServletException {

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		//从浏览器传输过来的字符串先由filter一开始拦截由utf8编码，再由servlet拦截
			request.setCharacterEncoding("utf-8");
			chain.doFilter(request, response); // http://localhost:8080/
	}

	public void destroy() {

	}

}
