package com.test.toy.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class AuthFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		//권한 체크
		HttpServletRequest req = (HttpServletRequest)request;
		HttpSession session = req.getSession();
		
		if (session.getAttribute("id") == null) {
			//System.out.println("익명 사용자");
			
			//req.getRequestURI()
			//- /toy/board/list.do
			
			if (req.getRequestURI().endsWith("add.do")
				|| req.getRequestURI().endsWith("edit.do")
				|| req.getRequestURI().endsWith("del.do")
				|| req.getRequestURI().endsWith("info.do")) {
				
				response.getWriter().print("<html><meta charset='UTF-8'><script>alert('not allowed');history.back();</script></html>");
				response.getWriter().close();
				
			}
			
		} else {
			//System.out.println("인증 사용자");
		}
		
		chain.doFilter(req, response);		
		
	}

	
	
}




