package com.test.toy.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.test.toy.user.model.UserDAO;
import com.test.toy.user.model.UserLogDTO;

public class LogFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		//방문 기록 추가
		HttpServletRequest req = (HttpServletRequest)request;
		HttpSession session = req.getSession();
		
		String id = "";
		String url = "";
		
		if (session.getAttribute("id") != null) {
			
			id = session.getAttribute("id").toString();
			url = req.getRequestURI();
			
			UserDAO dao = new UserDAO();
			
			UserLogDTO dto = new UserLogDTO();
			dto.setId(id);
			dto.setUrl(url);
			
			dao.addLog(dto);
		}
		
		chain.doFilter(request, response);		
	}
	
}










