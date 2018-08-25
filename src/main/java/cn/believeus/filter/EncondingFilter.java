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
		//�����������������ַ�������filterһ��ʼ������utf8���룬����servlet����
			request.setCharacterEncoding("utf-8");
			chain.doFilter(request, response); // http://localhost:8080/
	}

	public void destroy() {

	}

}
