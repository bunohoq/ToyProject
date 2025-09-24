package com.test.toy.user;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(value = "/user/logout.do")
public class Logout extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//Logout.java
		//세션 초기화
		//- 인증 티켓 제거
		HttpSession session = req.getSession();
		
		//session.removeAttribute("id");
		//session.removeAttribute("name");
		//session.removeAttribute("lv");
		
		session.invalidate();
		
		resp.sendRedirect("/toy/index.do");		
	}

}






