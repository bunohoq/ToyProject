package com.test.toy.user;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.test.toy.user.model.UserDAO;

@WebServlet(value = "/user/unregister.do")
public class Unregister extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//Unregister.java
		

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/user/unregister.jsp");
		dispatcher.forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		//회원 탈퇴
		//1. 개인 정보 파기
		//2. ing > 0
		
		HttpSession session = req.getSession();
		String id = session.getAttribute("id").toString();
		
		UserDAO dao = new UserDAO();
		
		if (dao.unregister(id) > 0) {
			
			session.invalidate(); //강제 로그아웃
			resp.sendRedirect("/toy/index.do");
			
		} else {
			resp.getWriter().print("<html><meta charset='UTF-8'><script>alert('failed');history.back();</script></html>");
			resp.getWriter().close();
		}
		
	}

}











