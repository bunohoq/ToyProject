package com.test.toy.user;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

@WebServlet(value = "/user/delmail.do")
public class DelMail extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//DelMail.java
		
		req.getSession().removeAttribute("validNumber");
		
		resp.setContentType("application/json");
		
		JSONObject obj = new JSONObject();
		obj.put("result", 1);
		
		resp.getWriter().print(obj.toString());
		resp.getWriter().close();
		
	}

}









