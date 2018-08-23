package com.util.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class CharacterEncodingFilter implements Filter {
	private String charset; 

	@Override
	public void init(FilterConfig config) throws ServletException {
		charset = config.getInitParameter("charset");
	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		
		if(req instanceof HttpServletRequest) {
			HttpServletRequest request = (HttpServletRequest)req;
			String uri = request.getRequestURI();
			
			if(request.getMethod().equalsIgnoreCase("POST")) {
				if(uri.indexOf("ajax.do") != -1)
					request.setCharacterEncoding("utf-8");
				else
					request.setCharacterEncoding(charset);
			}
		}
		
		// ���� ü��
		chain.doFilter(req, resp);
		
	}
	@Override
	public void destroy() {
	}

}
