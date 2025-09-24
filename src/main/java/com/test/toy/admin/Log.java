package com.test.toy.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.test.toy.admin.model.LogDAO;
import com.test.toy.user.model.UserDTO;

@WebServlet(value = "/admin/log.do")
public class Log extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//Log.java
		LogDAO dao = new LogDAO();
		
		List<UserDTO> list = dao.listUser();
		
		req.setAttribute("list", list);

		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/admin/log.jsp");
		dispatcher.forward(req, resp);
	}

}










