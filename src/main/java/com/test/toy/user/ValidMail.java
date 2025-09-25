package com.test.toy.user;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

@WebServlet(value = "/user/validmail.do")
public class ValidMail extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//ValidMail.java
		//1. 데이터 가져오기(validNumber)
		//2. 세션값과 비교
		//3. 마무리
		
		String validNumber = req.getParameter("validNumber");
		
		int result = 0;
		
		if (req.getSession().getAttribute("validNumber").toString().equals(validNumber)) {
			result = 1;
		}
		
		resp.setContentType("application/json");
		
		JSONObject obj = new JSONObject();
		obj.put("result", result);
		
		resp.getWriter().print(obj.toString());
		resp.getWriter().close();		
		
	}

}







