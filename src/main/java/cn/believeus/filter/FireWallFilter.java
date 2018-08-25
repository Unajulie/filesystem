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
		// ת��
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String refer = req.getHeader("Referer");
		// ���÷���ǽ�����ܴ� �����ֱ��������������ҳ���ַ
		// ֻ����ɴ���ҳһ����ڽ�����ڲ���ת
		String uri = req.getRequestURI();
		LOGGER.trace(uri);
		// �ж��Ƿ����������ҳ��ַ
		// http:://localhost:8080/add.xxx
		if (refer == null) {
			// ����ҳ
			if (uri.equals("/")) {
				chain.doFilter(request, response);// ����filter
			} else {
				resp.sendRedirect("/");// �����ҳ �ض�����˭������web.xml
			}
		} else {
				chain.doFilter(request, response);
			
		}
	}

	public void destroy() {

	}

}
